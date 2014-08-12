/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午11:05:33
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
public class OSkillNs extends OSkillBase {

	/** OTRIGGER_BASE_NAME */
	public static final String OTRIGGER_BASE_NAME = KernelString.rightSubString(OTriggerBase.class.getName(), 4) + "_";

	/**
	 * @param skillDefine
	 */
	public OSkillNs(XSkillDefine skillDefine) {
		super(skillDefine);
		// TODO Auto-generated constructor stub
		// triggerBase =
		// KernelClass.newInstance(KernelClass.forName(OTRIGGER_BASE_NAME +
		// skillDefine.getTriggerType()), null, new Object[] {
		// skillDefine.getTriggerParameters() });
	}
}
