package com.gmail.srthex7.oitc.system;

import java.util.ConcurrentModificationException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.srthex7.multicore.particles.ParticleEffect;

public class Particles extends BukkitRunnable{

	@Override
	public void run() {
		for (Arena arena : Arena.getArenas()) {
			try {
				for (String playerName : arena.getTargets().keySet()) {
					
					Player player = Bukkit.getPlayer(playerName);
					Player target = Bukkit.getPlayer(arena.getTargets().get(playerName));
					
					if (player != null && target != null) {
						
						//jugador ve a su objetivo con particulas 
						ParticleEffect.REDSTONE.display(0.25f,0.4f,0.25f, 0f, 20 , target.getLocation().clone().add(0, 1, 0), player);
						
						//objetivo ve al jugador que lo tiene como objetivo con particulas
						ParticleEffect.REDSTONE.display(0.25f,0.4f,0.25f, 1f, 20 , player.getLocation().clone().add(0, 1, 0), target);
						
					}
				}
			} catch (ConcurrentModificationException e) {}
		}
		
	}

}
