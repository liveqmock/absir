/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-9 下午9:22:19
 */
package com.absir.appserv.client.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.absir.appserv.client.context.value.OPlayerResult;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JaEntity(permissions = JePermission.SELECT)
@Entity
public class JActivityHistory extends JbBean {

	@JaLang("活动基本信息")
	private JActivityBase activityBase;

	@JaLang("开始时间")
	private long beginTime;

	@JaLang("过期时间")
	private long passTime;

	@JaLang("参加人数")
	private long participants;

	@JaLang("最大人数")
	private long maxOnline;

	@JaLang("排名纪录")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonList")
	private List<OPlayerResult> playerResults = new ArrayList<OPlayerResult>();

	/**
	 * @return the activityBase
	 */
	public JActivityBase getActivityBase() {
		return activityBase;
	}

	/**
	 * @param activityBase
	 *            the activityBase to set
	 */
	public void setActivityBase(JActivityBase activityBase) {
		this.activityBase = activityBase;
	}

	/**
	 * @return the beginTime
	 */
	public long getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the passTime
	 */
	public long getPassTime() {
		return passTime;
	}

	/**
	 * @param passTime
	 *            the passTime to set
	 */
	public void setPassTime(long passTime) {
		this.passTime = passTime;
	}

	/**
	 * @return the participants
	 */
	public long getParticipants() {
		return participants;
	}

	/**
	 * @param participants
	 *            the participants to set
	 */
	public void setParticipants(long participants) {
		this.participants = participants;
	}

	/**
	 * @return the maxOnline
	 */
	public long getMaxOnline() {
		return maxOnline;
	}

	/**
	 * @param maxOnline
	 *            the maxOnline to set
	 */
	public void setMaxOnline(long maxOnline) {
		this.maxOnline = maxOnline;
	}

	/**
	 * @return the playerResults
	 */
	public List<OPlayerResult> getPlayerResults() {
		return playerResults;
	}

	/**
	 * @param playerResults
	 *            the playerResults to set
	 */
	public void setPlayerResults(List<OPlayerResult> playerResults) {
		this.playerResults = playerResults;
	}
}
