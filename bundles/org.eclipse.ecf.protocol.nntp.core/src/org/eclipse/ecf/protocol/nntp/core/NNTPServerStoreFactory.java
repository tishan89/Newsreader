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

import org.eclipse.ecf.protocol.nntp.core.internal.NNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;


public class NNTPServerStoreFactory {

	private static NNTPServerStoreFactory factory;

	private INNTPServerStoreFacade facade;

	public INNTPServerStoreFacade getServerStoreFacade() {
		
		if (facade == null) {
			facade = new NNTPServerStoreFacade();
			facade.init();
		}
		return facade;
	}

	public static NNTPServerStoreFactory instance() {
		if (factory == null)
			factory = new NNTPServerStoreFactory();
		return factory;
	}

}
