/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-12 下午4:40:28
 */
package com.absir.appserv.system.helper;

import java.lang.reflect.Field;

import com.absir.appserv.system.bean.value.JaLang;
import com.absir.core.kernel.KernelReflect;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class HelperLang {

	/**
	 * @param type
	 * @return
	 */
	public static String getTypeCaption(Class<?> type) {
		JaLang jaLang = type.getAnnotation(JaLang.class);
		if (jaLang == null) {
			return type.getSimpleName();
		}

		return jaLang.value();
	}

	/**
	 * @param type
	 * @param name
	 * @return
	 */
	public static String getTypeCaption(Class<?> type, String typeName) {
		JaLang jaLang = type.getAnnotation(JaLang.class);
		if (jaLang == null) {
			return typeName == null ? type.getSimpleName() : typeName;
		}

		return jaLang.value();
	}

	/**
	 * @param enumerate
	 * @return
	 */
	public static String getFieldCaption(Field field) {
		return getFieldCaption(field, field.getDeclaringClass());
	}

	/**
	 * @param field
	 * @param cls
	 * @return
	 */
	public static String getFieldCaption(Field field, Class<?> cls) {
		JaLang jaLang = field.getAnnotation(JaLang.class);
		if (jaLang == null) {
			return field.getName();
		}

		return jaLang.value();
	}

	/**
	 * @param enumerate
	 * @return
	 */
	public static String getEnumNameCaption(Enum enumerate) {
		JaLang jaLang = KernelReflect.declaredField(enumerate.getClass(), enumerate.name()).getAnnotation(JaLang.class);
		if (jaLang == null) {
			return enumerate.name();
		}

		return jaLang.value();
	}
}
