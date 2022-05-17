package me.santipingui58.splindux.task.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.ranked.RankedManager;

public class RankedTask {
	
	
	public RankedTask() {
				
				new BukkitRunnable() {
					public void run() {
						RankedManager.getManager().getSpleefQueue().sendDuels();
						RankedManager.getManager().getSpleggQueue().sendDuels();
						RankedManager.getManager().getTNTRunQueue().sendDuels();
			}
			
		}.runTaskTimerAsynchronously(Main.get(),0L, 20L*15);
	}
}
