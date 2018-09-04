package com.gmail.srthex7.oitc.system.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gmail.srthex7.oitc.system.Arena;

/**
 * Esta clase administra a los creadores de arenas
 * @author Thex7
 */
public class EPlayer {

	private UUID uuid;
	private Arena arena;
	private EPlayerState state = EPlayerState.NAME;
	
	public EPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Obtiene la uuid del configurador
	 * @return uuid del configurador
	 */
	public UUID getUuid() {
		return uuid;
	}
	/**
	 * Se establece la uuid del configurador
	 * @param uuid uuid del configurador
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	/**
	 * Obtiene la arena que se esta configurando
	 * @return 
	 */
	public Arena getArena() {
		return arena;
	}
	/**
	 * Se establece la arena
	 * @param arena arena que quiere establecer
	 */
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	/**
	 * Obtiene el estado de la configuración de la arena
	 * @return estadp de configuración de la arena
	 */
	public EPlayerState getState() {
		return state;
	}
	/**
	 * Establece el estado de configuración de la arena
	 * @param state
	 */
	public void setState(EPlayerState state) {
		this.state = state;
	}
	/**
	 * Agrega a este configurador a la lista de configuradores
	 */
	public void add() {
		eplayers.add(this);
	}
	/**
	 * Elimina a este configurador de la lista de configuradores
	 */
	public void remove() {
		eplayers.remove(this);
	}
	
	/**
	 * Lista de configuradores
	 */
	private static List<EPlayer> eplayers = new ArrayList<>();
	/**
	 * Obtiene una lista con todos los EPlayers
	 * @return lista con todos los EPlayers
	 */
	public static List<EPlayer> getEPlayers() {
		return eplayers;
	}
	/**
	 * Obtienes un configurador en base a su uuid
	 * @param uuid uuide del configurador
	 * @return clase EPlayer perteneciente al configurador
	 */
	public static EPlayer getEPlayersFromUUID(UUID uuid) {
		for(EPlayer eplayer : eplayers) {
			if(!eplayer.getUuid().equals(uuid)) continue;
			return eplayer;
		}
		return null;
	}
}
