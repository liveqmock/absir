/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-20 下午1:39:32
 */
package com.absir.bean.core;

import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;
import com.absir.core.kernel.KernelClass;

/**
 * @author absir
 * 
 */
public abstract class BeanDefineAbstractor extends BeanDefineAbstract {

	/** loaded */
	private boolean loaded;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanObject(com.absir.bean.basis.
	 * BeanFactory, com.absir.bean.basis.BeanDefine,
	 * com.absir.bean.basis.BeanDefine)
	 */
	@Override
	public Object getBeanObject(BeanFactory beanFactory, BeanDefine beanDefineRoot, BeanDefine beanDefineWrapper) {
		if (loaded) {
			BeanDefine beanDefineLoaded = beanFactory.getBeanDefine(beanDefineRoot.getBeanName());
			if (beanDefineLoaded != null && beanDefineLoaded != beanDefineRoot) {
				return beanDefineLoaded.getBeanObject(beanFactory);
			}

		} else {
			if (beanDefineRoot.getBeanScope() != BeanScope.PROTOTYPE) {
				loaded = true;
			}
		}

		return getBeanObject(beanFactory, this, beanDefineRoot, beanDefineWrapper);
	}

	/**
	 * @param type
	 */
	public static void loadInterfaces(Class<?> type) {
		while (type != null && type != Object.class) {
			for (Class<?> iCls : type.getInterfaces()) {
				KernelClass.forName(iCls.getName());
			}

			type = type.getSuperclass();
		}
	}

	/**
	 * @param beanFactory
	 * @param beanDefine
	 * @param beanDefineRoot
	 * @param beanDefineWrapper
	 * @return
	 */
	public static Object getBeanObject(BeanFactory beanFactory, BeanDefine beanDefine, BeanDefine beanDefineRoot, BeanDefine beanDefineWrapper) {
		Object beanObject = beanDefine.getBeanObject(beanFactory);
		if (beanDefine instanceof BeanDefineAbstractor && ((BeanDefineAbstractor) beanDefine).loaded) {
			BeanDefine beanDefineLoaded = beanFactory.getBeanDefine(beanDefineRoot.getBeanName());
			if (beanDefineLoaded != null && beanDefineLoaded != beanDefineRoot) {
				return beanDefineLoaded.getBeanObject(beanFactory);
			}
		}

		BeanScope beanScope = beanDefineRoot.getBeanScope();
		Object beanProxy = beanObject;
		if (beanDefineRoot != null) {
			beanProxy = beanDefineRoot.getBeanProxy(beanProxy, beanDefineRoot, beanFactory);
		}

		beanDefine = getBeanDefine(beanDefineRoot.getBeanType(), beanDefineRoot.getBeanName(), beanProxy, beanScope, beanDefine);
		if (beanDefineWrapper != null && beanDefineWrapper instanceof BeanDefineWrapper) {
			((BeanDefineWrapper) beanDefineWrapper).beanDefine = beanDefine;
		}

		if (beanScope == BeanScope.PROTOTYPE) {
			beanDefineWrapper = beanDefine;
			if (beanDefineRoot != null && beanDefineRoot instanceof BeanDefineWrapper) {
				beanDefine = ((BeanDefineWrapper) beanDefineRoot).retrenchBeanDefine();
			}
		}

		if (beanDefine != beanDefineWrapper) {
			((BeanFactoryImpl) beanFactory).replaceRegisteredBeanDefine(beanDefine);
		}

		beanFactory.processBeanObject(beanScope, beanDefineRoot, beanObject, beanProxy);
		return beanProxy;
	}
}
