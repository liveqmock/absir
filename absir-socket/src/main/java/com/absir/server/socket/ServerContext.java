/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-18 下午7:26:21
 */
package com.absir.server.socket;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author absir
 * 
 */
public class ServerContext {

	/** server */
	private JbServer server;

	/** online */
	private long online;

	/** closed */
	private boolean closed;

	/** channelContexts */
	@JsonIgnore
	private Map<Serializable, SocketChannelContext> channelContexts = new ConcurrentHashMap<Serializable, SocketChannelContext>();

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
		return online;
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
	 * @param id
	 * @param channelContext
	 */
	public synchronized void loginSocketChannelContext(Serializable id, SocketChannelContext channelContext) {
		synchronized (channelContexts) {
			SocketChannelContext context = channelContexts.put(id, channelContext);
			if (context == null) {
				++online;
			}
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public SocketChannelContext logoutSocketChannelContext(Serializable id) {
		synchronized (channelContexts) {
			SocketChannelContext context = channelContexts.remove(id);
			if (context != null) {
				--online;
			}

			return context;
		}
	}

	/**
	 * @param contextTime
	 * @return
	 */
	public synchronized void stepDone(long contextTime) {
		Iterator<Entry<Serializable, SocketChannelContext>> iterator = channelContexts.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Serializable, SocketChannelContext> entry = iterator.next();
			SocketChannelContext socketChannelContext = entry.getValue();
			if (socketChannelContext.stepDone(contextTime)) {
				iterator.remove();
				try {
					socketChannelContext.getSocketChannel().close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
}
