/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 下午4:25:23
 */
package com.absir.appserv.client.bean;

import javax.persistence.Embeddable;

import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Embeddable
public class JActivityReward extends JbRewardBean {

	@JaLang("排名")
	private int ranking;

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
