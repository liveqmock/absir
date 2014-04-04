/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-23 下午4:31:39
 */
package com.absir.bean.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.absir.bean.basis.ParamName;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilParameter;
import com.absir.core.util.UtilParameter.ClassReader;

/**
 * @author absir
 * 
 */
public class BeanDefineDiscover {

	/** Instance */
	public static BeanDefineDiscover Instance;

	/** MethodOrCtorMapParameterNames */
	private static Map<AccessibleObject, String[]> MethodOrCtorMapParameterNames = new HashMap<AccessibleObject, String[]>();

	/** classMapDiscover */
	Map<Class<?>, ClassReader> classMapDiscover = new HashMap<Class<?>, ClassReader>();

	/**
	 * 
	 */
	private BeanDefineDiscover() {
	}

	/**
	 * @param methodOrCtor
	 * @param annotations
	 * @return
	 */
	public String[] getParamterNames(AccessibleObject methodOrCtor) {
		Class<?> beanType;
		if (methodOrCtor instanceof Method) {
			Method method = (Method) methodOrCtor;
			if (method.getParameterTypes().length == 0) {
				return KernelLang.NULL_STRINGS;
			}

			beanType = method.getDeclaringClass();

		} else {
			Constructor<?> constructor = (Constructor<?>) methodOrCtor;
			if (constructor.getParameterTypes().length == 0) {
				return KernelLang.NULL_STRINGS;
			}

			beanType = constructor.getDeclaringClass();
		}

		ClassReader classReader = classMapDiscover.get(beanType);
		if (classReader == null) {
			synchronized (classMapDiscover) {
				classReader = classMapDiscover.get(beanType);
				if (classReader == null) {
					classReader = UtilParameter.getClassReader(beanType);
					if (classReader == null) {
						return null;
					}

					classMapDiscover.put(beanType, classReader);
				}
			}
		}

		return UtilParameter.lookupParameterNames(classReader, methodOrCtor);
	}

	/**
	 * 
	 */
	public static void open() {
		if (Instance == null) {
			Instance = new BeanDefineDiscover();
		}
	}

	/**
	 * @param methodOrCtor
	 * @return
	 */
	public static String[] paramterNames(AccessibleObject methodOrCtor) {
		return paramterNames(methodOrCtor, methodOrCtor instanceof Method ? ((Method) methodOrCtor).getParameterAnnotations() : ((Constructor<?>) methodOrCtor).getParameterAnnotations());
	}

	/**
	 * @param methodOrCtor
	 * @param parameterAnnotations
	 * @return
	 */
	public static String[] paramterNames(AccessibleObject methodOrCtor, Annotation[][] parameterAnnotations) {
		if (parameterAnnotations == null || parameterAnnotations.length == 0) {
			return null;
		}

		String[] parameterNames = MethodOrCtorMapParameterNames.get(methodOrCtor);
		if (parameterNames == null) {
			if (Instance != null) {
				parameterNames = Instance.getParamterNames(methodOrCtor);
			}

			int length = parameterAnnotations.length;
			if (parameterNames == null) {
				parameterNames = new String[length];
			}

			for (int i = 0; i < length; i++) {
				for (Annotation annotation : parameterAnnotations[i]) {
					if (annotation instanceof ParamName) {
						String name = ((ParamName) annotation).value();
						if (!KernelString.isEmpty(name)) {
							parameterNames[i] = name;
						}

						break;
					}
				}
			}

			MethodOrCtorMapParameterNames.put(methodOrCtor, parameterNames);
		}

		return parameterNames;
	}

	/**
	 * 
	 */
	public static void clear() {
		if (Instance != null) {
			Instance.classMapDiscover.clear();
		}

		MethodOrCtorMapParameterNames.clear();
	}
}
