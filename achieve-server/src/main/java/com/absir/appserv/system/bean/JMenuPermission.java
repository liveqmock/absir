/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-29 下午3:32:06
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.lang.value.Langs;
import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JaName;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("菜单管理") }, name = "权限")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class JMenuPermission extends JbBase {

	@JaLang(value = "标识", tag = "identifie")
	@Id
	private String id;

	@JaLang("标题")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String caption;

	@JaLang(value = "授权角色", tag = "allowUserRoles")
	@JaName(value = "JUser")
	@JaEdit(groups = JaEdit.GROUP_SUGGEST)
	private long allowIds[];

	@JaLang(value = "禁用角色", tag = "forbidUserRoles")
	@JaName(value = "JUser")
	@JaEdit(groups = JaEdit.GROUP_SUGGEST)
	private long forbidIds[];

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the caption
	 */
	@Langs
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the allowIds
	 */
	public long[] getAllowIds() {
		return allowIds;
	}

	/**
	 * @param allowIds
	 *            the allowIds to set
	 */
	public void setAllowIds(long[] allowIds) {
		this.allowIds = allowIds;
	}

	/**
	 * @return the forbidIds
	 */
	public long[] getForbidIds() {
		return forbidIds;
	}

	/**
	 * @param forbidIds
	 *            the forbidIds to set
	 */
	public void setForbidIds(long[] forbidIds) {
		this.forbidIds = forbidIds;
	}
}
