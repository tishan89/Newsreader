package org.eclipse.ecf.channel.core.internal;

import org.eclipse.ecf.channel.model.IMessage;
import org.eclipse.ecf.channel.model.IMessageSource;
import org.eclipse.ecf.channel.model.IServerStoreFacade;
import org.eclipse.ecf.channel.model.IStore;

public class ServerStoreFacade implements IServerStoreFacade {

	@Override
	public Exception getLastException() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void catchUp(IMessageSource source) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public IStore getFirstStore() {
		// TODO Decide on generic Store or Unique store for everything.
		return null;
	}

	@Override
	public IStore[] getStores() {
		// TODO Decide on generic Store or Unique store for everything.
		return null;
	}

	@Override
	public void updateMessage(IMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public IMessage getMessageByMsgId(IMessageSource source, String msgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMessage getFirstMessageOfTread(IMessage message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMessage[] orderMessagesesFromNewestFirst(IMessage[] messages) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void syncStoreWithServer(IMessageSource messageSource,
			boolean isNewMessagePosted) throws Exception {
		// TODO Auto-generated method stub

	}

}
