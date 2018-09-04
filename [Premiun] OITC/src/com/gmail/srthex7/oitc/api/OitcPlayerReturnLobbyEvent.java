package com.gmail.srthex7.oitc.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.player.GPlayer;

public class OitcPlayerReturnLobbyEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private GPlayer player;
	private Arena arena;
	
	public OitcPlayerReturnLobbyEvent(GPlayer player, Arena arena){
		this.player = player;
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
	
	public GPlayer getPlayer() {
		return this.player;
	}
	
	public Arena getArena() {
		return this.arena;
	}
}
