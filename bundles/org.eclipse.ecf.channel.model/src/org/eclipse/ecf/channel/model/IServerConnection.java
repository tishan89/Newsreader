package org.eclipse.ecf.channel.model;

import java.net.Socket;

/*
 * Interface to be extended when creating a
 * ServerConnection interface for a particular interface
 * Ex: INNTPServerConnection.
 */

public interface IServerConnection {

	/**
	 * Connects to the server
	 * 
	 * @throws Exception
	 */
	public void connect() throws Exception;

	/**
	 * Disconnects from the server
	 * 
	 * @throws Exception
	 */
	public void disconnect() throws Exception;

	/**
	 * If you instantiate this object, make sure to call
	 * {@link #setServer(IServer)} before anyone accesses this method because
	 * you may not return null.
	 * 
	 * @return the {@link IServer} object this connection connects for which is
	 *         guaranteed not to be null.
	 */
	public IServer getServer();

	/**
	 * Queries the socket for a valid connection.
	 * 
	 * @return {@link Socket#isConnected()}
	 */
	public boolean isConnected();

	/**
	 * Sends a command to the server returns true if the command was send in
	 * which case the response can be had from {@link #getresponse()}
	 * 
	 * @param command
	 * @throws Exception
	 * 
	 */
	public void sendCommand(String command) throws Exception;

	/**
	 * Retrieves the response send by the last {@link #sendCommand(String)}. The
	 * response should be stored so that the {@link #getLastResponse()} method
	 * can do its work. Necessary exception should be thrown at implementation.
	 * 
	 * @return String response
	 * @throws Exception
	 */
	public String getResponse() throws Exception;

	/**
	 * Returns the last response from the server as saved by the last call to
	 * the {@link #getResponse()} method.
	 * 
	 * @return String the last known response
	 */
	public String getLastResponse();

	/**
	 * Retrieves the response send by the last {@link #sendCommand(String)} and
	 * returns the response in an array. The response of this command must be a
	 * list. This means that the response is terminated with a dot and the CRLF
	 * sequence.
	 * 
	 * @see <a href="http://tools.ietf.org/html/rfc3977#section-3.1.1">RFC3977
	 *      Section 3.1.1</a>
	 * 
	 *      Necessary exception should be thrown at implementation.
	 * 
	 * @return the response in a String[]
	 * @throws Exception
	 */
	public String[] getListResponse() throws Exception;

	/**
	 * Flushes the remaining data from the server. This is generally used to
	 * throw away all responses that are generated by a call to
	 * {@link #sendCommand(String)}.
	 */
	public void flush();

	/**
	 * Sets the credentials used to make the connection
	 * 
	 * @param credentials
	 */
	public void setCredentials(ICredentials credentials);

	public String getEmail();

	/**
	 * @return the login for this server or null if this is an anonymous login
	 */
	public String getLogin();

	public String getUser();

	/**
	 * @return the full user name
	 */
	public String getFullUserName();
	
	public void setLastResponse(String lastResponse);
	
	public void setPossibleResponseAvailable(boolean b);	
	
	public boolean isPossibleResponseAvailable() ;
	
	/**
	 * @return the array of all MessageSources found on this server.
	 * @throws Exception
	 * 
	 */
	
	public IMessageSource[] getMessageSource() throws Exception;
	
	/**
	 * Method to get the credentials of
	 * ServerConnection.
	 * @return credentials
	 */
	public ICredentials getCredentials();

}
