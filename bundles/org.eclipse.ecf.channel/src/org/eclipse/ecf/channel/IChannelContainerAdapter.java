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
	public void reply(IMessage message, ITransactionContext context);

	/**
	 * Method to post new messages to a source.
	 * 
	 * @param message
	 *            : message to be posted
	 * @param context
	 *            : Used to specify messageSource etc.
	 * @throws Exception
	 */
	public void postNewMessages(IMessageSource[] message, String subject, String body)
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

}
