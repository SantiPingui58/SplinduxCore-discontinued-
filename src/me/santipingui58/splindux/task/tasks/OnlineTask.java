package me.santipingui58.splindux.task.tasks;

import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;

public class OnlineTask {
	
	
	
	
	
	
	public OnlineTask() {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
		    	for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
		    		if (sp.getOfflinePlayer().isOnline()) {
		    		sp.addOnlineTime();    		
		    	}
		    	}
		    	
		    	//EconomyManager.getManager().checkSplinboxes();
		    	Date date = new Date();
		        if (date.getHours() == 6 && date.getMinutes() == 0) {
		        	if (date.getDay()==0) {
		        		DataManager.getManager().resetWeeklyStats();
		        		}
		            if (date.getDate()==1){
		            	DataManager.getManager().resetMonthlyStats();                	
		            }
		                            
		            DataManager.getManager().giveMutationTokens();
		            DataManager.getManager().giveRankeds();
		            
		            GuildsManager.getManager().pay();
		            
		        }
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20*60L);
	}
}
