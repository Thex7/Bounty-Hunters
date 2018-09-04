package com.gmail.srthex7.multicore.Scoreboard;

public class Wrapper
  extends Entry
{
  private WrapperType type;
  
  public WrapperType getType()
  {
    return this.type;
  }
  
  public Wrapper setType(WrapperType type)
  {
    this.type = type;return this;
  }
  
  public static enum WrapperType
  {
    TOP,  BOTTOM, CENTRAL;
  }
  
  public Wrapper(String id, PlayerScoreboard playerScoreboard, WrapperType type)
  {
    super(id, playerScoreboard);
    
    this.type = type;
  }
}
