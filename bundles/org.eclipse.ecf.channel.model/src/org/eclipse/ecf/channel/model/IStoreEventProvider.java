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
package org.eclipse.ecf.channel.model;


public interface IStoreEventProvider {

	/**
	 * Removes the listener from the list of listeners.
	 * 
	 * @param listener
	 */
	public void removeListener(IStoreEventListener listener);

	/**
	 * Adds the listener to the list of listeners that get notified if an event
	 * of the specified type occurs.
	 * 
	 * @param listener
	 * @param eventType
	 */
	public void addListener(IStoreEventListener listener, int eventType);

	/**
	 * Gets the number of listeners. Rather then exposing the list of listeners
	 * here is a count of the listeners. This is really a method that was
	 * inserted for testing.
	 * 
	 */
	public int getListenerCount();

}
