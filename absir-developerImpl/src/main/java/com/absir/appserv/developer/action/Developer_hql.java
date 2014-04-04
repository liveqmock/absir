/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.developer.action;

import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.value.Body;
import com.absir.server.value.Param;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Developer_hql extends DeveloperServer {

	/**
	 * @param hql
	 * @return
	 */
	@Body
	@Transaction
	public Object route(@Param String hql) {
		return SessionFactoryUtils.get().getSessionFactory().getCurrentSession().createQuery(hql).executeUpdate();
	}

	/**
	 * @param hql
	 */
	@Body
	@Transaction(readOnly = true)
	public Object list(@Param String hql) {
		return SessionFactoryUtils.get().getSessionFactory().getCurrentSession().createQuery(hql).list();
	}
}