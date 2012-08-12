package org.eclipse.ecf.channel.core;

import org.eclipse.ecf.core.IContainerManager;

public interface ISalvoUtil {
	
	/**
	 * Implement this method to get a default instance of SalvoUtil.
	 * Default constructor should be overridden to initialize
	 * a local instance of IContainerManager
	 * @return Default SalvoUtil instance
	 */
	public SalvoUtil getDefault();
	
	/**
	 * Local IContainerManage instance should be initialized 
	 * before using this method.
	 * @return IContainerManager Reference
	 */
	public IContainerManager getContainerManager();
}
