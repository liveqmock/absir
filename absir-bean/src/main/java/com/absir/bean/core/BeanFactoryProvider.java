/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-13 下午4:06:49
 */
package com.absir.bean.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.absir.bean.basis.Base;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanConfig;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.basis.Configure;
import com.absir.bean.basis.Environment;
import com.absir.bean.config.IBeanConfigProvider;
import com.absir.bean.config.IBeanDefineAware;
import com.absir.bean.config.IBeanDefineEager;
import com.absir.bean.config.IBeanDefineProcessor;
import com.absir.bean.config.IBeanDefineScanner;
import com.absir.bean.config.IBeanDefineSupply;
import com.absir.bean.config.IBeanFactoryAware;
import com.absir.bean.config.IBeanFactoryStarted;
import com.absir.bean.config.IBeanFactoryStopping;
import com.absir.bean.config.IBeanFactorySupport;
import com.absir.bean.config.IBeanObjectProcessor;
import com.absir.bean.config.IBeanSoftReferenceAware;
import com.absir.bean.config.IBeanTypeFilter;
import com.absir.bean.inject.InjectBeanDefine;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.CallbackBreak;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelList;
import com.absir.core.kernel.KernelReflect;

/**
 * @author absir
 * 
 */
public class BeanFactoryProvider implements IBeanConfigProvider {

	/** includePackages */
	private Set<String> includePackages = new HashSet<String>();

	/** excludePackages */
	private Set<String> excludePackages = new HashSet<String>();

	/** unMatchPatterns */
	private Set<String> unMatchPatterns = new HashSet<String>();

	/** beanTypes */
	private Set<Class<?>> beanTypes = new HashSet<Class<?>>();

	/** beanTypeFilters */
	private List<IBeanTypeFilter> beanTypeFilters = new ArrayList<IBeanTypeFilter>();

	/** beanDefineSupplies */
	private List<IBeanDefineSupply> beanDefineSupplies = new ArrayList<IBeanDefineSupply>();

	/** beanDefineProcessors */
	private List<IBeanDefineProcessor> beanDefineProcessors = new ArrayList<IBeanDefineProcessor>();

	/** beanFactoryStarteds */
	private List<IBeanFactoryStarted> beanFactoryStarteds = new ArrayList<IBeanFactoryStarted>();

	/** beanFactoryStoppings */
	private List<IBeanFactoryStopping> beanFactoryStoppings = new ArrayList<IBeanFactoryStopping>();

	/**
	 * @param beanType
	 * @return
	 */
	public static boolean filterBeanType(Class<?> beanType) {
		return beanType.isPrimitive() || beanType.isAnonymousClass();
	}

	/**
	 * @param beanTypes
	 * @return
	 */
	private List<Class<?>> getBeanTypes(Collection<Class<?>> beanTypes) {
		List<Class<?>> beanTypeList = new ArrayList<Class<?>>();
		for (Class<?> beanType : beanTypes) {
			if (!filterBeanType(beanType)) {
				beanTypeList.add(beanType);
			}
		}

		return beanTypeList;
	}

	/**
	 * @param includePackages
	 * @param excludePackages
	 * @param unMatchPatterns
	 */
	public BeanFactoryProvider(Collection<String> includePackages, Collection<String> excludePackages, Collection<String> unMatchPatterns) {
		if (includePackages != null) {
			this.includePackages.addAll(includePackages);
		}

		if (excludePackages != null) {
			this.excludePackages.addAll(excludePackages);
		}

		if (unMatchPatterns != null) {
			this.unMatchPatterns.addAll(unMatchPatterns);
		}
	}

	/**
	 * @return the beanFactoryStarteds
	 */
	public List<IBeanFactoryStarted> getBeanFactoryStarteds() {
		return beanFactoryStarteds;
	}

	/**
	 * @return the beanFactoryStoppings
	 */
	public List<IBeanFactoryStopping> getBeanFactoryStoppings() {
		return beanFactoryStoppings;
	}

