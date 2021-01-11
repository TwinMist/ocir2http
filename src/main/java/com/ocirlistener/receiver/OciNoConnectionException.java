package com.ocirlistener.receiver;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;

public class OciNoConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 112314124144L;

	public OciNoConnectionException(ConnectException e) {
		// TODO Auto-generated constructor stub
	}

	public OciNoConnectionException(NoRouteToHostException e) {
		// TODO Auto-generated constructor stub
	}

	public OciNoConnectionException(IOException ex) {
		// TODO Auto-generated constructor stub
	}

	public OciNoConnectionException(Exception e) {
		// TODO Auto-generated constructor stub
	}
	
	public OciNoConnectionException(String message) {
		super(message);
	}

}
