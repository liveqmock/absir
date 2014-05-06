/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-5-6 下午3:05:33
 */
package com.absir.appserv.system.service;

import java.io.Serializable;
import java.util.Map;

import com.absir.appserv.system.bean.value.JiActive;
import com.absir.appserv.system.domain.DActiver;
import com.absir.appserv.system.domain.DActiverMap;
import com.absir.async.value.Async;
import com.absir.bean.inject.value.Started;
import com.absir.context.core.ContextService;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelString;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.hibernate.boost.IEntityMerge;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 * 
 */
public abstract class ActiveService<T extends JiActive, K> extends ContextService implements IEntityMerge<T> {

	/** activer */
	protected DActiver<T> activer;

	/** activerMap */
	protected DActiverMap<T, K> activerMap;

	/**
	 * @author absir
	 * 
	 */
	protected class ActiveMap extends DActiverMap<T, K> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.domain.DActiverMap#createActiveContexts()
		 */
		@Override
		protected Map<Serializable, K> createActiveContexts() {
			// TODO Auto-generated method stub
			return ActiveService.this.createActiveContexts();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.domain.DActiverMap#isClosed(com.absir.core
		 * .base.IBase)
		 */
		@Override
		protected boolean isClosed(K activeContext) {
			// TODO Auto-generated method stub
			return ActiveService.this.isClosed(activeContext);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.domain.DActiverMap#createActiveContext(com
		 * .absir.core.base.IBase)
		 */
		@Override
		protected K createActiveContext(T active) {
			// TODO Auto-generated method stub
			return ActiveService.this.createActiveContext(active);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.domain.DActiverMap#updateActiveContext(com
		 * .absir.core.base.IBase, java.lang.Object)
		 */
		@Override
		protected K updateActiveContext(T active, K activeContext) {
			// TODO Auto-generated method stub
			return ActiveService.this.updateActiveContext(active, activeContext);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.system.domain.DActiverMap#closeActiveContext(java
		 * .lang.Object)
		 */
		@Override
		protected void closeActiveContext(Serializable id, K activeContext) {
			// TODO Auto-generated method stub
			ActiveService.this.closeActiveContext(id, activeContext);
		}
	}

	/**
	 * 开始服务
	 */
	@Started
	protected void started() {
		if (activer == null) {
			String entityName = SessionFactoryUtils.getEntityNameNull(KernelClass.argumentClass(getClass()));
			if (!KernelString.isEmpty(entityName)) {
				activer = new DActiver<T>(entityName);
			}
		}

		if (activerMap == null) {
			activerMap = new ActiveMap();
		}

		if (activer != null) {
			getInstance().reloadActives(ContextUtils.getContextTime());
		}
	}

	/**
	 * @return
	 */
	protected abstract ActiveService<T, K> getInstance();

	/**
	 * @return
	 */
	protected Map<Serializable, K> createActiveContexts() {
		return null;
	}

	/**
	 * @param activeContext
	 * @return
	 */
	protected abstract boolean isClosed(K activeContext);

	/**
	 * @param active
	 * @return
	 */
	protected abstract K createActiveContext(T active);

	/**
	 * @param active
	 * @param activeContext
	 * @return
	 */
	protected K updateActiveContext(T active, K activeContext) {
		return activeContext;
	}

	/**
	 * @param id
	 * @param activeContext
	 */
	protected abstract void closeActiveContext(Serializable id, K activeContext);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextService#step(long)
	 */
	@Override
	public void step(long contextTime) {
		// TODO Auto-generated method stub
		if (activer != null && activer.stepNext(contextTime)) {
			getInstance().reloadActives(contextTime);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.orm.hibernate.boost.IEntityMerge#merge(java.lang.String,
	 * java.lang.Object, com.absir.orm.hibernate.boost.IEntityMerge.MergeType,
	 * java.lang.Object)
	 */
	@Override
	public void merge(String entityName, T entity, com.absir.orm.hibernate.boost.IEntityMerge.MergeType mergeType, Object mergeEvent) {
		// TODO Auto-generated method stub
		if (activer != null) {
			activer.merge(entity, mergeType, mergeEvent);
		}
	}

	/**
	 * @param contextTime
	 */
	@Async
	@Transaction(readOnly = true)
	protected void reloadActives(long contextTime) {
		if (activerMap != null) {
			activerMap.setActives(activer.reloadActives(contextTime));
		}
	}
}
