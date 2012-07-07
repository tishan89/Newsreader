package org.eclipse.ecf.channel.model;
/*
 * This is the general interface to provide message
 * store facility. Any class which expects to provide
 * message storing capabilities should extend from this.
 */
public interface IStore {

	/**
	 * Stores the places where secure information can be stored. This could
	 * depend on the local implementation of the store or the capabilities of
	 * the platform the store is running on. For example, the Eclipse newsreader
	 * implementation is using the secure preferences.
	 * 
	 * @param secureStore
	 */
	public void setSecureStore(ISecureStore secureStore);
	
	/**
	 * Stores the messages into store
	 * @param messages
	 * @throws Exception
	 */
	public void storeMessage (IMessage[] messages)throws Exception;
	
	/**
	 * Store a single message into store
	 * @param message
	 * @param body
	 * @throws Exception
	 */
	public void storeMessageBody(IMessage message, String[] body)throws Exception;
	
	/**
	 * Updates the message passed as the parameter.
	 * @param message
	 * @throws Exception
	 */
	public void updateMessage(IMessage message) throws Exception;
	
	/**
	 * Gets the secure store which was previously set with
	 * {@link #setSecureStore(ISecureStore)}.
	 * 
	 */
	public ISecureStore getSecureStore();
	
	/**
	 * Returns a meaningful description of this store. <br>
	 * <br>
	 * Examples:
	 * 
	 * <pre>
	 * Local filesystem storage
	 * Derby 4.2.1
	 * </pre>
	 * 
	 * @return String
	 */
	public String getDescription();
	
	/**
	 * Get the article in the given message source
	 * with the given ID.
	 * @param source
	 * @param msgId
	 * @return
	 */
	public IMessage getMessageByMsgId(IMessageSource source,String msgId );
	
	/**
	 * Get the message given by the message ID
	 * @param msgId
	 * @return
	 */
	public IMessage getMessageByMsgId(String msgId );
	
	
}
