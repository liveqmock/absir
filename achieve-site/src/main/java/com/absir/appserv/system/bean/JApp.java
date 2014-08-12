/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-17 下午3:20:25
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;
import javax.persistence.Index;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.proxy.JpUpdate;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.crud.UploadCrudFactory;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("版本管理") }, name = "客户端")
@Entity
public class JApp extends JbBean implements JpUpdate {

	@JaLang("版本名称")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private String name;

	@JaLang("平台")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	// @Enumerated
	@JaColum(indexs = @Index(columnList = ""))
	private String platform;

	@JaLang("版本介绍")
	@JaEdit(types = "text")
	private String detail;

	@JaLang("文件")
	@JaEdit(types = "file")
	@JaCrud(factory = UploadCrudFactory.class, cruds = { Crud.CREATE, Crud.UPDATE, Crud.DELETE })
	private String file;

	@JaLang("版本号")
	@JaEdit(groups = { JaEdit.GROUP_LIST })
	private int version;

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
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail
	 *            the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
}
