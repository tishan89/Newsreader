/*******************************************************************************
 *  Copyright (c) 2011 University Of Moratuwa
 *                                                                      
 * All rights reserved. This program and the accompanying materials     
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at             
 * http://www.eclipse.org/legal/epl-v10.html                            
 *                                                                      
 * Contributors:                                                        
 *    Isuru Udana - UI Integration in the Workbench
 *******************************************************************************/
package org.eclipse.ecf.salvo.ui.internal.views.digest;

import java.util.ArrayList;

import org.eclipse.ecf.channel.core.Debug;
import org.eclipse.ecf.protocol.nntp.core.NNTPServerStoreFactory;
import org.eclipse.ecf.protocol.nntp.model.IArticle;
import org.eclipse.ecf.protocol.nntp.model.INewsgroup;
import org.eclipse.ecf.protocol.nntp.model.INNTPServer;
import org.eclipse.ecf.protocol.nntp.model.INNTPServerStoreFacade;
import org.eclipse.ecf.protocol.nntp.model.NNTPException;
import org.eclipse.ecf.protocol.nntp.model.StoreException;
import org.eclipse.ecf.salvo.ui.internal.resources.ISalvoResource;
import org.eclipse.ecf.salvo.ui.internal.resources.SalvoResourceFactory;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * This provides the ThisUserArticles for Digest View tree
 * 
 * @author isuru
 * 
 */
class ThisUserArticlesContentProvider implements ILazyTreeContentProvider {

	private TreeViewer viewer;
	private INNTPServerStoreFacade serverStoreFacade;
	private INewsgroup[] newsgroups;
	

	public ThisUserArticlesContentProvider(TreeViewer viewer) {
		this.viewer = viewer;
		serverStoreFacade = NNTPServerStoreFactory.instance()
				.getServerStoreFacade();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		newsgroups = getNotEmptyNewsgroups((INNTPServer) newInput);
	}
	
	/**
	 * Get not empty newsgroups
	 * @return not empty newsgroups
	 */
	private INewsgroup[] getNotEmptyNewsgroups(INNTPServer server){
		
		ArrayList<INewsgroup> result = new ArrayList<INewsgroup>(); 
		
		try {
			INewsgroup[] allNewsgroups = serverStoreFacade.getSubscribedNewsgroups(server);
			
			for (INewsgroup newsgroup : allNewsgroups) {
				if ((serverStoreFacade
						.getFirstArticleOfThisUserThreads(newsgroup)).length != 0) {
					result.add(newsgroup);
				}
			}
			
		} catch (StoreException e) {
			Debug.log(getClass(), "Error loading subscribed newsgroups from server");
		}
		
		return (INewsgroup[]) result.toArray(new INewsgroup[0]);
	}

	/**
	 * Get parent of the element
	 * 
	 * @param element
	 * @return the parent of the element
	 */
	public Object getParent(Object element) {

		if (element instanceof ISalvoResource) {
			return ((ISalvoResource) element).getParent();
		}
		return null;
	}

	/**
	 * Update the child count of a particular element
	 * 
	 * @param element
	 *            element
	 * @param currentChildCount
	 *            present child count of the element
	 */
	public void updateChildCount(Object element, int currentChildCount) {

		int length = 0;

		if (element instanceof INewsgroup) {

			length = (serverStoreFacade
					.getFirstArticleOfThisUserThreads((INewsgroup) element)).length;

		} else if (element instanceof INNTPServer) {

			length = newsgroups.length;

		} else if (element instanceof ISalvoResource
				&& ((ISalvoResource) element).getObject() instanceof IArticle) {
			length = currentChildCount;
		}

		viewer.setChildCount(element, length);

	}
	
	/**
	 * Buld the tree with adding child elements for the parent element
	 * 
	 * @param parent
	 *            Parent element
	 * @param index
	 *            index of the child element
	 */
	public void updateElement(Object parent, int index) {

		if (parent instanceof INNTPServer) {

			INewsgroup newsgroup = newsgroups[index];
			viewer.replace(parent, index, newsgroup);
			updateChildCount(newsgroup, -1);

		} else if (parent instanceof INewsgroup) {

			IArticle article = serverStoreFacade
					.getFirstArticleOfThisUserThreads((INewsgroup) parent)[index];
			ISalvoResource resource = SalvoResourceFactory.getResource(
					article.getSubject(), article);

			viewer.replace(parent, index, resource);

			try {
				updateChildCount(
						resource,
						serverStoreFacade.getFollowUps((IArticle) article).length);
			} catch (NNTPException e) {
				updateChildCount(resource, 0);
			}

		} else if (parent instanceof ISalvoResource
				&& ((ISalvoResource) parent).getObject() instanceof IArticle) {

			IArticle parentArticle = (IArticle) ((ISalvoResource) parent)
					.getObject();

			try {
				IArticle article = serverStoreFacade
						.getFollowUps((IArticle) parentArticle)[index];
				ISalvoResource resource = SalvoResourceFactory.getResource(
						article.getSubject(), article);

				viewer.replace(parent, index, resource);

				try {

					updateChildCount(
							resource,
							serverStoreFacade.getFollowUps((IArticle) article).length);

				} catch (NNTPException e) {
					updateChildCount(resource, 0);
				}

			} catch (NNTPException e) {
				Debug.log(getClass(), e);
			}

		}

	}

}