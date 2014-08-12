/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:36:09
 */
package com.absir.appserv.client.context.value;

import java.util.List;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.client.context.value.OFightBase.OCardBase;
import com.absir.appserv.game.value.IResult;
import com.absir.appserv.game.value.OEffect;

/**
 * @author absir
 * 
 */
public abstract class OEffectBase extends OEffect {

	/**
	 * @param parameters
	 */
	public OEffectBase(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param effectName
	 */
	protected void setEffectName(String effectName) {
		this.effectName = effectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OEffect#generateEffectName()
	 */
	@Override
	protected String generateEffectName() {
		return null;
	}

	/**
	 * @param camp
	 * @param self
	 * @param targetCards
	 * @param targetType
	 * @param targetCount
	 * @param result
	 */
	public abstract void effect(JeCamp camp, OCardBase self, List<OCardBase> targetCards, boolean targetType, int targetCount, IResult result);

}
