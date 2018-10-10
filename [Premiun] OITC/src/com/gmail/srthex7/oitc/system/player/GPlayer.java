package com.gmail.srthex7.oitc.system.player;

import java.util.HashMap;
import java.util.UUID;

import com.gmail.srthex7.oitc.system.Arena;

/**
 * Clase que administra los datos de un jugador
 * @author Thex7
 */
public class GPlayer {

	
	//Información del jugador
	private UUID uuid;
	private Arena arena;
	
	//Estadisticas del jugador
	private int kills = 0;
	private int deaths = 0;
	private int wins = 0;
//	private int losses = 0;
	private int played = 0;
	
	//Getters and Setters
	public GPlayer(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * Devuelve la uuid del jugador
	 * @return obtiene la uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * Establece la uuid del jugador
	 * @param uuid uuid del jugador
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Devuelve la arena donde se encuentra el jugador
	 * si el jugador no esta en la arena devuelve null
	 * @return arena del jugador
	 */
	public Arena getArena() {
		return arena;
	}

	/**
	 * Establece la arena en el cual se encuentra el jugador
	 * @param arena arena en la que se encuentra el jugador
	 */
	public void setArena(Arena arena) {
		this.arena = arena;
	}

	/**
	 * Devuelve los asesinatos del jugador
	 * @return asesinados del jugador
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * Establece los asesinatos del jugador
	 * @param kills numero de asesinatos
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}

	/**
	 * Devuelve las muertes del jugador
	 * @return muertes del jugador
	 */
	public int getDeaths() {
		return deaths;
	}

	/**
	 * Establece las muertes del jugador
	 * @param deaths numero de muertes
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	/**
	 * Devuelve las victorias del jugador
	 * @return victorias del jugador
	 */
	public int getWins() {
		return wins;
	}

	/**
	 * Establece las victorias del jugador
	 * @param wins numero de victorias
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}

	/**
	 * Devuelve las derrotas del jugador
	 * @return numero de derrotas
	 */
	public int getLosses() {
		return this.played - this.wins;
	}

	/**
	 * Establece las derrotas
	 * @param losses numero de derrotas del jugador
	 */
	/*public void setLosses(int losses) {
		this.losses = losses;
	}

	/**
	 * Devuelve las partidas jugadas del jugador
	 * @return numero de partidas jugadas
	 */
	public int getPlayed() {
		return played;
	}

	/**
	 * Establece las partidas jugadas del jugador
	 * @param played numero de partidas jugadas
	 */
	public void setPlayed(int played) {
		this.played = played;
	}
	
	/**
	 * Añade gplayer a la lista de gplayers
	 */
	public void add() {
		gplayers.put(this.uuid, this);
	}
	
	/**
	 * Remueve gplayer de la lista de gplayers
	 */
	public void delete() {
		gplayers.remove(this);
	}
	
	//Metodos para obtener esta clase
	private static HashMap<UUID, GPlayer> gplayers = new HashMap<>();
	
	public static GPlayer getGPlayer(UUID uuid) {
		return gplayers.get(uuid);
	}
	
	public static HashMap<UUID, GPlayer> getGPlayers() {
		return gplayers;
	}
}
