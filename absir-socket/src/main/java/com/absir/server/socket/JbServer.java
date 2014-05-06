/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-18 下午2:31:55
 */
package com.absir.server.socket;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JiActive;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbServer extends JbBean implements JiActive {

	@JaLang("服务器名称")
	private String name;

	@JaLang("端口号")
	private int port;

	@JaLang("服务器ip")
	private String ip;

	@JaLang("服务器ipV6")
	private String ipV6;

	@JaLang("开始时间")
	private long beginTime;

	@JaLang("关闭时间")
	private long passTime;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the ipV6
	 */
	public String getIpV6() {
		return ipV6;
	}

	/**
	 * @return
	 * @throws UnknownHostException
	 */
	public InetAddress getInetAddress() throws UnknownHostException {
		if (!KernelString.isEmpty(ipV6)) {
			return Inet6Address.getByName(ipV6);
		}

		if (!KernelString.isEmpty(ip)) {
			return Inet6Address.getByName(ip);
		}

		return null;
	}

	/**
	 * @return the beginTime
	 */
	public long getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the passTime
	 */
	public long getPassTime() {
		return passTime;
	}

	/**
	 * @param passTime
	 *            the passTime to set
	 */
	public void setPassTime(long passTime) {
		this.passTime = passTime;
	}
}
