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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelList;
import com.absir.core.kernel.KernelList.Orderable;
import com.absir.property.value.PropertyInfo;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class PropertySupply<O extends PropertyObject<T>, T> {

	/** supplySize */
	private static int supplySize;

	/**
	 * @return the supplySize
	 */
	protected static int getSupplySize() {
		return supplySize;
	}

	/** supplyIndex */
	private int supplyIndex;

	/** propertyResolvers */
	private PropertyResolver[] propertyResolvers;

	/**
	 * 
	 */
	public PropertySupply() {
		synchronized (PropertySupply.class) {
			supplyIndex = supplySize;
			supplySize++;
		}
	}

	/**
	 * @param propertyResolvers
	 */
	@Inject(type = InjectType.Selectable)
	public void setPropertyResolvers(PropertyResolver[] propertyResolvers) {
		List<PropertyResolver> propertyResolveList = new ArrayList<PropertyResolver>();
		Class<?> objectClass = KernelClass.argumentClass(getClass());
		for (PropertyResolver propertyResolver : propertyResolvers) {
			if (objectClass == KernelClass.argumentClass(propertyResolver.getClass())) {
				propertyResolveList.add(propertyResolver);
			}
		}

		if (!propertyResolveList.isEmpty()) {
			if (!Orderable.class.isAssignableFrom(objectClass)) {
				KernelList.sortCommonObjects(propertyResolveList);
			}

			this.propertyResolvers = KernelCollection.toArray(propertyResolveList, PropertyResolver.class);
		}
	}

	/**
	 * @return the supplyIndex
	 */
	public int getSupplyIndex() {
		return supplyIndex;
	}

	/**
	 * @param beanClass
	 * @return
	 */
	public final Map<String, PropertyData> getPropertyMap(Class<?> beanClass) {
		return PropertyUtils.getPropertyMap(beanClass, this).getNameMapPropertyData();
	}

	/**
	 * @param propertyData
	 * @return
	 */
	public final T getPropertyObject(PropertyData propertyData) {
		return (T) propertyData.getPropertyDatas()[supplyIndex];
	}

	/**
	 * @param property
	 * @param field
	 * @return
	 */
	public O getPropertyObject(O propertyObject, Field field) {
		if (propertyResolvers != null) {
			PropertyObject object = propertyObject;
			for (PropertyResolver propertyResolver : propertyResolvers) {
				object = propertyResolver.getPropertyObject(object, field);
			}

			propertyObject = (O) object;
		}

		return propertyObject;
	}

	/**
	 * @param property
	 * @param method
	 * @return
	 */
	public O getPropertyObjectGetter(O propertyObject, Method method) {
		if (propertyResolvers != null) {
			PropertyObject object = propertyObject;
			for (PropertyResolver propertyResolver : propertyResolvers) {
				object = propertyResolver.getPropertyObjectGetter(object, method);
			}

			propertyObject = (O) object;
		}

		return propertyObject;
	}

	/**
	 * @param propertyObject
	 * @param method
	 * @return
	 */
	public O getPropertyObjectSetter(O propertyObject, Method method) {
		if (propertyResolvers != null) {
			PropertyObject object = propertyObject;
			for (PropertyResolver propertyResolver : propertyResolvers) {
				object = propertyResolver.getPropertyObjectSetter(object, method);
			}

			propertyObject = (O) object;
		}

		return propertyObject;
	}

	/**
	 * @param propertyObject
	 * @param propertyInfos
	 * @return
	 */
	public O getPropertyObject(O propertyObject, PropertyInfo[] propertyInfos) {
		if (propertyResolvers != null) {
			PropertyObject object = propertyObject;
			for (PropertyResolver propertyResolver : propertyResolvers) {
				object = propertyResolver.getPropertyObject(object, propertyInfos);
			}

			propertyObject = (O) object;
		}

		return propertyObject;
	}
}
