package com.ocirlistener;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.ocirlistener.config.ConfigLoader;
import com.ocirlistener.worker.WorkerManagerImpl;

@SpringBootApplication
@ComponentScan(basePackages = "com.ocirlistener")
public class OcirListenerApplication {
	
	private static String[] commandLineArgs;
	public static void main(String[] args) {
		commandLineArgs = args;
		SpringApplication.run(OcirListenerApplication.class, args);
	}
	
	@Autowired
	private ApplicationContext appContext;
	
	@PostConstruct
    private void init() {
		System.out.println("starting ocir2http");
		appContext.getBean(ConfigLoader.class).load(commandLineArgs);
		appContext.getBean(WorkerManagerImpl.class).startWorker();
		System.out.println("started");
	}
}
