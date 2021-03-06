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
package org.eclipse.ecf.protocol.nntp.core.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.channel.model.IMessage;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.protocol.nntp.core.ArticleEventListnersFactory;
import org.eclipse.ecf.protocol.nntp.core.StoreStore;
import org.eclipse.ecf.protocol.nntp.core.StringUtils;
import org.eclipse.ecf.protocol.nntp.model.IArticle;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerConnection;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.INNTPStore;
import org.eclipse.ecf.protocol.nntp.model.NNTPConnectException;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.protocol.nntp.model.NNTPIOException;
import org.eclipse.ecf.protocol.nntp.model.ArticleEvent;
import org.eclipse.ecf.protocol.nntp.model.SALVO;
import org.eclipse.ecf.protocol.nntp.model.StoreException;
import org.eclipse.ecf.protocol.nntp.model.UnexpectedResponseException;

public class NNTPServerStoreFacade implements INNTPServerStoreFacade {

	public NNTPServerStoreFacade() {
	}

	public void init() {
		// startUpdateThread();
	}

	public INNTPStore[] getStores() {
		return StoreStore.instance().getStores();
	}

	public boolean postArticle(IArticle article) {
		return false;
	}

	public IArticle getArticle(INewsgroup newsgroup, int articleId)
			throws NNTPIOException, UnexpectedResponseException, StoreException {

		IArticle article = null;
		for (int i = 0; i < getStores().length;) {
			article = getStores()[i].getArticle(newsgroup, articleId);
			break;
		}

		if (article == null) {
			return newsgroup.getServer().getServerConnection()
					.getArticle(newsgroup, articleId);
		}
		return article;
	}

	public Exception getLastException() {
		// FIXME remove
		throw new RuntimeException("not implemented");
	}

	public void catchUp(INewsgroup newsgroup) throws NNTPIOException {
		// FIXME implement
		throw new RuntimeException("not yet implemented");
	}

