/*******************************************************************************
 *  Copyright (c) 2012 University of Moratuwa
 *                                                                      
 * All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html                            
 *                                                                      
 * Contributors:                                                        
 *    Tishan Dahanayakage - initial API 
 *******************************************************************************/
package org.eclipse.ecf.channel;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ecf.channel.model.*;
import org.eclipse.ecf.channel.provider.IMessageSourceProvider;

/*This is the Connectivity API for Salvo Newsreader. 
 * Any concrete protocol dependent implementation 
 * will have to implement this interface.
 * 
 * IMessage : Abstract representation of simplest 
 * news element(Eg: IAticle, Forum Post)
 * 
 * IMessageSource : Abstract representation of a
 */

public interface IChannelContainerAdapter extends IAdaptable {

	/**
	 * This is the method to fetch messages from a source. Method will return a
	 * list of messages.
	 * 
	 * @param context
	 *            : Context data needed to fetch messages. Ex: Newsgroup name
	 * @return List of messages.
	 */
	public IMessage[] fetchMessages(ITransactionContext context);

	/**
	 * Method to fetch a single message using message id.
	 * 
	 * @param source
	 * @param Id
	 * @return
	 */
	public IMessage fetchMessage(IMessageSource source, String Id);

	/**
	 * Method to add an reply to a message in the source.
	 * 
	 * @param message
	 *            : The reply message
	 * @param context
	 *            : Context data needed for replying.
	 */
	public void reply(IMessage message, ITransactionContext context) throws Exception;

	
	
	/**
	 * Method to post new messages to a given message source/s
	 * @param sources : List of sources where the message should be posted
	 * @param message : Message to be posted
	 * @param context : relevant context data
	 * @throws Exception
	 */
	public void postNewMessages(IMessageSource[] sources, IMessage message, ITransactionContext context)
			throws Exception;

	/**
	 * Method to get all message sources in a given server.
	 * 
	 * @param server
	 * @return
	 * @throws Exception
	 *             (Connection)
	 */
	public IMessageSource[] listMessageSources(IServer server) throws Exception;

	/**
	 * Method to delete article. user should full fill permission requirements.
	 * 
	 * @param context
	 *            :needed details.
	 * @param messageId
	 */
	public void delete(ITransactionContext context, String messageId)
			throws Exception;

	/**
	 * Method to connect to given set of message sources
	 * 
	 * @param collection
	 *            Set of message sources
	 * @throws Exception
	 */
	public void connectToMessageSource(Collection<IMessageSource> collection)
			throws Exception;

	/**
	 * Method to get the server associated with the connection.
	 * 
	 * @return ISrver
	 */
	public IServer getServer();
	
	/**
	 * Method to return the list of subcribed message sources
	 * by user.
	 * @return List of Message sources.
	 */
	public ArrayList<IMessageSource> fetchSubcribedMessageSources();

	
	/**Salvo uses the concept of providers. 
	 * Providers provide the Message sources(INewsgroup/Forum). A provider is associated with server.
	 * It is always recommended to get a source through provider  in Salvo.
	 * @return List of Message Source Providers.
	 */
	public IMessageSourceProvider[] getProviders();

	/**
	 * Get a message source associated with a provide.
	 * Provider concept is explained earliar.
	 * @param provider (IMessageSourcePrvider)
	 * @return IMessageSource
	 */
	public IMessageSource getMessageSource(IMessageSourceProvider provider);

	/**
	 * Method to subcribe to given message source.
	 * @param source
	 * @throws Exception
	 */
	public void subscribeMessageSource(IMessageSource source)throws Exception;
	
	/**
	 * Method to get follow up message of a given messages.
	 * (Eg: If it is a forum this will list up all the posts
	 * given under given post)
	 * @return List of follow up messages.
	 */
	public IMessage[] fetchFollowups(IMessage message) throws Exception;
	
	/**
	 * Method to update an existing message.
	 * @param message Message to-be updated
	 * @param context To pass needed information
	 * @throws Exception
	 */
	public void updateMessage(IMessage message, ITransactionContext context) throws Exception;

}
