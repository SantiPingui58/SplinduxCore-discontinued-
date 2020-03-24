package me.santipingui58.splindux.task;

import java.util.Date;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.SpleefPlayer;


public class OnlineTimeTask {

	public OnlineTimeTask() {
		task();
	}
	
	private void task() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    @SuppressWarnings("deprecation")
			public void run() {	   
		    	for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
		    		sp.addOnlineTime();    		
		    	}
		    	EconomyManager.getManager().checkSplinboxes();
		    	Date date = new Date();
                if (date.getHours() == 0 && date.getMinutes() == 0) {
                	if (date.getDay()==0) {
                		DataManager.getManager().resetWeeklyStats();
                		}
                	
                	
	                if (date.getDate()==1){
	                	DataManager.getManager().resetMonthlyStats();
	                }
	                
                }
		    	
		    }
		    }, 0, 20L);
	}
}
