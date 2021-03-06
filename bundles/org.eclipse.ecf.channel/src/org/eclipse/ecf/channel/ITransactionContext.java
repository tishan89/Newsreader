/*******************************************************************************
 *  Copyright (c) 2012 University of Moratuwa
 *                                                                      
 * All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html                            
 *                                                                      
 * Contributors:                                                        
 *    Tishan Dahanayakage - initial API 
 *******************************************************************************/
package org.eclipse.ecf.channel;

import org.eclipse.ecf.core.security.IConnectContext;

/**
 * Interface to be implemented for contexts that should be passed get
 * operations(Eg: reply, post new, connect) done.
 * 
 */
public interface ITransactionContext extends IConnectContext {

	/**
	 * Method to set key value pairs.(Eg: Password, Username, etc)
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            String
	 */
	public void set(String key, String value);

	/**
	 * Method to get value of a given key.
	 * 
	 * @param key
	 *            String
	 * @return Value of the given keys
	 */
	public String get(String key);
}
