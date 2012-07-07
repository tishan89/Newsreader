package org.eclipse.ecf.channel.model;

public interface IServerStoreFacade {
	/**
	 * This class controls fetching of information from the server or from the
	 * store. Any protocol specific implementation should extend from this.
	 * 
	 */

	/**
	 * If an exception occurred in the store or the sever connection it is
	 * stored and you can retrieve it with this method.
	 * 
	 * @return
	 */
	public Exception getLastException();

	/**
	 * Catches up since the last visit to the server and stores the information
	 * in the store.
	 * 
	 * @param source
	 * @throws Exception
	 */
	public void catchUp(IMessageSource source) throws Exception;

	public void init();

	/**
	 * Gets the first store can be null if no store service is active.
	 * 
	 * @return the first store or null if no store is available.
	 */
	public IStore getFirstStore();

	/**
	 * Gets all stores.
	 * 
	 * @return
	 */
	public IStore[] getStores();

	public void updateMessage(IMessage message) throws Exception;

	/**
	 * Get article from messageId
	 * 
	 * @param source
	 * 
	 * @param msgId
	 * 
	 * @return message which has the particular message id
	 * 
	 */
	public IMessage getMessageByMsgId(IMessageSource source, String msgId);

	/**
	 * Get the first message of a thread which corresponds to a follow-up
	 * message
	 * 
	 * @param message
	 *            a follow-up message of a thread
	 * 
	 * @return the first message of a thread which corresponds to the follow-up
	 *         message
	 */
	public IMessage getFirstMessageOfTread(IMessage message);

	/**
	 * Order Messsages from the Newest First.
	 * 
	 * @param messages
	 *            messages to be ordered
	 * @return ordered messages
	 */
	public IMessage[] orderMessagesesFromNewestFirst(IMessage[] messages);

	/**
	 * Update store with the server
	 * 
	 * @param messageSource
	 * 
	 * @param isNewMessagePosted
	 *            whether a new message is posted just before sync.
	 */
	public void syncStoreWithServer(IMessageSource messageSource,
			boolean isNewMessagePosted) throws Exception;
}
