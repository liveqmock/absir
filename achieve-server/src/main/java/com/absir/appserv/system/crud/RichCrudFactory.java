/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-7-9 下午3:36:15
 */
package com.absir.appserv.system.crud;

import java.io.File;
import java.util.List;

import org.hibernate.Session;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.crud.ICrudProcessorInput;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.JUpload;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.service.CrudService;
import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.helper.HelperFile;
import com.absir.orm.value.JoEntity;
import com.absir.property.PropertyErrors;
import com.absir.server.in.Input;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Base
@Bean
public class RichCrudFactory implements ICrudFactory, ICrudProcessorInput<Object> {

	/** REMOTE_RICH_NAME */
	private static final String REMOTE_RICH_NAME = "REMOTE_RICH@";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudProcessorInput#isMultipart()
	 */
	@Override
	public boolean isMultipart() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudProcessor#crud(com.absir.appserv.crud.
	 * CrudProperty, java.lang.Object, com.absir.appserv.crud.CrudHandler,
	 * com.absir.appserv.system.bean.proxy.JiUserBase)
	 */
	@Override
	public void crud(CrudProperty crudProperty, Object entity, CrudHandler handler, JiUserBase user) {
		// TODO Auto-generated method stub
		if (handler.getCrud() == Crud.DELETE) {
			String entityName = handler.getCrudEntity().getJoEntity().getEntityName();
			if (entityName != null) {
				Session session = BeanDao.getSession();
				String eid = entityName + "@" + CrudService.ME.getCrudSupply(entityName).getIdentifier(entityName, handler.getRoot());
				List<JUpload> uploads = QueryDaoUtils.createQueryArray(session, "SELECT o FROM JUpload o WHERE o.id.eid = ?", eid).list();
				if (!uploads.isEmpty()) {
					QueryDaoUtils.createQueryArray(session, "DELETE o FROM JUpload o WHERE o.id.eid = ?", eid).executeUpdate();
					session.flush();
					for (JUpload upload : uploads) {
						String mid = upload.getId().getMid();
						if (!QueryDaoUtils.createQueryArray(session, "SELECT o.id.eid FROM JUpload o WHERE o.id.mid = ?", mid).iterate().hasNext()) {
							HelperFile.deleteQuietly(new File(UploadCrudFactory.getUploadPath() + mid));
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudProcessorInput#crud(com.absir.appserv.crud
	 * .CrudProperty, com.absir.property.PropertyErrors,
	 * com.absir.appserv.crud.CrudHandler,
	 * com.absir.appserv.system.bean.proxy.JiUserBase,
	 * com.absir.server.in.Input)
	 */
	@Override
	public Object crud(CrudProperty crudProperty, PropertyErrors errors, CrudHandler handler, JiUserBase user, Input input) {
		// TODO Auto-generated method stub
		if (handler.getCrud() != Crud.DELETE) {
			Object remote = input.getParamMap().get(REMOTE_RICH_NAME);
			if (remote == null) {
				remote = input.getParamMap().get(REMOTE_RICH_NAME);
			}

			if (DynaBinder.to(remote, Boolean.class) == Boolean.TRUE) {
				return Boolean.TRUE;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudProcessorInput#crud(com.absir.appserv.crud
	 * .CrudProperty, java.lang.Object, com.absir.appserv.crud.CrudHandler,
	 * com.absir.appserv.system.bean.proxy.JiUserBase, java.lang.Object)
	 */
	@Override
	public void crud(CrudProperty crudProperty, Object entity, CrudHandler handler, JiUserBase user, Object inputBody) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudFactory#getProcessor(com.absir.appserv.support
	 * .entity.value.JoEntity, com.absir.appserv.support.developer.JCrudField)
	 */
	@Override
	public ICrudProcessor getProcessor(JoEntity joEntity, JCrudField crudField) {
		// TODO Auto-generated method stub
		return this;
	}
}
