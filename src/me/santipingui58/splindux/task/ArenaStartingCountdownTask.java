package me.santipingui58.splindux.task;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class ArenaStartingCountdownTask {
 
	
	private Arena arena;
	private int time;
	private int task;
	public ArenaStartingCountdownTask(Arena arena) {
		this.arena = arena;
		this.time = 3;
		arena.setState(GameState.STARTING);
		task();

	}
	
	private void task() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
			
			public void run() {
				String title = "";
				float d = 0.85F;
				switch(time) {
				case 3: title = ChatColor.AQUA+"READY!"; break;
				case 2: title = ChatColor.AQUA+"SET!"; break;
				case 1: title = ChatColor.AQUA+"GO!!"; d=0.95F;break;
				}	
				
				for (SpleefPlayer sp : arena.getViewers()) {
					Player p = sp.getPlayer();
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, d);
					sp.getPlayer().sendMessage(title);
	    			Utils.getUtils().sendTitles(sp,"", title, 5, 20, 5);
				}

		    	time = time-1;
		    	if (time<=0) {	    		
		    		arena.setState(GameState.GAME);
		    		for (SpleefPlayer sp : arena.getPlayers()) {
		    			if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
		    				sp.giveShovel();		    		
		    			sp.getPlayer().setAllowFlight(false);
		    			sp.getPlayer().setFlying(false);
		    			}
		    		}
		    		Bukkit.getScheduler().cancelTask(task);
		    	}
		    
		    }
		    }, 0, arena.getArenaStartingCountdownDelay());
	}
}
