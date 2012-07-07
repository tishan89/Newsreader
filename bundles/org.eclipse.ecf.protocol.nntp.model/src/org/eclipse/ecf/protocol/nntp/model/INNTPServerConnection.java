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
package org.eclipse.ecf.protocol.nntp.model;

//import java.net.Socket;

import org.eclipse.ecf.channel.model.ICredentials;
import org.eclipse.ecf.channel.model.IServerConnection;

public interface INNTPServerConnection extends IBasicNNTPInterface,
		IInputOutputSystem, IServerConnection {

	/**
	 * Connects to the server. Make sure to put the server in mode reader after
	 * the connect and getting the overview headers by calling the
	 * {@link #setModeReader()} and {@link #loadOverviewHeader()} methods.
	 * <p>
	 * Implementers take care to avoid cycles because the connect method can be
	 * used to rebuild a dropped connection on the fly and NNTP servers are very
	 * happy to drop idle connections.
	 * </p>
	 * Make sure to call the {@link #setServer(INNTPServer)} method or provide
	 * the server by the constructor before the {@link #getServer()} method is
	 * accessed.
	 * <p>
	 * Get the credentials for the server from the {@link ICredentials} object
	 * that was injected by using the {@link #setCredentials(ICredentials)}
	 * method.
	 * </p>
	 * 
	 * @throws NNTPIOException
	 * @throws NNTPConnectException
	 * @throws NNTPCredentialsException
	 */
	public void connect() throws NNTPIOException, NNTPConnectException,
			NNTPCredentialsException;

	/**
	 * Disconnects from the server.
	 * 
	 * @throws NNTPConnectException
	 */
	public void disconnect() throws NNTPConnectException;

	/**
	 * @return the array of all {@link INewsgroup}s found on this server.
	 * @throws NNTPIOException
	 * @throws UnexpectedResponseException
	 */
	public INewsgroup[] getNewsgroups() throws NNTPIOException,
			UnexpectedResponseException;

	/**
	 * Sends a command to the server returns true if the command was send in
	 * which case the response can be had from {@link #getresponse()}
	 * 
	 * @param command
	 * @throws NNTPIOException
	 * @throws {@link UnexpectedResponseException}
	 */
	public void sendCommand(String command) throws NNTPIOException,
			UnexpectedResponseException;

	/**
	 * Retrieves the response send by the last {@link #sendCommand(String)}. The
	 * response should be stored so that the {@link #getLastResponse()} method
	 * can do its work.
	 * 
	 * @return String response
	 * @throws NNTPIOException
	 */
	public String getResponse() throws NNTPIOException;

	/**
	 * Retrieves the response send by the last {@link #sendCommand(String)} and
	 * returns the response in an array. The response of this command must be a
	 * list. This means that the response is terminated with a dot and the CRLF
	 * sequence.
	 * 
	 * @see <a href="http://tools.ietf.org/html/rfc3977#section-3.1.1">RFC3977
	 *      Section 3.1.1</a>
	 * 
	 * @return the response in a String[]
	 * @throws NNTPIOException
	 */
	public String[] getListResponse() throws NNTPIOException;

	/**
	 * Sets the batch size of article headers to retrieve from the news server.
	 * If not set then it defaults to SALVO.DEFAULT_BATCH
	 * 
	 * @param size
	 *            the batch size
	 */
	public void setBatchSize(int size);

	/**
	 * Returns the batch size.
	 * 
	 * @see INNTPServer#setBatchSize(int)
	 * @return the batch size as int
	 */
	public int getBatchsize();

	/**
	 * This method goes to the server and asks for the active newsgroup
	 * attributes. These attributes are then placed back into the newsgroup.
	 * 
	 * @param newsgroup
	 * @throws NNTPConnectException
	 * @throws NNTPIOException
	 * @return Newsgroup attributes articleCount, lowWaterMark, highWaterMark
	 */
	public int[] getWaterMarks(INewsgroup newsgroup) throws NNTPIOException,
			UnexpectedResponseException;

}
