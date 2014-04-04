/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-14 上午10:43:16
 */
package com.absir.appserv.developer;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.absir.appserv.feature.IDeveloperJsp;
import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;

/**
 * @author absir
 * 
 */
@Base(order = -1)
@Bean
public class DeveloperJsp extends DeveloperService implements IDeveloperJsp {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.IDeveloperJsp#generate(java.lang.String,
	 * java.lang.String, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.ServletResponse)
	 */
	@Override
	public void generate(String filepath, String includePath, HttpServletRequest request, ServletResponse response) {
		// TODO Auto-generated method stub
		DeveloperUtils.generate(filepath, includePath, request, response);
	}

}