	/**
	 * @param beanScanner
	 * @param beanTypes
	 * @param beanNameAndObjects
	 */
	public synchronized void scan(BeanScanner beanScanner, Collection<Class<?>> beanTypes, Object... beanNameAndObjects) {
		beanTypes = beanTypes == null ? new HashSet<Class<?>>() : new HashSet<Class<?>>(beanTypes);
		ConcurrentHashMap<String, BeanDefine> beanNameDefineMap = new ConcurrentHashMap<String, BeanDefine>();
		List<IBeanFactoryAware> beanFactoryAwares = new ArrayList<IBeanFactoryAware>();
		List<IBeanDefineAware> beanDefineAwares = new ArrayList<IBeanDefineAware>();
		List<IBeanObjectProcessor> beanObjectProcessors = new ArrayList<IBeanObjectProcessor>();
		List<IBeanSoftReferenceAware> beanSoftReferenceAwares = new ArrayList<IBeanSoftReferenceAware>();
		BeanFactoryImpl beanFactory = new BeanFactoryImpl(getBeanConfig(), beanNameDefineMap, beanDefineAwares, beanObjectProcessors, beanSoftReferenceAwares);
		if (beanScanner == null) {
			beanScanner = new BeanScanner();
		}

		beanScanner.scanBeanTypes(beanTypes, includePackages, excludePackages, unMatchPatterns);
		beanTypes.addAll(this.beanTypes);
		Map<Object, BeanDefine> beanDefineObjects = new LinkedHashMap<Object, BeanDefine>();
		int length = beanNameAndObjects.length;
		for (int i = 0; i < length; i++) {
			Object nameObject = beanNameAndObjects[i];
			if (nameObject == null || nameObject instanceof String) {
				if (i++ < length) {
					Object beanObject = beanNameAndObjects[i];
					beanDefineObjects.put(beanObject, beanFactory.registerBeanObject((String) nameObject, beanObject));
					continue;
				}
			}

			if (nameObject != null) {
				beanDefineObjects.put(nameObject, beanFactory.registerBeanObject(nameObject));
			}
		}

		beanTypes = getBeanTypes(beanTypes);
		beanFactory.registerBeanObject(beanFactory);
		registerBeanTypes(beanFactory, beanTypes, new Class<?>[] { IBeanTypeFilter.class, IBeanDefineSupply.class, IBeanDefineProcessor.class }, new Collection<?>[] { beanTypeFilters,
				beanDefineSupplies, beanDefineProcessors });
		KernelList.sortOrderable(beanDefineSupplies);
		KernelList.sortOrderable(beanDefineProcessors);

		for (Iterator<Class<?>> iterator = beanTypes.iterator(); iterator.hasNext();) {
			Class<?> beanType = iterator.next();
			if (!isSupport(beanType)) {
				iterator.remove();
			}
		}

		Set<String> beanDefineNames = new HashSet<String>();
		Map<String, Entry<Integer, BeanDefine>> beanDefineBases = new HashMap<String, Entry<Integer, BeanDefine>>();
		for (Class<?> beanType : beanTypes) {
			List<BeanDefine> beanDefines = null;
			Environment environment = beanFactory.getBeanConfig().getEnvironment();
			for (IBeanDefineSupply beanDefineSupply : beanDefineSupplies) {
				if ((beanDefines = beanDefineSupply.getBeanDefines(beanFactory, beanType)) != null) {
					Base base = beanType.getAnnotation(Base.class);
					if (base != null && base.environment().compareTo(environment) < 0) {
						continue;
					}

					for (BeanDefine beanDefine : beanDefines) {
						if (beanDefine != null) {
							BeanDefine registerBeanDefine = beanFactory.getBeanDefineComponent(beanDefine.getBeanComponent());
							if (registerBeanDefine != null) {
								if (beanDefine.getBeanScope() != BeanScope.PROTOTYPE && registerBeanDefine.getClass() == BeanDefineOriginal.class) {
									beanDefine = new BeanDefineMerged(((BeanDefineOriginal) registerBeanDefine).getBeanDefine(), beanDefine.getBeanName(), beanDefine.getBeanScope(),
											beanDefine.getBeanComponent());
								}

							} else if (beanFactory.getBeanDefine(beanDefine.getBeanName()) != null) {
								if (BeanFactoryImpl.getBeanDefine(beanDefine, BeanDefineMethod.class) != null) {
									continue;
								}
							}

							for (IBeanDefineProcessor beanDefineProcessor : beanDefineProcessors) {
								if ((beanDefine = beanDefineProcessor.getBeanDefine(beanFactory, beanDefine)) == null) {
									break;
								}
							}
						}

						if (beanDefine != null) {
							if (base == null) {
								beanDefineNames.add(beanDefine.getBeanName());
								beanNameDefineMap.put(beanDefine.getBeanName(), beanDefine);

							} else {
								int order = base.order();
								Class<?> beanClass = beanDefine.getBeanType();
								Iterator<Entry<String, Entry<Integer, BeanDefine>>> baseIterator = beanDefineBases.entrySet().iterator();
								while (baseIterator.hasNext()) {
									Entry<String, Entry<Integer, BeanDefine>> entry = baseIterator.next();
									Class<?> registerClass = entry.getValue().getValue().getBeanType();
									int assignable = beanClass == registerClass ? 0 : registerClass.isAssignableFrom(beanClass) ? -1 : beanClass.isAssignableFrom(registerClass) ? 1 : 2;
									if (assignable < 2) {
										int registerOrder = entry.getValue().getKey();
										if (order == registerOrder) {
											if (assignable < 0) {
												baseIterator.remove();
												break;

											} else if (assignable > 0) {
												beanClass = null;
												break;
											}

										} else if (order < registerOrder) {
											if (assignable <= 0) {
												baseIterator.remove();
												break;
											}

										} else {
											if (assignable >= 0) {
												beanClass = null;
												break;
											}
										}
									}
								}

								if (beanClass != null) {
									beanDefineBases.put(beanDefine.getBeanName(), new ObjectEntry<Integer, BeanDefine>(order, beanDefine));
								}
							}
						}
					}

					break;
				}
			}
		}

		// BeanDefine Base
		registerBeanDefine(beanFactory, beanDefineNames);
		for (Entry<String, Entry<Integer, BeanDefine>> entry : beanDefineBases.entrySet()) {
			BeanDefine beanDefine = entry.getValue().getValue();
			if (beanFactory.getBeanDefine(null, beanDefine.getBeanType()) == null) {
				beanDefineNames.add(beanDefine.getBeanName());
				beanFactory.registerBeanDefine(beanDefine);
			}
		}

		// BeanDefine Object Processor
		for (String beanDefineName : beanDefineNames) {
			BeanDefine beanDefine = beanNameDefineMap.get(beanDefineName);
			if (beanDefine == null) {
				continue;
			}

			if (IBeanObjectProcessor.class.isAssignableFrom(beanDefine.getBeanType())) {
				beanObjectProcessors.add((IBeanObjectProcessor) beanDefine.getBeanObject(beanFactory));
			}
		}

		// BeanDefine Processor Object Processor
		KernelList.sortOrderable(beanObjectProcessors);
		for (BeanDefine beanDefineProcessor : beanFactory.getBeanDefines(IBeanDefineProcessor.class)) {
			beanFactory.processBeanObject(null, beanDefineProcessor, beanDefineProcessor.getBeanObject(beanFactory));
		}

		for (Entry<Object, BeanDefine> entry : beanDefineObjects.entrySet()) {
			Object beanObject = entry.getKey();
			BeanDefine beanDefine = entry.getValue();
			beanFactory.processBeanObject(null, beanDefine, beanObject);
			processorBeanDefineObject(beanFactory, beanDefine, beanObject);
		}

		beanDefineObjects.clear();

		// BeanDefine Processor IBeanDefineScanner
		for (String beanDefineName : beanDefineNames) {
			BeanDefine beanDefine = beanNameDefineMap.get(beanDefineName);
			if (beanDefine != null && IBeanDefineScanner.class.isAssignableFrom(beanDefine.getBeanType())) {
				beanDefine.getBeanObject(beanFactory);
			}
		}

		// BeanFacotry Aware Processor
		for (String beanDefineName : beanDefineNames) {
			BeanDefine beanDefine = beanNameDefineMap.get(beanDefineName);
			if (beanDefine == null) {
				continue;
			}

			if (IBeanFactoryAware.class.isAssignableFrom(beanDefine.getBeanType())) {
				Object beanObject = beanDefine.getBeanObject(beanFactory);
				beanDefineObjects.put(beanObject, beanDefine);
				beanFactoryAwares.add((IBeanFactoryAware) beanObject);
			}

			if (IBeanDefineSupply.class.isAssignableFrom(beanDefine.getBeanType())) {
				Object beanObject = beanDefine.getBeanObject(beanFactory);
				beanDefineObjects.put(beanObject, beanDefine);
			}

			if (IBeanDefineAware.class.isAssignableFrom(beanDefine.getBeanType())) {
				Object beanObject = beanDefine.getBeanObject(beanFactory);
				beanDefineObjects.put(beanObject, beanDefine);
				beanDefineAwares.add((IBeanDefineAware) beanObject);
			}

			if (IBeanSoftReferenceAware.class.isAssignableFrom(beanDefine.getBeanType())) {
				Object beanObject = beanDefine.getBeanObject(beanFactory);
				beanDefineObjects.put(beanObject, beanDefine);
				beanSoftReferenceAwares.add((IBeanSoftReferenceAware) beanObject);
			}

			if (IBeanFactoryStarted.class.isAssignableFrom(beanDefine.getBeanType())) {
				Object beanObject = beanDefine.getBeanObject(beanFactory);
				beanDefineObjects.put(beanObject, beanDefine);
				beanFactoryStarteds.add((IBeanFactoryStarted) beanObject);
			}

			if (IBeanFactoryStopping.class.isAssignableFrom(beanDefine.getBeanType())) {
				Object beanObject = beanDefine.getBeanObject(beanFactory);
				beanDefineObjects.put(beanObject, beanDefine);
				beanFactoryStoppings.add((IBeanFactoryStopping) beanObject);
			}
		}

		// BeanFacotry Aware Processor Object
		for (Entry<Object, BeanDefine> entry : beanDefineObjects.entrySet()) {
			beanFactory.processBeanObject(null, entry.getValue(), entry.getKey());
		}

		KernelList.sortOrderable(beanFactoryAwares);
		KernelList.sortOrderable(beanDefineAwares);
		KernelList.sortOrderable(beanSoftReferenceAwares);
		KernelList.sortOrderable(beanFactoryStarteds);
		KernelList.sortOrderable(beanFactoryStoppings);

		for (IBeanFactoryAware beanFactoryAware : beanFactoryAwares) {
			beanFactoryAware.beforeRegister(beanFactory);
		}

		for (String beanDefineName : beanDefineNames) {
			BeanDefine beanDefine = beanNameDefineMap.get(beanDefineName);
			if (beanDefine == null) {
				continue;
			}

			beanFactory.registerBeanDefine(null, beanDefine);
		}

		for (Class<?> beanType : beanTypes) {
			if (IBeanDefineEager.class.isAssignableFrom(beanType)) {
				KernelClass.forName(beanType.getName());
			}
		}

		for (IBeanFactoryAware beanFactoryAware : beanFactoryAwares) {
			beanFactoryAware.afterRegister(beanFactory);
		}

		Iterator<Entry<String, BeanDefine>> iteratorMap = beanNameDefineMap.entrySet().iterator();
		while (iteratorMap.hasNext()) {
			if (iteratorMap.next().getValue() instanceof BeanDefineOriginal) {
				iteratorMap.remove();
			}
		}
	}

