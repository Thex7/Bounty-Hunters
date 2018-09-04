package com.gmail.srthex7.oitc.stats.flat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.srthex7.multicore.File.ConfigUtil;
import com.gmail.srthex7.oitc.OITC;
import com.gmail.srthex7.oitc.system.player.GPlayer;

public class Flat {
	
	public Flat () {
		
	}

	public static void syncStats(GPlayer gplayer) {
		ConfigUtil cu = new ConfigUtil(OITC.getInstance(),"stats", gplayer.getUuid().toString());
		cu.getConfig().set("KILLS", gplayer.getKills());
		cu.getConfig().set("DEATHS", gplayer.getDeaths());
		cu.getConfig().set("WINS", gplayer.getWins());
	//	cu.getConfig().set("LOSSES", gplayer.getLosses());
		cu.getConfig().set("PLAYED", gplayer.getPlayed());
		cu.save();
	}
	
	public static void register(GPlayer gplayer, Player player) {
		ConfigUtil cu = new ConfigUtil(OITC.getInstance(),"stats", gplayer.getUuid().toString());
		cu.getConfig().set("NAME", player.getName());
		cu.getConfig().set("KILLS", gplayer.getKills());
		cu.getConfig().set("DEATHS", gplayer.getDeaths());
		cu.getConfig().set("WINS", gplayer.getWins());
	//	cu.getConfig().set("LOSSES", gplayer.getLosses());
		cu.getConfig().set("PLAYED", gplayer.getPlayed());
		cu.save();
	}
	
	public static void load(GPlayer gplayer) {
		ConfigUtil cu = new ConfigUtil(OITC.getInstance(),"stats", gplayer.getUuid().toString());
		gplayer.setKills(cu.getConfig().getInt("KILLS"));
		gplayer.setDeaths(cu.getConfig().getInt("DEATHS"));
		gplayer.setWins(cu.getConfig().getInt("WINS"));
//		gplayer.setLosses(cu.getConfig().getInt("LOSSES"));
		gplayer.setPlayed(cu.getConfig().getInt("PLAYED"));
		
		Player player = Bukkit.getPlayer(gplayer.getUuid());
		if (cu.getConfig().getString("NAME").equals(player.getName())) {
			cu.getConfig().set("NAME", player.getName());
			cu.save();
		}
	}
	
	public static boolean isregister(GPlayer gplayer) {
		ConfigUtil cu = new ConfigUtil(OITC.getInstance(),"stats", gplayer.getUuid().toString());
		return cu.exist();
	}
}
