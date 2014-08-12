/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 下午6:37:40
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.JPlayer;

/**
 * @author absir
 * 
 */
/**
 * @author absir
 * 
 */
public class OPlayerCorrect extends OPlayer {

	/** correct */
	private boolean correct;

	/**
	 * @param player
	 */
	public OPlayerCorrect(JPlayer player, boolean correct) {
		super(player);
		this.correct = correct;
	}

	/**
	 * @return the correct
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * @param correct
	 *            the correct to set
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
