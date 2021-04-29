package me.santipingui58.splindux.task;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.FFAEvent;

public class FFAEventFinishTask {
 
	private FFAEvent event;
	private int pos;
	private int task;
	private String winner;
	public FFAEventFinishTask(FFAEvent event) {
		this.event = event;
		pos = event.getTotalPoints().size();
		task();
		
	}
	
	private void task() {
		String bc1 = "§d§lThe Event has finished!";
		String bc2 ="§5-=-=-=-[§d§lFFA Event Positions§5]-=-=-=-";
		DataManager.getManager().broadcast(bc1,true);
		DataManager.getManager().broadcast(bc2,true);
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
			public void run() {		
				
				List<UUID> keyList = new ArrayList<UUID>();	
				keyList.addAll(event.getTotalPoints().keySet());
				if (pos<=0) {
					Bukkit.getScheduler().cancelTask(task);
				}
						UUID uuid = keyList.get(pos-1);
						int points = event.getTotalPoints().get(uuid);
						
						String bc3 = "§6§l" +pos+". §b§l" + Bukkit.getOfflinePlayer(uuid).getName() + "§8§l: §e§l" + points +" Points";
						 DataManager.getManager().broadcast(bc3,true);	
								if (pos==1) {
									winner = Bukkit.getOfflinePlayer(uuid).getName();
									String bc2 ="§5-=-=-=-[§d§lFFA Event Positions§5]-=-=-=-";
									 DataManager.getManager().broadcast(bc2,true);	
								} 
																
								pos--;
								
								if (winner!=null) {
									Bukkit.getScheduler().cancelTask(task);
									DataManager.getManager().broadcast("§d§lCongratulations to §b§l" + winner + " §d§l for winning the FFA Event!",true);
									event.givePrizes();
							
								}
		    }
		    }, 0, 100L);
		
		
	
	}
	
	
}
