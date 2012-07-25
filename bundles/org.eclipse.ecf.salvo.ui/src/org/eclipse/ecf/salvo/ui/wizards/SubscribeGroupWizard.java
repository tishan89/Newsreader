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

import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.protocol.nntp.core.Debug;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.salvo.ui.internal.wizards.SubscribeGroupWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;


public class SubscribeGroupWizard extends Wizard implements INewWizard {

	private SubscribeGroupWizardPage page1;
	private final INNTPServer server;

	public SubscribeGroupWizard(INNTPServer server) {
		this.server = server;
	}

	@Override
	public void addPages() {
		page1 = new SubscribeGroupWizardPage("Subscribe to Group");
		addPage(page1);
	}

	@Override
	public boolean performFinish() {

		INNTPServerStoreFacade storeFacade = NNTPServerStoreFactory.instance()
				.getServerStoreFacade();
		for (IMessageSource group : page1.getGroups()) {
			try {
				storeFacade.subscribeNewsgroup(group);
			} catch (NNTPException e) {
				// FIXME handle communication to user
				Debug.log(getClass(), e);
			}
		}
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public void createPageControls(Composite pageContainer) {
		IWizardPage page = (IWizardPage) super.getPages()[0];
		page.createControl(pageContainer);
		page1.setInput(server);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return super.getNextPage(page);
	}
}
