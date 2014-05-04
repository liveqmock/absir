/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-2 下午7:31:04
 */
package com.absir.validator;

import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelDyna;
import com.absir.property.PropertyResolverAbstract;
import com.absir.validator.value.Digits;

/**
 * @author absir
 * 
 */
@Bean
public class ValidatorDigits extends PropertyResolverAbstract<ValidatorObject, Digits> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotation
	 * (com.absir.property.PropertyObject, java.lang.annotation.Annotation)
	 */
	@Override
	public ValidatorObject getPropertyObjectAnnotation(ValidatorObject propertyObject, Digits annotation) {
		// TODO Auto-generated method stub
		if (propertyObject == null) {
			propertyObject = new ValidatorObject();
		}

		propertyObject.addValidator(new Validator() {

			@Override
			public String validateValue(Object value) {
				// TODO Auto-generated method stub
				if (value != null && KernelDyna.to(value, Integer.class) == null) {
					return "Digits";
				}

				return null;
			}

			@Override
			public Object[] getValidateParameters() {
				// TODO Auto-generated method stub
				return null;
			}

		});

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
	public ValidatorObject getPropertyObjectAnnotationValue(ValidatorObject propertyObject, String annotationValue) {
		// TODO Auto-generated method stub
		return getPropertyObjectAnnotation(propertyObject, null);
	}
}
