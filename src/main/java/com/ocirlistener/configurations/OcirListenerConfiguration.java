package com.ocirlistener.configurations;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import com.ocirlistener.models.ApplicationStatus;

@Configuration
public class OcirListenerConfiguration {
	
//	@Bean
//    public Function<String, ThirdPartyConnector<Void>> myPrototypeFactory() {
//        return arg -> getConnector(arg);
//    }
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ApplicationStatus getApplicationStatus(){
		return new ApplicationStatus();
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}
