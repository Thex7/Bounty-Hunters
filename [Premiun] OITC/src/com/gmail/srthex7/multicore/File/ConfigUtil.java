package com.gmail.srthex7.multicore.File;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtil {

	private File file;
	private FileConfiguration config;
	private String name;
	public ConfigUtil(JavaPlugin plugin,String name){
		this.file = new File(plugin.getDataFolder(),name+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		this.name = name;
	}
	
	public ConfigUtil(JavaPlugin plugin, String folder,String name){
		this.file = new File(plugin.getDataFolder(),"/"+folder+"/"+name+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		this.name = name;
	}
	
	public ConfigUtil(JavaPlugin plugin, String folder,String name,boolean extension){
		this.file = new File(plugin.getDataFolder(),"/"+folder+"/"+name);
		this.config = YamlConfiguration.loadConfiguration(file);
		this.name = name;
	}
	
	public File getFile(){
		return file;
	}
	
	public FileConfiguration getConfig(){
		return config;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean exist(){
		return file.exists();
	}
	
	public void save(){
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
