/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月9日 下午4:42:51
 */
package com.absir.template;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
public class TemplateEngine {

	/**
	 * @author absir
	 *
	 */
	public interface IStack {

		/**
		 * @param target
		 * @param parameter
		 * @return
		 */
		public Object eval(Object target, Object parameter);
	}

	public abstract class StackArgs implements IStack {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.template.TemplateEngine.IStack#eval(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public Object eval(Object target, Object parameter) {
			// TODO Auto-generated method stub
			return evalArgs(target, (Object[]) parameter);
		}

		public abstract Object evalArgs(Object target, Object[] args);
	}

	/** nameMapStack */
	private Map<String, IStack> nameMapStack = new HashMap<String, IStack>();

	/**
	 * @param name
	 * @return
	 */
	public IStack get(String name) {
		return nameMapStack.get(name);
	}

	/**
	 * @param name
	 * @param stack
	 */
	public IStack set(String name, IStack stack) {
		return nameMapStack.put(name, stack);
	}

	/**
	 * @param name
	 * @param stack
	 */
	public IStack unset(String name) {
		return nameMapStack.remove(name);
	}

	/**
	 * 
	 */
	public void initEngine() {
		set("#get", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return get((String) target);
			}
		});
		set("#set", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return set((String) target, (IStack) parameter);
			}
		});
		set("#unset", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return unset((String) target);
			}
		});
		set("@hash", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return target.hashCode();
			}
		});
		set("@equals", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return target.equals(parameter);
			}
		});
		set("@array", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return new Object[(Integer) parameter];
			}

		});
		set("@array.get", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return ((Object[]) target)[(Integer) parameter];
			}

		});
		set("@array.set", new StackArgs() {

			@Override
			public Object evalArgs(Object target, Object[] args) {
				// TODO Auto-generated method stub
				return ((Object[]) target)[(Integer) args[0]] = args[1];
			}

		});
		set("@map", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return new HashMap<Object, Object>();
			}
		});
		set("@map.get", new IStack() {

			@Override
			public Object eval(Object target, Object parameter) {
				// TODO Auto-generated method stub
				return ((Map<Object, Object>) target).get(parameter);
			}
		});
		set("@map.put", new StackArgs() {

			@Override
			public Object evalArgs(Object target, Object[] args) {
				// TODO Auto-generated method stub
				return ((Map<Object, Object>) target).put(args[0], args[1]);
			}

		});
	}

	/**
	 * @param inputStream
	 */
	public void load(InputStream inputStream) {

	}
}
