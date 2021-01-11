package com.ocirlistener.worker;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ocirlistener.config.ApplicationConfig;
import com.ocirlistener.connectorframework.MaximumRetriesReachedException;
import com.ocirlistener.models.ApplicationState;
import com.ocirlistener.receiver.Listener;
import com.ocirlistener.receiver.OciNoConnectionException;
import com.ocirlistener.statehandler.StateManager;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Worker extends Thread {

	public static AtomicBoolean isRunning = new AtomicBoolean(false);
	
	@Autowired
	private Listener listener;
	@Autowired
	private StateManager stateManager;
	private int noResponseCount = 0;
	private static Logger LISTENER_LOGGER = Logger.getLogger("listenerLogger");

	
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e1) {
			LISTENER_LOGGER.error(ApplicationConfig.getStackTraceString(e1));
		}
	}
	
	private boolean isNoResponseRestartRequired() {
		if (noResponseCount < ApplicationConfig.OCI_LISTENER_NO_RESPONSE_ALARM_COUNTER){
			noResponseCount+=1;
			sleep(1);
			return  false;
		}
		return true;
	}
	
	private void process() throws MaximumRetriesReachedException {
		stateManager.changeState(ApplicationState.PROCESSING);
		while(true) {
			if(isRunning.get()) {
				try {
					listener.startProcessing();
					noResponseCount =1;
				} catch (OciNoConnectionException e) {
					if(isNoResponseRestartRequired()) {
						break;
					}
				}
			}else {
				System.out.println("breaking process");
				break;
			}
		}
		listener.disconnect();
		stateManager.changeState(ApplicationState.DISCONNECTED);
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId()+"---singelton loop---"+listener);
		while(isRunning.get()) {
			if(listener.connect()) {
				//if connected
				stateManager.changeState(ApplicationState.CONNECTED);
				try {
					process();
					if(!isRunning.get())
						break;
				}catch(MaximumRetriesReachedException ex) {
					System.out.println("Max retries in worker caught");
					listener.disconnect();
					break;
				}
			}else {
				//wait for n seconds before attempting reconnect
				//try to connect infinitely
				stateManager.changeState(ApplicationState.TRYING_TO_CONNECT);				
				sleep(10);
			}
		}
		stateManager.changeState(ApplicationState.STOPPED);
		isRunning.set(false);
		System.out.println("exiting thread");
	}
}
