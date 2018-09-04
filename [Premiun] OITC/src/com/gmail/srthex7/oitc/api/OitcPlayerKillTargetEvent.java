package com.gmail.srthex7.oitc.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.player.GPlayer;

public class OitcPlayerKillTargetEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	
	private GPlayer target;
	private GPlayer killer;
	private Arena arena;
	
	public OitcPlayerKillTargetEvent(GPlayer killer, GPlayer target, Arena arena){
		this.killer = killer;
		this.target = target;
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
	
	public GPlayer getKiller() {
		return this.killer;
	}
	
	public GPlayer getTarget() {
		return this.target;
	}
	
	public Arena getArena() {
		return this.arena;
	}
}
