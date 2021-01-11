package com.ocirlistener.config;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConfigLoader {
	
	private static enum ENV_VARIABLES{OCIRHOST,OCIRDESTINATION};
	
	private  String getCommandLineArgumentValue(String[] args,int index) {
		if(index+1<args.length)
			return args[index+1];
		throw new IllegalArgumentException("Missing Argument Value");
	}

	private  void readFromCommandLine(String[] args) throws IllegalArgumentException  {
		for(int i = 0;i<args.length;i++) {
			switch(args[i]) {
			case "-host":
				if(ApplicationConfig.OCIR_AS_HOST==null)
					ApplicationConfig.OCIR_AS_HOST = getCommandLineArgumentValue(args, i);
				break;
			case "-url":
				if(ApplicationConfig.thirdPartyConfiguration.getRemoteEndPoint()==null)
					ApplicationConfig.thirdPartyConfiguration.setRemoteEndPoint(getCommandLineArgumentValue(args, i));
				break;
			}
		}
	}
	
	private  void readFromEnvironmentVariables() throws SecurityException {
		Map<String,String> envMap = System.getenv();
		//System.out.println(envMap);
		if(ApplicationConfig.OCIR_AS_HOST==null
				&&envMap.containsKey(ENV_VARIABLES.OCIRHOST.name()))
			ApplicationConfig.OCIR_AS_HOST = System.getenv(ENV_VARIABLES.OCIRHOST.name());
		if(ApplicationConfig.thirdPartyConfiguration.getRemoteEndPoint()==null
				&&envMap.containsKey(ENV_VARIABLES.OCIRDESTINATION.name()))
			ApplicationConfig.thirdPartyConfiguration.setRemoteEndPoint(System.getenv(ENV_VARIABLES.OCIRDESTINATION.name()));
	}
	
	
	public  void load(String[] args) throws IllegalArgumentException {
		try {
			readFromCommandLine(args);
		}catch(IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			System.out.println("Trying to fetch arguments from environment variables");
		}
		readFromEnvironmentVariables();
		if(ApplicationConfig.OCIR_AS_HOST==null||ApplicationConfig.thirdPartyConfiguration.getRemoteEndPoint()==null)
			throw new IllegalArgumentException("Missing mandatory parameters OCIRHOST,OCIRDESTINATION.");
	}
}
