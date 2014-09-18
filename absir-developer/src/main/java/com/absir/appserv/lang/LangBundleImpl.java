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
import java.util.Collection;
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
import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.CrudUtils;
import com.absir.appserv.crud.ICrudSupply;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.dyna.DynaBinderUtils;
import com.absir.appserv.lang.value.Langs;
import com.absir.appserv.support.Developer;
import com.absir.appserv.system.bean.JEmbedSL;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.crud.BeanCrudFactory;
import com.absir.appserv.system.helper.HelperBase;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.CrudService;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.IMethodEntry;
import com.absir.bean.inject.InjectBeanFactory;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Stopping;
import com.absir.binder.BinderData;
import com.absir.binder.IBinder;
import com.absir.context.lang.LangBundle;
import com.absir.core.base.IBase;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelReflect;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilAccessor;
import com.absir.core.util.UtilAccessor.Accessor;
import com.absir.orm.value.JoEntity;
import com.absir.property.PropertyData;
import com.absir.server.on.OnPut;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Base(order = -1)
@Bean
public class LangBundleImpl extends LangBundle {

	/** entityMapIdMapNameMapValue */
	private static Map<String, Map<String, Map<String, Map<String, Object>>>> entityMapIdMapNameMapValue = new HashMap<String, Map<String, Map<String, Map<String, Object>>>>();

	/** ME */
	public static final LangBundleImpl ME = BeanFactoryUtils.get(LangBundleImpl.class);

	/** RECORD */
	public static final String RECORD = "LOCALE@";

	/** langInterfaces */
	private Set<Class<?>> langInterfaces = new HashSet<Class<?>>();

	/** methodMapLangEntryImpl */
	private Map<Method, LangEntryImpl> methodMapLangEntryImpl = new HashMap<Method, LangEntryImpl>();

	/** entityMapLangInterceptors */
	private Map<String, Map<Method, Entry<String, Class<?>>>> entityMapLangInterceptors = new HashMap<String, Map<Method, Entry<String, Class<?>>>>();

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
	 * @param id
	 * @return
	 */
	public static Map<String, Map<String, Object>> findLangNameMapValue(String entityName, String id) {
		Map<String, Map<String, Map<String, Object>>> idMapNameMapValue = entityMapIdMapNameMapValue.get(entityName);
		if (idMapNameMapValue != null) {
			return idMapNameMapValue.get(id);
		}

		return null;
	}

	/**
	 * @param entityName
	 * @param id
	 * @return
	 */
	public static Map<String, Map<String, Object>> createLangNameMapValue(String entityName, String id) {
		Map<String, Map<String, Map<String, Object>>> idMapNameMapValue = entityMapIdMapNameMapValue.get(entityName);
		if (idMapNameMapValue == null) {
			idMapNameMapValue = new HashMap<String, Map<String, Map<String, Object>>>();
			entityMapIdMapNameMapValue.put(entityName, idMapNameMapValue);
		}

		Map<String, Map<String, Object>> nameMapValue = idMapNameMapValue.get(id);
		if (nameMapValue == null) {
			nameMapValue = new HashMap<String, Map<String, Object>>();
			idMapNameMapValue.put(id, nameMapValue);
		}

		return nameMapValue;
	}

