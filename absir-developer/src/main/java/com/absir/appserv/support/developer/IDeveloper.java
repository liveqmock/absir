/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-8-8 下午5:10:11
 */
package com.absir.appserv.support.developer;

import java.util.List;

import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public interface IDeveloper {

	/**
	 * @param joEntity
	 * @return
	 */
	public IModel getModelEntity(JoEntity joEntity);

	/**
	 * @param joEntity
	 * @return
	 */
	public List<JCrudField> getCrudFields(JoEntity joEntity);

	/**
	 * @param joEntity
	 * @param group
	 * @return
	 */
	public String[] getCrudFields(JoEntity joEntity, String group);
}
