package me.santipingui58.splindux.task;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefType;
import net.md_5.bungee.api.ChatColor;

public class ArenaStartingCountdownTask {

	
	private SpleefArena arena;
	private int time;
	private int task;
	private boolean keepDeadPlayers;
	public ArenaStartingCountdownTask(SpleefArena arena,boolean keepDeadPlayers) {
		this.arena = arena;
		this.time = 5;
		this.keepDeadPlayers = keepDeadPlayers;
		task();
	}
	
	private void task() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
			public void run() {
				if (!keepDeadPlayers) {
					GameMode mode = GameMode.SPECTATOR;
					if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
						mode = GameMode.SURVIVAL;
					} else if (arena.getSpleefType().equals(SpleefType.SPLEGG)) {
						mode = GameMode.ADVENTURE;
					}
				for (SpleefPlayer sp : arena.getDeadPlayers1()) sp.getPlayer().setGameMode(mode);
				for (SpleefPlayer sp : arena.getDeadPlayers2()) sp.getPlayer().setGameMode(mode);
			
				arena.getDeadPlayers1().clear();
				arena.getDeadPlayers2().clear();
				}
    		

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
