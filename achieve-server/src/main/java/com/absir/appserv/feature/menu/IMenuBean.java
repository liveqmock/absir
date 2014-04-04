/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-29 下午1:08:10
 */
package com.absir.appserv.feature.menu;

import java.util.Collection;

import com.absir.appserv.feature.menu.value.MeUrlType;
import com.absir.core.kernel.KernelList.Orderable;

/**
 * @author absir
 * 
 */
public interface IMenuBean extends Orderable {

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @return
	 */
	public int getOrder();

	/**
	 * @return
	 */
	public String getUrl();

	/**
	 * @return
	 */
	public String getRef();

	/**
	 * @return
	 */
	public MeUrlType getUrlType();

	/**
	 * @return
	 */
	public Collection<? extends IMenuBean> getChildren();

}
