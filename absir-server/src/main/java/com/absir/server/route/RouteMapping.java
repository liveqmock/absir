/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-18 下午5:57:40
 */
package com.absir.server.route;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Value;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelString;
import com.absir.server.in.InMethod;
import com.absir.server.value.Mapping;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class RouteMapping implements IRoute {

	@Inject
	private RouteAdapter routeAdapter;

	@Value("server.route.suffix")
	private String routeSuffix;

	/** routeParameter */
	private RouteParameter routeParameter;

	/** routeParameterPatterns */
	private Map<String, RouteParameter> routeParameterPatterns;

	/** routeParameterSplits */
	private Map<String, RouteParameter> routeParameterSplits;

	/**
	 * @return the routeAdapter
	 */
	public RouteAdapter getRouteAdapter() {
		return routeAdapter;
	}

	/**
	 * @param routeAction
	 * @param mappings
	 * @param inMethods
	 */
	public void routeMapping(RouteAction routeAction, String[] parameterPathNames, List<String> mappings, List<InMethod> inMethods) {
		if (mappings == null || mappings.isEmpty()) {
			return;
		}

		int length = parameterPathNames == null ? 0 : parameterPathNames.length;
		for (String mapping : mappings) {
			String suffix = null;
			String[] parameterNames = parameterPathNames;
			RouteParameter routeParameter = null;
			if (length > 0) {
				int[] matchs = null;
				int split = 0;
				String[] splits = new String[length];
				Matcher matcher = PATH_PATTERN.matcher(mapping);
				for (int i = 0; i < length; i++) {
					if (!matcher.find()) {
						throw new RuntimeException("can not match parameterNames in " + mapping);
					}

					if (i == 0) {
						splits[0] = mapping.substring(0, matcher.start());
					}

					splits[i] = mapping.substring(split, matcher.start());
					split = matcher.end();
					String param = getGroupParam(matcher);
					if (!"*".equals(param)) {
						// match param
						for (int j = 0; j < length; j++) {
							if (parameterPathNames[j].equals(param)) {
								if (j == i) {
									break;
								}

								if (matchs == null) {
									matchs = new int[length];
									Arrays.fill(matchs, -1);
									matchs[i] = j;

								} else {
									for (int match : matchs) {
										if (match == j) {
											continue;
										}
									}

									matchs[i] = j;
								}
							}
						}
					}
				}

				// parameter matchs
				if (matchs != null) {
					for (int i = 0; i < length; i++) {
						int m = -1;
						for (int j = 0; j < length; j++) {
							if (matchs[j] < 0) {
								m = j;

							} else if (matchs[j] == i) {
								m = -1;
								break;
							}
						}

						if (m > 0) {
							matchs[m] = i;
						}
					}
				}

				// new pathParameterNames
				if (matchs != null) {
					parameterNames = new String[length];
					for (int i = 0; i < length; i++) {
						parameterNames[i] = parameterPathNames[(matchs[i])];
					}
				}

				// suffix
				if (split < mapping.length()) {
					suffix = mapping.substring(split, mapping.length());
				}

				// generate route matcher mapping splits matchs suffix
				mapping = splits[0];
				routeParameter = getRouteParameter(splits);
			}

			RouteMatcher routeMatcher = new RouteMatcher(routeAction, mapping, suffix, inMethods, parameterNames, routeParameter);
			routeAdapter.getRouteMatchers().add(routeMatcher);
		}
	}

	/**
	 * @param splits
	 * @return
	 */
	public RouteParameter getRouteParameter(String[] splits) {
		int length = splits.length;
		if (length < 1) {
			return null;
		}

		if (length == 1) {
			if (routeParameter == null) {
				routeParameter = new RouteParameter();
			}

			return routeParameter;
		}

		String regex = splits[1];
		for (int i = 2; i < length; i++) {
			if (!splits[i].equals(regex)) {
				regex = null;
				break;
			}
		}

		RouteParameter routeParameter = null;
		if (regex == null) {
			regex = "(.*?)" + KernelString.implodeOffset(splits, 1, 0, null, null, "(.*?)") + "((.*?))";
			if (routeParameterPatterns == null) {
				routeParameterPatterns = new HashMap<String, RouteParameter>();

			} else {
				routeParameter = routeParameterPatterns.get(regex);
			}

			if (routeParameter == null) {
				regex.replace("\\", "\\\\");
				regex.replace("/", "\\/");
				routeParameter = new RouteParameterPattern('^' + regex + '$');
				routeParameterPatterns.put(regex, routeParameter);
			}

		} else {
			if (routeParameterSplits == null) {
				routeParameterSplits = new HashMap<String, RouteParameter>();

			} else {
				routeParameter = routeParameterSplits.get(regex);
			}

			if (routeParameter == null) {
				routeParameter = new RouteParameterSplit(regex);
				routeParameterSplits.put(regex, routeParameter);
			}
		}

		return routeParameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.route.IRoute#routeMapping(java.lang.String,
	 * java.util.Map.Entry, java.lang.reflect.Method, java.util.List,
	 * java.util.List, java.util.List)
	 */
	@Override
	public void routeMapping(String name, Entry<Mapping, List<String>> mapping, Method method, List<String> parameterPathNames, List<String> mappings, List<InMethod> inMethods) {
		// TODO Auto-generated method stub
		String parameterPathName = KernelString.implode(KernelArray.repeat('*', parameterPathNames.size()), '/');
		if (routeSuffix != null) {
			parameterPathName += routeSuffix;
		}

		routeMapping(null, name, mapping, method, method.getName(), parameterPathName, KernelCollection.toArray(parameterPathNames, String.class), mappings, inMethods);
	}

	/** PATH_PATTERN */
	public static Pattern PATH_PATTERN = Pattern.compile("\\*|\\{((.*?))\\}");

	/**
	 * @param matcher
	 * @return
	 */
	public static String getGroupParam(Matcher matcher) {
		String param = matcher.group(2);
		if (param == null) {
			param = matcher.group(1);
		}

		return param;
	}

	/**
	 * @param name
	 * @return
	 */
	public static boolean routeEquals(String name) {
		return "route".equals(name);
	}

	/**
	 * @param value
	 * @param routeName
	 * @return
	 */
	private static String routeMapping(String value, String routeName) {
		if (routeName == null) {
			value.replace("${route}" + HelperFileName.UNIX_SEPARATOR, "");
			value.replace("${route}", "");

		} else {
			value.replace("${route}", routeName);
			value.replace("", "");
		}

		return value;
	}

	/**
	 * @param value
	 * @param routeName
	 * @param methodPathName
	 * @return
	 */
	private static String routeMapping(String value, String routeName, String methodPathName) {
		value = routeMapping(value, routeName);
		if (methodPathName == null) {
			value.replace("${method}" + HelperFileName.UNIX_SEPARATOR, "");
			value.replace("${method}", "");

		} else {
			value.replace("${method}", methodPathName);
		}

		return value;
	}

	/**
	 * @param rootName
	 * @param routeName
	 * @param mapping
	 * @param method
	 * @param methodPathName
	 * @param parameterPathName
	 * @param parameterNames
	 * @param mappings
	 * @param inMethods
	 */
	public static void routeMapping(String rootName, String routeName, Entry<Mapping, List<String>> mapping, Method method, String methodPathName, String parameterPathName, String[] parameterNames,
			List<String> mappings, List<InMethod> inMethods) {
		// class name route
		if (KernelString.isEmpty(routeName) || routeEquals(routeName)) {
			routeName = null;
		}

		// method name route
		if (KernelString.isEmpty(methodPathName) || routeEquals(methodPathName)) {
			methodPathName = null;
		}

		// parameter name route
		if (KernelString.isEmpty(parameterPathName)) {
			parameterPathName = null;
		}

		// class name mapping
		List<String> routeNames = mapping.getValue();
		if (routeNames == null) {
			if (routeEquals(routeName)) {
				routeName = null;
			}

			routeNames = new ArrayList<String>();
			mapping.setValue(routeNames);
			if (mapping.getKey() != null) {
				for (String value : mapping.getKey().value()) {
					if (value != null) {
						value = routeMapping(value, routeName);
						value = BeanFactoryUtils.getBeanConfig().getExpression(value);
						value = HelperFileName.normalize(value, true);
						if (!KernelString.isEmpty(value)) {
							if (value.charAt(0) == HelperFileName.UNIX_SEPARATOR) {
								if (value.length() == 1) {
									value = "";

								} else if (value.charAt(value.length() - 1) == HelperFileName.UNIX_SEPARATOR) {
									value = value.substring(1, value.length() - 1);

								} else {
									value = value.substring(1);
									if (routeName != null) {
										value += HelperFileName.UNIX_SEPARATOR + routeName;
									}
								}

							} else {
								if (value.charAt(value.length() - 1) == HelperFileName.UNIX_SEPARATOR) {
									value = value.substring(0, value.length() - 1);
								}

								if (routeName != null) {
									value = routeName + HelperFileName.UNIX_SEPARATOR + value;
								}
							}

							routeNames.add(value);
						}
					}
				}
			}

			if (routeName != null && routeNames.isEmpty()) {
				routeNames.add(routeName);
			}
		}

		// mapping names
		List<String> mappingNames = new ArrayList<String>();
		Mapping methodMapping = method.getAnnotation(Mapping.class);
		if (methodMapping != null) {
			int length = parameterNames == null ? 0 : parameterNames.length;
			int[][] prameterGroups = null;
			for (String value : methodMapping.value()) {
				value = HelperFileName.normalize(value, true);
				value = routeMapping(value, routeName, methodPathName);
				if (!KernelString.isEmpty(value)) {
					if (parameterPathName == null || length <= 0) {
						mappingNames.add(value);

					} else {
						Matcher matcher = PATH_PATTERN.matcher(value);
						int i, p;
						for (i = p = 0; i < length; i++) {
							if (!matcher.find()) {
								break;
							}

							if (i == 0) {
								String param = getGroupParam(matcher);
								if (!"*".equals(param)) {
									for (int j = 0; j < length; j++) {
										if (parameterNames[j].equals(param)) {
											if (p < j) {
												p = j;
											}

											break;
										}
									}
								}
							}
						}

						if (i == 0) {
							value += HelperFileName.UNIX_SEPARATOR + parameterPathName;

						} else if (i < length) {
							boolean root = value.charAt(0) == HelperFileName.UNIX_SEPARATOR;
							if (root) {
								value = value.substring(1);
							}

							if (prameterGroups == null) {
								prameterGroups = new int[length][2];
								matcher = PATH_PATTERN.matcher(parameterPathName);
								for (int j = 0; j < length; j++) {
									if (!matcher.find()) {
										break;
									}

									prameterGroups[j] = new int[2];
									prameterGroups[j][0] = matcher.start();
									prameterGroups[j][1] = matcher.end();
								}
							}

							if (p > 0) {
								value = parameterPathName.substring(0, prameterGroups[p][0]) + value;
							}

							p += i;
							if (p < length) {
								value += parameterPathName.substring(prameterGroups[p - 1][1], parameterPathName.length());
							}

							if (root) {
								value += HelperFileName.UNIX_SEPARATOR;
							}
						}

						mappingNames.add(value);
					}
				}
			}

			for (InMethod inMethod : methodMapping.method()) {
				if (!inMethods.contains(inMethod)) {
					inMethods.add(inMethod);
				}
			}
		}

		if (mappingNames.isEmpty()) {
			String mappingName = methodPathName;
			if (parameterPathName != null) {
				mappingName = mappingName == null ? parameterPathName : (mappingName + HelperFileName.UNIX_SEPARATOR + parameterPathName);
			}

			if (mappingName != null) {
				mappingNames.add(mappingName);
			}
		}

		if (inMethods.isEmpty() && mapping.getKey() != null) {
			for (InMethod inMethod : mapping.getKey().method()) {
				if (!inMethods.contains(inMethod)) {
					inMethods.add(inMethod);
				}
			}
		}

		if (mappingNames.isEmpty()) {
			mappings.addAll(routeNames);

		} else {
			for (String mappingName : mappingNames) {
				if (!KernelString.isEmpty(mappingName)) {
					if (mappingName.charAt(0) == HelperFileName.UNIX_SEPARATOR) {
						if (mappingName.length() == 1) {
							continue;
						}

						mappingName = mappingName.substring(1);
						mappings.add(mappingName);

					} else {
						if (routeNames.isEmpty()) {
							mappings.add(mappingName);

						} else {
							for (String name : routeNames) {
								mappings.add(name + HelperFileName.UNIX_SEPARATOR + mappingName);
							}
						}
					}
				}
			}
		}

		if (mappings.isEmpty()) {
			mappings.add("");
		}

		if (!KernelString.isEmpty(rootName)) {
			int size = mappings.size();
			for (int i = 0; i < size; i++) {
				routeName = mappings.get(i);
				if (KernelString.isEmpty(routeName)) {
					routeName = rootName;

				} else {
					routeName = rootName + HelperFileName.UNIX_SEPARATOR + mappings.get(i);
				}

				mappings.set(i, routeName);
			}
		}
	}
}
