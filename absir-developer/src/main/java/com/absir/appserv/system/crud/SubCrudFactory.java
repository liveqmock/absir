/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-16 上午11:01:29
 */
package com.absir.appserv.system.crud;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaSubField;
import com.absir.core.kernel.KernelReflect;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilAccessor;
import com.absir.core.util.UtilAccessor.Accessor;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class SubCrudFactory implements ICrudFactory {

	/** SUB_PROCESSOR */
	private static final ICrudProcessor SUB_PROCESSOR = new ICrudProcessor() {

		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
			Object property = crudProperty.get(entity);
			if (property == null) {
				crudProperty.set(entity, 0);
				return;
			}

			if (!(property instanceof Integer)) {
				return;
			}

			Object[] parameters = crudProperty.getjCrud().getParameters();
			if (parameters[0] == null) {
				return;
			}

			if (parameters[0] instanceof String) {
				Class<?> entityClass = crudProperty.getCrudEntity().getJoEntity().getEntityClass();
				parameters[0] = UtilAccessor.getAccessorObj(entity, (String) parameters[0], entityClass == null ? null : entityClass.getName());
			}

			try {
				Collection<?> subtable = (Collection<?>) ((Accessor) parameters[0]).get(entity);
				if ((Integer) property >= subtable.size()) {
					crudProperty.set(entity, 0);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			crudProperty.set(entity, new Date());
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudFactory#getProcessor(com.absir.appserv.support
	 * .entity.value.JoEntity, com.absir.appserv.support.developer.JCrudField)
	 */
	@Override
	public ICrudProcessor getProcessor(JoEntity joEntity, JCrudField crudField) {
		// TODO Auto-generated method stub
		if (crudField.getjCrud().getParameters().length < 0) {
			return null;
		}

		if (KernelString.isEmpty((String) crudField.getjCrud().getParameters()[0])) {
			Field field = KernelReflect.declaredField(joEntity.getEntityClass(), crudField.getName());
			if (field == null) {
				return null;
			}

			JaSubField jaSubField = field.getAnnotation(JaSubField.class);
			if (jaSubField == null) {
				return null;
			}

			crudField.getjCrud().getParameters()[0] = jaSubField.value();
		}

		return SUB_PROCESSOR;
	}
}
