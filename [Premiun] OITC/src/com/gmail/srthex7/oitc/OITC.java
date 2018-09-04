package com.gmail.srthex7.oitc;

import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.srthex7.multicore.Scoreboard.Glaedr;
import com.gmail.srthex7.multicore.command.CommandFramework;
import com.gmail.srthex7.oitc.game.Lang;
import com.gmail.srthex7.oitc.game.Settings;
import com.gmail.srthex7.oitc.listeners.ArenaListeners;
import com.gmail.srthex7.oitc.listeners.PlayerListeners;
import com.gmail.srthex7.oitc.system.Arena;
import com.gmail.srthex7.oitc.system.StaffCommands;
import com.gmail.srthex7.oitc.system.Task;
import com.gmail.srthex7.oitc.system.editor.EPlayerListeners;

public class OITC extends JavaPlugin{

	private static OITC plugin;
	public static OITC getInstance() {
		return plugin;
	}
	
	private CommandFramework framework;
	private Glaedr glaedr;
	
	@Override
	public void onEnable() {
		plugin = this;
		framework = new CommandFramework(this);
		
		new Lang();
		new Settings();
		
		this.loadArenas();
		this.registerCommands();
		this.registerListeners();
		this.registerScoreboard();
	
		BukkitRunnable task = new Task();
		task.runTaskTimerAsynchronously(this, 0l, 20l);
	}
	
	public CommandFramework getFramework() {
		return this.framework;
	}
	
	public Glaedr getScoreboard() {
		return this.glaedr;
	}
	
	public void registerCommands() {
		new StaffCommands(this);
	}
	
	public void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new EPlayerListeners(), this);
		pm.registerEvents(new ArenaListeners(),this);
		pm.registerEvents(new PlayerListeners(), this);
	}
	
	public void registerScoreboard() {
		glaedr = new Glaedr(this,"&6&lBounty Hunters", false, true, true);
		glaedr.registerPlayers();
	}
	
	public void loadArenas() {
		Arena.loadAll();
	}
	
	
	
}
