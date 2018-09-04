package com.gmail.srthex7.oitc.system;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.srthex7.oitc.api.OitcFinishEvent;
import com.gmail.srthex7.oitc.scoreboard.Board;
import com.gmail.srthex7.oitc.system.lobby.Utilities;

public class Task extends BukkitRunnable{
	
	@Override
	public void run() {
		for (Arena arena : Arena.getArenas()) {
			if (arena.getArenaState().equals(ArenaState.INGAME)) {
				
				arena.timeToEnd--;
				arena.timeToRandomizeTargets--;
				
				if (arena.timeToEnd == 0) {
					Bukkit.getPluginManager().callEvent(new OitcFinishEvent(arena));
				}
				
				if (arena.timeToRandomizeTargets == 0) {
					arena.randomizeTargets();
				}
				
				if (arena.getPlayerUuids().size() == 1) {
					arena.finish();
				} else if (arena.getPlayerUuids().size() == 0) {
					arena.reset();
				}
				
				Board.updateScoreboardInGame(arena);
			}
		}
		
		Utilities.updateRoomsInventory();
		
	}
	
	
	
}
