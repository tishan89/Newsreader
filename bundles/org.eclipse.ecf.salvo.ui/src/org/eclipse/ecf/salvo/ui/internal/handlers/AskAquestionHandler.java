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
package org.eclipse.ecf.salvo.ui.internal.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.salvo.ui.wizards.AskAQuestionWizard;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class AskAquestionHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		AskAQuestionWizard wizard = new AskAQuestionWizard();
		WizardDialog dialog = new WizardDialog(
				HandlerUtil.getActiveShell(event), wizard);

		if (NNTPServerStoreFactory.instance().getServerStoreFacade()
				.getFirstStore() == null) {
			
			Shell dialogShell = new Shell();
			
			MessageDialog
					.openError(dialogShell, "Salvo Newsreader",
							"No stores found. Please start a store to use this feature");
			Debug.log(this.getClass(), "No stores found. Start a store to use this feature");
		} else {
			dialog.open();
		}
		return null;
	}

}
