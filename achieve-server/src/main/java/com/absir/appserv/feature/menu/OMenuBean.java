/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-29 下午5:13:24
 */
package com.absir.appserv.feature.menu;

import java.util.Collection;
import java.util.List;

import com.absir.appserv.feature.menu.value.MeUrlType;

/**
 * @author absir
 * 
 */
public class OMenuBean implements IMenuBean {

	/** menuBean */
	private IMenuBean menuBean;

	/** url */
	private String url;

	/** children */
	private List<? extends OMenuBean> children;

	/**
	 * @param menuBean
	 */
	public OMenuBean(IMenuBean menuBean) {
		this.menuBean = menuBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.menu.IMenuBean#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return menuBean.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.menu.IMenuBean#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return menuBean.getOrder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.menu.IMenuBean#getUrl()
	 */
	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		if (url == null) {
			url = MenuContextUtils.getUrl(getBaseUrl(), getUrlType());
		}

		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.menu.IMenuBean#getRef()
	 */
	@Override
	public String getRef() {
		// TODO Auto-generated method stub
		return menuBean.getRef();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.menu.IMenuBean#getUrlType()
	 */
	@Override
	public MeUrlType getUrlType() {
		// TODO Auto-generated method stub
		return menuBean.getUrlType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.menu.IMenuBean#getChildren()
	 */
	@Override
	public Collection<? extends OMenuBean> getChildren() {
		// TODO Auto-generated method stub
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<? extends OMenuBean> children) {
		this.children = children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.feature.menu.IMenuBean#getUrl()
	 */
	public String getBaseUrl() {
		return menuBean.getUrl();
	}
}
