/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-28 上午10:33:51
 */
package com.absir.appserv.game.value;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class OBuff_ATK_R extends OBuffRound<OCard> implements IBuffReBound<Object, OCard<OCard, OFight>> {

	// 伤害反弹比例
	private float atkR;

	/**
	 * @return the atkR
	 */
	public float getAtkR() {
		return atkR;
	}

	/**
	 * @param atkR
	 *            the atkR to set
	 */
	public void setAtkR(float atkR) {
		this.atkR = atkR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.IBuffFrom#supportsFrom(java.lang.Object)
	 */
	@Override
	public boolean supportsFrom(Object from) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.IBuffReBound#reBound(com.absir.appserv.game
	 * .value.OObject, com.absir.appserv.game.value.OObject, int,
	 * java.lang.Object, com.absir.appserv.game.value.IResult)
	 */
	@Override
	public int reBound(OCard<OCard, OFight> self, OCard<OCard, OFight> target, int damage, Object damageFrom, IResult result) {
		// TODO Auto-generated method stub
		int round = getRound();
		if (round < 0) {
			if (++round == 0) {
				result.setDone(true);
			}

			setRound(round);
		}

		int damageR = (int) (damage * atkR);
		target.damage(null, damageR, null, result);
		return damage - damageR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffRound#stepRound(com.absir.appserv.game
	 * .value.OObject, long, int, com.absir.appserv.game.value.IResult)
	 */
	@Override
	public void stepRound(OCard self, long time, int round, IResult result) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffReverse#revert(com.absir.appserv.game
	 * .value.OObject, com.absir.appserv.game.value.IResult)
	 */
	@Override
	public void revert(OCard self, IResult result) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuff#effect(com.absir.appserv.game.value
	 * .OObject, com.absir.appserv.game.value.IResult)
	 */
	@Override
	public void effect(OCard self, IResult result) {
		// TODO Auto-generated method stub

	}

}
