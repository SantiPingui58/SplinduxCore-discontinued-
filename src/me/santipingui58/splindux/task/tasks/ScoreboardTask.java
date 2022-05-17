package me.santipingui58.splindux.task.tasks;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class ScoreboardTask {
	
	
	private int timer;
	
	public ScoreboardTask() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
					if (sp==null) continue;
					if (sp.isInArena() || sp.isSpectating()) {
						sp.updateScoreboard();
					}
					
					if (sp.getParkourPlayer()!=null && sp.getParkourPlayer().getArena()!=null) {
						sp.updateScoreboard();
					}
				} 
								
					timer++;
					
					if (timer>=15) {
						timer=0;
						for (Player p : Bukkit.getOnlinePlayers()) {
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
							if (sp==null) continue;
							if (!sp.getOfflinePlayer().isOnline()) continue;
								sp.updateScoreboard();				
						} 
					}
				
				
			}
		}.runTaskTimer(Main.get(), 0L, 10L);
	}
}
