package com.gmail.srthex7.oitc.game;

import java.text.SimpleDateFormat;

import com.gmail.srthex7.multicore.File.ConfigUtil;
import com.gmail.srthex7.multicore.Regions.Loc;
import com.gmail.srthex7.oitc.OITC;
import com.gmail.srthex7.oitc.scoreboard.Board;
import com.gmail.srthex7.oitc.system.lobby.Lobby;
import com.gmail.srthex7.oitc.system.lobby.Utilities;

public class Settings {

	public static boolean autoArena = false;
	public static String positionFormat = "<playername>: &a<score>";
	
	public static final int timeToStartArena = 10;
	public static final int timeToStopArena = 10;
	
	public static final int timeToEnd = 300;
	public static final int timeToRandomizeTargets = 50;
	
	public static String domain = "www.example.com";
	public static String date = "";
	
	public static String savestats = "flat";
	
	
	
	public Settings() {
		ConfigUtil cu = new ConfigUtil(OITC.getInstance(), "settings");
		this.createYaml(cu);
		
		date = new SimpleDateFormat("M/dd/yyyy").toString();
		
		if (cu.exist()) {
			if (cu.getConfig().get("lobby") != null) {
				Lobby.spawnpoint = Loc.deserializeLoc(cu.getConfig().getString("lobby"));
			}
		}
	}
	
	private void createYaml(ConfigUtil cu) {
		
		Board.pregame.add("");
		Board.pregame.add("Map: &a<map>" );
		Board.pregame.add("Players: &a<onlineplayers>/<maxplayers>");
		Board.pregame.add("");
		Board.pregame.add("<state>");
		Board.pregame.add("");
		Board.pregame.add("Server: &a<server>");
		Board.pregame.add("");
	//	Board.pregame.add("&6<domain>");
		
		Board.ingame.add("&7<date>");
		Board.ingame.add("");
		Board.ingame.add("Time Left: &a<timeToEnd>");
		Board.ingame.add("");
		Board.ingame.add("&c&lTarget");
		Board.ingame.add("<target>");
		Board.ingame.add("");
		Board.ingame.add("<1>");
		Board.ingame.add("<2>");
		Board.ingame.add("<3>");
		Board.ingame.add("<4>");
		Board.ingame.add("<5>");
		Board.ingame.add("<6>");
		Board.ingame.add("<7>");
		Board.ingame.add("<8>");
		
		Board.lobby.add("");
		Board.lobby.add("Name: &a<player>");
		Board.lobby.add("");
		Board.lobby.add("Kills: &a<kills>" );
		Board.lobby.add("Deaths: &a<deaths>");
		Board.lobby.add("Wins: &a<wins>");
		Board.lobby.add("Losses: &a<losses>");
		Board.lobby.add("Played: &a<played>");
		Board.lobby.add("");
	//	Board.lobby.add("<domain>");
		
		Utilities.arenaDescription.add("&7Map: &a<map>");
		Utilities.arenaDescription.add("&7State: &a<state>");
		Utilities.arenaDescription.add("&7Players: &a<onlineplayers>/<maxplayers>");
		
	/*	cu.getConfig().set("scoreboard.pregame", Board.pregame);
		cu.getConfig().set("scoreboard.ingame", Board.ingame);
		
		cu.getConfig().set("autoArena", autoArena);
		cu.getConfig().set("positionFormat", positionFormat);
		
		cu.getConfig().set("timeToStartArena", timeToStartArena);
		cu.getConfig().set("timeToStopArena", timeToStopArena);
		
		for (ArenaState state : ArenaState.values()) {
			cu.getConfig().set(state.name().toLowerCase(), state.toString());
		}
		*/
		
		
		Utilities.applyColors();
	}
	
	public static String getVersion() {
		String s = "";
		
		return s;
	}
}