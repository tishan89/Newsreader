package org.eclipse.ecf.channel.model;

import java.io.Serializable;



/*This is the interface to which represent a general(abstract) message.
 * Any specific type(Ex: Article, Post) should implement this.
 */

public interface IMessage extends Serializable,IProperties{
	/**
	 * @param attribute
	 *            The String attribute
	 * @return the String value of the specified header attribute.
	 */
	public String getHeaderAttributeValue(String attribute);
	
	/**
	 * @return the server that holds the message.
	 */
	public IServer getServer();	
	
	
	/**
	 * @return the parent element of the message. 
	 */
	public IMessage getParent();
	
	/**
	 * @return the subject of the message
	 */
	public String getSubject();
	
	/**
	 * @return the name of the author.
	 */
	public String getFrom();
	
	/**
	 * @return the Id of the message
	 */
	public String getMessageId();
	
	/**
	 * @return String[] - the references of the article contain the article id's
	 *         that this article is a reply to. The first entry is the main
	 *         topic and the next entries are replies to that in a thread.
	 */
	public String[] getReferences();
	
	/**
	 * @return String - The size in readable format like 25KB
	 */
	public String getFormattedSize();

	/**
	 * @return int - The size of this message
	 */
	public int getSize();
	
	/**
	 * @return boolean - true if this article was already read or if it is your
	 *         own article.
	 */
	public boolean isRead();

	/**
	 * @return boolean - true if all the replies to this article are read or if
	 *         the unread article is your own reply.
	 */
	public boolean isReplyRead();

	/**
	 * Sets the read state of this article if this is not your own article. Your
	 * own articles always have the read state.
	 * 
	 * @param read
	 *            true if this article was read and false if this article was
	 *            not read.
	 */
	public void setRead(boolean read);
	
	/**
	 * Sets the read state of all replies to this article.
	 * 
	 * @param read
	 *            true if all replies to this article are read.
	 */
	public void setReplyRead(boolean read);

	/**
	 * 
	 * @return
	 */
	public boolean isPosted();
	
	/**
	 * Returns the last reference of this message which is the article replied
	 * to.
	 * 
	 * @return the last reference or null if no such reference exists
	 */
	public String getLastReference();
	
	/**
	 * @return true if this was posted by the current user according to the
	 *         server information.
	 */
	public boolean isMine();
	
	/**
	 * Set whether user is commenting on the thread.
	 * 
	 * @param commenting true if user is commenting.
	 */
	public void setCommenting(boolean commenting);
	
	/**
	 * Indicates if this user is commenting on this thread.
	 */
	public boolean isCommenting();
	
	/**
	 * Indicates if this message is a reply.
	 */
	public boolean isReply();

	/**
	 * Indicates if we follow this thread.
	 */
	public boolean isMarked();

	/**
	 * Set if we must follow this thread.
	 */
	public void setMarked(boolean marked);
	
	/**
	 * Gets the URL of this article.
	 * 
	 * @return the URL
	 */
	public String getURL();
	
	/**
	 * Set the parent of a message
	 * @param message
	 * 
	 */
	public void setParent(IMessage message);
	
	/**
	 * Get the message source that holds the message
	 * @return MessageSource
	 */
	public IMessageSource getMessageSource();
	/**
	 * Sets the specified header <code>element</code> to the specified
	 * <code>value</code>.
	 * 
	 * @param element
	 * @param value
	 */
	public void setHeaderAttributeValue(String element, String value);

	/**
	 * Method to return the message body as a String array.
	 * @return String[] Message body
	 */
	public String[] getMessageBody() throws Exception;
	
	/**
	 * Method to set associated content(Eg: follow ups) of a given
	 * message.
	 * @param replies
	 */
	public void setThreadAttributes(IMessage[] replies);
}
