package me.santipingui58.splindux.task.tasks;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import github.scarsz.discordsrv.dependencies.google.common.collect.Iterables;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;

public class OnlineTask {
	
	
	
	
	
	
	public OnlineTask() {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				
		    	EconomyManager.getManager().checkSplinboxes();
		    	for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
		    		if (sp.getOfflinePlayer().isOnline()) {
		    		sp.addOnlineTime();    		
		    	}
		    	}
		    	
		    	
		    	
		    	 ByteArrayDataOutput out = ByteStreams.newDataOutput();
		    	 out.writeUTF("PlayerCount");
		    	 out.writeUTF("thetowers");
		    	 
		    	 try {
		    	  Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		    	  
		    	  player.sendPluginMessage(Main.get(), "BungeeCord", out.toByteArray());
		    	 } catch(Exception ex) {}
		    	
		    	Date date = new Date();
		        if (date.getHours() == 6 && date.getMinutes() == 0) {
		        	if (date.getDay()==0) {
		        		
		        		DataManager.getManager().resetWeeklyStats();
		        		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #admin **Weekly reset done**");
		        		}
		        	
		            if (date.getDate()==1) {
		            	DataManager.getManager().resetMonthlyStats();    
		            	Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #admin **Monthly reset done**");
		            }
		                            
		            DataManager.getManager().giveMutationTokens();
		            //DataManager.getManager().giveRankeds();
		            DataManager.getManager().eloDecay();
		            
		            GuildsManager.getManager().pay();
		            	new BukkitRunnable() {
		            		public void run() {
		            			Bukkit.broadcastMessage("§b§lServer automatic restart in 1 minute!");
		            	 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		            	}
		            	}.runTaskLater(Main.get(), 20L*60);
		        }
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20*60L);
	}
}
