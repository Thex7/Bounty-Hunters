package com.gmail.srthex7.oitc.utils.vanish;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.srthex7.oitc.game.Settings;
import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.lobby.Lobby;

public class VanishSystem {

	//====================================
	// Codigo reciente (16-08-18)
	//====================================
	
	/**
	 * Utilizar cuando un jugador entra en una arena
	 * @param player jugador que ingresa a la arena
	 * @param arena arena a la que entra el jugador
	 */
	public static void playerJoinArena (Player player, Arena arena) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (arena.getPlayerUuids().contains(p.getUniqueId())) {
				player.showPlayer(p);
				p.showPlayer(player);
			} else {
				player.hidePlayer(p);
				p.hidePlayer(player);
			}
		}
	}
	
	/**
	 * Utilizar cuando un jugador sale de una arena
	 * @param player jugador que sale de la arena
	 * @param arena arena de la que sale el jugador
	 */
	public static void playerQuitArena (Player player, Arena arena) {
		for (UUID uuid : arena.getPlayerUuids()) {
			Player p = Bukkit.getPlayer(uuid);
			if (p != null) {
				player.hidePlayer(p);
				p.hidePlayer(player);
			}
		}
	}
	
	/**
	 * Utilizar cuando un jugador entra al lobby
	 * sin salir del servidor
	 * @param player jugador que ingresa al lobby
	 */
	public static void playerJoinLobby (Player player) {
		for (UUID uuid : Lobby.getUuids()) {
			Player p = Bukkit.getPlayer(uuid);
			if(p == null) continue;
			player.showPlayer(p);
			p.showPlayer(player);
		}
	}
	
	/**
	 * Utilizar cuando un jugador entra al servidor
	 * @param player jugador que entra al servidor
	 */
	public static void playerJoinServer (Player player) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p == null) continue;
			if (Lobby.getUuids().contains(p.getUniqueId())) continue;
			player.hidePlayer(p);
			p.hidePlayer(player);
		}
	}
	
}
