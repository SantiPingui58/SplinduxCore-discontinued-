package me.santipingui58.splindux.task;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;

public class ArenaNewStartTask {

	
	private Arena arena;
	private int time;
	public ArenaNewStartTask(Arena arena) {
		task();
		if (arena.isInEvent()) {
			this.time = 10;
		} else {
		this.time = 5;
		}
		this.arena = arena;
	}
	
	
	private void task() {
		
		
		new BukkitRunnable() {
			public void run() {
				   int size = 3;
            	   if (arena.getGameType().equals(GameType.DUEL)) {
            		   size = 2;
            	   }
            	   if (arena.getQueue().size()>=size) {
            		   
            	   if (time==0) {
                  arena.startGame(false);
               	cancel();
            	   } 
            	   time--;
            	   } else {
            		   arena.setState(GameState.LOBBY);
            			cancel();
            			GameManager.getManager().sendSyncMessage(arena.getQueue(), GameManager.getManager().getGamePrefix(arena)+"§cThere are not enough players to start! Countdown cancelled.");
            	   }
            	   
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20L);
		
	}
}
