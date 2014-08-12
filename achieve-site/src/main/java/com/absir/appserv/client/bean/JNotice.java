/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-16 下午1:57:06
 */
package com.absir.appserv.client.bean;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.proxy.JpUpdate;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
@MaEntity(parent = { @MaMenu("活动管理") }, name = "通知")
@Entity
public class JNotice extends JbBean implements JpUpdate {

	@JaLang("通知名称")
	@JaEdit(groups = JaEdit.GROUP_SUGGEST)
	private String name;

	@JaLang("通知内容")
	@JaEdit(types = "text")
	@Column(length = 2048)
	private String content;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
