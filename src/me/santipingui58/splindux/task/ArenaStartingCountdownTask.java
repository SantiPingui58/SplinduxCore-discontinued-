package me.santipingui58.splindux.task;


import org.bukkit.Bukkit;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import net.md_5.bungee.api.ChatColor;

public class ArenaStartingCountdownTask {

	
	private SpleefArena arena;
	private int time;
	private int task;
	public ArenaStartingCountdownTask(SpleefArena arena) {
		this.arena = arena;
		this.time = 5;
		task();
	}
	
	private void task() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
			public void run() {
				
		    	if (time==3) {
		    		for (SpleefPlayer sp : arena.getViewers()) {
		    			sp.getPlayer().sendMessage(ChatColor.AQUA+"READY!");
		    		}
		    	} else if (time==2) {
		    		for (SpleefPlayer sp : arena.getViewers()) {
		    			sp.getPlayer().sendMessage(ChatColor.AQUA+"SET!");
		    		}
		    	} else if (time==1) {
		    		for (SpleefPlayer sp : arena.getViewers()) {
		    			sp.getPlayer().sendMessage(ChatColor.AQUA+"GOOOOOOOO!");
		    		}
		    	}
		    	time = time-1;
		    	if (time<=0) {	    		
		    		
		    		arena.setState(GameState.GAME);
		    		Bukkit.getScheduler().cancelTask(task);
		    	}
		    
		    }
		    }, 10, 20L);
	}
}
