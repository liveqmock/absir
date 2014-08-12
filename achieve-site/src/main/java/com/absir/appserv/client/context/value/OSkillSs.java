/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-20 下午10:23:17
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.configure.xls.XSkillDefine;

/**
 * @author absir
 * 
 */
public class OSkillSs extends OSkillBase {

	// 必须答对数量
	private int ssCorrect;

	/**
	 * @param skillDefine
	 */
	public OSkillSs(XSkillDefine skillDefine) {
		super(skillDefine);
		// TODO Auto-generated constructor stub
		if (effectBase != null) {
			effectBase.setEffectName("SS_" + effectBase.getEffectName());
			this.ssCorrect = 1;
		}
	}

	/**
	 * @return
	 */
	public int getSsCorrect() {
		return ssCorrect;
	}
}
