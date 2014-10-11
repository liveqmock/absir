/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月11日 下午3:34:37
 */
package com.absir.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanConfig;
import com.absir.bean.config.IBeanFactoryAware;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Stopping;

/**
 * @author absir
 *
 */
@Basis
@Bean
public class SpringBeanDefineAware implements IBeanFactoryAware {

	/** CONTEXT */
	public static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.config.IBeanFactoryAware#beforeRegister(com.absir.bean
	 * .core.BeanFactoryImpl)
	 */
	@Override
	public void beforeRegister(BeanFactoryImpl beanFactory) {
		// TODO Auto-generated method stub
		BeanConfig beanConfig = beanFactory.getBeanConfig();
		String[] locations = beanConfig.getExpressionObject("spring.locations", null, String[].class);
		if (locations == null) {
			locations = new String[] { beanConfig.getClassPath() + "spring/*.xml" };
		}

		CONTEXT.setConfigLocations(locations);
		CONTEXT.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.config.IBeanFactoryAware#afterRegister(com.absir.bean.
	 * core.BeanFactoryImpl)
	 */
	@Override
	public void afterRegister(BeanFactoryImpl beanFactory) {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 */
	@Stopping
	protected void stopping() {
		CONTEXT.stop();
	}
}
