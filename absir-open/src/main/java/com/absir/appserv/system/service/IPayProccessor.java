/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午3:21:33
 */
package com.absir.appserv.system.service;

import com.absir.appserv.system.bean.JPayTrade;

/**
 * @author absir
 * 
 */
public interface IPayProccessor {

	/**
	 * @param payTrade
	 * @return
	 */
	public Object proccess(JPayTrade payTrade);

}
