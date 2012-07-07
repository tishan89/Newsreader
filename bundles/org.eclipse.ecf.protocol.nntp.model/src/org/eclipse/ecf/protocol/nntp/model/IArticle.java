/*******************************************************************************
 *  Copyright (c) 2010 Weltevree Beheer BV, Remain Software & Industrial-TSI
 *                                                                      
 * All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html                            
 *                                                                      
 * Contributors:                                                        
 *    Wim Jongman - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.protocol.nntp.model;

import org.eclipse.ecf.channel.model.IMessage;

/**
 * This class contains the article meta data. The actual contents must be
 * fetched from the server or store.
 * 
 * @author Wim Jongman
 */
public interface IArticle extends IMessage{

	
	/**
	 * 
	 * @return the newsgroup that holds this article
	 */
	public INewsgroup getNewsgroup();
	

	/**
	 * @return The number of this article in this newsgroup
	 * 
	 */
	public int getArticleNumber();

		/**
	 * Either the full name is omitted, or it appears in parentheses after the
	 * electronic address of the person posting the article, or it appears
	 * before an electronic address enclosed in angle brackets. Thus, the three
	 * permissible forms are:<br>
	 * <br>
	 * From: mark@cbosgd.UUCP <br>
	 * From: mark@cbosgd.UUCP (Mark Horton) <br>
	 * From: Mark Horton &lt;mark@cbosgd.UUCP&gt;
	 * 
	 * @return - The full user name as specified in RFC850
	 */
	public String getFullUserName();

	/**
	 * @return String - the post date
	 */
	public String getDate();
	
	/**
	 * @return String - the references of this article
	 */
	public String getXRef();

	/**
	 * Sets the specified header <code>element</code> to the specified
	 * <code>value</code>.
	 * 
	 * @param element
	 * @param value
	 */
	public void setHeaderAttributeValue(String element, String value);

	
	public void setNewsgroup(INewsgroup newsgroup);

	public String[] getHeaderAttributes();

	/**
	 * Called to add information to the article that can be queried at display
	 * time.
	 * 
	 * @param article
	 * @param replies
	 */
	public void setThreadAttributes(IArticle[] replies);

	
	public String[] getHeaderAttributeValues();
	
	public INNTPServer getServer();
		
}
