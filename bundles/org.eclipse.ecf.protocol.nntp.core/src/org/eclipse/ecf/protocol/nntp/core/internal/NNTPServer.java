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
package org.eclipse.ecf.protocol.nntp.core.internal;

import org.eclipse.ecf.channel.core.internal.Server;
import org.eclipse.ecf.channel.model.IServerConnection;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerConnection;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.protocol.nntp.model.NNTPIOException;
import org.eclipse.ecf.protocol.nntp.model.UnexpectedResponseException;

public class NNTPServer extends Server implements INNTPServer {

	private INNTPServerConnection connection;

	private String[] overviewHeaders;

	private boolean dirty;

	private boolean initialized;

	/**
	 * Note that this is an internal class. A server factory cannot be far away.
	 * 
	 * @param address
	 * @param port
	 * @param secure
	 * @throws NNTPIOException
	 * @throws UnexpectedResponseException
	 */
	public NNTPServer(String address, int port, boolean secure)
			throws NNTPIOException, UnexpectedResponseException {
		super(address, port, secure);

	}

	public boolean equals(Object obj) {
		if (obj instanceof INNTPServer)
			return toString().equals(obj.toString());
		return super.equals(obj);
	}

	public String toString() {
		return getAddress() + "::" + getPort() + "::"
				+ getServerConnection().getUser() + "::"
				+ getServerConnection().getEmail() + "::"
				+ getServerConnection().getLogin() + "::" + isSecure() + "::"
				+ isSubscribed();
	}
	
	public int hashCode() {
		return toString().hashCode();
	}

	public INNTPServerConnection getServerConnection() {
		return connection;
	}

	public void init() throws NNTPException {
		connection.connect();
		connection.setModeReader(this);
		setInitialized(true);

	}

	public void setServerConnection(IServerConnection connection) {

		this.connection = (INNTPServerConnection) connection;
	}

	public String[] getOverviewHeaders() {
		return overviewHeaders;
	}

	public void setOverviewHeaders(String[] headers) {
		overviewHeaders = headers;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}


	private void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isInitialized() {
		return initialized;
	}
}
