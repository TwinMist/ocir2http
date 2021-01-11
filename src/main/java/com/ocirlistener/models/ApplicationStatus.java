package com.ocirlistener.models;

import java.time.LocalDate;

public class ApplicationStatus {
	private LocalDate lastModifiedAt;
	private ApplicationState state;
	private String modifiedBy;
	public LocalDate getLastModifiedAt() {
		return lastModifiedAt;
	}
	public void setLastModifiedAt(LocalDate lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}
	public ApplicationState getState() {
		return state;
	}
	public void setState(ApplicationState state) {
		this.state = state;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	

}
