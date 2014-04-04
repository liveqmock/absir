/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-18 下午1:47:59
 */
package com.absir.bean.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.absir.bean.basis.BeanConfig;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.basis.Environment;
import com.absir.core.kernel.KernelList;
import com.absir.core.kernel.KernelList.Orderable;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class BeanFactoryUtils {

	/**
	 * @return
	 */
	public static Environment getEnvironment() {
		BeanConfig beanConfig = getBeanConfig();
		return beanConfig == null ? Environment.PRODUCT : beanConfig.getEnvironment();
	}

	/**
	 * @return
	 */
	public static BeanConfig getBeanConfig() {
		BeanFactory beanFactory = get();
		return beanFactory == null ? null : beanFactory.getBeanConfig();
	}

	/**
	 * @return
	 */
	public static BeanFactory get() {
		return getBeanFactoryImpl();
	}

	/**
	 * @param beanType
	 * @return
	 */
	public static <T> T get(Class<T> beanType) {
		return get().getBeanObject(beanType);
	}

	/**
	 * @return
	 */
	private static BeanFactoryImpl getBeanFactoryImpl() {
		return BeanFactoryImpl.getInstance();
	}

	/**
	 * @param beanObject
	 */
	public static void processBeanObjec(Object beanObject) {
		processBeanObjec(null, beanObject);
	}

	/**
	 * @param beanScope
	 * @param beanObject
	 */
	public static void processBeanObjec(BeanScope beaScope, Object beanObject) {
		getBeanFactoryImpl().processBeanObject(beaScope, null, beanObject);
	}

	/**
	 * @param beanType
	 * @return
	 */
	public static <T> List<T> getOrderBeanObjects(Class<T> beanType) {
		List<T> beanObjects = get().getBeanObjects(beanType);
		if (!Orderable.class.isAssignableFrom(beanType)) {
			KernelList.sortCommonObjects(beanObjects);
		}

		return beanObjects;
	}

	/**
	 * @param contextBean
	 */
	public static void onCreate(Object contextBean) {
		getBeanFactoryImpl().registerStackBeanObject(contextBean);
		processBeanObjec(contextBean);
	}

	/**
	 * @param contextBean
	 */
	public static void onDestroy(Object contextBean) {
		getBeanFactoryImpl().unRegisterStackBeanObject(contextBean);
	}

	/**
	 * @param beanType
	 * @return
	 */
	public static <T> T getRegisterBeanObject(Class<T> beanType) {
		BeanFactoryImpl beanFactory = getBeanFactoryImpl();
		T beanObject = beanFactory.getBeanObject(beanType);
		if (beanObject == null) {
			BeanDefine beanDefine = new BeanDefineOriginal(new BeanDefineType(beanType));
			beanFactory.registerBeanDefine(beanDefine);
			beanObject = (T) beanDefine.getBeanObject(beanFactory);
		}

		return beanObject;
	}

	/** TYPE_MAP_INSTANCE */
	public static final Map<Class<?>, Object> TYPE_MAP_INSTANCE = new ConcurrentHashMap<Class<?>, Object>();

	/**
	 * @param beanType
	 * @return
	 */
	public static <T> T getBeanTypeInstance(Class<T> beanType) {
		Object beanObject = TYPE_MAP_INSTANCE.get(beanType);
		if (beanObject == null) {
			synchronized (beanType) {
				beanObject = TYPE_MAP_INSTANCE.get(beanType);
				if (beanObject == null) {
					beanObject = getBeanFactoryImpl().getBeanObject(beanType);
					if (beanObject == null) {
						BeanDefine beanDefine = new BeanDefineOriginal(new BeanDefineType(beanType));
						BeanFactoryImpl beanFactory = getBeanFactoryImpl();
						beanFactory.registerBeanDefine(beanDefine);
						beanObject = beanDefine.getBeanObject(beanFactory);
						beanFactory.unRegisterBeanDefine(beanDefine);
					}

					TYPE_MAP_INSTANCE.put(beanType, beanObject);
				}
			}
		}

		return (T) beanObject;
	}

	/**
	 * @param beanName
	 * @param beanType
	 * @param beanClass
	 * @return
	 */
	public static <T> T getRegisterBeanObject(String beanName, Class<T> beanType, Class<? extends T> beanClass) {
		if (beanClass == null || beanType == void.class || beanClass == void.class) {
			return (T) get().getBeanObject(beanName);
		}

		T beanObject = get().getBeanObject(beanName, beanClass);
		if (beanObject == null) {
			beanObject = getRegisterBeanObject(beanClass);
		}

		return beanObject;
	}
}
