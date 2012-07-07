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
 *******************************************************************************/
package org.eclipse.ecf.protocol.nntp.model;

import org.eclipse.ecf.channel.model.IStoreFactory;

/**
 * Store factories create IStore objects which are used to save the news
 * information.
 * 
 */
public interface INNTPStoreFactory extends IStoreFactory{

	/**
	 * This factory method creates a INNTPStore. If there needs to be something
	 * written to the filesystem then the client suggests that it be at this
	 * root location. The factory must create a place inside here, e.g. a
	 * directory, where it will save the data. If the data does not need to be
	 * stored locally then the hint can be ignored.
	 * 
	 * @param rootDirectoryHint
	 * @return the store
	 */
	public INNTPStore createStore(String rootDirectoryHint) throws StoreException;

	public void deleteStore() throws StoreException;

}
