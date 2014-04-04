/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-17 下午1:23:18
 */
package com.absir.appserv.configure.xls;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.absir.appserv.system.helper.HelperString;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XlsAccessorParamMap extends XlsAccessorParam {

	/** keyClass */
	private Class<?> keyClass;

	/** valueClass */
	private Class<?> valueClass;

	/**
	 * @param field
	 * @param beanClass
	 */
	public XlsAccessorParamMap(Field field, Class<?> beanClass, Class<?> valueClass) {
		super(field, beanClass);
		// TODO Auto-generated constructor stub
		this.keyClass = beanClass;
		this.valueClass = valueClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.configure.xls.XlsAccessorParam#getParamValues(java.
	 * lang.String)
	 */
	@Override
	protected Object getParamValues(String value) {
		if (KernelString.isEmpty(value)) {
			return null;
		}

		String[] params = HelperString.split(value, ',');
		Map<Object, Object> paramMap = new TreeMap<Object, Object>();
		int length = params.length;
		XlsDao xlsDao = xlsClass == null ? null : XlsUtils.getXlsDao(xlsClass);
		for (int i = 1; i < length; i += 2) {
			Object key = params[i - 1];
			if (xlsDao == null) {
				key = DynaBinder.to(key, keyClass);

			} else {
				key = xlsDao.find(key);
			}

			if (key == null) {
				continue;
			}

			paramMap.put(key, DynaBinder.to(params[i], valueClass));
		}

		return DynaBinder.INSTANCE.bind(paramMap, null, getField().getGenericType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.configure.xls.XlsAccessorParam#getValueParams(java.
	 * lang.Object)
	 */
	@Override
	protected String getValueParams(Object value) {
		if (value == null) {
			return null;
		}

		Map<Object, Object> paramMap = DynaBinder.to(value, Map.class);
		Map<String, String> params = new TreeMap<String, String>();
		for (Entry<Object, Object> entry : paramMap.entrySet()) {
			Object key = entry.getKey();
			if (xlsClass != null) {
				key = ((XlsBase) key).getId();
			}

			params.put(DynaBinder.to(key, String.class), DynaBinder.to(entry.getValue(), String.class));
		}

		return KernelString.implode(params, ',');
	}

}
