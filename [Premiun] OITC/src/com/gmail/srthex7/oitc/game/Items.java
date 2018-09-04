package com.gmail.srthex7.oitc.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.srthex7.multicore.Item.ItemBuilder;

public class Items {

	public static void setGameInventory (Player player) {
		player.getInventory().setItem(0, sword());
		player.getInventory().setItem(8, arrow(1));
		player.getInventory().setItem(1, bow());
	}
	
	public static ItemStack arrow (int amount) {
		return new ItemBuilder(Material.ARROW).amount(amount).build();
	}
	
	public static ItemStack sword () {
		return new ItemBuilder(Material.WOOD_SWORD).build();
	}
	
	public static ItemStack bow () {
		return new ItemBuilder(Material.BOW).build();
	}
	
	public static void addArrow(Player player) {
		if (!player.getInventory().contains(Material.ARROW)) 
			player.getInventory().addItem(arrow(1));
	}
}
