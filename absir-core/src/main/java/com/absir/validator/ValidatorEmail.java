/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-2 下午7:31:04
 */
package com.absir.validator;

import java.util.regex.Pattern;

import com.absir.bean.inject.value.Bean;
import com.absir.property.PropertyResolverAbstract;
import com.absir.validator.value.Email;

/**
 * @author absir
 * 
 */
@Bean
public class ValidatorEmail extends PropertyResolverAbstract<ValidatorObject, Email> {

	/** PATTERN */
	public static final Pattern PATTERN = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.property.PropertyResolverAbstract#getPropertyObjectAnnotation
	 * (com.absir.property.PropertyObject, java.lang.annotation.Annotation)
	 */
	@Override
	public ValidatorObject getPropertyObjectAnnotation(ValidatorObject propertyObject, Email annotation) {
		// TODO Auto-generated method stub
		if (propertyObject == null) {
			propertyObject = new ValidatorObject();
		}

		propertyObject.addValidator(new Validator() {

			@Override
			public String validateValue(Object value) {
				// TODO Auto-generated method stub
				if (value != null && value instanceof String && !PATTERN.matcher((String) value).find()) {
					return "Email";
				}

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
