/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-27 下午4:23:28
 */
package com.absir.appserv.configure.xls;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * @author absir
 * 
 */
public class XlsAccessorCollection extends XlsAccessorArray {

	/**
	 * @param field
	 * @param beanClass
	 * @param xlsBase
	 */
	public XlsAccessorCollection(Field field, Class<?> beanClass, XlsBase xlsBase) {
		super(field, beanClass, xlsBase);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.configure.xls.XlsAccessor#writeXlsCells(java.util
	 * .List, java.lang.Object, com.absir.appserv.configure.xls.XlsBase)
	 */
	@Override
	public void writeXlsCells(List<XlsCell> xlsCells, Object obj, XlsBase xlsBase) {
		if (obj != null) {
			obj = getAccessor().get(obj);
		}

		XlsCell xlsCell = new XlsCell();
		xlsCells.add(xlsCell);
		if (obj == null) {
			super.writeXlsCells(xlsCell.addColumnList(null), null, accessors, xlsBase);

		} else {
			for (Object o : (Collection<?>) obj) {
				super.writeXlsCells(xlsCell.addColumnList(null), o, accessors, xlsBase);
			}
		}
	}
}
