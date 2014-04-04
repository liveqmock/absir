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

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
@EntityListeners(SocketServerContext.Listener.class)
@MappedSuperclass
public class JbServer extends JbBean {

	@JaLang("服务器名称")
	private String name;

	@JaLang("端口号")
	private int port;

	@JaLang("服务器ip")
	private String ip;

	@JaLang("服务器ipV6")
	private String ipV6;

	@JaLang("开启时间")
	private long startTime;

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
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
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
}
