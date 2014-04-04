/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 下午5:14:51
 */
package com.absir.appserv.data;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.core.kernel.KernelList;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataQueryDetached {

	/** sql */
	private String sql;

	/** list */
	private boolean single;

	/**
	 * @param sql
	 * @param nativeSql
	 */
	public DataQueryDetached(String sql, boolean nativeSql, String name, Class<?> returnType) {
		this.sql = sql;
		single = !Collection.class.isAssignableFrom(returnType);
	}

	/**
	 * @param parameters
	 * @return
	 */
	public Object invoke(Object[] parameters) {
		Session session = BeanDao.getSession();
		Query query = session.createQuery(sql);
		QueryDaoUtils.setParameterArray(query, parameters);
		List list = query.list();
		return single ? KernelList.get(list, 0) : list;
	}
}
