/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-6 下午9:07:41
 */
package com.absir.appserv.game.value;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class OBuff_Invincible extends OBuffRound<OCard> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuff#against(com.absir.appserv.game.value
	 * .OBuff)
	 */
	/*
	 * public int against(OBuff buff) { if (getClass() != buff.getClass()) {
	 * return 0; }
	 * 
	 * return getRound() >= ((OBuff_Invincible) buff).getRound() ? 1 : -1; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffRound#stepRound(com.absir.appserv.game
	 * .value.OObject, long, int, com.absir.appserv.game.value.IResult)
	 */
	@Override
	public void stepRound(OCard self, long time, int round, IResult buffResult) {
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
		self.setInvincible(false);
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
		self.setInvincible(true);
	}

}
