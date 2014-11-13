/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-15 下午5:41:36
 */
package com.absir.core.kernel;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.CallbackBreak;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class KernelClass {

	/**
	 * @param cls
	 * @return
	 */
	public static boolean isBasicClass(Class cls) {
		return cls.getPackage() == Character.class.getPackage() || Date.class.isAssignableFrom(cls) || Enum.class.isAssignableFrom(cls) || !Object.class.isAssignableFrom(cls);
	}

	/**
	 * @param cls
	 * @return
	 */
	public static boolean isCustomClass(Class cls) {
		return cls.getPackage() != Map.class.getPackage() && !isBasicClass(cls);
	}

	/**
	 * @param classname
	 * @return
	 */
	public static String parentName(Class cls) {
		return KernelString.rightSubString(cls.getName(), cls.getSimpleName().length() + 1);
	}

	/**
	 * @param cls
	 * @param types
	 * @return
	 */
	public static boolean isAssignableFrom(Class cls, Class[] types) {
		for (Class type : types) {
			if (cls.isAssignableFrom(type)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param classes
	 * @param type
	 * @return
	 */
	public static boolean isAssignableFrom(Class[] classes, Class type) {
		for (Class cls : classes) {
			if (cls.isAssignableFrom(type)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param classes
	 * @param types
	 * @return
	 */
	public static boolean isAssignableFrom(Class[] classes, Class[] types) {
		int length = classes.length;
		if (length == types.length) {
			for (int i = 0; i < length; i++) {
				if (!classes[i].isAssignableFrom(types[i])) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * @param cls
	 * @param T
	 * @return
	 */
	public static <T> Class<? extends T> classAssignable(Class cls, Class T) {
		return classAssignable(cls, null, T);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param T
	 * @return
	 */
	public static <T> Class<? extends T> classAssignable(Class cls, Class<? extends T> defaultValue, Class T) {
		if (cls == null || !T.isAssignableFrom(cls)) {
			return defaultValue;
		}

		return cls;
	}

	/**
	 * @param cls
	 * @param type
	 * @return
	 */
	public static boolean isMatchableFrom(Class cls, Class type) {
		if (type == null || type == Object.class || cls.isAssignableFrom(type)) {
			return true;
		}

		if (cls == byte.class) {
			return type == Byte.class;

		} else if (cls == Byte.class) {
			return type == byte.class;

		} else if (cls == short.class) {
			return type == Short.class;

		} else if (cls == Short.class) {
			return type == short.class;

		} else if (cls == int.class) {
			return type == Integer.class;

		} else if (cls == Integer.class) {
			return type == int.class;

		} else if (cls == float.class) {
			return type == Float.class;

		} else if (cls == Float.class) {
			return type == float.class;

		} else if (cls == double.class) {
			return type == Double.class;

		} else if (cls == Double.class) {
			return type == double.class;

		} else if (cls == boolean.class) {
			return type == Boolean.class;

		} else if (cls == Boolean.class) {
			return type == boolean.class;

		} else if (cls == char.class) {
			return type == Character.class;

		} else if (cls == Character.class) {
			return type == char.class;

		} else if (cls == long.class) {
			return type == Long.class;

		} else if (cls == Long.class) {
			return type == long.class;

		} else if (cls.isArray() && type.isArray()) {
			cls.getComponentType().isAssignableFrom(type.getComponentType());
		}

		return false;
	}

	/**
	 * @param cls
	 * @param types
	 * @return
	 */
	public static Class getMatchableFrom(Class cls, Class[] types) {
		for (Class type : types) {
			if (isMatchableFrom(cls, type)) {
				return type;
			}
		}

		return null;
	}

	/**
	 * @param classes
	 * @param types
	 * @return
	 */
	public static boolean isMatchableFrom(Class[] classes, Class[] types) {
		int length = classes.length;
		if (length == types.length) {
			for (int i = 0; i < length; i++) {
				if (!isMatchableFrom(classes[i], types[i])) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	/** NUMBER_CLASSES */
	public static final Class<?>[] NUMBER_CLASSES = new Class<?>[] { int.class, long.class, float.class, double.class };

	/**
	 * @param cls
	 * @return
	 */
	public static Class<?> getMatchNumberClass(Class cls) {
		return getMatchableFrom(cls, NUMBER_CLASSES);
	}

	/**
	 * @param args
	 * @return
	 */
	public static Class[] parameterTypes(Object... args) {
		int length = args.length;
		Class[] parameterTypes = new Class[length];
		Object arg;
		for (int i = 0; i < length; i++) {
			arg = args[i];
			parameterTypes[i] = arg == null ? null : args[i].getClass();
		}

		return parameterTypes;
	}

	/**
	 * @param type
	 * @return
	 */
	public static Class rawClass(Type type) {
		if (type instanceof Class) {
			return (Class) type;

		} else if (type instanceof ParameterizedType) {
			return (Class) ((ParameterizedType) type).getRawType();

		} else if (type instanceof GenericArrayType) {
			try {
				return Array.newInstance(rawClass(((GenericArrayType) type).getGenericComponentType()), 0).getClass();

			} catch (Exception e) {
			}
		}

		return Object.class;
	}

	/**
	 * @param type
	 * @return
	 */
	public static Type[] typeArguments(Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments();
		}

		return null;
	}

	/**
	 * @param types
	 * @return
	 */
	public static Class[] rawClasses(Type[] types) {
		int length = types.length;
		Class[] classes = new Class[length];
		for (int i = 0; i < length; i++) {
			classes[i] = rawClass(types[i]);
		}

		return classes;
	}

	/**
	 * @param cls
	 * @return
	 */
	public static Class componentClass(Class<?> cls) {
		if (cls.isArray()) {
			return cls.getComponentType();
		}

		Class superClass = componentClass(cls.getGenericSuperclass());
		return superClass == cls.getSuperclass() ? cls : superClass;
	}

	/**
	 * @param cls
	 * @return
	 */
	public static Class[] componentClasses(Class<?> cls) {
		if (cls.isArray()) {
			return new Class[] { cls.getComponentType() };
		}

		Class[] superClasses = componentClasses(cls.getGenericSuperclass());
		if (superClasses.length == 1 && superClasses[0] == cls.getSuperclass()) {
			superClasses[0] = cls;
		}

		return superClasses;
	}

	/**
	 * @param type
	 * @return
	 */
	public static Class componentClass(Type type) {
		return argumentClass(type, false);
	}

	/**
	 * @param type
	 * @return
	 */
	public static Class[] componentClasses(Type type) {
		return argumentClasses(type, false);
	}

	/**
	 * @param cls
	 * @return
	 */
	public static Class argumentClass(Class<?> cls) {
		if (cls.isArray()) {
			return cls.getComponentType();
		}

		Class superClass = argumentClass(cls.getGenericSuperclass(), true);
		return superClass == cls.getSuperclass() ? cls : superClass;
	}

	/**
	 * @param cls
	 * @return
	 */
	public static Class[] argumentClasses(Class<?> cls) {
		if (cls.isArray()) {
			return new Class[] { cls.getComponentType() };
		}

		Class[] superClasses = argumentClasses(cls.getGenericSuperclass(), true);
		if (superClasses.length == 1 && superClasses[0] == cls.getSuperclass()) {
			superClasses[0] = cls;
		}

		return superClasses;
	}

	/**
	 * @param cls
	 * @return
	 */
	public static boolean isArgumentClass(Class cls) {
		return cls != null && (cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls));
	}

	/**
	 * @param type
	 * @return
	 */
	public static Class argumentClass(Type type, boolean force) {
		Type componentType = type;
		while (type != null) {
			Type[] types = typeArguments(type);
			if (types == null || types.length <= 0) {
				Class cls = rawClass(type);
				for (Type interfaceType : cls.getGenericInterfaces()) {
					types = typeArguments(interfaceType);
					if (types != null && types.length > 0) {
						break;
					}
				}

				if (types == null || types.length <= 0) {
					if (!(force || isArgumentClass(cls))) {
						break;
					}

					if (cls.isArray()) {
						return cls.getComponentType();

					} else {
						type = cls.getGenericSuperclass();
						continue;
					}
				}
			}

			return rawClass(types[0]);
		}

		return rawClass(componentType);
	}

	/**
	 * @param type
	 * @param force
	 * @return
	 */
	public static Class[] argumentClasses(Type type, boolean force) {
		Type componentType = type;
		while (type != null) {
			Type[] types = typeArguments(type);
			if (types == null || types.length <= 0) {
				Class cls = rawClass(type);
				for (Type interfaceType : cls.getGenericInterfaces()) {
					types = typeArguments(interfaceType);
					if (types != null && types.length > 0) {
						break;
					}
				}

				if (types == null || types.length <= 0) {
					if (!(force || isArgumentClass(cls))) {
						break;
					}

					if (cls.isArray()) {
						return new Class[] { cls.getComponentType() };

					} else {
						type = cls.getGenericSuperclass();
						continue;
					}
				}
			}

			return rawClasses(types);
		}

		return new Class[] { rawClass(componentType) };
	}

	/**
	 * @param type
	 * @param force
	 * @return
	 */
	public static Type[] argumentTypes(Type type, boolean force) {
		while (type != null) {
			Type[] types = typeArguments(type);
			if (types == null || types.length <= 0) {
				Class cls = rawClass(type);
				for (Type interfaceType : cls.getGenericInterfaces()) {
					types = typeArguments(interfaceType);
					if (types != null && types.length > 0) {
						break;
					}
				}

				if (types == null || types.length <= 0) {
					if (!(force || isArgumentClass(cls))) {
						break;
					}

					if (cls.isArray()) {
						return new Class[] { cls.getComponentType() };

					} else {
						type = cls.getGenericSuperclass();
						continue;
					}
				}
			}

			return types;
		}

		return null;
	}

	/**
	 * @param cls
	 * @param typeVariable
	 * @return
	 */
	public static Type type(Class cls, TypeVariable typeVariable) {
		Class root = cls;
		Type type = null;
		GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
		boolean impl = genericDeclaration instanceof Class && !((Class) genericDeclaration).isInterface();
		Class superCls = cls.getSuperclass();
		while (cls != null) {
			if (impl) {
				if (superCls == genericDeclaration) {
					type = cls.getGenericSuperclass();
				}

			} else {
				int i = 0;
				for (Class iCls : cls.getInterfaces()) {
					if (iCls == genericDeclaration) {
						type = cls.getGenericInterfaces()[i];
					}

					i++;
				}
			}

			if (type != null) {
				if (type instanceof ParameterizedType) {
					Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
					int i = 0;
					int len = typeArguments.length;
					String name = typeVariable.getName();
					type = null;
					for (TypeVariable var : genericDeclaration.getTypeParameters()) {
						if (name.equals(var.getName())) {
							type = typeArguments[i];
							if (type instanceof TypeVariable) {
								return type(root, (TypeVariable) type);
							}
						}

						if (i++ >= len) {
							break;
						}
					}

				} else {
					type = null;
				}

				break;
			}

			cls = superCls;
			superCls = cls.getSuperclass();
		}

		return type;
	}

	/**
	 * @param cls
	 * @param type
	 * @return
	 */
	public static int similar(Class cls, Class type) {
		int similar = -1;
		while (cls != null) {
			if (similar < 0) {
				if (type == null || cls == type) {
					similar = 0;

				} else {
					for (Class iCls : cls.getInterfaces()) {
						if (iCls == type) {
							similar = 0;
							break;
						}
					}
				}

			} else {
				similar++;
			}

			cls = cls.getSuperclass();
		}

		return similar;
	}

	/**
	 * @param classes
	 * @param types
	 * @return
	 */
	public static int similar(Class[] classes, Class[] types) {
		int similar = -1;
		int length = classes.length;
		if (length == types.length) {
			for (int i = 0; i < length; i++) {
				similar += similar(classes[i], types[i]);
			}
		}

		return similar;
	}

	/**
	 * @param className
	 * @return
	 */
	public static Class forName(String className) {
		return forName(className, null);
	}

	/**
	 * @param className
	 * @param defaultValue
	 * @return
	 */
	public static Class forName(String className, Class defaultValue) {
		try {
			return Class.forName(className);

		} catch (ClassNotFoundException e) {
		}

		return defaultValue;
	}

	/**
	 * @param cls
	 * @param annotationType
	 * @return
	 */
	public static <T extends Annotation> T fetchAnnotation(Class<?> cls, Class<T> annotationType) {
		while (cls != null) {
			T annotation = cls.getAnnotation(annotationType);
			if (annotation != null) {
				return annotation;
			}

			for (Class c : cls.getInterfaces()) {
				annotation = fetchAnnotation(c, annotationType);
				if (annotation != null) {
					return annotation;
				}
			}

			cls = cls.getSuperclass();
		}

		return null;
	}

	/**
	 * @param cls
	 * @param annotationType
	 * @return
	 */
	public static <T extends Annotation> T getAnnotation(AnnotatedElement cls, Class<T> annotationType) {
		T annotation = cls.getAnnotation(annotationType);
		if (annotation == null && (!(cls instanceof Class) || !Annotation.class.isAssignableFrom((Class) cls))) {
			Annotation[] annotations = cls.getAnnotations();
			if (annotations != null) {
				for (Annotation obj : annotations) {
					annotation = getAnnotation(obj.annotationType(), annotationType);
					if (annotation != null) {
						break;
					}
				}
			}
		}

		return annotation;
	}

	/**
	 * @param cls
	 * @return
	 */
	public static <T> T newInstance(Class<T> cls) {
		return newInstance(null, cls);
	}

	/**
	 * @param defaultValue
	 * @param cls
	 * @return
	 */
	public static <T> T newInstance(T defaultValue, Class<T> cls) {
		try {
			return cls.newInstance();

		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		return defaultValue;
	}

	/**
	 * @param cls
	 * @param initargs
	 * @return
	 */
	public static <T> T newInstance(Class<T> cls, Object... initargs) {
		return newInstance(cls, null, initargs);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param initargs
	 * @return
	 */
	public static <T> T newInstance(Class<T> cls, T defaultValue, Object... initargs) {
		if (initargs.length == 0) {
			return newInstance(defaultValue, cls);
		}

		return newInstance(cls, defaultValue, KernelClass.parameterTypes(initargs), true, initargs);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param parameterTypes
	 * @param initargs
	 * @return
	 */
	public static <T> T newInstance(Class<T> cls, T defaultValue, Class[] parameterTypes, Object... initargs) {
		return newInstance(cls, defaultValue, parameterTypes, false, initargs);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param parameterTypes
	 * @param assignable
	 * @param initargs
	 * @return
	 */
	public static <T> T newInstance(Class<T> cls, T defaultValue, Class[] parameterTypes, boolean assignable, Object... initargs) {
		return declaredNew(cls, defaultValue, false, assignable, parameterTypes, initargs);
	}

	/**
	 * @param cls
	 * @param initargs
	 * @return
	 */
	public static <T> T declaredNew(Class<T> cls, Object... initargs) {
		return declaredNew(cls, null, KernelClass.parameterTypes(initargs), true, initargs);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param initargs
	 * @return
	 */
	public static <T> T declaredNew(Class<T> cls, T defaultValue, Object... initargs) {
		return declaredNew(cls, defaultValue, KernelClass.parameterTypes(initargs), true, initargs);
	}

	/**
	 * @param cls
	 * @param parameterTypes
	 * @param initargs
	 * @return
	 */
	public static <T> T declaredNew(Class<T> cls, Class[] parameterTypes, Object... initargs) {
		return declaredNew(cls, null, parameterTypes, initargs);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param parameterTypes
	 * @param initargs
	 * @return
	 */
	public static <T> T declaredNew(Class<T> cls, T defaultValue, Class[] parameterTypes, Object... initargs) {
		return declaredNew(cls, defaultValue, parameterTypes, false, initargs);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param parameterTypes
	 * @param assignable
	 * @param initargs
	 * @return
	 */
	public static <T> T declaredNew(Class<T> cls, T defaultValue, Class[] parameterTypes, boolean assignable, Object... initargs) {
		return declaredNew(cls, defaultValue, true, assignable, parameterTypes, initargs);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param declared
	 * @param assignable
	 * @param parameterTypes
	 * @param initargs
	 * @return
	 */
	public static <T> T declaredNew(Class<T> cls, T defaultValue, boolean declared, boolean assignable, Class[] parameterTypes, Object... initargs) {
		Constructor<T> constructor = KernelReflect.assignableConstructor(cls, declared, assignable, parameterTypes);
		return KernelReflect.newInstance(constructor, initargs);
	}

	/** Class_Map_Instance */
	static final Map<Class, Object> Class_Map_Instance = new HashMap<Class, Object>();

	/**
	 * @param cls
	 * @return
	 */
	public static <T> T getInstance(Class<T> cls) {
		T instance = (T) Class_Map_Instance.get(cls);
		if (instance == null) {
			synchronized (cls) {
				instance = (T) Class_Map_Instance.get(cls);
				if (instance == null) {
					instance = newInstance(cls);
					Class_Map_Instance.put(cls, instance);
				}
			}
		}

		return instance;
	}

	/**
	 * @param cls
	 * @param initargs
	 * @return
	 */
	public static <T> T getInstance(Class<T> cls, Object... initargs) {
		T instance = (T) Class_Map_Instance.get(cls);
		if (instance == null) {
			synchronized (cls) {
				instance = (T) Class_Map_Instance.get(cls);
				if (instance == null) {
					instance = newInstance(cls, initargs);
					Class_Map_Instance.put(cls, instance);
				}
			}
		}

		return instance;
	}

	/**
	 * @param cls
	 * @param name
	 * @return
	 */
	public static Object declaredGet(Class cls, String name) {
		Field field = KernelReflect.declaredField(cls.getClass(), name);
		return KernelReflect.get(cls, field);
	}

	/**
	 * @param cls
	 * @param name
	 * @param value
	 * @return
	 */
	public static boolean declaredSet(Class cls, String name, Object value) {
		Field field = KernelReflect.declaredField(cls.getClass(), name);
		return KernelReflect.set(cls, field, value);
	}

	/**
	 * @param cls
	 * @param name
	 * @param args
	 * @return
	 */
	public static Object declaredSend(Class cls, String name, Object... args) {
		Method method = KernelReflect.assignableMethod(cls, name, 0, true, parameterTypes(args));
		if (method != null) {
			return KernelReflect.invoke(cls, method, args);
		}

		return null;
	}

	/**
	 * @param field
	 * @return
	 */
	public static Method setter(Field field) {
		return setter(field.getDeclaringClass(), field);
	}

	/**
	 * @param cls
	 * @param field
	 * @return
	 */
	public static Method setter(Class cls, Field field) {
		return setter(cls, field.getName(), field.getType());
	}

	/**
	 * @param cls
	 * @param field
	 * @param fieldType
	 * @return
	 */
	public static Method setter(Class cls, String field, Class fieldType) {
		return declaredSetter(cls, field, fieldType, false);
	}

	/**
	 * @param cls
	 * @param field
	 * @return
	 */
	public static Method declaredSetter(Class cls, Field field) {
		return declaredSetter(cls, field.getName(), field.getType(), true);
	}

	/**
	 * @param cls
	 * @param field
	 * @param fieldType
	 * @param declared
	 * @return
	 */
	public static Method declaredSetter(Class cls, String field, Class fieldType, boolean declared) {
		String name = "set" + KernelString.capitalize(field);
		return KernelReflect.assignableMethod(cls, name, 0, declared, fieldType == null ? true : false, true, fieldType);
	}

	/**
	 * @param field
	 * @return
	 */
	public static Method getter(Field field) {
		return getter(field.getDeclaringClass(), field);
	}

	/**
	 * @param cls
	 * @param field
	 * @return
	 */
	public static Method getter(Class cls, Field field) {
		return getter(cls, field.getName(), field.getType());
	}

	/**
	 * @param cls
	 * @param field
	 * @param fieldType
	 * @return
	 */
	public static Method getter(Class cls, String field, Class fieldType) {
		return declaredGetter(cls, field, fieldType, false);
	}

	/**
	 * @param cls
	 * @param field
	 * @return
	 */
	public static Method getter(Class cls, String field) {
		return declaredGetter(cls, field, null, false);
	}

	/**
	 * @param cls
	 * @param field
	 * @return
	 */
	public static Method declaredGetter(Class cls, Field field) {
		return declaredGetter(cls, field.getName(), field.getType(), true);
	}

	/**
	 * @param cls
	 * @param field
	 * @param fieldType
	 * @param declared
	 * @return
	 */
	public static Method declaredGetter(Class cls, String field, Class fieldType, boolean declared) {
		field = KernelString.capitalize(field);
		boolean is = fieldType != null && (fieldType == boolean.class || fieldType == Boolean.class);
		Method method = KernelReflect.assignableMethod(cls, fieldType, (is ? "is" : "get") + field, 0, declared, true, true);
		if (method == null || (fieldType != null && !fieldType.isAssignableFrom(method.getReturnType()))) {
			if (fieldType == null) {
				method = KernelReflect.assignableMethod(cls, fieldType, "is" + field, 0, declared, true, true);

			} else {
				if (is) {
					method = KernelReflect.assignableMethod(cls, fieldType, "get" + field, 0, declared, true, true);
				}
			}

			if (method != null && fieldType != null && !fieldType.isAssignableFrom(method.getReturnType())) {
				method = null;
			}
		}

		return method;
	}

	/**
	 * @param cls
	 * @param obj
	 * @return
	 */
	public static <T> T instanceOf(Class<T> cls, Object obj) {
		return instanceOf(cls, null, obj);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param obj
	 * @return
	 */
	public static <T> T instanceOf(Class<T> cls, T defaultValue, Object obj) {
		T value = newInstance(cls, obj);
		if (value == null) {
			value = valueOf(cls, obj);
		}

		if (value == null) {
			return defaultValue;

		} else {
			return value;
		}
	}

	/**
	 * @param cls
	 * @param obj
	 * @return
	 */
	public static <T> T valueOf(Class<T> cls, Object obj) {
		return valueOf(cls, null, obj);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param obj
	 * @return
	 */
	public static <T> T valueOf(Class<T> cls, T defaultValue, Object obj) {
		Method method = KernelReflect.assignableMethod(cls, "valueOf", obj.getClass());
		if (method != null && cls.isAssignableFrom(method.getReturnType())) {
			return (T) KernelReflect.invoke(cls, method, obj);
		}

		return defaultValue;
	}

	/**
	 * @param cls
	 * @param toClass
	 * @return
	 */
	public static <T> Class<? extends T> cast(Class cls, Class<T> toClass) {
		return cast(cls, null, toClass);
	}

	/**
	 * @param cls
	 * @param defaultValue
	 * @param toClass
	 * @return
	 */
	public static <T> Class<? extends T> cast(Class cls, Class<? extends T> defaultValue, Class<T> toClass) {
		if (cls != null && toClass.isAssignableFrom(cls)) {
			return cls;
		}

		return defaultValue;
	}

	/**
	 * @param cls
	 * @param callback
	 */
	public static void doWithSuperClass(Class cls, CallbackBreak<Class<?>> callback) {
		try {
			while (!(cls == null || cls == Object.class)) {
				callback.doWith(cls);
				cls = cls.getSuperclass();
			}

		} catch (BreakException e) {
			// TODO: handle exception
		}
	}

	/**
	 * @param cls
	 * @param callback
	 */
	public static void doWithAncestClass(Class cls, CallbackBreak<Class<?>> callback) {
		try {
			while (!(cls == null || cls == Object.class)) {
				callback.doWith(cls);
				for (Class c : cls.getInterfaces()) {
					callback.doWith(c);
				}

				cls = cls.getSuperclass();
			}

		} catch (BreakException e) {
			// TODO: handle exception
		}
	}
}
