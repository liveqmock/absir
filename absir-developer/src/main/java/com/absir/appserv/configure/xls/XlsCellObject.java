/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-25 下午3:42:17
 */
package com.absir.appserv.configure.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * @author absir
 * 
 */
public class XlsCellObject extends XlsCellBase {

	/** obj */
	private Object obj;

	/** xlsBase */
	private XlsBase xlsBase;

	/**
	 * @param obj
	 * @param xlsBase
	 */
	public XlsCellObject(Object obj, XlsBase xlsBase) {
		this.obj = obj;
		this.xlsBase = xlsBase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.configure.xls.XlsCell#wirteHssfCell(org.apache.poi.
	 * hssf.usermodel.HSSFCell)
	 */
	@Override
	public void wirteHssfCell(HSSFCell hssfCell) {
		// TODO Auto-generated method stub
		if (obj != null) {
			xlsBase.write(hssfCell, obj.toString());
		}
	}
}
