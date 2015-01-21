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
import javax.persistence.Index;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.context.core.ContextUtils;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("支付管理") }, name = "订单")
@Entity
public class JPayTrade extends JbBase {

	/** id */
	@JaLang(value = "订单号", tag = "tradeId")
	@Id
	private String id;

	/** createTime */
	@JaLang("创建时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long createTime = ContextUtils.getContextTime();

	/** uid */
	@JaLang(value = "用户", tag = "user")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String uid;

	/** name */
	@JaLang(value = "商品名", tag = "goodsName")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String name;

	/** nameData */
	@JaLang(value = "商品参数", tag = "goodsData")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String nameData;

	/** amount */
	@JaLang("金额")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private float amount;

	/** status */
	@JaLang(value = "交易状态", tag = "tradeStatus")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaColum(indexs = @Index(columnList = ""))
	private JePayStatus status;

	/** tradeNo */
	@JaLang("交易号")
	@Column(length = 1024)
	private String tradeNo;

	/** platform */
	@JaLang(value = "平台名称", tag = "platformName")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private String platform;

	/** platformData */
	@JaLang("平台参数")
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
