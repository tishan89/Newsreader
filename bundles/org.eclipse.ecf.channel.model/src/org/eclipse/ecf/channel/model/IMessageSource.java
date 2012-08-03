package org.eclipse.ecf.channel.model;

import java.io.Serializable;
import java.util.Calendar;

/*
 * This is the interface to represent message sources
 * that lies within Servers. Ex: Newsgroups, Forums
 * Specific interfaces should extend from this
 * interface.
 */
public interface IMessageSource extends IProperties, ISubscribable,
		Serializable {
	public final String POSTING_PERMITTED = "Y";

	public final String POSTING_NOT_PERMITTED = "n";

	public final String POSTING_PERMITTED_MODERATED = "m";

	public String getCreatedBy();

	public Calendar getDateCreated();

	public String getDescription();

	public IServer getServer();

	/**
	 * 
	 * @return the string that makes this messageSource unique
	 */
	public String getURL();
	
	public String getMessageSourceName();

}
