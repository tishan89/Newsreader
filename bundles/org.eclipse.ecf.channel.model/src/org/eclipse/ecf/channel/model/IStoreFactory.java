package org.eclipse.ecf.channel.model;

/**
 * Store factories create IStore objects which are used to save the news
 * information.
 * 
 */
public interface IStoreFactory {

	/**
	 * This factory method creates a IStore. If there needs to be something
	 * written to the filesystem then the client suggests that it be at this
	 * root location. The factory must create a place inside here, e.g. a
	 * directory, where it will save the data. If the data does not need to be
	 * stored locally then the hint can be ignored.
	 * 
	 * @param rootDirectoryHint
	 * @return the store
	 */
	
	public IStore createStore(String rootDirectoryHint) throws Exception;
	
	public void deleteStore() throws Exception;
}
