package com.gmail.srthex7.oitc.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.player.GPlayer;

public class OitcPlayerDeathEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	
	private GPlayer killer;
	private GPlayer player;
	private Arena arena;
	private EntityDamageByEntityEvent event;
	
	public OitcPlayerDeathEvent(GPlayer player, GPlayer killer, Arena arena, EntityDamageByEntityEvent event){
		this.player = player;
		this.killer = killer;
		this.arena = arena;
		this.event = event;
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
	
	public GPlayer getKiller() {
		return this.killer;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public EntityDamageByEntityEvent getEntityDamageByEntityEvent() {
		return this.event;
	}
}
