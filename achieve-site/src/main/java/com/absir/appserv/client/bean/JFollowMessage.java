/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-21 上午11:17:26
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;
import javax.persistence.Index;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.system.bean.JEmbedLL;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class JFollowMessage extends JbBean {

	@JaLang("关联用户")
	@JsonIgnore
	@JaColum(indexs = @Index(name = "emid", columnList = "eid,mid"))
	private JEmbedLL emid;

	@JaLang("用户ID")
	private long playerId;

	@JaLang("创建时间")
	private long createTime;

	@JaLang("消息")
	private String message;

	/**
	 * @return the emid
	 */
	public JEmbedLL getEmid() {
		return emid;
	}

	/**
	 * @param emid
	 *            the emid to set
	 */
	public void setEmid(JEmbedLL emid) {
		this.emid = emid;
	}

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
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
