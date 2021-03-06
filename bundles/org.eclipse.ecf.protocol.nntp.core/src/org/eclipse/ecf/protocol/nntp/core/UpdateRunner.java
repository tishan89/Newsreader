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
package org.eclipse.ecf.protocol.nntp.core;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.INNTPStore;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.protocol.nntp.model.StoreException;
import org.osgi.service.prefs.Preferences;

public final class UpdateRunner implements Runnable {
	private boolean threadRunning;

	public void run() {

		setThreadRunning(true);

		INNTPServerStoreFacade facade = NNTPServerStoreFactory.instance()
				.getServerStoreFacade();
		while (facade.getStores().length == 0 && isThreadRunning()) {
			facade = NNTPServerStoreFactory.instance().getServerStoreFacade();
			try {
				Debug.log(getClass(), "Salvo Thread: Waiting for Store");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				setThreadRunning(false);
				break;
			}
		}

		while (isThreadRunning()) {
			INNTPServer[] subscribedServers = new INNTPServer[0];
			INNTPStore[] stores = facade.getStores();
			for (int i = 0; i < stores.length; i++) {
				INNTPStore store = stores[i];
				subscribedServers = getSubscribedServers(store);
				for (int j = 0; j < subscribedServers.length; j++) {
					INNTPServer server = subscribedServers[j];
					INewsgroup[] subscribedNewsgroups;
					try {
						subscribedNewsgroups = facade
								.getSubscribedNewsgroups(server);
						for (int k = 0; k < subscribedNewsgroups.length; k++) {
							INewsgroup group = subscribedNewsgroups[k];
							try {
								facade.syncStoreWithServer(group,false);
							} catch (Exception e) {
								Debug.log(this.getClass(), e);
							}
						}
					} catch (StoreException e1) {
						Debug.log(this.getClass(), e1);
					}
					server.setDirty(false);
				}
			}
			try {
				
				int syncInterval = getSyncInterval();
				Debug.log(getClass(),
						"Salvo Thread: Update finished sleeping for "+syncInterval+" seconds");
				
				for (int i = 0; i < syncInterval; i++)
					if (isThreadRunning() && serversClean(subscribedServers))
						Thread.sleep(1000);
					else
						break;
			} catch (InterruptedException e) {
				setThreadRunning(false);
				break;
			}
		}
	}

	private INNTPServer[] getSubscribedServers(INNTPStore store) {
		try {
			return store.getServers();
		} catch (NNTPException e) {
			Debug.log(getClass(), e);
			return new INNTPServer[0];
		}
	}

	private boolean serversClean(INNTPServer[] subscribedServers) {

		for (int j = 0; j < subscribedServers.length; j++) {
			INNTPServer server = subscribedServers[j];
			if (server.isDirty())
				return false;
		}
		return true;
	}

	private void setThreadRunning(boolean threadRunning) {
		this.threadRunning = threadRunning;
	}

	public boolean isThreadRunning() {
		return threadRunning;
	}

	public void stop() {
		if (isThreadRunning())
			setThreadRunning(false);
	}

	public void start() {
		if (!isThreadRunning())
			new Thread(this, "Salvo newsreader update thread").start();
	}
	
	/*
	 * Get the synchronization time
	 */
	private int getSyncInterval() {
		Preferences prefs = new InstanceScope()
				.getNode("org.eclipse.ecf.protocol.nntp.core");
		return prefs.getInt("syncinterval", 300);
	}
	
}