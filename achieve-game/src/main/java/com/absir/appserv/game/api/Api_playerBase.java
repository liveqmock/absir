/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-9 下午12:19:53
 */
package com.absir.appserv.game.api;

import java.util.Collection;
import java.util.List;

import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.bean.value.IPropDefine;
import com.absir.appserv.game.bean.value.IRewardDefine;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.appserv.game.context.value.IPropEvolute;
import com.absir.appserv.game.service.ArenaService;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.context.core.ContextMap;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.value.Attribute;
import com.absir.server.value.Param;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class Api_playerBase extends PlayerServer {

	@JaLang("玩家信息")
	public JbPlayerContext route(@Attribute JbPlayerContext playerContext) {
		return playerContext;
	}

	@JaLang("全部卡牌")
	public Collection<JbCard> allCards(@Attribute JbPlayerContext playerContext) {
		return playerContext.getAllCards();
	}

	@JaLang("更改签名")
	public void sign(@Param String sign, @Attribute JbPlayerContext playerContext) {
		playerContext.sign(sign);
	}

	@JaLang("更改阵型")
	protected void formation(@Bodys long[] cardIds, @Attribute JbPlayerContext playerContext) {
		playerContext.formation(cardIds);
	}

	@JaLang("更改阵型")
	public void formation(long cardId, int position, @Attribute JbPlayerContext playerContext) {
		playerContext.formation(cardId, position);
	}

	@JaLang("出售卡牌")
	public Object sell(long cardId, @Attribute JbPlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerContext.sell(cardId);
		return playerMap.comparedMap();
	}

	@JaLang("出售卡牌")
	public Object sell(@Bodys long[] cardIds, @Attribute JbPlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerContext.sell(cardIds);
		return playerMap.comparedMap();
	}

	@JaLang("升级卡牌")
	public Object feed(long cardId, @Bodys long[] cardIds, @Attribute JbPlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.feed(cardId, cardIds));
		return playerMap.comparedMap();
	}

	@JaLang("进化卡牌")
	public Object evolute(long cardId, @Bodys long[] cardIds, @Attribute JbPlayerContext playerContext) {
		return evolute(cardId, null, cardIds, playerContext);
	}

	@JaLang("进化卡牌")
	public Object evolute(long cardId, Integer prop, @Bodys long[] cardIds, @Attribute JbPlayerContext playerContext) {
		IPropEvolute propEvolute = null;
		if (prop != null) {
			IPropDefine propDefine = JbPlayerContext.COMPONENT.getPropDefine(prop);
			if (propDefine != null) {
				Object propInvoker = propDefine.getPropInvoker();
				if (!(propInvoker instanceof IPropEvolute)) {
					throw new ServerException(ServerStatus.NO_PARAM);
				}

				playerContext.modifyProp(propDefine, -1, false);
				propEvolute = (IPropEvolute) propInvoker;
			}
		}

		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.evolute(cardId, cardIds, propEvolute));
		return playerMap.comparedMap();
	}

	@JaLang("购买卡牌数")
	public int card(@Attribute JbPlayerContext playerContext) {
		return card(1, playerContext);
	}

	@JaLang("购买卡牌数")
	public int card(int number, @Attribute JbPlayerContext playerContext) {
		return playerContext.card(number);
	}

	@JaLang("购买好友数")
	public int friend(@Attribute JbPlayerContext playerContext) {
		return friend(1, playerContext);
	}

	@JaLang("购买好友数")
	public int friend(int number, @Attribute JbPlayerContext playerContext) {
		return playerContext.friend(number);
	}

	@JaLang("奖励数量")
	public int reward(@Attribute JbPlayerContext playerContext) {
		return playerContext.getPlayerA().getRewardNumber();
	}

	@JaLang("领取奖励")
	public Object reward(long rewardId, @Attribute JbPlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.reward(rewardId));
		return playerMap.comparedMap();
	}

	@JaLang("奖励列表")
	public Object rewards(int pageIndex, @Attribute JbPlayerContext playerContext) {
		return PlayerServiceBase.ME.getPlayerRewards((Long) playerContext.getId(), pageIndex);
	}

	@JaLang("领取奖励")
	public Object oReward(String rewardId, @Attribute JbPlayerContext playerContext) {
		IRewardDefine rewardDefine = JbPlayerContext.COMPONENT.getRewardDefine(rewardId);
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.reward(rewardDefine));
		return playerMap.comparedMap();
	}

	@JaLang("使用道具")
	public Object prop(int propId, @Attribute JbPlayerContext playerContext) {
		IPropDefine propDefine = JbPlayerContext.COMPONENT.getPropDefine(propId);
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.prop(propDefine));
		return playerMap.comparedMap();
	}

	@JaLang("使用道具")
	public Object prop(int propId, long cardId, @Attribute JbPlayerContext playerContext) {
		IPropDefine propDefine = JbPlayerContext.COMPONENT.getPropDefine(propId);
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.prop(propDefine, cardId));
		return playerMap.comparedMap();
	}

	@JaLang("进入任务")
	public Object task(int scene, int pass, int detail, @Attribute JbPlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.task(scene, pass, detail));
		return playerMap.comparedMap();
	}

	/**
	 * 竞技场匹配
	 * 
	 * @author absir
	 *
	 */
	public static class Arenas {

		/** arean */
		public int arean;

		/** players */
		public List<JbPlayer> players;

	}

	@JaLang("竞技场匹配")
	public Arenas arenas(@Attribute JbPlayerContext playerContext) {
		List<JbPlayer> players = playerContext.getArenas();
		Arenas arenas = new Arenas();
		arenas.arean = playerContext.getPlayer().getArena();
		arenas.players = players;
		return arenas;
	}

	@JaLang("竞技场列表")
	public List<JbPlayer> arena(int arena, @Attribute JbPlayerContext playerContext) {
		return ArenaService.ME.getArenaList(arena, JdbcPage.PAGE_SIZE, playerContext.getPlayer().getServerId());
	}

	@JaLang("修改玩家数据")
	public Object modifier(int level, int ep, int money, int diamond, int friendShipNumber, @Attribute JbPlayerContext playerContext) {
		if (!playerContext.getPlayerA().isDebug()) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerContext.modifyLevel(level);
		playerContext.modifyEp(level, true);
		playerContext.modifyMoney(money, true);
		playerContext.modifyDiamond(diamond, true);
		playerContext.modifyFriendShipNumber(friendShipNumber, true);
		return playerMap.comparedMap();
	}
}
