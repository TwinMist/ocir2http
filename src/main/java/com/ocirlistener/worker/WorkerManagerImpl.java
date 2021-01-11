package com.ocirlistener.worker;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ocirlistener.models.ApplicationState;
import com.ocirlistener.statehandler.StateManager;


@Component
public class WorkerManagerImpl implements WorkerManager {

	@Autowired
	StateManager stateManager;
	@Autowired 
	ObjectFactory<Worker> workerFactory; 
	
	@Override
	public void startWorker() {
		// TODO Auto-generated method stub
		if(!Worker.isRunning.get()) {
			Thread worker = workerFactory.getObject();
			stateManager.changeState(ApplicationState.STARTING);
			Worker.isRunning.set(true);
			worker.start();	
		}		
	}

	@Override
	public void stopWorker() {
		if(Worker.isRunning.get()) {
			stateManager.changeState(ApplicationState.STOPPING);
			Worker.isRunning.set(false);
			System.out.println("stopping---"+Worker.isRunning.get());
		}		
	}

}
