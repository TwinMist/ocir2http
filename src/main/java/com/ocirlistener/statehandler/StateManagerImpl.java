package com.ocirlistener.statehandler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ocirlistener.models.ApplicationState;
import com.ocirlistener.models.ApplicationStatus;
@Component
public class StateManagerImpl implements StateManager {

	@Autowired
	ApplicationStatus applicationStatus;
	
	@Override
	public void changeState(ApplicationState state) {
		applicationStatus.setState(state);
		applicationStatus.setLastModifiedAt(LocalDate.now());
	}

	@Override
	public  ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

}
