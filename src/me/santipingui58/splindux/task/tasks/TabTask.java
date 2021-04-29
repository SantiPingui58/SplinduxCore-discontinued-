package me.santipingui58.splindux.task.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.hologram.HologramManager;

public class TabTask {
	private int holograms;
	
	public TabTask() {
		new BukkitRunnable() {
			public void run() {
		    	 
		    	   holograms++;
		    	   if (holograms>=40) {
		    		   holograms =0;
		    		   HologramManager.getManager().updateHolograms(false);
		    	   } else {
		    		   HologramManager.getManager().updateHolograms(true);
		    		   }	   
		    	 
			}
		}.runTaskTimer(Main.get(), 0L, 20*60L);
	}
}