	public void unsubscribeNewsgroup(INewsgroup newsGroup, boolean permanent)
			throws StoreException {
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].unsubscribeNewsgroup(newsGroup, permanent);
		}
	}

	public boolean cancelArticle(IArticle article) {
		// TODO Auto-generated method stub
		return false;
	}

	public void subscribeNewsgroup(INewsgroup group) throws NNTPIOException,
			UnexpectedResponseException, StoreException {
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].subscribeNewsgroup(group);
		}
		syncStoreWithServer(group,false);
		//updateAttributes(group);
	}

	public void subscribeServer(INNTPServer server, String passWord)
			throws StoreException {
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].subscribeServer(server, passWord);
		}
	}

	public void unsubscribeServer(INNTPServer server, boolean permanent)
			throws StoreException {
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].unsubscribeServer(server, permanent);
		}
	}

	public void updateAttributes(INewsgroup newsgroup) throws NNTPIOException,
			UnexpectedResponseException, StoreException {
		try {
			newsgroup.getServer().getServerConnection()
					.setWaterMarks(newsgroup);
		} catch (NNTPConnectException e) {
			throw new NNTPIOException(e.getMessage(), e);
		}
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].updateAttributes(newsgroup);
		}
	}
	
	public void updateAttributesInStore(INewsgroup newsgroup) throws NNTPIOException, UnexpectedResponseException, StoreException {
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].updateAttributes(newsgroup);
		}
	}
	

	public INewsgroup[] getSubscribedNewsgroups(INNTPServer server)
			throws StoreException {
		for (int i = 0; i < getStores().length;) {
			return getStores()[i].getSubscribedNewsgroups(server);
		}
		return new INewsgroup[0];
	}

	// public IArticle[] getArticles(INewsgroup newsgroup, int from,
	// int to) throws NNTPIOException {
	//
	// try {
	// IServerConnection connection = newsgroup.getServer()
	// .getServerConnection();
	//
	// // Adjust for sanity
	// if ((to - from) > SALVO.BATCH_SIZE)
	// from = to - SALVO.BATCH_SIZE;
	//
	// // Check what is first in store
	// IStore firstStore = getFirstStore();
	// int firstInStore = 0;
	// if (firstStore != null
	// && firstStore.getFirstArticle(newsgroup) != null)
	// firstInStore = getFirstStore().getFirstArticle(newsgroup)
	// .getArticleNumber();
	//
	// // Fetch from the server what is not in store
	// if (firstInStore > 0 && firstInStore > from) {
	// IArticle[] result = connection.getArticles(newsgroup,
	// from, firstInStore);
	// if (result != null) {
	// for (int i = 0; i < stores.length; i++) {
	// stores[i].storeArticles(result);
	// }
	//
	// // Adjust the requested values
	// if (firstStore != null)
	// firstInStore = firstStore.getFirstArticle(newsgroup)
	// .getArticleNumber();
	// if (firstInStore > from)
	// from = firstInStore;
	// }
	//
	// // Check what is last in store
	// int lastInStore = 0;
	// if (firstStore != null
	// && firstStore.getLastArticle(newsgroup) != null)
	// lastInStore = firstStore.getLastArticle(newsgroup)
	// .getArticleNumber();
	//
	// // Fetch from the server what is not in store
	// if (lastInStore > 0 && lastInStore < to) {
	// result = connection.getArticles(newsgroup,
	// lastInStore, to);
	// if (result != null)
	// for (int i = 0; i < stores.length; i++) {
	// stores[i].storeArticles(result);
	//
	// // Adjust the requested values
	// if (firstStore != null)
	// lastInStore = firstStore.getLastArticle(newsgroup)
	// .getArticleNumber();
	// if (lastInStore < to)
	// to = lastInStore;
	// }
	//
	// result = null;
	// if (firstStore != null)
	// result = firstStore.getArticles(newsgroup, from, to);
	//
	// if (result == null) {
	// result = connection.getArticles(newsgroup, from, to);
	// if (result != null)
	// for (int i = 0; i < stores.length; i++) {
	// stores[i].storeArticles(result);
	// }
	// if (firstStore != null)
	// result = firstStore.getArticles(newsgroup, from, to);
	// }
	// if (result == null)
	// result = new IArticle[0];
	// return result;
	//
	// }}} catch (NNTPIOException e) {
	// throw new NNTPIOException(e.getMessage(), e);
	// }
	// }

	public IArticle[] getArticles(INewsgroup newsgroup, int from, int to)
			throws NNTPIOException, UnexpectedResponseException, StoreException {

		try {
			INNTPServerConnection connection = newsgroup.getServer()
					.getServerConnection();

			// Adjust for sanity
			if ((to - from) > SALVO.BATCH_SIZE)
				from = to - SALVO.BATCH_SIZE;

			// Check what is first in store
			INNTPStore firstStore = getFirstStore();
			int firstArticleInStore = 0;
			if (firstStore != null
					&& firstStore.getFirstArticle(newsgroup) != null)
				firstArticleInStore = getFirstStore()
						.getFirstArticle(newsgroup).getArticleNumber();

			// Fetch from the server what is not in store
			if (firstArticleInStore > 0 && firstArticleInStore > from) {
				IArticle[] result = connection.getArticles(newsgroup, from,
						firstArticleInStore);
				if (result != null) {
					for (int i = 0; i < getStores().length; i++) {
						getStores()[i].storeArticles(result);
					}
				}

				// Adjust the requested values
				if (firstStore != null)
					firstArticleInStore = firstStore.getFirstArticle(newsgroup)
							.getArticleNumber();
				if (firstArticleInStore > from)
					from = firstArticleInStore;
			}

			// Check what is last in store
			int lastArticleInStore = 0;
			if (firstStore != null
					&& firstStore.getLastArticle(newsgroup) != null)
				lastArticleInStore = firstStore.getLastArticle(newsgroup)
						.getArticleNumber();

			// Fetch from the server what is not in store
			if (lastArticleInStore > 0 && lastArticleInStore < to) {
				IArticle[] result = connection.getArticles(newsgroup,
						lastArticleInStore, to);
				if (result != null) {
					for (int i = 0; i < getStores().length; i++) {
						getStores()[i].storeArticles(result);
					}
				}
				// Adjust the requested values
				if (firstStore != null)
					lastArticleInStore = firstStore.getLastArticle(newsgroup)
							.getArticleNumber();
				if (lastArticleInStore < to)
					to = lastArticleInStore;
			}

			IArticle[] result = null;
			if (firstStore != null)
				result = firstStore.getArticles(newsgroup, from, to);

			if (result == null) {
				result = connection.getArticles(newsgroup, from, to);
				if (result != null) {
					for (int i = 0; i < getStores().length; i++) {
						getStores()[i].storeArticles(result);
					}
				}
				if (firstStore != null)
					result = firstStore.getArticles(newsgroup, from, to);
			}
			if (result == null)
				result = new IArticle[0];

			return result;

		} catch (NNTPConnectException e) {
			throw new NNTPIOException(e.getMessage(), e);
		}
	}

	public INNTPStore getFirstStore() {
		if (getStores().length > 0)
			return getStores()[0];
		return null;
	}

	// public String[] getBody(IArticle article) throws NNTPIOException,
	// UnexpectedResponseException {
	//
	// // FIXME Decide thru preference if article bodies should be stored in
	// // the store or always fetched from server?
	//
	// // Get From Store
	// String[] body = null;
	// if (getFirstStore() != null)
	// try {
	// body = getFirstStore().getArticleBody(article);
	// } catch (NNTPConnectException e) {
	// Debug.log(getClass(), e);
	// throw new NNTPIOException(e.getMessage(), e);
	// }
	//
	// // Not in store get from server
	// if (body == null) {
	// try {
	// body = article.getServer().getServerConnection()
	// .getArticleBody(article);
	// } catch (UnexpectedResponseException e) {
	// Debug.log(getClass(), e);
	// throw e;
	// }
	//
	// if (!(body == null)) {
	// for (int i = 0; i < stores.length; i++) {
	// stores[i].storeArticleBody(article, body);
	// }
	// }
	// }
	// return body;
	// }

	public IArticle[] getFollowUps(IArticle article) throws NNTPIOException,
			UnexpectedResponseException, StoreException {

		// FIXME Decide if article bodies should be stored in the store or
		// always fetched from server.
		IArticle[] result = null;

		if (getFirstStore() != null)
			Debug.log(getClass(), "Trying to Fetch articles from store...");

		result = getFirstStore().getFollowUps(article);

		if (result == null) {

			try {
				Debug.log(getClass(), "Trying to Fetch articles from server...");

				result = article.getServer().getServerConnection()
						.getFollowUps(article);
			} catch (NNTPConnectException e) {
				throw new NNTPIOException(e.getMessage(), e);
			}
			if (!(result == null)) {
				if (getFirstStore() != null)
					getFirstStore().storeArticles(result);
			}
		}
		return result;
	}

	// public IArticle[] getAllFollowUps(IArticle article)
	// throws NNTPIOException {
	//
	// // FIXME Decide if article bodies should be stored in the store or
	// // always fetched from server.
	// IArticle[] result2 = getFollowUps(article);
	// Collection result = new ArrayList<IArticle>(result2);
	// for (IArticle reply : result) {
	// Collection<IArticle> r2 = getAllFollowUps(reply);
	// result2.addAll(r2);
	// }
	// return result2;
	// }

	public IArticle[] getAllFollowUps(IArticle article) throws NNTPIOException,
			UnexpectedResponseException, StoreException {

		// FIXME Decide if article bodies should be stored in the store or
		// always fetched from server.
		ArrayList result2 = new ArrayList();
		result2.addAll(Arrays.asList(getFollowUps(article)));

		Collection result = new ArrayList(result2);
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			IArticle reply = (IArticle) iterator.next();
			Collection r2 = Arrays.asList(getAllFollowUps(reply));
			result2.addAll(r2);
		}
		return (IArticle[]) result2.toArray(new IArticle[0]);
	}

	public void updateArticle(IArticle article) throws StoreException {
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].updateArticle(article);
		}
	}

	public void replyToArticle(String replySubject, IArticle article, String body)
			throws NNTPIOException, UnexpectedResponseException, StoreException {
		article.getServer().getServerConnection().replyToArticle(replySubject, article, body);

		syncStoreWithServer(article.getNewsgroup(),true);
	}

	public void postNewArticle(INewsgroup[] newsgroups, String subject,
			String body) throws NNTPIOException, StoreException {

		try {
			INNTPServerConnection connection = newsgroups[0].getServer()
					.getServerConnection();
			connection.postNewArticle(newsgroups, subject, body);
			for (int i = 0; i < newsgroups.length; i++) {
				if (newsgroups[i].isSubscribed()) {
					syncStoreWithServer(newsgroups[i],true);
				}
			}
		} catch (UnexpectedResponseException e) {
			throw new NNTPIOException(e.getMessage(), e);
		}
	}

	public INewsgroup[] listNewsgroups(INNTPServer server) throws NNTPIOException,
			NNTPIOException, UnexpectedResponseException {
		return server.getServerConnection().listNewsgroups(server);
	}

	public INewsgroup[] listNewsgroups(INNTPServer server, Date since)
			throws NNTPIOException, UnexpectedResponseException {

		// FIXME implement
		throw new RuntimeException("not yet implemented");
		// return listNewsgroups(server);
	}

	public String[] getArticleBody(IArticle article) throws NNTPIOException,
			UnexpectedResponseException, StoreException {
		try {
			String[] articleBody = getStores()[0].getArticleBody(article);
			if (articleBody.length > 0)
				return articleBody;
		} catch (UnexpectedResponseException e) {
			// cannot happen
		}

		String[] articleBody = article.getServer().getServerConnection()
				.getArticleBody(article);
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].storeArticleBody(article, articleBody);
		}

		return articleBody;
	}

	public void setWaterMarks(INewsgroup newsgroup) throws NNTPIOException,
			UnexpectedResponseException, StoreException {
		newsgroup.getServer().getServerConnection().setWaterMarks(newsgroup);
		for (int i = 0; i < getStores().length; i++) {
			getStores()[i].setWaterMarks(newsgroup);
		}
	}

	public String[] getOverviewHeaders(INNTPServer server) throws NNTPIOException,
			UnexpectedResponseException {
		return server.getServerConnection().getOverviewHeaders(server);
	}

	public void setModeReader(INNTPServer server) throws NNTPIOException,
			UnexpectedResponseException {
		setModeReader(server);
	}

	public INNTPServer[] getServers() throws NNTPException {
		return getStores()[0].getServers();
	}

	public INewsgroup getSubscribedNewsgroup(INNTPServer server, String newsgroup) {
		// FIXME implement
		throw new RuntimeException("not yet implemented");
	}

	public IArticle getArticle(String URL) throws NNTPIOException,
			UnexpectedResponseException, NNTPException {

		int articleNumber;
		String newsgroup;
		String server;

		try {
			String[] split = StringUtils.split(URL, "/");
			server = split[2];
			split = StringUtils.split(split[split.length - 1], "?");
			articleNumber = Integer.parseInt(split[1]);
			newsgroup = split[0];
		} catch (Exception e) {
			throw new NNTPException("Error parsing URL " + URL, e);
		}

		INNTPServer[] servers = getServers();
		for (int i = 0; i < servers.length; i++) {
			if (servers[i].getAddress().equals(server)) {
				INewsgroup[] groups = getSubscribedNewsgroups(servers[i]);
				for (int j = 0; j < groups.length; j++) {
					if (groups[j].getNewsgroupName().equals(newsgroup))
						return getArticle(groups[j], articleNumber);
				}
				return null;
			}
		}

		return null;
	}

	public int purge(Calendar purgeDate, int number) throws NNTPIOException {
		INNTPStore[] stores = getStores();
		int result = 0;
		if (stores.length > 0)
			result = stores[0].purge(purgeDate, number);
		for (int i = 1; i < stores.length; i++) {
			stores[i].purge(purgeDate, number);
		}
		return result;
	}

	public int delete(IArticle article) throws NNTPIOException {
		INNTPStore[] stores = getStores();
		int result = 0;
		if (stores.length > 0)
			result = stores[0].delete(article);
		for (int i = 1; i < stores.length; i++) {
			stores[i].delete(article);
		}
		return result;
	}

	public IArticle[] getThisUserArticles(INewsgroup newsgroup) {
		return getFirstStore().getArticlesByUserId(newsgroup,
				newsgroup.getServer().getServerConnection().getFullUserName());
	}

	public IArticle[] getMarkedArticles(INewsgroup newsgroup) {
		return orderArticlesFromNewestFirst (getFirstStore().getMarkedArticles(newsgroup));
	}

	public IArticle[] getAllMarkedArticles(INNTPServer server) {
		return orderArticlesFromNewestFirst (getFirstStore().getAllMarkedArticles(server));
	}

	public IArticle getArticleByMsgId(INewsgroup newsgroup, String msgId) {

		return getFirstStore().getArticleByMsgId(newsgroup, msgId);

	}

	public IArticle getFirstArticleOfTread(IArticle article) {

		if (article.getLastReference() == null) {
			return article;
		} else {
			return getFirstArticleOfTread(getArticleByMsgId(
					article.getNewsgroup(), article.getLastReference()));
		}
	}

	public IArticle[] getFirstArticleOfThisUserThreads(INewsgroup newsgroup) {

		IArticle[] thisUserarticles = getThisUserArticles(newsgroup);

		ArrayList<IArticle> result = new ArrayList<IArticle>();

		for (IArticle thisUserArticle : thisUserarticles) {

			IArticle firstArticle = getFirstArticleOfTread(thisUserArticle);

			if (!result.contains(firstArticle)) { // prevent duplicates
				result.add(firstArticle);
			}

		}
		
		IArticle[] unorderedArticles = (IArticle[]) result.toArray(new IArticle[0]);
		return orderArticlesFromNewestFirst(unorderedArticles); 
	}
	
	public int getLastReplyArticleNumber(IArticle article){
		int lastArticleNumber = article.getArticleNumber();
		
		try {
			IArticle[] followups = getAllFollowUps(article);
		
			if (followups.length == 0) { // For no reply
				return lastArticleNumber;			
			}
			
			for (IArticle followupArticle : followups) {
				if (followupArticle.getArticleNumber() > lastArticleNumber) {
					lastArticleNumber = followupArticle.getArticleNumber();
				}
			}
		} catch (NNTPException e) {
			Debug.log(getClass(), e);
		}
		
		return lastArticleNumber;
	}
	
	public IArticle[] orderArticlesFromNewestFirst(IArticle[] articles) {

		Map<Integer, IArticle> unorderedArticles = new HashMap<Integer, IArticle>();
		Integer[] lastArticleNumbers = new Integer[articles.length];

		for (int i = 0; i < articles.length; i++) {
			int articleNumber = getLastReplyArticleNumber(articles[i]);
			unorderedArticles.put(articleNumber, articles[i]);
			lastArticleNumbers[i] = articleNumber;
		}

		Arrays.sort(lastArticleNumbers, Collections.reverseOrder());
		IArticle[] orderedArticles = new IArticle[articles.length];

		for (int i = 0; i < articles.length; i++) {
			orderedArticles[i] = unorderedArticles.get(lastArticleNumbers[i]);
		}

		return orderedArticles;
	}
	
	public int getStoreHighWatermark (INewsgroup newsgroup) {
		return newsgroup.getHighWaterMark();
	}
	
	public int[] getServerWatermarks (INewsgroup newsgroup) {
		try {
			return newsgroup.getServer().getServerConnection().getWaterMarks(newsgroup);
		} catch (NNTPException e) {
			Debug.log(getClass(), e);
			return null;
		}
	}
	
	public void syncStoreWithServer(INewsgroup newsgroup, boolean isNewArticlePosted) throws NNTPIOException, UnexpectedResponseException, StoreException {
		
		int storeHighWatermark = getStoreHighWatermark(newsgroup);	
		int serverHighWatermark = getServerWatermarks(newsgroup)[2];
		
		IArticle[] articles = null;
		
		if (storeHighWatermark < serverHighWatermark) {
			articles = getArticles(newsgroup, storeHighWatermark+1, serverHighWatermark);
		}
		updateAttributesInStore(newsgroup);
		
		if (articles != null) {
			if (isNewArticlePosted && articles.length == 1) {
				ArticleEventListnersFactory.instance().getRegistry().fireEvent(new ArticleEvent(articles,false));
			} else {
				ArticleEventListnersFactory.instance().getRegistry().fireEvent(new ArticleEvent(articles,true));
			}
		} 
		
	}

	public void catchUp(IMessageSource source) throws Exception {
		catchUp((INewsgroup)source);
		
	}

	public void updateMessage(IMessage message) throws Exception {
		updateArticle((IArticle)message);		
	}

	public IMessage getMessageByMsgId(IMessageSource source, String msgId) {
		
		return getArticleByMsgId((INewsgroup)source, msgId);
	}

	public IMessage getFirstMessageOfTread(IMessage message) {
		return getFirstArticleOfTread((IArticle)message);
	}

	public IMessage[] orderMessagesesFromNewestFirst(IMessage[] messages) {
			return orderArticlesFromNewestFirst((IArticle[])messages);
	}

	public void syncStoreWithServer(IMessageSource messageSource,
			boolean isNewMessagePosted) throws Exception {
		syncStoreWithServer((INewsgroup)messageSource, isNewMessagePosted);
		
	}

}
