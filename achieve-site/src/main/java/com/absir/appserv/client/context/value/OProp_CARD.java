/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-20 下午5:27:16
 */
package com.absir.appserv.client.context.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.configure.xls.XlsDao;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.core.kernel.KernelCollection;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
public class OProp_CARD implements IPropCard {

	/** propDefine */
	private XPropDefine propDefine;

	/** skillDefines */
	private XSkillDefine[] skillDefines;

	/**
	 * @param propDefine
	 */
	public OProp_CARD(XPropDefine propDefine) {
		this.propDefine = propDefine;
		XlsDao<XSkillDefine, Serializable> skillDao = XlsUtils.getXlsDao(XSkillDefine.class);
		List<XSkillDefine> skillDefines = new ArrayList<XSkillDefine>();
		for (String id : propDefine.getPropParmas()) {
			skillDefines.add(skillDao.find(id));
		}

		this.skillDefines = KernelCollection.toArray(skillDefines, XSkillDefine.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.ICardProp#prop(com.absir.appserv
	 * .client.bean.JCard)
	 */
	@Override
	public void prop(JCard card) {
		// TODO Auto-generated method stub
		XCardDefine cardDefine = card.getCardDefine();
		if ((propDefine.getCamp() != null && propDefine.getCamp() != cardDefine.getCamp()) || propDefine.getRare() > cardDefine.getRare()) {
			throw new ServerException(ServerStatus.NO_PARAM, "Can not use prop");
		}

		OProp_SKILL.prop(card, skillDefines, 1);
	}
}
