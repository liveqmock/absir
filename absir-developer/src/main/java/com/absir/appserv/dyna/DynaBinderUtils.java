/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-10 上午11:06:13
 */
package com.absir.appserv.dyna;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import com.absir.appserv.configure.xls.XlsBase;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.appserv.system.helper.HelperJson;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.bean.basis.Configure;
import com.absir.bean.inject.value.Started;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.dyna.DynaConvert;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.orm.hibernate.SessionFactoryUtils;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Configure
public class DynaBinderUtils extends DynaBinder {

	@Started
	private static void started() {
		DynaBinder.INSTANCE.addConvert(new DynaConvert() {

			@Override
			public Object to(Object obj, String name, Class<?> toClass, BreakException breakException) throws Exception {
				// TODO Auto-generated method stub
				if (breakException == null) {
					if (XlsBase.class.isAssignableFrom(toClass)) {
						Object xlsBase = XlsUtils.findXlsBean((Class<? extends XlsBase>) toClass, obj);
						if (xlsBase == null) {
							throw new BreakException();
						}

						return xlsBase;
					}
				}

				return null;
			}

			@Override
			public Object mapTo(Map<?, ?> map, String name, Class<?> toClass, BreakException breakException) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		});

		DynaBinder.INSTANCE.addConvert(new DynaConvert() {

			@Override
			public Object to(Object obj, String name, Class<?> toClass, BreakException breakException) throws Exception {
				// TODO Auto-generated method stub
				if (breakException == null) {
					if (name == null) {
						name = SessionFactoryUtils.getEntityNameNull(toClass);
					}

					if (name != null) {
						Object entityObject = CrudServiceUtils.find(name, obj, null);
						if (entityObject == null) {
							throw new BreakException();
						}

						return entityObject;
					}
				}

				return null;
			}

			@Override
			public Object mapTo(Map<?, ?> map, String name, Class<?> toClass, BreakException breakException) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	/**
	 * @param cls
	 * @return
	 */
	public static boolean is(Class cls) {
		return KernelClass.isBasicClass(cls) || Date.class.isAssignableFrom(cls) || Enum.class.isAssignableFrom(cls);
	}

	/**
	 * @param obj
	 * @param toClass
	 * @return
	 */
	public static <T> T to(Object obj, Class<T> toClass) {
		if (obj == null || toClass.isAssignableFrom(obj.getClass())) {
			return (T) obj;
		}

		if (toClass.isAssignableFrom(String.class)) {
			if (!is(obj.getClass())) {
				return (T) HelperJson.encodeNull(obj);
			}

		} else {
			if (obj instanceof String && !is(toClass)) {
				return HelperJson.decodeNull((String) obj, toClass);
			}
		}

		return DynaBinder.to(obj, toClass);
	}

	/**
	 * @param obj
	 * @param toType
	 */
	public static Object to(Object obj, Type toType) {
		if (obj == null) {
			return null;
		}

		if (toType instanceof Class) {
			return to(obj, (Class) toType);
		}

		if (obj instanceof String) {
			return HelperJson.decodeNull((String) obj, toType);
		}

		return DynaBinder.INSTANCE.bind(obj, null, toType);
	}
}
