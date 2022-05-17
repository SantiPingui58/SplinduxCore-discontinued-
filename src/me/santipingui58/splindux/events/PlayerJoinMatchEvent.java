package me.santipingui58.splindux.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class PlayerJoinMatchEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
	
    private SpleefPlayer spleefPlayer;
    private Arena arena;
    
    
    public PlayerJoinMatchEvent(SpleefPlayer spleefPlayer, Arena arena) {
    	this.spleefPlayer = spleefPlayer;
    	this.arena = arena;
    }
    
    
    public SpleefPlayer getPlayer() {
    	return this.spleefPlayer;
    }
    
    public Arena getArena() {
    	return this.arena;
    }
    
    
	@Override
	public HandlerList getHandlers() {
		return null;
	}
	
	 public static HandlerList getHandlerList() {
	        return HANDLERS;
	    }
	 
	
}
