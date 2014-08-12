/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-20 下午5:27:16
 */
package com.absir.appserv.client.context.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.JbSkill;
import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.core.kernel.KernelCollection;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
public class OProp_SKILL implements IPropSkill {

	/** propDefine */
	private XPropDefine propDefine;

	/** skillDefines */
	private XSkillDefine skillDefine;

	/**
	 * @param propDefine
	 */
	public OProp_SKILL(XPropDefine propDefine) {
		this.propDefine = propDefine;
		this.skillDefine = XlsUtils.findXlsBean(XSkillDefine.class, propDefine.getPropParmas()[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.IPropSkill#prop(com.absir.appserv
	 * .client.bean.JCard, int)
	 */
	@Override
	public void prop(JCard card, int index) {
		// TODO Auto-generated method stub
		XCardDefine cardDefine = card.getCardDefine();
		if (skillDefine == null || (propDefine.getCamp() != null && propDefine.getCamp() != cardDefine.getCamp()) || propDefine.getRare() > cardDefine.getRare()) {
			throw new ServerException(ServerStatus.NO_PARAM, "Can not use prop");
		}

		prop(card, skillDefine, index);
	}

	/** campRareMapSkillDefine */
	private static Map<String, XSkillDefine[]> campRareMapSkillDefine = new HashMap<String, XSkillDefine[]>();

	/**
	 * @param camp
	 * @param rare
	 * @return
	 */
	public static XSkillDefine[] getXSkillDefines(JeCamp camp, int rare) {
		String id = camp + "@" + rare;
		XSkillDefine[] skillDefines = campRareMapSkillDefine.get(id);
		if (skillDefines == null) {
			List<XSkillDefine> skillDefinesList = new ArrayList<XSkillDefine>();
			for (XSkillDefine skillDefine : XlsUtils.getXlsBeans(XSkillDefine.class)) {
				// && skillDefine.getRare() == rare
				if (skillDefine.getCamp() == camp && skillDefine.getTargetType() < 1) {
					skillDefinesList.add(skillDefine);
				}
			}

			skillDefines = KernelCollection.toArray(skillDefinesList, XSkillDefine.class);
			campRareMapSkillDefine.put(id, skillDefines);
		}

		return skillDefines;
	}

	/**
	 * @param card
	 * @param skillDefine
	 * @param index
	 */
	public static void prop(JCard card, XSkillDefine skillDefine, int index) {
		List<JbSkill> skills = card.getSkills();
		int size = skills == null ? 0 : skills.size();
		for (int i = 0; i < size; i++) {
			if (i != index && skills.get(i).getSkillDefine() == skillDefine) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}
		}

		if (index < 0 || index >= size) {
			if (size >= card.getSkillm()) {
				throw new ServerException(ServerStatus.NO_PARAM);
			}

			card.addSkill(skillDefine);

		} else {
			skills.get(index).setSkillDefine(skillDefine);
		}
	}

	/**
	 * @param card
	 * @param skillDefines
	 * @param difficulty
	 */
	public static void prop(JCard card, XSkillDefine[] skillDefines, float difficulty) {
		List<XSkillDefine> defines = new LinkedList<XSkillDefine>();
		for (XSkillDefine skillDefine : skillDefines) {
			defines.add(skillDefine);
		}

		int skillSize = 0;
		if (card.getSkills() != null) {
			skillSize = card.getSkills().size();
			for (JbSkill skill : card.getSkills()) {
				defines.remove(skill.getSkillDefine());
			}
		}

		if (defines.isEmpty()) {
			throw new ServerException(ServerStatus.NO_PARAM, "Can not lean new skill");
		}

		XSkillDefine skillDefine = HelperRandom.randElement(defines);
		if (skillSize >= card.getSkillm() || (HelperRandom.RANDOM.nextFloat() * difficulty) > PlayerContext.PLAYER_CONFIGURE.getSkillProb()[skillSize]) {
			card.replaceSkill(skillDefine);

		} else {
			card.addSkill(skillDefine);
		}
	}
}
