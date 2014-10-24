/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 下午4:33:13
 */
package com.absir.appserv.game.value;

import java.io.Serializable;

/**
 * 详细战报
 * 
 * @author absir
 * 
 */
public class OReportDetail {

	// 战报深度
	// private int depth;

	// 战报对象
	private Serializable self;

	// 目标对象
	private Serializable[] targets;

	// 战斗效果
	private String effect;

	// 战斗效果结果数据
	private Object effectData;

	/**
	 * 
	 */
	public OReportDetail() {
	}

	/**
	 * @param effectData
	 */
	public OReportDetail(Object effectData) {
		this();
		this.effectData = effectData;
	}

	/**
	 * @return the self
	 */
	public Serializable getSelf() {
		return self;
	}

	/**
	 * @param self
	 *            the self to set
	 */
	public void setSelf(Serializable self) {
		this.self = self;
	}

	/**
	 * @return the targets
	 */
	public Serializable[] getTargets() {
		return targets;
	}

	/**
	 * @param targets
	 *            the targets to set
	 */
	public void setTargets(Serializable[] targets) {
		this.targets = targets;
	}

	/**
	 * @return the effect
	 */
	public String getEffect() {
		return effect;
	}

	/**
	 * @param effect
	 *            the effect to set
	 */
	public void setEffect(String effect) {
		this.effect = effect;
	}

	/**
	 * @return the effectData
	 */
	public Object getEffectData() {
		return effectData;
	}

	/**
	 * @param effectData
	 *            the effectData to set
	 */
	public void setEffectData(Object effectData) {
		this.effectData = effectData;
	}
}
