package org.eclipse.ecf.channel;

import org.eclipse.ecf.core.security.IConnectContext;

//Interface to be implemented to send relevant context to 
//message services.
public interface ITransactionContext extends IConnectContext{

	public void setPWord(String pWord);
	
	public String getPWord();
}
