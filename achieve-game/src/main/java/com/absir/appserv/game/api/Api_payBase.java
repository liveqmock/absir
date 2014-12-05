/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午2:12:46
 */
package com.absir.appserv.game.api;

import java.io.IOException;

import com.absir.appserv.game.bean.JbDiamond;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.service.PayService;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.appserv.system.service.utils.PayUtils;
import com.absir.context.core.ContextMap;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.value.Attribute;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class Api_payBase extends PlayerServer {

	@JaLang("请求订单")
	public JPayTrade buy(Long diamondId, @Attribute JbPlayerContext playerContext) {
		return buy(diamondId, 1, playerContext);
	}

	@JaLang("请求订单")
	public JPayTrade buy(Long diamondId, int count, @Attribute JbPlayerContext playerContext) {
		return PayService.ME.buyDiamond(playerContext.getPlayer(), JbPlayerContext.COMPONENT.getDiamond(diamondId), count);
	}

	@JaLang("收取订单")
	public Object take(String platform, JPayTrade payTrade) {
		return PayUtils.proccess(platform, payTrade);
	}

	@JaLang("获取宝石")
	public int diamond(@Attribute JbPlayerContext playerContext) {
		return playerContext.getPlayer().getDiamond();
	}

	@JaLang("IAP验证")
	public Object iap(Long diamondId, int count, @Bodys String receipt, @Attribute JbPlayerContext playerContext) throws IOException {
		JbDiamond diamond = JbPlayerContext.COMPONENT.getDiamond(diamondId);
		if (diamond == null) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		ContextMap contextMap = new ContextMap(playerContext.getPlayer());
		PayService.ME.butInItunes(diamond, count, receipt, playerContext);
		return contextMap.comparedMap();
	}
}
