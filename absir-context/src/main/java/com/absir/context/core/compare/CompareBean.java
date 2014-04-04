/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午2:53:56
 */
package com.absir.context.core.compare;

/**
 * @author absir
 * 
 */
public class CompareBean extends CompareAbstract<Object, Integer> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.context.value.CompareAbstract#getCompareValue
	 * (java.lang.Object)
	 */
	@Override
	protected Integer getCompareValue(Object value) {
		// TODO Auto-generated method stub
		return value.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.context.value.CompareAbstract#compareValue(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public boolean compareValue(Integer compare, Object value) {
		// TODO Auto-generated method stub
		return compare == value.hashCode();
	}
}
