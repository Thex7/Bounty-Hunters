package com.gmail.srthex7.oitc.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.srthex7.multicore.Others.Utils;
import com.gmail.srthex7.oitc.OITC;
import com.gmail.srthex7.oitc.api.OitcFinishEvent;
import com.gmail.srthex7.oitc.api.OitcPlayerDeathEvent;
import com.gmail.srthex7.oitc.api.OitcPlayerKillTargetEvent;
import com.gmail.srthex7.oitc.api.OitcPreStartEvent;
import com.gmail.srthex7.oitc.api.OitcStartEvent;
import com.gmail.srthex7.oitc.game.Items;
import com.gmail.srthex7.oitc.game.Lang;
import com.gmail.srthex7.oitc.game.Settings;
import com.gmail.srthex7.oitc.game.Sounds;
import com.gmail.srthex7.oitc.scoreboard.Board;
import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.ArenaState;
import com.gmail.srthex7.oitc.system.lobby.Lobby;
import com.gmail.srthex7.oitc.system.player.GPlayer;
import com.gmail.srthex7.oitc.utils.centermessage.CenterMessage;

public class ArenaListeners implements Listener {
	
	@EventHandler
	public void onOitcPreStart(OitcPreStartEvent e) {
		e.getArena().setArenaState(ArenaState.STARTING);
		new BukkitRunnable() {
			int timeToStart = Settings.timeToStartArena;
			@Override
			public void run () {
				timeToStart--;
				
				if (e.getArena().getPlayerUuids().size() < e.getArena().getMinusers()) {
					e.getArena().setArenaState(ArenaState.WAITING);
					for (UUID uuid : e.getArena().getPlayerUuids()) {
						Player player = Bukkit.getPlayer(uuid);
						if (player != null) {
							Board.setScoreboardPreGame(player, e.getArena());
						}
					}
					cancel();
				}
				
				if (timeToStart <= 0) {
					e.getArena().start();
					cancel();
				} else {
					
				}
			}
		}.runTaskTimer(OITC.getInstance(), 0l, 20l);
	}
	
	@EventHandler
	public void onOitcStart(OitcStartEvent e) {
		Arena arena = e.getArena();
		arena.setAvailable(false);
		arena.setArenaState(ArenaState.INGAME);
		//Acciones individuales de todos los jugadores participantes
		int index = 0;
		for(UUID uuid : arena.getPlayerUuids()) {
			Player player = Bukkit.getPlayer(uuid);
			if(player != null) {
				player.teleport(arena.getSpawnslocation().get(index));
				Items.setGameInventory(player);
				index++;
				
				//Scoreboard
				Board.removeAllEntry(player);
				
				//Kills
				arena.getKills().put(player.getName(), 0);
				
				//Stats
				GPlayer gplayer = GPlayer.getGPlayer(uuid);
				gplayer.setArena(arena);
				gplayer.setPlayed(gplayer.getPlayed()+1);
			}
		}
		
		arena.randomizeTargets();
		arena.sortedKills();
		
		//Scoreboard
		Bukkit.getServer().getScheduler().runTaskLater(OITC.getInstance(), () -> {
			for (UUID uuid : e.getArena().getPlayerUuids()) {
				Player player = Bukkit.getPlayer(uuid);
				Board.setScoreboardInGame(player, arena);
			}
		}, 2L);
		
		//...
	}
	
