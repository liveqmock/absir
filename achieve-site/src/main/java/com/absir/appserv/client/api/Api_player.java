/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-9 下午12:19:53
 */
package com.absir.appserv.client.api;

import java.util.Collection;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.client.configure.xls.XRewardDefine;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.PlayerService;
import com.absir.appserv.client.context.value.IPropEvolute;
import com.absir.appserv.client.service.PropService;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.context.core.ContextMap;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.value.Attribute;
import com.absir.server.value.Param;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_player extends PlayerServer {

	@JaLang("玩家信息")
	public PlayerContext route(@Attribute PlayerContext playerContext) {
		return playerContext;
	}

	@JaLang("全部卡牌")
	public Collection<JCard> allCards(@Attribute PlayerContext playerContext) {
		return playerContext.getAllCards();
	}

	@JaLang("更改签名")
	public void sign(@Param String sign, @Attribute PlayerContext playerContext) {
		playerContext.sign(sign);
	}

	@JaLang("更改阵型")
	protected void formation(@Bodys long[] cardIds, @Attribute PlayerContext playerContext) {
		playerContext.formation(cardIds);
	}

	@JaLang("更改阵型")
	public void formation(long cardId, int position, @Attribute PlayerContext playerContext) {
		playerContext.formation(cardId, position);
	}

	@JaLang("出售卡牌")
	public Object sell(long cardId, @Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerContext.sell(cardId);
		return playerMap.comparedMap();
	}

	@JaLang("出售卡牌")
	public Object sell(@Bodys long[] cardIds, @Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerContext.sell(cardIds);
		return playerMap.comparedMap();
	}

	@JaLang("升级卡牌")
	public Object feed(long cardId, @Bodys long[] cardIds, @Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.feed(cardId, cardIds));
		return playerMap.comparedMap();
	}

	@JaLang("进化卡牌")
	public Object evolute(long cardId, @Bodys long[] cardIds, @Attribute PlayerContext playerContext) {
		return evolute(cardId, null, cardIds, playerContext);
	}

	@JaLang("进化卡牌")
	public Object evolute(long cardId, XPropDefine propDefine, @Bodys long[] cardIds, @Attribute PlayerContext playerContext) {
		IPropEvolute propEvolute = null;
		if (propDefine != null) {
			Object propInvoker = propDefine.getPropInvoker();
			if (!(propInvoker instanceof IPropEvolute)) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}

			PropService.ME.useProp(playerContext, propDefine, 1);
			propEvolute = (IPropEvolute) propInvoker;
		}

		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.evolute(cardId, cardIds, propEvolute));
		return playerMap.comparedMap();
	}

	@JaLang("购买物品栏")
	public int item(@Attribute PlayerContext playerContext) {
		return item(1, playerContext);
	}

	@JaLang("购买物品栏")
	public int item(int number, @Attribute PlayerContext playerContext) {
		return playerContext.item(number);
	}

	@JaLang("购买好友数")
	public int friend(@Attribute PlayerContext playerContext) {
		return friend(1, playerContext);
	}

	@JaLang("购买好友数")
	public int friend(int number, @Attribute PlayerContext playerContext) {
		return playerContext.friend(number);
	}

	@JaLang("奖励数量")
	public int reward(@Attribute PlayerContext playerContext) {
		return playerContext.getPlayerA().getRewardNumber();
	}

	@JaLang("领取奖励")
	public Object reward(long rewardId, @Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.reward(rewardId));
		return playerMap.comparedMap();
	}

	@JaLang("奖励列表")
	public Object rewards(int pageIndex, @Attribute PlayerContext playerContext) {
		return PlayerService.ME.getPlayerRewards(playerContext.getId(), pageIndex);
	}

	@JaLang("x领取奖励")
	public Object xReward(XRewardDefine rewardDefine, @Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.xReward(rewardDefine));
		return playerMap.comparedMap();
	}

	@JaLang("进入任务")
	public Object task(int scene, int pass, int detail, @Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerMap.put("data", playerContext.task(scene, pass, detail));
		return playerMap.comparedMap();
	}

	@JaLang("修改玩家数据")
	public Object modifier(int level, int money, int diamond, int friendShipNumber, @Attribute PlayerContext playerContext) {
		if (!playerContext.getPlayerA().isDebug()) {
			throw new ServerException(ServerStatus.ON_DENIED);
		}

		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		playerContext.modifyLevel(level);
		playerContext.modifyMoney(money);
		playerContext.modifyDiamond(diamond);
		playerContext.modifyFriendShipNumber(friendShipNumber);
		return playerMap.comparedMap();
	}
}
