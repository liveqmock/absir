/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月16日 上午11:54:10
 */
package com.absir.appserv.system.service;

import com.absir.appserv.system.dao.BeanDao;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 *
 */
@Bean
public abstract class FilterService {

	/** ME */
	public final static FilterService ME = BeanFactoryUtils.get(FilterService.class);

	/**
	 * @param value
	 * @return
	 */
	@Transaction(readOnly = true)
	public boolean findFilter(String value) {
		return BeanDao.getSession().createQuery("SELECT o.id FROM JFilter o WHERE ? LIKE o.id").setParameter(0, value).iterate().hasNext();
	}
}
