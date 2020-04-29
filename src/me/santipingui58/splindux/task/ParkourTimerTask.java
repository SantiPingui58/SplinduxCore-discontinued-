package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class ParkourTimerTask {

	public ParkourTimerTask() {
		task();
	}
	
	private void task() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    public void run() {
		    	for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
		    		if (sp.isInParkour()) {
		    			sp.addParkourTimer();
		    		}
		    	}
		    }
		    }, 0, 10L);
	}
}
