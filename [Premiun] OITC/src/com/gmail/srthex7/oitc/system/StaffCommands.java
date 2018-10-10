package com.gmail.srthex7.oitc.system;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.srthex7.multicore.command.Command;
import com.gmail.srthex7.multicore.command.CommandArgs;
import com.gmail.srthex7.oitc.OITC;
import com.gmail.srthex7.oitc.api.LeaveReason;
import com.gmail.srthex7.oitc.system.editor.EPlayer;
import com.gmail.srthex7.oitc.system.lobby.Lobby;
import com.gmail.srthex7.oitc.system.player.GPlayer;
import com.gmail.srthex7.oitc.utils.Msg;
import com.gmail.srthex7.oitc.utils.Perms;

public class StaffCommands {
	
	private HashMap<String,String> commandinfo = new HashMap<String,String>();
	private String bar = ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------------------------------";
	
	public StaffCommands(OITC plugin) {
		plugin.getFramework().registerCommands(this);
		plugin.getFramework().registerHelp();
		
		commandinfo.put("oitc", "Show all the staff commands");
		commandinfo.put("oitc create", "Create an arena");
		commandinfo.put("oitc delete <name>", "Remove an arena");
		commandinfo.put("oitc lobby", "Set lobby");
		commandinfo.put("leave", "Leave arena");
		commandinfo.put("oitc arenas", "View all arenas");
	}
	
	
	@Command(name = "oitc", permission = Perms.CREATOR)
	public void oitc(CommandArgs args) {
		
		args.getSender().sendMessage(bar);
		args.getSender().sendMessage(ChatColor.GREEN + "Commands");
		args.getSender().sendMessage(bar);
		for(String command : commandinfo.keySet()) {
			args.getSender().sendMessage(Msg.CommandList(command, commandinfo.get(command)));
		}
		args.getSender().sendMessage(bar);
	}
	
	@Command(name = "oitc.create", permission = Perms.CREATOR)
	public void create(CommandArgs args) {
		EPlayer eplayer = new EPlayer(args.getPlayer().getUniqueId());
		eplayer.setArena(new Arena());
	//	args.getPlayer().sendMessage(Msg.info("Escriba el nombre de la arena"));
		args.getPlayer().sendMessage(Msg.info("Write the name of the arena"));
		args.getPlayer().sendMessage(Msg.info("Write 'cancel' if you want to cancel the process"));
		eplayer.add();
	}
	
	@Command(name = "oitc.arenas", permission = Perms.CREATOR)
	public void arenas(CommandArgs args) {
		args.getSender().sendMessage(bar);
		args.getSender().sendMessage(ChatColor.GREEN + "Arenas" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + Arena.getArenas().size());
		args.getSender().sendMessage(bar);
		for (Arena arena : Arena.getArenas()) {
			args.getSender().sendMessage(Msg.viewArenas(arena));
		}
		args.getSender().sendMessage(bar);
	}
	
	@Command(name = "oitc.lobby", permission = Perms.CREATOR)
	public void lobby(CommandArgs args) {
		if (!args.isPlayer()) {
			args.getSender().sendMessage("Command for Players");
			return;
		}
		
		Player player = args.getPlayer();
		
		Lobby.setSpawnpoint(player.getLocation());
		player.sendMessage(Msg.playeMessage("Lobby set"));
	}
	
	@Command(name = "leave")
	public void leave(CommandArgs args) {
		if (!args.isPlayer()) {
			args.getSender().sendMessage("Command for Players");
			return;
		}
		
		Player player = args.getPlayer();
		GPlayer gplayer = GPlayer.getGPlayer(player.getUniqueId());
		
		if (gplayer.getArena() == null) return;
		Arena arena = gplayer.getArena();
		arena.removePlayerUUID(gplayer.getUuid(), LeaveReason.LEAVE);
		
	}
}
