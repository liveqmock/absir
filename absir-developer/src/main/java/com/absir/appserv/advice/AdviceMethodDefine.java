/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月10日 下午5:28:07
 */
package com.absir.appserv.advice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import com.absir.aop.AopInterceptor;
import com.absir.aop.AopInterceptorAbstract;
import com.absir.aop.AopMethodDefineAbstract;
import com.absir.aop.AopProxyHandler;
import com.absir.appserv.advice.AdviceMethodDefine.AopMethodInterceptor;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.core.kernel.KernelCollection;

/**
 * @author absir
 *
 */
@SuppressWarnings("rawtypes")
@Basis
@Bean
public class AdviceMethodDefine extends AopMethodDefineAbstract<AopMethodInterceptor, IMethodAdvice[], Object> {

	/**
	 * @author absir
	 *
	 */
	public static class AopMethodInterceptor extends AopInterceptorAbstract<IMethodAdvice[]> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#before(java.lang.Object,
		 * java.util.Iterator, java.lang.Object, com.absir.aop.AopProxyHandler,
		 * java.lang.reflect.Method, java.lang.Object[],
		 * net.sf.cglib.proxy.MethodProxy)
		 */
		@Override
		public Object before(final Object proxy, Iterator<AopInterceptor> iterator, final IMethodAdvice[] interceptor, AopProxyHandler proxyHandler, final Method method, final Object[] args,
				MethodProxy methodProxy) throws Throwable {
			// TODO Auto-generated method stub
			AdviceInvoker adviceInvoker = new AdviceInvoker(interceptor) {

				@Override
				public Object invoke() throws Throwable {
					// TODO Auto-generated method stub
					Object value;
					while (true) {
						try {
							value = before(interceptor, proxy, method, args);
							if (value == AopProxyHandler.VOID) {
								value = invoke();
							}

						} finally {
							// TODO: handle exception
						}
					}

					// return value;
				}

			};

			return adviceInvoker.invoke();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#after(java.lang.Object,
		 * java.lang.Object, java.lang.Object, com.absir.aop.AopProxyHandler,
		 * java.lang.reflect.Method, java.lang.Object[], java.lang.Throwable)
		 */
		@Override
		public Object after(Object proxy, Object returnValue, IMethodAdvice[] interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, Throwable e) throws Throwable {
			// TODO Auto-generated method stub
			return returnValue;
		}
	}

	/** methodAdvices */
	@Inject(type = InjectType.Selectable)
	private IMethodAdvice[] methodAdvices;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.aop.AopMethodDefine#getAopInterceptor(com.absir.bean.basis.
	 * BeanDefine, java.lang.Object)
	 */
	@Override
	public AopMethodInterceptor getAopInterceptor(BeanDefine beanDefine, Object beanObject) {
		// TODO Auto-generated method stub
		return methodAdvices == null ? null : new AopMethodInterceptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#getAopInterceptor(java.lang.Object,
	 * java.lang.Class)
	 */
	@Override
	public IMethodAdvice[] getAopInterceptor(Object variable, Class<?> beanType) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#getAopInterceptor(java.lang.Object,
	 * java.lang.Object, java.lang.Class, java.lang.reflect.Method)
	 */
	@Override
	public IMethodAdvice[] getAopInterceptor(IMethodAdvice[] interceptor, Object variable, Class<?> beanType, Method method) {
		// TODO Auto-generated method stub
		List<IMethodAdvice> advices = new ArrayList<IMethodAdvice>();
		for (IMethodAdvice advice : methodAdvices) {
			if (advice.matching(method)) {
				advices.add(advice);
			}
		}

		return advices.isEmpty() ? null : KernelCollection.toArray(advices, IMethodAdvice.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -1024;
	}
}
