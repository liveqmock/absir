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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.aop.AopInterceptor;
import com.absir.aop.AopProxyHandler;
import com.absir.context.core.ContextUtils;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class AysncRunable {

	/** LOGGER */
	protected static final Logger LOGGER = LoggerFactory.getLogger(AysncRunable.class);

	/** timeout */
	protected long timeout;

	/**
	 * @param timeout
	 */
	public AysncRunable(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @param proxy
	 * @param iterator
	 * @param proxyHandler
	 * @param method
	 * @param args
	 * @param methodProxy
	 * @throws Throwable
	 */
	public void aysnc(final Object proxy, final Iterator<AopInterceptor> iterator, final AopProxyHandler proxyHandler, final Method method, final Object[] args, final MethodProxy methodProxy)
			throws Throwable {
		aysncRun(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					proxyHandler.invoke(proxy, iterator, method, args, methodProxy);

				} catch (Throwable e) {
					LOGGER.error("", e);
				}
			}
		});
	}

	/**
	 * @param runnable
	 */
	public void aysncRun(Runnable runnable) {
		if (timeout <= 0) {
			ContextUtils.getThreadPoolExecutor().execute(runnable);

		} else {
			final Thread thread = new Thread(runnable);
			thread.start();
			ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(timeout);
						thread.interrupt();

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
				}
			});
		}
	}

}
