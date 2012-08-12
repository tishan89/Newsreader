package org.eclipse.ecf.channel.core.internal;

import java.io.IOException;
import java.net.Socket;

import org.eclipse.ecf.channel.model.ICredentials;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.channel.model.IServer;
import org.eclipse.ecf.channel.model.IServerConnection;
import org.eclipse.ecf.channel.core.Debug;


/**
 * 
 * Do not extend this class ever. 
 * This class is not complete and implemented for the sole purpose of
 * carrying a general ServerConnection.
 * 
 *
 */
public class ServerConnection implements IServerConnection{
	private IServer server;
	private Socket socket;
	private boolean possibleResponseAvailable;
	private String lastResponse;
	private ICredentials credentials;
	
	public ServerConnection(IServer server) {
		this.server = server;
		server.setServerConnection(this);
	}

	
	@Override
	public void connect() throws Exception{
		
		
	}

	@Override
	public void disconnect() throws Exception {
		
		
	}

	@Override
	public IServer getServer() {
		return server;
	}

	@Override
	public boolean isConnected() {
		if (socket == null)
			return false;
		try {
			socket.getOutputStream().write(" ".getBytes());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public void sendCommand(String command) throws Exception {


		synchronized (socket) {

			Debug.log(this.getClass(), command);

			try {
				socket.getOutputStream().write((command + "\r\n").getBytes());
			} catch (IOException e) {
				Debug.log(this.getClass(), "Error received, reconnecting");
				Debug.log(this.getClass(), e);
				connect();
				
				try {
					socket.getOutputStream().write(
							(command + "\r\n").getBytes());
				} catch (IOException e1) {
					Debug.log(this.getClass(), "Could not send command due to "
							+ e1.getMessage());		
					throw e1;
				}
			}
			setPossibleResponseAvailable(true);
		}

	
		
	}

	public void setPossibleResponseAvailable(boolean b) {
		this.possibleResponseAvailable = b;
		
	}
	
	public boolean isPossibleResponseAvailable() {
		return possibleResponseAvailable;
	}

	

	@Override
	public String getLastResponse() {
		return lastResponse;
	}

	@Override
	public String[] getListResponse() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCredentials(ICredentials credentials) {
		this.credentials = credentials;
		
	}
	public ICredentials getCredentials(){
		return this.credentials;
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFullUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLastResponse(String lastResponse) {
		this.lastResponse = lastResponse;
		
	}

	@Override
	public String getResponse() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * This method should be overridden by any extending
	 * concrete implementation.
	 */
	@Override
	public IMessageSource[] getMessageSource() throws Exception{
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
