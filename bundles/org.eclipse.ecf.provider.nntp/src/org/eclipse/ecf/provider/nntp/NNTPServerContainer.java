/*******************************************************************************
 *  Copyright (c) 2012 University of Moratuwa
 *                                                                      
 * All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html                            
 *                                                                      
 * Contributors:
 * 	  Wim Jongman                                                    
 *    Tishan Dahanayakage 
 *******************************************************************************/
package org.eclipse.ecf.provider.nntp;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.ecf.core.AbstractContainer;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.events.ContainerConnectedEvent;
import org.eclipse.ecf.core.events.ContainerConnectingEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectingEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerFactory;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.IArticle;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.protocol.nntp.model.NNTPIOException;
import org.eclipse.ecf.protocol.nntp.model.StoreException;
import org.eclipse.ecf.protocol.nntp.model.UnexpectedResponseException;
import org.eclipse.ecf.provider.nntp.internal.HookedNewsgroupProvider;
import org.eclipse.ecf.provider.nntp.internal.NNTPNameSpace;
import org.eclipse.ecf.channel.IChannelContainerAdapter;
import org.eclipse.ecf.channel.ITransactionContext;
import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.channel.model.AbstractCredentials;
import org.eclipse.ecf.channel.model.IMessage;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.channel.model.IServer;
import org.eclipse.ecf.channel.provider.IMessageSourceProvider;

public class NNTPServerContainer extends AbstractContainer implements
		IChannelContainerAdapter {

	private ID targetID;
	private ID containerID;
	private INNTPServer server;
	private IConnectContext context;	
	private ArrayList<INewsgroup> subscribedNewsGroups;

	protected NNTPServerContainer(ID id) {
		super();
		this.containerID = id;
	}

	public Namespace getConnectNamespace() {
		return IDFactory.getDefault().getNamespaceByName(NNTPNameSpace.NAME);
	}

	public void connect(ID targetID, IConnectContext connectContext)
			throws ContainerConnectException {
		if (!targetID.getNamespace().getName()
				.equals(getConnectNamespace().getName()))
			throw new ContainerConnectException(
					"targetID not of appropriate Namespace");

		fireContainerEvent(new ContainerConnectingEvent(getID(), targetID));	
		INNTPServerStoreFacade serverStoreFacade = NNTPServerStoreFactory
				.instance().getServerStoreFacade();
		try {
			serverStoreFacade.subscribeServer(this.getServer(),
					((ITransactionContext) connectContext).get("pWord"));
		} catch (StoreException e) {			
			e.printStackTrace();
		}

		this.targetID = targetID;
		fireContainerEvent(new ContainerConnectedEvent(getID(), targetID));
	}

	public void disconnect() {
		fireContainerEvent(new ContainerDisconnectingEvent(getID(), targetID));
		final ID oldID = targetID;
		// XXX disconnect here
		fireContainerEvent(new ContainerDisconnectedEvent(getID(), oldID));
	}

	public ID getConnectedID() {
		return targetID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIdentifiable#getID()
	 */
	public ID getID() {
		return containerID;
	}

	public void setServer(IServer server) throws NNTPException {
		AbstractCredentials credentials = (AbstractCredentials) server
				.getServerConnection().getCredentials();
		this.server = NNTPServerFactory.getCreateServer(server.getAddress(),
				server.getPort(), credentials, server.isSecure());

	}

	public INNTPServer getServer() {
		return server;
	}	

	public void reply(IMessage message, ITransactionContext context)
			throws NNTPIOException, UnexpectedResponseException, StoreException {
		INNTPServerStoreFacade serverStoreFacade = NNTPServerStoreFactory
				.instance().getServerStoreFacade();
		serverStoreFacade.replyToArticle(context.get("subject"),
				(IArticle) message, context.get("body"));

	}	

	public void connectToMessageSource(IMessageSource[] sources)
			throws NNTPException {
		INNTPServerStoreFacade serverStoreFacade = NNTPServerStoreFactory
				.instance().getServerStoreFacade();
		INewsgroup[] newsgroup = (INewsgroup[]) sources;
		for (INewsgroup group : newsgroup) {
			serverStoreFacade.subscribeNewsgroup(group);
		}
	}

	public void connectToMessageSource(Collection<IMessageSource> collection)
			throws NNTPException {
		INNTPServerStoreFacade serverStoreFacade = NNTPServerStoreFactory
				.instance().getServerStoreFacade();
		ArrayList<INewsgroup> newsgroups = new ArrayList<INewsgroup>();
		for (IMessageSource source : collection) {
			newsgroups.add((INewsgroup) source);
		}

		for (INewsgroup group : newsgroups) {
			serverStoreFacade.subscribeNewsgroup(group);

		}
	}
	

	public ArrayList<IMessageSource> fetchSubcribedMessageSources() {
		subscribedNewsGroups = new ArrayList<INewsgroup>();

		try {
			for (INNTPServer server : NNTPServerStoreFactory.instance()
					.getServerStoreFacade().getFirstStore().getServers()) {

				INewsgroup[] groups = NNTPServerStoreFactory.instance()
						.getServerStoreFacade().getSubscribedNewsgroups(server);

				for (INewsgroup group : groups) {

					if (!subscribedNewsGroups.contains(group)) {
						subscribedNewsGroups.add(group);
					}
				}

			}
		} catch (NNTPException e) {
			Debug.log(this.getClass(), e);
		}

		ArrayList<IMessageSource> result = new ArrayList<IMessageSource>();
		for (INewsgroup group : subscribedNewsGroups) {
			result.add((IMessageSource) group);
		}
		return result;
	}

	public IMessageSourceProvider[] getProviders() {
		return HookedNewsgroupProvider.instance().getProviders();

	}

	public IMessageSource getMessageSource(IMessageSourceProvider provider) {

		return HookedNewsgroupProvider.instance().getNewsgroup(provider);
	}

	public void postNewMessages(IMessageSource[] source, IMessage message,
			ITransactionContext context) throws Exception {
		INNTPServerStoreFacade serverStoreFacade = NNTPServerStoreFactory
				.instance().getServerStoreFacade();

		serverStoreFacade.postNewArticle((INewsgroup[]) source,
				context.get("subject"), context.get("body                 "));

	}

	public void subscribeMessageSource(IMessageSource group)
			throws NNTPIOException, UnexpectedResponseException, StoreException {
		INNTPServerStoreFacade serverStoreFacade = NNTPServerStoreFactory
				.instance().getServerStoreFacade();
		serverStoreFacade.subscribeNewsgroup((INewsgroup) group);

	}

	public IMessage[] fetchFollowups(IMessage message) throws NNTPIOException,
			UnexpectedResponseException, StoreException {

		return NNTPServerStoreFactory.instance().getServerStoreFacade()
				.getAllFollowUps((IArticle) message);
	}

	public void updateMessage(IMessage message, ITransactionContext context)
			throws Exception {
		NNTPServerStoreFactory.instance().getServerStoreFacade()
				.updateArticle((IArticle) message);

	}
	public IMessage fetchMessage(IMessageSource source, String Id) {
		// TODO Auto-generated method stub
		return null;
	}
	public IMessage[] fetchMessages(ITransactionContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IMessageSource[] listMessageSources(INNTPServer server)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void delete(ITransactionContext context, String messageId)
			throws Exception {
		// TODO Auto-generated method stub

	}
	public IMessageSource[] listMessageSources(IServer server) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
