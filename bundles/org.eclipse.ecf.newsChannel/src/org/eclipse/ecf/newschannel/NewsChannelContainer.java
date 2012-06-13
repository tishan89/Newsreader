package org.eclipse.ecf.newschannel;

import org.eclipse.ecf.core.AbstractContainer;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.provider.nntp.NNTPServerContainer;

public class NewsChannelContainer extends AbstractContainer {
	
	private ID DEFAULT_ID;
	private IContainer container;
	private IConnectContext context;

	
	public NewsChannelContainer(ID id) throws ContainerCreateException{
		
			container = ContainerFactory.getDefault().createContainer(id);
		
	}	
	
	/**
	 * If no arguments are provided to the constructor
	 * predifined default protocol will be used
	 * @throws ContainerCreateException 
	 */
	public NewsChannelContainer() throws ContainerCreateException{
		new NewsChannelContainer(DEFAULT_ID);
	}	

	
	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainer#connect(org.eclipse.ecf.core.identity.ID, org.eclipse.ecf.core.security.IConnectContext)
	 * Relevant container implementation will handle the actual connecting
	 */
	public void connect(ID id, IConnectContext context)
			throws ContainerConnectException {
		this.context=context;
		container.connect(id, context);

	}

	
	public void disconnect() {
		container.disconnect();

	}
	
	public void getContainer(){
		if(container instanceof NNTPServerContainer){
			container = (NNTPServerContainer) container;
		}
	}

	public void fetchMessages(){
		if(container instanceof NNTPServerContainer){
			container.getNews(context);
		}
		//Redirect to other Implementations
	}
	public Namespace getConnectNamespace() {		
		return container.getConnectNamespace();
	}

	
	public ID getConnectedID() {	
		return container.getConnectedID();
	}

	
	public ID getID() {		
		return container.getID();
	}

}
