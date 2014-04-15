/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-25 上午10:43:47
 */
package com.absir.server.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.absir.bean.basis.Base;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelUtil;
import com.absir.server.in.IDispatcher;
import com.absir.server.in.InMatcher;
import com.absir.server.in.InMethod;
import com.absir.server.in.InModel;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class RouteAdapter {

	/** routeMatchers */
	private final List<RouteMatcher> routeMatchers = new ArrayList<RouteMatcher>();

	/**
	 * @return the routeMatchers
	 */
	public List<RouteMatcher> getRouteMatchers() {
		return routeMatchers;
	}

	/**
	 * 路由匹配
	 * 
	 * @param uries
	 * @param getMethod
	 * @return
	 */
	public <T> Object[] route(String uri, IDispatcher<T> dispatcher, T req) {
		byte[] uries = uri.getBytes();
		int length = uries.length;
		int max = routeMatchers.size();
		if (max < 1) {
			return null;
		}

		int min = 0;
		if (max == 1) {
			max = compare(routeMatchers.get(0), uries, length) == 0 ? 0 : -1;

		} else {
			// find match min - max
			int m;
			int compare = 0;
			int mMin = Integer.MAX_VALUE;
			int mMax = -1;
			while (min < max) {
				m = (min + max) / 2;
				compare = compare(routeMatchers.get(m), uries, length);
				if (compare < 0) {
					if (max == m) {
						break;
					}

					max = m;

				} else {
					if (compare == 0) {
						if (mMin > m) {
							mMin = m;
						}

						if (mMax < m) {
							mMax = m;
						}
					}

					if (min == m) {
						break;
					}

					min = m;
				}
			}

			if (mMax == -1) {
				max = -1;

			} else {
				min = mMin;
				max = mMax;
			}
		}

		if (max >= 0) {
			InMethod inMethod = dispatcher.getInMethod(req);
			String parameterPath = null;
			String[] parameters = null;
			int mlen = 0, slen = 0;
			int mmlen, imlen, islen;
			RouteParameter mRouteParameter;
			RouteParameter routeParameter = null;
			boolean urlDecode = false;
			for (; max >= min; max--) {
				// 路由匹配
				RouteMatcher routeMatcher = routeMatchers.get(max);
				if (routeMatcher.find(inMethod)) {
					imlen = routeMatcher.getMapping().length;
					islen = routeMatcher.getSuffixLength();

					// 参数剩余长度
					mmlen = length - (imlen + islen);
					if (mmlen < 0) {
						continue;
					}

					// 路由参数对象
					mRouteParameter = routeMatcher.getRouteParameter();
					if (mmlen == 0) {
						if (mRouteParameter == null) {
							return new Object[] { routeMatcher, inMethod, new InModel() };
						}

						continue;
					}

					if (mRouteParameter == null) {
						continue;
					}

					// 路由参数位数
					if (mmlen < routeMatcher.getParameterLength() * 2 - 1) {
						continue;
					}

					// 通用路由参数
					if (mlen == imlen && slen == islen) {
						if (routeParameter != mRouteParameter && (parameters.length != 1 || mRouteParameter.getClass() != RouteParameter.class)) {
							routeParameter = mRouteParameter;
							parameters = routeParameter.findParameters(parameterPath);
						}

					} else {
						// 新路由参数
						if (routeMatcher.getRouteAction().isUrlDecode() && !urlDecode) {
							// 需要URL解码
							urlDecode = true;
							uri = dispatcher.decodeUri(uri, req);
							length = uri.length();
							// 检测参数位数
							if (mmlen < routeMatcher.getParameterLength() * 2 - 1) {
								continue;
							}
						}

						mlen = imlen;
						slen = islen;
						parameterPath = uri.substring(mlen, length - slen);
						String[] mParameters = mRouteParameter.findParameters(parameterPath);
						if (mParameters == null) {
							continue;
						}

						parameters = mParameters;
						routeParameter = mRouteParameter;
					}

					// 路由参数匹配
					InModel model = routeMatcher.find(parameters);
					if (model != null) {
						return urlDecode ? new Object[] { routeMatcher, inMethod, model, uri } : new Object[] { routeMatcher, inMethod, model };
					}
				}
			}
		}

		return null;
	}

	/**
	 * 注册全部匹配
	 */
	public void registerAllMatcher() {
		Collections.sort(routeMatchers, ROUTE_MATCHER_COMPARATOR);
		if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
			for (RouteMatcher routeMatcher : routeMatchers) {
				System.out.println(routeMatcher.toString());
			}
		}
	}

	/**
	 * 路由匹配比较
	 * 
	 * @param inMatcher
	 * @param uries
	 * @param length
	 * @return
	 */
	public static int compare(InMatcher inMatcher, byte[] uries, int length) {
		byte[] to = inMatcher.getMapping();
		int toLength = to.length;
		int compare = KernelUtil.compareNo(uries, to, length, toLength);
		if (compare == 0) {
			to = inMatcher.getSuffix();
			if (to != null) {
				compare = KernelUtil.compareEndNo(uries, to, length - toLength, to.length);
			}
		}

		return compare;
	}

	/** IN_MATCHER_COMPARATOR */
	public static final Comparator<InMatcher> IN_MATCHER_COMPARATOR = new Comparator<InMatcher>() {

		@Override
		public int compare(InMatcher o1, InMatcher o2) {
			// TODO Auto-generated method stub
			int compare = KernelUtil.compareNo(o1.getMapping(), o2.getMapping());
			if (compare == 0) {
				compare = KernelUtil.compareEndNoNull(o1.getSuffix(), o2.getSuffix());
				if (compare == 0) {
					compare = o1.getMapping().length + o1.getSuffixLength() - o2.getMapping().length - o2.getSuffixLength();
					if (compare == 0) {
						compare = o1.getParameterLength() - o2.getParameterLength();
					}
				}
			}

			return compare;
		}
	};

	/** ROUTE_MATCHER_COMPARATOR */
	public static final Comparator<RouteMatcher> ROUTE_MATCHER_COMPARATOR = new Comparator<RouteMatcher>() {

		@Override
		public int compare(RouteMatcher o1, RouteMatcher o2) {
			// TODO Auto-generated method stub
			int compare = IN_MATCHER_COMPARATOR.compare(o1, o2);
			if (compare == 0) {
				int len1 = o1.getInMethodLength();
				int len2 = o2.getInMethodLength();
				compare = len1 == 0 || len2 == 0 ? len1 - len2 : len2 - len1;
			}

			return compare;
		}

	};
}
