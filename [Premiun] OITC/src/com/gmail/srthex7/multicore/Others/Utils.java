package com.gmail.srthex7.multicore.Others;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Utils {

	public static int rndInt (int min, int max){
		Random r = new Random();
		int i = r.nextInt((max-min) + 1) + min;
		return i;
	}
	
	public static String transColor (String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static void clearPlayer(Player player){
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		for(PotionEffect p : player.getActivePotionEffects()){
			player.removePotionEffect(p.getType());
		}
	}
	
	public static boolean isInt(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String formatMilisecondsToSeconds(final Long n) {
        return String.format("%1$.1f", (n + 0.0f) / 1000.0f);
	}
	    
	public static String formatSecondsToMinutes(final int n) {
	    return String.format("%02d:%02d", n / 60, n % 60);
	}
	
	public static String formatSecondsToHours(final int n) {
	    return String.format("%02d:%02d:%02d", n / 3600, n % 3600 / 60, n % 60);
	}
    
}
