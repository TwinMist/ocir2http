package com.ocirlistener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocirlistener.config.ApplicationConfig;
import com.ocirlistener.models.ApplicationStatus;
import com.ocirlistener.worker.WorkerManagerImpl;

@RequestMapping("/ocir2http")
@RestController
public class AdminController {
	
	@Autowired
	private ApplicationContext appContext;
	
	@GetMapping("/config")
	String getActiveConfig() {
		return ApplicationConfig.getConfiguration();
	}
	
	@GetMapping("/status")
	ApplicationStatus status() {
		return appContext.getBean(ApplicationStatus.class);
	}
	
	@GetMapping("/ping")
	String ping() {
	    return "Keep-Alive";
	}
	
	@GetMapping("/start")
	ApplicationStatus startApplication() {
		appContext.getBean(WorkerManagerImpl.class).startWorker();
		return appContext.getBean(ApplicationStatus.class);
	}
	
	@GetMapping("/stop")
	ApplicationStatus stopApplication() {
		appContext.getBean(WorkerManagerImpl.class).stopWorker();
		return appContext.getBean(ApplicationStatus.class);
	}
	
	@GetMapping("/kill")
	void killApplication() {
		appContext.getBean(WorkerManagerImpl.class).stopWorker();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(1);
	}
}
