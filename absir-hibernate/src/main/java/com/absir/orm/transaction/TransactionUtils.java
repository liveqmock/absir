/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-13 下午4:38:14
 */
package com.absir.orm.transaction;

import java.util.Collection;
import java.util.Map.Entry;

import com.absir.bean.core.BeanFactoryUtils;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class TransactionUtils {

	/** transactionService */
	private static TransactionService transactionService = BeanFactoryUtils.get(TransactionService.class);

	/**
	 * @return
	 */
	public static TransactionService get() {
		return transactionService;
	}

	/**
	 * @param transactionAttribute
	 * @param transactionAttributes
	 * @param transactionAttributeDefault
	 */
	public static void open(TransactionAttribute transactionAttribute, Collection<Entry<TransactionContext, TransactionAttribute>> transactionAttributes,
			TransactionAttribute transactionAttributeDefault) {
		if (transactionAttribute != null) {
			transactionService.getTransactionContext().add(transactionAttribute);
		}

		if (transactionAttributes != null) {
			for (Entry<TransactionContext, TransactionAttribute> entry : transactionAttributes) {
				entry.getKey().add(entry.getValue());
			}
		}

		if (transactionAttributeDefault != null) {
			transactionService.addTransactionAttributeDefault(transactionAttributeDefault);
		}
	}

	/**
	 * @param transactionAttribute
	 * @param transactionAttributes
	 * @param transactionAttributeDefault
	 * @param e
	 * @return
	 */
	public static Throwable close(TransactionAttribute transactionAttribute, Collection<Entry<TransactionContext, TransactionAttribute>> transactionAttributes,
			TransactionAttribute transactionAttributeDefault, Throwable e) {
		if (transactionAttributeDefault == null) {
			Throwable throwable = null;
			if (transactionAttribute != null) {
				throwable = transactionService.getTransactionContext().closeCurrent(e, throwable);
			}

			if (transactionAttributes != null) {
				for (Entry<TransactionContext, TransactionAttribute> entry : transactionAttributes) {
					throwable = entry.getKey().closeCurrent(e, throwable);
				}
			}

			return throwable;

		} else {
			return transactionService.closeAllCurrent(e, null);
		}
	}
}
