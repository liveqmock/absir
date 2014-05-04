/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-30 下午5:09:42
 */
package com.absir.appserv.developer.editor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.absir.appserv.system.bean.value.JaIngore;
import com.absir.bean.inject.value.Bean;
import com.absir.property.PropertySupply;

/**
 * @author absir
 * 
 */
@Bean
class EditorSupply extends PropertySupply<EditorObject, EntityField> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.property.PropertySupply#getIngoreAnnotationClass()
	 */
	@Override
	public Class<? extends Annotation> getIngoreAnnotationClass() {
		// TODO Auto-generated method stub
		return JaIngore.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertySupply#getPropertyObject(com.absir.property
	 * .PropertyObject, java.lang.reflect.Field)
	 */
	@Override
	public EditorObject getPropertyObject(EditorObject propertyObject, Field field) {
		// TODO Auto-generated method stub
		if (propertyObject == null) {
			propertyObject = new EditorObject();
		}

		return super.getPropertyObject(propertyObject, field);
	}
}
