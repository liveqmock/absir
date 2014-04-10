/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 下午1:48:14
 */
package com.absir.context.core;

/**
 * @author absir
 * 
 */
public abstract class ContextService extends ContextBase {

	/**
	 * @param contextTime
	 */
	public abstract void step(long contextTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.IContext#stepDone(long)
	 */
	@Override
	public final boolean stepDone(long contextTime) {
		step(contextTime);
		return true;
	}
}
