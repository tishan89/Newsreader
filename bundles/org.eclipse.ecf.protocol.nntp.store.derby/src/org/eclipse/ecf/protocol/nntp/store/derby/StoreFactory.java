/*******************************************************************************
 *  Copyright (c) 2010 Weltevree Beheer BV, Remain Software & Industrial-TSI
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *     Wim Jongman - initial API and implementation
 *
 *
 *******************************************************************************/
package org.eclipse.ecf.protocol.nntp.store.derby;

import org.eclipse.ecf.protocol.nntp.model.INNTPStore;
import org.eclipse.ecf.protocol.nntp.model.INNTPStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.SALVO;
import org.eclipse.ecf.protocol.nntp.model.StoreException;
import org.eclipse.ecf.protocol.nntp.store.derby.internal.Store;

/**
 * The store factory will create {@link INNTPStore} implementations.
 * 
 * @author Wim Jongman
 * 
 */
public class StoreFactory implements INNTPStoreFactory {

	private Store store;

	public INNTPStore createStore(String root) throws StoreException {
		if (store == null)
			store = new Store(root + SALVO.SEPARATOR + "SalvoDerbyStore");
		return store;
	}

	@Override
	public void deleteStore() throws StoreException {
		if (store == null)
			throw new StoreException("Cannot delete. Store is null.");

		store.dropDB();

	}

}
