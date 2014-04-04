/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-5 下午2:34:52
 */
package com.absir.appserv.game.value;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OBuff_HP_P extends OBuffRound<OCard> {

	// 生命恢复比例
	float hpP;

	/**
	 * @return the hpP
	 */
	public float getHpP() {
		return hpP;
	}

	/**
	 * @param hpP
	 *            the hpP to set
	 */
	public void setHpP(float hpP) {
		this.hpP = hpP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffRound#stepRound(com.absir.appserv.game
	 * .value.OObject, long, int, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void stepRound(OCard object, long time, int round, IResult result) {
		// TODO Auto-generated method stub
		if (hpP > 0) {
			object.treat(null, (int) (object.getMaxHp() * hpP), null, result);

		} else {
			object.damage(null, (int) -(object.getMaxHp() * hpP), null, result);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffReverse#revert(com.absir.appserv.game
	 * .value.OObject, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void revert(OCard object, IResult result) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuff#effect(com.absir.appserv.game.value
	 * .OObject, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void effect(OCard object, IResult result) {
		// TODO Auto-generated method stub
	}

}
