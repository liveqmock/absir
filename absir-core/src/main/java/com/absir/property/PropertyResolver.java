/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-6 下午12:46:01
 */
package com.absir.property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.absir.bean.inject.InjectOnce;
import com.absir.property.value.PropertyInfo;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes" })
public abstract class PropertyResolver<O extends PropertyObject> implements InjectOnce {

	/**
	 * @param property
	 * @param field
	 * @return
	 */
	public abstract O getPropertyObject(O propertyObject, Field field);

	/**
	 * @param property
	 * @param method
	 * @return
	 */
	public abstract O getPropertyObjectGetter(O propertyObject, Method method);

	/**
	 * @param propertyObject
	 * @param method
	 * @return
	 */
	public abstract O getPropertyObjectSetter(O propertyObject, Method method);

	/**
	 * @param propertyObject
	 * @param propertyInfos
	 * @return
	 */
	public abstract O getPropertyObject(O propertyObject, PropertyInfo[] propertyInfos);

}
