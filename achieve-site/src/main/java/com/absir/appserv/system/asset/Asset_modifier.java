/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-6 下午3:33:33
 */
package com.absir.appserv.system.asset;

import java.io.IOException;

import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XCardExpDefine;
import com.absir.appserv.client.configure.xls.XPlayerDefine;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.client.configure.xls.XRewardDefine;
import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.client.configure.xls.XStoryDefine;
import com.absir.appserv.client.configure.xls.XTaskDefine;
import com.absir.appserv.configure.xls.XlsUtils;
import com.absir.server.value.Body;
import com.absir.server.value.Server;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
@Server
public class Asset_modifier extends AssetServer {

	/**
	 * @param index
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Body
	public Object route(int index, InputRequest input) throws IOException {
		XlsUtils.reloadXlsDao(XPlayerDefine.class);
		XlsUtils.reloadXlsDao(XCardExpDefine.class);
		XlsUtils.reloadXlsDao(XCardDefine.class);
		XlsUtils.reloadXlsDao(XTaskDefine.class);
		XlsUtils.reloadXlsDao(XStoryDefine.class);

		XlsUtils.reloadXlsDao(XPropDefine.class);
		XlsUtils.reloadXlsDao(XSkillDefine.class);

		XlsUtils.reloadXlsDao(XRewardDefine.class);
		return "reload complete";
	}
}
