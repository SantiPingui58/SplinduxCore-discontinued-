package me.santipingui58.splindux.task;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
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
	private List<ArmorStand> armorstands;
	public ArenaStartingCountdownTask(Arena arena,List<ArmorStand> armorstands) {
		this.arena = arena;
		this.time = 3;
		this.armorstands = armorstands;
		arena.setState(GameState.STARTING);
		task();

	}
	
	private void task() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			public void run() {
				if (time==3) {
					if (arena.getDecayTask()!=null) arena.getDecayTask().orderLocations();
				}
				
				
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
		    		
		    		if (armorstands!=null) {
		    			for (ArmorStand stand : armorstands) {
		    				stand.remove();
		    			}
		    		}
		    		
		    		Bukkit.getScheduler().cancelTask(task);
		    	}
		    
		    }
		    }, 2L, arena.getArenaStartingCountdownDelay());
	}
}
