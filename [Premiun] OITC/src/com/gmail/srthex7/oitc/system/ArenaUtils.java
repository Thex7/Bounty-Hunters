package com.gmail.srthex7.oitc.system;

import org.bukkit.Bukkit;

import com.gmail.srthex7.oitc.system.player.GPlayer;

public class ArenaUtils {

	//Nombre que se mostrara cuando el usuario no tenga
	
	/**
	 * Obtiene una arena disponible
	 * @return
	 */
	public static Arena getArenaAvailable() {
		for (Arena arena : Arena.getArenas()) {
			if (!arena.isAvailable()) continue;
				return arena; 
		}
		return null;
	}
	
	/**
	 * Envia a un jugador a la arena
	 * @param gplayer Jugador
	 * @param arena Arena a donde se quiere enviar al jugador
	 */
	public static void sendPlayerArena(GPlayer gplayer, Arena arena) {
		arena.addPlayerUUID(gplayer.getUuid());
		Bukkit.getPlayer(gplayer.getUuid()).teleport(arena.getInitialspawn());
		gplayer.setArena(arena);
	}
	
	/**
	 * Obtiene la arena que esta mas proxima a iniciar
	 */
	public static Arena getReadyArena() {
		Arena result = null;
		for (Arena arena : Arena.getArenas()) {
			if (!arena.isAvailable()) continue;
			if (result != null) {
				if (result.getPlayerUuids().size() >= result.getPlayerUuids().size()) {
					result = arena;
				}
			} else {
				result = arena;
			}
		}
		return result;
	}
	
	public static void sendPlayerToAvailableArena(GPlayer gplayer) {
		sendPlayerArena(gplayer,getReadyArena());
	}
	
	public static Arena getArenaFromName(String name) {
		for (Arena arena : Arena.getArenas()) {
			if (!arena.getArenaname().equals(name)) continue;
			return arena;
		}
		return null;
	}
	
	public static void sendPlayerToArenaName(GPlayer gplayer, String name) {
		sendPlayerArena(gplayer, getArenaFromName(name));
	}
}
