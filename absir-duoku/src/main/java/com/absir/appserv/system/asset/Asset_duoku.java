/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午7:54:43
 */
package com.absir.appserv.system.asset;

import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.DuoKuService;
import com.absir.appserv.system.service.utils.PayUtils;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextUtils;
import com.absir.server.value.Body;
import com.absir.server.value.Param;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Asset_duoku extends AssetServer {

	@Inject
	private DuoKuService duoKuService;

	/**
	 * @param orderid
	 * @param amount
	 * @param result
	 * @param cardtype
	 * @param timetamp
	 * @param client_secret
	 * @return
	 */
	@Body
	public String notify(@Param String orderid, @Param float amount, final @Param int result, @Param String cardtype, @Param long timetamp, @Param String client_secret) {
		final JPayTrade payTrade = BeanService.ME.get(JPayTrade.class, orderid);
		if (payTrade != null) {
			if (payTrade.getStatus() != null) {
				// 订单重复通知,且上次通知无异常
				return "ERROR_REPEAT";
			}

			if ((int) Math.floor(amount) != payTrade.getAmount()) {
				// 订单用户信息不匹配
				return "ERROR_USER";
			}

			// 平台信息
			payTrade.setPlatform(DuoKuService.PLAT_FORM_NAME);
			payTrade.setPlatformData(cardtype);
			// 通知回调
			ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					PayUtils.notify(duoKuService, payTrade, result == 1 ? JePayStatus.PAYED : JePayStatus.ERROR);
				}
			});

			return "SUCCESS";
		}

		// 其他错误
		return "ERROR_FAIL";
	}
}
