/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-6 下午3:33:33
 */
package com.absir.appserv.system.asset;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.absir.appserv.configure.xls.XlsAccessorUtils;
import com.absir.appserv.system.crud.UploadCrudFactory;
import com.absir.appserv.system.helper.HelperHtml;
import com.absir.server.value.Body;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Asset_excel extends AssetServer {

	/**
	 * @param index
	 * @param request
	 * @return
	 */
	@Body
	public Object route(int index, HttpServletRequest request) {
		return route(index, false, request);
	}

	/**
	 * @param binder
	 * @param result
	 * @param request
	 * @return
	 */
	@Body
	public Object route(int index, boolean orientation, HttpServletRequest request) {
		FileItem excel = UploadCrudFactory.getUploadFile(request, "excel");
		if (excel != null) {
			try {
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excel.getInputStream());
				Map<String, Object> workmap = new TreeMap<String, Object>();
				try {
					workmap.put("version", hssfWorkbook.getSummaryInformation().getOSVersion());

				} catch (Exception e) {
					// TODO: handle exception
				}

				workmap.put("sheets", XlsAccessorUtils.getSheetList(hssfWorkbook, index, orientation));
				return workmap;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				return "read excel file:" + excel.getName() + " is error!";
			}
		}

		return "can not find upload excel!";
	}

	/**
	 * @param response
	 * @throws IOException
	 */
	@Body
	public void test(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.println(HelperHtml.HTML4_DOC_TYPE);
		out.println("<form action='./' enctype='multipart/form-data' method='POST'>");
		out.println("<input type='file' name='excel'/>");
		out.println("<input type='submit'/>");
		out.println("</form>");
	}
}
