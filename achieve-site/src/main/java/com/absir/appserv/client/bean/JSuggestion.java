/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-6-3 上午10:30:28
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;
import javax.persistence.Index;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.client.assoc.PlayerIdAssoc;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.base.JbPermission;
import com.absir.appserv.system.bean.proxy.JpCreate;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaColum;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JaField;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("游戏管理") }, name = "反馈")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JaEntity(permissions = { JePermission.SELECT, JePermission.INSERT, JePermission.UPDATE, JePermission.DELETE })
@Entity
public class JSuggestion extends JbBean implements JpCreate {

	@JaLang("玩家")
	@JaField(assocClasses = JbPermission.class, referencEntityClass = PlayerIdAssoc.class)
	@JaCrud(factory = PlayerIdAssoc.class, cruds = Crud.CREATE)
	@JaColum(indexs = @Index(name = "playerId", columnList = "playerId"))
	private long playerId;

	@JaLang("问题")
	private String issue;

	@JaLang("回答")
	private String reply;

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
	 * @return the issue
	 */
	public String getIssue() {
		return issue;
	}

	/**
	 * @param issue
	 *            the issue to set
	 */
	public void setIssue(String issue) {
		this.issue = issue;
	}

	/**
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}

	/**
	 * @param reply
	 *            the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}
}
