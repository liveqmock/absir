/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-2 下午7:31:04
 */
package com.absir.validator;

import java.util.Map;
import java.util.regex.Pattern;

import com.absir.bean.inject.value.Bean;
import com.absir.property.PropertyResolverAbstract;
import com.absir.validator.value.Regex;

/**
 * @author absir
 * 
 */
@Bean
public class ValidatorRegex extends PropertyResolverAbstract<ValidatorObject, Regex> {

	/**
	 * @param propertyObject
	 * @param pattern
	 * @return
	 */
	public ValidatorObject getPropertyObjectPattern(ValidatorObject propertyObject, final String regex) {
		if (propertyObject == null) {
			propertyObject = new ValidatorObject();
		}

		final Pattern pattern = java.util.regex.Pattern.compile(regex);
		propertyObject.addValidator(new Validator() {

			@Override
			public String validateValue(Object value) {
				// TODO Auto-generated method stub
				if (value instanceof String && !pattern.matcher((CharSequence) value).matches()) {
					return regex + " Regex";
				}

				return null;
			}

			@Override
			public String getValidateClass(Map<String, Object> validatorMap) {
				// TODO Auto-generated method stub
				validatorMap.put("pattern", regex);
				return null;
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
	public ValidatorObject getPropertyObjectAnnotation(ValidatorObject propertyObject, Regex annotation) {
		// TODO Auto-generated method stub
		return getPropertyObjectPattern(propertyObject, annotation.value());
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
		return getPropertyObjectPattern(propertyObject, annotationValue);
	}
}
