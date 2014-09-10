/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月9日 下午4:42:51
 */
package com.absir.template;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		 * @param args
		 * @return
		 */
		public Object eval(Object target, Object[] args);
	}

	/** referStacks */
	public Map<String, Integer> referStacks = new HashMap<String, Integer>();

	/** stacks */
	public List<IStack> stacks = new ArrayList<IStack>();

	/**
	 * @param refer
	 * @param stack
	 */
	public void register(String refer, IStack stack) {
		referStacks.put(refer, stacks.size());
		stacks.add(stack);
	}

	/**
	 * @param refer
	 * @return
	 */
	public IStack get(String refer) {
		return stacks.get(referStacks.get(refer));
	}

	/**
	 * 
	 */
	public void initEngine() {
		register("#register", new IStack() {

			@Override
			public Object eval(Object target, Object[] args) {
				// TODO Auto-generated method stub
				register((String) target, (IStack) args[0]);
				return null;
			}
		});
		register("#get", new IStack() {

			@Override
			public Object eval(Object target, Object[] args) {
				// TODO Auto-generated method stub
				return get((String) target);
			}
		});
		register("#map", new IStack() {

			@Override
			public Object eval(Object target, Object[] args) {
				// TODO Auto-generated method stub
				return new HashMap<Object, Object>();
			}
		});
		register("#map.get", new IStack() {

			@Override
			public Object eval(Object target, Object[] args) {
				// TODO Auto-generated method stub
				return ((Map<Object, Object>)target).get(args[0]);
			}
		});
	}

	/**
	 * @param inputStream
	 */
	public void load(InputStream inputStream) {

	}
}
