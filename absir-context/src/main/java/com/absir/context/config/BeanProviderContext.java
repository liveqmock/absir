/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-13 下午12:37:45
 */
package com.absir.context.config;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.absir.bean.basis.BeanConfig;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanDefineSingleton;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.bean.core.BeanFactoryProvider;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.InjectBeanDefine;
import com.absir.bean.inject.InjectBeanFactory;
import com.absir.bean.inject.InjectInvoker;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelReflect;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class BeanProviderContext extends BeanFactoryProvider {

	/** contextFilenames */
	private Map<String, Boolean> contextFilenames = new HashMap<String, Boolean>();

	/** beanNameMapAtInjectInvokers */
	private Map<String, List<Entry<String, InjectInvoker>>> beanNameMapAtInjectInvokers = new HashMap<String, List<Entry<String, InjectInvoker>>>();

	/**
	 * @param includePackages
	 * @param excludePackages
	 * @param unMatchPatterns
	 */
	public BeanProviderContext(Collection<String> includePackages, Collection<String> excludePackages, Collection<String> unMatchPatterns) {
		super(includePackages, excludePackages, unMatchPatterns);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.core.BeanFactoryProvider#loadBeanConfig(com.absir.bean
	 * .basis.BeanConfig, java.util.Set, java.util.Set, java.util.Map)
	 */
	@Override
	public void loadBeanConfig(final BeanConfig beanConfig, Set<String> propertyFilenames, Set<String> loadedPropertyFilenames, Map<String, CallbackTemplate<String>> beanConfigTemplates) {
		super.loadBeanConfig(beanConfig, propertyFilenames, loadedPropertyFilenames, beanConfigTemplates);
		beanConfigTemplates.put("context", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				for (String filename : template.split(",")) {
					filename = filename.trim();
					if (filename.length() > 0) {
						filename = beanConfig.getClassPath(filename);
						filename = HelperFileName.normalizeNoEndSeparator(filename);
						if (!contextFilenames.containsKey(filename)) {
							contextFilenames.put(filename, null);
						}
					}
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.core.BeanFactoryProvider#registerBeanDefine(com.absir.
	 * bean.core.BeanFactoryImpl, java.util.Set)
	 */
	@Override
	protected void registerBeanDefine(BeanFactoryImpl beanFactory, Set<String> beanDefineNames) {
		super.registerBeanDefine(beanFactory, beanDefineNames);
		registerBeanDefine(beanFactory.getBeanConfig().getClassPath() + "context.xml", beanFactory, beanDefineNames);
		registerBeanDefine(beanFactory.getBeanConfig().getClassPath() + "context-" + beanFactory.getBeanConfig().getEnvironment().name().toLowerCase() + ".xml", beanFactory, beanDefineNames);
		registerBeanDefine(beanFactory.getBeanConfig().getClassPath() + "contexts", beanFactory, beanDefineNames);
		for (String filename : contextFilenames.keySet()) {
			registerBeanDefine(filename, beanFactory, beanDefineNames);
		}
	}

	/**
	 * @param filename
	 * @param beanFactory
	 * @param beanDefineNames
	 */
	private void registerBeanDefine(String filename, BeanFactoryImpl beanFactory, Set<String> beanDefineNames) {
		if (contextFilenames.get(filename) != null) {
			return;
		}

		contextFilenames.put(filename, Boolean.TRUE);
		File xmlFile = new File(filename);
		if (!xmlFile.exists()) {
			return;
		}

		if (xmlFile.isDirectory()) {
			final String environment = beanFactory.getBeanConfig().getEnvironment().name().toLowerCase();
			File[] files = xmlFile.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					if (name.endsWith(".xml")) {
						int index = name.indexOf('_');
						return index <= 0 || name.substring(0, index).equals(environment) ? true : false;
					}

					return false;
				}
			});

			for (File file : files) {
				registerBeanDefine(file.getPath(), beanFactory, beanDefineNames);
			}

		} else {
			SAXReader saxReader = new SAXReader();
			try {
				Element element = saxReader.read(xmlFile).getRootElement();
				Iterator<Element> iterator = element.elementIterator();
				while (iterator.hasNext()) {
					Element beanElement = iterator.next();
					if ("bean".equals(beanElement.getName())) {
						registerBeanDefineBean(beanElement, true, beanFactory, beanDefineNames);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * @param beanElement
	 * @param root
	 * @param beanFactory
	 * @return
	 */
	private BeanDefine registerBeanDefine(Element element, BeanFactoryImpl beanFactory, Set<String> beanDefineNames) {
		Iterator<Element> iterator = element.elementIterator();
		if (iterator.hasNext()) {
			String name = iterator.next().getName();
			if (KernelString.isEmpty(name)) {
				if ("bean".equals(name)) {
					return registerBeanDefineBean(element, false, beanFactory, beanDefineNames);

				} else if ("array".equals(name)) {
					return registerBeanDefineArray(element, beanFactory, beanDefineNames);

				} else if ("map".equals(name)) {
					return registerBeanDefineMap(element, beanFactory, beanDefineNames);
				}
			}

		} else {
			String ref = element.attributeValue("ref");
			String required = element.attributeValue("required");
			if (ref != null || required != null) {
				return new BeanDefineReference(ref, required);
			}

			String value = element.attributeValue("value");
			if (value == null) {
				value = element.getText();
			}

			return new BeanDefineSingleton(beanFactory.getBeanConfig().getExpression(value));
		}

		return new BeanDefineSingleton(element.getText());
	}

	/**
	 * @param beanElement
	 * @param root
	 * @param beanFactory
	 * @param beanDefineNames
	 * @return
	 */
	private BeanDefine registerBeanDefineBean(Element beanElement, boolean root, BeanFactoryImpl beanFactory, Set<String> beanDefineNames) {
		Class<?> beanType = null;
		try {
			String classname = beanElement.attributeValue("class");
			beanType = Thread.currentThread().getContextClassLoader().loadClass(classname);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
				e.printStackTrace();
			}

			return null;
		}

		List<InjectInvoker> injectInvokers = new ArrayList<InjectInvoker>();
		List<Entry<String, InjectInvoker>> atInjectInvokers = new ArrayList<Entry<String, InjectInvoker>>();
		String beanName = beanElement.attributeValue("name");
		String beanScope = beanElement.attributeValue("scope");
		BeanDefineArray constructorBeanDefine = null;
		Iterator<Element> iterator = beanElement.elementIterator();
		while (iterator.hasNext()) {
			Element element = iterator.next();
			String name = element.getName();
			if ("constructor".equals(name)) {
				constructorBeanDefine = registerBeanDefineArray(element, beanFactory, beanDefineNames);

			} else if ("property".equals(name)) {
				String propertyName = element.attributeValue("name");
				if (propertyName == null) {
					continue;
				}

				injectInvokers.add(new InjectFieldBean(propertyName, registerBeanDefine(element, beanFactory, beanDefineNames)));

			} else if ("method".equals(name)) {
				String methodName = element.attributeValue("name");
				if (methodName == null) {
					continue;
				}

				Iterator<Element> methodIterator = element.elementIterator();
				BeanDefineArray beanDefineArray = methodIterator.hasNext() ? registerBeanDefineArray(methodIterator.next(), beanFactory, beanDefineNames) : null;
				int size = beanDefineArray == null ? 0 : beanDefineArray.getBeanDefines().size();
				Method method = size == 0 ? KernelReflect.assignableMethod(beanType, methodName, 0, true, true, false) : KernelReflect.assignableMethod(beanType, methodName, 0, true, true, false,
						KernelArray.repeat(Object.class, size));
				if (method == null) {
					continue;
				}

				String methodAt = element.attributeValue("at");
				InjectMethodBean injectMethodBean = new InjectMethodBean(method, beanDefineArray);
				if (KernelString.isEmpty(methodAt)) {
					injectInvokers.add(injectMethodBean);

				} else {
					atInjectInvokers.add(new ObjectEntry<String, InjectInvoker>(methodAt, injectMethodBean));
				}
			}
		}

		BeanDefine beanDefine = BeanDefineFactory.getBeanDefineBean(beanType, beanName, constructorBeanDefine);
		((BeanDefineBean) beanDefine).getInjectInvokers().addAll(injectInvokers);
		if (root || !KernelString.isEmpty(beanName) || !KernelString.isEmpty(beanScope)) {
			BeanScope scope = BeanScope.SINGLETON;
			try {
				scope = BeanScope.valueOf(beanScope);

			} catch (Exception e) {
			}

			beanDefine = new InjectBeanDefine(beanDefine, scope);
			beanDefineNames.add(beanDefine.getBeanName());
			beanFactory.registerBeanDefine(beanDefine);
			beanNameMapAtInjectInvokers.put(beanDefine.getBeanName(), atInjectInvokers);

		} else {
			if (!atInjectInvokers.isEmpty()) {
				if (beanFactory.getBeanConfig().getEnvironment() == Environment.DEBUG) {
					System.out.println("Method at can not define for " + beanType);
				}
			}
		}

		return beanDefine;
	}

	/**
	 * @param arrayElement
	 * @param beanFactory
	 * @param beanDefineNames
	 * @return
	 */
	private BeanDefineArray registerBeanDefineArray(Element arrayElement, BeanFactoryImpl beanFactory, Set<String> beanDefineNames) {
		BeanDefineArray beanDefineArray = new BeanDefineArray();
		Iterator<Element> iterator = arrayElement.elementIterator();
		while (iterator.hasNext()) {
			beanDefineArray.getBeanDefines().add(registerBeanDefine(iterator.next(), beanFactory, beanDefineNames));
		}

		return beanDefineArray;
	}

	/**
	 * @param MapElement
	 * @param beanFactory
	 * @param beanDefineNames
	 * @return
	 */
	private BeanDefineMap registerBeanDefineMap(Element MapElement, BeanFactoryImpl beanFactory, Set<String> beanDefineNames) {
		BeanDefineMap beadefineMap = new BeanDefineMap();
		Iterator<Element> iterator = MapElement.elementIterator();
		while (iterator.hasNext()) {
			Element element = iterator.next();
			String key = element.attributeValue("key");
			beadefineMap.getBeanDefines().put(key, registerBeanDefine(element, beanFactory, beanDefineNames));
		}

		return beadefineMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.core.BeanFactoryProvider#processorBeanDefineObject(com
	 * .absir.bean.core.BeanFactoryImpl, com.absir.bean.basis.BeanDefine,
	 * java.lang.Object)
	 */
	@Override
	protected void processorBeanDefineObject(BeanFactoryImpl beanFactory, BeanDefine beanDefine, Object beanObject) {
		super.processorBeanDefineObject(beanFactory, beanDefine, beanObject);
		List<Entry<String, InjectInvoker>> atInjectInvokers = beanNameMapAtInjectInvokers.get(beanDefine.getBeanName());
		if (atInjectInvokers != null) {
			for (Entry<String, InjectInvoker> entry : atInjectInvokers) {
				String[] ats = entry.getKey().split(" ");
				if (KernelArray.contain(ats, "started")) {
					InjectBeanFactory.getInstance().addStated(beanObject, entry.getValue());
				}

				if (KernelArray.contain(ats, "stopping")) {
					InjectBeanFactory.getInstance().addStopping(beanObject, entry.getValue());
				}
			}

			if (beanDefine.getBeanScope() != BeanScope.PROTOTYPE) {
				beanNameMapAtInjectInvokers.remove(beanDefine.getBeanName());
			}
		}
	}
}
