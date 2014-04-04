/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 上午10:04:28
 */
package com.absir.appserv.system.pay.utils;

import com.absir.appserv.system.bean.JTrader;
import com.absir.appserv.system.pay.IPayProccessor;
import com.absir.bean.basis.Configure;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;

/**
 * @author absir
 * 
 */
@Configure
public abstract class PayUtils {

	@Inject(type = InjectType.Selectable)
	private static IPayProccessor[] payProccessors;

	/**
	 * @param trader
	 * @return
	 */
	public static boolean process(JTrader trader) {
		if (payProccessors != null) {
			for (IPayProccessor payProccessor : payProccessors) {
				if (payProccessor.proccess(trader)) {
					return true;
				}
			}
		}

		return false;
	}

}
