package com.gmail.srthex7.oitc.system;

public enum ArenaState {
	
	WAITING("Waiting..."),
	STARTING("Starting"), 
	INGAME("Ingame"), 
	ENDING("Ending"), 
	RESTARTING("Restarting");
	
	private String statename;
	
	private ArenaState(String statename) {
		this.statename = statename;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}
	
	@Override
	public String toString() {
		return this.statename;
	}
}
