/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-14 下午4:12:13
 */
package com.absir.context.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectOrder;
import com.absir.bean.inject.value.Started;
import com.absir.bean.inject.value.Stopping;
import com.absir.bean.inject.value.Value;
import com.absir.context.bean.IContext;
import com.absir.core.kernel.KernelClass;
import com.absir.core.util.UtilAbsir;
import com.absir.core.util.UtilAtom;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Base
@Bean
public class ContextFactory {

	/** logger */
	protected final Logger logger = LoggerFactory.getLogger(ContextFactory.class);

	/** contextTime */
	private long contextTime = System.currentTimeMillis();

	/** contextBases */
	private Queue<ContextBase> contextBases = new ConcurrentLinkedQueue<ContextBase>();

	/** tokenMap */
	private Map<Object, Object> tokenMap = new HashMap<Object, Object>();

	/** classMapIdMapContext */
	private Map<Class<?>, Map<Serializable, Context>> classMapIdMapContext = new HashMap<Class<?>, Map<Serializable, Context>>();

	/** contextBeans */
	private Queue<ContextBean> contextBeans = new ConcurrentLinkedQueue<ContextBean>();

	/** threadPoolExecutor */
	private ThreadPoolExecutor threadPoolExecutor;

	/** utilAtom */
	private UtilAtom utilAtom;

	/** maxThread */
	@Value("context.maxThread")
	private int maxThread = 1024;

	/** stopDelay */
	@Value("context.stopDelay")
	private int stopDelay = 1000;

