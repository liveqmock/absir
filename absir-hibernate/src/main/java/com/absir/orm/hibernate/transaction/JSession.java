/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-4 上午11:27:46
 */
package com.absir.orm.hibernate.transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author absir
 * 
 */
public class JSession {

	/** session */
	private Session session;

	/** transaction */
	private Transaction transaction;

	/**
	 * 
	 */
	public JSession() {
	}

	/**
	 * @param session
	 */
	public JSession(Session session) {
		this(session, session.beginTransaction());
	}

	/**
	 * @param session
	 * @param transaction
	 */
	public JSession(Session session, Transaction transaction) {
		this.session = session;
		this.transaction = transaction;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
