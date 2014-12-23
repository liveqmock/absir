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
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	public BeanConfigImpl(IBeanConfigProvider beanConfigProvider) {
		this(beanConfigProvider, null);
	}

	/**
	 * @param beanConfigProvider
	 * @param classPath
	 */
	public BeanConfigImpl(IBeanConfigProvider beanConfigProvider, String classPath) {
		if (classPath == null) {
			classPath = getResourceClassPath();
		}

		BeanFactory beanFactory = BeanFactoryUtils.get();
		this.beanConfig = beanFactory == null ? null : beanFactory.getBeanConfig();
		setClassPath(classPath);
		setResourcePath(classPath);
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
	 * @param classPath
	 */
	private void setClassPath(String classPath) {
		this.classPath = classPath;
		configMap.put("classPath", classPath);
		System.setProperty("classPath", classPath);
	}

	/**
	 * @param resourcePath
	 */
	private void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
		configMap.put("resourcePath", resourcePath);
		System.setProperty("resourcePath", resourcePath);
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
				setResourcePath(HelperFileName.normalizeNoEndSeparator(template) + HelperFileName.SYSTEM_SEPARATOR);
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
	private void readProperties(String filename, Set<String> propertyFilenames, Set<String> loadedPropertyFilenames, Map<String, CallbackTemplate<String>> beanConfigTemplates) {
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
				readProperties(this, configMap, propertyFile, beanConfigTemplates);
			}
		}
	}

	/**
	 * @return
	 */
	public static String getResourceClassPath() {
		String classPath = null;
		URL resourceURL = Thread.currentThread().getContextClassLoader().getResource("");
		if (resourceURL == null) {
			try {
				classPath = BeanConfigImpl.class.getResource("").getPath();
				int length = classPath.length();
				classPath = classPath.substring((length > 4 && "file:".equals(classPath.substring(0, 5).toLowerCase())) ? 5 : 0, length - BeanConfigImpl.class.getPackage().getName().length() - 3);
				classPath = HelperFileName.getFullPath(classPath);

			} catch (Throwable e) {
				classPath = "file:///";
			}

		} else {
			classPath = resourceURL.getPath();
		}

		return classPath;
	}

	/**
	 * @param beanConfig
	 * @param configMap
	 * @param propertyFile
	 * @param propertyFilenames
	 * @param loadedPropertyFilenames
	 * @param beanConfigTemplates
	 */
	public static void readProperties(final BeanConfig beanConfig, final Map<String, Object> configMap, File propertyFile, final Map<String, CallbackTemplate<String>> beanConfigTemplates) {
		if (propertyFile.exists()) {
			try {
				HelperFile.doWithReadLine(propertyFile, new CallbackBreak<String>() {

					/** blockBuilder */
					private StringBuilder blockBuilder;

					/** blockAppending */
					private int blockAppending;

					@Override
					public void doWith(String template) throws BreakException {
						// TODO Auto-generated method stub
						int length = template.length();
						if (length < 1) {
							return;
						}

						char chr = template.charAt(0);
						if (blockBuilder == null) {
							if (chr == '#') {
								return;

							} else if (chr == '{' && length == 2 && template.charAt(1) == '"') {
								blockBuilder = new StringBuilder();
								blockAppending = 1;
								return;
							}

						} else if (blockAppending > 0) {
							if (chr == '"' && length == 2 && template.charAt(1) == '}') {
								blockAppending = 0;

							} else {
								if (blockAppending > 1) {
									blockBuilder.append("\r\n");

								} else {
									blockAppending = 2;
								}

								blockBuilder.append(beanConfig == null ? template : beanConfig.getExpression(template));
							}

							return;
						}

						if (length < 3) {
							return;
						}

						int index = template.indexOf('=');
						if (index > 0 && index < length) {
							String name;
							chr = template.charAt(index - 1);
							if (chr == '.' || chr == '#' || chr == '+') {
								if (index < 1) {
									return;
								}

								name = template.substring(0, index - 1);

							} else {
								chr = 0;
								name = template.substring(0, index);
							}

							length = name.length();
							if (length == 0) {
								return;
							}

							template = template.substring(index + 1);
							if (beanConfig == null) {
								template = KernelString.unTransferred(template);
								if (blockBuilder != null) {
									if (template.length() > 0) {
										blockBuilder.append("\r\n");
										blockBuilder.append(template);
									}

									template = blockBuilder.toString();
									blockBuilder = null;
									blockAppending = 0;
								}

								configMap.put(name, template);

							} else {
								name = name.trim();
								if (length == 0) {
									return;
								}

								length = name.length();
								template = template.trim();
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

								if (environments == null || KernelArray.contain(environments, beanConfig.getEnvironment().name())) {
									template = beanConfig.getExpression(KernelString.unTransferred(template));
									if (blockBuilder != null) {
										if (template.length() > 0) {
											if (template.length() > 0) {
												blockBuilder.append("\r\n");
												blockBuilder.append(template);
											}
										}

										template = blockBuilder.toString();
										blockBuilder = null;
										blockAppending = 0;
									}

									CallbackTemplate<String> callbackTemplate = chr == 0 ? beanConfigTemplates == null ? null : beanConfigTemplates.get(name) : null;
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

										configMap.put(name, value);

									} else {
										callbackTemplate.doWith(template);
									}

								} else if (blockBuilder != null) {
									blockBuilder = null;
									blockAppending = 0;
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

	/**
	 * @param beanConfig
	 * @param configMap
	 * @param propertyDir
	 * @param beanConfigTemplates
	 */
	public static void readDirProperties(final BeanConfig beanConfig, final Map<String, Object> configMap, File propertyDir, final Map<String, CallbackTemplate<String>> beanConfigTemplates) {
		if (propertyDir.exists()) {
			File[] files = propertyDir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.endsWith(".properties");
				}
			});

			for (File file : files) {
				readProperties(beanConfig, configMap, file, beanConfigTemplates);
			}
		}
	}

	/**
	 * @param configMap
	 * @param propertyFile
	 */
	public static void writeProperties(Map<String, ?> configMap, File propertyFile) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, ?> entry : configMap.entrySet()) {
			stringBuilder.append(entry.getKey());
			stringBuilder.append('=');
			stringBuilder.append(entry.getValue());
			stringBuilder.append("\r\n");
		}

		try {
			HelperFile.write(propertyFile, stringBuilder.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return getExpression(expression, false);
	}

	/**
	 * @param expression
	 * @param strict
	 * @return
	 */
	public String getExpression(String expression, boolean strict) {
		int fromIndex = expression.indexOf("${");
		int length = expression.length();
		if (fromIndex >= 0 && fromIndex < length - 2) {
			StringBuilder stringBuilder = new StringBuilder();
			int endIndex = 0;
			while (true) {
				if (fromIndex > endIndex) {
					stringBuilder.append(expression.substring(endIndex, fromIndex));

				} else if (fromIndex < endIndex) {
					if (fromIndex < 0) {
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
					if (value == null) {
						if (strict) {
							return null;
						}

					} else {
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

				return null;
			}
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
	 * @see
	 * com.absir.bean.basis.BeanConfig#getExpressionDefaultValue(java.lang.String
	 * , java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getExpressionDefaultValue(String expression, String beanName, Class<T> toClass) {
		// TODO Auto-generated method stub
		T value = getExpressionObject(expression, beanName, toClass);
		if (value == null) {
			value = DynaBinder.to(expression, beanName, toClass);
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanConfig#getExpressionDefaultValue(java.lang.String
	 * , java.lang.String, java.lang.reflect.Type)
	 */
	@Override
	public Object getExpressionDefaultValue(String expression, String beanName, Type toType) {
		// TODO Auto-generated method stub
		Object value = getExpressionObject(expression, beanName, toType);
		if (value == null) {
			value = DynaBinder.INSTANCE.bind(expression, beanName, toType);
		}

		return value;
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
