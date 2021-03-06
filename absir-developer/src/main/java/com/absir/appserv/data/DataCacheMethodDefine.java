/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-5-7 下午7:37:01
 */
package com.absir.appserv.data;

import java.lang.reflect.Method;
import java.util.Iterator;

import net.sf.cglib.proxy.MethodProxy;

import com.absir.aop.AopImplDefine;
import com.absir.aop.AopInterceptor;
import com.absir.aop.AopInterceptorAbstract;
import com.absir.aop.AopMethodDefineAbstract;
import com.absir.aop.AopProxyHandler;
import com.absir.appserv.data.DataCacheMethodDefine.DataCacheInterceptor;
import com.absir.appserv.data.value.DataCache;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelLang.ObjectTemplate;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
@Basis
@Bean
public class DataCacheMethodDefine extends AopMethodDefineAbstract<DataCacheInterceptor, ObjectTemplate<Object>, String> {

	/** DATA_CACHE_EMPTY */
	private static final Object DATA_CACHE_EMPTY = new Object();

	/**
	 * @author absir
	 * 
	 */
	public static class DataCacheInterceptor extends AopInterceptorAbstract<ObjectTemplate<Object>> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#before(java.lang.Object,
		 * java.util.Iterator, java.lang.Object, com.absir.aop.AopProxyHandler,
		 * java.lang.reflect.Method, java.lang.Object[],
		 * net.sf.cglib.proxy.MethodProxy)
		 */
		@Override
		public Object before(Object proxy, Iterator<AopInterceptor> iterator, ObjectTemplate<Object> interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, MethodProxy methodProxy)
				throws Throwable {
			// TODO Auto-generated method stub
			if (interceptor.object == DATA_CACHE_EMPTY) {
				interceptor.object = proxyHandler.invoke(proxy, iterator, method, args, methodProxy);
			}

			return interceptor.object;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.aop.AopMethodDefine#getAopInterceptor(com.absir.bean.basis.
	 * BeanDefine, java.lang.Object)
	 */
	@Override
	public DataCacheInterceptor getAopInterceptor(BeanDefine beanDefine, Object beanObject) {
		// TODO Auto-generated method stub
		return BeanFactoryImpl.getBeanDefine(beanDefine, AopImplDefine.class) == null ? null : new DataCacheInterceptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#getAopInterceptor(java.lang.Object,
	 * java.lang.Class)
	 */
	@Override
	public ObjectTemplate<Object> getAopInterceptor(String variable, Class<?> beanType) {
		// TODO Auto-generated method stub
		DataCache dataCache = beanType.getAnnotation(DataCache.class);
		return dataCache == null || !dataCache.cacheable() ? null : new ObjectTemplate<Object>(DATA_CACHE_EMPTY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#getAopInterceptor(java.lang.Object,
	 * java.lang.Object, java.lang.Class, java.lang.reflect.Method)
	 */
	@Override
	public ObjectTemplate<Object> getAopInterceptor(ObjectTemplate<Object> interceptor, String variable, Class<?> beanType, Method method) {
		// TODO Auto-generated method stub
		DataCache dataCache = beanType.getAnnotation(DataCache.class);
		return dataCache == null ? interceptor : dataCache.cacheable() ? interceptor == null ? new ObjectTemplate<Object>(DATA_CACHE_EMPTY) : interceptor : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1024;
	}
}
