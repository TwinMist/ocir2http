package com.ocirlistener.config;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.ocirlistener.models.ThirdPartyConfiguration;


public class ApplicationConfig {
	public static int THIRDPARTY_CONNECT_RETRY_IN_SECONDS=3;
	public static int THIRDPARTY_MAX_RECONNECTS=10;
	public static String OCIR_CHARSET="iso-8859-1";
	public static int OCIR_CONNECT_RETRY_IN_SECONDS=10;
	public static int OCIR_MAX_RECONNECTS=10;
	public static String OCIR_AS_HOST=null;
	public static int OCIR_AS_PORT=8025;
	public static int OCI_LISTENER_NO_RESPONSE_ALARM_COUNTER=40;
	public static ThirdPartyConfiguration thirdPartyConfiguration = new ThirdPartyConfiguration();	
	
	public static String getConfiguration() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\nOCIR_AS_HOST:"+OCIR_AS_HOST);
		sb.append("\r\nOCIR_AS_HOST:"+OCIR_AS_HOST);
		sb.append("\r\nOCIR_CHARSET:"+OCIR_CHARSET);
		sb.append("\r\nOCIR_MAX_RECONNECTS:"+OCIR_MAX_RECONNECTS);
		sb.append("\r\nOCIR_CONNECT_RETRY_IN_SECONDS:"+OCIR_CONNECT_RETRY_IN_SECONDS);
		sb.append("\r\nOCI_LISTENER_NO_RESPONSE_ALARM_COUNTER:"+OCI_LISTENER_NO_RESPONSE_ALARM_COUNTER);
		sb.append("\r\nTHIRDPARTY_ENDPOINT:"+thirdPartyConfiguration.getRemoteEndPoint());
		sb.append("\r\nTHIRDPARTY_CONNECT_TIMEOUT:"+thirdPartyConfiguration.getConnectTimeOut());
		sb.append("\r\nTHIRDPARTY_READ_TIMEOUT:"+thirdPartyConfiguration.getReadTimeOut());
		sb.append("\r\nTHIRDPARTY_CONNECT_RETRY_IN_SECONDS:"+THIRDPARTY_CONNECT_RETRY_IN_SECONDS);
		sb.append("\r\nTHIRDPARTY_MAX_RECONNECTS:"+THIRDPARTY_MAX_RECONNECTS);
		return sb.toString();
	}
	
	public static String getStackTraceString(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}	
}
