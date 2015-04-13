/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-27 下午4:20:12
 */
package com.absir.appserv.configure.xls;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.absir.appserv.configure.xls.value.XaIgnore;
import com.absir.appserv.configure.xls.value.XaParam;
import com.absir.appserv.configure.xls.value.XaReferenced;
import com.absir.appserv.system.helper.HelperAccessor;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang.ObjectTemplate;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class XlsAccessorBean extends XlsAccessor {

	/** beanClass */
	protected Class<?> beanClass;

	/** accessors */
	protected List<XlsAccessor> accessors;

	/**
	 * @param field
	 */
	public XlsAccessorBean(Field field, Class<?> beanClass) {
		super(field, beanClass);
		this.beanClass = beanClass;
	}

	/**
	 * @param field
	 * @param beanClass
	 * @param xlsBase
	 */
	public XlsAccessorBean(Field field, Class<?> beanClass, XlsBase xlsBase) {
		this(field, beanClass);
		// TODO Auto-generated constructor stub
		if (!xlsBase.is(beanClass)) {
			XaReferenced xaReferenced = field.getAnnotation(XaReferenced.class);
			if (xaReferenced == null || xaReferenced.value()) {
				accessors = getXlsAccessors(beanClass, xlsBase);
			}
		}
	}

	/**
	 * @param beanClass
	 * @param xlsBase
	 * @return
	 */
	protected List<XlsAccessor> getXlsAccessors(final Class<?> beanClass, final XlsBase xlsBase) {
		final List<XlsAccessor> xlsAccessors = new ArrayList<XlsAccessor>();
		for (Field field : HelperAccessor.getFields(beanClass, XaIgnore.class)) {
			Class<?> type = field.getType();
			if (xlsBase.is(type)) {
				xlsAccessors.add(new XlsAccessor(field, beanClass));

			} else {
				if (type.isArray()) {
					Class<?> componentType = type.getComponentType();
					if ((field.getAnnotation(XaParam.class) != null && KernelClass.isBasicClass(componentType)) || XlsBase.class.isAssignableFrom(componentType)) {
						xlsAccessors.add(new XlsAccessorParam(field, componentType));

					} else {
						xlsAccessors.add(new XlsAccessorArray(field, componentType, xlsBase));
					}

				} else if (Collection.class.isAssignableFrom(type)) {
					xlsAccessors.add(new XlsAccessorCollection(field, KernelClass.componentClass(field.getGenericType()), xlsBase));

				} else if (Map.class.isAssignableFrom(type)) {
					Type[] types = KernelClass.typeArguments(field.getGenericType());
					Class<?> componentType = KernelClass.rawClass(types[0]);
					if (field.getAnnotation(XaParam.class) != null && (KernelClass.isBasicClass(componentType) || XlsBase.class.isAssignableFrom(componentType))) {
						xlsAccessors.add(new XlsAccessorParamMap(field, componentType, KernelClass.rawClass(types[1])));

					} else {
						xlsAccessors.add(new XlsAccessorMap(field, componentType, KernelClass.rawClass(types[1]), xlsBase));
					}

				} else {
					xlsAccessors.add(new XlsAccessorBean(field, type, xlsBase));
				}
			}
		}

		return xlsAccessors.size() == 0 ? null : xlsAccessors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.configure.xls.XlsAccessor#isMulti()
	 */
	@Override
	public boolean isMulti() {
		if (accessors != null) {
			for (XlsAccessor accessor : accessors) {
				if (accessor.isMulti()) {
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.configure.xls.XlsAccessor#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		if (accessors == null) {
			return 1;
		}

		int column = 0;
		for (XlsAccessor accessor : accessors) {
			column += accessor.getColumnCount();
		}

		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.configure.xls.XlsAccessor#readHssfSheet(org.apache
	 * .poi.hssf.usermodel.HSSFSheet, java.util.List, int, int, int)
	 */
	@Override
	public int readHssfSheet(HSSFSheet hssfSheet, List<Object> cells, int firstRow, int firstColumn, int lastRow) {
		if (accessors != null) {
			int iColumn = firstColumn;
			for (XlsAccessor accessor : accessors) {
				if (!accessor.isMulti()) {
					String value = XlsAccessorUtils.getCellValue(hssfSheet.getRow(firstRow).getCell(iColumn));
					for (int i = firstRow + 1; i < lastRow; i++) {
						HSSFRow row = hssfSheet.getRow(i);
						String next = row == null ? null : XlsAccessorUtils.getCellValue(row.getCell(iColumn));
						if (!(KernelString.isEmpty(next) || next.equals(value))) {
							lastRow = i;
						}
					}
				}

				iColumn += accessor.getColumnCount();
			}
		}

		return readHssfSheet(hssfSheet, cells, accessors, firstRow, firstColumn, lastRow);
	}

	/**
	 * @param hssfSheet
	 * @param cells
	 * @param accessors
	 * @param firstRow
	 * @param firstColumn
	 * @param lastRow
	 * @param readRow
	 * @return
	 */
	protected int readHssfSheet(HSSFSheet hssfSheet, List<Object> cells, List<XlsAccessor> accessors, int firstRow, int firstColumn, int lastRow) {
		if (accessors == null) {
			return super.readHssfSheet(hssfSheet, cells, firstRow, firstColumn, lastRow);

		} else {
			List<Object> list = new ArrayList<Object>();
			int iColumn = firstColumn;
			for (XlsAccessor accessor : accessors) {
				accessor.readHssfSheet(hssfSheet, list, firstRow, iColumn, lastRow);
				iColumn += accessor.getColumnCount();
			}

			cells.add(list);
			return lastRow;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.configure.xls.XlsAccessor#setObject(java.lang.Object ,
	 * java.lang.Object, com.absir.appserv.configure.xls.XlsBase,
	 * com.absir.core.kernel.KernelLang.ObjectTemplate)
	 */
	@Override
	public void setObject(Object obj, Object cell, XlsBase xlsBase, ObjectTemplate<Boolean> empty) {
		getAccessor().set(obj, readObject(beanClass, cell, accessors, xlsBase, empty));
	}

	/**
	 * @param beanClass
	 * @param cell
	 * @param accessors
	 * @param xlsBase
	 * @param empty
	 * @return
	 */
	protected Object readObject(Class<?> beanClass, Object cell, List<XlsAccessor> accessors, XlsBase xlsBase, ObjectTemplate<Boolean> empty) {
		if (accessors == null) {
			HSSFCell hssfCell = (HSSFCell) cell;
			if (empty != null && empty.object) {
				if (!KernelString.isEmpty(XlsAccessorUtils.getCellValue(hssfCell))) {
					empty.object = false;
				}
			}

			return xlsBase.read((HSSFCell) cell, beanClass);

		} else {
			Object bean = KernelClass.newInstance(beanClass);
			List<?> cells = (List<?>) cell;
			int size = accessors.size();
			for (int i = 0; i < size; i++) {
				accessors.get(i).setObject(bean, cells.get(i), xlsBase, empty);
			}

			return bean;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.configure.xls.XlsAccessorUtils.XlsAccessorBean#
	 * getHeader()
	 */
	@Override
	public XlsCell getHeader() {
		XlsCell xlsCell = new XlsCell();
		if (accessors != null) {
			List<XlsCell> cells = new ArrayList<XlsCell>();
			for (XlsAccessor accessor : accessors) {
				cells.add(accessor.getHeader());
			}

			xlsCell.addColumnList(cells);
		}

		return xlsCell;
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

		writeXlsCells(xlsCells, obj, accessors, xlsBase);
	}

	/**
	 * @param xlsCells
	 * @param obj
	 * @param accessors
	 * @param xlsBase
	 */
	protected void writeXlsCells(List<XlsCell> xlsCells, Object obj, List<XlsAccessor> accessors, XlsBase xlsBase) {
		if (obj == null) {
			super.writeXlsCells(xlsCells, obj, xlsBase);

		} else {
			if (accessors == null) {
				xlsCells.add(new XlsCellObject(obj, xlsBase));

			} else {
				XlsCell xlsCell = new XlsCell();
				List<XlsCell> cells = xlsCell.addColumnList(null);
				for (XlsAccessor accessor : accessors) {
					accessor.writeXlsCells(cells, obj, xlsBase);
				}

				xlsCells.add(xlsCell);
			}
		}
	}

}
