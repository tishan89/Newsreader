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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.ecf.channel.model.IStoreEvent;
import org.eclipse.ecf.channel.model.IStoreEventListener;
import org.eclipse.ecf.channel.model.IStoreEventProvider;
import org.eclipse.ecf.channel.model.StoreEvent;
import org.eclipse.ecf.protocol.nntp.model.INNTPStore;
import org.eclipse.ecf.protocol.nntp.model.SALVO;

public class StoreStore implements IStoreEventProvider {

	private static StoreStore factory;

	private HashMap stores = new HashMap();

	private HashMap listeners;

	public void registerStore(INNTPStore store) {
		stores.put(store.getDescription(), store);
		fireEvent(new StoreEvent(store, SALVO.EVENT_REGISTER_STORE));
	}

	public void unregisterStore(INNTPStore store) {
		stores.remove(store.getDescription());
		fireEvent(new StoreEvent(store, SALVO.EVENT_UNREGISTER_STORE));
	}

	public INNTPStore[] getStores() {
		return (INNTPStore[]) stores.values().toArray(new INNTPStore[0]);
	}

	public static StoreStore instance() {
		if (factory == null)
			factory = new StoreStore();
		return factory;
	}

	public void removeListener(IStoreEventListener listener) {
		if (listeners == null || listeners.isEmpty())
			return;

		Set keySet = listeners.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			Integer key = (Integer) iterator.next();
			ArrayList list = (ArrayList) listeners.get(key);
			if (list.contains(listener)) {
				list.remove(listener);
				if (list.isEmpty()) {
					listeners.remove(key);
				}
			}
		}
	}

	public void addListener(IStoreEventListener listener, int eventType) {

		if (listeners == null) {
			listeners = new HashMap();
		}

		synchronized (listeners) {

			ArrayList list = (ArrayList) listeners.get(new Integer(eventType));
			if (list == null) {
				list = new ArrayList();
				listeners.put(new Integer(eventType), list);
			}
			if (!list.contains(listener))
				list.add(listener);
		}
	}

	public int getListenerCount() {
		if (listeners == null)
			return 0;
		return listeners.size();
	}

	private void fireEvent(IStoreEvent event) {

		if (listeners == null || listeners.isEmpty())
			return;

		synchronized (listeners) {
			Set keys = listeners.keySet();
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				Integer key = (Integer) iterator.next();
				if ((event.getEventType() | key.intValue()) == key.intValue()) {
					Debug.log(getClass(), "Listeners found for event type "
							+ key.intValue() + " (" + event.getEventType()
							+ "): ");

					ArrayList list = (ArrayList) listeners.get(key);
					for (Iterator iterator2 = list.iterator(); iterator2
							.hasNext();) {
						IStoreEventListener listener = (IStoreEventListener) iterator2
								.next();
						Debug.log(getClass(), "Calling Listener "
								+ listener.getClass().getName());
						listener.storeEvent(event);
					}
				}
			}
		}
	}
}
