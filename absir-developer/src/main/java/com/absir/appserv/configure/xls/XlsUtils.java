/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-24 下午12:05:11
 */
package com.absir.appserv.configure.xls;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.absir.appserv.configure.xls.value.XaWorkbook;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked" })
public abstract class XlsUtils {

	/**
	 * @return
	 */
	public static <T extends XlsBase> XlsDao<T, Serializable> getXlsDao(Class<T> xlsClass) {
		XlsDao<T, Serializable> xlsDao = XlsAccessorUtils.getXlsDao(xlsClass);
		if (xlsDao == null) {
			synchronized (xlsClass) {
				xlsDao = XlsAccessorUtils.getXlsDao(xlsClass);
				if (xlsDao == null) {
					try {
						reloadXlsDao(xlsClass);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						return null;
					}

					xlsDao = XlsAccessorUtils.getXlsDao(xlsClass);
				}
			}
		}

		return xlsDao;
	}

	/**
	 * @param xlsClass
	 * @throws IOException
	 */
	public static <T extends XlsBase> void reloadXlsDao(Class<T> xlsClass) throws IOException {
		synchronized (xlsClass) {
			XlsBase xlsBase = KernelClass.newInstance(xlsClass);
			XaWorkbook xaWorkbook = xlsClass.getAnnotation(XaWorkbook.class);
			String workbook = xaWorkbook == null || KernelString.isEmpty(xaWorkbook.workbook()) ? xlsClass.getSimpleName() : xaWorkbook.workbook();
			getXlsBeans(xlsBase.getHssfWorkbook(workbook), xaWorkbook == null ? null : xaWorkbook.sheets(), xlsClass, xlsBase);
		}
	}

	/**
	 * @param xlsClass
	 */
	public static <T extends XlsBase> void clearXlsDao(Class<T> xlsClass) {
		XlsAccessorUtils.clearXlsDao(xlsClass);
	}

	/**
	 * @param xlsClass
	 * @param id
	 * @return
	 */
	public static <T extends XlsBase> T getXlsBean(Class<T> xlsClass, Serializable id) {
		return getXlsDao(xlsClass).get(id);
	}

	/**
	 * @param xlsClass
	 * @param id
	 * @return
	 */
	public static <T extends XlsBase> T findXlsBean(Class<T> xlsClass, Object id) {
		return getXlsDao(xlsClass).find(id);
	}

	/**
	 * @param hssfSheet
	 * @return
	 */
	public static <T extends XlsBase> Collection<T> getXlsBeans(Class<T> xlsClass) {
		return getXlsDao(xlsClass).getAll();
	}

	/** XLS_BASE */
	public static final XlsBase XLS_BASE = new XlsBase();

	/**
	 * @param workbook
	 * @param beanClass
	 * @return
	 */
	public static <T extends XlsBase> Collection<T> getXlsBeans(HSSFWorkbook workbook, Class<T> beanClass) {
		return getXlsBeans(workbook, null, beanClass);
	}

	/**
	 * @param workbook
	 * @param sheets
	 * @param beanClass
	 * @return
	 */
	public static <T extends XlsBase> Collection<T> getXlsBeans(HSSFWorkbook workbook, int[] sheets, Class<T> beanClass) {
		return getXlsList(workbook, sheets, beanClass, KernelClass.newInstance(beanClass), false);
	}

	/**
	 * @param workbook
	 * @param sheets
	 * @param beanClass
	 * @param xlsBase
	 * @return
	 */
	public static <T extends XlsBase> Collection<T> getXlsBeans(HSSFWorkbook workbook, int[] sheets, Class<T> beanClass, XlsBase xlsBase) {
		return getXlsList(workbook, sheets, beanClass, xlsBase, true);
	}

	/**
	 * @param workbook
	 * @param beanClass
	 * @return
	 */
	public static <T> List<T> getXlsList(HSSFWorkbook workbook, Class<T> beanClass) {
		return getXlsList(workbook, null, beanClass, XLS_BASE, false);
	}

	/**
	 * @param workbook
	 * @param sheets
	 * @param beanClass
	 * @param xlsBase
	 * @param cacheable
	 * @return
	 */
	public static <T> List<T> getXlsList(HSSFWorkbook workbook, int[] sheets, Class<T> beanClass, XlsBase xlsBase, boolean cacheable) {
		if (xlsBase == null) {
			xlsBase = XLS_BASE;
		}

		return XlsAccessorUtils.getXlsBeans(workbook, sheets, beanClass, xlsBase, cacheable);
	}

	/**
	 * @param beanClass
	 * @return
	 */
	public static HSSFWorkbook getWorkbook(Class<? extends XlsBase> beanClass) {
		return getWorkbook(null, beanClass, null, KernelClass.newInstance(beanClass));
	}

	/**
	 * @param beans
	 * @return
	 */
	public static HSSFWorkbook getWorkbook(List<? extends XlsBase> beans) {
		return getWorkbook(null, beans);
	}

	/**
	 * @param beanName
	 * @param beans
	 * @return
	 */
	public static <T extends XlsBase> HSSFWorkbook getWorkbook(String beanName, Collection<T> beans) {
		T xlsBase = null;
		for (Object bean : beans) {
			if (bean != null) {
				xlsBase = (T) bean;
				break;
			}
		}

		if (xlsBase == null) {
			return null;
		}

		return getWorkbook(beanName, (Class<T>) xlsBase.getClass(), beans, xlsBase);
	}

	/**
	 * @param beans
	 * @param xlsBase
	 * @return
	 */
	public static HSSFWorkbook getWorkbook(Collection<Object> beans, XlsBase xlsBase) {
		return getWorkbook(null, beans, xlsBase);
	}

	/**
	 * @param beanName
	 * @param beans
	 * @param xlsBase
	 * @return
	 */
	public static <T> HSSFWorkbook getWorkbook(String beanName, Collection<T> beans, XlsBase xlsBase) {
		Class<T> beanClass = null;
		for (Object bean : beans) {
			if (bean != null) {
				beanClass = (Class<T>) bean.getClass();
				break;
			}
		}

		if (beanClass == null) {
			return null;
		}

		return getWorkbook(beanName, beanClass, beans, xlsBase);
	}

	/**
	 * @param beanName
	 * @param beanClass
	 * @param beans
	 * @param xlsBase
	 * @return
	 */
	public static <T> HSSFWorkbook getWorkbook(String beanName, Class<T> beanClass, Collection<T> beans, XlsBase xlsBase) {
		if (xlsBase == null) {
			xlsBase = XLS_BASE;
		}

		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		XlsAccessorUtils.writeHssfWorkbook(hssfWorkbook, beanClass, beans, xlsBase);
		return hssfWorkbook;
	}
}
