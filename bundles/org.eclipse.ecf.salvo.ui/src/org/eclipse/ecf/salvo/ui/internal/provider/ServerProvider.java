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
package org.eclipse.ecf.salvo.ui.internal.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.salvo.ui.internal.resources.ISalvoResource;
import org.eclipse.ecf.salvo.ui.internal.resources.SalvoResourceFactory;

public class ServerProvider implements IChildProvider {

	public Collection<ISalvoResource> getChildren() {

		ArrayList<ISalvoResource> result = new ArrayList<ISalvoResource>();

		try {
			for (INNTPServer server : NNTPServerStoreFactory.instance()
					.getServerStoreFacade().getFirstStore()
					.getServers()) {
				ISalvoResource s1 = SalvoResourceFactory.getResource(server
						.getAddress(), server);
				s1.setChildProvider(new NewsGroupProvider(s1));
				result.add(s1);
			}
			return result;
		} catch (NNTPException e) {
			return new ArrayList<ISalvoResource>();
		}
	}

	public ISalvoResource getParent() {
		return null;
	}

}
