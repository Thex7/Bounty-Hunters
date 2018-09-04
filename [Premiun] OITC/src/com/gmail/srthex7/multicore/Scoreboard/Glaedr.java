package com.gmail.srthex7.multicore.Scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

public class Glaedr
implements Listener {
    private static JavaPlugin plugin;
    private String title;
    private boolean hook;
    private boolean overrideTitle;
    private boolean scoreCountUp;
    private List<String> bottomWrappers;
    private List<String> topWrappers;

    public Glaedr(JavaPlugin plugin, String title, boolean hook, boolean overrideTitle, boolean scoreCountUp) {
        Glaedr.plugin = plugin;
        this.title = ChatColor.translateAlternateColorCodes((char)'&', (String)title);
        this.hook = hook;
        this.overrideTitle = overrideTitle;
        this.scoreCountUp = scoreCountUp;
        this.bottomWrappers = new ArrayList<String>();
        this.topWrappers = new ArrayList<String>();
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    public Glaedr(JavaPlugin plugin, String title) {
        this(plugin, title, false, true, false);
    }

    public void registerPlayers() {
    	ArrayList<Player> arrplayer = new ArrayList<>();
        for(Player all:Bukkit.getOnlinePlayers()){
        	arrplayer.add(all);
        }
        int n = arrplayer.size();
        int n2 = 0;
        while (n2 < n) {
            Player player = arrplayer.get(n2);
            new PlayerScoreboard(this, player);
            ++n2;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerScoreboard playerScoreboard = PlayerScoreboard.getScoreboard(player);
        if (playerScoreboard == null) {
            new PlayerScoreboard(this, player);
        } else {
            if (player.getScoreboard() != playerScoreboard.getScoreboard()) {
                if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                    playerScoreboard.setObjective(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR));
                } else {
                    Objective objective = player.getScoreboard().registerNewObjective(player.getName(), "dummy");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    objective.setDisplayName(this.title);
                    playerScoreboard.setObjective(objective);
                }
                playerScoreboard.setScoreboard(player.getScoreboard());
                for (Entry entry : playerScoreboard.getEntries()) {
                    entry.setup();
                }
                for (Wrapper wrapper : playerScoreboard.getWrappers()) {
                    wrapper.setup();
                }
            }
            player.setScoreboard(playerScoreboard.getScoreboard());
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        cleanScoreboard(player);
    }
    /*
    @EventHandler
    public void onDamage(EntityDamageEvent e){
    	if(e.getEntity() instanceof Player && PlayerScoreboard.ENABLEPLAYERHEALTH){
    		Player player = (Player)e.getEntity();
    		for(Player all : Bukkit.getOnlinePlayers()){
    		//	PlayerScoreboard.getScoreboard(player).getObjectiveList().getScore(all).setScore((int)player.getHealth());
    		}
    	}
    }
*/
    public static void cleanScoreboard(Player player) {
        PlayerScoreboard playerScoreboard = PlayerScoreboard.getScoreboard(player);
        if (playerScoreboard != null) {
            PlayerScoreboard.getScoreboards().remove(playerScoreboard);
            for (Entry entry : playerScoreboard.getEntries()) {
                entry.cancel();
            }
            for(Team t : playerScoreboard.getScoreboard().getTeams()){
            	t.unregister();
            }
        }
    }
    
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isHook() {
        return this.hook;
    }

    public boolean isOverrideTitle() {
        return this.overrideTitle;
    }

    public boolean isScoreCountUp() {
        return this.scoreCountUp;
    }

    public List<String> getBottomWrappers() {
        return this.bottomWrappers;
    }

    public List<String> getTopWrappers() {
        return this.topWrappers;
    }
}

