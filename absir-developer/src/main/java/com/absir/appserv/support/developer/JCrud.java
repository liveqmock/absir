/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-8-8 下午5:17:28
 */
package com.absir.appserv.support.developer;

import java.io.Serializable;

import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelReflect;
import com.absir.core.util.UtilAnnotation;

/**
 * @author absir
 * 
 */
@SuppressWarnings("serial")
public class JCrud implements Serializable {

	/** value */
	protected String value;

	/** value */
	protected Class<?> factory;

	/** parameters */
	protected Object[] parameters;

	/** cruds */
	protected JaCrud.Crud[] cruds;

	/** jaCrud */
	@JaCrud
	private static JaCrud jaCrud = KernelReflect.declaredField(JCrud.class, "jaCrud").getAnnotation(JaCrud.class);

	/**
	 * 
	 */
	public JCrud() {
		this(jaCrud);
	}

	/**
	 * @param crud
	 */
	public JCrud(JaCrud crud) {
		UtilAnnotation.copy(crud, this);
		parameters = new Object[crud.parameters().length];
		KernelArray.copy(crud.parameters(), parameters);
	}

	/**
	 * @return the factory
	 */
	public Class<?> getFactory() {
		return factory;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the cruds
	 */
	public JaCrud.Crud[] getCruds() {
		return cruds;
	}

}
