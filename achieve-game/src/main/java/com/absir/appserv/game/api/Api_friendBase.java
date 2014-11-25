/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月25日 上午9:56:40
 */
package com.absir.appserv.game.api;

import java.util.List;

import com.absir.appserv.game.bean.JbPlayer;
import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.appserv.game.service.FriendService;
import com.absir.appserv.jdbc.JdbcEntities;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.value.Attribute;
import com.absir.server.value.Param;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "rawtypes" })
public abstract class Api_friendBase extends PlayerServer {

	@JaLang("好友列表")
	public JdbcEntities friends(JdbcPage jdbcPage, @Attribute JbPlayerContext playerContext) {
		return new JdbcEntities(FriendService.ME.getFriendPlayers(playerContext.getPlayer().getId(), jdbcPage), jdbcPage);
	}

	@JaLang("好友申请列表")
	public JdbcEntities friendings(JdbcPage jdbcPage, @Attribute JbPlayerContext playerContext) {
		return new JdbcEntities(FriendService.ME.getFriendings(playerContext.getPlayer().getId(), jdbcPage), jdbcPage);
	}

	@JaLang("系统推荐")
	public List<JbPlayer> recommends(@Attribute JbPlayerContext playerContext) {
		return FriendService.ME.getRecommends(playerContext.getPlayer());
	}

	@JaLang("搜索玩家")
	public Object search(@Param String name, JdbcPage jdbcPage, @Attribute JbPlayerContext playerContext) {
		return FriendService.ME.searchFriends(playerContext.getPlayer(), name, jdbcPage);
	}

	@JaLang("添加好友")
	public int add(Long targetId, @Attribute JbPlayerContext playerContext) {
		JbPlayer target = PlayerServiceBase.ME.findPlayer(targetId);
		if (target == null) {
			throw new ServerException(ServerStatus.ON_ERROR);
		}

		return FriendService.ME.addFriend(playerContext, target);
	}

	@JaLang("删除好友")
	public int remove(Long targetId, @Attribute JbPlayerContext playerContext) {
		return FriendService.ME.removeFriend(playerContext, targetId);
	}
}
