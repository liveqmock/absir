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

import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.context.core.ContextUtils;

/**
 * @author absir
 * 
 */
@Entity
public class JPayTrade extends JbBase {

	/** id */
	@JaLang("订单号")
	@Id
	private String id;

	/** createTime */
	@JaLang("创建时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long createTime = ContextUtils.getContextTime();

	/** uid */
	@JaLang("用户名")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String uid;

	/** name */
	@JaLang("商品类名")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String name;

	@JaLang("商品参数")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String nameData;

	/** amount */
	@JaLang("支付总金额")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private float amount;

	/** status */
	@JaLang("交易状态")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private JePayStatus status;

	/** orderNo */
	@JaLang("支付订单号")
	@Column(length = 1024)
	private String tradeNo;

	/** platform */
	@JaLang("平台名称")
	private String platform;

	/** platformData */
	@JaLang("平台支付参数")
	private String platformData;

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
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

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
	 * @return the nameData
	 */
	public String getNameData() {
		return nameData;
	}

	/**
	 * @param nameData
	 *            the nameData to set
	 */
	public void setNameData(String nameData) {
		this.nameData = nameData;
	}

	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @return the status
	 */
	public JePayStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(JePayStatus status) {
		this.status = status;
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
	 * @return the platformData
	 */
	public String getPlatformData() {
		return platformData;
	}

	/**
	 * @param platformData
	 *            the platformData to set
	 */
	public void setPlatformData(String platformData) {
		this.platformData = platformData;
	}
}
