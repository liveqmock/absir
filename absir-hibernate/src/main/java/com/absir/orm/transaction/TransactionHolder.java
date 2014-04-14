/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-4 上午10:14:34
 */
package com.absir.orm.transaction;

/**
 * @author absir
 * 
 */
public abstract class TransactionHolder implements ISessionHolder {

	// 0 false|false 1 true|true 2 false|true 3 true|false
	/** readOnly */
	protected byte readOnly;

	/** rollback */
	protected Class<?>[] rollback;

	// 0 false 1 true 2 null 3 empty
	/** nested */
	protected byte nested;

	/** timeout */
	protected long timeout;

	/**
	 * 
	 */
	public TransactionHolder() {
	}

	/**
	 * @param holderBefore
	 * @param transactionAttributeBefore
	 */
	public TransactionHolder(ISessionHolder holderBefore, TransactionAttribute transactionAttribute) {
		if (holderBefore == null) {
			nested = (byte) (transactionAttribute.isRequired() ? 1 : 2);
			readOnly = (byte) (transactionAttribute.isReadOnly() ? 1 : 0);

		} else {
			if (transactionAttribute.isNested()) {
				nested = (byte) (transactionAttribute.isRequired() ? 1 : 3);

			} else {
				readOnly = (byte) (holderBefore.isReadOnly() == transactionAttribute.isReadOnly() ? (holderBefore.isReadOnly() ? 1 : 0) : (holderBefore.isReadOnly() ? 3 : 2));
				if (transactionAttribute.getTimeout() > 0) {
					timeout = holderBefore.getTimeout();
					if (timeout == 0) {
						timeout = -1;
					}
				}
			}
		}

		// self rollback set
		rollback = transactionAttribute.getRollback();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.orm.transaction.ISessionHolder#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return readOnly == 1 || readOnly == 2;
	}

	/**
	 * @return the readOnly
	 */
	public byte getReadOnly() {
		return readOnly;
	}

	/**
	 * @param readonly
	 *            the readonly to set
	 */
	public void setReadOnly(byte readonly) {
		this.readOnly = readonly;
	}

	/**
	 * @return the rollback
	 */
	public Class<?>[] getRollback() {
		return rollback;
	}

	/**
	 * @param rollback
	 *            the rollback to set
	 */
	public void setRollback(Class<?>[] rollback) {
		this.rollback = rollback;
	}

	/**
	 * @return the nested
	 */
	public byte getNested() {
		return nested;
	}

	/**
	 * @param nested
	 *            the nested to set
	 */
	public void setNested(byte nested) {
		this.nested = nested;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
