/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月4日 下午2:32:30
 */
package jetbrick.template;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jetbrick.template.parser.VariableResolver;
import jetbrick.template.runtime.JetPageContext;
import jetbrick.template.runtime.JetTagContext;

import com.absir.core.kernel.KernelObject;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
public class VariableResolverBean {

	/**
	 * @param engine
	 * @param config
	 */
	public static void load(JetEngine engine, JetConfig config) {
		engine.load(config);
	}

	/** variableResolver */
	private VariableResolver variableResolver;

	private Map<String, List<Method>> methodMap1 = new HashMap<String, List<Method>>(64);
	private Map<String, List<Method>> methodMap2 = new HashMap<String, List<Method>>(32);
	private Map<String, List<Method>> functionMap1 = new HashMap<String, List<Method>>(32);
	private Map<String, List<Method>> functionMap2 = new HashMap<String, List<Method>>();
	private Map<String, List<Method>> tagMap = new HashMap<String, List<Method>>();

	/**
	 * @param engine
	 */
	public VariableResolverBean(JetEngine engine) {
		variableResolver = engine.getVariableResolver();
		methodMap1 = (Map<String, List<Method>>) KernelObject.declaredGet(variableResolver, "methodMap1");
		methodMap2 = (Map<String, List<Method>>) KernelObject.declaredGet(variableResolver, "methodMap2");
		functionMap1 = (Map<String, List<Method>>) KernelObject.declaredGet(variableResolver, "functionMap1");
		functionMap2 = (Map<String, List<Method>>) KernelObject.declaredGet(variableResolver, "functionMap2");
		tagMap = (Map<String, List<Method>>) KernelObject.declaredGet(variableResolver, "tagMap");
	}

	/**
	 * @return the variableResolver
	 */
	public VariableResolver getVariableResolver() {
		return variableResolver;
	}

	/**
	 * @param name
	 * @param method
	 */
	public void registerMethod(String name, Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length == 0) {
			return;
		}
		int modifiers = method.getModifiers();
		if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
			// String name = method.getName();

			List<Method> list;
			if (parameterTypes.length > 1 && JetPageContext.class.equals(parameterTypes[1])) {
				list = methodMap2.get(name);
				if (list == null) {
					list = new ArrayList<Method>(4);
					methodMap2.put(name, list);
				}
			} else {
				list = methodMap1.get(name);
				if (list == null) {
					list = new ArrayList<Method>(4);
					methodMap1.put(name, list);
				}
			}
			list.add(method);
		}
	}

	/**
	 * @param name
	 * @param method
	 */
	public void registerFunction(String name, Method method) {
		int modifiers = method.getModifiers();
		if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
			// String name = method.getName();

			List<Method> list;
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length > 0 && JetPageContext.class.equals(parameterTypes[0])) {
				list = functionMap2.get(name);
				if (list == null) {
					list = new ArrayList<Method>(4);
					functionMap2.put(name, list);
				}
			} else {
				list = functionMap1.get(name);
				if (list == null) {
					list = new ArrayList<Method>(4);
					functionMap1.put(name, list);
				}
			}
			list.add(method);
		}
	}

	/**
	 * @param name
	 * @param method
	 */
	public void registerTag(String name, Method method) {
		int modifiers = method.getModifiers();
		if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
			if (!Void.TYPE.equals(method.getReturnType())) {
				return;
			}
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length > 0 && JetTagContext.class.equals(parameterTypes[0])) {
				// String name = method.getName();
				List<Method> list = tagMap.get(name);
				if (list == null) {
					list = new ArrayList<Method>(4);
					tagMap.put(name, list);
				}
				list.add(method);
			}
		}
	}
}
