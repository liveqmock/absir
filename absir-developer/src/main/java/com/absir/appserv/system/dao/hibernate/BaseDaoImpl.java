/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-27 上午9:55:32
 */
package com.absir.appserv.system.dao.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.dao.BaseDao;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.CallbackBreak;
import com.absir.core.kernel.KernelLang.ObjectTemplate;
import com.absir.core.kernel.KernelObject;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

	/** Base_Class_Map_Dao */
	private static final Map<Class<?>, BaseDaoImpl<?, ?>> Base_Class_Map_Dao = new HashMap<Class<?>, BaseDaoImpl<?, ?>>();

	/**
	 * @param baseClass
	 * @return
	 */
	public static <T> BaseDaoImpl<T, ?> getBaseDaoImpl(Class<T> baseClass) {
		BaseDaoImpl<T, ?> baseDao = (BaseDaoImpl<T, ?>) Base_Class_Map_Dao.get(baseClass);
		if (baseDao == null) {
			synchronized (baseClass) {
				baseDao = (BaseDaoImpl<T, ?>) Base_Class_Map_Dao.get(baseClass);
				if (baseDao == null) {
					final ObjectTemplate<BaseDaoImpl<?, ?>> objectBaseDao = new ObjectTemplate<BaseDaoImpl<?, ?>>();
					KernelClass.doWithSuperClass(baseClass, new CallbackBreak<Class<?>>() {

						@Override
						public void doWith(Class<?> template) throws BreakException {
							// TODO Auto-generated method stub
							BaseDaoImpl<?, ?> baseDao = Base_Class_Map_Dao.get(template);
							if (baseDao != null) {
								objectBaseDao.object = baseDao;
								throw new BreakException();
							}
						}
					});

					if (objectBaseDao.object == null) {
						baseDao = new BaseDaoImpl<T, Serializable>(baseClass);

					} else {
						baseDao = (BaseDaoImpl<T, ?>) KernelObject.clone(objectBaseDao.object);
						baseDao.baseClass = baseClass;
					}

					Base_Class_Map_Dao.put(baseClass, baseDao);
				}
			}
		}

		return baseDao;
	}

	/** baseClass */
	private Class<?> baseClass;

	/**
	 * 
	 */
	public BaseDaoImpl() {
		if (baseClass == null) {
			baseClass = KernelClass.componentClass(getClass().getGenericSuperclass());
		}

		Base_Class_Map_Dao.put(baseClass, this);
	}

	/**
	 * @param baseClass
	 */
	public BaseDaoImpl(Class<?> baseClass) {
		this.baseClass = baseClass;
		Base_Class_Map_Dao.put(baseClass, this);
	}

	/**
	 * @return the baseClass
	 */
	public Class<?> getBaseClass() {
		return baseClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.dao.BaseDao#crud(com.absir.appserv.support.entity
	 * .value.JaCrud.Crud, com.absir.appserv.crud.CrudProperty,
	 * com.absir.appserv.crud.CrudHandler, java.lang.Object)
	 */
	@Override
	public void crud(Crud crud, CrudProperty property, CrudHandler crudHandler, T entity) {
		// TODO Auto-generated method stub
	}
}