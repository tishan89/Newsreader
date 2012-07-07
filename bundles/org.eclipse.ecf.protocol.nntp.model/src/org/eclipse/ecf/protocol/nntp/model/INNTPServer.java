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

import org.eclipse.ecf.channel.model.IServer;
/**
 * @author jongw
 * 
 */
public interface INNTPServer extends IServer {	

	/**
	 * Returns the {@link INNTPServerConnection} object used for communications.
	 * 
	 * @return the {@link INNTPServerConnection} object used for communcations.
	 *         Cannot be null.
	 */
	public INNTPServerConnection getServerConnection();

	/**
	 * Initializes the server. You can setup the connection here but note that
	 * news servers are very impatient and close the connection as soon as they
	 * can. You could do other one time setup here like getting the overview
	 * headers, query for capabilities etc. This method must set the
	 * initialized flag, if {@link #isInitialized()} returns false, no
	 * initialization whatsoever may be assumed to have occurred.
	 * 
	 * @throws NNTPException
	 */
	public void init() throws NNTPException;

	

	/**
	 * Gets the overview headers from this server, could be null if they were
	 * not set before.
	 * 
	 * @return the list over overview headers, or null
	 */
	public String[] getOverviewHeaders();

	/**
	 * Sets the overview headers for quick reference. This should be set by the
	 * {@link INNTPServerConnection} if it is fetched for the first time.
	 * 
	 * @param headers
	 */
	public void setOverviewHeaders(String[] headers);

	

	/**
	 * Asynchronous flag to indicate that the server should be visited again to
	 * make sure everything is still in sync with the database. This is a
	 * convenience flag, no operation of the server may be blocked by the server
	 * being dirty.
	 * 
	 * @param dirty
	 */
	public void setDirty(boolean dirty);

	/**
	 * Asynchronous flag to indicate that the server should be visited again to
	 * make sure everything is still in sync with the database. This is a
	 * convenience flag, no operation of the server may be blocked by the server
	 * being dirty.
	 * 
	 */
	public boolean isDirty();

	
	
	
}
