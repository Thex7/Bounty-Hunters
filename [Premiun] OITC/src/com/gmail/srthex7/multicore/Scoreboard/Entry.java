package com.gmail.srthex7.multicore.Scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

 public class Entry {
	private PlayerScoreboard playerScoreboard;
	private boolean countdown;
	private boolean countup;
  	private String text;
  	private String id;
  	private String key;
  	private String textTime;
   	private String originalText;
  	private java.math.BigDecimal time;
 	private int interval;
  	private Team team;
  	private boolean cancelled;
   	private boolean paused;
 
   	public Entry setPlayerScoreboard(PlayerScoreboard playerScoreboard) {
     this.playerScoreboard = playerScoreboard;
     return this; 
     } 
   	
   	public Entry setCountdown(boolean countdown) { 
   		this.countdown = countdown;
   	return this; 
   	} 
   	
   	public Entry setCountup(boolean countup) { 
   		this.countup = countup;
   	return this; 
   	} 
   	
   	public Entry setId(String id) { 
   		this.id = id;
   	return this; 
   	} 
   	
   	public Entry setKey(String key) { 
   		this.key = key;
   		return this; 
   		} 
   	
   	public Entry setTextTime(String textTime) { 
   		this.textTime = textTime;
   		return this; 
   		} 
   	
   	public Entry setOriginalText(String originalText) { 
   		this.originalText = originalText;
   		return this; 
   		} 
   	
   	public Entry setInterval(int interval) { 
   		this.interval = interval;
   		return this; 
   		} 
   	
   	public Entry setTeam(Team team) { 
   		this.team = team;
   		return this; 
   		} 
   	
   	public Entry setCancelled(boolean cancelled) { 
   		this.cancelled = cancelled;
   		return this; 
   		} 
   	
   	public Entry setPaused(boolean paused) { 
   		this.paused = paused;
   		return this; 
   		} 
   	
   	public Entry setSet(boolean set) { 
   		this.set = set;
   		return this; 
   	}
   	
   	public Entry setBypassAutoFormat(boolean bypassAutoFormat) { 
   		this.bypassAutoFormat = bypassAutoFormat;return this;
   		} 
   	public Entry setRemoveTimeSuffix(boolean removeTimeSuffix) { 
   		this.removeTimeSuffix = removeTimeSuffix;
   		return this; 
   		}
   
    private boolean set;
 
   	public PlayerScoreboard getPlayerScoreboard() { 
   		return this.playerScoreboard; 
   		}
   	
   	public boolean isCountdown() { 
   		return this.countdown; 
   		} 
   	
   	public boolean isCountup() { 
   		return this.countup; 
   		}
   	
  	public String getText() { 
	  return this.text; 
	  } 
  	
  	public String getId() { 
	  return this.id; 
	  } 
  	
  	public String getKey() { 
	  return this.key; 
	  } 
  	
  	public String getTextTime() { 
	  return this.textTime; 
	  } 
  	
  	public String getOriginalText() { 
	  return this.originalText; 
	  }
  	
  	public java.math.BigDecimal getTime() { 
	  return this.time; 
	  }
  	
   	public int getInterval() { 
	   return this.interval; 
	   }
   	
   	public Team getTeam() { 
	  return this.team; 
	  }
  	public boolean isCancelled() { 
	  return this.cancelled;
	  } 
  	
  	public boolean isPaused() {
	  return this.paused; 
	  } 
  	
  	public boolean isSet() { 
	  return this.set; 
	  } 
  	
  	public boolean isBypassAutoFormat() { 
	  return this.bypassAutoFormat; 
	  } 
  	
  	public boolean isRemoveTimeSuffix() { 
	  return this.removeTimeSuffix; 
	  }
  
  	public Entry(String id, PlayerScoreboard playerScoreboard) {
  		this.id = id;
  		this.playerScoreboard = playerScoreboard; 
  		}
 
  	private boolean bypassAutoFormat;
  	private boolean removeTimeSuffix;
  	public Entry send() { if (this.playerScoreboard.getEntries().size() + this.playerScoreboard.getWrappers().size() < 15) {
      setup();
      this.paused = false;
      if (!(this instanceof Wrapper)) {
         if (!this.playerScoreboard.getEntries().contains(this)) {
         this.playerScoreboard.getEntries().add(this);
         }
     }
      else if (!this.playerScoreboard.getWrappers().contains(this)) {
         this.playerScoreboard.getWrappers().add((Wrapper)this);
     }
    }
    
     return this;
  }
  

/*
public void sendScoreboardUpdate(String text) {
    Objective objective = playerScoreboard.getObjective();

    if (text.length() > 16) {
        team.setPrefix(text.substring(0, 16));

        String suffix = ChatColor.getLastColors(team.getPrefix()) + text.substring(16, text.length());

        if (suffix.length() > 16) {

            if (suffix.length() - 2 <= 16) {
                suffix = text.substring(16, text.length());
                team.setSuffix(suffix.substring(0, suffix.length()));
            } else {
                team.setSuffix(suffix.substring(0, 16));
            }



        } else {
            team.setSuffix(suffix);
        }
    } else {
        team.setPrefix(text);
        team.setSuffix("");
    } 

    Score score = objective.getScore(key);
    score.setScore(playerScoreboard.getScore(this));


    playerScoreboard.getPlayer().setScoreboard(playerScoreboard.getScoreboard());
}*/
  String subtext(String text, int sub1,int sub2){
	  String a1 = text.substring(0,sub2);
	  String a2 = text.substring(sub1-1);
	  return a2 + a1;
  }
  	public void sendScoreboardUpdate(String text) {
  	    Objective objective = playerScoreboard.getObjective();
  	  if(text.length()<=16){
  		  team.setPrefix(text);
  		  team.setSuffix("");
  	  }else{
  		  String t1 = text.substring(0,16);//borra los ultimos 17 caracteres
  		  String t2 = t1.substring(14); //borra los primero 14 caracteres
  		  String t3 = t2.substring(0, 1); //char 15
  		  String t4 = t2.substring(1); //char 16
  		  if(t3.contains("§")){
  			  team.setPrefix(text.substring(0,14));
  			  team.setSuffix(text.substring(14));
  		  }else if(t4.contains("§")){
  			  team.setPrefix(text.substring(0,15));
  			  team.setSuffix(text.substring(15));
  		  }else{
  			  team.setPrefix(text.substring(0,16));
  			  team.setSuffix(ChatColor.getLastColors(text.substring(0,16)) + text.substring(16));
  		  }
  	  }
  	    
  	    Score score = objective.getScore(key);
  	    score.setScore(playerScoreboard.getScore(this));


  	    playerScoreboard.getPlayer().setScoreboard(playerScoreboard.getScoreboard());
  	}
  public void setup() {
     org.bukkit.scoreboard.Scoreboard scoreboard = this.playerScoreboard.getScoreboard();
     
   this.text = org.bukkit.ChatColor.translateAlternateColorCodes('&', this.text);
    this.key = this.playerScoreboard.getAssignedKey(this);
   
    String teamName = this.id;
    if (teamName.length() > 16) {
     teamName = teamName.substring(0, 16);
    }
   
    if (scoreboard.getTeam(teamName) != null) {
      this.team = scoreboard.getTeam(teamName);
     } else {
      this.team = scoreboard.registerNewTeam(teamName);
     }
   if (!this.team.getEntries().contains(this.key)) {
      this.team.addEntry(this.key);
   }
  }
   
  private boolean isValid() {
     if (this.text == null) {
      throw new NullPointerException("Entry text not defined!");
     }
     if (this.text.length() > 32) {
      throw new StringIndexOutOfBoundsException("Entry text must be equal to or below 32 characters long!");
     }
     return true;
  }
  
  public Entry setTime(double time) {
    this.time = java.math.BigDecimal.valueOf(time);
     return this;
   }
   
   public Entry setText(String text) {
     this.text = ChatColor.translateAlternateColorCodes('&', text);
     
     if (!this.set) {
      this.set = true;
      this.originalText = ChatColor.translateAlternateColorCodes('&', text);;
    }
     
     return this;
   }
  
   public Entry setTime(java.math.BigDecimal time) {
     this.time = time;
     return this;
   }
   
   public void cancel() {
     setCancelled(true);
  }
 }
/*
 
 public void sendScoreboardUpdate(String text) {
   Objective objective = playerScoreboard.getObjective();
 //  Objective objHealth = playerScoreboard.getObjectiveHealth();
   if(text.length()>16){
   	String c = ChatColor.getLastColors(text.substring(17));
   //	String ult = text.substring(16-2);
   //	ult = ult.substring(0, 2);
   	//String color = text.substring(15);
   	//color = color.substring(0, 2);
   //	String ucolor = ChatColor.getLastColors(color);
   		team.setPrefix(text.substring(0, 16));
 		team.setSuffix(text.substring(16));
  // 	}
   	
   }else{
   	team.setPrefix(text);
   	team.setSuffix("  ");
   }
   
   Score score = objective.getScore(key);
   score.setScore(playerScoreboard.getScore(this));


   playerScoreboard.getPlayer().setScoreboard(playerScoreboard.getScoreboard());
}*/