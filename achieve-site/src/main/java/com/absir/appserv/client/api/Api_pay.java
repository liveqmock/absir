/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午2:12:46
 */
package com.absir.appserv.client.api;

import java.io.IOException;

import com.absir.appserv.client.bean.JDiamond;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.PayService;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.appserv.system.service.utils.PayUtils;
import com.absir.context.core.ContextMap;
import com.absir.server.value.Attribute;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_pay extends PlayerServer {

	@JaLang("请求订单")
	public JPayTrade buy(JDiamond diamond, @Attribute PlayerContext playerContext) {
		return buy(diamond, 1, playerContext);
	}

	@JaLang("请求订单")
	public JPayTrade buy(JDiamond diamond, int count, @Attribute PlayerContext playerContext) {
		return PayService.ME.buyDiamond(playerContext.getPlayer(), diamond, count);
	}

	@JaLang("收取订单")
	public Object take(String platform, JPayTrade payTrade) {
		return PayUtils.proccess(platform, payTrade);
	}

	@JaLang("获取宝石")
	public int diamond(@Attribute PlayerContext playerContext) {
		return playerContext.getPlayer().getDiamond();
	}

	@JaLang("IAP验证")
	public Object iap(JDiamond diamond, int count, @Bodys String receipt, @Attribute PlayerContext playerContext) throws IOException {
		ContextMap contextMap = new ContextMap(playerContext.getPlayer());
		PayService.ME.butInItunes(diamond, count, receipt, playerContext);
		return contextMap.comparedMap();
	}
}
