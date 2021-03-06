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

import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.salvo.ui.internal.resources.ISalvoResource;
import org.eclipse.ecf.salvo.ui.internal.resources.SalvoResourceFactory;

public class SubscribedServerProvider implements IChildProvider {

	public Collection<ISalvoResource> getChildren() {

		ArrayList<ISalvoResource> result = new ArrayList<ISalvoResource>();

		if (NNTPServerStoreFactory.instance().getServerStoreFacade()
				.getFirstStore() == null) {
			Debug.log(this.getClass(), "No stores defined");
			ISalvoResource er = SalvoResourceFactory.getResource("No store service is running", "No store service is running");
			ArrayList<ISalvoResource> ar = new ArrayList<ISalvoResource>();
			ar.add(er);
			return ar;
		}

		try {
			for (INNTPServer server : NNTPServerStoreFactory.instance()
					.getServerStoreFacade().getFirstStore()
					.getServers()) {
				ISalvoResource s1 = SalvoResourceFactory.getResource(
						server.getAddress(), server);
				s1.setChildProvider(new SubscribedNewsGroupProvider(s1));
				result.add(s1);
			}
			return result;
		} catch (NNTPException e) {
			Debug.log(this.getClass(), e);
			ISalvoResource er = SalvoResourceFactory.getResource(e.getMessage(), e);
			ArrayList<ISalvoResource> ar = new ArrayList<ISalvoResource>();
			ar.add(er);
			return ar;
		}
	}

	public ISalvoResource getParent() {
		return null;
	}

}
