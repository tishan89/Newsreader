package org.eclipse.ecf.channel.core;

import java.util.HashMap;

import org.eclipse.ecf.channel.core.internal.MessageSource;
import org.eclipse.ecf.channel.core.internal.Server;
import org.eclipse.ecf.channel.core.internal.ServerConnection;
import org.eclipse.ecf.channel.model.ICredentials;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.channel.model.IServer;
import org.eclipse.ecf.channel.model.IServerConnection;

public class ServerFactory {
	private ServerType type;
	private static HashMap servers = new HashMap();

	public static IServer getServer(String address, int port,
			ICredentials credentials, boolean secure) {

		return (IServer) servers.get(address + "::" + port + "::"
				+ credentials.getUser() + "::" + credentials.getEmail() + "::"
				+ credentials.getLogin() + "::" + secure);
	}

	public static IServer getCreateServer(String address, int port,
			ICredentials credentials, boolean secure) {

		IServer server = (IServer) servers.get(address + "::" + port + "::"
				+ credentials.getUser() + "::" + credentials.getEmail() + "::"
				+ credentials.getLogin() + "::" + secure);

		if (server != null) {
			server.getServerConnection().setCredentials(credentials);
			return server;
		}

		server = new Server(address, port, secure);
		IServerConnection connection = new ServerConnection(server);
		connection.setCredentials(credentials);
		if (servers.get(server.toString()) != null)
			return (IServer) servers.get(server.toString());

		try {
			server.init();
		} catch (Exception e) {
			// Swallow
		}

		servers.put(server.toString(), server);
		return server;

	}

	private void setServerType(String address) {
		if (address.split(":")[0].contains("nntp")) {
			type = ServerType.NNTP;
		}
	}

	/*
	 * public static IMessageSource getMessageSource(IServer server, String
	 * source, String description) { return new MessageSource(server, source,
	 * description); }
	 */
}
