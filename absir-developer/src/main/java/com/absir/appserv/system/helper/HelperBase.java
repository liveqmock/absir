/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-6 上午10:27:23
 */
package com.absir.appserv.system.helper;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.proxy.HibernateProxy;

import com.absir.appserv.system.bean.proxy.JiBase;

/**
 * @author absir
 * 
 */
public class HelperBase {

	/**
	 * @param base
	 * @return
	 */
	public static Serializable getLazyId(JiBase base) {
		if (base == null) {
			return null;
		}

		if (base instanceof HibernateProxy) {
			return ((HibernateProxy) base).getHibernateLazyInitializer().getIdentifier();

		} else {
			return base.getId();
		}
	}

	/**
	 * @param bases
	 * @return
	 */
	public static Serializable[] getBaseIds(JiBase[] bases) {
		int length = bases.length;
		Serializable[] ids = new Serializable[length];
		for (int i = 0; i < length; i++) {
			ids[i] = bases[i].getId();
		}

		return ids;
	}

	/**
	 * @param bases
	 * @return
	 */
	public static Serializable[] getBaseIds(Collection<? extends JiBase> bases) {
		Serializable[] ids = new Serializable[bases.size()];
		int i = -1;
		for (JiBase base : bases) {
			ids[++i] = base.getId();
		}

		return ids;
	}
}
