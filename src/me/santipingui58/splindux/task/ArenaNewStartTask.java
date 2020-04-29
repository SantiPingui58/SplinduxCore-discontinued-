package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class ArenaNewStartTask {

	
	private SpleefArena arena;
	private int task;
	private int time;
	public ArenaNewStartTask(SpleefArena arena) {
		task();
		if (arena.isInEvent()) {
			this.time = 15;
		} else {
		this.time = 5;
		}
		this.arena = arena;
	}
	
	
	private void task() {
		
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
		          
               @Override
               public void run() {
            	   int size = 3;
            	   if (arena.getGameType().equals(GameType.DUEL)) {
            		   size = 2;
            	   }
            	   if (arena.getQueue().size()>=size) {
            		   
            	   if (time==0) {
                  arena.startGame();
               	Bukkit.getScheduler().cancelTask(task);
            	   } 
            	   time--;
            	   } else {
            		   arena.setState(GameState.LOBBY);
            			Bukkit.getScheduler().cancelTask(task);
            			for (SpleefPlayer p : arena.getQueue()) {
        					p.getPlayer().sendMessage("§cThere are not enough players to start! Countdown cancelled.");
        				}
            	   }
            	   
               
               }
		 }, 10, 20L);
		
	}
}
