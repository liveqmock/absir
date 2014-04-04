/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-8 下午4:49:27
 */
package com.absir.servlet;

import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.Environment;
import com.absir.bean.config.IBeanFactoryStopping;
import com.absir.bean.core.BeanDefineDiscover;
import com.absir.bean.core.BeanFactoryProvider;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.context.config.BeanProviderContext;

/**
 * @author absir
 * 
 */
public class InDispathContext extends InDispathFilter {

	/** beanFactoryStoppings */
	List<IBeanFactoryStopping> beanFactoryStoppings;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.servlet.InDispathFilter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		super.init(filterConfig);
		BeanDefineDiscover.open();
		BeanProviderContext beanProviderContext = new BeanProviderContext(BeanFactoryProvider.getParameterList(filterConfig.getInitParameter("include")),
				BeanFactoryProvider.getParameterList(filterConfig.getInitParameter("exclude")), BeanFactoryProvider.getParameterList(filterConfig.getInitParameter("filter")));
		beanProviderContext.scan(null, null, filterConfig.getServletContext());
		if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
			System.out.println("beanfactory start from " + this);
		}

		beanProviderContext.started();
		beanFactoryStoppings = beanProviderContext.getBeanFactoryStoppeds();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		List<IBeanFactoryStopping> stoppings = beanFactoryStoppings;
		if (stoppings != null) {
			beanFactoryStoppings = null;
			boolean inDebug = BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0;
			if (inDebug) {
				System.out.println("beanfactory stopping... [" + (stoppings == null ? 0 : stoppings.size()) + "]");
			}

			BeanFactory beanFactory = BeanFactoryUtils.get();
			for (IBeanFactoryStopping stopping : stoppings) {
				if (inDebug) {
					System.out.println("beanfactory stopping... [" + stopping + "]");
				}

				stopping.stopping(beanFactory);
			}
		}
	}
}
