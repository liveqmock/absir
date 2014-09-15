/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月29日 下午5:15:42
 */
package com.absir.appserv.lang;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.cglib.proxy.MethodProxy;

import com.absir.aop.AopInterceptor;
import com.absir.aop.AopProxy;
import com.absir.aop.AopProxyHandler;
import com.absir.aop.AopProxyUtils;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.dyna.DynaBinderUtils;
import com.absir.appserv.lang.value.Langs;
import com.absir.appserv.support.Developer;
import com.absir.appserv.system.bean.JEmbedSL;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.service.BeanService;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.IMethodEntry;
import com.absir.bean.inject.InjectBeanFactory;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Stopping;
import com.absir.context.lang.LangBundle;
import com.absir.core.base.IBase;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelReflect;
import com.absir.core.kernel.KernelString;
import com.absir.server.on.OnPut;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Base(order = -1)
@Bean
public class LangBundleImpl extends LangBundle implements IMethodEntry<Entry<String, Class<?>>> {

	/** entityMapIdMapNameMapValue */
	private static Map<String, Map<String, Map<String, Map<String, Object>>>> entityMapIdMapNameMapValue = new HashMap<String, Map<String, Map<String, Map<String, Object>>>>();

	/** ME */
	public static final LangBundleImpl ME = BeanFactoryUtils.get(LangBundleImpl.class);

	/** langInterfaces */
	private Set<Class<?>> langInterfaces = new HashSet<Class<?>>();

	/** methodMapLangEntryImpl */
	private Map<Method, LangEntryImpl> methodMapLangEntryImpl = new HashMap<Method, LangEntryImpl>();

	/** methodLangEmbededs */
	private List<LangEmbeded> methodLangEmbededs = new ArrayList<LangBundleImpl.LangEmbeded>();

	/** entityMapLangInterceptors */
	private Map<String, Map<Method, Entry<String, Class<?>>>> entityMapLangInterceptors = new HashMap<String, Map<Method, Entry<String, Class<?>>>>();

	/** entityClassMapLangInterceptors */
	private Map<Class<?>, Map<Method, Entry<String, Class<?>>>> entityClassMapLangInterceptors = new HashMap<Class<?>, Map<Method, Entry<String, Class<?>>>>();

	/**
	 * @param entityName
	 * @param id
	 * @return
	 */
	public static Map<String, Map<String, Object>> getLangNameMapValue(String entityName, String id) {
		Map<String, Map<String, Map<String, Object>>> idMapNameMapValue = entityMapIdMapNameMapValue.get(entityName);
		if (idMapNameMapValue == null) {
			idMapNameMapValue = new HashMap<String, Map<String, Map<String, Object>>>();
			entityMapIdMapNameMapValue.put(entityName, idMapNameMapValue);
		}

		Map<String, Map<String, Object>> nameMapValue = idMapNameMapValue.get(id);
		if (nameMapValue == null) {
			nameMapValue = new HashMap<String, Map<String, Object>>();
			for (Map<String, Object> value : (List<Map<String, Object>>) BeanService.ME.selectQuery("SELECT o FROM JLocale o WHERE o.entity = ? AND o.id = ?", entityName, id)) {
				nameMapValue.put((String) value.get("name"), value);
			}

			idMapNameMapValue.put(id, nameMapValue);
		}

		return nameMapValue;
	}

	/**
	 * @param entityName
	 */
	public static void clearLangNameMapValue(String entityName) {
		entityMapIdMapNameMapValue.remove(entityName);
	}

	/**
	 * @author absir
	 *
	 */
	protected static abstract class LangEntry extends ObjectEntry<String, Class<?>> {

		/**
		 * @param value
		 */
		public LangEntry() {
			super(null, null);
		}

