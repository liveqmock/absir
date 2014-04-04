/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 上午10:03:48
 */
package com.absir.appserv.system.pay;

import com.absir.appserv.system.bean.JTrader;

/**
 * @author absir
 * 
 */
public interface IPayProccessor {

	/**
	 * @param trader
	 * @return
	 */
	public boolean proccess(JTrader trader);
}
