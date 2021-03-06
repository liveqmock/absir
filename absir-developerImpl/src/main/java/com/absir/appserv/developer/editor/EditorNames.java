/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-30 下午5:23:31
 */
package com.absir.appserv.developer.editor;

import com.absir.bean.inject.value.Bean;
import com.absir.orm.value.JaNames;
import com.absir.property.PropertyResolverAbstract;

/**
 * @author absir
 * 
 */
@Bean
public class EditorNames extends PropertyResolverAbstract<EditorObject, JaNames> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotation
	 * (com.absir.property.PropertyObject, java.lang.annotation.Annotation)
	 */
	@Override
	public EditorObject getPropertyObjectAnnotation(EditorObject propertyObject, JaNames annotation) {
		// TODO Auto-generated method stub
		if (propertyObject == null) {
			propertyObject = new EditorObject();
		}

		propertyObject.setKeyName(annotation.key());
		propertyObject.setValueName(annotation.value());
		return propertyObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotationValue
	 * (com.absir.property.PropertyObject, java.lang.String)
	 */
	@Override
	public EditorObject getPropertyObjectAnnotationValue(EditorObject propertyObject, String annotationValue) {
		// TODO Auto-generated method stub
		if (propertyObject == null) {
			propertyObject = new EditorObject();
		}

		String[] names = annotationValue.split(",");
		if (names.length == 1) {
			propertyObject.setValueName(names[0]);

		} else if (names.length == 2) {
			propertyObject.setKeyName(names[0]);
			propertyObject.setValueName(names[1]);
		}

		return propertyObject;
	}

}
