/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-24 上午11:08:07
 */
package com.absir.appserv.system.helper;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.absir.core.base.IBase;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelString;
import com.absir.core.kernel.KernelString.ImplodeBuilder;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class HelperString extends StringUtils {

	/** PARAM_MAP_IMPLODE_BUILDER */
	private static final ImplodeBuilder PARAM_MAP_IMPLODE_BUILDER = new ImplodeBuilder() {

		@Override
		public Object glue(StringBuilder builder, Object glue, int index, Object value, Object target) {
			// TODO Auto-generated method stub
			if (index == 0 && value != null && value instanceof IBase) {
				value = ((IBase) value).getId();
			}

			return value;
		}
	};

	/**
	 * @param str
	 * @param keyClass
	 * @param valueClass
	 * @return
	 */
	public static String paramMap(Map<?, ?> paramMap) {
		if (paramMap == null) {
			return null;
		}

		return KernelString.implode(paramMap, PARAM_MAP_IMPLODE_BUILDER, null, ",");
	}

	/**
	 * @param str
	 * @param keyClass
	 * @param valueClass
	 * @return
	 */
	public static <K, V> LinkedHashMap<K, V> paramMap(String str, Class<K> keyClass, Class<V> valueClass) {
		if (str == null) {
			return null;
		}

		String[] paramArray = HelperString.split(str, ',');
		LinkedHashMap<K, V> paramMap = new LinkedHashMap<K, V>();
		int length = paramArray.length;
		for (int i = 1; i < length; i += 2) {
			K key = DynaBinder.to(paramArray[i - 1], keyClass);
			if (key != null) {
				V val = DynaBinder.to(paramArray[i], valueClass);
				if (val != null) {
					paramMap.put(key, val);
				}
			}
		}

		return paramMap;
	}
}