	/** contextTimerTask */
	private TimerTask contextTimerTask = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			contextTime = System.currentTimeMillis();
			Iterator<ContextBase> contextBaseIterator = contextBases.iterator();
			while (contextBaseIterator.hasNext()) {
				final ContextBase contextBase = contextBaseIterator.next();
				if (contextBase.isExpiration() || contextBase.stepDone(contextTime)) {
					contextBaseIterator.remove();
					utilAtom.increment();
					threadPoolExecutor.execute(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								contextBase.uninitialize();

							} catch (Throwable e) {
								logger.error("failed!", e);

							} finally {
								utilAtom.decrement();
							}
						}
					});
				}
			}

			Iterator<ContextBean> contextBeanIterator = contextBeans.iterator();
			while (contextBeanIterator.hasNext()) {
				final ContextBean contextBean = contextBeanIterator.next();
				if (contextBean.isExpiration() || contextBean.stepDone(contextTime)) {
					contextBeanIterator.remove();
					contextBean.setExpiration();
					utilAtom.increment();
					threadPoolExecutor.execute(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Map<Serializable, Context> contextMap = classMapIdMapContext.get(contextBean.getClass());
								if (contextMap != null) {
									contextBean.uninitialize();
									synchronized (contextMap) {
										if (contextBean.isExpiration()) {
											contextMap.remove(contextBean.getId());
											return;
										}
									}

									contextBeans.add(contextBean);
								}

							} catch (Throwable e) {
								logger.error("failed!", e);

							} finally {
								utilAtom.decrement();
							}
						}
					});
				}
			}

			utilAtom.await();
		}
	};

	/**
	 * @return the contextTime
	 */
	public long getContextTime() {
		return contextTime;
	}

	/**
	 * @return the threadPoolExecutor
	 */
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	/**
	 * @param contexts
	 */
	@Started
	private void scanner() {
		List<IContext> contexts = BeanFactoryUtils.get().getBeanObjects(IContext.class);
		for (IContext context : contexts) {
			contextBases.add(context instanceof ContextBase ? (ContextBase) context : new ContextWrapper(context));
		}
	}

	/**
	 * @param context
	 */
	public void addContext(ContextBase context) {
		context.retainAt(contextTime);
		contextBases.add(context);
	}

	/**
	 * @param context
	 */
	public void removeContext(ContextBase context) {
		context.setExpiration();
	}

	@Inject
	@InjectOrder(10)
	protected void injectExecutor(@Value(value = "context.corePoolSize", defaultValue = "1024") int corePoolSize,
			@Value(value = "context.maximumPoolSize", defaultValue = "10240") int maximumPoolSize, @Value(value = "context.keepAliveTime", defaultValue = "90000") int keepAliveTime) {
		setThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime);
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 */
	protected void setThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int keepAliveTime) {
		// 请求处理线程池
		threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(corePoolSize));
	}

	/**
	 * @param tokenId
	 * @return
	 */
	public Object getToken(String tokenId) {
		return UtilAbsir.getToken(tokenId, tokenMap);
	}

	/**
	 * @param tokenId
	 */
	public void clearToken(String tokenId) {
		tokenMap.remove(tokenId);
	}

	/**
	 * @param cls
	 * @return
	 */
	public Map<Serializable, Context> getContextMap(Class<?> cls) {
		Map<Serializable, Context> contextMap = classMapIdMapContext.get(cls);
		if (contextMap == null) {
			synchronized (cls) {
				contextMap = classMapIdMapContext.get(cls);
				if (contextMap == null) {
					contextMap = new ConcurrentHashMap<Serializable, Context>();
					classMapIdMapContext.put(cls, contextMap);
				}
			}
		}

		return contextMap;
	}

	/**
	 * @param cls
	 * @return
	 */
	public Map<Serializable, Context> findContextMap(Class<?> cls) {
		return classMapIdMapContext.get(cls);
	}

	/**
	 * @param ctxClass
	 * @param id
	 * @param cls
	 * @param concurrent
	 * @return
	 */
	public <T extends Context<ID>, ID extends Serializable> T getContext(Class<T> ctxClass, ID id, Class<?> cls, boolean concurrent) {
		return getContext(getContextMap(cls), ctxClass, id, cls, concurrent);
	}

	/**
	 * @param contextMap
	 * @param ctxClass
	 * @param id
	 * @param cls
	 * @param concurrent
	 * @return
	 */
	private <T extends Context<ID>, ID extends Serializable> T getContext(Map<Serializable, Context> contextMap, Class<T> ctxClass, ID id, Class<?> cls, boolean concurrent) {
		Context context = contextMap.get(id);
		if (context == null) {
			String tokenId = UtilAbsir.getId(cls, id);
			synchronized (concurrent ? getToken(tokenId) : contextMap) {
				try {
					context = contextMap.get(id);
					if (context == null) {
						context = KernelClass.newInstance(ctxClass);
						context.setId(id);
						context.initialize();
						if (concurrent) {
							Context initialized = null;
							synchronized (contextMap) {
								initialized = contextMap.get(id);
								if (initialized == null) {
									contextMap.put(id, context);

								} else if (initialized instanceof IContext) {
									((IContext) initialized).retainAt(contextTime);
								}
							}

							if (initialized != null) {
								return (T) initialized;
							}

						} else {
							contextMap.put(id, context);
						}

						if (context instanceof ContextBean) {
							((ContextBean) context).retainAt(contextTime);
							contextBeans.add((ContextBean) context);
							return (T) context;
						}
					}

				} finally {
					if (context instanceof ContextBean) {
						clearToken(tokenId);
					}
				}
			}
		}

		if (context instanceof IContext) {
			((IContext) context).retainAt(contextTime);
		}

		return (T) context;
	}

	/**
	 * @param context
	 * @param cls
	 * @param concurrent
	 */
	public void clearContext(Context context, Class cls, boolean concurrent) {
		if (context instanceof ContextBean) {
			((ContextBean) context).setExpiration();

		} else {
			Map<Serializable, Context> contextMap = classMapIdMapContext.get(cls);
			if (contextMap != null) {
				synchronized (concurrent ? UtilAbsir.getToken(cls, context.getId(), contextMap) : contextMap) {
					context.uninitialize();
					contextMap.remove(context.getId());
				}

			} else {
				context.uninitialize();
			}
		}
	}

	/**
	 * @param maxThread
	 * @return
	 */
	public static UtilAtom getUtilAtom(int maxThread) {
		return maxThread <= 0 ? new UtilAtom() : new ContextAtom(maxThread);
	}

	/**
	 * 
	 */
	@Started
	private void start() {
		utilAtom = getUtilAtom(maxThread);
		new Timer().schedule(contextTimerTask, 0, 1000);
	}

	/**
	 * 
	 */
	@Stopping
	private void stop() {
		contextTimerTask.cancel();
		final UtilAtom utilAtom = getUtilAtom(maxThread * 10);
		Queue<ContextBase> contextBases = this.contextBases;
		this.contextBases = new ConcurrentLinkedQueue<ContextBase>();
		for (final ContextBase contextBase : contextBases) {
			utilAtom.increment();
			threadPoolExecutor.execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						contextBase.uninitialize();

					} finally {
						utilAtom.decrement();
					}
				}
			});
		}

		Map<Class<?>, Map<Serializable, Context>> classMapIdMapContext = this.classMapIdMapContext;
		this.classMapIdMapContext = new HashMap<Class<?>, Map<Serializable, Context>>();
		for (Entry<Class<?>, Map<Serializable, Context>> entry : classMapIdMapContext.entrySet()) {
			for (Entry<Serializable, Context> contextEntry : entry.getValue().entrySet()) {
				utilAtom.increment();
				final Context context = contextEntry.getValue();
				threadPoolExecutor.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							context.uninitialize();

						} finally {
							utilAtom.decrement();
						}
					}
				});

			}
		}

		utilAtom.await();
		threadPoolExecutor.shutdownNow();

		try {
			Thread.sleep(stopDelay);

		} catch (Throwable e) {
		}
	}
}
