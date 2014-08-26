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
import com.absir.appserv.lang.ILangBase;
import com.absir.appserv.lang.LangBundleImpl;
import com.absir.appserv.system.bean.JConfigure;
import com.absir.appserv.system.service.BeanService;
import com.absir.core.kernel.KernelObject;
import com.absir.core.kernel.KernelReflect;
import com.absir.orm.hibernate.SessionFactoryUtils;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class JConfigureBase implements ILangBase {

	/** fieldMapConfigure */
	protected transient Map<Field, JConfigure> fieldMapConfigure = new HashMap<Field, JConfigure>();

	/** fieldMapConfigureLang */
	protected transient Map<String, JConfigureLang> fieldMapConfigureLang = new HashMap<String, JConfigureLang>();

	/**
	 * @author absir
	 *
	 */
	protected static class JConfigureLang {

		/** type */
		public Class<?> type;

		/** value */
		public Object value;

		/** configure */
		public JConfigure configure;

	}

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
	 * @param fieldName
	 * @param locale
	 * @param type
	 * @return
	 */
	protected JConfigureLang getConfigureLang(String fieldName, Locale locale, Class<?> type) {
		String fieldKey = getFieldKey(fieldName, locale);
		JConfigureLang configureLang = fieldMapConfigureLang.get(fieldKey);
		if (configureLang == null) {
			configureLang = new JConfigureLang();
			String id = getIdentitier(fieldKey);
			JConfigure configure = BeanService.ME.get(JConfigure.class, id);
			if (configure == null) {
				configure = new JConfigure();
				configure.setId(id);
				configureLang.value = KernelObject.declaredGet(this, fieldName);

			} else {
				configureLang.value = configure.getValue();
			}

			configureLang.configure = configure;
		}

		if (type != null && configureLang.type == null) {
			configureLang.type = type;
			configureLang.value = DynaBinderUtils.to(configureLang.value, type);
		}

		return configureLang;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.lang.ILangBase#getLang(java.lang.String,
	 * java.util.Locale, java.lang.Class)
	 */
	@Override
	public <T> T getLang(String fieldName, Locale locale, Class<T> type) {
		// TODO Auto-generated method stub
		return (T) getConfigureLang(fieldName, locale, type).value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.lang.ILangBase#setLang(java.lang.String,
	 * java.util.Locale, java.lang.Object)
	 */
	@Override
	public void setLang(String fieldName, Locale locale, Object value) {
		// TODO Auto-generated method stub
		JConfigureLang configureLang = getConfigureLang(fieldName, locale, null);
		configureLang.value = DynaBinderUtils.to(value, configureLang.type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.lang.ILangBase#setLangEntity(java.lang.String)
	 */
	@Override
	public void setLangEntity(String value) {
		// TODO Auto-generated method stub
		LangBundleImpl.setLangEntity(this, value);
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

		for (JConfigureLang configureLang : fieldMapConfigureLang.values()) {
			JConfigure configure = configureLang.configure;
			configure.setValue(DynaBinderUtils.to(configureLang.value, String.class));
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
