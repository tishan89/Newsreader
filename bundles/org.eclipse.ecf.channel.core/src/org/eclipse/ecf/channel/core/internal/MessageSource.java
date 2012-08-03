package org.eclipse.ecf.channel.core.internal;

import java.util.Calendar;
import java.util.Map;

import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.channel.model.IServer;

public class MessageSource implements IMessageSource {
	
	private IServer server;
	private String messageSourceName;
	private String description;
	public MessageSource (IServer server, String source, String description){
		this.server=server;
		this.messageSourceName=source.trim();
		this.description=description.trim();
	}

	@Override
	public void setProperty(String key, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSubscribed(boolean subscribe) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSubscribed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCreatedBy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getDateCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IServer getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessageSourceName() {
		// TODO Auto-generated method stub
		return null;
	}

}
