/* *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.ecf.channel.core;

import org.eclipse.ecf.core.IContainerManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.framework.FrameworkUtil;

/**
 * Class to get needed Salvo utilities at runtime. Use FrameworkUtils to get
 * this class and then get the relevant utilities by using this class.
 * 
 */
public class SalvoUtil implements ISalvoUtil {

	private ServiceTracker containerManagerTracker;
	private IContainerManager containerManager;
	private SalvoUtil INSTANCE;
	private BundleContext context;
	private Bundle containerManagerBundle;

	/**
	 * Constructor for SalvoUtil. Constructor is called once a new instance is
	 * called. Constructor will initialize IContainerManager and make it usable
	 * for others.
	 */
	public SalvoUtil() {
		if (containerManagerTracker == null) {

			context = FrameworkUtil.getBundle(this.getClass())
					.getBundleContext();
			ServiceReference reference = context
					.getServiceReference(IContainerManager.class.getName());
			containerManager = (IContainerManager) context
					.getService(reference);
		}
	}

	/**
	 * @return Default SalvoUtil instance
	 */
	public SalvoUtil getDefault() {
		if (INSTANCE == null) {
			INSTANCE = new SalvoUtil();
		}
		return INSTANCE;
	}

	/**
	 * This method returns the ICOntainerManager which can be used to manage and
	 * share IContainers.
	 * 
	 * @return IContainerManager
	 */
	public IContainerManager getContainerManager() {
		if(containerManager != null)
		return containerManager;
		return null;
	}

}
