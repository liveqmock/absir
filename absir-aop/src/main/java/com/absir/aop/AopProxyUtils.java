/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-21 下午2:05:23
 */
package com.absir.aop;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;

import com.absir.aop.value.Proxy;
import com.absir.core.kernel.KernelCollection;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AopProxyUtils {

	/** aopProxyClass */
	private static Class<?>[] aopProxyClass = new Class<?>[] { AopProxy.class };

	/**
	 * @param beanObject
	 * @return
	 */
	public static <T> T getProxy(T beanObject) {
		return (T) getProxy(beanObject, false, false);
	}

	/**
	 * @param beanObject
	 * @param jdk
	 * @param impl
	 * @return
	 */
	public static AopProxy getProxy(Object beanObject, boolean jdk, boolean impl) {
		return getProxy(beanObject, null, jdk, impl);
	}

	/**
	 * @param beanObject
	 * @param interfaces
	 * @param jdk
	 * @param impl
	 * @return
	 */
	public static AopProxy getProxy(Object beanObject, Set<Class<?>> interfaces, boolean jdk, boolean impl) {
		return getProxy(beanObject, null, interfaces, jdk, impl);
	}

	/**
	 * @param beanObject
	 * @param beanType
	 * @param interfaces
	 * @param jdk
	 * @param impl
	 * @return
	 */
	public static AopProxy getProxy(Object beanObject, Class<?> beanType, Set<Class<?>> interfaces, boolean jdk, boolean impl) {
		if (interfaces == null) {
			interfaces = new HashSet<Class<?>>();
		}

		Class<?> beanClass = null;
		if (beanObject != null) {
			beanClass = beanObject.getClass();
			while (beanClass != null && beanClass != Object.class) {
				for (Class<?> iCls : beanClass.getInterfaces()) {
					interfaces.add(iCls);
				}

				beanClass = beanClass.getSuperclass();
			}

			beanClass = beanObject.getClass();
		}

		if (beanType == null) {
			beanType = beanClass;

		} else {
			if (beanType.isInterface()) {
				interfaces.add(beanType);

			} else {
				if (beanClass == null && Modifier.isFinal(beanType.getModifiers())) {
					beanClass = beanType;
				}
			}
		}

		Class<?>[] iterfaceArray = null;
		if (interfaces.isEmpty()) {
			iterfaceArray = aopProxyClass;

		} else {
			interfaces.add(AopProxy.class);
			iterfaceArray = KernelCollection.toArray(interfaces, Class.class);
		}

		if (beanClass != null && (beanClass.isInterface() || Modifier.isFinal(beanClass.getModifiers()))) {
			beanClass = null;
		}

		if (impl || beanType != null) {
			Enhancer enhancer = new Enhancer();
			if (!(beanClass == null)) {
				enhancer.setSuperclass(beanClass);
			}

			enhancer.setInterfaces(iterfaceArray);
			enhancer.setCallback(new AopProxyCglib(beanType, beanObject));
			return (AopProxy) enhancer.create();
		}

		return (AopProxy) java.lang.reflect.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), iterfaceArray, new AopProxyJDK(beanType, beanObject));
	}

	/**
	 * @param beanObject
	 * @return
	 */
	public static Class<?> getBeanType(Object beanObject) {
		if (beanObject instanceof AopProxy) {
			AopProxy aopProxy = (AopProxy) beanObject;
			Class<?> beanType = aopProxy.getBeanType();
			if (beanType != null) {
				return beanType;
			}

			beanObject = aopProxy.getBeanObject();
			if (beanObject != null) {
				return beanObject.getClass();
			}

			return aopProxy.getBeanType();
		}

		return beanObject.getClass();
	}

	/**
	 * @param beanObject
	 * @return
	 */
	public static AopProxy proxyInterceptor(Object beanObject, AopInterceptor aopInterceptor) {
		AopProxy aopProxy = null;
		if (beanObject instanceof AopProxy) {
			aopProxy = (AopProxy) beanObject;

		} else {
			Proxy proxy = beanObject.getClass().getAnnotation(Proxy.class);
			aopProxy = getProxy(beanObject, proxy == null ? false : proxy.jdk(), proxy == null ? false : proxy.impl());
		}

		if (aopInterceptor != null) {
			aopProxy.getAopInterceptors().add(aopInterceptor);
		}

		return aopProxy;
	}
}
