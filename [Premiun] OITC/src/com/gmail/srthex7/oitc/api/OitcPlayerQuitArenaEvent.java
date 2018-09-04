package com.gmail.srthex7.oitc.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.player.GPlayer;

public class OitcPlayerQuitArenaEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private GPlayer player;
	private Arena arena;
	private LeaveReason reason;
	
	public OitcPlayerQuitArenaEvent(GPlayer player, Arena arena, LeaveReason reason){
		this.player = player;
		this.arena = arena;
		this.reason = reason;
	}
	
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public GPlayer getPlayer() {
		return this.player;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public LeaveReason getLeaveReason() {
		return this.reason;
	}
}
