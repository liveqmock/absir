/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-3 上午11:12:23
 */
package com.absir.appserv.client.configure.xls;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.client.bean.JbReward;
import com.absir.appserv.client.context.value.IPropPlayer;
import com.absir.appserv.client.context.value.OReward;
import com.absir.appserv.configure.xls.XlsBeanUpdate;
import com.absir.appserv.configure.xls.value.XaParam;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.core.kernel.KernelClass;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
public class XRewardDefine extends XlsBeanUpdate<String> {

	@JaLang("奖励名称")
	private String name;

	@JaLang("奖励说明")
	private String desc;

	@JaLang("奖励类型")
	private String rewardType;

	@JaLang("奖励条件")
	@XaParam
	private String[] rewardParams;

	@JaLang("奖励内容")
	private JbReward reward;

	/** propInvoker */
	@JaLang("奖励执行类")
	@JsonIgnore
	private transient OReward rewardInvoker;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the rewardType
	 */
	public String getRewardType() {
		return rewardType;
	}

	/**
	 * @return the rewardParams
	 */
	public String[] getRewardParams() {
		return rewardParams;
	}

	/**
	 * @return the reward
	 */
	public JbReward getReward() {
		return reward;
	}

	/**
	 * @return the rewardInvoker
	 */
	@JsonIgnore
	public OReward getRewardInvoker() {
		if (rewardInvoker == null) {
			Class<?> rewardClass = KernelClass.forName(IPropPlayer.class.getPackage().getName() + ".OReward_" + rewardType);
			rewardInvoker = (OReward) KernelClass.newInstance(rewardClass, this);
		}

		return rewardInvoker;
	}
}
