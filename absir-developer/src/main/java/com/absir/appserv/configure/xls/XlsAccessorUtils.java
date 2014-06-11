/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-25 下午3:57:49
 */
package com.absir.appserv.configure.xls;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import com.absir.appserv.system.bean.JEmbedSS;
import com.absir.appserv.system.bean.JUpdateXls;
import com.absir.appserv.system.bean.proxy.JiUpdate;
import com.absir.appserv.system.helper.HelperAccessor;
import com.absir.appserv.system.helper.HelperJson;
import com.absir.appserv.system.service.BeanService;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelReflect;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XlsAccessorUtils {

	/**
	 * @param hssfCell
	 * @return
	 */
	public static Object getCellObject(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return null;
		}

		if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return hssfCell.getNumericCellValue();
		}

		if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return hssfCell.getBooleanCellValue();
		}

		return hssfCell.toString();
	}

	/**
	 * @param hssfCell
	 * @param hssfDataFormatter
	 * @return
	 */
	public static String getCellValue(HSSFCell hssfCell) {
		return DynaBinder.to(getCellObject(hssfCell), String.class);
	}

	/**
	 * @param hssfSheet
	 * @return
	 */
	public static int[] accessorsSheet(HSSFSheet hssfSheet) {
		int[] accessors = new int[4];
		accessors[0] = 0;
		accessors[1] = 0;
		accessors[2] = hssfSheet.getLastRowNum() + 1;
		accessors[3] = accessors[2] <= 1 ? 0 : hssfSheet.getRow(0).getPhysicalNumberOfCells();
		return accessors;
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	public static String columnWithRow(int column, int row) {
		return column + "@" + row;
	}

	/**
	 * @param hssfWorkbook
	 * @param index
	 * @param orientation
	 * @return
	 */
	public static List<Object> getSheetList(HSSFWorkbook hssfWorkbook, int index, boolean orientation) {
		List<Object> sheetList = new ArrayList<Object>();
		int size = hssfWorkbook.getNumberOfSheets();
		for (int i = 0; i < size; i++) {
			if (index >= 0) {
				Map<String, Object> sheetRows = getSheetMap(hssfWorkbook.getSheetAt(i), orientation);
				if (sheetRows != null && index > 0 && --index == 0) {
					sheetList.add(sheetRows);
					break;
				}

			} else {
				if (++index == 0) {
					Map<String, Object> sheetRows = getSheetMap(hssfWorkbook.getSheetAt(i), orientation);
					if (sheetRows != null) {
						sheetList.add(sheetRows);
						break;
					}

					index = 1;
				}
			}
		}

		return sheetList;
	}

	/**
	 * @param hssfSheet
	 * @param orientation
	 * @return
	 */
	public static Map<String, Object> getSheetMap(HSSFSheet hssfSheet, boolean orientation) {
		int rows = hssfSheet.getLastRowNum() + 1;
		int columns = rows <= 0 ? 0 : hssfSheet.getRow(0).getLastCellNum() + 1;
		if (rows <= 0 || columns <= 0) {
			return null;
		}

		Map<String, HSSFCell> mergedCells = new HashMap<String, HSSFCell>();
		int size = hssfSheet.getNumMergedRegions();
		for (int i = 0; i < size; i++) {
			CellRangeAddress cellRangeAddress = hssfSheet.getMergedRegion(i);
			HSSFCell hssfCell = hssfSheet.getRow(cellRangeAddress.getFirstRow()).getCell(cellRangeAddress.getFirstColumn());
			int lastRow = cellRangeAddress.getLastRow();
			int lastColumn = cellRangeAddress.getLastColumn();
			for (int j = cellRangeAddress.getFirstRow(); j <= lastRow; j++) {
				for (int k = cellRangeAddress.getFirstColumn(); k <= lastColumn; k++) {
					if (!(i == hssfCell.getRowIndex() && j == hssfCell.getColumnIndex())) {
						mergedCells.put(columnWithRow(j, i), hssfCell);
					}
				}
			}
		}

		if (orientation) {
			int tmp = rows;
			rows = columns;
			columns = tmp;
		}

		Map<String, Object> sheetMap = new HashMap<String, Object>();
		sheetMap.put("name", hssfSheet.getSheetName());
		List<Object> listRows = new ArrayList<Object>();
		sheetMap.put("rows", listRows);
		int titled = columns;
		for (int i = 0; i < rows; i++) {
			List<Object> listColumns = new ArrayList<Object>();
			listRows.add(listColumns);
			int merged = 0;
			for (int j = 0; j < columns; j++) {
				int row, column;
				if (orientation) {
					row = j;
					column = i;

				} else {
					row = i;
					column = j;
				}

				if (mergedCells.containsKey(columnWithRow(column, row))) {
					listColumns.add(null);
					merged++;

				} else {
					HSSFRow hssfRow = hssfSheet.getRow(row);
					if (column < hssfRow.getPhysicalNumberOfCells()) {
						String content = getCellValue(hssfSheet.getRow(row).getCell(column));
						if (content == null || content.isEmpty()) {
							content = "";
							merged++;
						}

						listColumns.add(content);
					}
				}
			}

			if (titled > merged) {
				titled = merged;
				sheetMap.put("title", i);
			}
		}

		return sheetMap;
	}

	/** Base_Class_Map_Dao */
	private static final Map<Class, XlsDao> Base_Class_Map_Dao = new HashMap<Class, XlsDao>();

	/**
	 * @return
	 */
	public static Set<Class> getXlsBases() {
		return Base_Class_Map_Dao.keySet();
	}

	/**
	 * @param xlsClass
	 * @return
	 */
	public static <T extends XlsBase> XlsDao<T, Serializable> getXlsDao(Class<T> xlsClass) {
		return Base_Class_Map_Dao.get(xlsClass);
	}

	/**
	 * @param hssfWorkbook
	 * @param sheets
	 * @param beanClass
	 * @param xlsBase
	 * @param cacheable
	 * @return
	 */
	public static <T> List<T> getXlsBeans(HSSFWorkbook hssfWorkbook, int sheets[], Class<T> beanClass, XlsBase xlsBase, boolean cacheable) {
		if (cacheable && !XlsBase.class.isAssignableFrom(beanClass)) {
			cacheable = false;
		}

		int length = hssfWorkbook.getNumberOfSheets();
		if (length == 0) {
			return null;
		}

		HSSFSheet[] hssfSheets = null;
		if (sheets == null || sheets.length == 0) {
			hssfSheets = new HSSFSheet[length];
			for (int i = 0; i < length; i++) {
				hssfSheets[i] = hssfWorkbook.getSheetAt(i);
			}

		} else {
			List<HSSFSheet> sheetList = new ArrayList<HSSFSheet>();
			for (int i : sheets) {
				if (i >= 0 && i < length) {
					sheetList.add(hssfWorkbook.getSheetAt(i));
				}
			}

			hssfSheets = sheetList.isEmpty() ? null : KernelCollection.toArray(sheetList, HSSFSheet.class);
		}

		if (hssfSheets == null || hssfSheets.length == 0) {
			return null;
		}

		length = hssfSheets.length;
		int[][] accessors = new int[length][];
		for (int i = 0; i < length; i++) {
			accessors[i] = accessorsSheet(hssfSheets[i]);
		}

		return readXlsBeans(hssfSheets, accessors, beanClass, xlsBase, cacheable);
	}

	/**
	 * @author absir
	 * 
	 */
	public static class XlsDaoBase extends XlsDao {

		/** beans */
		private List<Object> beans = new ArrayList<Object>();

		/**
		 * @param idType
		 */
		public XlsDaoBase(Class idType) {
			super(idType);
			// TODO Auto-generated constructor stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.configure.xls.XlsDao#get(java.io.Serializable)
		 */
		@Override
		public Object get(Serializable id) {
			// TODO Auto-generated method stub
			Integer index = (Integer) id;
			return index < 0 || index >= beans.size() ? null : beans.get(index);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.configure.xls.XlsDao#getAll()
		 */
		@Override
		public Collection getAll() {
			// TODO Auto-generated method stub
			return beans;
		}
	}

	/**
	 * @author absir
	 * 
	 */
	public static class XlsDaoBean extends XlsDao {

		/** beans */
		private Map<Serializable, Object> beans = new LinkedHashMap<Serializable, Object>();

		/**
		 * @param idType
		 */
		public XlsDaoBean(Class idType) {
			super(idType);
			// TODO Auto-generated constructor stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.configure.xls.XlsDao#get(java.io.Serializable)
		 */
		@Override
		public Object get(Serializable id) {
			// TODO Auto-generated method stub
			return beans.get(id);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.appserv.configure.xls.XlsDao#getAll()
		 */
		@Override
		public Collection getAll() {
			// TODO Auto-generated method stub
			return beans.values();
		}
	}

	/**
	 * @param hssfSheets
	 * @param accessors
	 * @param beanClass
	 * @param xlsBase
	 * @param cacheable
	 * @return
	 */
	private static <T> List<T> readXlsBeans(HSSFSheet[] hssfSheets, int[][] accessors, Class<T> beanClass, XlsBase xlsBase, boolean cacheable) {
		XlsAccessorContext xlsAccessorContext = new XlsAccessorContext(beanClass, xlsBase);
		XlsDao<? extends XlsBase, Serializable> xlsDao = null;
		XlsDaoBase xlsDaoBase = null;
		XlsDaoBean xlsDaoBean = null;
		if (cacheable) {
			if (xlsAccessorContext.isXlsBean()) {
				xlsDaoBean = new XlsDaoBean(xlsAccessorContext.getIdType());

			} else {
				xlsDaoBase = new XlsDaoBase(xlsAccessorContext.getIdType());
			}

			xlsDao = getXlsDao((Class<? extends XlsBase>) beanClass);
			if (xlsDao == null) {
				Base_Class_Map_Dao.put(beanClass, xlsDaoBase == null ? xlsDaoBean : xlsDaoBase);

			} else {
				if (xlsAccessorContext.isXlsBean()) {
					for (XlsBase base : xlsDao.getAll()) {
						xlsDaoBean.beans.put(base.getId(), base);
					}

					XlsDaoBean dao = (XlsDaoBean) xlsDao;
					xlsDao = xlsDaoBean;
					xlsDaoBean = dao;
					dao.beans.clear();

				} else {
					xlsDaoBase.beans.addAll(xlsDao.getAll());
					XlsDaoBase dao = (XlsDaoBase) xlsDao;
					xlsDao = xlsDaoBase;
					xlsDaoBase = dao;
					dao.beans.clear();
				}
			}
		}

		int length = hssfSheets.length;
		final List<T> beans = new ArrayList<T>();
		for (int i = 0; i < length; i++) {
			HSSFSheet hssfSheet = hssfSheets[i];
			int[] frclrc = accessors[i];
			int firstRow = frclrc[0];
			int firstColumn = frclrc[1];
			int lastRow = frclrc[2];
			int lastColumn = frclrc[3];
			int columnCount = xlsAccessorContext.getColumnCount();
			boolean[] headers = new boolean[columnCount];
			HSSFRow row = null;
			int maxColumn = firstColumn + columnCount;
			if (lastColumn >= maxColumn) {
				lastColumn = maxColumn;
				maxColumn = 0;
			}

			int endColumn = lastColumn;
			while (firstRow < lastRow) {
				int headerCount = 0;
				row = hssfSheet.getRow(firstRow);
				if (maxColumn > 0) {
					endColumn = Math.min(row.getPhysicalNumberOfCells(), maxColumn);
				}

				for (int column = firstColumn; column < endColumn; column++) {
					if (headers[column]) {
						headerCount++;

					} else if (!KernelString.isEmpty(getCellValue(row.getCell(column)))) {
						headers[column] = true;
						headerCount++;
					}
				}

				firstRow++;
				if (headerCount >= columnCount) {
					break;
				}
			}

			List<Object> cells = new ArrayList<Object>();
			while (firstRow < lastRow) {
				firstRow = xlsAccessorContext.readHssfSheet(hssfSheet, cells, firstRow, firstColumn, lastRow);
			}

			int size = cells.size();
			int first = beans.size();
			for (int j = 0; j < size; j++) {
				beans.add((T) xlsAccessorContext.newInstance(xlsDao, cells.get(j), xlsBase, first + j));
			}

			if (cacheable) {
				for (int j = 0; j < size; j++) {
					Object bean = beans.get(first + j);
					if (xlsDaoBase == null) {
						Serializable id = ((XlsBase) bean).getId();
						if (id != null) {
							xlsDaoBean.beans.put(id, bean);
						}

					} else {
						xlsDaoBase.beans.add(bean);
					}
				}
			}

			for (int j = 0; j < size; j++) {
				Object bean = beans.get(j + first);
				xlsAccessorContext.setObject(bean, cells.get(j), xlsBase, null);
			}
		}

		if (XlsBase.class.isAssignableFrom(beanClass) && JiUpdate.class.isAssignableFrom(beanClass)) {
			Field updateTime = KernelReflect.declaredField(beanClass, "updateTime");
			Object updateTimeValue = null;
			long updateTimeMillis = 0;
			String updateEid = null;
			if (updateTime != null && !HelperAccessor.isAccessor(updateTime)) {
				updateTimeMillis = System.currentTimeMillis();
				updateTimeValue = KernelDyna.to(updateTimeMillis, updateTime.getType());
				updateEid = beanClass.getSimpleName();
			}

			for (XlsBase bean : (List<XlsBase>) beans) {
				bean.initializing();
				Serializable id = bean.getId();
				if (id == null) {
					continue;
				}

				if (updateTimeValue != null) {
					JEmbedSS embedSS = new JEmbedSS();
					embedSS.setEid(updateEid);
					embedSS.setMid(KernelDyna.to(id, String.class));
					JUpdateXls updateXls = BeanService.ME.get(JUpdateXls.class, embedSS);
					String serialize = HelperJson.encodeNull(bean);
					if (updateXls == null) {
						updateXls = new JUpdateXls();
						updateXls.setId(embedSS);
					}

					if (!(updateXls.getSerialize() == null ? serialize == null : new String(updateXls.getSerialize()).equals(serialize))) {
						updateXls.setSerialize(serialize == null ? null : serialize.getBytes());
						updateXls.setUpdateTime(updateTimeMillis);
						BeanService.ME.merge(updateXls);
						KernelReflect.set(bean, updateTime, updateTimeValue);

					} else {
						KernelReflect.set(bean, updateTime, KernelDyna.to(updateXls.getUpdateTime(), updateTime.getType()));
					}
				}
			}
		}

		return beans;
	}

	/**
	 * @param xlsClass
	 */
	public static <T extends XlsBase> XlsDao<T, ?> clearXlsDao(Class<T> xlsClass) {
		return Base_Class_Map_Dao.remove(xlsClass);
	}

	/**
	 * @param hssfSheet
	 * @param xlsCell
	 */
	public static void writeHssfSheet(HSSFSheet hssfSheet, XlsCell xlsCell) {
		int rows = hssfSheet.getPhysicalNumberOfRows();
		int rowCount = xlsCell.getRowCount();
		int columnCount = xlsCell.getColumnCount();
		for (int i = 0; i < rowCount; i++) {
			HSSFRow hssfRow = hssfSheet.createRow(i);
			for (int j = 0; j < columnCount; j++) {
				hssfRow.createCell(j);
			}
		}

		writeHssfSheet(hssfSheet, xlsCell, rows, 0, rowCount, columnCount);
	}

	/**
	 * @param hssfSheet
	 * @param xlsCell
	 * @param row
	 * @param column
	 * @param rowCount
	 * @param columnCount
	 */
	private static void writeHssfSheet(HSSFSheet hssfSheet, XlsCell xlsCell, int row, int column, int rowCount, int columnCount) {
		int basicRow = xlsCell.getBasicRow();
		if (basicRow > 0) {
			xlsCell.wirteHssfCell(hssfSheet.getRow(row).getCell(column));
		}

		if (xlsCell.getChildren() == null) {
			if (rowCount > 1 && columnCount > 0) {
				hssfSheet.addMergedRegion(new CellRangeAddress(row, row + rowCount - 1, column, column + columnCount - 1));
			}

		} else {
			if ((basicRow > 1 && columnCount > 0) || (basicRow > 0 && columnCount > 1)) {
				hssfSheet.addMergedRegion(new CellRangeAddress(row, row + basicRow - 1, column, column + columnCount - 1));
			}

			row += basicRow;
			for (List<XlsCell> xlsCells : xlsCell.getChildren()) {
				rowCount = XlsCell.getLineRowCount(xlsCells);
				int iColumn = column;
				for (XlsCell cell : xlsCells) {
					columnCount = cell.getColumnCount();
					writeHssfSheet(hssfSheet, cell, row, iColumn, rowCount, columnCount);
					iColumn += columnCount;
				}

				row += rowCount;
			}
		}
	}

	/**
	 * @param hssfWorkbook
	 * @param beanClass
	 * @param beans
	 * @param xlsBase
	 */
	public static void writeHssfWorkbook(HSSFWorkbook hssfWorkbook, Class beanClass, Collection beans, XlsBase xlsBase) {
		int[] rowCounts = new int[2];
		rowCounts[1] = SpreadsheetVersion.EXCEL97.getLastRowIndex();
		XlsAccessorContext xlsAccessorContext = new XlsAccessorContext(beanClass, xlsBase);
		XlsCell xlsCellHeader = xlsAccessorContext.getHeader();
		rowCounts[0] = xlsCellHeader.getRowCount();
		int index = 0;
		HSSFSheet hssfSheet = hssfWorkbook.createSheet(beanClass.getSimpleName());
		if (beans != null) {
			for (Object bean : beans) {
				XlsCell xlsCell = xlsAccessorContext.writeXlsCellBean(xlsCellHeader, bean, xlsBase, rowCounts);
				if (xlsCell != null) {
					writeHssfSheet(hssfSheet, xlsCellHeader);
					hssfSheet = hssfWorkbook.createSheet(beanClass.getSimpleName() + "(" + ++index + ")");
					xlsCellHeader = xlsAccessorContext.getHeader();
					rowCounts[0] = xlsCellHeader.getRowCount() + xlsCell.getRowCount();
					xlsCellHeader.addColumnList(null).add(xlsCell);
				}
			}
		}

		writeHssfSheet(hssfSheet, xlsCellHeader);
	}
}
