/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-7 下午2:06:52
 */
package com.absir.binder;

import java.lang.annotation.Annotation;

import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;
import com.absir.binder.value.BinderIngore;
import com.absir.property.PropertySupply;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class BinderSupply extends PropertySupply<BinderObject, Binder> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.property.PropertySupply#getIngoreAnnotationClass()
	 */
	@Override
	public Class<? extends Annotation> getIngoreAnnotationClass() {
		// TODO Auto-generated method stub
		return BinderIngore.class;
	}

}
