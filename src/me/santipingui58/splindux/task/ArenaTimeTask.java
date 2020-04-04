package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefType;


public class ArenaTimeTask {

	public ArenaTimeTask() {
		task();
	}
	
	private void task() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    public void run() {
		    	for (SpleefArena arena : DataManager.getManager().getArenas()) {
		    		if (arena.getState().equals(GameState.GAME)) {	
		    			if (arena.getGameType().equals(GameType.DUEL)) {
		    				arena.addTotalTime();
		    			}
		    			if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
		    				arena.time();
		    				if (arena.getGameType().equals(GameType.DUEL)) {
		    					if (arena.getTime()==10) {
		    						for (SpleefPlayer sp : arena.getViewers()) sp.getPlayer().sendMessage("§bAutomatic reset of the arena in 10 seconds.");
		    					} else if (arena.getTime()==3 || arena.getTime()==2 || arena.getTime()==1) {
		    						for (SpleefPlayer sp : arena.getViewers()) sp.getPlayer().sendMessage("§bAutomatic reset of the arena in " +arena.getTime() +"!");
		    					}
		    				}
		    			}
		    			
		    		}

		    		if (arena.getTime()==0) {
		    			if (arena.getGameType().equals(GameType.FFA)) {
		    			GameManager.getManager().endGameFFA(arena,GameEndReason.TIME_OUT);
		    			} else if (arena.getGameType().equals(GameType.DUEL)) {
		    				GameManager.getManager().timeReset1vs1(arena);
		    			}
		    		}
		    	}
		    }
		    }, 20, 20L);
	}
}
