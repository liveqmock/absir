/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 上午11:39:03
 */
package com.absir.appserv.game.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.absir.appserv.game.bean.JbPlayerA.PlayerAtt;
import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaEmbedd;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JeEditable;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
@MappedSuperclass
public abstract class JbPlayerA<T extends PlayerAtt> extends JbBase {

	@JaLang("编号ID")
	@Id
	private Long id;

	@JaLang("调试")
	private boolean debug;

	@JaLang("最近下线")
	private long lastOffline;

	@JaLang("最早登录")
	private int onlineDay;

	@JaLang("连续登录")
	private int online;

	@JaLang("在线时间")
	private long onlineTime;

	@JaLang("友情抽奖免费")
	private long friendLotFree;

	@JaLang("宝石抽奖免费")
	private long diamondLotFree;

	@JaLang("引导")
	private int guide;

	@JaLang("奖励数量")
	@JaEdit(editable = JeEditable.LOCKED)
	private transient int rewardNumber;

	@JaLang("消息数量")
	@JaEdit(editable = JeEditable.LOCKED)
	private transient int messageNumber;

	@JaLang("数据检查")
	private transient boolean att;

	@JaLang("存档数据")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonDynamic")
	@JaEmbedd
	private T playerAtt;

	/**
	 * @author absir
	 * 
	 */
	public static class PlayerAtt {

		/** taskProgresses */
		@JaLang("任务进度")
		public Map<String, Integer> taskProgresses;

		/** propNumbers */
		@JaLang("参数纪录")
		public Map<String, Integer> metaRecards;

		/** dailyRecards */
		@JaLang("每日纪录")
		public Map<String, Integer> dailyRecards;

		/** propNumbers */
		@JaLang("道具数量")
		public Map<Serializable, Integer> propNumbers;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug
	 *            the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return the lastOffline
	 */
	public long getLastOffline() {
		return lastOffline;
	}

	/**
	 * @param lastOffline
	 *            the lastOffline to set
	 */
	public void setLastOffline(long lastOffline) {
		this.lastOffline = lastOffline;
	}

	/**
	 * @return the onlineDay
	 */
	public int getOnlineDay() {
		return onlineDay;
	}

	/**
	 * @param onlineDay
	 *            the onlineDay to set
	 */
	public void setOnlineDay(int onlineDay) {
		this.onlineDay = onlineDay;
	}

	/**
	 * @return the online
	 */
	public int getOnline() {
		return online;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(int online) {
		this.online = online;
	}

	/**
	 * @return the onlineTime
	 */
	public long getOnlineTime() {
		return onlineTime;
	}

	/**
	 * @param onlineTime
	 *            the onlineTime to set
	 */
	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	/**
	 * @return the friendLotFree
	 */
	public long getFriendLotFree() {
		return friendLotFree;
	}

	/**
	 * @param friendLotFree
	 *            the friendLotFree to set
	 */
	public void setFriendLotFree(long friendLotFree) {
		this.friendLotFree = friendLotFree;
	}

	/**
	 * @return the diamondLotFree
	 */
	public long getDiamondLotFree() {
		return diamondLotFree;
	}

	/**
	 * @param diamondLotFree
	 *            the diamondLotFree to set
	 */
	public void setDiamondLotFree(long diamondLotFree) {
		this.diamondLotFree = diamondLotFree;
	}

	/**
	 * @return the guide
	 */
	public int getGuide() {
		return guide;
	}

	/**
	 * @param guide
	 *            the guide to set
	 */
	public void setGuide(int guide) {
		this.guide = guide;
	}

	/**
	 * @return the rewardNumber
	 */
	public int getRewardNumber() {
		return rewardNumber;
	}

	/**
	 * @param rewardNumber
	 *            the rewardNumber to set
	 */
	public void setRewardNumber(int rewardNumber) {
		this.rewardNumber = rewardNumber;
	}

	/**
	 * @return the messageNumber
	 */
	public int getMessageNumber() {
		return messageNumber;
	}

	/**
	 * @param messageNumber
	 *            the messageNumber to set
	 */
	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	/**
	 * @return
	 */
	private PlayerAtt getPlayerAtt() {
		if (!att) {
			// 数据安全检查
			att = true;
			playerAtt();
		}

		return playerAtt;
	}

	/**
	 * 数据安全检查
	 */
	protected void playerAtt() {
		if (playerAtt == null) {
			playerAtt = (T) new PlayerAtt();
		}

		playerAtt.taskProgresses = playerAtt.taskProgresses == null ? new HashMap<String, Integer>() : new HashMap<String, Integer>(playerAtt.taskProgresses);
		playerAtt.metaRecards = playerAtt.metaRecards == null ? new HashMap<String, Integer>() : new HashMap<String, Integer>(playerAtt.metaRecards);
		playerAtt.dailyRecards = playerAtt.dailyRecards == null ? new HashMap<String, Integer>() : new HashMap<String, Integer>(playerAtt.dailyRecards);
		if (playerAtt.propNumbers == null) {
			playerAtt.propNumbers = new LinkedHashMap<Serializable, Integer>();
		}
	}

	/**
	 * @return the taskProgresses
	 */
	public Map<String, Integer> getTaskProgresses() {
		return getPlayerAtt().taskProgresses;
	}

	/**
	 * @param taskProgresses
	 *            the taskProgresses to set
	 */
	public void setTaskProgresses(Map<String, Integer> taskProgresses) {
		getPlayerAtt().taskProgresses = taskProgresses;
	}

	/**
	 * @return the metaRecards
	 */
	public Map<String, Integer> getMetaRecards() {
		return getPlayerAtt().metaRecards;
	}

	/**
	 * @param metaRecards
	 *            the metaRecards to set
	 */
	public void setMetaRecards(Map<String, Integer> metaRecards) {
		getPlayerAtt().metaRecards = metaRecards;
	}

	/**
	 * @return the dailyRecards
	 */
	public Map<String, Integer> getDailyRecards() {
		return getPlayerAtt().dailyRecards;
	}

	/**
	 * @param dailyRecards
	 *            the dailyRecards to set
	 */
	public void setDailyRecards(Map<String, Integer> dailyRecards) {
		getPlayerAtt().dailyRecards = dailyRecards;
	}

	/**
	 * @return the propNumbers
	 */
	public Map<Serializable, Integer> getPropNumbers() {
		return getPlayerAtt().propNumbers;
	}

	/**
	 * @param propNumbers
	 *            the propNumbers to set
	 */
	public void setPropNumbers(Map<Serializable, Integer> propNumbers) {
		getPlayerAtt().propNumbers = propNumbers;
	}

}
