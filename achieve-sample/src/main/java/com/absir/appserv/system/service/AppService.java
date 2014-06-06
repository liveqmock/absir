/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-19 上午10:10:42
 */
package com.absir.appserv.system.service;

import com.absir.appserv.data.value.DataQuery;
import com.absir.appserv.system.bean.JApp;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 * 
 */
@Bean
public abstract class AppService {

	/** ME */
	public static final AppService ME = BeanFactoryUtils.get(AppService.class);

	/**
	 * @param platform
	 * @return
	 */
	@Transaction(readOnly = true)
	@DataQuery("SELECT o FROM JApp o WHERE o.platform = ? ORDER BY o.updateTime DESC")
	public abstract JApp getLastApp(String platform);

}
