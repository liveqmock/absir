/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-18 上午9:57:48
 */
package com.absir.appserv.game.value;

import com.absir.core.kernel.KernelLang.CloneTemplate;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes" })
public abstract class OSkill<T extends OObject> implements CloneTemplate<OSkill<T>> {

	/**
	 * 释放技能
	 * 
	 * @param self
	 * @param result
	 * @return
	 */
	public final boolean cast(T self, IResult result) {
		if (isTrigger(self, result)) {
			clone().effect(self, result);
			return true;
		}

		return false;
	}

	/**
	 * 触发判断
	 * 
	 * @param self
	 * @param result
	 * @return
	 */
	public boolean isTrigger(T self, IResult result) {
		return result.getResult() == EResult.CONTINUE;
	}

	/**
	 * 技能生效
	 * 
	 * @param oCard
	 * @param oFight
	 */
	public abstract void effect(T self, IResult result);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public OSkill<T> clone() {
		return this;
	}
}