	/**
	 * 
	 */
	public synchronized void started() {
		BeanFactory beanFactory = BeanFactoryUtils.get();
		for (IBeanFactoryStarted beanFactoryStarted : beanFactoryStarteds) {
			beanFactoryStarted.started(beanFactory);
		}

		beanFactoryStarteds.clear();
	}

	/**
	 * 
	 */
	public synchronized void stopping() {
		BeanFactory beanFactory = BeanFactoryUtils.get();
		for (IBeanFactoryStopping beanFactoryStopping : beanFactoryStoppings) {
			beanFactoryStopping.stopping(beanFactory);
		}

		beanFactoryStoppings.clear();
	}

	/**
	 * @return
	 */
	protected BeanConfig getBeanConfig() {
		return new BeanConfigImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.config.IBeanConfigProvider#loadBeanConfig(com.absir.bean
	 * .basis.BeanConfig, java.util.Set, java.util.Set, java.util.Map)
	 */
	@Override
	public void loadBeanConfig(final BeanConfig beanConfig, Set<String> propertyFilenames, Set<String> loadedPropertyFilenames, Map<String, CallbackTemplate<String>> beanConfigTemplates) {
		// TODO Auto-generated method stub
		beanConfigTemplates.put("include", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				for (String name : template.split(" ")) {
					name = name.trim();
					if (name.length() > 0) {
						includePackages.add(name);
					}
				}
			}
		});

		beanConfigTemplates.put("exclude", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				for (String name : template.split(" ")) {
					name = name.trim();
					if (name.length() > 0) {
						excludePackages.add(name);
					}
				}
			}
		});

		beanConfigTemplates.put("filter", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				for (String name : template.split(" ")) {
					name = name.trim();
					if (name.length() > 0) {
						unMatchPatterns.add(name);
					}
				}
			}
		});

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		beanConfigTemplates.put("bean", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				for (String name : template.split(" ")) {
					name = name.trim();
					if (name.length() > 0) {
						try {
							beanTypes.add(classLoader.loadClass(name));

						} catch (Exception e) {
							if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
	}

	/**
	 * @param beanFactory
	 * @param beanDefineNames
	 */
	protected void registerBeanDefine(BeanFactoryImpl beanFactory, Set<String> beanDefineNames) {
		for (Class<?> beanClass : beanTypes) {
			BeanDefine beanDefine = new InjectBeanDefine(new BeanDefineType(beanClass), BeanScope.SINGLETON);
			beanDefineNames.add(beanDefine.getBeanName());
			beanFactory.registerBeanDefine(beanDefine);
		}
	}

	/**
	 * @param beanFactory
	 * @param beanDefine
	 * @param beanObject
	 */
	protected void processorBeanDefineObject(BeanFactoryImpl beanFactory, BeanDefine beanDefine, Object beanObject) {
	}

	/**
	 * @param beanType
	 * @return
	 */
	private boolean isSupport(Class<?> beanType) {
		for (IBeanTypeFilter beanTypeFilter : beanTypeFilters) {
			if (beanTypeFilter.filt(beanType)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param beanFactory
	 * @param beanTypes
	 * @param basisTypes
	 * @param collections
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void registerBeanTypes(final BeanFactoryImpl beanFactory, final Collection<Class<?>> beanTypes, final Class<?>[] basisTypes, final Collection[] collections) {
		final List<BeanDefine> beanDefines = new ArrayList<BeanDefine>();
		for (final Class<?> beanType : beanTypes) {
			if (!this.beanTypes.contains(beanType) && beanType.getAnnotation(Basis.class) == null) {
				continue;
			}

			BeanDefine beanDefineType = null;
			if (!BeanDefineOriginal.isAbstractBeanType(beanType)) {
				String beanName = BeanDefineType.getBeanName(null, beanType);
				beanDefineType = beanFactory.getBeanDefine(beanName);
				if (beanDefineType == null) {
					beanDefineType = new BeanDefineOriginal(new BeanDefineType(beanName, beanType));
					beanDefines.add(beanDefineType);
					beanFactory.registerBeanDefine(beanDefineType);
				}
			}

			final BeanDefine beanDefine = beanDefineType;
			if (beanType.getAnnotation(Configure.class) != null) {
				KernelReflect.doWithDeclaredMethods(beanType, new CallbackBreak<Method>() {

					@Override
					public void doWith(Method template) throws BreakException {
						// TODO Auto-generated method stub
						if (KernelClass.isAssignableFrom(basisTypes, template.getReturnType()) && template.getAnnotation(Basis.class) != null) {
							if (beanDefine != null || Modifier.isStatic(template.getModifiers())) {
								String beanName = BeanDefineMethod.getBeanName(null, template);
								BeanDefine beanDefineMethod = beanFactory.getBeanDefine(beanName);
								if (beanDefineMethod == null) {
									if (!Modifier.isPrivate(template.getModifiers())) {
										template = KernelReflect.declaredMethod(beanType, template.getName(), template.getParameterTypes());
										if (template == null) {
											return;
										}
									}

									template.setAccessible(true);
									beanDefineMethod = new BeanDefineOriginal(new BeanDefineMethod(beanName, beanDefine, template));
									beanDefines.add(beanDefineMethod);
									beanFactory.registerBeanDefine(beanDefineMethod);
								}
							}
						}
					}
				});
			}
		}

		int length = basisTypes.length;
		for (int i = 0; i < length; i++) {
			Collection collection = collections[i];
			for (Object beanObject : beanFactory.getBeanObjects(basisTypes[i])) {
				if (beanObject instanceof IBeanFactorySupport) {
					if (!((IBeanFactorySupport) beanObject).supports(beanFactory)) {
						continue;
					}
				}

				collection.add(beanObject);
			}
		}
	}

	/**
	 * @param parameter
	 * @return
	 */
	public static List<String> getParameterList(String parameter) {
		return getParameterList(parameter, " ");
	}

	/**
	 * @param parameter
	 * @param split
	 * @return
	 */
	public static List<String> getParameterList(String parameter, String split) {
		if (parameter == null) {
			return null;
		}

		parameter = parameter.trim();
		if (parameter.length() == 0) {
			return null;
		}

		List<String> parameterList = new ArrayList<String>();
		for (String param : parameter.split(split)) {
			param = param.trim();
			if (param.length() > 0) {
				parameterList.add(parameter);
			}
		}

		return parameterList;
	}
}