		/**
		 * @param iterceptor
		 * @param entity
		 * @param method
		 * @param args
		 * @return
		 */
		public abstract Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args);
	}

	/**
	 * @author absir
	 *
	 */
	protected static abstract class LangEntryImpl extends LangEntry {

		/** langEntry */
		private LangEntry langEntry;

		/**
		 * @return
		 */
		public LangEntry getLangEntry() {
			if (langEntry == null) {
				langEntry = generateLangEnry();
			}

			return langEntry;
		}

		/**
		 * @return
		 */
		protected abstract LangEntry generateLangEnry();
	}

	/**
	 * @author absir
	 *
	 */
	protected static abstract class LangEmbeded extends LangEntry {

		/**
		 * @param method
		 * @return
		 */
		public abstract Class<?> componentType(Method method);

		/**
		 * @param method
		 * @return
		 */
		public boolean matching(Method method) {
			Class<?> componentType = componentType(method);
			return componentType != null;
		}
	}

	/**
	 * @author absir
	 *
	 */
	protected static class LangIterceptor implements AopInterceptor<Entry<String, Class<?>>> {

		/** entityName */
		private String entityName;

		/** id */
		private String id;

		/** langInterceptors */
		private Map<Method, Entry<String, Class<?>>> langInterceptors;

		/** nameMapValue */
		private Map<String, Map<String, Object>> nameMapValue;

		/** nameLocaleMapValue */
		private Map<JEmbedSL, Object> nameLocaleMapValue;

		/**
		 * @param entityName
		 * @param langInterceptors
		 */
		public LangIterceptor(String entityName, String id, Map<Method, Entry<String, Class<?>>> langInterceptors) {
			this.entityName = entityName;
			this.id = id;
			this.langInterceptors = langInterceptors;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#getInterface()
		 */
		@Override
		public Class<?> getInterface() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.aop.AopInterceptor#getInterceptor(com.absir.aop.AopProxyHandler
		 * , java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
		 */
		@Override
		public Entry<String, Class<?>> getInterceptor(AopProxyHandler proxyHandler, Object beanObject, Method method, Object[] args) throws Throwable {
			// TODO Auto-generated method stub
			return langInterceptors.get(method);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#before(java.lang.Object,
		 * java.util.Iterator, java.lang.Object, com.absir.aop.AopProxyHandler,
		 * java.lang.reflect.Method, java.lang.Object[],
		 * net.sf.cglib.proxy.MethodProxy)
		 */
		@Override
		public Object before(Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, MethodProxy methodProxy)
				throws Throwable {
			// TODO Auto-generated method stub
			if (interceptor.getKey() == null) {
				((LangEntry) interceptor).invoke(this, proxyHandler.getBeanObject(), method, args);

			} else {
				OnPut onPut = OnPut.get();
				if (onPut != null) {
					Integer locale = onPut.getInput().getLocalCode();
					if (ME.isLocaleCode(locale)) {
						return ((ILangBase) proxy).getLang(interceptor.getKey(), locale, interceptor.getValue());
					}
				}
			}

			return AopProxyHandler.VOID;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#after(java.lang.Object,
		 * java.lang.Object, java.lang.Object, com.absir.aop.AopProxyHandler,
		 * java.lang.reflect.Method, java.lang.Object[], java.lang.Throwable)
		 */
		@Override
		public Object after(Object proxy, Object returnValue, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, Throwable e) throws Throwable {
			// TODO Auto-generated method stub
			return returnValue;
		}

		/**
		 * @return the nameMapValue
		 */
		public Map<String, Map<String, Object>> getNameMapValue() {
			if (nameMapValue == null) {
				nameMapValue = getLangNameMapValue(entityName, id);
			}

			return nameMapValue;
		}

		/**
		 * @param nameMapValue
		 *            the nameMapValue to set
		 */
		public void setNameMapValue(Map<String, Map<String, Object>> nameMapValue) {
			this.nameMapValue = nameMapValue;
		}

		/**
		 * @param fieldName
		 * @param locale
		 * @param type
		 * @return
		 */
		public Object getLang(String fieldName, Integer locale, Class<?> type) {
			// TODO Auto-generated method stub
			if (ME.isLocaleCode(locale)) {
				return AopProxyHandler.VOID;
			}

			Object value = null;
			if (nameLocaleMapValue == null) {
				value = DynaBinderUtils.getMapValue(nameLocaleMapValue, new JEmbedSL(fieldName, (long) locale), type);
				if (value != null) {
					return value;
				}
			}

			Map<String, Object> valueLocale = getNameMapValue().get(fieldName);
			if (valueLocale != null) {
				value = DynaBinderUtils.getMapValue(valueLocale, "_" + locale, type);
			}

			return value == null ? AopProxyHandler.VOID : value;
		}

		/**
		 * @param fieldName
		 * @param locale
		 * @param value
		 * @return
		 */
		public Object setLang(String fieldName, Integer locale, Object value) {
			// TODO Auto-generated method stub
			if (ME.isLocaleCode(locale)) {
				return AopProxyHandler.VOID;

			} else {
				if (nameLocaleMapValue == null) {
					nameLocaleMapValue = new HashMap<JEmbedSL, Object>();
				}

				nameLocaleMapValue.put(new JEmbedSL(fieldName, (long) locale), value);
				return null;
			}
		}

		/**
		 * @param values
		 */
		public void setLangValues(String[] values) {
			// TODO Auto-generated method stub
			for (String value : values) {
				String[] langs = value.split(",", 3);
				if (langs.length == 3) {
					setLang(langs[0], DynaBinder.to(langs[1], Integer.class), langs[2]);
				}
			}
		}

		/**
		 * @param entity
		 * @param crud
		 */
		public void proccessCrud(Object entity, Crud crud) {
			// TODO Auto-generated method stub
			if (crud == Crud.CREATE || crud == Crud.UPDATE) {
				if (nameMapValue == null && crud == Crud.CREATE) {
					getNameMapValue();
				}

				Map<String, Map<String, Object>> mapValue = new HashMap<String, Map<String, Object>>();
				if (nameMapValue != null) {
					for (Entry<Method, Entry<String, Class<?>>> methodEntry : langInterceptors.entrySet()) {
						Entry<String, Class<?>> entry = methodEntry.getValue();
						if (!nameMapValue.containsKey(entry.getKey())) {
							Map<String, Object> value = new HashMap<String, Object>();
							mapValue.put(entry.getKey(), value);
							value.put("entityName", entityName);
							value.put("id", id);
							value.put("name", entry.getKey());
							value.put("_" + ME.getLocaleCode(), DynaBinderUtils.to(KernelReflect.invoke(entity, methodEntry.getKey()), String.class));
						}
					}

					if (nameLocaleMapValue != null) {
						for (Entry<JEmbedSL, Object> entry : nameLocaleMapValue.entrySet()) {
							Map<String, Object> value = mapValue.get(entry.getKey().getEid());
							if (value == null) {
								value = new HashMap<String, Object>();
								mapValue.put(entry.getKey().getEid(), value);
								value.put("entityName", entityName);
								value.put("id", id);
								value.put("name", entry.getKey().getEid());
							}

							value.put("_" + entry.getKey().getMid(), DynaBinderUtils.to(entry.getValue(), String.class));
						}
					}
				}

				if (!mapValue.isEmpty()) {
					BeanService.ME.mergers(mapValue.values());
				}

			} else if (crud == Crud.DELETE) {
				BeanService.ME.executeUpdate("DELETE FROM JLocale o WHERE o.entity = ?, o.id = ?", entityName, id);
			}
		}
	}

	/**
	 * @param methodMapLangEntryImpl
	 * @param methodLangEmbededs
	 */
	private static void InitLangBundleImpl(Map<Method, LangEntryImpl> methodMapLangEntryImpl, List<LangEmbeded> methodLangEmbededs) {
		for (Method method : ILangBase.class.getMethods()) {
			LangEntryImpl langEntryImpl = null;
			if ("getLang".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args) {
						// TODO Auto-generated method stub
						return iterceptor.getLang((String) args[0], (Integer) args[1], (Class<?>) args[2]);
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor inIterceptor, Object entity, Method method, Object[] args) {
								// TODO Auto-generated method stub
								return ((ILangBase) entity).getLang((String) args[0], (Integer) args[1], (Class<?>) args[2]);
							}
						};
					}
				};

			} else if ("setLang".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args) {
						// TODO Auto-generated method stub
						return iterceptor.setLang((String) args[0], (Integer) args[1], args[2]);
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args) {
								// TODO Auto-generated method stub
								Integer locale = (Integer) args[1];
								if (ME.isLocaleCode(locale)) {
									return AopProxyHandler.VOID;

								} else {
									((ILangBase) entity).setLang((String) args[0], (Integer) args[1], args[2]);
									return null;
								}
							}
						};
					}
				};

			} else if ("setLangEntity".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args) {
						// TODO Auto-generated method stub
						iterceptor.setLangValues((String[]) args[0]);
						return null;
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args) {
								// TODO Auto-generated method stub
								((ILangBase) entity).setLangValues((String[]) args[0]);
								return null;
							}
						};
					}
				};
			}

			if (langEntryImpl != null) {
				methodMapLangEntryImpl.put(method, langEntryImpl);
			}
		}

		for (Method method : ICrudBean.class.getMethods()) {
			LangEntryImpl langEntryImpl = null;
			if ("proccessCrud".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args) {
						// TODO Auto-generated method stub
						iterceptor.proccessCrud(entity, (Crud) args[0]);
						return null;
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor iterceptor, Object entity, Method method, Object[] args) {
								// TODO Auto-generated method stub
								((ICrudBean) entity).proccessCrud((Crud) args[0]);
								invoke(iterceptor, entity, method, args);
								return null;
							}
						};
					}
				};
			}

			if (langEntryImpl != null) {
				methodMapLangEntryImpl.put(method, langEntryImpl);
			}
		}

	}

	/**
	 * 
	 */
	public LangBundleImpl() {
		langInterfaces.add(ILangBase.class);
		langInterfaces.add(ICrudBean.class);
		InitLangBundleImpl(methodMapLangEntryImpl, methodLangEmbededs);
	}

	/**
	 * @param entityName
	 * @param entity
	 * @return
	 */
	public <T> T getLangProxy(String entityName, T entity) {
		if (isI18n() && entity instanceof IBase) {
			return getLangProxy(entityName, entity, ((IBase) entity).getId());
		}

		return entity;
	}

	/**
	 * @param entityName
	 * @param entity
	 * @param id
	 * @return
	 */
	public <T> T getLangProxy(String entityName, T entity, Object id) {
		if (isI18n()) {
			Map<Method, Entry<String, Class<?>>> langInterceptors = entityMapLangInterceptors.get(entityName);
			if (langInterceptors == null) {
				Class<?> entityClass = entity.getClass();
				langInterceptors = new HashMap<Method, Map.Entry<String, Class<?>>>();
				entityClassMapLangInterceptors.put(entityClass, langInterceptors);
				InjectBeanFactory.getInstance().getMethodEntries(entityClass, this);
				if (langInterceptors.isEmpty()) {
					langInterceptors = (Map<Method, Entry<String, Class<?>>>) (Object) KernelLang.NULL_MAP;

				} else {
					// 执行接口方法
					langInterceptors.putAll(methodMapLangEntryImpl);
				}

				entityMapLangInterceptors.put(entityName, langInterceptors);
				entityClassMapLangInterceptors.remove(entityClass);
			}

			if ((Object) langInterceptors == KernelLang.NULL_MAP) {
				return entity;
			}

			AopProxy proxy = AopProxyUtils.getProxy(entity, langInterfaces, false, true);
			proxy.getAopInterceptors().add(getLangIterceptor(entityName, DynaBinderUtils.getParamFromValue(id), langInterceptors));
			return (T) proxy;
		}

		return entity;
	}

	/**
	 * @param entityName
	 * @param entity
	 * @return
	 */
	public <T> Iterator<T> getLangProxy(final String entityName, final Iterator<T> entityIterator) {
		if (isI18n()) {
			return new Iterator<T>() {

				@Override
				public boolean hasNext() {
					// TODO Auto-generated method stub
					return entityIterator.hasNext();
				}

				@Override
				public T next() {
					// TODO Auto-generated method stub
					return getLangProxy(entityName, entityIterator.next());
				}

				@Override
				public void remove() {
					// TODO Auto-generated method stub
					entityIterator.remove();
				}
			};
		}

		return entityIterator;
	}

	/**
	 * @param entityName
	 * @param entities
	 * @return
	 */
	public <T> List<T> getLangProxy(final String entityName, List<T> entities) {
		if (isI18n() && !entities.isEmpty()) {

		}

		return entities;
	}

	/**
	 * @param entityName
	 * @param id
	 * @param langInterceptors
	 * @return
	 */
	protected LangIterceptor getLangIterceptor(String entityName, String id, Map<Method, Entry<String, Class<?>>> langInterceptors) {
		return new LangIterceptor(entityName, id, langInterceptors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.IMethodEntry#getMethod(java.lang.Class,
	 * java.lang.reflect.Method)
	 */
	@Override
	public Entry<String, Class<?>> getMethod(Class<?> beanType, Method method) {
		// TODO Auto-generated method stub
		LangEntryImpl langEntryImpl = methodMapLangEntryImpl.get(method);
		if (langEntryImpl == null) {
			if (method.getParameterTypes().length == 0 && method.getName().startsWith("get") && method.getAnnotation(Langs.class) != null) {
				return new ObjectEntry<String, Class<?>>(KernelString.unCapitalize(method.getName().substring(3)), method.getReturnType());
			}

		} else {
			return langEntryImpl.getLangEntry();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.IMethodEntry#setMethodEntry(java.lang.Object,
	 * java.lang.Class, java.lang.reflect.Method, java.lang.reflect.Method)
	 */
	@Override
	public void setMethodEntry(Entry<String, Class<?>> define, Class<?> beanType, Method beanMethod, Method method) {
		// TODO Auto-generated method stub
		entityClassMapLangInterceptors.get(beanType).put(beanMethod, define);
	}

	/**
	 * 内置国际化资源写入
	 */
	@Stopping
	public void stopping() {
		if (!resourceLangs.isEmpty()) {
			String var = locale.getLanguage();
			if (!KernelString.isEmpty(var)) {
				String resource = langResource + var;
				var = locale.getCountry();
				if (!KernelString.isEmpty(var)) {
					resource += '_' + var;
					var = locale.getVariant();
					if (!KernelString.isEmpty(var)) {
						resource += '_' + var;
					}
				}

				File file = new File(resource + "/general.properties");
				BeanConfigImpl.writeProperties(resourceLangs, file);
				Developer.doEntry(file);
			}
		}
	}
}