package com.gmail.srthex7.oitc.system.lobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.srthex7.multicore.Item.ItemBuilder;
import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.ArenaState;

public class Utilities {

	public static String inventoryTitle = ChatColor.DARK_GRAY + "Rooms";
	private static Inventory roomsInventory = Bukkit.createInventory(null, 2*9, inventoryTitle);
	public static List<String> arenaDescription = new ArrayList<>();
	
	public static void applyColors () {
		List<String> temp = new ArrayList<>();
		for (String s : arenaDescription) {
			temp.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		arenaDescription = temp;
	}
	
	public static ItemStack getSearchRoom () {
		return new ItemBuilder(Material.COMPASS).displayname("&aSearch Room").build();
	}
	
	public static Inventory getRoomsInventory () {
		return roomsInventory;
	}
	
	@SuppressWarnings("deprecation")
	public static void updateRoomsInventory() {
		Inventory inv = Bukkit.createInventory(null, 2*9, inventoryTitle);
		for (Arena arena : Arena.getArenas()) {
			ItemBuilder ib = new ItemBuilder(Material.WOOL);
			
			if (arena.getArenaState().equals(ArenaState.WAITING)) {
				ib.durability(DyeColor.LIME.getWoolData());
			} else if (arena.getArenaState().equals(ArenaState.STARTING)) {
				ib.durability(DyeColor.ORANGE.getWoolData());
			} else if (arena.getArenaState().equals(ArenaState.INGAME)) {
				ib.durability(DyeColor.RED.getWoolData());
			} else if (arena.getArenaState().equals(ArenaState.ENDING)) {
				ib.durability(DyeColor.GRAY.getWoolData());
			} else {
				ib.durability(DyeColor.BLACK.getWoolData());
			}
			
			ib.displayname(ChatColor.GREEN + arena.getArenaname());
			
			for (String s : arenaDescription) {
				String text = ChatColor.translateAlternateColorCodes('&', s
						.replaceAll("<state>", arena.getArenaState().toString())
						.replaceAll("<map>", arena.getMapname())
						.replaceAll("<maxplayers>", arena.getMaxusers()+"")
						.replaceAll("<onlineplayers>", arena.getPlayerUuids().size()+""));
				ib.lore(text);
			}
			
			inv.addItem(ib.build());
		}
		roomsInventory = inv;
	}
}
