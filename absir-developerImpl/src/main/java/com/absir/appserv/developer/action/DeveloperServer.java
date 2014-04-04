/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-17 下午2:08:23
 */
package com.absir.appserv.developer.action;

import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.orm.transaction.TransactionInterceptor;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.value.Before;
import com.absir.server.value.Mapping;

/**
 * @author absir
 * 
 */
@Mapping("/developer")
public class DeveloperServer {

	/**
	 * 
	 */
	@Before
	public void onAuthentication() {
		if (BeanFactoryUtils.getEnvironment() != Environment.DEVELOP) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}
	}

	/**
	 * @author absir
	 * 
	 */
	public static class Route extends TransactionInterceptor {
		
	}
}
