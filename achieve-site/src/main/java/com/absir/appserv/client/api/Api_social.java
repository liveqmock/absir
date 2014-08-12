/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-11 上午10:54:42
 */
package com.absir.appserv.client.api;

import java.util.List;

import com.absir.appserv.client.bean.JPlayer;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.SocialService;
import com.absir.appserv.client.service.utils.CommonUtils;
import com.absir.appserv.jdbc.JdbcEntities;
import com.absir.appserv.jdbc.JdbcPage;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.context.core.ContextMap;
import com.absir.server.value.Attribute;
import com.absir.server.value.Param;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_social extends PlayerServer {

	@JaLang("关注(取消)对象")
	public boolean follow(JPlayer friend, @Attribute PlayerContext playerContext) {
		return SocialService.ME.follow(playerContext, friend);
	}

	@JaLang("应援对象")
	public void encourage(long friendId, @Attribute PlayerContext playerContext) {
		SocialService.ME.encourage(playerContext, friendId);
	}

	@JaLang("一键应援")
	public Object encourage(@Attribute PlayerContext playerContext) {
		ContextMap playerMap = new ContextMap(playerContext.getPlayer());
		SocialService.ME.encourageAll(playerContext);
		return playerMap.comparedMap();
	}

	@JaLang("关注列表")
	public JdbcEntities following(int pageIndex, @Attribute PlayerContext playerContext) {
		return following(CommonUtils.getJdbcPage(pageIndex), playerContext);
	}

	@JaLang("关注列表")
	public JdbcEntities following(@Bodys JdbcPage jdbcPage, @Attribute PlayerContext playerContext) {
		return new JdbcEntities(SocialService.ME.following(playerContext, jdbcPage), jdbcPage);
	}

	@JaLang("被关注列表")
	public JdbcEntities follower(int pageIndex, @Attribute PlayerContext playerContext) {
		return follower(CommonUtils.getJdbcPage(pageIndex), playerContext);
	}

	@JaLang("被关注列表")
	public JdbcEntities follower(@Bodys JdbcPage jdbcPage, @Attribute PlayerContext playerContext) {
		return new JdbcEntities(SocialService.ME.follower(playerContext.getPlayer(), jdbcPage), jdbcPage);
	}

	@JaLang("系统推荐")
	public List<JPlayer> favorites(@Attribute PlayerContext playerContext) {
		return SocialService.ME.favorites(playerContext);
	}

	@JaLang("搜索好友")
	public JdbcEntities search(int pageIndex, @Bodys String name, @Attribute PlayerContext playerContext) {
		return search(name, CommonUtils.getJdbcPage(pageIndex), playerContext);
	}

	@JaLang("搜索好友")
	public JdbcEntities search(@Param String name, @Bodys JdbcPage jdbcPage, @Attribute PlayerContext playerContext) {
		return new JdbcEntities(SocialService.ME.search(name, playerContext, jdbcPage), jdbcPage);
	}

	/**
	 * @author absir
	 * 
	 */
	public static class NameJdbcPage {

		/** name */
		public String name;

		/** jdbcPage */
		public JdbcPage jdbcPage;
	}

	@JaLang("搜索好友")
	public JdbcEntities searchN(@Bodys NameJdbcPage nameJdbcPage, @Attribute PlayerContext playerContext) {
		return search(nameJdbcPage.name, nameJdbcPage.jdbcPage, playerContext);
	}

	@JaLang("相互关注列表")
	public JdbcEntities friends(int pageIndex, @Attribute PlayerContext playerContext) {
		return friends(CommonUtils.getJdbcPage(pageIndex), playerContext);
	}

	@JaLang("相互关注列表")
	public JdbcEntities friends(@Bodys JdbcPage jdbcPage, @Attribute PlayerContext playerContext) {
		return new JdbcEntities(SocialService.ME.friends(playerContext.getPlayer(), jdbcPage), jdbcPage);
	}

	@JaLang("发送消息")
	public long send(long friendId, @Bodys String message, @Attribute PlayerContext playerContext) {
		return SocialService.ME.send(playerContext.getPlayer(), friendId, message).getCreateTime();
	}

	@JaLang("获取消息列表")
	public JdbcEntities messages(long friendId, int pageIndex, @Attribute PlayerContext playerContext) {
		return messages(friendId, CommonUtils.getJdbcPage(pageIndex), playerContext);
	}

	@JaLang("获取消息列表")
	public JdbcEntities messages(long friendId, @Bodys JdbcPage jdbcPage, @Attribute PlayerContext playerContext) {
		return new JdbcEntities(SocialService.ME.messages(playerContext.getPlayer(), friendId, jdbcPage), jdbcPage);
	}

	@JaLang("平台分享奖励")
	public Object share(@Bodys String shareCode, @Attribute PlayerContext playerContext) {
		return SocialService.ME.share(playerContext, shareCode);
	}
}