	/**
	 * @param entityName
	 * @param ids
	 * @return
	 */
	public static Map<String, Map<String, Map<String, Object>>> getLangNameMapValues(String entityName, List<String> ids) {
		Map<String, Map<String, Map<String, Object>>> nameMapValues = new HashMap<String, Map<String, Map<String, Object>>>();
		for (Map<String, Object> value : (List<Map<String, Object>>) BeanService.ME.selectQuery("SELECT o FROM JLocale o WHERE o.entity = ? AND o.id in ?", entityName,
				KernelCollection.toArray(ids, String.class))) {
			String id = (String) value.get("id");
			Map<String, Map<String, Object>> nameMapValue = nameMapValues.get(id);
			if (nameMapValue == null) {
				nameMapValue = new HashMap<String, Map<String, Object>>();
				nameMapValues.put(id, nameMapValue);
			}

			nameMapValue.put((String) value.get("name"), value);
		}

		Map<String, Map<String, Map<String, Object>>> idMapNameMapValue = entityMapIdMapNameMapValue.get(entityName);
		if (idMapNameMapValue == null) {
			idMapNameMapValue = new HashMap<String, Map<String, Map<String, Object>>>();
			entityMapIdMapNameMapValue.put(entityName, idMapNameMapValue);
		}

		idMapNameMapValue.putAll(nameMapValues);
		return nameMapValues;
	}

	/**
	 * @param entityName
	 * @param id
	 */
	protected static void deleteLangMapValue(String entityName, String id) {
		BeanService.ME.executeUpdate("DELETE FROM JLocale o WHERE o.entity = ?, o.id = ?", entityName, id);
		Map<String, Map<String, Map<String, Object>>> idMapNameMapValue = entityMapIdMapNameMapValue.get(entityName);
		if (idMapNameMapValue != null) {
			idMapNameMapValue.remove(id);
		}
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
		public abstract Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method,
				Object[] args, MethodProxy methodProxy) throws Throwable;
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

		/** name */
		protected String name;

		/** accessor */
		protected Accessor accessor;

		/**
		 * @param beanType
		 * @param property
		 */
		public LangEmbeded(Class<?> beanType, String property) {
			name = property;
			accessor = UtilAccessor.getAccessorProperty(beanType, property);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.lang.LangBundleImpl.LangEntry#invoke(com.absir.
		 * appserv.lang.LangBundleImpl.LangIterceptor, java.lang.Object,
		 * java.util.Iterator, java.util.Map.Entry,
		 * com.absir.aop.AopProxyHandler, java.lang.reflect.Method,
		 * java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
		 */
		@Override
		public Object invoke(LangIterceptor langIterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method,
				Object[] args, MethodProxy methodProxy) throws Throwable {
			// TODO Auto-generated method stub
			Object value = proxyHandler.invoke(proxyHandler, iterator, method, args, methodProxy);
			if (value != null) {
				if (langIterceptor.embed(method)) {
					value = langProxy(langIterceptor, value);
					if (accessor != null && accessor.set(proxyHandler.getBeanObject(), value)) {
						langIterceptor.setEmbed(method);
					}
				}
			}

			return value;
		}

		/**
		 * @param langIterceptor
		 * @param value
		 * @return
		 */
		public abstract Object langProxy(LangIterceptor langIterceptor, Object value);
	}

	/**
	 * @author absir
	 *
	 */
	protected static class LangIterceptor implements AopInterceptor<Entry<String, Class<?>>> {

		/** entityName */
		private String entityName;

		/** parent */
		private LangIterceptor parent;

		/** nameId */
		private Object nameId;

		/** id */
		private String id;

		/** embededs */
		private Set<Method> embededs;

		/** langInterceptors */
		private Map<Method, Entry<String, Class<?>>> langInterceptors;

		/** nameMapValue */
		private Map<String, Map<String, Object>> nameMapValue;

		/** nameLocaleMapValue */
		private Map<JEmbedSL, Object> nameLocaleMapValue;

		/**
		 * @param parent
		 * @param nameId
		 * @return
		 */
		public static String parentNameId(LangIterceptor parent, Object nameId) {
			return parent == null || parent.id == null ? null : (nameId + "@" + parent.id);
		}

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

		/**
		 * @param parent
		 * @param nameId
		 */
		public void setNameId(LangIterceptor parent, Object nameId) {
			this.parent = parent;
			if (id == null) {
				this.nameId = nameId;
				initId();
			}
		}

