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
package org.eclipse.ecf.salvo.ui.wizards;

import org.eclipse.ecf.channel.IChannelContainerAdapter;
import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.channel.core.ISalvoUtil;
import org.eclipse.ecf.channel.core.SalvoUtil;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.salvo.ui.internal.wizards.ComposeNewMessageWizardPage;
import org.eclipse.ecf.salvo.ui.internal.wizards.SelectNewsgroupWizardPage;
import org.eclipse.ecf.salvo.ui.tools.PreferencesUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * This class is responsible for providing the "Ask A Question" wizard.
 * 
 * @author isuru
 * 
 */

//  class needs to be changed to match with new API
public class AskAQuestionWizard extends Wizard {

	private SelectNewsgroupWizardPage selectNewsgroupWizardPage;
	private ComposeNewMessageWizardPage composeNewArticleWizardPage;
	private IContainer iContainer;
	private IChannelContainerAdapter adaptor;

	public AskAQuestionWizard() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle("Ask a Question");
	}

	@Override
	public void addPages() {
		selectNewsgroupWizardPage = new SelectNewsgroupWizardPage();
		composeNewArticleWizardPage = new ComposeNewMessageWizardPage();
		addPage(selectNewsgroupWizardPage);
		addPage(composeNewArticleWizardPage);

	}

	@Override
	public boolean canFinish() {
		if (composeNewArticleWizardPage.isValuesSet()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean performFinish() {

		IMessageSource group = selectNewsgroupWizardPage.getSelectedNewsgroup();

		if (group != null) {

			// Saving preferences
			PreferencesUtil.instance().savePluginSettings(
					"recentSelectedNewsgroup", group.getMessageSourceName());
			PreferencesUtil.instance().savePluginSettings(
					"recentSelectedServer", group.getServer().getAddress());

			String subject = composeNewArticleWizardPage.getSubject();
			String body = composeNewArticleWizardPage.getBodyText();
			setupContainer();
			
			try {

				// posting article
				adaptor.postNewMessages(new INewsgroup[] { (INewsgroup)group },
						subject, body);

				// Subscribe newsgroup
				if (!group.isSubscribed()
						&& composeNewArticleWizardPage.doSubscribe()) {
					adaptor.subscribeMessageSource((INewsgroup)group);
				}

				MessageDialog.openInformation(
						getShell(),
						"Article Posted",
						"Your question is posted to "
								+ group.getMessageSourceName());

			} catch (NNTPException e) {
				MessageDialog.openError(
						getShell(),
						"Problem posting message",
						"The message could not be posted. \n\r"
								+ e.getMessage());
				Debug.log(this.getClass(), e);
				e.printStackTrace();

			} catch (Exception e) {
				MessageDialog.openError(
						getShell(),
						"Problem posting message",
						"The message could not be posted. \n\r"
								+ e.getMessage());
				Debug.log(this.getClass(), e);
				e.printStackTrace();
			}
		} else {
			MessageDialog.openError(getShell(), "Problem posting message",
					"The message could not be posted. \n\r Messaage Source Failure");
		}
		return true;
	}

	private void setupContainer() {
		BundleContext context = FrameworkUtil.getBundle(this.getClass())
				.getBundleContext();
		ServiceReference reference = context
				.getServiceReference(ISalvoUtil.class.getName());
		SalvoUtil salvoUtil = (SalvoUtil) context.getService(reference);
		this.iContainer = salvoUtil.getDefault().getContainerManager()
				.getAllContainers()[0];
		adaptor = (IChannelContainerAdapter) iContainer
				.getAdapter(IChannelContainerAdapter.class);
		
	}

}
