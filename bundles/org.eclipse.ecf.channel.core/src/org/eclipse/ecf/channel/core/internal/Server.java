/* 
* All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html   
 */

package org.eclipse.ecf.channel.core.internal;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.ecf.channel.model.IServer;
import org.eclipse.ecf.channel.model.IServerConnection;

public class Server implements IServer
{
	private final String address;

	private final int port;

	private final boolean secure;

	private IServerConnection connection;

	private String organization;

	private boolean subscribed;

	private HashMap properties;

	private boolean initialized;
	
	
	/**
	 * This is the concrete implementation of IServer. To be used internally.
	 * @param address
	 * @param port
	 * @param secure
	 */
	public Server (String address, int port, boolean secure){
		this.address = address;
		this.port = port;
		this.secure = secure ;
	}
	

	@Override
	public void setProperty(String key, String value) {
		if (value == null) {
			getProperties().remove(key);
			return;
		}
		getProperties().put(key, value);
		
	}

	@Override
	public String getProperty(String key) {
		return (String) getProperties().get(key);
	}

	@Override
	public Map getProperties() {
		if (properties == null) {
			properties = new HashMap();
		}
		return properties;
	}

	@Override
	public void setSubscribed(boolean subscribe) {
		this.subscribed = subscribe;
		
	}

	@Override
	public boolean isSubscribed() {
		return subscribed;
	}

	@Override
	public IServerConnection getServerConnection() {
		return this.connection;
	}

	@Override
	public void setServerConnection(IServerConnection connection) {
		this.connection = connection;
		
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void init() throws Exception {
		// TODO This method should be overridden by 
		//protocol specific implementations.
		
	}

	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public boolean isAnonymous() {
		return getServerConnection().getLogin() == null;
	}

	@Override
	public String getOrganization() {
		return organization;
	}

	@Override
	public String getID() {
		return toString();
	}

	@Override
	public String getURL() {
		return getAddress().trim() + ":" + getPort();
	}

	@Override
	public boolean isInitialized() {
		return false;
	}
	
	public String toString() {
		return getAddress() + "::" + getPort() + "::"
				+ getServerConnection().getUser() + "::"
				+ getServerConnection().getEmail() + "::"
				+ getServerConnection().getLogin() + "::" + isSecure() + "::"
				+ isSubscribed();
	}
	public boolean equals(Object obj) {
		if (obj instanceof IServer)
			return toString().equals(obj.toString());
		return super.equals(obj);
	}


}
