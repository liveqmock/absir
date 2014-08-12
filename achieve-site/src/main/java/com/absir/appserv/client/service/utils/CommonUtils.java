/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-20 上午11:12:08
 */
package com.absir.appserv.client.service.utils;

import com.absir.appserv.jdbc.JdbcPage;

/**
 * @author absir
 * 
 */
public class CommonUtils {

	/**
	 * 全局分页参数
	 * 
	 * @param pageIndex
	 * @return
	 */
	public static JdbcPage getJdbcPage(int pageIndex) {
		JdbcPage jdbcPage = new JdbcPage();
		jdbcPage.setPageSize(10);
		jdbcPage.setPageIndex(pageIndex);
		return jdbcPage;
	}

}
