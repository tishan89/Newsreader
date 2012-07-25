package org.eclipse.ecf.salvo.ui.wizards;

import org.eclipse.ecf.channel.ITransactionContext;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.salvo.ui.internal.wizards.ISalvoWizard;
import org.eclipse.jface.wizard.Wizard;

public class SalvoWizard extends Wizard implements ISalvoWizard {
	private IContainer container;
	private ITransactionContext transactionContext;

	public void setIContainer(IContainer container) {
		this.container = container;

	}
	/* 
	 * setContainer method should be executed
	 * before executing this method.
	 */
	public IContainer getIContainer() {
		return this.container;
	}

	public void setTransactioncontext(ITransactionContext transactionContext) {
		this.transactionContext = transactionContext;
	}

	
	/* 
	 * setTransactioncontext should be executed
	 * before executing this method.
	 */
	public ITransactionContext getTransactionContext() {
		return this.transactionContext;
	}
	@Override
	public boolean performFinish() {
		// will be overrided at implementation level..
		return false;
	}

}
