/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-1 上午9:36:38
 */
package com.absir.appserv.system.bean.proxy;

import java.util.Collection;

/**
 * @author absir
 * 
 */
public interface JiTree<T> {

	public Collection<T> getChildren();
}
