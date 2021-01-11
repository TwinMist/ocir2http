package com.ocirlistener.models;

public class ThirdPartyConfiguration {
	private String remoteEndPoint;
	private int connectTimeOut;
	private int readTimeOut;
	public String getRemoteEndPoint() {
		return remoteEndPoint;
	}
	public void setRemoteEndPoint(String remoteEndPoint) {
		this.remoteEndPoint = remoteEndPoint;
	}
	public int getConnectTimeOut() {
		return connectTimeOut;
	}
	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}
	public int getReadTimeOut() {
		return readTimeOut;
	}
	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}
	
}
