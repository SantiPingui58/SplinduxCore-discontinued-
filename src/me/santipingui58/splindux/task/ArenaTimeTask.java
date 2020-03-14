package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
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
		    			if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
		    				arena.addTotalTime();
		    			}
		    			
		    			arena.time();
		    		}

		    		
		    		
		    		
		    		
		    		if (arena.getTime()==0) {
		    			if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
		    			GameManager.getManager().endGameFFA(arena,GameEndReason.TIME_OUT);
		    			} else if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
		    				GameManager.getManager().timeReset1vs1(arena);
		    			}
		    		}
		    	}
		    }
		    }, 20, 20L);
	}
}
