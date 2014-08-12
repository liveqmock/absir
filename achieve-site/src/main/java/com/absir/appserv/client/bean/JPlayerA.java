/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-3 下午12:35:01
 */
package com.absir.appserv.client.bean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaEmbedd;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JeEditable;

/**
 * @author absir
 * 
 */
@Entity
public class JPlayerA extends JbBase {

	@JaLang("编号ID")
	@Id
	private Long id;

	@JaLang("调试")
	private boolean debug;

	@JaLang("最近下线")
	private long lastOffline;

	/** online */
	@JaLang("最早登录")
	private int onlineDay;

	/** online */
	@JaLang("连续登录")
	private int online;

	/** onlineTime */
	@JaLang("在线时间")
	private long onlineTime;

	@JaLang("教学进度")
	private int teach;

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
	private PlayerAtt playerAtt;

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

		/** propNumbers */
		@JaLang("道具数量")
		public Map<Integer, Integer> propNumbers;
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
	 * @return the teach
	 */
	public int getTeach() {
		return teach;
	}

	/**
	 * @param teach
	 *            the teach to set
	 */
	public void setTeach(int teach) {
		this.teach = teach;
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
			if (playerAtt == null) {
				playerAtt = new PlayerAtt();
			}

			playerAtt.taskProgresses = playerAtt.taskProgresses == null ? new HashMap<String, Integer>() : new HashMap<String, Integer>(playerAtt.taskProgresses);
			playerAtt.metaRecards = playerAtt.metaRecards == null ? new HashMap<String, Integer>() : new HashMap<String, Integer>(playerAtt.metaRecards);
			if (playerAtt.propNumbers == null) {
				playerAtt.propNumbers = new LinkedHashMap<Integer, Integer>();
			}
		}

		return playerAtt;
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
	 * @return the propNumbers
	 */
	public Map<Integer, Integer> getPropNumbers() {
		return getPlayerAtt().propNumbers;
	}

	/**
	 * @param propNumbers
	 *            the propNumbers to set
	 */
	public void setPropNumbers(Map<Integer, Integer> propNumbers) {
		getPlayerAtt().propNumbers = propNumbers;
	}
}
