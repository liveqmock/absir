/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-8 下午12:43:09
 */
package com.absir.appserv.system.bean.base;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.proxy.JiUpdate;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbRecycleBase extends JbBase implements JiUpdate {

	@Id
	@JaLang("纪录编号")
	private Long id;

	@JaLang("修改时间")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaCrud(value = "dateCrudFactory", cruds = { Crud.CREATE, Crud.UPDATE })
	private long updateTime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
