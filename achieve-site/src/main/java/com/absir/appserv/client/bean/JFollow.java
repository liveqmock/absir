/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-20 下午1:21:11
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;
import javax.persistence.Index;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.system.bean.base.JbBeanLL;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class JFollow extends JbBeanLL {

	@JaLang("关注")
	@JaColum(indexs = @Index(columnList = ""))
	private boolean following;

	@JaLang("被关注")
	@JaColum(indexs = @Index(columnList = ""))
	private boolean follower;

	@JaLang("应援")
	@JaColum(indexs = @Index(columnList = "encouraging,encouragingTime"))
	private boolean encouraging;

	@JaLang("应援时间")
	private long encouragingTime;

	@JaLang("留言数量")
	private int messageNumber;

	@JaLang("留言时间")
	@JaColum(indexs = @Index(columnList = ""))
	private long messageTime;

	/**
	 * @return the following
	 */
	public boolean isFollowing() {
		return following;
	}

	/**
	 * @param following
	 *            the following to set
	 */
	public void setFollowing(boolean following) {
		this.following = following;
	}

	/**
	 * @return the follower
	 */
	public boolean isFollower() {
		return follower;
	}

	/**
	 * @param follower
	 *            the follower to set
	 */
	public void setFollower(boolean follower) {
		this.follower = follower;
	}

	/**
	 * @return the encouraging
	 */
	public boolean isEncouraging() {
		return encouraging;
	}

	/**
	 * @param encouraging
	 *            the encouraging to set
	 */
	public void setEncouraging(boolean encouraging) {
		this.encouraging = encouraging;
	}

	/**
	 * @return the encouragingTime
	 */
	public long getEncouragingTime() {
		return encouragingTime;
	}

	/**
	 * @param encouragingTime
	 *            the encouragingTime to set
	 */
	public void setEncouragingTime(long encouragingTime) {
		this.encouragingTime = encouragingTime;
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
	 * @return the messageTime
	 */
	public long getMessageTime() {
		return messageTime;
	}

	/**
	 * @param messageTime
	 *            the messageTime to set
	 */
	public void setMessageTime(long messageTime) {
		this.messageTime = messageTime;
	}
}
