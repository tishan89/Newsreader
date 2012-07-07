package org.eclipse.ecf.protocol.nntp.store.filesystem;

import org.eclipse.ecf.protocol.nntp.model.INNTPStore;
import org.eclipse.ecf.protocol.nntp.model.INNTPStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.SALVO;
import org.eclipse.ecf.protocol.nntp.model.StoreException;
import org.eclipse.ecf.protocol.nntp.store.filesystem.internal.Store;

/**
 * The store factory will create {@link INNTPStore} implementations.
 * 
 * @author Wim Jongman
 * 
 */
public class StoreFactory implements INNTPStoreFactory {

	private INNTPStore store;

	public INNTPStore createStore(String root) {
		if (store == null)
			store = new Store(root + SALVO.SEPARATOR + "SalvoFilesystemStore");
		return store;
	}

	public void deleteStore() throws StoreException {
		throw new StoreException("not implemented ..");		
	}
}
