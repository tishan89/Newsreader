package org.eclipse.ecf.salvo.ui.internal.wizards;

import org.eclipse.ecf.channel.ITransactionContext;
import org.eclipse.ecf.core.IContainer;

/**
 * this class should be implemented by 
 * all wizard classes in salvo. This class contains the 
 * container instance which is used to connect to
 * news sources.
 *
 */
public interface ISalvoWizard {
	/**
	 * Get and set the container instance used
	 * to create the connection.
	 */
	public void setIContainer(IContainer container);
	public IContainer getIContainer();
	
	
	/**
	 * Get and set the transactionContext
	 * associated with the current container.
	 */
	public void setTransactioncontext (ITransactionContext transactionContext);
	public ITransactionContext getTransactionContext();
}
