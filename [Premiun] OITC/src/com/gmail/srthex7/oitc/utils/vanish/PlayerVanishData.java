package com.gmail.srthex7.oitc.utils.vanish;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerVanishData {

	private boolean invanish = false;
	private List<UUID> exceptions = new ArrayList<UUID>();
	private UUID uuid;
	
	public PlayerVanishData (UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public boolean inVanish() {
		return invanish;
	}
	
	public void setVanish(boolean invanish) {
		this.invanish = invanish;
	}
	
	public List<UUID> getExceptions() {
		return exceptions;
	}
	
	public void setExceptions(List<UUID> exceptions) {
		this.exceptions = exceptions;
	}
	
}
