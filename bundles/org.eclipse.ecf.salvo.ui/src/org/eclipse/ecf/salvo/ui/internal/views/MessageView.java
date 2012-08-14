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
package org.eclipse.ecf.salvo.ui.internal.views;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.ecf.channel.IChannelContainerAdapter;
import org.eclipse.ecf.channel.core.ISalvoUtil;
import org.eclipse.ecf.channel.core.SalvoUtil;
import org.eclipse.ecf.channel.model.IMessage;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.protocol.nntp.model.SALVO;
import org.eclipse.ecf.salvo.ui.internal.MimeArticleContentHandler;
import org.eclipse.ecf.salvo.ui.internal.editor.ArticlePanel;
import org.eclipse.ecf.salvo.ui.internal.editor.ArticleWidgetBuilder;
import org.eclipse.ecf.salvo.ui.internal.resources.ISalvoResource;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class MessageView extends ViewPart implements ISelectionListener,
		ISelectionProvider {

	private IMessage message;

	private Composite parent;

	public static String ID = "org.eclipse.ecf.salvo.ui.internal.views.messageView";
	
	private IContainer iContainer;
	private IChannelContainerAdapter adaptor;

	public MessageView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 0;
		gridLayout_1.marginHeight = 0;
		parent.setLayout(gridLayout_1);

		initializeToolBar();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().addPostSelectionListener(this);
		getSite().setSelectionProvider(this);

	}

	@Override
	public void setFocus() {

	}

	private void initializeToolBar() {
		// IToolBarManager toolBarManager =
		// getViewSite().getActionBars().getToolBarManager();
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		setupContainer();
		IMessage newArticle = null;
		ISalvoResource resource = null;

		if (part == this)
			return;

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection struct = (IStructuredSelection) selection;

			if (struct.size() != 1) {
				return;
			}

			if (!(struct.getFirstElement() instanceof ISalvoResource)) {
				return;
			}

			if (!(((ISalvoResource) struct.getFirstElement()).getObject() instanceof IMessage)) {
				return;
			}

			resource = (ISalvoResource) struct.getFirstElement();
			newArticle = (IMessage) resource.getObject();
			setSelection(selection);

			if (message != newArticle) {

				// FIXME same code is used in ReplyView

				try {
					message = newArticle;

					setContentDescription("From: " + message.getFrom()
							+ "  To: " );	//removed '+ message.getXRef()' since getXRef is not present in IMessage. Need to figure out whether it is a generic info.
					setPartName(DecoderUtil.decodeEncodedWords(message
							.getSubject()));

					message.setRead(true);
					message.setThreadAttributes(adaptor.fetchFollowups(message));
					adaptor.updateMessage(message, null);

					StringBuffer buffer = new StringBuffer();
					String[] body = (String[]) message.getMessageBody();
					for (String line : body) {
						buffer.append(line + SALVO.CRLF);
					}

					MimeArticleContentHandler handler = new MimeArticleContentHandler(
							message);
					MimeStreamParser parser = new MimeStreamParser();
					parser.setContentHandler(handler);

					parser.parse(new ByteArrayInputStream(buffer.toString()
							.getBytes()));

					if (!parent.isDisposed())
						for (Control child : parent.getChildren()) {
							child.dispose();
						}

					ArticleWidgetBuilder.build(parent, message, handler);
					// composite.setLayout(gridLayout);
					parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							true));
					parent.layout(true);

					// FIXME tight coupling is a no go. Need to set the read
					// state
					// some other way
					if (part instanceof ArticlePanel) {
						((ArticlePanel) part).updateArticle(resource);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			getViewSite().getPage().bringToTop(this);
		}

	}

	private void setupContainer() {
		BundleContext context = FrameworkUtil.getBundle(this.getClass())
				.getBundleContext();
		ServiceReference reference = context
				.getServiceReference(ISalvoUtil.class.getName());
		SalvoUtil salvoUtil = (SalvoUtil) context.getService(reference);
		this.iContainer = salvoUtil.getDefault().getContainerManager()
				.getAllContainers()[0];
		adaptor = (IChannelContainerAdapter) iContainer
				.getAdapter(IChannelContainerAdapter.class);
		
	}

	List<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();

	ISelection theSelection = StructuredSelection.EMPTY;

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	public ISelection getSelection() {
		return theSelection;
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	public void setSelection(ISelection selection) {
		theSelection = selection;
		final SelectionChangedEvent e = new SelectionChangedEvent(this,
				selection);
		Object[] listenersArray = listeners.toArray();

		for (int i = 0; i < listenersArray.length; i++) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listenersArray[i];
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(e);
				}
			});
		}
	}
}
