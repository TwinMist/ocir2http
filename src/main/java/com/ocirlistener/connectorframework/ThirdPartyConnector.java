package com.ocirlistener.connectorframework;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.ocirlistener.config.ApplicationConfig;

public abstract class ThirdPartyConnector<T> {
	private static Logger HTTP_LOGGER = Logger.getLogger("httpLogger");
	protected abstract ResponseEntity<T> sendRecv(String url,String report) throws ConnectorException;
	protected abstract boolean validateResponse(ResponseEntity<T> response);
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e1) {
			System.out.println(ApplicationConfig.getStackTraceString(e1));
		}
	} 

	private boolean doProcess(String url,String report) {
		try {
			ResponseEntity<T> response = sendRecv(url,report);
			HTTP_LOGGER.info(response);
			if(response!=null)
				return validateResponse(response);
		} catch (ConnectorException e) {
			HTTP_LOGGER.error(ApplicationConfig.getStackTraceString(e));
		}
		return false;
	}
	
	public final void transact(String url,String report) throws MaximumRetriesReachedException {
		int failedAttempts = 0;
		do {
			if(!doProcess(url,report)) {
				sleep(ApplicationConfig.THIRDPARTY_CONNECT_RETRY_IN_SECONDS);
				failedAttempts++;
			}else {
				failedAttempts = 0;
			}
		}while(failedAttempts>0&&failedAttempts<ApplicationConfig.THIRDPARTY_MAX_RECONNECTS);
		
		if(failedAttempts>=ApplicationConfig.THIRDPARTY_MAX_RECONNECTS)
			throw new MaximumRetriesReachedException();
	}
}
