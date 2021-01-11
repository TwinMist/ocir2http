package com.ocirlistener.statehandler;

import com.ocirlistener.models.ApplicationState;
import com.ocirlistener.models.ApplicationStatus;

public interface StateManager {
	ApplicationStatus getApplicationStatus();
	void changeState(ApplicationState state);
}
