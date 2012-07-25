/*******************************************************************************
 *  Copyright (c) 2010 Weltevree Beheer BV, Remain Software & Industrial-TSI
 *                                                                      
 * All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html                            
 *                                                                      
 * Contributors:                                                        
 *    Wim Jongman - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.salvo.ui.wizards;

import org.eclipse.ecf.channel.IChannelContainerAdapter;
import org.eclipse.ecf.channel.ITransactionContext;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.channel.model.IServer;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.security.CallbackHandler;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.protocol.nntp.core.Debug;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.provider.nntp.NNTPServerContainer;
import org.eclipse.ecf.salvo.ui.internal.wizards.NewNewsServerWizardPage;
import org.eclipse.ecf.salvo.ui.internal.wizards.SubscribeGroupWizardPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewNewsServerWizard extends SalvoWizard implements INewWizard {

	protected NewNewsServerWizardPage page1;
	protected SubscribeGroupWizardPage page2;
	private INNTPServer server;
	private IContainer container;
	private IConnectContext context;
	private ID targetID;
	private ITransactionContext tContext;

	public NewNewsServerWizard() {
	}

	public NewNewsServerWizard(INNTPServer server) {
		this.server = server;
	}

	@Override
	public void addPages() {
		page1 = new NewNewsServerWizardPage("Get Server Information");
		page2 = new SubscribeGroupWizardPage("Subscribe to Group");
		addPage(page1);
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
		// IServer targetServer = page1.getServer();

		try {
			if(page1.getAddress().split(":")[0].contains("nntp")){
			container = ContainerFactory.getDefault().createContainer(
					"ecf.provider.nntp");
			
			}
			// context =
			// ConnectContextFactory.createPasswordConnectContext(page1.getPass());
			tContext = new ITransactionContext() {
				private String pWord;

				public CallbackHandler getCallbackHandler() {
					// TODO Auto-generated method stub
					return null;
				}

				public void setPWord(String pWord) {
					this.pWord = pWord;

				}

				public String getPWord() {
					return pWord;
				}
			};
			tContext.setPWord(page1.getPass());
			targetID = IDFactory.getDefault()
					.createID(container.getConnectNamespace(),
							page1.getServer().getURL());
			
			//just for testing
			if (container instanceof NNTPServerContainer) {
				((NNTPServerContainer) container).setServer((INNTPServer)page1.getServer());
			}
			container.connect(targetID, tContext);
		} catch (ContainerCreateException e2) {
			 //TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IDCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NNTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ContainerConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*INNTPServerStoreFacade storeFacade = ServerStoreFactory.instance()
				.getServerStoreFacade();
		try {
			storeFacade.subscribeServer(page1.getServer(), page1.getPass());
		} catch (NNTPException e1) {
			Debug.log(getClass(), e1);
			setWindowTitle(e1.getMessage());
			return false;
		}*/

		IChannelContainerAdapter adaptor = (IChannelContainerAdapter) container
				.getAdapter(IChannelContainerAdapter.class);
		try {
			adaptor.connectToMessageSource(page2.getGroups());
		} catch (Exception e) {

			MessageDialog.openError(getShell(), "Problem subscribing",
					e.getMessage());
			e.printStackTrace();
		}

		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public void createPageControls(Composite pageContainer) {
		IWizardPage page = (IWizardPage) super.getPages()[0];
		page.createControl(pageContainer);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		try {
			if (page instanceof SubscribeGroupWizardPage)
				((SubscribeGroupWizardPage) page).setInput(page1.getServer());
		} catch (NNTPException e) {
			Debug.log(getClass(), e);
		}
		return super.getNextPage(page);
	}

	public INNTPServer getServer() {
		return server;
	}
}
