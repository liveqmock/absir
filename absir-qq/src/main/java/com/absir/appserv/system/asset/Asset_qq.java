/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午7:54:43
 */
package com.absir.appserv.system.asset;

import javax.servlet.http.HttpServletRequest;

import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.QQService;
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
public class Asset_qq extends AssetServer {

	@Inject
	private QQService duoKuService;

	/**
	 * @param billno
	 * @param amt
	 * @param token
	 * @param appmeta
	 * @param sig
	 * @return
	 */
	@Body
	public String notify(@Param String billno, @Param float amt, final @Param String token, @Param String appmeta, @Param String sig, HttpServletRequest request) {
		final JPayTrade payTrade = BeanService.ME.get(JPayTrade.class, billno);
		if (payTrade != null) {
			if (payTrade.getStatus() != null) {
				// 订单重复通知,且上次通知无异常
				return "ERROR_REPEAT";
			}

			if ((int) Math.floor(amt) != payTrade.getAmount()) {
				// 订单用户信息不匹配
				return "ERROR_USER";
			}

			// 平台信息
			payTrade.setPlatform(QQService.PLAT_FORM_NAME);
			payTrade.setPlatformData(sig);
			// 通知回调
			String sign = QQService.sign(request);
			if (sign != null && sign.equals(sign)) {
				ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						PayUtils.notify(duoKuService, payTrade, JePayStatus.PAYED);
					}
				});
			}

			return "SUCCESS";
		}

		// 其他错误
		return "ERROR_FAIL";
	}
}
