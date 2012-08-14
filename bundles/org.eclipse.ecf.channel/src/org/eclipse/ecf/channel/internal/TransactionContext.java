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
package org.eclipse.ecf.channel.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.channel.ITransactionContext;
import org.eclipse.ecf.core.security.CallbackHandler;

public class TransactionContext implements ITransactionContext {

	private Map<String, String> context = new HashMap<String, String>();

	public CallbackHandler getCallbackHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public void set(String key, String value) {
		context.put(key, value);

	}

	public String get(String key) {
		return context.get(key);
	}

}
