/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午1:31:09
 */
package com.absir.core.util;

import java.io.Serializable;
import java.util.Map;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UtilAbsir {

	/**
	 * @param cls
	 * @param id
	 * @return
	 */
	public static String getId(Class<?> cls, Serializable id) {
		return cls.getName() + '@' + id;
	}

	/**
	 * @param id
	 * @param tokenMap
	 */
	public static Object getToken(Object id, Map<?, ?> tokenMap) {
		Object token = tokenMap.get(id);
		if (token == null) {
			synchronized (tokenMap) {
				token = tokenMap.get(id);
				if (token == null) {
					token = new Object();
					((Map) tokenMap).put(id, token);
				}
			}
		}

		return token;
	}

	/**
	 * @param cls
	 * @param id
	 * @param tokenMap
	 * @return
	 */
	public static Object getToken(Class<?> cls, Serializable id, Map<?, ?> tokenMap) {
		return getToken(getId(cls, id), tokenMap);
	}
}
