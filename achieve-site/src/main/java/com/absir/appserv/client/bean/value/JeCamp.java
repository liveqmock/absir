/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-17 上午11:11:26
 */
package com.absir.appserv.client.bean.value;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
public enum JeCamp {

	@JaLang("水")
	WATER

	{

		@Override
		public int getDefence(JCard card) {
			// TODO Auto-generated method stub
			return card.getWater();
		}

	},

	@JaLang("火")
	FIRE

	{

		@Override
		public int getDefence(JCard card) {
			// TODO Auto-generated method stub
			return card.getFire();
		}

	},

	@JaLang("雷")
	THUNDER {

		@Override
		public int getDefence(JCard card) {
			// TODO Auto-generated method stub
			return card.getThunder();
		}

	};

	public abstract int getDefence(JCard card);

	/**
	 * @param camp
	 * @param hp
	 * @return
	 */
	public float aioiGramsCamp(JeCamp camp) {
		if (this == camp) {
			return 1.0f;
		}

		int ordinal = ordinal() - camp.ordinal();
		return ordinal < 0 || ordinal >= JeCamp.THUNDER.ordinal() ? 1.25f : 0.75f;
	}
}
