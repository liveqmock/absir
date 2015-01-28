/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-9 下午3:05:01
 */
package com.absir.appserv.game.api;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.confiure.JPlayerConfigure;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.appserv.system.api.ApiServer;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.SecurityService;
import com.absir.appserv.system.service.utils.SecurityServiceUtils;
import com.absir.context.core.ContextUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.server.value.Nullable;
import com.absir.server.value.Param;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class Api_userBase extends ApiServer {

	/** 用户名验证 */
	public static final Pattern NAME_PATTERN = Pattern.compile("([\\w]|[\\u4e00-\\u9fa5]){2,8}");

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
		public JiUserBase user;

		/** players */
		public List<JbPlayer> players;
	}

	@JaLang("选择服务区")
	public UserPlayers getPlayerIds() {
		return getPlayerIds(null);
	}

	@JaLang("选择服务区")
	public UserPlayers getPlayerIds(Long serverId) {
		UserPlayers userPlayers = new UserPlayers();
		userPlayers.user = SecurityServiceUtils.getUserBase();
		userPlayers.players = PlayerServiceBase.ME.players(serverId, userPlayers.user);
		return userPlayers;
	}

	@JaLang("角色设置")
	public JPlayerConfigure configure() {
		return JbPlayerContext.CONFIGURE;
	}

	@JaLang("创建角色")
	public JbPlayerContext playParam(int gender, int cardId, @Param String name, @Nullable @Param Long serverId) {
		if (play(gender, name, serverId)) {
			return playCard(cardId);
		}

		return null;
	}

	@JaLang("创建名称")
	protected boolean isMatchName(String name) {
		return name == null || NAME_PATTERN.matcher(name).matches() || PlayerServiceBase.ME.findFilter(name);
	}

	@JaLang("创建角色")
	public boolean play(int gender, @Bodys String name, @Nullable @Param Long serverId) {
		if (!isMatchName(name)) {
			throw new ServerException(ServerStatus.ON_ERROR);
		}

		JiUserBase userBase = SecurityServiceUtils.getUserBase();
		Long playerId = PlayerServiceBase.ME.getPlayerId(serverId, userBase);
		if (playerId == null) {
			try {
				PlayerServiceBase.ME.create(null, userBase, name, gender);

			} catch (Throwable e) {
				e.printStackTrace();
				throw new ServerException(ServerStatus.ON_FAIL);
			}

		} else {
			JbPlayerContext playerContext = ContextUtils.getContext(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, playerId);
			JbPlayer player = playerContext.getPlayer();
			if (player.getCard() == 0) {
				player.setName(name);
				player.setGender(gender);
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
	public JbPlayerContext playCard(int cardId) {
		JiUserBase userBase = SecurityServiceUtils.getUserBase();
		Long playerId = PlayerServiceBase.ME.getPlayerId(null, userBase);
		if (playerId == null) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		JPlayerConfigure playerConfigure = configure();
		if (!ArrayUtils.contains(playerConfigure.getCardIds(), cardId)) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		JbPlayerContext playerContext = ContextUtils.getContext(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, playerId);
		if (playerContext.getPlayer().getCard() != 0) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		// 初始化角色
		List<JbCard> cards = doPlayCard(cardId, playerContext);

		// 初始阵型
		int size = cards.size();
		if (size > 5) {
			size = 5;
		}

		JbPlayer player = playerContext.getPlayer();
		for (int i = 0; i < size; i++) {
			player.setCard(cards.get(i), i);
		}

		// 保存初始化数据
		playerContext.uninitialize();
		return playerContext;
	}

	protected abstract List<JbCard> doPlayCard(int cardId, JbPlayerContext playerContext);
}
