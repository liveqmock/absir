/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-12 下午4:40:28
 */
package com.absir.appserv.system.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.absir.appserv.system.bean.value.JaLang;
import com.absir.context.lang.LangBundle;
import com.absir.core.kernel.KernelMap;
import com.absir.core.kernel.KernelReflect;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class HelperLang {

	/**
	 * @param lang
	 * @param name
	 * @return
	 */
	public static String getLangName(JaLang lang, String name) {
		return getLangName(lang == null ? null : lang.value(), lang == null ? null : lang.tag(), name);
	}

	/**
	 * @param lang
	 * @param tag
	 * @param name
	 * @return
	 */
	public static String getLangName(String lang, String tag, String name) {
		if (!KernelString.isEmpty(tag)) {
			int length = tag.length();
			if (length > 1) {
				if (tag.charAt(0) == '.') {
					tag = lang + tag.substring(1);

				} else if (tag.charAt(length - 1) == '.') {
					tag = lang + tag.substring(0, length - 1);
				}
			}

			name = tag;
		}

		if (lang != null) {
			LangBundle.ME.setResourceLang(name, lang);
		}

		return name;
	}

	/**
	 * @param type
	 * @return
	 */
	public static String getTypeCaption(Class<?> type) {
		return getTypeCaption(type, null);
	}

	/**
	 * @param type
	 * @param name
	 * @return
	 */
	public static String getTypeCaption(Class<?> type, String typeName) {
		return getLangName(type.getAnnotation(JaLang.class), typeName == null ? type.getSimpleName() : typeName);
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
		return getLangName(field.getAnnotation(JaLang.class), KernelString.capitalize(field.getName()));
	}

	/**
	 * @param lang
	 * @param cls
	 * @return
	 */
	public static String getFieldCaption(String lang, String tag, String field, Class<?> cls) {
		return getLangName(lang, tag, KernelString.capitalize(field));
	}

	/**
	 * @param method
	 * @return
	 */
	public static String getMethodCaption(Method method) {
		return getMethodCaption(method, method.getDeclaringClass());
	}

	/**
	 * @param method
	 * @param cls
	 * @return
	 */
	public static String getMethodCaption(Method method, Class<?> cls) {
		return getLangName(method.getAnnotation(JaLang.class), KernelString.capitalize(method.getName()));
	}

	/**
	 * @param enumerate
	 * @return
	 */
	public static String getEnumNameCaption(Enum enumerate) {
		return getLangName(KernelReflect.declaredField(enumerate.getClass(), enumerate.name()).getAnnotation(JaLang.class), enumerate.name());
	}

	/**
	 * @param lang
	 * @return
	 */
	public static String getCaptionLang(String lang) {
		String name = KernelMap.getKey(LangBundle.ME.getResourceBundle(), lang);
		if (name == null) {
			name = lang;
		}

		return name;
	}
}
