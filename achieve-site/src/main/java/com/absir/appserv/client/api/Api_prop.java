/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午5:23:28
 */
package com.absir.appserv.client.api;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.PropService;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.context.core.ContextMap;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.value.Attribute;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_prop extends PlayerServer {

	@JaLang("购买道具")
	public int buy(@Attribute PlayerContext playerContext, XPropDefine propDefine) {
		return buy(playerContext, propDefine, 1);
	}

	@JaLang("购买道具")
	public int buy(@Attribute PlayerContext playerContext, XPropDefine propDefine, int size) {
		return buy(playerContext, propDefine, propDefine.getPrice() <= 0, size);
	}

	@JaLang("购买道具")
	public int buy(@Attribute PlayerContext playerContext, XPropDefine propDefine, boolean diamond, int size) {
		if (size <= 0) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		return PropService.ME.buyProp(playerContext, propDefine, diamond, size);
	}

	@JaLang("出售道具")
	public Object sell(@Attribute PlayerContext playerContext, XPropDefine propDefine, int size) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", PropService.ME.sellProp(playerContext, propDefine, size));
		return playerMap.comparedMap();
	}

	@JaLang("出售道具")
	public Object sell(@Attribute PlayerContext playerContext, @Bodys int[] ids) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		for (int id : ids) {
			XPropDefine propDefine = PlayerContext.PROP_DEFINE_XLS_DAO.get(id);
			if (propDefine != null) {
				PropService.ME.sellProp(playerContext, propDefine, 0);
			}
		}

		return playerMap.comparedMap();
	}

	@JaLang("开启技能")
	public int open(@Attribute PlayerContext playerContext, int cardId) {
		JCard card = playerContext.getCard(cardId);
		PropService.ME.openCard(playerContext, card);
		return card.getSkillm();
	}

	@JaLang("锁定技能")
	public boolean lock(@Attribute PlayerContext playerContext, int cardId, int index) {
		JCard card = playerContext.getCard(cardId);
		PropService.ME.lockCard(playerContext, card, index);
		return card.getSkills().get(index).isLocked();
	}

	@JaLang("卡牌道具")
	public Object card(@Attribute PlayerContext playerContext, XPropDefine propDefine, int cardId) throws Throwable {
		JCard card = playerContext.getCard(cardId);
		PropService.ME.propCard(playerContext, propDefine, playerContext.getCard(cardId));
		return card;
	}

	@JaLang("技能道具")
	public Object skill(@Attribute PlayerContext playerContext, XPropDefine propDefine, int cardId, int index) throws Throwable {
		JCard card = playerContext.getCard(cardId);
		PropService.ME.propSkill(playerContext, propDefine, card, index);
		return card.getSkills();
	}
}
