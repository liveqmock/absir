/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-13 下午5:08:29
 */
package com.absir.appserv.configure;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.absir.aop.AopBeanDefine;
import com.absir.appserv.lang.LangBundleImpl;
import com.absir.appserv.system.bean.JConfigure;
import com.absir.appserv.system.helper.HelperAccessor;
import com.absir.appserv.system.service.BeanService;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelObject;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked" })
public abstract class JConfigureUtils {

	/** Configure_Class_Map_Instance */
	private static Map<Serializable, JConfigureBase> Configure_Class_Map_Instance = new HashMap<Serializable, JConfigureBase>();

	/** Configure_Class_Map_Class */
	private static Map<Class<? extends JConfigureBase>, Class<? extends JConfigureBase>> Configure_Class_Map_Class = new HashMap<Class<? extends JConfigureBase>, Class<? extends JConfigureBase>>();

	/**
	 * @param cls
	 * @param configureClass
	 */
	public static void put(Class<? extends JConfigureBase> cls, Class<? extends JConfigureBase> configureClass) {
		Configure_Class_Map_Class.put(cls, configureClass);
	}

	/**
	 * @param cls
	 */
	public static <T extends JConfigureBase> T getConfigure(Class<T> cls) {
		JConfigureBase configure = Configure_Class_Map_Instance.get(cls);
		if (configure == null) {
			synchronized (cls) {
				configure = Configure_Class_Map_Instance.get(cls);
				if (configure == null) {
					Class<? extends JConfigureBase> configureClass = Configure_Class_Map_Class.get(cls);
					if (configureClass == null) {
						configure = KernelClass.newInstance(cls);
						configure = LangBundleImpl.ME.getLangProxy(cls.getSimpleName(), configure);
						try {
							initConfigure(configure);

						} catch (Throwable e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					} else {
						configure = getConfigure(configureClass);
					}

					Configure_Class_Map_Instance.put(cls, configure);
				}
			}
		}

		return (T) configure;
	}

	/**
	 * @param configureBase
	 */
	private static void initConfigure(final JConfigureBase configureBase) {
		String identitier = configureBase.getIdentitier();
		Map<String, JConfigure> configureMap = new HashMap<String, JConfigure>();
		for (JConfigure configure : (List<JConfigure>) BeanService.ME.list("JConfigure", null, 0, 0, "o.id", identitier)) {
			configureMap.put(configure.getName(), configure);
		}

		for (Field field : HelperAccessor.getFields(configureBase.getClass())) {
			JConfigure configure = configureMap.get(field.getName());
			if (configure == null) {
				configure = new JConfigure();
				configure.setId(identitier);
				configure.setName(field.getName());

			} else {
				KernelObject.declaredSetter(configureBase, field, configureBase.set(configure.getValue(), field));
			}

			configureBase.fieldMapConfigure.put(field, configure);
		}
	}

	/**
	 * @param cls
	 * @param args
	 * @return
	 */
	public static <T extends JConfigureBase> String getConfigureId(Class<T> cls, Object... args) {
		return cls.getName() + KernelString.implode(args, ',');
	}

	/**
	 * @param cls
	 * @param initargs
	 * @return
	 */
	public <T extends JConfigureBase> T getConfigure(Class<T> cls, Object... initargs) {
		return initargs.length == 0 ? getConfigure(cls) : getConfigure(cls, getConfigureId(cls, initargs), initargs);
	}

	/**
	 * @param cls
	 * @param configureKey
	 * @param initargs
	 * @return
	 */
	public static <T extends JConfigureBase> T getConfigure(Class<T> cls, String configureKey, Object... initargs) {
		JConfigureBase configure = Configure_Class_Map_Instance.get(configureKey);
		if (configure == null) {
			synchronized (JConfigureUtils.class) {
				configure = Configure_Class_Map_Instance.get(configureKey);
				if (configure == null) {
					configure = AopBeanDefine.instanceBeanObject(cls, initargs);
					try {
						initConfigure(configure);

					} catch (Throwable e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					Configure_Class_Map_Instance.put(configureKey, configure);
				}
			}
		}

		return (T) configure;
	}

	/**
	 * @param cls
	 */
	public static <T extends JConfigureBase> void clearConfigure(Class<T> cls) {
		synchronized (cls) {
			JConfigureBase configure = Configure_Class_Map_Instance.get(cls);
			if (configure != null) {
				configure.merge();
				Configure_Class_Map_Instance.remove(cls);
			}
		}
	}

	/**
	 * @param cls
	 * @param initargs
	 */
	public static <T extends JConfigureBase> void clearConfigure(Class<T> cls, Object... initargs) {
		clearConfigure(getConfigureId(cls, initargs));
	}

	/**
	 * @param configureKey
	 */
	public static <T extends JConfigureBase> void clearConfigure(String configureKey) {
		synchronized (JConfigureUtils.class) {
			JConfigureBase configure = Configure_Class_Map_Instance.get(configureKey);
			if (configure != null) {
				configure.merge();
				Configure_Class_Map_Instance.remove(configureKey);
			}
		}
	}
}
