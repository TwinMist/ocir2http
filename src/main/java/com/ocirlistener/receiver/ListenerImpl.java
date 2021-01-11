package com.ocirlistener.receiver;

/*File Name: OciListener.class
 Author : TCS
 Company : TCS
 Description : This class acts as a listener that catches user configuration change events from the Application Server.       
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ocirlistener.config.ApplicationConfig;
import com.ocirlistener.connectorframework.MaximumRetriesReachedException;
import com.ocirlistener.connectorframework.ThirdPartyConnector;

@Component
public class ListenerImpl implements Listener {

	/**
	 * @param args
	 */
	private Socket m_socket;
	private BufferedReader m_inStream;
	private ByteArrayOutputStream m_outStream = new ByteArrayOutputStream();  
	String outStream = "";
	private int noResponseCount;
	@Autowired
	private ObjectFactory<ThirdPartyConnector<Void>> objectFactory;
	private static Logger LISTENER_LOGGER = Logger.getLogger("listenerLogger");
	
	
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e1) {
			LISTENER_LOGGER.error(ApplicationConfig.getStackTraceString(e1));
		}
	}

	public void reconnect(String reason) {
		LISTENER_LOGGER.info("Reconnecting: reason :"+ reason);
		disconnect();
		connect();

	}
	
	private void noResponseRestart() throws OciNoConnectionException{
		if (noResponseCount == ApplicationConfig.OCI_LISTENER_NO_RESPONSE_ALARM_COUNTER){
			//reconnect("No response received from AS for " + noResponseCount + " seconds");
			throw new OciNoConnectionException("No response received from AS for " + noResponseCount + " seconds");
		}else{
			System.out.println("Waiting for response");
			noResponseCount+=1;
			sleep(1);
		}
	}
	
	public ThirdPartyConnector<Void> getThirdPartyConnector() {
		ThirdPartyConnector<Void> connector = objectFactory.getObject();
		try {
			connector.transact("", "");
		} catch (MaximumRetriesReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connector;
	}
	
	@Override
	public void startProcessing() throws MaximumRetriesReachedException, OciNoConnectionException {
			try{
				//if(m_inStream.ready()) {
					outStream = m_inStream.readLine();
					if(outStream == null){
						throw new OciNoConnectionException("Exception while reading from AS.");		
					}else {
						noResponseCount = 0;
						if(outStream.endsWith("<BroadsoftOCIReportingDocument")){
							m_outStream.reset();
						}else if(outStream.endsWith("</BroadsoftOCIReportingDocument>\n")||(outStream.endsWith("</BroadsoftOCIReportingDocument>"))){
							if (outStream
								.contains("OCIReportingServerStatusNotification")) {
								LISTENER_LOGGER.info("OCIReportingServerStatusNotification : Keep Alive");
							} else {
								LISTENER_LOGGER.info(outStream);
								objectFactory.getObject().transact(ApplicationConfig.thirdPartyConfiguration.getRemoteEndPoint(),outStream);
								outStream = "";
							}
						}
					}
				//} else {
				//	System.out.println("Dont receive response from AS");
				//	throw new OciNoConnectionException("Exception while reading from AS.");		
				//}
			}catch(MaximumRetriesReachedException ex) {
				LISTENER_LOGGER.error("Maximum retries reached.");
				disconnect();
				throw ex;
			}catch(SocketTimeoutException ex) {
				throw new OciNoConnectionException("Exception while reading from AS.");				
			}catch (Exception e) {
				LISTENER_LOGGER.error("Error in reading from AS.");
				LISTENER_LOGGER.error(ApplicationConfig.getStackTraceString(e));
				//sleep(1);
				throw new OciNoConnectionException("Exception while reading from AS.");
			}
	}

	@Override
	public boolean connect() {
		try {
			LISTENER_LOGGER.info("Connecting to AS : " + ApplicationConfig.OCIR_AS_HOST + "Port:"+ApplicationConfig.OCIR_AS_PORT);
			m_socket = new Socket();
			m_socket.connect(new InetSocketAddress(ApplicationConfig.OCIR_AS_HOST,ApplicationConfig.OCIR_AS_PORT),2000);
			LISTENER_LOGGER.info("Connected");
			m_socket.setSoTimeout(40000);
			m_inStream = new BufferedReader(new InputStreamReader(m_socket.getInputStream(),ApplicationConfig.OCIR_CHARSET));
			noResponseCount = 0;
		} catch (Exception e) {		
			LISTENER_LOGGER.info("Unable To Connect");
			LISTENER_LOGGER.error(ApplicationConfig.getStackTraceString(e));
			disconnect();
			LISTENER_LOGGER.error("Waiting for "+ApplicationConfig.OCIR_CONNECT_RETRY_IN_SECONDS+" seconds brfore reconnecting");
			return false;
		}
		return true;
	}

	@Override
	public boolean disconnect() {
		try {
			if (m_socket != null) {
				if (m_socket.isConnected()){
					m_socket.shutdownOutput();
				}
				if(!m_socket.isClosed())
					m_socket.close();
			}
		} catch (IOException e) {
			LISTENER_LOGGER.error("Error in disconnecting socket"+ApplicationConfig.getStackTraceString(e));
			return false;
		} finally {
			m_socket = null;
		}
		return true;
	}
}