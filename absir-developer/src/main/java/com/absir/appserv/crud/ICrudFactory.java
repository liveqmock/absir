/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-10 上午11:28:08
 */
package com.absir.appserv.crud;

import com.absir.appserv.support.developer.JCrudField;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public interface ICrudFactory {

	/**
	 * @param joEntity
	 * @param crudField
	 * @return
	 */
	public ICrudProcessor getProcessor(JoEntity joEntity, JCrudField crudField);

}
