/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-24 下午3:40:07
 */
package com.absir.aop;

import java.util.Collections;
import java.util.List;

import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.core.BeanDefineWrapper;
import com.absir.core.kernel.KernelLang;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AopBeanDefine extends BeanDefineWrapper {

	/** aopInterceptors */
	private List<AopInterceptor> aopInterceptors;

	/**
	 * @param beanDefine
	 */
	public AopBeanDefine(BeanDefine beanDefine) {
		super(beanDefine);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.core.BeanDefineWrapper#getBeanProxy(java.lang.Object,
	 * com.absir.bean.basis.BeanDefine, com.absir.bean.basis.BeanFactory)
	 */
	@Override
	public Object getBeanProxy(Object beanObject, BeanDefine beanDefineRoot, BeanFactory beanFactory) {
		if (aopInterceptors == null) {
			aopInterceptors = AopDefineProcessor.getAopInterceptors(beanDefineRoot, beanObject);
			if (aopInterceptors == null || aopInterceptors.isEmpty()) {
				aopInterceptors = KernelLang.NULL_LIST_SET;

			} else {
				aopInterceptors = Collections.unmodifiableList(aopInterceptors);
			}
		}

		if (!aopInterceptors.isEmpty()) {
			AopProxy aopProxy = null;
			for (AopInterceptor aopInterceptor : aopInterceptors) {
				if (aopProxy == null) {
					aopProxy = AopProxyUtils.proxyInterceptor(beanObject, aopInterceptor);

				} else {
					aopProxy.getAopInterceptors().add(aopInterceptor);
				}
			}

			beanObject = aopProxy;
		}

		return beanObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.core.BeanDefineWrappered#retrenchBeanDefine()
	 */
	@Override
	public BeanDefine retrenchBeanDefine() {
		if (beanDefine instanceof BeanDefineWrapper) {
			beanDefine = ((BeanDefineWrapper) beanDefine).retrenchBeanDefine();
		}

		return aopInterceptors.isEmpty() ? beanDefine : this;
	}
}
