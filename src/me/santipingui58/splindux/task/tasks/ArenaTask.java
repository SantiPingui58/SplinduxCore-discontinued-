package me.santipingui58.splindux.task.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefType;

public class ArenaTask {
	
	
	private int update;
	public ArenaTask() {
		new BukkitRunnable() {
			public void run() {
				
				if (update>=30) {
					update=0;
 				   RankedManager.getManager().updateQueues();
				} else {
					update++;
				}
				
			 for (Arena arena : DataManager.getManager().getArenas()) {
		    		if (arena.getState().equals(GameState.GAME)) {	
		    			if (arena.getGameType().equals(GameType.DUEL)) {
		    				arena.addTotalTime();
		    			}
		    			if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
		    				arena.time();
		    				
		    				if (arena.getGameType().equals(GameType.DUEL)) {
		    					if (arena.getTime()==10) {
		    						GameManager.getManager().sendSyncMessage(arena.getViewers(), "§bAutomatic reset of the arena in 10 seconds.");
		    					} else if (arena.getTime()==3 || arena.getTime()==2 || arena.getTime()==1) {
		    						GameManager.getManager().sendSyncMessage(arena.getViewers(), "§bAutomatic reset of the arena in " +arena.getTime() +"!");
		    					}		
		    				} else if (arena.getGameType().equals(GameType.FFA) && arena.getSpleefType().equals(SpleefType.SPLEEF)) {
		    					if (arena.getTime()==35 || arena.getTime()==95) {
		    						GameManager.getManager().sendSyncMessage(arena.getViewers(), "§bAutomatic regeneration of the arena in 5 seconds.");
		    					}
		    					if (arena.getTime()==30 || arena.getTime()==90) {
		    						arena.ice();
		    					}
		    				}
		    				
		    				
		    			}
		    			
		    		}

		    		if (arena.getTime()==0) {
		    			if (arena.getGameType().equals(GameType.FFA)) {
		    			GameManager.getManager().endGameFFA(GameEndReason.TIME_OUT,arena.getSpleefType());
		    			} else if (arena.getGameType().equals(GameType.DUEL)) {
		    				GameManager.getManager().timeReset1vs1(arena);
		    			}
		    		}
		    	}
			 
			 
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20L);
	}
}
