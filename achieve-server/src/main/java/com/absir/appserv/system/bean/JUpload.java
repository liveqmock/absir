/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月9日 下午2:57:07
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBeanSS;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JaName;
import com.absir.appserv.system.crud.DateCrudFactory;

/**
 * @author absir
 *
 */
@MaEntity(parent = { @MaMenu("附件管理") }, name = "上传")
@Entity
public class JUpload extends JbBeanSS {

	@JaLang(value = "文件类型")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaName("JUser")
	private String fileType;

	@JaLang(value = "关联用户", tag = "assocUser")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaName("JUser")
	private long userId;

	@JaLang("创建时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	@JaCrud(value = "dateCrudFactory", cruds = { Crud.CREATE }, factory = DateCrudFactory.class)
	private long createTime;

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
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
}
