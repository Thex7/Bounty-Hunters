package com.gmail.srthex7.oitc.system;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.srthex7.multicore.File.ConfigUtil;
import com.gmail.srthex7.multicore.Others.Utils;
import com.gmail.srthex7.multicore.Regions.Loc;
import com.gmail.srthex7.oitc.OITC;
import com.gmail.srthex7.oitc.api.LeaveReason;
import com.gmail.srthex7.oitc.api.OitcChangeTargetEvent;
import com.gmail.srthex7.oitc.api.OitcFinishEvent;
import com.gmail.srthex7.oitc.api.OitcPlayerJoinArenaEvent;
import com.gmail.srthex7.oitc.api.OitcPlayerQuitArenaEvent;
import com.gmail.srthex7.oitc.api.OitcStartEvent;
import com.gmail.srthex7.oitc.game.Settings;
import com.gmail.srthex7.oitc.system.player.GPlayer;

import net.md_5.bungee.api.ChatColor;

/**
 * Esta clase administra una arena 
 * @author Thex7
 */
public class Arena {

	private String arenaname;
	private int maxusers;
	private int minusers;
	private String mapname;
	private Location initialspawn;
	private List<Location> spawnslocation = new ArrayList<>();
	
	private List<UUID> uuids = new CopyOnWriteArrayList<>();
	
	private boolean available = true;
	private ArenaState state = ArenaState.WAITING;
	
	/**
	 * Obtiene el nombre de la arena
	 * @return nombre de la arena
	 */
	public String getArenaname() {
		return arenaname;
	}
	/**
	 * Establece el nombre de la arena
	 * @param arenaname nombre de la arena
	 */
	public void setArenaname(String arenaname) {
		this.arenaname = arenaname;
	}
	/**
	 * Obtiene el nombre del mapa de la arena
	 * @return nombre del mapa
	 */
	public String getMapname() {
		return mapname;
	}
	/**
	 * Establece el nombre del mapa de la arena
	 * @param mapname nombre del mapa
	 */
	public void setMapname(String mapname) {
		this.mapname = mapname;
	}
	/**
	 * Obtiene el maximo de usuarios de esta arena
	 * @return numero maximo de usuarios
	 */
	public int getMaxusers() {
		return maxusers;
	}
	/**
	 * Establece el maximo de usuarios en la arena
	 * @param maxusers numero maximo de usuarios
	 */
	public void setMaxusers(int maxusers) {
		this.maxusers = maxusers;
	}
	/**
	 * Obtiene el minimo de usuaris en esta arena
	 * @return numero del minimo de usuarios
	 */
	public int getMinusers() {
		return minusers;
	}
	/**
	 * Establece el minimo de usuarios para jugar la arena
	 * @param minusers numero minimo de usuarios
	 */
	public void setMinusers(int minusers) {
		this.minusers = minusers;
	}
	/**
	 * Obtiene el punto de aparición inicial
	 * @return punto de aparición inicial
	 */
	public Location getInitialspawn() {
		return initialspawn;
	}
	/**
	 * Establece punto de aparición inicial
	 * @param punto de aparición inicial
	 */
	public void setInitialspawn(Location initialspawn) {
		this.initialspawn = initialspawn;
	}
	/**
	 * Obtiene una lista con todos los puntos de aparición
	 * @return lista con puntos de aparición
	 */
	public List<Location> getSpawnslocation() {
		return spawnslocation;
	}
	/**
	 * Agrega un punto de aparición a la lista de puntos de aparición
	 */
	public void addSpawnlocation(Location spawnpoint) {
		spawnslocation.add(spawnpoint);
	}
	/**
	 * Obtiene una lista con todas las uuids de los jugadores en la arena
	 * @return lista de uuids
	 */
	public List<UUID> getPlayerUuids() {
		return uuids;
	}
	/**
	 * Establece una lista de uuids
	 * @param uuids lista de uuids
	 */
	public void setPlayerUuids(List<UUID> uuids) {
		this.uuids = uuids;
	}
	/**
	 * Agrega una uuid a la lista de uuids
	 * @param uuid uuid que desdea agregar
	 */
	public void addPlayerUUID(UUID uuid) {
		if (this.uuids.contains(uuid)) return;
		uuids.add(uuid);
		Bukkit.getPluginManager().callEvent(new OitcPlayerJoinArenaEvent(GPlayer.getGPlayer(uuid), this));
	}
	/**
	 * Remueve la uuid de la lista de uuids
	 * @param uuid uuid que quiere remover
	 */
	public void removePlayerUUID(UUID uuid, LeaveReason reason) {
		uuids.remove(uuid);
		Bukkit.getPluginManager().callEvent(new OitcPlayerQuitArenaEvent(GPlayer.getGPlayer(uuid), this, reason));
	}
	/**
	 * Agrega la arena a la lista de arenas
	 */
	public void addArena() {
		arenas.add(this);
	}
	/**
	 * Remueve la arena de la lista de arenas
	 */
	public void removeArena() {
		arenas.remove(this);
	}
	
