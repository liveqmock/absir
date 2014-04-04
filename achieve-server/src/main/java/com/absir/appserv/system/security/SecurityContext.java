/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-12 下午5:32:11
 */
package com.absir.appserv.system.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.context.core.ContextBean;
import com.absir.context.core.ContextUtils;

/**
 * @author absir
 * 
 */
public class SecurityContext extends ContextBean<String> {

	/** user */
	private JiUserBase user;

	/** metas */
	private Map<String, Serializable> metas;

	/** lifeTime */
	private long lifeTime;

	/** maxExpirationTime */
	private long maxExpirationTime = 0;

	/**
	 * @return the user
	 */
	public JiUserBase getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(JiUserBase user) {
		this.user = user;
	}

	/**
	 * @param name
	 * @return
	 */
	public Serializable getMeta(String name) {
		return metas == null ? null : metas.get(name);
	}

	/**
	 * @param name
	 */
	public void removeMeta(String name) {
		if (metas != null) {
			metas.remove(name);
		}
	}

	/**
	 * @return the meta
	 */
	public void setMeta(String name, Serializable value) {
		if (metas == null) {
			synchronized (this) {
				metas = new HashMap<String, Serializable>();
			}
		}

		metas.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextBean#getLifeTime()
	 */
	@Override
	protected long getLifeTime() {
		return lifeTime;
	}

	/**
	 * @param lifeTime
	 *            the lifeTime to set
	 */
	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

	/**
	 * @return the maxExpirationTime
	 */
	public long getMaxExpirationTime() {
		return maxExpirationTime;
	}

	/**
	 * @param maxExpirationTime
	 *            the maxExpirationTime to set
	 */
	public void setMaxExpirationTime(long maxExpirationTime) {
		if (maxExpirationTime == 0) {
			maxExpirationTime = -1;
		}

		this.maxExpirationTime = maxExpirationTime + ContextUtils.getContextTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.IContext#retainAt(long)
	 */
	@Override
	public void retainAt(long contextTime) {
		// TODO Auto-generated method stub
		super.retainAt(contextTime);
		if (maxExpirationTime > 0) {
			maxExpirationTime += contextTime;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.IContext#stepDone(long)
	 */
	@Override
	public boolean stepDone(long contextTime) {
		// TODO Auto-generated method stub
		return maxExpirationTime != 0 && (super.stepDone(contextTime) || (maxExpirationTime > 0 && maxExpirationTime < contextTime));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.Context#initialize()
	 */
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
	}
}
