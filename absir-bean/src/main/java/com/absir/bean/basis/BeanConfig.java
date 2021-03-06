/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-10 下午1:43:31
 */
package com.absir.bean.basis;

import java.lang.reflect.Type;

/**
 * @author absir
 * 
 */
public interface BeanConfig {

	/**
	 * @return
	 */
	public String getClassPath();

	/**
	 * @return
	 */
	public String getResourcePath();

	/**
	 * @return
	 */
	public Environment getEnvironment();

	/**
	 * @param environment
	 */
	public void setEnvironment(Environment environment);

	/**
	 * @param name
	 * @return
	 */
	public Object getValue(String name);

	/**
	 * @param name
	 * @param obj
	 */
	public void setValue(String name, Object obj);

	/**
	 * @param expression
	 * @return
	 */
	public String getExpression(String expression);

	/**
	 * @param expression
	 * @param beanName
	 * @param toClass
	 * @return
	 */
	public <T> T getExpressionObject(String expression, String beanName, Class<T> toClass);

	/**
	 * @param expression
	 * @param beanName
	 * @param toType
	 * @return
	 */
	public Object getExpressionObject(String expression, String beanName, Type toType);

	/**
	 * @param expression
	 * @param beanName
	 * @param toClass
	 * @return
	 */
	public <T> T getExpressionValue(String expression, String beanName, Class<T> toClass);

	/**
	 * @param expression
	 * @param beanName
	 * @param toClass
	 * @return
	 */
	public <T> T getExpressionDefaultValue(String expression, String beanName, Class<T> toClass);

	/**
	 * @param expression
	 * @param beanName
	 * @param toType
	 * @return
	 */
	public Object getExpressionDefaultValue(String expression, String beanName, Type toType);

	/**
	 * @param filename
	 * @return
	 */
	public String getClassPath(String filename);

	/**
	 * @param filename
	 * @return
	 */
	public String getResourcePath(String filename);

	/**
	 * @param filename
	 * @param nullPrefix
	 * @return
	 */
	public String getResourcePath(String filename, String nullPrefix);
}
