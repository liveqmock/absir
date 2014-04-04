/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-10 下午1:43:31
 */
package com.absir.bean.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.absir.bean.basis.BeanConfig;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.Environment;
import com.absir.bean.config.IBeanConfigProvider;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.helper.HelperFile;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.CallbackBreak;
import com.absir.core.kernel.KernelLang.CallbackTemplate;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BeanConfigImpl implements BeanConfig {

	/** beanConfig */
	private BeanConfig beanConfig;

	/** classPath */
	private String classPath;

	/** resourcePath */
	private String resourcePath;

	/** environment */
	private Environment environment = Environment.PRODUCT;

	/** configMap */
	private Map<String, Object> configMap = new HashMap<String, Object>();

	/**
	 * @param beanConfigProvider
	 * @param beanConfig
	 */
	protected BeanConfigImpl(IBeanConfigProvider beanConfigProvider) {
		this(beanConfigProvider, null);
	}

	/**
	 * @param beanConfigProvider
	 * @param classPath
	 */
	protected BeanConfigImpl(IBeanConfigProvider beanConfigProvider, String classPath) {
		if (classPath == null) {
			classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		}

		BeanFactory beanFactory = BeanFactoryUtils.get();
		this.beanConfig = beanFactory == null ? null : beanFactory.getBeanConfig();
		this.classPath = classPath;
		this.resourcePath = classPath;
		Set<String> propertyFilenames = new HashSet<String>();
		Set<String> loadedPropertyFilenames = new HashSet<String>();
		Map<String, CallbackTemplate<String>> beanConfigTemplates = new HashMap<String, CallbackTemplate<String>>();
		loadBeanConfig(beanConfigProvider, propertyFilenames, loadedPropertyFilenames, beanConfigTemplates);
		resourcePath = HelperFileName.normalizeNoEndSeparator(resourcePath) + HelperFileName.SYSTEM_SEPARATOR;
		readProperties(resourcePath + "config.properties", propertyFilenames, loadedPropertyFilenames, beanConfigTemplates);
		readProperties(resourcePath + environment.name().toLowerCase() + ".properties", propertyFilenames, loadedPropertyFilenames, beanConfigTemplates);
		readProperties(resourcePath + "properties", propertyFilenames, loadedPropertyFilenames, beanConfigTemplates);
		while (true) {
			Iterator<String> iterator = propertyFilenames.iterator();
			if (iterator.hasNext()) {
				String filename = iterator.next();
				iterator.remove();
				readProperties(filename, propertyFilenames, loadedPropertyFilenames, beanConfigTemplates);

			} else {
				break;
			}
		}
	}

	/**
	 * @param beanConfigProvider
	 * @param propertyFilenames
	 * @param loadedPropertyFilenames
	 * @param beanConfigTemplates
	 */
	protected void loadBeanConfig(IBeanConfigProvider beanConfigProvider, final Set<String> propertyFilenames, final Set<String> loadedPropertyFilenames,
			final Map<String, CallbackTemplate<String>> beanConfigTemplates) {
		beanConfigTemplates.put("environment", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				try {
					Environment env = Environment.valueOf(template.toUpperCase());
					if (environment != null) {
						environment = env;
					}

				} catch (Exception e) {
				}
			}
		});

		beanConfigTemplates.put("resourcePath", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				template = getResourcePath(template);
				resourcePath = HelperFileName.normalizeNoEndSeparator(template) + HelperFileName.SYSTEM_SEPARATOR;
			}

		});

		beanConfigTemplates.put("propterties", new CallbackTemplate<String>() {

			@Override
			public void doWith(String template) {
				// TODO Auto-generated method stub
				for (String filename : template.split(",")) {
					filename = filename.trim();
					if (filename.length() > 0) {
						filename = getClassPath(filename);
						filename = HelperFileName.normalizeNoEndSeparator(filename);
						if (!loadedPropertyFilenames.contains(filename)) {
							propertyFilenames.add(filename);
						}
					}
				}
			}

		});

		beanConfigProvider.loadBeanConfig(this, propertyFilenames, loadedPropertyFilenames, beanConfigTemplates);
	}

	/**
	 * @param filename
	 * @param propertyFilenames
	 * @param loadedPropertyFilenames
	 * @param beanConfigTemplates
	 */
	private void readProperties(String filename, final Set<String> propertyFilenames, final Set<String> loadedPropertyFilenames, final Map<String, CallbackTemplate<String>> beanConfigTemplates) {
		if (!loadedPropertyFilenames.add(filename)) {
			return;
		}

		File propertyFile = new File(filename);
		if (propertyFile.exists()) {
			if (propertyFile.isDirectory()) {
				File[] files = propertyFile.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						// TODO Auto-generated method stub
						if (name.endsWith(".properties")) {
							int index = name.indexOf('_');
							return index <= 0 || name.substring(0, index).equals(environment.name().toLowerCase()) ? true : false;
						}

						return false;
					}
				});

				for (File file : files) {
					filename = file.getPath();
					if (!loadedPropertyFilenames.contains(filename)) {
						propertyFilenames.add(file.getPath());
					}
				}

			} else {
				try {
					HelperFile.doWithReadLine(propertyFile, new CallbackBreak<String>() {

						@Override
						public void doWith(String template) throws BreakException {
							// TODO Auto-generated method stub
							template = template.trim();
							int length = template.length();
							if (length <= 2 || template.charAt(0) == '#') {
								return;
							}

							int index = template.indexOf('=');
							if (index > 0 && index < length - 1) {
								char chr = template.charAt(index - 1);
								String name;
								if (chr == '.' || chr == '#' || chr == '+') {
									if (index < 1) {
										return;
									}

									name = template.substring(0, index - 1).trim();

								} else {
									chr = 0;
									name = template.substring(0, index).trim();
								}

								length = name.length();
								if (length == 0) {
									return;
								}

								template = template.substring(index + 1).trim();
								String[] environments = null;
								index = name.indexOf('|');
								if (index > 0) {
									if (length <= 1) {
										return;
									}

									String environmentParams = name.substring(index + 1);
									name = name.substring(0, index).trim();
									length = name.length();
									if (length == 0) {
										return;
									}

									environments = environmentParams.trim().split("\\|");
								}

								if (environments == null || KernelArray.contain(environments, environment.name())) {
									template = getExpression(template);
									CallbackTemplate<String> callbackTemplate = chr == 0 ? beanConfigTemplates.get(name) : null;
									if (callbackTemplate == null) {
										Object value = template;
										if (chr != 0) {
											Object old;
											switch (chr) {
											case '.':
												old = DynaBinder.to(configMap.get(name), String.class);
												if (old != null) {
													value = old + template;
												}

												break;
											case '#':
												old = DynaBinder.to(configMap.get(name), String.class);
												if (old != null) {
													value = old + "\r\n" + template;
												}

												break;
											case '+':
												old = DynaBinder.to(configMap.get(name), List.class);
												if (old != null) {
													((List) old).add(template);
													value = old;
												}

												break;
											default:
												break;
											}
										}

										setValue(name, value);

									} else {
										callbackTemplate.doWith(template);
									}
								}
							}
						}
					});

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the classPath
	 */
	public String getClassPath() {
		return classPath;
	}

	/**
	 * @return the resourcePath
	 */
	public String getResourcePath() {
		return resourcePath;
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * @param name
	 * @return
	 */
	public Object getValue(String name) {
		Object obj = configMap.get(name);
		if (obj == null && !configMap.containsKey(name)) {
			if (beanConfig != null) {
				obj = beanConfig.getValue(name);
			}
		}

		return obj;
	}

	/**
	 * @param name
	 * @param obj
	 */
	public void setValue(String name, Object obj) {
		configMap.put(name, obj);
	}

	/**
	 * @param expression
	 * @return
	 */
	public String getExpression(String expression) {
		int fromIndex = expression.indexOf("${");
		if (fromIndex >= 0 && fromIndex < expression.length() - 2) {
			StringBuilder stringBuilder = new StringBuilder();
			int endIndex = 0;
			while (true) {
				if (fromIndex > endIndex) {
					stringBuilder.append(expression.substring(endIndex, fromIndex));

				} else if (fromIndex < endIndex) {
					if (fromIndex < 0) {
						int length = expression.length();
						if (length > ++endIndex) {
							stringBuilder.append(expression.substring(endIndex, length));
						}
					}

					break;
				}

				if ((endIndex = expression.indexOf('}', fromIndex)) < 0) {
					stringBuilder.append(expression.substring(fromIndex));
					break;
				}

				fromIndex += 2;
				if (fromIndex < endIndex) {
					Object value = getValue(expression.substring(fromIndex, endIndex));
					if (value != null) {
						stringBuilder.append(value);
					}
				}

				fromIndex = expression.indexOf("${", endIndex);
			}

			expression = stringBuilder.toString();
			expression.replace("$$", "$");
		}

		return expression;
	}

	/**
	 * @param expression
	 * @param beanName
	 * @param toClass
	 * @return
	 */
	public <T> T getExpressionObject(String expression, String beanName, Class<T> toClass) {
		Object obj = getExpression(expression);
		if (obj == expression) {
			obj = getMapValue(configMap, expression, beanName, toClass);
			if (obj == null && !configMap.containsKey(expression)) {
				if (beanConfig != null) {
					return beanConfig.getExpressionObject(expression, beanName, toClass);
				}
			}

			return null;
		}

		return DynaBinder.to(obj, beanName, toClass);
	}

	/**
	 * @param expression
	 * @param beanName
	 * @param toType
	 * @return
	 */
	public Object getExpressionObject(String expression, String beanName, Type toType) {
		Object obj = getExpression(expression);
		if (obj == expression) {
			obj = getMapValue(configMap, expression, beanName, toType);
			if (obj == null && !configMap.containsKey(expression)) {
				if (beanConfig != null) {
					obj = beanConfig.getExpressionObject(expression, beanName, toType);
				}
			}

			return obj;

		} else {
			return DynaBinder.INSTANCE.bind(obj, beanName, toType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanConfig#getExpressionValue(java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getExpressionValue(String expression, String beanName, Class<T> toClass) {
		// TODO Auto-generated method stub
		return DynaBinder.to(getExpressionObject(expression, beanName, toClass), null, toClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanConfig#getClassPath(java.lang.String)
	 */
	@Override
	public String getClassPath(String filename) {
		// TODO Auto-generated method stub
		return getResourcePath(filename, classPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanConfig#getResourcePath(java.lang.String)
	 */
	@Override
	public String getResourcePath(String filename) {
		// TODO Auto-generated method stub
		return getResourcePath(filename, resourcePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanConfig#getResourcePath(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getResourcePath(String filename, String nullPrefix) {
		// TODO Auto-generated method stub
		filename = filename.replace("classpath:", classPath);
		filename = filename.replace("resourcePath:", resourcePath);
		if (KernelString.isEmpty(HelperFileName.getPrefix(filename))) {
			filename = nullPrefix + filename;
		}

		return filename;
	}

	/**
	 * @param map
	 * @param name
	 * @param beanName
	 * @param toClass
	 * @return
	 */
	public static <T> T getMapValue(Map map, Object name, String beanName, Class<T> toClass) {
		Object obj = map.get(name);
		if (obj != null) {
			T toObject = DynaBinder.to(obj, beanName, toClass);
			if (toObject != obj) {
				map.put(name, toObject);
			}

			return toObject;
		}

		return null;
	}

	/**
	 * @param map
	 * @param name
	 * @param beanName
	 * @param toType
	 * @return
	 */
	public static Object getMapValue(Map map, Object name, String beanName, Type toType) {
		Object obj = map.get(name);
		if (obj != null) {
			Object toObject = DynaBinder.INSTANCE.bind(obj, beanName, toType);
			if (toObject != obj) {
				map.put(name, toObject);
			}

			return toObject;
		}

		return null;
	}
}
