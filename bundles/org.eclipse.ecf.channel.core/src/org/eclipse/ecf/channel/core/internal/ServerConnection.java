package org.eclipse.ecf.channel.core.internal;

import java.io.IOException;
import java.net.Socket;

import org.eclipse.ecf.channel.model.ICredentials;
import org.eclipse.ecf.channel.model.IServer;
import org.eclipse.ecf.channel.model.IServerConnection;
import org.eclipse.ecf.channel.core.Debug;


public class ServerConnection implements IServerConnection{
	private IServer server;
	private Socket socket;
	private boolean possibleResponseAvailable;
	private String lastResponse;
	
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
		// TODO Auto-generated method stub
		
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
	
	

}
