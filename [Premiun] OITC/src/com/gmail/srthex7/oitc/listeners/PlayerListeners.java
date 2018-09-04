package com.gmail.srthex7.oitc.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.srthex7.oitc.OITC;
import com.gmail.srthex7.oitc.api.OitcPlayerJoinArenaEvent;
import com.gmail.srthex7.oitc.api.OitcPlayerQuitArenaEvent;
import com.gmail.srthex7.oitc.api.OitcPlayerReturnLobbyEvent;
import com.gmail.srthex7.oitc.api.OitcPreStartEvent;
import com.gmail.srthex7.oitc.api.LeaveReason;
import com.gmail.srthex7.oitc.game.Lang;
import com.gmail.srthex7.oitc.game.Settings;
import com.gmail.srthex7.oitc.scoreboard.Board;
import com.gmail.srthex7.oitc.stats.flat.Flat;
import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.ArenaState;
import com.gmail.srthex7.oitc.system.ArenaUtils;
import com.gmail.srthex7.oitc.system.lobby.Lobby;
import com.gmail.srthex7.oitc.system.lobby.Utilities;
import com.gmail.srthex7.oitc.system.player.GPlayer;
import com.gmail.srthex7.oitc.utils.vanish.VanishSystem;

public class PlayerListeners implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Player player = e.getPlayer();
		Lobby.getUuids().add(player.getUniqueId());
		
		GPlayer gplayer = new GPlayer(e.getPlayer().getUniqueId());
		//sync stats
			if (Settings.savestats.equals("flat")) {
				if (Flat.isregister(gplayer)) {
					Flat.load(gplayer);
				} else {
					Flat.register(gplayer, player);
				}
			}
		//...
		gplayer.add();
		
		player.getInventory().clear();
		player.setHealth(20.0D);
		Lobby.sendItems(player);
		
		VanishSystem.playerJoinServer(e.getPlayer());
		if (Settings.autoArena) {
			Arena arena = ArenaUtils.getReadyArena();
			if (arena != null) {
				ArenaUtils.sendPlayerArena(gplayer, arena);
			}
		} else {
			if (Lobby.getSpawnpoint() != null) {
				player.teleport(Lobby.getSpawnpoint());
			}
			//Scoreboard
			Bukkit.getScheduler().scheduleSyncDelayedTask(OITC.getInstance(), () -> {
				Board.setScoreboardLobby(e.getPlayer());
			},1l);
		}
		
	
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		GPlayer gplayer = GPlayer.getGPlayer(e.getPlayer().getUniqueId());
	
		if (gplayer.getArena() != null) {
			gplayer.getArena().removePlayerUUID(gplayer.getUuid(), LeaveReason.DISCONNECTED);
		}
		
		if (gplayer != null) {
			gplayer.delete();
		}	
		
		if (Lobby.getUuids().contains(e.getPlayer().getUniqueId())) {
			Lobby.getUuids().remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		e.setLeaveMessage(null);
		GPlayer gplayer = GPlayer.getGPlayer(e.getPlayer().getUniqueId());
		
		if (gplayer.getArena() != null) {
			gplayer.getArena().removePlayerUUID(gplayer.getUuid(), LeaveReason.DISCONNECTED);
		}
		
		if (gplayer != null) {
			gplayer.delete();
		}	
		
		if (Lobby.getUuids().contains(e.getPlayer().getUniqueId())) {
			Lobby.getUuids().remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onOitcPlayerJoinArena(OitcPlayerJoinArenaEvent e) {
		Player player = Bukkit.getPlayer(e.getPlayer().getUuid());
		Lobby.getUuids().remove(e.getPlayer().getUuid());
		player.getInventory().clear();
		
		VanishSystem.playerJoinArena(player, e.getArena());
		
		if (e.getArena().getMinusers() <= e.getArena().getPlayerUuids().size() && e.getArena().getArenaState().equals(ArenaState.WAITING)) {
			Bukkit.getPluginManager().callEvent(new OitcPreStartEvent(e.getArena()));
		}
		
		//Scoreboard
		Board.removeAllEntry(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OITC.getInstance(), () -> {
			for (UUID uuid : e.getArena().getPlayerUuids()) {
				Player p = Bukkit.getPlayer(uuid);
				if (p != null) Board.setScoreboardPreGame(p, e.getArena());
			}
		},2l);
		
		//message
		e.getArena().broadcastMessage(Lang.playerjoinarena.replaceAll("<player>", player.getName())
				.replaceAll("<maxplayers>", e.getArena().getMaxusers()+"")
				.replaceAll("<onlineplayers>", e.getArena().getPlayerUuids().size()+""));
	}
	
	@EventHandler
	public void onOitcPlayerQuitArena(OitcPlayerQuitArenaEvent e) {
		
		if (e.getArena().getArenaState().equals(ArenaState.INGAME)) {
			if (e.getArena().getPlayerUuids().size() <= 1) {
				e.getArena().finish();
			}
			
		}
		
		//sync stats
		if (Settings.savestats.equals("flat")) {
			if (e.getArena().getArenaState().equals(ArenaState.INGAME)) {
		//		e.getPlayer().setLosses(e.getPlayer().getLosses()+1);				
			}
			Flat.syncStats(e.getPlayer());
		}
		
		//Scoreboard
		if (e.getArena().getArenaState().equals(ArenaState.WAITING) || e.getArena().getArenaState().equals(ArenaState.STARTING)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(OITC.getInstance(), () -> {
				for (UUID uuid : e.getArena().getPlayerUuids()) {
					Player p = Bukkit.getPlayer(uuid);
					if (p != null && p != Bukkit.getPlayer(uuid)) Board.setScoreboardPreGame(p, e.getArena());
				}
			},2l);
		}
		
		if (!e.getLeaveReason().equals(LeaveReason.DISCONNECTED)) {
			VanishSystem.playerQuitArena(Bukkit.getPlayer(e.getPlayer().getUuid()), e.getArena());
			if (!Settings.autoArena) Bukkit.getPluginManager().callEvent(new OitcPlayerReturnLobbyEvent(e.getPlayer(),e.getArena()));
		}		
		
		//message
		if (e.getArena().getArenaState().equals(ArenaState.INGAME) && !e.getLeaveReason().equals(LeaveReason.DISCONNECTED)) {
			e.getArena().broadcastMessage(Lang.playerleftarenaingame.replaceAll("<player>", Bukkit.getPlayer(e.getPlayer().getUuid()).getName())
					.replaceAll("<maxplayers>", e.getArena().getMaxusers()+"")
					.replaceAll("<onlineplayers>", e.getArena().getPlayerUuids().size()+""));
		}
		
		if (e.getArena().getArenaState().equals(ArenaState.STARTING) || e.getArena().getArenaState().equals(ArenaState.WAITING)) {
			if (!e.getLeaveReason().equals(LeaveReason.DISCONNECTED)) {
				e.getArena().broadcastMessage(Lang.playerleftarena.replaceAll("<player>", Bukkit.getPlayer(e.getPlayer().getUuid()).getName())
						.replaceAll("<maxplayers>", e.getArena().getMaxusers()+"")
						.replaceAll("<onlineplayers>", e.getArena().getPlayerUuids().size()+""));
			}
		}
	}
	
	@EventHandler
	public void onOitcPlayerReturnLobby(OitcPlayerReturnLobbyEvent e) {
		Player player = Bukkit.getPlayer(e.getPlayer().getUuid());
		
		VanishSystem.playerJoinLobby(player);
		
		Lobby.getUuids().add(e.getPlayer().getUuid());
		Lobby.sendItems(player);
		
		//Scoreboard
		Board.removeAllEntry(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OITC.getInstance(), () -> {
			Board.setScoreboardLobby(player);
		},2l);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDropItem(BlockPlaceEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (e.getPlayer().getItemInHand() != null) {
			if (e.getPlayer().getItemInHand().equals(Utilities.getSearchRoom())) {
				e.setCancelled(true);
				e.getPlayer().openInventory(Utilities.getRoomsInventory());
			}
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) return;
		Player player = ((Player)e.getWhoClicked());
		
		if (Lobby.getUuids().contains(player.getUniqueId())) {
			e.setCancelled(true);
		}
		
		if (e.getInventory().getTitle() != null) {
			if (e.getInventory().getTitle().equals(Utilities.inventoryTitle)) {
				if (e.getCurrentItem() == null) return;
				if (e.getCurrentItem().getType().equals(Material.WOOL))
				ArenaUtils.sendPlayerToArenaName(GPlayer.getGPlayer(player.getUniqueId()), ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
			}
		}
	}
	
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}
}
