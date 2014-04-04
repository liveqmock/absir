/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-21 下午2:05:23
 */
package com.absir.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodProxy;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AopProxyHandler {

	/** VOID */
	public static final Object VOID = new Object();

	/** AOP_PROXY_METHOD_ZERO */
	private static final Map<String, Integer> AOP_PROXY_METHOD_ZERO = new HashMap<String, Integer>();

	/** AOP_PROXY_METHOD_ONE */
	private static final Map<String, Integer> AOP_PROXY_METHOD_ONE = new HashMap<String, Integer>();

	static {
		AOP_PROXY_METHOD_ZERO.put("getBeanType", 0);
		AOP_PROXY_METHOD_ZERO.put("getBeanObject", 1);
		AOP_PROXY_METHOD_ZERO.put("getAopInterceptors", 2);
		AOP_PROXY_METHOD_ZERO.put("hashCode", 3);
		AOP_PROXY_METHOD_ZERO.put("toString", 4);

		AOP_PROXY_METHOD_ONE.put("equals", 0);
	}

	/** beanType */
	Class<?> beanType;

	/** beanObject */
	Object beanObject;

	/** aopInterceptors */
	List<AopInterceptor> aopInterceptors = new ArrayList<AopInterceptor>();

	/**
	 * @param beanType
	 * @param beanObject
	 */
	public AopProxyHandler(Class<?> beanType, Object beanObject) {
		this.beanType = beanType;
		this.beanObject = beanObject;
	}

	/**
	 * @return the beanObject
	 */
	public Object getBeanObject() {
		return beanObject;
	}

	/**
	 * @return the aopInterceptors
	 */
	public List<AopInterceptor> getAopInterceptors() {
		return aopInterceptors;
	}

	/**
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy
	 * @return
	 * @throws Throwable
	 */
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		int length = args == null ? 0 : args.length;
		if (length == 0) {
			Integer interceptor = AOP_PROXY_METHOD_ZERO.get(method.getName());
			if (interceptor != null) {
				switch (interceptor) {
				case 0:
					return beanType;

				case 1:
					return beanObject;

				case 2:
					return aopInterceptors;

				case 3:
					return hashCode();

				case 4:
					return toString();
				}
			}

		} else if (length == 1) {
			Integer interceptor = AOP_PROXY_METHOD_ZERO.get(method.getName());
			if (interceptor != null) {
				switch (interceptor) {
				case 0:
					return this == args[0];
				}
			}
		}

		return intercept(aopInterceptors.iterator(), method, args, proxy);
	}

	/**
	 * @param iterator
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy
	 * @return
	 * @throws Throwable
	 */
	public Object intercept(Iterator<AopInterceptor> iterator, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		while (iterator.hasNext()) {
			AopInterceptor aopInterceptor = iterator.next();
			Object interceptor = aopInterceptor.getInterceptor(this, beanObject, method, args);
			if (interceptor != null) {
				Object value = null;
				Throwable ex = null;
				try {
					value = aopInterceptor.before(iterator, interceptor, this, method, args, proxy);
					if (value == VOID) {
						value = intercept(iterator, method, args, proxy);
					}

					return value;

				} catch (Throwable e) {
					ex = e;
					throw e;

				} finally {
					value = aopInterceptor.after(interceptor, value, this, beanObject, method, args, ex);
				}
			}
		}

		return invoke(method, args, proxy);
	}

	/**
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(Method method, Object[] args, MethodProxy proxy) throws Throwable {
		return method.invoke(beanObject, args);
	}
}
