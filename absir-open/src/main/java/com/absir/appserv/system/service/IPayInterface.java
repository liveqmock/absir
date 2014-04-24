/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 上午10:03:48
 */
package com.absir.appserv.system.service;

import com.absir.appserv.system.bean.JPayTrade;

/**
 * @author absir
 * 
 */
public interface IPayInterface {

	/**
	 * @param payTrade
	 * @return
	 */
	public boolean validator(JPayTrade payTrade);

}
