/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-4 下午8:08:17
 */
package com.absir.appserv.feature.menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;

import org.hibernate.SessionFactory;

import com.absir.appserv.crud.ICrudSupply;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaFactory;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.feature.menu.value.MaSupply;
import com.absir.appserv.feature.menu.value.MeUrlType;
import com.absir.appserv.lang.LangBundleImpl;
import com.absir.appserv.support.Developer;
import com.absir.appserv.system.admin.AdminServer;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.helper.HelperLang;
import com.absir.appserv.system.service.utils.SecurityServiceUtils;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.bean.inject.value.Value;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelLang.BreakException;
import com.absir.core.kernel.KernelLang.FilterTemplate;
import com.absir.core.kernel.KernelString;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JaEntity;
import com.absir.server.in.InDispatcher;
import com.absir.server.route.RouteMatcher;

/**
 * @author absir
 * 
 */
@Inject
public abstract class MenuContextUtils {

	/** CONTEXT */
	protected static final MenuContext CONTEXT = BeanFactoryUtils.get(MenuContext.class);

	/**
	 * @author absir
	 *
	 */
	@Bean
	protected static class MenuContext {
		/** App_Name */
		@Value(value = "app.name")
		protected String App_Name;

		/** Site_Route */
		@Value(value = "site.route")
		protected String Site_Route;

		/** Admin_Route */
		protected String Admin_Route;

		/** menuBeanService */
		@Inject
		protected MenuBeanService menuBeanService;

		/**
		 * @param servletContext
		 */
		@Inject(type = InjectType.Selectable)
		protected void setServletContext(ServletContext servletContext) {
			// 全局链接参数
			if (App_Name == null) {
				App_Name = servletContext.getServletContextName();
			}

			if (Site_Route == null) {
				Site_Route = servletContext.getContextPath();
			}

			Admin_Route = Site_Route + "/" + AdminServer.getRoute();
			servletContext.setAttribute("app_name", App_Name);
			servletContext.setAttribute("site_route", Site_Route);
			servletContext.setAttribute("admin_route", Admin_Route);

			if (Developer.isDeveloper()) {
				// 初始化菜单
				MenuBeanRoot menuBeanRoot = new MenuBeanRoot();
				// 扫瞄后台菜单
				proccessMenuRoot(AdminServer.getRoute(), menuBeanRoot, new FilterTemplate<RouteMatcher>() {

					@Override
					public boolean doWith(RouteMatcher template) throws BreakException {
						// TODO Auto-generated method stub
						return AdminServer.class.isAssignableFrom(template.getRouteAction().getRouteEntity().getRouteType());
					}

				});

				// 权限实体收集
				List<String> entityNames = Developer.isDeveloper() ? new ArrayList<String>() : null;
				// 实体CRUD菜单
				Set<Object> entityClasses = new HashSet<Object>();
				for (Entry<String, Entry<Class<?>, SessionFactory>> entry : SessionFactoryUtils.get().getJpaEntityNameMapEntityClassFactory().entrySet()) {
					Class<?> entityClass = entry.getValue().getKey();
					if (entityClasses.add(entityClass)) {
						addMenuBeanRoot(menuBeanRoot, entry.getKey(), entityClass, LangBundleImpl.ME.getunLang("内容管理", MenuBeanRoot.TAG), "content",
								LangBundleImpl.ME.getunLang("列表", MenuBeanRoot.TAG), "list", entityNames);
					}
				}

				// 配置CRUD菜单
				for (ICrudSupply crudSupply : BeanFactoryUtils.get().getBeanObjects(ICrudSupply.class)) {
					Set<Entry<String, Class<?>>> entityNameMapClass = crudSupply.getEntityNameMapClass();
					if (entityNameMapClass != null) {
						MaSupply maSupply = KernelClass.fetchAnnotation(crudSupply.getClass(), MaSupply.class);
						if (maSupply != null) {
							for (Entry<String, Class<?>> entry : entityNameMapClass) {
								addMenuBeanRoot(menuBeanRoot, entry.getKey(), entry.getValue(), maSupply.folder(), maSupply.icon(), maSupply.name(), maSupply.method(), entityNames);
							}
						}
					}
				}

				// 添加实体权限
				menuBeanService.addEntityPermission(entityNames);

				// 添加后台菜单
				menuBeanService.addMenuBeanRoot(menuBeanRoot, "admin", LangBundleImpl.ME.getunLang("后台菜单", MenuBeanRoot.TAG), MeUrlType.ADMIN);
			}
		}
	}

	/**
	 * @return
	 */
	public static MenuBeanService getService() {
		return CONTEXT.menuBeanService;
	}

	/**
	 * @return the app_Name
	 */
	public static String getAppName() {
		return CONTEXT.App_Name;
	}

	/**
	 * @return the site_Route
	 */
	public static String getSiteRoute() {
		return CONTEXT.Site_Route;
	}

	/**
	 * @return the admin_Route
	 */
	public static String getAdminRoute() {
		return CONTEXT.Admin_Route;
	}

	/**
	 * @param 获取别名菜单
	 * @return
	 */
	public static List<OMenuBean> getMenuBeans(String cite) {
		return getMenuBeans(cite, SecurityServiceUtils.getUserBase());
	}

