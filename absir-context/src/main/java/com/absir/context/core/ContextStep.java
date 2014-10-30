/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月24日 上午11:51:48
 */
package com.absir.context.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.absir.context.bean.IStep;

/**
 * @author absir
 *
 */
public class ContextStep implements Runnable {

	/** cancel */
	private boolean cancel;

	/** delay */
	private long delay;

	/** detlaTime */
	private long detlaTime;

	/** steps */
	private List<IStep> steps = new LinkedList<IStep>();

	/** addSteps */
	private List<IStep> addSteps = new ArrayList<IStep>();

	/**
	 * @param delay
	 */
	public ContextStep(long delay) {
		this.delay = delay;
	}

	/**
	 * @return the detlaTime
	 */
	public long getDetlaTime() {
		return detlaTime;
	}

	/**
	 * @param step
	 */
	public synchronized void addStep(IStep step) {
		addSteps.add(step);
	}

	/**
	 * 
	 */
	public void start() {
		ContextUtils.getThreadPoolExecutor().execute(this);
	}

	/**
	 * 
	 */
	public void cancel() {
		cancel = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			detlaTime = System.currentTimeMillis();
			while (!cancel) {
				List<IStep> adds = addSteps;
				if (!adds.isEmpty()) {
					addSteps = new ArrayList<IStep>();
					synchronized (this) {
						steps.addAll(adds);
					}
				}

				long contextTime = System.currentTimeMillis();
				detlaTime = contextTime - detlaTime;
				Iterator<IStep> iterator = steps.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().stepDone(contextTime)) {
						iterator.remove();
					}
				}

				detlaTime = contextTime;
				Thread.sleep(delay);
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
