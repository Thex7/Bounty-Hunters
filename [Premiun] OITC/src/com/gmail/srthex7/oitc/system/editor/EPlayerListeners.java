package com.gmail.srthex7.oitc.system.editor;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.srthex7.multicore.Others.Utils;
import com.gmail.srthex7.oitc.utils.Msg;

public class EPlayerListeners implements Listener{

	/**
	 * Commands:
	 * name
	 * minplayers
	 * maxplayers
	 * initialspawn
	 * gamespawms
	 * 
	 * back -> retrocede un paso
	 * ispawn -> initialspawn
	 * spawn -> spawnpoints
	 */
	@EventHandler
	public void onCommands(AsyncPlayerChatEvent e) {
		if (!EPlayer.getEPlayers().isEmpty()) {
			EPlayer eplayer = EPlayer.getEPlayersFromUUID(e.getPlayer().getUniqueId());
			if (eplayer == null) return;
			e.setCancelled(true);
			if (e.getMessage().equalsIgnoreCase("cancel")) {
				eplayer.remove();
			//	e.getPlayer().sendMessage("Se cancelo la creación de la arena");
				e.getPlayer().sendMessage(Msg.info("The creation of the arena was canceled"));
				return;
			}
			if (eplayer.getState().equals(EPlayerState.NAME)) {
				eplayer.getArena().setArenaname(e.getMessage());
			//	e.getPlayer().sendMessage(Msg.info("Ahora escriba el nombre del mapa"));
				e.getPlayer().sendMessage(Msg.info("Now type the name of the map"));
				eplayer.setState(EPlayerState.MAPNAME);
				return;
			}
			
			if (eplayer.getState().equals(EPlayerState.MAPNAME)) {
				eplayer.getArena().setMapname(e.getMessage());
			//	e.getPlayer().sendMessage(Msg.info("Ahora escriba el numero mínimo de jugadores"));
				e.getPlayer().sendMessage(Msg.info("Now write the minimum number of players"));
				eplayer.setState(EPlayerState.MINPLAYERS);
				return;
			}
			
			if (eplayer.getState().equals(EPlayerState.MINPLAYERS)) {
				if (!Utils.isInt(e.getMessage())) {
			//		e.getPlayer().sendMessage(e.getMessage() + " no es un numero valido!");
					e.getPlayer().sendMessage(ChatColor.RED + e.getMessage() + " It is not a valid number!");
					return;
				}
				eplayer.getArena().setMinusers(Integer.valueOf(e.getMessage()));
			//	e.getPlayer().sendMessage(Msg.info("Ahora escriba el numero maximo de jugadores"));
				e.getPlayer().sendMessage(Msg.info("Now write the maximum number of players"));
				eplayer.setState(EPlayerState.MAXPLAYERS);
				return;
			}
			
			if (eplayer.getState().equals(EPlayerState.MAXPLAYERS)) {
				if (!Utils.isInt(e.getMessage())) {
			//		e.getPlayer().sendMessage(e.getMessage() + " no es un numero valido!");
					e.getPlayer().sendMessage(ChatColor.RED + e.getMessage() + " It is not a valid number!");
					return;
				}
				eplayer.getArena().setMaxusers(Integer.valueOf(e.getMessage()));
			//	e.getPlayer().sendMessage("Escriba 'ispawn' para establecer el punto de aparición principal");
				e.getPlayer().sendMessage(Msg.info("Enter 'ispawn' to set the main point of appearance"));
				eplayer.setState(EPlayerState.INITIALSPAWN);
				return;
			}
			
			if (eplayer.getState().equals(EPlayerState.INITIALSPAWN)) {
				if (e.getMessage().equalsIgnoreCase("ispawn")) {
					eplayer.getArena().setInitialspawn(e.getPlayer().getLocation());
					/*e.getPlayer().sendMessage(Msg.info("Ahora establesca los puntos de reaparición en el juego"));
					e.getPlayer().sendMessage(Msg.info("Escriba 'spawn' cuando este sobre ellos"));
					e.getPlayer().sendMessage(Msg.info("Escriba 'undo' si desea borrar un punto de reaparición anterior"));
					e.getPlayer().sendMessage(Msg.info("Escriba 'end' cuando termine"));*/
					
					e.getPlayer().sendMessage(Msg.info("Now set the reappearance points in the game"));
					e.getPlayer().sendMessage(Msg.info("Type 'spawn' when this is on them"));
					e.getPlayer().sendMessage(Msg.info("Enter 'undo' if you wish to delete a previous reappearance point"));
					e.getPlayer().sendMessage(Msg.info("Type 'end' when finished"));
					eplayer.setState(EPlayerState.GAMESPAWNS);
				}
				return;
			}
			
			if (eplayer.getState().equals(EPlayerState.GAMESPAWNS)) {
				if (e.getMessage().equalsIgnoreCase("spawn")) {
					eplayer.getArena().addSpawnlocation(e.getPlayer().getLocation());
				//	e.getPlayer().sendMessage("Punto de aparición agregado");
					e.getPlayer().sendMessage(Msg.info("Aggregate point of appearance"));
					return;
				}
				if (e.getMessage().equalsIgnoreCase("undo")) {
					if (eplayer.getArena().getSpawnslocation().isEmpty()) {
				//		e.getPlayer().sendMessage("No existen puntos de aparición para remover");
						e.getPlayer().sendMessage(ChatColor.RED + "There are no points of appearance to remove");
						return;
					}
					eplayer.getArena().getSpawnslocation().remove(eplayer.getArena().getSpawnslocation().size()-1);
				//	e.getPlayer().sendMessage("Punto de aparición removido");
					e.getPlayer().sendMessage(Msg.info("Removed appearance point"));
				}
			}
			
			if (e.getMessage().equalsIgnoreCase("end")) {
				if (eplayer.getArena().getSpawnslocation().size() < eplayer.getArena().getMaxusers()) {
				//	e.getPlayer().sendMessage("El numero de puntos de aparición tiene que ser mayor al numero maximo de jugadores");	
					e.getPlayer().sendMessage(ChatColor.RED + "The number of points of appearance must be greater than the maximum number of players");
					return;
				}
				eplayer.getArena().save();
				eplayer.remove();
				//e.getPlayer().sendMessage("Arena creada correctamente");
				e.getPlayer().sendMessage(ChatColor.GREEN + "Sand created correctly");
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		EPlayer eplayer = EPlayer.getEPlayersFromUUID(e.getPlayer().getUniqueId());
		if (eplayer != null) 
			eplayer.remove();
	}
}
