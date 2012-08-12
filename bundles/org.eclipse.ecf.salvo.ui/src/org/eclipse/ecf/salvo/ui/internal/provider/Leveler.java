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
import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.channel.model.IServer;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;


public class Leveler {

	private HashMap<String, Leveler> serverLevelers = new HashMap<String, Leveler>();

	private HashMap<String, Leveler> groupLevelers = new HashMap<String, Leveler>();
	private HashMap<String, IMessageSource> groups = new HashMap<String, IMessageSource>();

	private final Leveler parent;

	private String levelText;

	private final String server;

	public Leveler(Leveler parent) {
		if (parent != null)
			server = parent.server;
		else
			server = null;
		this.parent = parent;
	}

	public Leveler(String server) {
		this.server = server;
		parent = null;
	}

	public void storeGroup(INNTPServer server, INewsgroup group) {
		Leveler serverLeveler = getServerLeveler(server);
		serverLeveler.storeDeep(null, group, 1);

	}

	/**
	 * This stores the newsgroup inside its correct location. Given the
	 * newsgroup alt.group.subject we first determine how many levels are in the
	 * group. In this case there are 3.
	 * 
	 * In this method, we start with level 1. The first level "alt" is retrieved
	 * and the leveler for "alt" is fetched or created. Then we check if the
	 * level is the same as the levels in the group. This is not the case, so
	 * the group is passed to this new leveler with an incremented level of 2.
	 * The sequence repeats until the number of levels is exhausted in which
	 * case the group is stored in the leveler.
	 * 
	 * @param group
	 * @param level
	 */
	private void storeDeep(Leveler parent, IMessageSource group, int level) {

		StringTokenizer tizer = new StringTokenizer(group.getMessageSourceName(), ".");
		int tokens = tizer.countTokens();
		if (tokens < level) {
			return;
		}

		if (level == tokens) {
			groups.put(group.getMessageSourceName(), group);
		} else {

			Leveler leveler = groupLevelers.get(getLevelText(group, level));
			if (leveler == null) {
				leveler = new Leveler(this);
				leveler.setLevelText(group, level);
				groupLevelers.put(leveler.getLevelText(), leveler);
				IMessageSource topGroup = groups.get(leveler.getLevelText());
				if (topGroup != null) {
					leveler.putGroup(topGroup);
					groups.remove(topGroup.getMessageSourceName());
				}
			}
			leveler.storeDeep(this, group, level + 1);
		}
	}

	private void putGroup(IMessageSource group) {
		groups.put(group.getMessageSourceName(), group);
	}

	protected void removeGroup(IMessageSource group) {
		groups.remove(group.getMessageSourceName());
	}

	private String getLevelText(IMessageSource group, int level) {
		StringTokenizer tizer = new StringTokenizer(group.getMessageSourceName(), ".");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < level; i++) {
			if (i > 0) {
				builder.append(".");
			}
			builder.append(tizer.nextToken());
		}

		return builder.toString();
	}

	public String getLevelText() {
		return levelText;
	}

	private void setLevelText(IMessageSource group, int level) {
		this.levelText = getLevelText(group, level);
	}

	private Leveler getServerLeveler(IServer server) {
		String serverKey = server.getAddress() + ":" + server.getPort();
		Leveler serverLeveler = serverLevelers.get(serverKey);
		if (serverLeveler == null) {
			serverLeveler = new Leveler((Leveler) null);
			serverLevelers.put(serverKey, serverLeveler);
		}
		return serverLeveler;
	}

	public Leveler getParent() {
		return parent;
	}

	public Collection<Object> getChildren(IServer server) {

		ArrayList<Object> result = new ArrayList<Object>();
		result.addAll(getServerLeveler(server).getLevelers());
		result.addAll(getServerLeveler(server).getMessageSource());
		return result;
	}

	private Collection<IMessageSource> getMessageSource() {
		return groups.values();
	}

	private Collection<Leveler> getLevelers() {
		return groupLevelers.values();
	}

	public Collection<Object> getChildren() {
		ArrayList<Object> result = new ArrayList<Object>();
		result.addAll(getLevelers());
		result.addAll(getMessageSource());
		return result;
	}
}
