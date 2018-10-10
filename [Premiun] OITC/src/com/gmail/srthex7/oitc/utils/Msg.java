package com.gmail.srthex7.oitc.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.gmail.srthex7.oitc.system.Arena;

/**
 * En esta clase guardan los
 * formatos de los mensajes
 *
 */
public class Msg {

	/**
	 * Da un formato al texto.
	 * @param command nombre del comando.
	 * @param description descripción del comando.
	 * @return formato con el que se mostrara el texto
	 */
	public static String CommandList(String command, String description) {
		return ChatColor.GREEN + command + "§8: " + ChatColor.GRAY + description;
	}
	
	/**
	 * Da un formato al texto.
	 * @param clan nombre del clan
	 * @param siglas siglas del clan
	 * @param lideres lista de los lideres del clan
	 * @return formato con el que se mostrara el texto
	 */
	public static String ClanList(String clan, String siglas, List<String> lideres) {
		String l = "";
		for(String s : lideres) l += s + ", ";
		return clan + " | " + siglas + " | " + l.substring(0, l.length()-2);
	}
	
	/**
	 * Da un formato al texto
	 * @param user nombre del usuario o miembro
	 * @param clan nombre del clan
	 * @return formato con el que se mostrara el texto
	 */
	public static String ClanVer(String user, String clan) {
		return "§a" + user + " §eesta en el clan §b" + clan;
	}
	
	/**
	 * Da un formato al texto
	 * @param member nombre del miembro del clan
	 * @param rank nombre del rango (Lider, SubLider, Miembro)
	 * @return formato con el que se mostrara el texto
	 * SUGERENCIA: Se puede hacer una condicional con los rangos para que se muestren de distinto color.
	 */
	public static String ClanMiembros(String member, String rank) {
		return "- §e" + member + " §a" + rank;
	}
	
	/**
	 * Imprime un mensaje en consola con el prefijo del plugin
	 * @param log mensaje
	 */
	public static void log(String log) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "OITC >" + ChatColor.GRAY + log);
	}
	
	public static String info(String info) {
		return ChatColor.YELLOW + "*" + info;
	}
	
	public static String viewArenas(Arena arena) {
		return ChatColor.GRAY + "> " + 
	ChatColor.GREEN + arena.getArenaname() + ChatColor.GRAY + " : " + 
	ChatColor.GREEN + arena.getMapname() + ChatColor.GRAY + " : " + 
	ChatColor.GREEN + arena.getArenaState().toString() + ChatColor.GRAY + " : " + 
	ChatColor.GREEN + "(" + ChatColor.GRAY +  arena.getPlayerUuids().size() + ChatColor.GREEN + "/" + ChatColor.GRAY +  arena.getMaxusers() + ChatColor.GREEN + ")";
	}
	
	public static String playeMessage(String text) {
		return ChatColor.YELLOW + text;
	}
}

