package com.gmail.srthex7.oitc.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.srthex7.oitc.system.Arena;

public class OitcFinishEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	
	private Arena arena;
	
	public OitcFinishEvent(Arena arena) {
		this.arena = arena;
	}
	
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public Arena getArena(){
		return this.arena;
	}
}