	@EventHandler
	public void onOitcFinish(OitcFinishEvent e) {
		e.getArena().setArenaState(ArenaState.ENDING);
		
		List<String> message = new ArrayList<>();
		int v = 1;
		message.add("&e&m-----------------------------------------");
		message.add("&f&lBounty Hunters");
		message.add("");
		for (String key : e.getArena().getSortedKills().keySet()) {
			if (v == 1) {
				message.add("&e&l1st Place &8- &7<1>".replaceAll("<1>", key));
				
				Player player = Bukkit.getPlayer(key);
				if (player != null) {
					GPlayer gplayer = GPlayer.getGPlayer(player.getUniqueId());
					gplayer.setWins(gplayer.getWins()+1);
				}
				
			} else if (v == 2) {
				message.add("&6&l2nd Place &8- &7<2>".replaceAll("<2>", key));
			} else if (v == 3) {
				message.add("&c&l3rd Place &8- &7<3>".replaceAll("<3>", key));
			} else {
				break;
			}
			v++;
		}
		
		message.add("");
		message.add("&e&m-----------------------------------------");
		
		for (UUID uuid : e.getArena().getPlayerUuids()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				CenterMessage.sendCenteredMessage(player, message);
			}
		}
		
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!Settings.autoArena) {
					Lobby.callPlayersInArena(e.getArena());
				} 
				e.getArena().reset();
			}
		}.runTaskLater(OITC.getInstance(), Settings.timeToStopArena*20);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause().equals(DamageCause.FALL)) {
				e.setCancelled(true);
			}
			GPlayer gplayer = GPlayer.getGPlayer(((Player)e.getEntity()).getUniqueId());
			if (gplayer.getArena() == null) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() == null) return;
		
		if (!(e.getEntity() instanceof Player)) return;
		
		Player player = (Player)e.getEntity();
		GPlayer gplayer = GPlayer.getGPlayer(player.getUniqueId());
		
		
		if(gplayer.getArena() == null) {
			e.setCancelled(true);
			return;
		}
		
		if(!gplayer.getArena().getArenaState().equals(ArenaState.INGAME)) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow)e.getDamager();
			GPlayer killer = GPlayer.getGPlayer(((Player)arrow.getShooter()).getUniqueId());
			
			if (player.getUniqueId().equals(killer.getUuid())) {
				e.setCancelled(true);
				return;
			}
			
			arrow.remove();
			e.setCancelled(true);
			Bukkit.getPluginManager().callEvent(new OitcPlayerDeathEvent(gplayer, killer, gplayer.getArena(),e));
			return;
		}
		
		if (e.getDamager() instanceof Player) {
			
			Player pkiller = (Player) e.getDamager();
			if (pkiller.getItemInHand() != null) {
				if (pkiller.getItemInHand().getType().equals(Material.WOOD_SWORD)) {
					pkiller.getInventory().getItemInHand().setDurability((short)0);
				}
			}
			
			if (player.getHealth() - e.getFinalDamage() <= 0.0D) {
				e.setCancelled(true);
				GPlayer killer = GPlayer.getGPlayer(((Player)e.getDamager()).getUniqueId());
				Bukkit.getPluginManager().callEvent(new OitcPlayerDeathEvent(gplayer, killer, gplayer.getArena(),e));
			}
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onOitcPlayerDeathEvent(OitcPlayerDeathEvent e) {
		Player player = Bukkit.getPlayer(e.getPlayer().getUuid());
		Player killer = Bukkit.getPlayer(e.getKiller().getUuid());
		
		//Acciones dentro de la arena
		player.teleport(e.getArena().getSpawnslocation().get(Utils.rndInt(0, e.getArena().getSpawnslocation().size()-1)));
		
		Sounds.playSound(player, Sounds.WITHER_SHOOT.bukkitSound());
		Sounds.playSound(killer, Sounds.NOTE_PLING.bukkitSound());
		
		player.playEffect(null, Effect.BLAZE_SHOOT, 20F);
		
		player.setHealth(player.getMaxHealth());
		killer.setHealth(killer.getMaxHealth());
		
		e.getArena().addKill(killer);
		
		e.getArena().sortedKills();
		
		Items.addArrow(killer);
		Items.addArrow(player);
		
		
		if (e.getArena().existTarget(killer)) {
			if (e.getArena().getTargetName(killer).equals(player.getName())) {
				Bukkit.getPluginManager().callEvent(new OitcPlayerKillTargetEvent(e.getKiller(),e.getPlayer(),e.getArena()));
				e.getArena().addKill(killer);
			}
		}
		
		//Scoreboard
		Board.updateScoreboardInGame(e.getArena());
		//....
		
		
		//stats
		e.getKiller().setKills(e.getKiller().getKills()+1);
		e.getPlayer().setDeaths(e.getPlayer().getDeaths()+1);
		
		//messages
		e.getArena().broadcastMessage(Lang.playerkillplayer.replaceAll("<victim>", player.getName()).replaceAll("<killer>", killer.getName()));
	}
	
	@EventHandler
	public void onOitcPlayerKillTargetEvent(OitcPlayerKillTargetEvent e) {
		
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		e.getEntity().remove();
	}
	
	@EventHandler
	public void onEntityRegainHealth (EntityRegainHealthEvent e) {
		e.setCancelled(true);
	}
}
