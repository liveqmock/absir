/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-6 下午3:05:14
 */
package com.absir.appserv.system.asset;

import com.absir.appserv.feature.menu.value.MaPermission;
import com.absir.appserv.system.crud.RichCrudFactory;
import com.absir.bean.basis.Base;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Base
@Server
public class Asset_upload extends AssetServer {

	@MaPermission(RichCrudFactory.UPLOAD)
	public void upload() {

	}

}
