/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月16日 上午11:54:10
 */
package com.absir.appserv.system.service;

import com.absir.appserv.data.value.DataQuery;
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
	 * @param name
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery(value = "SELECT COUNT(o) FROM JFilter o WHERE o.id LIKE ?")
	public abstract int findFilter(String name);

}
