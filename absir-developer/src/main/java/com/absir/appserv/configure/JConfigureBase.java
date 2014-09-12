/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-13 下午5:04:15
 */
package com.absir.appserv.configure;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.absir.appserv.dyna.DynaBinderUtils;
import com.absir.appserv.system.bean.JConfigure;
import com.absir.appserv.system.service.BeanService;
import com.absir.core.kernel.KernelReflect;
import com.absir.orm.hibernate.SessionFactoryUtils;

/**
 * @author absir
 * 
 */
public class JConfigureBase {

	/** fieldMapConfigure */
	protected transient Map<Field, JConfigure> fieldMapConfigure = new HashMap<Field, JConfigure>();

	/**
	 * @param id
	 * @return
	 */
	protected String getIdentitier(String id) {
		return getClass().getName() + '*' + id;
	}

	/**
	 * @param value
	 * @param fieldType
	 * @return
	 */
	protected Object set(String value, Field field) {
		Class<? extends Serializable> identifierType = SessionFactoryUtils.getIdentifierType(null, field.getType(), SessionFactoryUtils.get().getSessionFactory());
		if (identifierType == null) {
			return DynaBinderUtils.to(value, field.getGenericType());

		} else {
			return BeanService.ME.get(field.getType(), DynaBinderUtils.to(value, identifierType));
		}
	}

	/**
	 * @param fieldName
	 * @param locale
	 * @return
	 */
	protected String getFieldKey(String fieldName, Locale locale) {
		return fieldName + '@' + locale;
	}

	/**
	 * 
	 */
	public final void merge() {
		for (Entry<Field, JConfigure> entry : fieldMapConfigure.entrySet()) {
			JConfigure configure = entry.getValue();
			configure.setValue(DynaBinderUtils.to(KernelReflect.get(this, entry.getKey()), String.class));
			mergeConfigure(configure);
		}
	}

	/**
	 * @param configure
	 */
	protected void mergeConfigure(JConfigure configure) {
		BeanService.ME.merge(configure);
	}
}
