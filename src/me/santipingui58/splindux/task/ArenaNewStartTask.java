package me.santipingui58.splindux.task;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.Arena;

public class ArenaNewStartTask {

	
	private Arena arena;
	private int time;
	private FFAArena ffa;
	private Set<SpleefPlayer> queue;
	public ArenaNewStartTask(Arena arena) {
		ffa = GameManager.getManager().getFFAArenaByArena(arena);	
		this.queue = arena.getGameType().equals(GameType.DUEL) ? new HashSet<SpleefPlayer>(arena.getQueue()) : ffa.getQueue();
		task();
		if (ffa!=null && ffa.isInEvent()) {
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
            	   
            	   if (queue.size()>=size) {
            		   
            	   if (time==0) {
                 if (arena.getGameType().equals(GameType.DUEL)) {
                	 arena.startGameDuel(false);
                 } else {
                	 ffa.startGame();
                 }
               	cancel();
            	   } 
            	   time--;
            	   } else {
            		   arena.setState(GameState.LOBBY);
            			cancel();
            			GameManager.getManager().sendSyncMessage(queue, GameManager.getManager().getGamePrefix(arena)+"§cThere are not enough players to start! Countdown cancelled.");
            	   }
            	   
			}
		}.runTaskTimer(Main.get(), 0L, 20L);
		
	}
}
