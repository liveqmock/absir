/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-5 下午7:51:15
 */
package com.absir.appserv.feature.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.JMenu;
import com.absir.core.kernel.KernelList;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
public class MenuBeanRoot {

	/** menuBean */
	private JMenu menuBean;

	/** children */
	private Map<String, MenuBeanRoot> children = new HashMap<String, MenuBeanRoot>();

	/**
	 * @return the menuBean
	 */
	public JMenu getMenuBean() {
		return menuBean;
	}

	/**
	 * @param menuBean
	 *            the menuBean to set
	 */
	public void setMenuBean(JMenu menuBean) {
		this.menuBean = menuBean;
	}

	/**
	 * @return the children
	 */
	public Map<String, MenuBeanRoot> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(Map<String, MenuBeanRoot> children) {
		this.children = children;
	}

	/**
	 * @return
	 */
	public List<JMenu> getMenuBeans() {
		List<JMenu> menuBeans = new ArrayList<JMenu>();
		for (Entry<String, MenuBeanRoot> entry : children.entrySet()) {
			JMenu menuBean = entry.getValue().menuBean;
			menuBeans.add(menuBean);
			menuBean.setName(entry.getKey());
			if (entry.getValue().children.size() > 0) {
				menuBean.setChildren(entry.getValue().getMenuBeans());
			}
		}

		KernelList.sortOrderable(menuBeans);
		return menuBeans;
	}

	/**
	 * @param name
	 * @return
	 */
	public MenuBeanRoot getChildrenRoot(String name) {
		return getChildrenRoot(name, 0, null, null, null);
	}

	/**
	 * @param name
	 * @param order
	 * @param ref
	 * @param url
	 * @param type
	 * @return
	 */
	public MenuBeanRoot getChildrenRoot(String name, int order, String ref, String url, String type) {
		if (name == null) {
			return this;
		}

		MenuBeanRoot menuBeanRoot = children.get(name);
		if (menuBeanRoot == null) {
			menuBeanRoot = new MenuBeanRoot();
			children.put(name, menuBeanRoot);
			menuBeanRoot.setMenuBean(new JMenu());
			menuBeanRoot.menuBean.setName(name);
			menuBeanRoot.menuBean.setOrdinal(order);
			menuBeanRoot.menuBean.setRef(ref);
			menuBeanRoot.menuBean.setUrl(url);
			menuBeanRoot.menuBean.setType(type);

		} else {
			if (menuBeanRoot.menuBean.getOrder() >= order) {
				if (order < menuBeanRoot.menuBean.getOrder()) {
					menuBeanRoot.menuBean.setOrdinal(order);
				}

				if (ref != null && menuBeanRoot.menuBean.getRef() == null) {
					menuBeanRoot.menuBean.setRef(ref);
				}

				if (url != null && menuBeanRoot.menuBean.getUrl() == null) {
					menuBeanRoot.menuBean.setUrl(url);
				}

				if (type != null && menuBeanRoot.menuBean.getType() == null) {
					menuBeanRoot.menuBean.setType(type);
				}
			}
		}

		return menuBeanRoot;
	}

	/**
	 * @param maMenu
	 * @param name
	 * @return
	 */
	public MenuBeanRoot getChildrenRoot(MaMenu maMenu, String name) {
		return getChildrenRoot(maMenu, name, null, null);
	}

	/**
	 * @param maMenu
	 * @param name
	 * @param ref
	 * @param url
	 * @return
	 */
	public MenuBeanRoot getChildrenRoot(MaMenu maMenu, String name, String ref, String url) {
		return getChildrenRoot(maMenu, name, 0, ref, url);
	}

	/**
	 * @param maMenu
	 * @param name
	 * @param order
	 * @param ref
	 * @param url
	 * @return
	 */
	public MenuBeanRoot getChildrenRoot(MaMenu maMenu, String name, int order, String ref, String url) {
		return getChildrenRoot(maMenu, name, null, order, ref, url, null);
	}

	/**
	 * @param maMenu
	 * @param name
	 * @param suffix
	 * @param order
	 * @param ref
	 * @param url
	 * @param type
	 * @return
	 */
	public MenuBeanRoot getChildrenRoot(MaMenu maMenu, String name, String suffix, int order, String ref, String url, String type) {
		String caption = name;
		if (maMenu != null) {
			if (!KernelString.isEmpty(maMenu.value())) {
				caption = maMenu.value();
				if (suffix != null) {
					caption = caption.replace("?", suffix);
				}
			}

			if (maMenu.order() != 0) {
				order = maMenu.order();
			}

			if (!KernelString.isEmpty(maMenu.ref())) {
				ref = maMenu.ref();
			}
		}

		if (caption == name && caption != null && suffix != null) {
			caption += suffix;
		}

		return getChildrenRoot(caption, order, ref, url, type);
	}
}
