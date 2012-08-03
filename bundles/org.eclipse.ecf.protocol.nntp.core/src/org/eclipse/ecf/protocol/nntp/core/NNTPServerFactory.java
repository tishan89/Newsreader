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
package org.eclipse.ecf.protocol.nntp.core;

import java.util.HashMap;

import org.eclipse.ecf.channel.model.ICredentials;
import org.eclipse.ecf.protocol.nntp.core.internal.Newsgroup;
import org.eclipse.ecf.protocol.nntp.core.internal.NNTPServer;
import org.eclipse.ecf.protocol.nntp.core.internal.NNTPServerConnection;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerConnection;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;

public class NNTPServerFactory {

	private static HashMap servers = new HashMap();

	public static INNTPServer getServer(String address, int port,
			ICredentials credentials, boolean secure) {

		return (INNTPServer) servers.get(address + "::" + port + "::"
				+ credentials.getUser() + "::" + credentials.getEmail() + "::"
				+ credentials.getLogin() + "::" + secure);
	}

	public static INNTPServer getCreateServer(String address, int port,
			ICredentials credentials, boolean secure) throws NNTPException {

		INNTPServer server = (INNTPServer) servers.get(address + "::" + port + "::"
				+ credentials.getUser() + "::" + credentials.getEmail() + "::"
				+ credentials.getLogin() + "::" + secure);

		if (server != null) {
			server.getServerConnection().setCredentials(credentials);
			server.setDirty(false);
			return server;
		}

		server = new NNTPServer(address, port, secure);
		INNTPServerConnection connection = new NNTPServerConnection(server);
		connection.setCredentials(credentials);
		if (servers.get(server.toString()) != null)
			return (INNTPServer) servers.get(server.toString());

		try {
			server.init();
		} catch (Exception e) {
			// Swallow
		}

		servers.put(server.toString(), server);
		return server;
	}

	public static INewsgroup getGroup(INNTPServer server, String newsGroup,
			String description) {
		return new Newsgroup(server, newsGroup, description);
	}

}
