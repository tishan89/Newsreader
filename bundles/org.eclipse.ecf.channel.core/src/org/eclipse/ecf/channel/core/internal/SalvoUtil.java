package org.eclipse.ecf.channel.core.internal;

import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.IContainerManagerListener;
import org.eclipse.ecf.core.identity.ID;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class SalvoUtil {

	private ServiceTracker containerManagerTracker;
	private IContainerManager containerManager;
	private SalvoUtil INSTANCE;
	private BundleContext context;
	
	public SalvoUtil(){
		if (containerManagerTracker == null) {
			containerManagerTracker = new ServiceTracker(context, IContainerManager.class.getName(), null);
			containerManagerTracker.open();
		}
		containerManager = (IContainerManager) containerManagerTracker.getService();
		
		
	}
	
	public SalvoUtil getDefault(){
		if(INSTANCE==null){
			INSTANCE = new SalvoUtil();
		}
		return INSTANCE;
	}
	
	public IContainerManager getContainerManager(){
		return containerManager;
	}

}
