/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午7:54:43
 */
package com.absir.appserv.system.asset;

import java.util.HashMap;
import java.util.Map;

import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.helper.HelperJson;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.YiDongService;
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
public class Asset_yidong extends AssetServer {

	@Inject
	private YiDongService yiDongService;

	/**
	 * @param userId
	 * @param key
	 * @param cpId
	 * @param cpServiceId
	 * @param channelId
	 * @param p
	 * @param region
	 * @param Ua
	 * @return
	 */
	@Body
	public int session(@Param String userId, @Param String key, @Param String cpId, @Param String cpServiceId, @Param String channelId, @Param String p, @Param String region, @Param String Ua) {
		return yiDongService.loginUser(userId, key, cpId, cpServiceId, channelId, p, region, Ua);
	}

	/**
	 * @param xmlParams
	 * @return
	 */
	@Body
	public String notify(@Body String xmlParams) {
		Map<String, Object> mapParams = HelperJson.xmlToMap(xmlParams);
		String orderId = (String) mapParams.get("cpparam");
		final JPayTrade payTrade = BeanService.ME.get(JPayTrade.class, orderId);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		while (true) {
			if (payTrade != null) {
				if (payTrade.getStatus() != null) {
					// 订单重复通知,且上次通知无异常
					responseMap.put("hRet", 1);
					responseMap.put("message", "repeat");
					break;
				}

				// 平台信息
				payTrade.setPlatform(YiDongService.PLAT_FORM_NAME);
				final int hRet = (Integer) mapParams.get("hRet");
				// 通知回调
				ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						PayUtils.notify(yiDongService, payTrade, hRet == 0 ? JePayStatus.PAYED : JePayStatus.ERROR);
					}
				});

				responseMap.put("hRet", 0);
				responseMap.put("message", "successful");

			} else {
				responseMap.put("hRet", 1);
				responseMap.put("message", "not found");
			}

			break;
		}

		// 其他错误
		return HelperJson.mapToXml(responseMap, "response");
	}
}
