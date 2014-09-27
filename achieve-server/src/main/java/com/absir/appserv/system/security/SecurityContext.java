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

	/** securitySupply */
	private ISecuritySupply securitySupply;

	/** user */
	private JiUserBase user;

	/** address */
	private String address;

	/** agent */
	private String agent;

	/** metas */
	private Map<String, Serializable> metas;

	/** lifeTime */
	private long lifeTime;

	/** maxExpirationTime */
	private long maxExpirationTime;

	/** metaObjects */
	private Map<String, Object> metaObjects;

	/**
	 * @return the securitySupply
	 */
	public ISecuritySupply getSecuritySupply() {
		return securitySupply;
	}

	/**
	 * @param securitySupply
	 *            the securitySupply to set
	 */
	public void setSecuritySupply(ISecuritySupply securitySupply) {
		this.securitySupply = securitySupply;
	}

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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @return the metas
	 */
	public Map<String, Serializable> getMetas() {
		return metas;
	}

	/**
	 * @param metas
	 *            the metas to set
	 */
	public void setMetas(Map<String, Serializable> metas) {
		this.metas = metas;
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
				if (metas == null) {
					metas = new HashMap<String, Serializable>();
				}
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

		this.maxExpirationTime = ContextUtils.getContextTime() + maxExpirationTime;
	}

	/**
	 * @param name
	 * @return
	 */
	public Object getMetaObject(String name) {
		return metaObjects == null ? null : metaObjects.get(name);
	}

	/**
	 * @param name
	 */
	public void removeMetaObjects(String name) {
		if (metaObjects != null) {
			metaObjects.remove(name);
		}
	}

	/**
	 * @return the meta
	 */
	public void setMetaObjects(String name, Object value) {
		if (metaObjects == null) {
			synchronized (this) {
				if (metaObjects == null) {
					metaObjects = new HashMap<String, Object>();
				}
			}
		}

		metaObjects.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.IContext#stepDone(long)
	 */
	@Override
	public boolean stepDone(long contextTime) {
		// TODO Auto-generated method stub
		return maxExpirationTime != 0 && (maxExpirationTime < contextTime || super.stepDone(contextTime));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextBean#uninitializeDone()
	 */
	public boolean uninitializeDone() {
		// TODO Auto-generated method stub
		return securitySupply == null || user == null || maxExpirationTime <= ContextUtils.getContextTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextBean#uninitialize()
	 */
	@Override
	public void uninitialize() {
		// TODO Auto-generated method stub
		securitySupply.saveSession(this);
	}
}
