/*******************************************************************************
 *  Copyright (c) 2011 University Of Moratuwa
 *                                                                      
 * All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html                            
 *                                                                      
 * Contributors:                                                        
 *    Isuru Udana - UI Integration in the Workbench
 *******************************************************************************/
package org.eclipse.ecf.salvo.ui.internal.wizards;

import java.util.ArrayList;

import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.salvo.ui.external.provider.HookedNewsgroupProvider;
import org.eclipse.ecf.salvo.ui.external.provider.IMessageSourceProvider;
import org.eclipse.ecf.salvo.ui.tools.ImageUtils;
import org.eclipse.ecf.salvo.ui.tools.PreferencesUtil;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class SelectNewsgroupWizardPage extends WizardPage {

	private Composite container;
	private List messageSourceList;
	private Text searchBar;
	private ArrayList<IMessageSource> subscribedMessageSources;
	private Button btnCheckPickSubscribed;
	private Combo cboSuggestedNewgroups;
	private IMessageSourceProvider[] hookedMessageSourceProviders;
	private Label lblSuggested;

	public SelectNewsgroupWizardPage() {
		super("Select Newsgroup");
		setTitle("Select Newsgroup");
		setDescription("Select the Newsgroup you want to ask the question");
		initHookedNewsgroupsProviders();
		fetchSubscribedNewsgroups();
		setImageDescriptor(ImageUtils.getInstance().getImageDescriptor(
				"selectnewsgroup.png"));
	}

	public void createControl(Composite parent) {

		// Container
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		setControl(container);

		if (isHookedMessageSourceAvailable()) {
			// Label Suggested
			{
				lblSuggested = new Label(container, SWT.NULL);
				lblSuggested.setText("Suggested Message Sources");
			}
			// Combobox of suggested newsgroups
			{
				cboSuggestedNewgroups = new Combo(container, SWT.READ_ONLY);
				cboSuggestedNewgroups.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));
				fillCboSuggestedNewsgroups();
				cboSuggestedNewgroups.select(0);
			}
		}

		if (isHookedMessageSourceAvailable() && isSubscribedNewsgroupsAvailable()) {
			// Checkbox
			{
				btnCheckPickSubscribed = new Button(container, SWT.CHECK);
				btnCheckPickSubscribed
						.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								if (btnCheckPickSubscribed.getSelection()) {
									cboSuggestedNewgroups.setEnabled(false);
									lblSuggested.setEnabled(false);
									messageSourceList.setEnabled(true);
									searchBar.setEnabled(true);
								} else {
									cboSuggestedNewgroups.setEnabled(true);
									lblSuggested.setEnabled(true);
									messageSourceList.setEnabled(false);
									searchBar.setEnabled(false);
									searchBar.setText("");
									setPageComplete(true);
								}
							}
						});
				btnCheckPickSubscribed
						.setText("Pick from subscribed newsgroups");
			}
		}

		if (isSubscribedNewsgroupsAvailable()) {
			// Search bar
			{
				searchBar = new Text(container, SWT.BORDER);
				searchBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, false, 1, 1));
				searchBar.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						setFilteredListItems();
						if (messageSourceList.getItemCount() == 0) {
							setPageComplete(false);
						} else {
							setPageComplete(true);
						}
					}
				});
			}
			// Newsgroup List
			{
				messageSourceList = new List(container, SWT.SINGLE | SWT.BORDER
						| SWT.V_SCROLL);
				messageSourceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
						true, true, 1, 1));
				initNewsgroupList();
			}
		}

		if (isHookedMessageSourceAvailable() && isSubscribedNewsgroupsAvailable()) {
			cboSuggestedNewgroups.setEnabled(true);
			btnCheckPickSubscribed.setSelection(false);
			messageSourceList.setEnabled(false);
			searchBar.setEnabled(false);
		}

		// Page completeness
		if (!isHookedMessageSourceAvailable()
				&& !isSubscribedNewsgroupsAvailable()) {
			lblSuggested = new Label(container, SWT.NULL);
			lblSuggested.setText("No News Sources available");
			setPageComplete(false);
		}

	}

	/**
	 * Fill CboSugesstedNewsgroups combo box
	 */
	private void fillCboSuggestedNewsgroups() {
		for (IMessageSourceProvider messageSourceProvider : hookedMessageSourceProviders) {

			String messageSourceName = messageSourceProvider.getNewsgroupName();
			String serverAddress = messageSourceProvider.getServerAddress();
			String description = messageSourceProvider.getNewsgroupDescription();

			cboSuggestedNewgroups.add(messageSourceName + "  (Server: "
					+ serverAddress + ") -  " + description);
		}
	}

	/**
	 * Initialize MessagrSourceList for the first time with all the available
	 * newsgroups
	 */
	private void initNewsgroupList() {

		// Load preferences
		String recentlySelectedNewsgroup = PreferencesUtil.instance()
				.loadPluginSettings("recentSelectedNewsgroup");
		String recentlySelectedServer = PreferencesUtil.instance()
				.loadPluginSettings("recentSelectedServer");

		int selectionIndex = 0;

		for (int i = 0, size = subscribedMessageSources.size(); i < size; i++) {

			String newsgroupName = subscribedMessageSources.get(i)
					.getMessageSourceName();
			String serverAddress = subscribedMessageSources.get(i).getServer()
					.getAddress();

			messageSourceList.add(newsgroupName + "  (Server: " + serverAddress
					+ ")");

			// calculate the recently selected item from the list
			if (newsgroupName.equals(recentlySelectedNewsgroup)
					&& serverAddress.equals(recentlySelectedServer)) {
				selectionIndex = i;
			}

		}
		messageSourceList.select(selectionIndex);
	}

	/**
	 * Fill the newsgroupList according to the filter specified on the search
	 * bar
	 */
	private void setFilteredListItems() {
		messageSourceList.removeAll();

		for (IMessageSource source : subscribedMessageSources) {
			if (matchPattern(source.getMessageSourceName())) {
				String newsgroupName = source.getMessageSourceName();
				String serverAddress = source.getServer().getAddress();

				messageSourceList.add(newsgroupName + "  (Server: " + serverAddress
						+ ")");
			}
		}
		messageSourceList.select(0);
	}

	/**
	 * Match whether the given newsgroup is match with the filter.
	 * 
	 * @param newsgroupName
	 *            Name of the newsgroup
	 * @return whether the newsgroup is match with the filter
	 */
	private boolean matchPattern(String newsgroupName) {

		String searchText = searchBar.getText();
		if (newsgroupName.contains(searchText)) {
			if (newsgroupName.startsWith(searchText)) {
				return true;
			} else if (newsgroupName.contains("." + searchText)) {
				return true;
			} else if (newsgroupName.contains("-" + searchText)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the selected Newsgroup from the list
	 * 
	 * @return selected Source
	 */
	public IMessageSource getSelectedNewsgroup() {

		IMessageSource resultMessageSource = null;

		if (isHookedMessageSourceAvailable() && isSubscribedNewsgroupsAvailable()) {
			if (btnCheckPickSubscribed.getSelection()) {
				resultMessageSource = getSelectedSubscribedMessageSource();
			} else {
				resultMessageSource = getSelectedHookedNewsgroup();
			}
		} else if (isHookedMessageSourceAvailable()) {
			resultMessageSource = getSelectedHookedNewsgroup();
		} else {
			resultMessageSource = getSelectedSubscribedMessageSource();
		}

		return resultMessageSource;
	}

	/**
	 * Get the selected hooked newsgroup
	 */
	// TODO change INewsGroupProvider
	private INewsgroup getSelectedHookedNewsgroup() {
		INewsgroup resultNewsgroup = null;

		IMessageSourceProvider provider = hookedMessageSourceProviders[cboSuggestedNewgroups
				.getSelectionIndex()];
		if (!HookedNewsgroupProvider.instance().isServerSubscribed(provider)) {
			if (provider.initCredentials()) {
				resultNewsgroup = HookedNewsgroupProvider.instance()
						.getNewsgroup(provider);
			}
		} else {
			resultNewsgroup = HookedNewsgroupProvider.instance().getNewsgroup(
					provider);
		}
		return resultNewsgroup;
	}

	/**
	 * Get the selected subscribed newsgroup
	 */
	private IMessageSource getSelectedSubscribedMessageSource() {
		IMessageSource resultNewsgroup = null;

		try {
			String selectedNewsgroupString = messageSourceList.getItem(
					messageSourceList.getSelectionIndex()).replace(")", "");

			String selectedNewsgroup = selectedNewsgroupString.split("\\(")[0]
					.trim();
			String selectedServer = selectedNewsgroupString.split("\\(")[1]
					.replace("Server: ", "").trim();

			for (IMessageSource source : subscribedMessageSources) {
				if (source.getMessageSourceName().equals(selectedNewsgroup)
						&& source.getServer().getAddress()
								.equals(selectedServer)) {
					resultNewsgroup = source;
				}
			}
		} catch (Exception e) {
			// No newsgroups
		}
		return resultNewsgroup;
	}

	/**
	 * Fetch all subscribed newsgroups from the store
	 */
	private void fetchSubscribedNewsgroups() {

		subscribedMessageSources = new ArrayList<IMessageSource>();

		try {
			for (INNTPServer server : NNTPServerStoreFactory.instance()
					.getServerStoreFacade().getFirstStore().getServers()) {

				INewsgroup[] groups = NNTPServerStoreFactory.instance()
						.getServerStoreFacade().getSubscribedNewsgroups(server);

				for (INewsgroup group : groups) {

					if (!subscribedMessageSources.contains(group)) {
						subscribedMessageSources.add(group);
					}
				}

			}
		} catch (NNTPException e) {
			Debug.log(this.getClass(), e);
		}

	}

	/**
	 * initialize hooked newsgroup providers
	 */
	private void initHookedNewsgroupsProviders() {
		hookedMessageSourceProviders = HookedNewsgroupProvider.instance()
				.getProviders();
	}

	/**
	 * check whether subscribed newsgroups available
	 */
	private boolean isSubscribedNewsgroupsAvailable() {
		if (subscribedMessageSources.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * check whether hooked newsgroups available
	 */
	private boolean isHookedMessageSourceAvailable() {
		if (hookedMessageSourceProviders.length == 0) {
			return false;
		}
		return true;
	}

}
