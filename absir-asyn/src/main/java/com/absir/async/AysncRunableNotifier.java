/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-26 下午4:39:08
 */
package com.absir.async;

import java.lang.reflect.Method;
import java.util.Iterator;

import net.sf.cglib.proxy.MethodProxy;

import com.absir.aop.AopInterceptor;
import com.absir.aop.AopProxyHandler;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class AysncRunableNotifier extends AysncRunable {

	/** notifying */
	private boolean notifying;

	/** notifierIterator */
	private NotifierIterator notifierIterator;

	/**
	 * @param timeout
	 */
	public AysncRunableNotifier(long timeout) {
		super(timeout);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @author absir
	 * 
	 */
	private static class NotifierIterator {

		/** iterator */
		private Iterator<AopInterceptor> iterator;

		/** proxyHandler */
		private AopProxyHandler proxyHandler;

		/** method */
		private Method method;

		/** args */
		private Object[] args;

		/** proxy */
		private MethodProxy proxy;
	}

	/**
	 * @param iterator
	 * @param proxyHandler
	 * @param method
	 * @param args
	 * @param proxy
	 * @return
	 */
	public Runnable notifierRunable(final Iterator<AopInterceptor> iterator, final AopProxyHandler proxyHandler, final Method method, final Object[] args, final MethodProxy proxy) {
		return new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					proxyHandler.intercept(iterator, method, args, proxy);

				} catch (Throwable e) {
					LOGGER.error("failed!", e);

				} finally {
					NotifierIterator iterator = null;
					synchronized (AysncRunableNotifier.this) {
						if (notifierIterator != null) {
							iterator = notifierIterator;
							notifierIterator = null;

						} else {
							notifying = false;
							return;
						}
					}

					aysncRun(notifierRunable(iterator.iterator, iterator.proxyHandler, iterator.method, iterator.args, iterator.proxy));
				}
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.async.AysncRunable#aysnc(java.util.Iterator,
	 * com.absir.aop.AopProxyHandler, java.lang.reflect.Method,
	 * java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
	 */
	@Override
	public void aysnc(final Iterator<AopInterceptor> iterator, final AopProxyHandler proxyHandler, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		synchronized (this) {
			if (notifying) {
				if (notifierIterator == null) {
					notifierIterator = new NotifierIterator();
				}

				notifierIterator.iterator = iterator;
				notifierIterator.proxyHandler = proxyHandler;
				notifierIterator.method = method;
				notifierIterator.args = args;
				notifierIterator.proxy = proxy;
				return;
			}

			notifying = true;
		}

		aysncRun(notifierRunable(iterator, proxyHandler, method, args, proxy));
	}
}
