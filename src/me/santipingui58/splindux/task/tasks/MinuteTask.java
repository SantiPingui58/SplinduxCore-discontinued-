package me.santipingui58.splindux.task.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.anouncements.AnnouncementManager;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;

public class MinuteTask {
	
	
	public MinuteTask() {
		new BukkitRunnable() {
			public void run() {
				TimeLimitManager.getManager().time();
				new BukkitRunnable() {
					public void run() {
			   	   AnnouncementManager.getManager().time();
					}
				}.runTask(Main.get());
			   	   RankedManager.getManager().updateQueues();
			   	  ParkourManager.getManager().calculateProbabilities();
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20*60L);
	}
}
