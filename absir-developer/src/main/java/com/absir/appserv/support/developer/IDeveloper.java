/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-8-8 下午5:10:11
 */
package com.absir.appserv.support.developer;

import java.io.IOException;
import java.util.List;

import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Inject;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
@Inject
public interface IDeveloper {

	/** ME */
	public static final IDeveloper ME = BeanFactoryUtils.get(IDeveloper.class);

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

	/**
	 * @param filepath
	 * @param includePath
	 * @param request
	 * @param renders
	 * @throws IOException
	 */
	public void generate(String filepath, String includePath, Object... renders) throws IOException;
}