		/**
		 * 
		 */
		public void initId() {
			if (id == null) {
				id = parentNameId(parent, nameId);
				if (id != null) {
					nameId = null;
				}
			}
		}

		/**
		 * @param embed
		 * @return
		 */
		public boolean embed(Method method) {
			return embededs == null || !embededs.contains(method);
		}

		/**
		 * @param method
		 */
		public void setEmbed(Method method) {
			if (embededs == null) {
				embededs = new HashSet<Method>();
			}

			embededs.add(method);
		}

		/**
		 * @param method
		 */
		public void removeEmbed(Method method) {
			if (embededs != null) {
				embededs.remove(method);
			}
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
				return ((LangEntry) interceptor).invoke(this, proxy, iterator, interceptor, proxyHandler, method, args, methodProxy);

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
			Object value = null;
			if (nameLocaleMapValue != null) {
				value = DynaBinderUtils.getMapValue(nameLocaleMapValue, new JEmbedSL(fieldName, (long) locale), type);
				if (value != null) {
					return value;
				}
			}

			if (id != null) {
				Map<String, Object> valueLocale = getNameMapValue().get(fieldName);
				if (valueLocale != null) {
					value = DynaBinderUtils.getMapValue(valueLocale, "_" + locale, type);
				}
			}

			return value;
		}

		/**
		 * @param fieldName
		 * @param locale
		 * @param value
		 */
		public void setLang(String fieldName, Integer locale, Object value) {
			// TODO Auto-generated method stub
			if (nameLocaleMapValue == null) {
				nameLocaleMapValue = new HashMap<JEmbedSL, Object>();
			}

			nameLocaleMapValue.put(new JEmbedSL(fieldName, (long) locale), value);
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
		 * @param mapValue
		 * @param name
		 * @param handler
		 * @return
		 */
		protected Map<String, Object> getMapValue(Map<String, Map<String, Object>> mapValue, String name, CrudHandler handler) {
			Map<String, Object> value = new HashMap<String, Object>();
			mapValue.put(name, value);
			if (handler.getCrudRecord() != null) {
				handler.getCrudRecord().put(RECORD + entityName + "@" + id, Boolean.TRUE);
			}

			value.put("entity", entityName);
			value.put("id", id);
			value.put("name", name);
			return value;
		}

		/**
		 * @param entity
		 * @param crud
		 * @param handler
		 */
		public void proccessCrud(Object entity, Crud crud, CrudHandler handler) {
			// TODO Auto-generated method stub
			if (crud == Crud.CREATE || crud == Crud.UPDATE) {
				if (nameMapValue == null && crud == Crud.CREATE) {
					if (entity == handler.getRoot() && entity instanceof IBase) {
						if (((IBase) entity).getId() == null) {
							String entityName = handler.getCrudEntity().getJoEntity().getEntityName();
							ICrudSupply crudSupply = CrudService.ME.getCrudSupply(entityName);
							if (crudSupply != null) {
								crudSupply.mergeEntity(entityName, entity, true);
							}
						}
					}

					initId();
					getNameMapValue();
				}

				Map<String, Map<String, Object>> mapValue = new HashMap<String, Map<String, Object>>();
				if (nameMapValue != null) {
					for (Entry<Method, Entry<String, Class<?>>> methodEntry : langInterceptors.entrySet()) {
						Entry<String, Class<?>> entry = methodEntry.getValue();
						if (entry.getKey() != null && !nameMapValue.containsKey(entry.getKey())) {
							Map<String, Object> value = getMapValue(mapValue, entry.getKey(), handler);
							value.put("_" + ME.getLocaleCode(), DynaBinderUtils.to(KernelReflect.invoke(entity, methodEntry.getKey()), String.class));
						}
					}

					if (nameLocaleMapValue != null) {
						for (Entry<JEmbedSL, Object> entry : nameLocaleMapValue.entrySet()) {
							Map<String, Object> value = mapValue.get(entry.getKey().getEid());
							if (value == null) {
								value = getMapValue(mapValue, entry.getKey().getEid(), handler);
							}

							value.put("_" + entry.getKey().getMid(), DynaBinderUtils.to(entry.getValue(), String.class));
						}
					}
				}

				if (!mapValue.isEmpty()) {
					BeanService.ME.mergers("JLocale", mapValue.values());
				}

			} else if (crud == Crud.DELETE) {
				if (handler.getCrudRecord() == null || !handler.getCrudRecord().containsKey(RECORD + entityName + "@" + id)) {
					deleteLangMapValue(entityName, id);
				}
			}
		}

		/**
		 * @param entity
		 * @param name
		 * @param value
		 * @param propertyData
		 * @param binderData
		 */
		public void bind(Object entity, String name, Object value, PropertyData propertyData, BinderData binderData) {
			if (entity instanceof ILangBase) {
				String[] args = value.toString().split(".", 2);
				if (args.length == 2) {
					((ILangBase) entity).setLang(name, KernelDyna.toInteger(args[0], 0), args[1]);
				}
			}
		}
	}

