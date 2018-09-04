package com.gmail.srthex7.multicore.Scoreboard;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerScoreboard {
    private static Set<PlayerScoreboard> scoreboards = new HashSet<PlayerScoreboard>();
    private Player player;
    private Objective objective;
    private Objective objHealth;
    private Objective objPlayerList;
    private Scoreboard scoreboard;
    private Map<Entry, String> keys;
    private Map<Entry, Integer> scores;
    private List<Entry> entries;
    private List<Wrapper> wrappers;
    private BukkitTask task;
    private boolean countup = false;

    public PlayerScoreboard(Glaedr main, Player player) {
        String string;
        this.player = player;
        this.keys = new HashMap<Entry, String>();
        this.scores = new HashMap<Entry, Integer>();
        this.wrappers = new ArrayList<Wrapper>();
        this.entries = new ArrayList<Entry>();
        this.countup = main.isScoreCountUp();
        this.createScoreboard(main.getTitle(), main.isHook(), main.isOverrideTitle());
        int i = 0;
        while (i < main.getTopWrappers().size()) {
            string = main.getTopWrappers().get(i);
            new Wrapper("top_" + i, this, Wrapper.WrapperType.TOP).setText(string).send();
            ++i;
        }
        i = 0;
        while (i < main.getBottomWrappers().size()) {
            string = main.getBottomWrappers().get(i);
            new Wrapper("bottom_" + i, this, Wrapper.WrapperType.BOTTOM).setText(string).send();
            ++i;
        }
        this.run();
        scoreboards.add(this);
    }

    public String getAssignedKey(Entry entry) {
        if (keys.containsKey(entry)) {
            return keys.get(entry);
        }
        for (ChatColor color : ChatColor.values()) {

            String colorText = color + "" + ChatColor.WHITE;

            if (entry.getText().length() > 16) {
                String sub = entry.getText().substring(0, 16);
                colorText = colorText + ChatColor.getLastColors(sub);
            }

            if (!keys.values().contains(colorText)) {
                keys.put(entry, colorText);
                return colorText;
            }
        }
        throw new IndexOutOfBoundsException("No more keys available!");
    }
    public int getScore(Entry entry) {
        int start = 15 - this.getTopWrappers().size();
        int goal = 0;
        if (entry instanceof Wrapper) {
            Wrapper wrapper = (Wrapper)entry;
            if (wrapper.getType() == Wrapper.WrapperType.TOP) {
                goal = start;
                start = 15;
            } else {
                goal = (start -= this.getEntries().size()) - this.getBottomWrappers().size();
            }
        }
        int i = start;
        while (i > goal) {
            if (!this.scores.containsKey(entry)) {
                if (!this.scores.values().contains(i)) {
                    this.scores.put(entry, i);
                    return i;
                }
            } else {
                int score = this.scores.get(entry);
                int toSub = 0;
                while (toSub < start) {
                    if (i - toSub > score && !this.scores.values().contains(i - toSub)) {
                        this.scores.put(entry, i - toSub);
                        return i - toSub;
                    }
                    ++toSub;
                }
                if (entry instanceof Wrapper && ((Wrapper)entry).getType() == Wrapper.WrapperType.BOTTOM && score > start) {
                    this.scores.put(entry, start);
                    return start;
                }
                return score;
            }
            --i;
        }
        return 0;
    }

    public Entry getEntry(String id) {
        for (Entry entry : this.getEntries()) {
            if (!entry.getId().equals(id)) continue;
            return entry;
        }
        return null;
    }

    public static PlayerScoreboard getScoreboard(Player player) {
        for (PlayerScoreboard playerScoreboard : PlayerScoreboard.getScoreboards()) {
            if (!playerScoreboard.getPlayer().getName().equals(player.getName())) continue;
            return playerScoreboard;
        }
        return null;
    }

    public static Set<PlayerScoreboard> getScoreboards() {
        return scoreboards;
    }

    private List<Wrapper> getTopWrappers() {
        ArrayList<Wrapper> toReturn = new ArrayList<Wrapper>();
        for (Wrapper wrapper : this.getWrappers()) {
            if (wrapper.getType() != Wrapper.WrapperType.TOP) continue;
            toReturn.add(wrapper);
        }
        return toReturn;
    }

    private List<Wrapper> getBottomWrappers() {
        ArrayList<Wrapper> toReturn = new ArrayList<Wrapper>();
        for (Wrapper wrapper : this.getWrappers()) {
            if (wrapper.getType() != Wrapper.WrapperType.BOTTOM) continue;
            toReturn.add(wrapper);
        }
        return toReturn;
    }


    public void createScoreboard(String title, boolean hook, boolean overrideTitle) {
        if (hook && this.player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            this.scoreboard = this.player.getScoreboard();
            if (this.scoreboard.getObjective(DisplaySlot.SIDEBAR) != null) {
                this.objective = this.scoreboard.getObjective(DisplaySlot.SIDEBAR);
                if (overrideTitle) {
                    this.objective.setDisplayName(title);
                }
            } else {
                this.objective = this.scoreboard.registerNewObjective(this.player.getName(), "dummy");
                this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                this.objective.setDisplayName(title);
            }
            return;
        }
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective(this.player.getName(), "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(title);
        
        if(PlayerScoreboard.ENABLEPLAYERHEALTH){
        	this.objHealth=  this.scoreboard.registerNewObjective("corazones", "health");
        	this.objHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
        	this.objHealth.setDisplayName(ChatColor.RED + "❤");
        	
        /*	this.objPlayerList = this.scoreboard.registerNewObjective("numero", "dummy");
        	this.objPlayerList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        	Score s = this.objPlayerList.getScore(player);
        	s.setScore((int)player.getHealth());*/
        }
        /*
        scoreboard.registerNewTeam("RED").setPrefix(ChatColor.RED+"");
        scoreboard.registerNewTeam("GOLD").setPrefix(ChatColor.GOLD+"");
        scoreboard.registerNewTeam("DGREEN").setPrefix(ChatColor.DARK_GREEN+"");
        scoreboard.registerNewTeam("GREEN").setPrefix(ChatColor.GREEN+"");
        scoreboard.getTeam("RED").setAllowFriendlyFire(true);
        scoreboard.getTeam("GOLD").setAllowFriendlyFire(true);
        scoreboard.getTeam("DGREEN").setAllowFriendlyFire(true);
        scoreboard.getTeam("GREEN").setAllowFriendlyFire(true);
        /*
        this.objHealth =  this.scoreboard.registerNewObjective(this.player.getName()+"_UpT", "aaa");
        this.objHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
        this.objHealth.setDisplayName(ChatColor.GRAY + FactionUtil.getFactionName(player));*/
    }

    public static boolean ENABLEPLAYERHEALTH = false;
    
    private void run() {
        task = new BukkitRunnable() {
            @Override
            public void run() {

                Iterator<Entry> entryIterator = getEntries().iterator();
                while (entryIterator.hasNext()) {


                    Entry entry = entryIterator.next();

                    if (entry.isCancelled()) {
                        scoreboard.resetScores(entry.getKey());
                        keys.remove(entry);
                        scores.remove(entry);
                        entryIterator.remove();

                        if (entry.getTime() != null && entry.getTime().doubleValue() > 0) {
                            Bukkit.getPluginManager().callEvent(new EntryCancelEvent(entry, PlayerScoreboard.this));
                        }
                        continue;
                    }


                    Iterator<Wrapper> wrapperIterator = getWrappers().iterator();
                    while (wrapperIterator.hasNext()) {
                        Wrapper wrapper = wrapperIterator.next();

                        if (getEntries().size() == 0) {
                            scoreboard.resetScores(wrapper.getKey());
                            keys.remove(wrapper);
                            scores.remove(wrapper);
                            continue;
                        }

                        wrapper.sendScoreboardUpdate(wrapper.getText());
                    }

                    Bukkit.getPluginManager().callEvent(new EntryTickEvent(entry, PlayerScoreboard.this));

                    if (!(entry.isCountdown()) && !entry.isCountup()) {
                        entry.sendScoreboardUpdate(entry.getText());
                        continue;
                    }

                    if (entry.getTime().doubleValue() <= 0 && !entry.isCountup()) {
                        entry.setCancelled(true);
                        Bukkit.getPluginManager().callEvent(new EntryFinishEvent(entry, PlayerScoreboard.this));
                        continue;
                    }

                    if (60 > entry.getTime().intValue() || entry.isBypassAutoFormat()) {
                        String toSend = entry.getText() + " " + entry.getTime();
                        if (!entry.isRemoveTimeSuffix()) {
                            toSend = toSend + "s";
                        }
                        entry.setTextTime(entry.getTime() + "s");
                        entry.sendScoreboardUpdate(toSend);
                        if (!(entry.isPaused())) {
                            if (entry.isCountup()) {
                                entry.setTime(entry.getTime().add(BigDecimal.valueOf(0.1)));
                            } else {
                                entry.setTime(entry.getTime().subtract(BigDecimal.valueOf(0.1)));
                            }
                        }
                        continue;
                    }
                    if (3600 > entry.getTime().intValue()) {
                        entry.setInterval(entry.getInterval() - 1);

                        int minutes = entry.getTime().intValue() / 60;
                        int seconds = entry.getTime().intValue() % 60;
                        DecimalFormat formatter = new DecimalFormat("00");
                        String toSend = entry.getText() + " " + formatter.format(minutes) + ":" + formatter.format(seconds);
                        entry.setTextTime(formatter.format(minutes) + ":" + formatter.format(seconds));

                        if (!entry.isRemoveTimeSuffix()) {
                            toSend = toSend + "m";
                            entry.setTextTime(formatter.format(minutes) + ":" + formatter.format(seconds) + "m");
                        }

                        entry.sendScoreboardUpdate(toSend);

                        if (entry.getInterval() <= 0) {
                            if (!(entry.isPaused())) {
                                if (entry.isCountup()) {
                                    entry.setTime(entry.getTime().add(BigDecimal.ONE));
                                } else {
                                    entry.setTime(entry.getTime().subtract(BigDecimal.ONE));
                                }
                            }
                            entry.setInterval(10);
                        }
                        continue;
                    }
                    entry.setInterval(entry.getInterval() - 1);

                    int hours = entry.getTime().intValue() / 3600;
                    int minutes = (entry.getTime().intValue() % 3600) / 60;
                    int seconds = entry.getTime().intValue() % 60;

                    DecimalFormat formatter = new DecimalFormat("00");
                    String toSend = entry.getText() + " " + formatter.format(hours) + ":" + formatter.format(minutes) + ":" + formatter.format(seconds);
                    entry.setTextTime(formatter.format(hours) + ":" + formatter.format(minutes) + ":" + formatter.format(seconds));

                    if (!entry.isRemoveTimeSuffix()) {
                        toSend = toSend + "m";
                        entry.setTextTime(formatter.format(minutes) + ":" + formatter.format(seconds) + "m");
                    }

                    entry.sendScoreboardUpdate(toSend);

                    if (entry.getInterval() <= 0) {
                        if (!(entry.isPaused())) {
                            if (entry.isCountup()) {
                                entry.setTime(entry.getTime().add(BigDecimal.ONE));
                            } else {
                                entry.setTime(entry.getTime().subtract(BigDecimal.ONE));
                            }
                        }

                        entry.setInterval(10);
                    }

                    continue;
                }

                Iterator<Wrapper> wrapperIterator = getWrappers().iterator();
                while (wrapperIterator.hasNext()) {
                    Wrapper wrapper = wrapperIterator.next();

                    if (getEntries().size() == 0) {
                        scoreboard.resetScores(wrapper.getKey());
                        keys.remove(wrapper);
                        scores.remove(wrapper);
                        continue;
                    }

                    wrapper.sendScoreboardUpdate(wrapper.getText());
                }

                
                
                
                
            }
        }.runTaskTimer(Glaedr.getPlugin(), 2l, 2l);
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }
    
    public void setObjectiveHealth(Objective objective){
    	this.objHealth = objective;
    }
    /*
    public void setObjectiveList(Objective objective){
    	this.objHealth = objective;
    }
    */
    public Objective getSpecificObjective(String obj){
    	return this.scoreboard.getObjective(obj);
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Objective getObjective() {
        return this.objective;
    }
    
    public Objective getObjectiveHealth() {
        return this.objHealth;
    }
    /*
    public Objective getObjectiveList(){
    	return this.objPlayerList;
    }*/

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public Map<Entry, String> getKeys() {
        return this.keys;
    }

    public Map<Entry, Integer> getScores() {
        return this.scores;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public List<Wrapper> getWrappers() {
        return this.wrappers;
    }

    public BukkitTask getTask() {
        return this.task;
    }

    public boolean isCountup() {
        return this.countup;
    }

    
}

