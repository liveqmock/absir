/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-13 下午5:44:29
 */
package com.absir.context.config;

import java.util.ArrayList;
import java.util.List;

import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.core.BeanDefineAbstract;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
public class BeanDefineArray extends BeanDefineAbstract {

	/** beanDefines */
	private List<BeanDefine> beanDefines = new ArrayList<BeanDefine>();

	/**
	 * @return the beanDefines
	 */
	public List<BeanDefine> getBeanDefines() {
		return beanDefines;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanType()
	 */
	@Override
	public Class<?> getBeanType() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanScope()
	 */
	@Override
	public BeanScope getBeanScope() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanComponent()
	 */
	@Override
	public Object getBeanComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanObject(com.absir.bean.basis.
	 * BeanFactory)
	 */
	@Override
	public List<Object> getBeanObject(BeanFactory beanFactory) {
		// TODO Auto-generated method stub
		List<Object> beanList = new ArrayList<Object>();
		for (BeanDefine beanDefine : beanDefines) {
			beanList.add(beanDefine.getBeanObject(beanFactory));
		}

		return beanList;
	}

	/**
	 * @param beanFactory
	 * @param paramNames
	 * @param parmeterTypes
	 * @return
	 */
	public List<Object> getBeanObject(BeanFactory beanFactory, String[] paramNames, Class<?>[] parameterTypes) {
		List<Object> beanList = new ArrayList<Object>();
		int size = beanDefines.size();
		for (int i = 0; i < size; i++) {
			BeanDefine beanDefine = beanDefines.get(i);
			if (beanDefine instanceof BeanDefineReference) {
				beanList.add(beanFactory.getBeanObject(KernelString.isEmpty(beanDefine.getBeanName()) ? paramNames[i] : beanDefine.getBeanName(), parameterTypes[i],
						((BeanDefineReference) beanDefine).isRequired()));

			} else {
				beanList.add(beanDefine.getBeanObject(beanFactory));
			}
		}

		return beanList;
	}
}
