package com.gmail.srthex7.multicore.Regions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.srthex7.multicore.File.ConfigUtil;

public class Region {
	private String name;
	private Location loc1;
	private Location loc2;
	private List<Block> blocks = new ArrayList<Block>();
	
	public Region (String name,Location loc1 , Location loc2){
		this.name = name.toLowerCase();
		this.loc1 = loc1;
		this.loc2 = loc2;
		this.analizeblockinarea();
	}
	
	public Location getPointOne(){
		return loc1;
	}
	
	public Location getPointTwo(){
		return loc2;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPointOne(Location loc1){
		this.loc1 = loc1;
	}
	
	public void setPointTwo(Location loc2){
		this.loc2 = loc2;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public List<Block> getBlocks(){
		return this.blocks;
	}
	
	public boolean inRegion(Location loc){
		int x = loc.getBlockX();
		int y = loc.getBlockY();
        int z = loc.getBlockZ();
        
        int xmin = loc1.getBlockX();
        int ymin = loc1.getBlockY();
        int zmin = loc1.getBlockZ();
        
        int xmax = loc2.getBlockX();
        int ymax = loc2.getBlockY();
        int zmax = loc2.getBlockZ();
        if(loc.getWorld().getName() != loc1.getWorld().getName())return false;
        if((x >= xmin && x <= xmax )|| (xmin >= x && xmax <= x)){
        	if((y >= ymin && y <= ymax) || (ymin >= y && ymax <= y)){
        		if((z >=zmin && z <= zmax) || (zmin >= z && zmax <= z)){
        			return true;
        		}
        		return false;
        	}
        	return false;
        }
        return false;
	}
	
	private List<Block> analizeblockinarea(){
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
 
        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
 
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
 
        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
       
        return blocks;
    }
	
	public void save(JavaPlugin plugin,String folder){
		ConfigUtil cu = new ConfigUtil(plugin,folder,name.toLowerCase());
		cu.getConfig().set("NAME", name);
		cu.getConfig().set("LOC1", Loc.serializeLoc(loc1));
		cu.getConfig().set("LOC2", Loc.serializeLoc(loc2));
		cu.save();
	}
	
	static ArrayList<Region> regions = new ArrayList<Region>();
	
	public static void loadRegions(JavaPlugin plugin,String folder){
		File f = new File(plugin.getDataFolder(),folder);
		if(f.exists()){
			for(String fl : f.list()){
				ConfigUtil cu = new ConfigUtil(plugin,folder,fl,false);
				if(cu.exist()){
					if(cu.getConfig().getName() != null){
						regions.add(new Region(cu.getConfig().getString("NAME"),
								Loc.deserializeLoc(cu.getConfig().getString("LOC1")),
								Loc.deserializeLoc(cu.getConfig().getString("LOC2"))));
					}
				}
			}
		}
	}
	
	public static ArrayList<Region> getRegions(){
		return regions;
	}
	
	/*		File file = new File(plugin.getDataFolder(),"/"+folder+"/"+fl);
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if(file.exists()){
	 * */
}
