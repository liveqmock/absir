/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-3 上午11:04:41
 */
package com.absir.orm.hibernate.transaction;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.spi.CurrentSessionContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import com.absir.core.kernel.KernelClass;
import com.absir.orm.transaction.ISessionContext;
import com.absir.orm.transaction.ISessionHolder;
import com.absir.orm.transaction.TransactionAttribute;
import com.absir.orm.transaction.TransactionContext;
import com.absir.orm.transaction.TransactionHolder;
import com.absir.orm.transaction.TransactionSession;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class JSessionContext implements CurrentSessionContext, ISessionContext {

	/** sessionFactory */
	private SessionFactoryImplementor sessionFactory;

	/** transactionContext */
	private JTransactionContext transactionContext;

	/**
	 * @param sessionFactory
	 */
	public JSessionContext(SessionFactoryImplementor sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.context.spi.CurrentSessionContext#currentSession()
	 */
	@Override
	public Session currentSession() throws HibernateException {
		// TODO Auto-generated method stub
		if (transactionContext == null) {
			throw new HibernateException("No TransactionContext configured!");
		}

		JTransactionSession transactionSession = transactionContext.getTransactionSession();
		if (transactionSession == null) {
			throw new HibernateException("No transactionSession configured!");
		}

		transactionSession.open(this);
		JSession session = transactionSession.getCurrentSession();
		return session == null ? null : session.getSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.orm.transaction.ISessionContext#get(java.lang.String)
	 */
	@Override
	public TransactionContext get(String name) {
		// TODO Auto-generated method stub
		if (transactionContext == null) {
			transactionContext = new JTransactionContext(name);
		}

		return transactionContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.orm.transaction.ISessionContext#open(com.absir.orm.transaction
	 * .ISessionHolder, com.absir.orm.transaction.TransactionAttribute,
	 * com.absir.orm.transaction.TransactionSession)
	 */
	@Override
	public ISessionHolder open(ISessionHolder sessionHolder, TransactionAttribute transactionAttribute, TransactionSession transactionSession) {
		// TODO Auto-generated method stub
		return open(sessionFactory, sessionHolder, transactionAttribute, (JTransactionSession) transactionSession);
	}

	/**
	 * @param sessionFactory
	 * @param transactionAttributeBefore
	 * @param transactionAttribute
	 * @param jTransactionSession
	 * @return
	 */
	public static ISessionHolder open(SessionFactory sessionFactory, ISessionHolder sessionHolder, TransactionAttribute transactionAttribute, final JTransactionSession jTransactionSession) {
		if (jTransactionSession.getCurrentSession() == null && !transactionAttribute.isRequired()) {
			return null;
		}

		TransactionHolder transactionHolder = new TransactionHolder(sessionHolder, transactionAttribute) {

			@Override
			public void close(Throwable e) {
				// TODO Auto-generated method stub
				if (nested == 0) {
					JSession jSession = jTransactionSession.getCurrentSession();
					if (jSession != null) {
						if (readOnly == 2) {
							jSession.getSession().setFlushMode(FlushMode.COMMIT);
							jSession.getSession().setDefaultReadOnly(false);

						} else if (readOnly == 3) {
							jSession.getSession().setFlushMode(FlushMode.MANUAL);
							jSession.getSession().setDefaultReadOnly(true);
						}

						if (timeout != 0) {
							jSession.getTransaction().setTimeout((int) timeout);
						}
					}

				} else {
					JSession jSession = jTransactionSession.closeCurrentSession();
					if (jSession != null) {
						if (readOnly == 0 || readOnly == 2) {
							if (e == null || rollback == null || !KernelClass.isAssignableFrom(rollback, e.getClass())) {
								jSession.getTransaction().commit();

							} else {
								jSession.getTransaction().rollback();
							}
						}

						jSession.getSession().close();
					}
				}
			}
		};

		JSession jSession = null;
		switch (transactionHolder.getNested()) {
		case 0:
			jSession = jTransactionSession.getCurrentSession();
			if (jSession != null) {
				if (transactionHolder.getReadOnly() >= 2) {
					if (transactionAttribute.isReadOnly()) {
						jSession.getSession().setFlushMode(FlushMode.MANUAL);
						jSession.getSession().setDefaultReadOnly(true);

					} else {
						jSession.getSession().setFlushMode(FlushMode.COMMIT);
						jSession.getSession().setDefaultReadOnly(false);
					}
				}

				if (transactionHolder.getTimeout() != 0) {
					jSession.getTransaction().setTimeout((int) transactionAttribute.getTimeout());
				}
			}

			break;

		case 1:
			jSession = new JSession(sessionFactory.openSession());
			jTransactionSession.openCurrentSession(jSession);
			if (transactionAttribute.isReadOnly()) {
				jSession.getSession().setFlushMode(FlushMode.MANUAL);
				jSession.getSession().setDefaultReadOnly(true);

			} else {
				jSession.getSession().setFlushMode(FlushMode.COMMIT);
				jSession.getSession().setDefaultReadOnly(false);
			}

			if (transactionAttribute.getTimeout() > 0) {
				jSession.getTransaction().setTimeout((int) transactionAttribute.getTimeout());
			}

			break;

		default:
			jTransactionSession.pushCurrentSession();
			break;
		}

		return transactionHolder;
	}
}
