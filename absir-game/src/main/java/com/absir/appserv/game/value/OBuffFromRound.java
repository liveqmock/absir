/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-6 上午10:02:43
 */
package com.absir.appserv.game.value;

import com.absir.core.kernel.KernelClass;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class OBuffFromRound<T, O extends OObject> extends OBuffRound<O> implements IBuffFrom<T> {

	/** formType */
	private Class<T> formType;

	/**
	 * 
	 */
	public OBuffFromRound() {
		formType = KernelClass.componentClass(getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.IBuffFrom#supportsFrom(java.lang.Object)
	 */
	@Override
	public boolean supportsFrom(Object from) {
		// TODO Auto-generated method stub
		return from == null ? isFromNullable() : formType.isAssignableFrom(formType.getClass());
	}

	/**
	 * @return
	 */
	public boolean isFromNullable() {
		return false;
	}
}
