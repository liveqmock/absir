/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-24 上午10:27:47
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.core.kernel.KernelDyna;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 * 
 */
public class OProp_EVOLUTE implements IPropEvolute {

	/** camp */
	private JeCamp camp;

	/** luck */
	private float luck;

	/**
	 * @param propDefine
	 */
	public OProp_EVOLUTE(XPropDefine propDefine) {
		camp = propDefine.getCamp();
		luck = KernelDyna.to(propDefine.getPropParmas()[0], float.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.client.context.value.IPropEvolute#getLuck(com.absir
	 * .appserv.client.bean.JCard)
	 */
	@Override
	public float getLuck(JCard card) {
		// TODO Auto-generated method stub
		if (camp != null || camp != card.getCardDefine().getCamp()) {
			throw new ServerException(ServerStatus.NO_PARAM);
		}

		return luck;
	}

}