	/**
	 * @param cite
	 * @param user
	 * @return
	 */
	public static List<OMenuBean> getMenuBeans(String cite, JiUserBase user) {
		return CONTEXT.menuBeanService.getMenuBeans(cite, user, 3);
	}

	/**
	 * @param url
	 * @param urlType
	 * @return
	 */
	public static String getUrl(String url, MeUrlType urlType) {
		if (url == null) {
			return KernelLang.NULL_STRING;
		}

		String route = urlType == null ? null : urlType.getRoute();
		return route == null ? url : route + url;
	}

	/**
	 * 添加权限菜单
	 * 
	 * @param menuBeanRoot
	 * @param entityName
	 * @param entityClass
	 * @param menuName
	 * @param menuIcon
	 * @param order
	 * @param ref
	 * @param url
	 * @param name
	 * @param suffix
	 * @param option
	 * @param parent
	 * @param menu
	 */
	public static void addMenuBeanRoot(MenuBeanRoot menuBeanRoot, String entityName, Class<?> entityClass, String menuName, String menuIcon, int order, String ref, String url, String name,
			String suffix, String option, MaMenu[] parent, MaMenu menu) {
		int length = parent == null ? 0 : parent.length;
		int index = length - 2;
		menuBeanRoot = menuBeanRoot.getChildrenRoot(index >= 0 ? parent[index] : null, menuName, menuIcon);
		String entityCaption = HelperLang.getTypeCaption(entityClass, entityName);
		menuBeanRoot = menuBeanRoot.getChildrenRoot(++index >= 0 ? parent[index] : null, entityCaption, null);
		while (++index < length) {
			menuBeanRoot = menuBeanRoot.getChildrenRoot(parent[index], null, null);
		}

		if (KernelString.isEmpty(url)) {
			menuBeanRoot = menuBeanRoot.getChildrenRoot(name, order, ref, url, KernelString.isEmpty(ref) ? null : "MENU", menuIcon);

		} else {
			entityCaption = KernelString.isEmpty(name) ? entityCaption : LangBundleImpl.ME.getunLang(name, MenuBeanRoot.TAG);
			option = LangBundleImpl.ME.getunLang(option, MenuBeanRoot.TAG);
			menuBeanRoot.getChildrenRoot(menu, entityCaption, suffix, 0, entityName, "/entity/" + option + "/" + entityName, "ENTITY", menuIcon);
		}
	}

	/**
	 * 添加实体菜单
	 * 
	 * @param menuBeanRoot
	 * @param entityName
	 * @param entityClass
	 * @param menuName
	 * @param menuIcon
	 * @param suffix
	 * @param option
	 * @param entityNames
	 */
	public static void addMenuBeanRoot(MenuBeanRoot menuBeanRoot, final String entityName, Class<?> entityClass, String menuName, String menuIcon, String suffix, String option,
			List<String> entityNames) {
		String entityCaption = null;
		MaEntity maEntity = KernelClass.fetchAnnotation(entityClass, MaEntity.class);
		if (maEntity != null && !maEntity.closed()) {
			entityCaption = HelperLang.getTypeCaption(entityClass, entityName);
			int index = maEntity.parent().length - 2;
			menuBeanRoot = menuBeanRoot.getChildrenRoot(index >= 0 ? maEntity.parent()[index] : null, menuName, menuIcon);
			menuBeanRoot = menuBeanRoot.getChildrenRoot(++index >= 0 ? maEntity.parent()[index] : null, entityCaption, null);
			while (++index < maEntity.parent().length) {
				menuBeanRoot = menuBeanRoot.getChildrenRoot(maEntity.parent()[index], null, null);
			}

			entityCaption = KernelString.isEmpty(maEntity.name()) ? entityCaption : LangBundleImpl.ME.getunLang(maEntity.name(), MenuBeanRoot.TAG);
			option = LangBundleImpl.ME.getunLang(option, MenuBeanRoot.TAG);
			menuBeanRoot.getChildrenRoot(maEntity.value(), entityCaption, suffix, 0, entityName, "/entity/" + option + "/" + entityName, "ENTITY", null);
		}

		// 添加实体权限控制
		if (entityNames != null && (maEntity != null || entityClass.getAnnotation(JaEntity.class) != null)) {
			if (entityCaption == null) {
				entityCaption = HelperLang.getTypeCaption(entityClass, entityName);
			}

			entityNames.add(entityName);
			entityNames.add(entityCaption);
		}
	}

	/**
	 * 处理收集链接
	 * 
	 * @param route
	 * @param menuBeanRoot
	 * @param filter
	 */
	public static void proccessMenuRoot(String route, MenuBeanRoot menuBeanRoot, FilterTemplate<RouteMatcher> filter) {
		try {
			for (RouteMatcher routeMatcher : InDispatcher.getRouteAdapter().getRouteMatchers()) {
				if (filter == null || filter.doWith(routeMatcher)) {
					MaFactory maFactory = KernelClass.getAnnotation(routeMatcher.getRouteAction().getRouteMethod().getMethod(), MaFactory.class);
					if (maFactory != null) {
						IMenuFactory menuFactory = BeanFactoryUtils.getRegisterBeanObject(maFactory.value(), IMenuFactory.class, maFactory.factory());
						if (menuFactory != null) {
							menuFactory.proccess(route, menuBeanRoot, routeMatcher, maFactory);
						}
					}
				}
			}

		} catch (BreakException e) {
			// TODO Auto-generated catch block
		}
	}
}
