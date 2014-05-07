/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-12 下午2:05:05
 */
package com.absir.appserv.system.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.context.core.ContextUtils;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("支付管理") }, name = "支付")
@Entity
public class JPayHistory extends JbBase {

	@JaLang("订单号")
	@Id
	private String id;

	/** id */
	@JaLang("支付订单号")
	@Column(length = 1024, unique = true)
	private String tradeNo;

	/** createTime */
	@JaLang("创建时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long createTime = ContextUtils.getContextTime();

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
	 * @return the tradeNo
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * @param tradeNo
	 *            the tradeNo to set
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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
