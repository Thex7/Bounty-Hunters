package com.gmail.srthex7.oitc.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.srthex7.multicore.Others.Utils;
import com.gmail.srthex7.multicore.Scoreboard.Entry;
import com.gmail.srthex7.multicore.Scoreboard.PlayerScoreboard;
import com.gmail.srthex7.oitc.game.Settings;
import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.player.GPlayer;

public class Board {

	public static List<String> pregame = new ArrayList<>();
	public static List<String> ingame = new ArrayList<>();
	
	public static List<String> lobby = new ArrayList<>();
	
	public Board () {
		
	}
	
	/**
	 * Establece el marcador de juego al jugador
	 * Estado: En juego
	 * @param player jugador al que se le establecera el marcador
	 * @param arena arena en la que se encuentra el jugador
	 */
	public static void setScoreboardInGame(Player player, Arena arena) {
		PlayerScoreboard board = PlayerScoreboard.getScoreboard(player);
		int i = 0;
		for (String score : ingame) {
			String text = score.replaceAll("<target>", arena.existTarget(player) ? arena.getTargetName(player) : "?")
					;//.replaceAll("<date>", Settings.date);

			int v = 1;
			for (String key : arena.getSortedKills().keySet()) {
				text = text.replaceAll("<" + v + ">", arena.getSortedKills().size() >= v ? Settings.positionFormat.replaceAll("<playername>", key).replaceAll("<score>", arena.getSortedKills().get(key)+"") : "")
						.replaceAll("<timeToEnd>", "" + Utils.formatSecondsToMinutes(arena.timeToEnd));
				v++;
			}
			if (!(text.contains("<") && text.contains(">"))) {
				if (board.getEntry("ingame" + i) == null) {
						new Entry("ingame" + i, board).setText(text).send();
				} else {
					board.getEntry("ingame" + i).setText(text);
				}
			}
			i++;
		}
	}
	
	/**
	 * Establece el marcador de juego al jugador
	 * Estado: Iniciando
	 * @param player jugador al que se le establecera el marcador
	 * @param arena arena en la que se encuentra el jugador
	 */
	public static void setScoreboardPreGame(Player player, Arena arena) {
		PlayerScoreboard board = PlayerScoreboard.getScoreboard(player);
		int i = 0;
		for (String score : pregame) {
			String text = score
					.replaceAll("<map>", arena.getMapname())
					.replaceAll("<state>", arena.getArenaState().toString())
					.replaceAll("<onlineplayers>", ""+arena.getPlayerUuids().size())
					.replaceAll("<maxplayers>", ""+arena.getMaxusers())
					.replaceAll("<server>", arena.getArenaname())
					.replaceAll("<domain>", Settings.domain)
					.replaceAll("<date>", Settings.date);
			
			if (board.getEntry("pregame" + i) == null) {
				new Entry("pregame" + i, board).setText(text).send();
			} else {
				board.getEntry("pregame" + i).setText(text);
			}
			i++;
		}
	}
	
	/**
	 * Establece el marcador de espera al jugador
	 * @param player jugador al que se establecera el marcador
	 */
	public static void setScoreboardLobby(Player player) {
		GPlayer gplayer = GPlayer.getGPlayer(player.getUniqueId());
		PlayerScoreboard board = PlayerScoreboard.getScoreboard(player);
		int i = 0;
		for (String score : lobby) {
			String text = score
					.replaceAll("<player>", player.getName())
					.replaceAll("<kills>", ""+gplayer.getKills())
					.replaceAll("<deaths>", ""+gplayer.getDeaths())
					.replaceAll("<wins>", ""+gplayer.getWins())
					.replaceAll("<losses>", ""+gplayer.getLosses())
					.replaceAll("<played>", ""+gplayer.getPlayed())
					.replaceAll("<domain>", Settings.domain)
					.replaceAll("<date>", Settings.date);
			
			if (board.getEntry("lobby" + i) == null) {
				new Entry("lobby" + i, board).setText(text).send();
			} else {
				board.getEntry("lobby" + i).setText(text);
			}
			i++;
		}
	}
	
	/**
	 * Remueve una linea del marcador 
	 * @param player jugador al que se le removera una linea de su marcador
	 * @param entryid ID de la linea que se removera
	 */
	public static void removeEntry(Player player, String entryid) {
		PlayerScoreboard board = PlayerScoreboard.getScoreboard(player);
		if (board.getEntry(entryid) == null) return;
		board.getEntry(entryid).cancel();
	}
	
	/**
	 * Remueve todas las lineas del marcador
	 * @param player jugador al que se le removera todas las lineas del marcador
	 */
	public static void removeAllEntry(Player player) {
		PlayerScoreboard board = PlayerScoreboard.getScoreboard(player);
		for (Entry entry : board.getEntries()) {
			entry.cancel();
		}
	}
	
	/**
	 * Actualiza la scoreboard de todos los jugadores en la arena
	 * Estado: En juego
	 * @param arena arena en la que se quiere actualizar la scoreboard
	 */
	public static void updateScoreboardInGame(Arena arena) {
		for (UUID uuid : arena.getPlayerUuids()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				setScoreboardInGame(player, arena);
			}
		}
	}
}