	/**
	 * @param methodMapLangEntryImpl
	 */
	private static void InitLangBundleImpl(Map<Method, LangEntryImpl> methodMapLangEntryImpl) {
		for (Method method : ILangBase.class.getMethods()) {
			LangEntryImpl langEntryImpl = null;
			if ("getLang".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method,
							Object[] args, MethodProxy methodProxy) {
						// TODO Auto-generated method stub
						return iterceptor.getLang((String) args[0], (Integer) args[1], (Class<?>) args[2]);
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor inIterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler,
									Method method, Object[] args, MethodProxy methodProxy) {
								// TODO Auto-generated method stub
								return ((ILangBase) proxyHandler.getBeanObject()).getLang((String) args[0], (Integer) args[1], (Class<?>) args[2]);
							}
						};
					}
				};

			} else if ("setLang".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method,
							Object[] args, MethodProxy methodProxy) {
						// TODO Auto-generated method stub
						iterceptor.setLang((String) args[0], (Integer) args[1], args[2]);
						return null;
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler,
									Method method, Object[] args, MethodProxy methodProxy) {
								// TODO Auto-generated method stub
								((ILangBase) proxyHandler.getBeanObject()).setLang((String) args[0], (Integer) args[1], args[2]);
								return null;
							}
						};
					}
				};

			} else if ("setLangEntity".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method,
							Object[] args, MethodProxy methodProxy) {
						// TODO Auto-generated method stub
						iterceptor.setLangValues((String[]) args[0]);
						return null;
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler,
									Method method, Object[] args, MethodProxy methodProxy) {
								// TODO Auto-generated method stub
								((ILangBase) proxyHandler.getBeanObject()).setLangValues((String[]) args[0]);
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

		for (Method method : IBinder.class.getMethods()) {
			LangEntryImpl langEntryImpl = null;
			if ("bind".equals(method.getName())) {
				langEntryImpl = new LangEntryImpl() {

					@Override
					public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method,
							Object[] args, MethodProxy methodProxy) {
						// TODO Auto-generated method stub
						iterceptor.proccessCrud(proxyHandler.getBeanObject(), (Crud) args[0], (CrudHandler) args[1]);
						return null;
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler,
									Method method, Object[] args, MethodProxy methodProxy) {
								// TODO Auto-generated method stub
								((IBinder) proxyHandler.getBeanObject()).bind((String) args[0], (Object) args[1], (PropertyData) args[2], (BinderData) args[3]);
								invoke(iterceptor, proxy, iterator, interceptor, proxyHandler, method, args, methodProxy);
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
					public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method,
							Object[] args, MethodProxy methodProxy) {
						// TODO Auto-generated method stub
						iterceptor.proccessCrud(proxyHandler.getBeanObject(), (Crud) args[0], (CrudHandler) args[1]);
						return null;
					}

					@Override
					public LangEntry generateLangEnry() {
						// TODO Auto-generated method stub
						return new LangEntry() {

							@Override
							public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler,
									Method method, Object[] args, MethodProxy methodProxy) {
								// TODO Auto-generated method stub
								((ICrudBean) proxyHandler.getBeanObject()).proccessCrud((Crud) args[0], (CrudHandler) args[1]);
								invoke(iterceptor, proxy, iterator, interceptor, proxyHandler, method, args, methodProxy);
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
		langInterfaces.add(IBinder.class);
		langInterfaces.add(ICrudBean.class);
		InitLangBundleImpl(methodMapLangEntryImpl);
	}

	/**
	 * @param joEntity
	 * @return
	 */
	protected Map<Method, Entry<String, Class<?>>> getLangInterceptors(JoEntity joEntity) {
		return getLangInterceptors(joEntity.getEntityName() == null ? joEntity.getEntityClass().getName() : joEntity.getEntityName(), joEntity.getClass());
	}

	/**
	 * @param entityName
	 * @param entityClass
	 * @return
	 */
	protected Map<Method, Entry<String, Class<?>>> getLangInterceptors(final String entityName, final Class<?> entityClass) {
		if (isI18n()) {
			Map<Method, Entry<String, Class<?>>> langInterceptors = entityMapLangInterceptors.get(entityName);
			if (langInterceptors == null) {
				langInterceptors = new HashMap<Method, Map.Entry<String, Class<?>>>();
				final Map<Method, Entry<String, Class<?>>> interceptors = langInterceptors;
				InjectBeanFactory.getInstance().getMethodEntries(entityClass, new IMethodEntry<Entry<String, Class<?>>>() {

					@Override
					public Entry<String, Class<?>> getMethod(Class<?> beanType, Method method) {
						// TODO Auto-generated method stub
						LangEntryImpl langEntryImpl = methodMapLangEntryImpl.get(method);
						if (langEntryImpl == null) {
							if (method.getParameterTypes().length == 0 && method.getName().startsWith("get")) {
								if (method.getAnnotation(Langs.class) != null) {
									return new ObjectEntry<String, Class<?>>(KernelString.unCapitalize(method.getName().substring(3)), method.getReturnType());
								}

								Class<?> returnType = method.getReturnType();
								if (!KernelClass.isBasicClass(returnType)) {
									String property = KernelString.unCapitalize(method.getName().substring(3));
									CrudProperty crudProperty = CrudUtils.getCrudProperty(new JoEntity(entityName, entityClass), property);
									if (crudProperty != null && crudProperty.getCrudProcessor() != null && crudProperty.getCrudProcessor() instanceof BeanCrudFactory.Proccessor) {
										final JoEntity joEntity = crudProperty.getValueEntity();
										if (joEntity != null) {
											if (joEntity != null && KernelClass.isCustomClass(joEntity.getEntityClass())) {
												final boolean ided = CrudService.ME.getCrudSupply(joEntity.getEntityName()) != null && IBase.class.isAssignableFrom(joEntity.getEntityClass());
												if (returnType.isArray()) {
													return new LangEmbeded(beanType, property) {

														@Override
														public Object langProxy(LangIterceptor langIterceptor, Object value) {
															// TODO
															// Auto-generated
															// method stub
															Object[] entities = (Object[]) value;
															Object[] nameIds = null;
															int size = entities.length;
															if (size > 0) {
																if (ided) {
																	nameIds = HelperBase.getBaseIds((IBase[]) entities);

																} else {
																	nameIds = new Object[size];
																	for (int i = 0; i < size; i++) {
																		nameIds[i] = langIterceptor.entityName + '.' + name + "@" + i;
																	}
																}
															}

															return ME.getLangProxy(langIterceptor, joEntity, entities, nameIds, ided);
														}

													};

												} else if (Collection.class.isAssignableFrom(returnType)) {
													return new LangEmbeded(beanType, property) {

														@Override
														public Object langProxy(LangIterceptor langIterceptor, Object value) {
															// TODO
															// Auto-generated
															// method stub
															Collection<?> entities = (Collection<?>) value;
															Object[] ids = null;
															int size = entities.size();
															if (size > 0) {
																if (ided) {
																	ids = HelperBase.getBaseIds((Collection<? extends IBase>) entities);

																} else {
																	ids = new Object[size];
																	for (int i = 0; i < size; i++) {
																		ids[i] = langIterceptor.entityName + '.' + name + "@" + langIterceptor.id + "@" + i;
																	}
																}
															}

															return ME.getLangProxy(langIterceptor, joEntity, entities, ids);
														}
													};

												} else if (Map.class.isAssignableFrom(returnType)) {
													return new LangEmbeded(beanType, property) {

														@Override
														public Object langProxy(LangIterceptor langIterceptor, Object value) {
															// TODO
															// Auto-generated
															// method stub
															Map<?, ?> entities = (Map<?, ?>) value;
															Object[] ids = null;
															int size = entities.size();
															if (size > 0) {
																if (ided) {
																	ids = HelperBase.getBaseIds(((Map<?, ? extends IBase>) entities).values());

																} else {
																	ids = new Object[size];
																	for (int i = 0; i < size; i++) {
																		ids[i] = langIterceptor.entityName + '.' + name + "@" + langIterceptor.id + "@" + i;
																	}
																}
															}

															return ME.getLangProxy(langIterceptor, joEntity, entities, ids);
														}
													};

												} else {
													return new LangEmbeded(beanType, property) {

														@Override
														public Object langProxy(LangIterceptor langIterceptor, Object value) {
															// TODO
															// Auto-generated
															// method stub
															return ME.getLangProxy(langIterceptor, joEntity, value, ided ? ((IBase) value).getId()
																	: (langIterceptor.entityName + '.' + name + "@" + langIterceptor.id));
														}
													};
												}
											}
										}
									}
								}
							}

						} else {
							return langEntryImpl.getLangEntry();
						}

						return null;
					}

					@Override
					public void setMethodEntry(Entry<String, Class<?>> define, Class<?> beanType, final Method beanMethod, Method method) {
						// TODO Auto-generated method stub
						interceptors.put(beanMethod, define);
						if (define instanceof LangEmbeded) {
							LangEmbeded langEmbeded = (LangEmbeded) define;
							if (langEmbeded.accessor != null && langEmbeded.accessor.getSetter() != null) {
								interceptors.put(langEmbeded.accessor.getSetter(), new LangEntry() {

									@Override
									public Object invoke(LangIterceptor iterceptor, Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler,
											Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
										// TODO Auto-generated method stub
										iterceptor.removeEmbed(beanMethod);
										Object arg = args[0];
										if (arg != null && arg instanceof AopProxy) {
											args[0] = ((AopProxy) arg).getBeanObject();
										}

										return AopProxyHandler.VOID;
									}
								});
							}
						}
					}
				});

				if (langInterceptors.isEmpty()) {
					langInterceptors = (Map<Method, Entry<String, Class<?>>>) (Object) KernelLang.NULL_MAP;

				} else {
					// 执行接口方法
					langInterceptors.putAll(methodMapLangEntryImpl);
				}

				// 最后设置
				entityMapLangInterceptors.put(entityName, langInterceptors);
			}

			if ((Object) langInterceptors == KernelLang.NULL_MAP) {
				return null;
			}

			return langInterceptors;
		}

		return null;
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
		Map<Method, Entry<String, Class<?>>> langInterceptors = getLangInterceptors(entityName, entity.getClass());
		if (langInterceptors != null) {
			AopProxy proxy = AopProxyUtils.getProxy(entity, langInterfaces, false, true);
			proxy.getAopInterceptors().add(getLangIterceptor(entityName, DynaBinderUtils.getParamFromValue(id), langInterceptors));
			return (T) proxy;
		}

		return entity;
	}

	/**
	 * @param entityName
	 * @param entities
	 * @return
	 */
	public <T> List<T> getLangProxy(final String entityName, final List<T> entities) {
		if (isI18n() && !entities.isEmpty()) {
			Object entity = entities.get(0);
			if (entity instanceof IBase) {
				return (List<T>) getLangProxy(entityName, new JoEntity(entityName, entity.getClass()), entities, HelperBase.getBaseIds((Collection<? extends IBase>) entities));
			}
		}

		return entities;
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
	 * @param joEntity
	 * @param entity
	 * @param id
	 * @return
	 */
	public <T> T getLangProxy(String entityName, JoEntity joEntity, T entity, Object id) {
		if (isI18n()) {
			Map<Method, Entry<String, Class<?>>> langInterceptors = getLangInterceptors(joEntity);
			if (langInterceptors != null) {
				AopProxy proxy = AopProxyUtils.getProxy(entity, langInterfaces, false, true);
				proxy.getAopInterceptors().add(getLangIterceptor(joEntity.getEntityName() == null ? entityName : joEntity.getEntityName(), DynaBinderUtils.getParamFromValue(id), langInterceptors));
				return (T) proxy;
			}
		}

		return entity;
	}

	/**
	 * @param parent
	 * @param joEntity
	 * @param entity
	 * @param id
	 * @param nameId
	 * @param langInterceptors
	 * @param nameMapValue
	 * @return
	 */
	protected <T> T getLangProxy(LangIterceptor parent, JoEntity joEntity, T entity, String id, Object nameId, Map<Method, Entry<String, Class<?>>> langInterceptors,
			Map<String, Map<String, Object>> nameMapValue) {
		AopProxy proxy = AopProxyUtils.getProxy(entity, langInterfaces, false, true);
		LangIterceptor langIterceptor = getLangIterceptor(joEntity.getEntityName() == null ? parent.entityName : joEntity.getEntityName(), id, langInterceptors);
		langIterceptor.setNameId(parent, nameId);
		langIterceptor.nameMapValue = nameMapValue;
		proxy.getAopInterceptors().add(langIterceptor);
		return (T) proxy;
	}

	/**
	 * @param parent
	 * @param joEntity
	 * @param entities
	 * @param ids
	 * @param ided
	 * @return
	 */
	public <T> T[] getLangProxy(LangIterceptor parent, JoEntity joEntity, T[] entities, Object[] ids, boolean ided) {
		if (isI18n() && entities.length > 0) {
			String entityName = joEntity.getEntityName() == null ? parent.entityName : joEntity.getEntityName();
			Map<Method, Entry<String, Class<?>>> langInterceptors = getLangInterceptors(joEntity);
			if (langInterceptors != null) {
				List<Integer> is = new ArrayList<Integer>();
				List<String> finds = new ArrayList<String>();
				int length = entities.length;
				for (int i = 0; i < length; i++) {
					Object nameId = ids[i];
					String id = ided ? DynaBinderUtils.to(nameId, String.class) : LangIterceptor.parentNameId(parent, nameId);
					if (id == null) {
						entities[i] = getLangProxy(parent, joEntity, entities[i], id, nameId, langInterceptors, null);

					} else {
						Map<String, Map<String, Object>> nameMapValue = getLangNameMapValue(entityName, id);
						if (nameMapValue == null) {
							is.add(i);
							finds.add(id);

						} else {
							entities[i] = getLangProxy(parent, joEntity, entities[i], id, nameId, langInterceptors, nameMapValue);
						}
					}
				}

				if (!finds.isEmpty()) {
					Map<String, Map<String, Map<String, Object>>> nameMapValues = getLangNameMapValues(entityName, finds);
					for (int i : is) {
						String id = finds.get(i);
						Map<String, Map<String, Object>> nameMapValue = nameMapValues.get(id);
						if (nameMapValue == null) {
							nameMapValue = createLangNameMapValue(joEntity.getEntityName(), id);
						}

						entities[i] = getLangProxy(parent, joEntity, entities[i], id, ids[i], langInterceptors, nameMapValue);
					}
				}
			}
		}

		return entities;
	}

	/**
	 * @param parent
	 * @param joEntity
	 * @param entities
	 * @param nameIds
	 * @param langInterceptors
	 * @return
	 */
	protected <T> List<T> getLangProxyList(LangIterceptor parent, JoEntity joEntity, Collection<T> entities, String[] nameIds, Map<Method, Entry<String, Class<?>>> langInterceptors) {
		String entityName = joEntity.getEntityName() == null ? parent.entityName : joEntity.getEntityName();
		List<Integer> is = new ArrayList<Integer>();
		List<String> finds = new ArrayList<String>();
		List<T> list = new ArrayList<T>(entities);
		int length = list.size();
		for (int i = 0; i < length; i++) {
			String nameId = nameIds[i];
			String id = LangIterceptor.parentNameId(parent, nameId);
			if (id == null) {
				list.set(i, getLangProxy(parent, joEntity, list.get(i), id, nameId, langInterceptors, null));

			} else {
				Map<String, Map<String, Object>> nameMapValue = findLangNameMapValue(entityName, id);
				if (nameMapValue == null) {
					is.add(i);
					finds.add(id);

				} else {
					list.set(i, getLangProxy(parent, joEntity, list.get(i), id, nameId, langInterceptors, nameMapValue));
				}
			}
		}

		if (!finds.isEmpty()) {
			Map<String, Map<String, Map<String, Object>>> nameMapValues = getLangNameMapValues(joEntity.getEntityName(), finds);
			for (int i : is) {
				String id = finds.get(i);
				Map<String, Map<String, Object>> nameMapValue = nameMapValues.get(id);
				if (nameMapValue == null) {
					nameMapValue = createLangNameMapValue(joEntity.getEntityName(), id);
				}

				list.set(i, getLangProxy(parent, joEntity, list.get(i), id, nameIds[i], langInterceptors, nameMapValue));
			}
		}

		return list;
	}

	/**
	 * @param parent
	 * @param joEntity
	 * @param entities
	 * @param nameIds
	 * @return
	 */
	public <T> Collection<T> getLangProxy(LangIterceptor parent, JoEntity joEntity, Collection<T> entities, String[] nameIds) {
		if (isI18n() && !entities.isEmpty()) {
			Map<Method, Entry<String, Class<?>>> langInterceptors = getLangInterceptors(joEntity.getEntityName(), joEntity.getEntityClass());
			if (langInterceptors != null) {
				List<T> list = getLangProxyList(parent, joEntity, entities, nameIds, langInterceptors);
				entities.clear();
				entities.addAll(list);
			}
		}

		return entities;
	}

	/**
	 * @param parent
	 * @param joEntity
	 * @param entityMap
	 * @param nameIds
	 * @return
	 */
	public <K, V> Map<K, V> getLangProxy(LangIterceptor parent, JoEntity joEntity, Map<K, V> entityMap, String[] nameIds) {
		if (isI18n() && !entityMap.isEmpty()) {
			Map<Method, Entry<String, Class<?>>> langInterceptors = getLangInterceptors(joEntity.getEntityName(), joEntity.getEntityClass());
			if (langInterceptors != null) {
				if (langInterceptors != null) {
					List<V> list = getLangProxyList(parent, joEntity, entityMap.values(), nameIds, langInterceptors);
					int i = 0;
					for (Entry<K, V> entry : entityMap.entrySet()) {
						entry.setValue(list.get(i++));
					}
				}
			}
		}

		return entityMap;
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