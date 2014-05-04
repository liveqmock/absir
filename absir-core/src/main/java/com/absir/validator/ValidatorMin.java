/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-2 下午7:31:04
 */
package com.absir.validator;

import com.absir.bean.inject.value.Bean;
import com.absir.core.dyna.DynaBinder;
import com.absir.property.PropertyResolverAbstract;
import com.absir.validator.value.Min;

/**
 * @author absir
 * 
 */
@Bean
public class ValidatorMin extends PropertyResolverAbstract<ValidatorObject, Min> {

	/**
	 * @param propertyObject
	 * @param min
	 * @return
	 */
	public ValidatorObject getPropertyObjectMin(ValidatorObject propertyObject, final int min) {
		if (propertyObject == null) {
			propertyObject = new ValidatorObject();
		}

		propertyObject.addValidator(new Validator() {

			@Override
			public String validateValue(Object value) {
				// TODO Auto-generated method stub
				if (DynaBinder.to(value, int.class) < min) {
					return min + " Min";
				}

				return null;
			}

			@Override
			public Object[] getValidateParameters() {
				// TODO Auto-generated method stub
				return new Object[] { min };
			}
		});

		return propertyObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotation
	 * (com.absir.property.PropertyObject, java.lang.annotation.Annotation)
	 */
	@Override
	public ValidatorObject getPropertyObjectAnnotation(ValidatorObject propertyObject, Min annotation) {
		// TODO Auto-generated method stub
		return getPropertyObjectMin(propertyObject, annotation.value());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotationValue
	 * (com.absir.property.PropertyObject, java.lang.String)
	 */
	@Override
	public ValidatorObject getPropertyObjectAnnotationValue(ValidatorObject propertyObject, String annotationValue) {
		// TODO Auto-generated method stub
		return getPropertyObjectMin(propertyObject, DynaBinder.to(annotationValue, int.class));
	}
}
