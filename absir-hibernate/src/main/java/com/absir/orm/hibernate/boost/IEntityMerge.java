/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-8 下午2:43:55
 */
package com.absir.orm.hibernate.boost;

/**
 * @author absir
 * 
 */
public interface IEntityMerge<T> {

	// 0 persist 1 merge 2 delete
	public void merge(String entityName, T entity, int type);

}