	/**
	 * Pregunta si una arena esta disponible
	 * @return devuelve si una arena esta disponible
	 */
	public boolean isAvailable() {
		return this.available;
	}
	
	/**
	 * Establece si una arena esta disponible
	 * @param available devuelve true si esta disponible, false si no lo esta
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	/**
	 * Devuelve el estado de la arena
	 * @return estado la arena
	 */
	public ArenaState getArenaState() {
		return this.state;
	}
	
	/**
	 * Establece el estado de la arena
	 * @param arenastate estado que desea establecer
	 */
	public void setArenaState(ArenaState arenastate) {
		this.state = arenastate;
	}
	
	/**
	 * Guarda la arena en un archivo yaml
	 */
	public void save() {
		ConfigUtil cu = new ConfigUtil(OITC.getInstance(),"Arenas", this.arenaname.toLowerCase());
		cu.getConfig().set("NAME", this.arenaname);
		cu.getConfig().set("MAPNAME", this.mapname);
		cu.getConfig().set("MAXUSERS", this.maxusers);
		cu.getConfig().set("MINUSERS", this.minusers);
		cu.getConfig().set("INITIALSPAWN", Loc.serializeLoc(this.initialspawn));
		List<String> spawns = new ArrayList<>();
		for(Location loc : this.spawnslocation) {
			spawns.add(Loc.serializeLoc(loc));
		}
		cu.getConfig().set("SPAWNPOINTS", spawns);
		cu.save();
	}
	
	/**
	 * Lista con todas las arenas cargadas
	 */
	private static List<Arena> arenas = new ArrayList<>();
	
	/**
	 * Devuelve lista con todas las arenas
	 * @return lista de arenas
	 */
	public static List<Arena> getArenas() {
		return arenas;
	}
	
	/**
	 * Carga todas las arenas
	 */
	public static void loadAll() {
		File folder = new File(OITC.getInstance().getDataFolder(),"Arenas");
		if(folder.exists()) {
			for(String files : folder.list()) {
				File file = new File(OITC.getInstance().getDataFolder(),"/Arenas/"+files);
				if(file.exists()) {
					FileConfiguration config = YamlConfiguration.loadConfiguration(file);
					Arena arena = new Arena();
					arena.setArenaname(config.getString("NAME"));
					arena.setMapname(config.getString("MAPNAME"));
					arena.setMaxusers(config.getInt("MAXUSERS"));
					arena.setMinusers(config.getInt("MINUSERS"));
					arena.setInitialspawn(Loc.deserializeLoc(config.getString("INITIALSPAWN")));
					for(String s : config.getStringList("SPAWNPOINTS")) {
						arena.addSpawnlocation(Loc.deserializeLoc(s));
					}
					arena.addArena();
					
					World w = arena.getInitialspawn().getWorld();
					w.setDifficulty(Difficulty.PEACEFUL);
					w.setThundering(false);
					w.setAnimalSpawnLimit(0);
				}
			}
		}
	}
	
	//-----------------------
	// Mecanismos de juego
	//-----------------------
	
	private HashMap<String,String> targets = new HashMap<>();
	private HashMap<String,Integer> kills = new HashMap<>();
	
	private Map<String, Integer> sortedkills = new TreeMap<>();
	
	public int timeToEnd = Settings.timeToEnd;
	public int timeToRandomizeTargets = Settings.timeToRandomizeTargets;
	
