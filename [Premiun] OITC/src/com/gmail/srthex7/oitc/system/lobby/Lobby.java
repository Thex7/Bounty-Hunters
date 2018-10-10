package com.gmail.srthex7.oitc.system.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.srthex7.multicore.File.ConfigUtil;
import com.gmail.srthex7.multicore.Regions.Loc;
import com.gmail.srthex7.oitc.OITC;
import com.gmail.srthex7.oitc.api.LeaveReason;
import com.gmail.srthex7.oitc.system.Arena;

public class Lobby {

	private static List<UUID> uuids = new ArrayList<>();
	
	public static Location spawnpoint;

	public static List<UUID> getUuids() {
		return uuids;
	}

	public static Location getSpawnpoint() {
		return spawnpoint;
	}

	public static void setSpawnpoint(Location spawnpoint) {
		ConfigUtil cu = new ConfigUtil(OITC.getInstance(),"settings");
		cu.getConfig().set("lobby", Loc.serializeLoc(spawnpoint));
		cu.save();
		Lobby.spawnpoint = spawnpoint;
	
	}
	
	public static void callPlayersInArena (Arena arena) {
		for (UUID uuid : arena.getPlayerUuids()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null && spawnpoint != null) {
				player.teleport(spawnpoint);
				arena.removePlayerUUID(uuid, LeaveReason.ENDGAME);
			}
		}
	}
	
	public static void sendItems(Player player) {
		player.getInventory().clear();
		player.getInventory().setItem(0, Utilities.getSearchRoom());
	}
	
	public static void broadcastMessage(String message) {
		for (UUID uuid : uuids) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				player.sendMessage(message);
			}
		}
	}
}
