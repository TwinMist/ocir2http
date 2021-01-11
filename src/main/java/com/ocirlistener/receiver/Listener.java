package com.ocirlistener.receiver;

import com.ocirlistener.connectorframework.MaximumRetriesReachedException;

public interface Listener {
	boolean connect();
	void startProcessing() throws MaximumRetriesReachedException, OciNoConnectionException;
	boolean disconnect();
}
