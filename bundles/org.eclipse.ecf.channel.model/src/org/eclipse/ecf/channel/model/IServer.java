package org.eclipse.ecf.channel.model;

/*
 * This interface defines the server which clients connects to get the
 * Articles/posts/etc.
 * This interface should be extended/implemented by any protocol specific
 * server interface/class. 
 * 
 */
public interface IServer extends IProperties, ISubscribable{

	/**
	 * Returns the {@link IServerConnection} object used for communications.
	 * 
	 * @return the {@link IServerConnection} object used for communications.
	 *         Cannot be null.
	 */
	public IServerConnection getServerConnection();
	
	/**
	 * Sets the server connection. Please note that the
	 * {@link #getServerConnection()} method is guaranteed not to return null.
	 * Server factories would call this method.
	 * 
	 * @param connection
	 */
	public void setServerConnection(IServerConnection connection);
	
	/**
	 * Returns the port.
	 * 
	 * @return int
	 */
	public int getPort();

	/**
	 * Returns the TCP/IP address.
	 * 
	 * @return String
	 */
	public String getAddress();
	
	/**
	 * Initializes the server. You can setup the connection here but note that
	 * servers are very impatient and close the connection as soon as they
	 * can. You could do other one time setup here like getting the overview
	 * headers, query for capabilities et cetera. This method must set the
	 * initialized flag, if {@link #isInitialized()} returns false, no
	 * initialization whatsoever may be assumed to have occurred.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception;
	
	/**
	 * Indicates that this server communicates over a secure layer (SSL/TSL).
	 * 
	 * @return
	 */
	public boolean isSecure();

	/**
	 * Returns true if this is an anonymous server connection.
	 * 
	 * @return boolean
	 */
	public boolean isAnonymous();

	/**
	 * Gets the organization this user belongs to.
	 * 
	 * @return
	 */
	public String getOrganization();
	
	/**
	 * @return the unique id of this server
	 */
	String getID();
	
	/**
	 * Composes a server url Ex: nntp://address:port
	 *  
	 * @return the url for this server
	 */
	public String getURL();

	/**
	 * Indicates if the initialize method has run successfully.
	 * 
	 * @return
	 */
	public boolean isInitialized();
	

}