	/**
	 * Organiza los objetivos de los jugadores
	 */
	public void randomizeTargets() {
		
		if (!this.state.equals(ArenaState.INGAME)) return;
		
		targets.clear();
		
		HashMap<String, String> localtargets = new HashMap<>();
		
		ArrayList<String> players = new ArrayList<>();
		
		for (UUID uuid : this.uuids) {
			players.add(Bukkit.getPlayer(uuid).getName());
			localtargets.put(Bukkit.getPlayer(uuid).getName(), "");
		}
		
		for (String p : (ArrayList<String>)players.clone()) {
			if (players.size() > 1) {
				String target = players.get(Utils.rndInt(0, players.size()-1));
				
				while (target.equals(p)) {
					target = players.get(Utils.rndInt(0, players.size()-1));
				}
				
				int tried = 3;
				
				while (localtargets.get(target).equals(p) && tried > 0) {
					String temptarget = players.get(Utils.rndInt(0, players.size()-1));
					if (!temptarget.equals(p)) {
						target = temptarget;
					}
					tried--;
				}
				
				localtargets.put(p, target);
				players.remove(target);
			} else {
				String target = players.get(0);
				
				while (target.equals(p)) {
					target = Bukkit.getPlayer(this.uuids.get(Utils.rndInt(0, this.uuids.size()-1))).getName();
				}
				
				int tried = 3;
				
				while (localtargets.get(target).equals(p) && tried > 0) {
					String temptarget = Bukkit.getPlayer(this.uuids.get(Utils.rndInt(0, this.uuids.size()-1))).getName();
					if (!temptarget.equals(p)) {
						target = temptarget;
					}
					tried--;
				}
				
				localtargets.put(p, target);
			}
		}
		
		
		for (String key : localtargets.keySet()) {
			this.changeTarget(Bukkit.getPlayer(key), Bukkit.getPlayer(localtargets.get(key)));
		}
		
		
		
		/*
		while (!copyuuid.isEmpty()) {
			Player p1 = null;
			Player p2 = null;
			
			if (copyuuid.size() == 2) {
				p1 = Bukkit.getPlayer(copyuuid.remove(0));
				p2 = Bukkit.getPlayer(copyuuid.remove(0));
			}
			
			this.changeTarget(p1, p2);
			this.changeTarget(p2, p1);
			
			if (copyuuid.size() == 1) {
				Player p3 = Bukkit.getPlayer(copyuuid.remove(0));
				this.changeTarget(p3, p1);
			}
		}*/
		timeToRandomizeTargets = Settings.timeToRandomizeTargets;
	}

	/**
	 * Retorna un hashmap con los objetivos <jugador, objetivo>
	 * @return hashmap con objetivos
	 */
	public HashMap<String,String> getTargets() {
		return this.targets;
	}
	
	/**
	 * Cambia los objetivos de los jugadores
	 * @param player jugador
	 * @param target objetivo del jugador
	 */
	public void changeTarget(Player player, Player target) {
		targets.put(player.getName(), target.getName());
		Bukkit.getPluginManager().callEvent(new OitcChangeTargetEvent(GPlayer.getGPlayer(player.getUniqueId()), GPlayer.getGPlayer(target.getUniqueId()), this));
	}
	
	/**
	 * Obtiene el objetivo de un jugador
	 * @param player jugador del que se quiere obtener el objetivo
	 * @return Objetivo del jugador
	 */
	public String getTargetName(Player player) {
		return targets.get(player.getName());
	}
	
	/**
	 * Comprueba si el jugador tiene un objetivo
	 * @param player jugador del que se quiere comprobar su objetivo
	 * @return verdadero si el jugador tiene un objetivo - falso si no lo tiene
	 */
	public boolean existTarget(Player player) {
		return targets.containsKey(player.getName());
	}
	
	/**
	 * Agrega un asesinato al jugador
	 * @param player jugador al que se le quiere agregar un asesinato
	 */
	public void addKill(Player player) {
		kills.put(player.getName(), this.getKills(player)+1);
	}
	
	/**
	 * Obtiene los asesinatos de un jugaodr
	 * @param player jugador del que se quiere obtener los asesinatos
	 * @return numero de asesinatos del jugador
	 */
	public int getKills(Player player) {
		return this.kills.get(player.getName());
	}
	
	/**
	 * Envia un mensaje a todos los jugadores de la arena
	 * @param message mensaje que desea enviar
	 */
	public void broadcastMessage(String message) {
		for (UUID uuid : this.uuids) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}
		}
	}
	

	/**
	 * Obtiene la lista de asesinatos sin ordenar
	 * @return lista de asesinatos
	 */
	public HashMap<String,Integer> getKills() {
		return this.kills;
	}
	
	/**
	 * Ordena los asesinatos en orden decreciente
	 */	
	public void sortedKills() {
		sortedkills.clear();
		sortedkills = kills.entrySet().stream().sorted((Entry<String, Integer> o1, Entry<String, Integer> o2) -> o2.getValue().compareTo(o1.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	/**
	 * Obtiene una lista con los asesinatos en orden
	 * @return
	 */
	public Map<String, Integer> getSortedKills() {
		return this.sortedkills;
	}

	public void start() {
		Bukkit.getPluginManager().callEvent(new OitcStartEvent(this));
	}
	
	public void finish() {
		Bukkit.getPluginManager().callEvent(new OitcFinishEvent(this));
	}
	
	public void reset() {
		this.sortedkills.clear();
		this.targets.clear();
		this.kills.clear();
		this.uuids.clear();
		this.state = ArenaState.WAITING;
		this.available = true;
		this.timeToEnd = Settings.timeToEnd;
		this.timeToRandomizeTargets = Settings.timeToRandomizeTargets;
	}
	
}