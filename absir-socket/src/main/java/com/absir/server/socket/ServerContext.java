/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-18 下午7:26:21
 */
package com.absir.server.socket;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author absir
 * 
 */
public class ServerContext {

	/** server */
	private JbServer server;

	/** closed */
	private boolean closed;

	/** channelContexts */
	@JsonIgnore
	private Map<Serializable, SocketChannelContext> channelContexts = new ConcurrentSkipListMap<Serializable, SocketChannelContext>();

	/** channelContextSet */
	@JsonIgnore
	private Set<SocketChannelContext> channelContextSet = new ConcurrentSkipListSet<SocketChannelContext>();

	/**
	 * @return the server
	 */
	public JbServer getServer() {
		return server;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public void setServer(JbServer server) {
		this.server = server;
	}

	/**
	 * @return the online
	 */
	public long getOnline() {
		return channelContexts.size() + channelContextSet.size();
	}

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * @return the channelContexts
	 */
	public Map<Serializable, SocketChannelContext> getChannelContexts() {
		return channelContexts;
	}

	/**
	 * @return the channelContextSet
	 */
	public Set<SocketChannelContext> getChannelContextSet() {
		return channelContextSet;
	}

	/**
	 * @param id
	 * @param channelContext
	 */
	public void loginSocketChannelContext(Serializable id, SocketChannelContext channelContext) {
		SocketChannelContext context = channelContexts.put(id, channelContext);
		if (context != null) {
			channelContextSet.add(channelContext);
		}
	}

	/**
	 * @param id
	 * @param socketChannel
	 * @return
	 */
	public void logoutSocketChannelContext(Serializable id, SocketChannel socketChannel) {
		SocketChannelContext context = channelContexts.remove(id);
		if (context == null || context.getSocketChannel() != socketChannel) {
			channelContextSet.remove(socketChannel);
			if (context != null) {
				channelContextSet.add(context);
			}
		}
	}

	/**
	 * @param contextTime
	 * @return
	 */
	public void stepDone(long contextTime) {
		Iterator<Entry<Serializable, SocketChannelContext>> iterator = channelContexts.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Serializable, SocketChannelContext> entry = iterator.next();
			SocketChannelContext socketChannelContext = entry.getValue();
			if (socketChannelContext.stepDone(contextTime)) {
				iterator.remove();
				SocketServer.close(socketChannelContext.getSocketChannel());
			}
		}

		Iterator<SocketChannelContext> setIterator = channelContextSet.iterator();
		while (iterator.hasNext()) {
			SocketChannelContext socketChannelContext = setIterator.next();
			if (socketChannelContext.stepDone(contextTime)) {
				setIterator.remove();
				SocketServer.close(socketChannelContext.getSocketChannel());
			}
		}
	}
}
