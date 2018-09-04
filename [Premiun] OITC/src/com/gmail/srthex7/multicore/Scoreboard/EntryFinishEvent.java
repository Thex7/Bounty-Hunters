package com.gmail.srthex7.multicore.Scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntryFinishEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Entry entry;
    private PlayerScoreboard scoreboard;
    private Player player;

    public EntryFinishEvent(Entry entry, PlayerScoreboard scoreboard) {
        this.entry = entry;
        this.scoreboard = scoreboard;
        this.player = scoreboard.getPlayer();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Entry getEntry() {
        return this.entry;
    }

    public PlayerScoreboard getScoreboard() {
        return this.scoreboard;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public void setScoreboard(PlayerScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

