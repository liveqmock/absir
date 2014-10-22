/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月22日 下午12:50:39
 */
package com.absir.core.util;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author absir
 *
 */
public class UtilQueueBlock<T> extends UtilQueue<T> {

	/** reading */
	protected Boolean reading;

	/** lock */
	protected Lock lock = new ReentrantLock();

	/** condition */
	protected Condition condition = lock.newCondition();

	/**
	 * @param capacity
	 */
	public UtilQueueBlock(int capacity) {
		super(capacity);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.util.UtilQueue#addElement(java.lang.Object)
	 */
	@Override
	public synchronized void addElement(T element) {
		super.addElement(element);
		if (reading) {
			lock.lock();
			reading = false;
			condition.signal();
			lock.unlock();
		}
	}

	/**
	 * @throws InterruptedException
	 */
	protected void readingWaite() throws InterruptedException {
		lock.lock();
		reading = true;
		condition.wait();
		lock.unlock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.util.UtilQueue#readElement()
	 */
	@Override
	public synchronized T readElement() {
		T element;
		try {
			while (true) {
				element = super.readElement();
				if (element == null) {
					readingWaite();

				} else {
					return element;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.util.UtilQueue#readElements(int)
	 */
	@Override
	public synchronized List<T> readElements(int max) {
		List<T> elements;
		try {
			while (true) {
				elements = super.readElements(max);
				if (elements == null) {
					readingWaite();

				} else {
					return elements;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
