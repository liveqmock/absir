/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午5:23:28
 */
package com.absir.appserv.client.api;

import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.LotService;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.context.core.ContextMap;
import com.absir.server.value.Attribute;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_lot extends PlayerServer {

	@JaLang("获取抽奖池")
	public Object route(@Attribute PlayerContext playerContext) {
		return LotService.ME.getRareMapCard();
	}

	@JaLang("友情抽奖")
	public Object free(@Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", LotService.ME.lotFriendShip(playerContext));
		return playerMap.comparedMap();
	}

	@JaLang("抽一次")
	public Object lot(@Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", LotService.ME.lotDiamond(playerContext));
		return playerMap.comparedMap();
	}

	@JaLang("十连抽")
	public Object lots(@Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", LotService.ME.lotDiamonds(playerContext));
		return playerMap.comparedMap();
	}
}
