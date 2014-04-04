/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 下午8:35:22
 */
package com.absir.server.route.returned;

import java.lang.reflect.Method;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelString;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.RouteAction;
import com.absir.server.value.View;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class ReturnedResolverView implements ReturnedResolver<String> {

	/** ME */
	public static final ReturnedResolverView ME = BeanFactoryUtils.get(ReturnedResolverView.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolver#getReturned(java.lang
	 * .reflect.Method)
	 */
	@Override
	public String getReturned(Method method) {
		// TODO Auto-generated method stub
		View view = method.getAnnotation(View.class);
		return view == null ? null : view.value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolver#resolveReturnedValue
	 * (java.lang.Object, java.lang.Object, com.absir.server.on.OnPut)
	 */
	@Override
	public void resolveReturnedValue(Object returnValue, String returned, OnPut onPut) throws Exception {
		// TODO Auto-generated method stub
		if (returnValue != null) {
			if (returnValue instanceof String) {
				returned = (String) returnValue;

			} else {
				onPut.getInput().write(returnValue.toString());
				return;
			}
		}

		if (KernelString.isEmpty(returned)) {
			Input input = onPut.getInput();
			RouteAction routeAction = input.getRouteAction();
			if (routeAction == null) {
				return;
			}

			if (routeAction.getRouteView() == null) {
				routeAction.setRouteView(HelperFileName.normalizeNoEndSeparator(new String(input.getRouteMatcher().getMapping())));
			}

			returned = routeAction.getRouteView();
		}

		resolveReturnedView(returned, onPut);
	}

	/**
	 * @param viewValue
	 * @param onPut
	 * @throws Exception
	 */
	public void resolveReturnedView(String view, OnPut onPut) throws Exception {
		onPut.getInput().write(view);
	}
}
