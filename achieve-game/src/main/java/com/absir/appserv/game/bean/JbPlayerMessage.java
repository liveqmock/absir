/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年12月2日 下午8:20:18
 */
package com.absir.appserv.game.bean;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JaName;

/**
 * @author absir
 *
 */
@MappedSuperclass
public abstract class JbPlayerMessage extends JbPlayerReward {

	@JaLang("来源角色编号")
	@JaName("JPlayer")
	private long fromPlayerId;

	@JaLang("已读")
	private boolean readed;

	@JaLang("奖励")
	private transient boolean rewarded;

	@JaLang("数据")
	@Lob
	private String content;

	/**
	 * @return the fromPlayerId
	 */
	public long getFromPlayerId() {
		return fromPlayerId;
	}

	/**
	 * @param fromPlayerId
	 *            the fromPlayerId to set
	 */
	public void setFromPlayerId(long fromPlayerId) {
		this.fromPlayerId = fromPlayerId;
	}

	/**
	 * @return the readed
	 */
	public boolean isReaded() {
		return readed;
	}

	/**
	 * @param readed
	 *            the readed to set
	 */
	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	/**
	 * @return the rewarded
	 */
	public boolean isRewarded() {
		return rewarded;
	}

	/**
	 * @param rewarded
	 *            the rewarded to set
	 */
	public void setRewarded(boolean rewarded) {
		this.rewarded = rewarded;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
