/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-9 下午9:22:19
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.client.assoc.PlayerIdAssoc;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.base.JbPermission;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaColum;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JaField;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JaEntity(permissions = JePermission.SELECT)
@Entity
public class JActivityRecard extends JbBean {

	@JaLang("玩家")
	@JaField(assocClasses = JbPermission.class, referencEntityClass = PlayerIdAssoc.class)
	@JaColum(indexs = @Index(name = "playerId", columnList = "playerId"))
	private long playerId;

	@JaLang("活动历史")
	@ManyToOne(fetch = FetchType.EAGER)
	private JActivityHistory activityHistory;

	@JaLang("排名")
	int ranking;

	/**
	 * @return the playerId
	 */
	public long getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 *            the playerId to set
	 */
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the activityHistory
	 */
	public JActivityHistory getActivityHistory() {
		return activityHistory;
	}

	/**
	 * @param activityHistory
	 *            the activityHistory to set
	 */
	public void setActivityHistory(JActivityHistory activityHistory) {
		this.activityHistory = activityHistory;
	}

	/**
	 * @return the ranking
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * @param ranking
	 *            the ranking to set
	 */
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
}
