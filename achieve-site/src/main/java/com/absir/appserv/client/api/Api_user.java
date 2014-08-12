/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-9 下午3:05:01
 */
package com.absir.appserv.client.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.configure.JPlayerConfigure;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.PlayerService;
import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.system.api.ApiServer;
import com.absir.appserv.system.bean.JApp;
import com.absir.appserv.system.bean.JUser;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.appserv.system.server.value.Result;
import com.absir.appserv.system.service.AppService;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.FilterService;
import com.absir.appserv.system.service.SecurityService;
import com.absir.appserv.system.service.UserService;
import com.absir.appserv.system.service.utils.SecurityServiceUtils;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.binder.BinderData;
import com.absir.context.core.ContextUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.server.value.Param;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_user extends ApiServer {

	@JaLang("检测更新")
	public JApp app(String platfrom) {
		return AppService.ME.getLastApp(platfrom);
	}

	/** 用户名验证 */
	public static final Pattern NAME_PATTERN = Pattern.compile("([\\w]|[\\u4e00-\\u9fa5]){2,8}");

	@JaLang("注册账号")
	public Object register(@Bodys @Result(group = 1) JUser user, BinderData binderData, Input input) {
		if (binderData.getBinderResult().hashErrors()) {
			return binderData.getBinderResult().getPropertyErrors();
		}

		String username = user.getUsername();
		if (username == null || !NAME_PATTERN.matcher(username).find() || FilterService.ME.findFilter(username) > 0) {
			return null;
		}

		UserService.ME.register(user);
		return user.getUserId();
	}

	@JaLang("登录帐号")
	public JiUserBase login() {
		return SecurityServiceUtils.getUserBase();
	}

	@JaLang("注销帐号")
	public void logout(Input input) {
		SecurityService.ME.logout("api", input);
	}

	/**
	 * @author absir
	 * 
	 */
	protected static class UserPlayers {

		/** userBase */
		public JiUserBase userBase;

		/** players */
		public List<JPlayer> players;
	}

	@JaLang("选择服务区")
	public UserPlayers getPlayerIds() {
		return getPlayerIds(null);
	}

	@JaLang("选择服务区")
	public UserPlayers getPlayerIds(Long serverId) {
		UserPlayers userPlayers = new UserPlayers();
		userPlayers.userBase = SecurityServiceUtils.getUserBase();
		userPlayers.players = PlayerService.ME.players(serverId, userPlayers.userBase);
		return userPlayers;
	}

	@JaLang("角色设置")
	public JPlayerConfigure player() {
		return JConfigureUtils.getConfigure(JPlayerConfigure.class);
	}

	@JaLang("创建角色")
	public PlayerContext playParam(boolean sex, int cardId, @Param String name) {
		if (play(sex, name)) {
			return playCard(cardId);
		}

		return null;
	}

	@JaLang("创建角色")
	public boolean play(boolean sex, @Bodys String name) {
		if (!NAME_PATTERN.matcher(name).matches() || FilterService.ME.findFilter(name) > 0) {
			throw new ServerException(ServerStatus.ON_ERROR);
		}

		JiUserBase userBase = SecurityServiceUtils.getUserBase();
		Long playerId = PlayerService.ME.getPlayerId(null, userBase);
		if (playerId == null) {
			try {
				PlayerService.ME.create(null, userBase, name, sex);

			} catch (Throwable e) {
				throw new ServerException(ServerStatus.ON_FAIL);
			}

		} else {
			PlayerContext playerContext = ContextUtils.getContext(PlayerContext.class, playerId);
			JPlayer player = playerContext.getPlayer();
			if (player.getCard0() == null) {
				player.setName(name);
				player.setSex(sex);
				try {
					BeanService.ME.merge(playerContext.getPlayer());

				} catch (Throwable e) {
					throw new ServerException(ServerStatus.ON_FAIL);
				}

			} else {
				return false;
			}
		}

		return true;
	}

	@JaLang("选择卡牌")
	public PlayerContext playCard(int cardId) {
		JiUserBase userBase = SecurityServiceUtils.getUserBase();
		Long playerId = PlayerService.ME.getPlayerId(null, userBase);
		if (playerId == null) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		JPlayerConfigure playerConfigure = player();
		if (!ArrayUtils.contains(playerConfigure.getCardIds(), cardId)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		PlayerContext playerContext = ContextUtils.getContext(PlayerContext.class, playerId);
		if (playerContext.getPlayer().getCard0() != null) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		JPlayer player = playerContext.getPlayer();
		if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= -2) {
			// 调试数据
			player.setMoney(10000);
			player.setDiamond(10000);
			player.setMaxEp(10000);
			player.setMaxCardNumber(10000);
			player.setMaxFriendNumber(10000);

		} else {
			player.setMoney(PlayerContext.PLAYER_CONFIGURE.getMoney());
			player.setDiamond(PlayerContext.PLAYER_CONFIGURE.getDiamond());
		}

		// 初始化角色
		playerContext.modifyLevel(1);
		playerContext.modifyMoney(playerConfigure.getMoney());
		List<JCard> cards = new ArrayList<JCard>();
		XCardDefine cardDefine = PlayerContext.CARD_DEFINE_XLS_DAO.get(cardId);
		JCard card = playerContext.gainCard(cardDefine, cardDefine.getMaxLevel());
		cards.add(card);
		JeCamp cardCamp = card.getCardDefine().getCamp();
		int size = 1;
		for (int cid : playerConfigure.getOtherCardIds()) {
			cardDefine = PlayerContext.CARD_DEFINE_XLS_DAO.get(cid);
			if (cardCamp != null && cardDefine.getCamp() == cardCamp) {
				cardCamp = null;
				continue;
			}

			if (size++ < 5) {
				cards.add(playerContext.gainCard(cardDefine, 1));

			} else {
				break;
			}
		}

		// 初始阵型
		size = cards.size();
		if (size > 5) {
			size = 5;
		}

		for (int i = 0; i < size; i++) {
			player.setCard(cards.get(i), i);
		}

		// 保存初始化数据
		playerContext.uninitialize();
		return playerContext;
	}
}
