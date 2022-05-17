package me.santipingui58.splindux.economy;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;


import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.sws.SWSManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class EconomyManager /*extends GEconomyProvider*/ {

	


//	public EconomyManager(Plugin plugin, String storage) {
	//	super(plugin, storage);
	//}

	
	
	private static EconomyManager pm;
	  public static EconomyManager getManager() {
	        if (pm == null)
	           // pm = new EconomyManager(Main.get(), "Splindux");
	        	pm = new EconomyManager();
	        return pm;
	    }


	
	  
	  public void checkSplinboxes() {
		  
		  for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			  if (!sp.getOfflinePlayer().isOnline()) continue;
			  if (sp.getSplinboxPoints()>=400) {
				  sp.resetSplinboxPoints();

				   int r = new Random().nextInt((100 - 1) + 1) + 1;
				  boolean luck = false;
				   
				  
				  if (sp.getPlayer().hasPermission("splindux.extreme")) {
					  if (r<40) {
						  luck =true;
					  }
				  } else if (sp.getPlayer().hasPermission("splindux.epic")) {
					  if (r<35) {
						  luck =true;
					  }
				  }else if (sp.getPlayer().hasPermission("splindux.vip")) {
					  if (r<30) {
						  luck =true;
					  }
				  }else {
					  if (r<25) {
						  luck =true;
					  } 
				  }
				  
				  SWSManager.getManager().addPoints(sp,50, true,true);
				  if(luck) {
					  		  
				  new BukkitRunnable() {
					  public void run() {
						  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ultracosmetics give key 1 " + sp.getOfflinePlayer().getName());
				 Bukkit.broadcastMessage("§fThe player §b" + sp.getName() + "§f has found a §bSplinKey §f!");
					  }
				  }.runTask(Main.get());
				  
				  }
			  }
			  
			  if (sp.getCoinsPoints()>=150) {
				  sp.resetCoinsPoints();
				  EconomyManager.getManager().addCoins(sp, 25, true,false);
			  }
			  
			  
		  }
	  }
	  
	  
	  public void addSplinboxes(SpleefPlayer sp, int amount, int level) {
		 // Player p = sp.getPlayer();
		 // MysteryBoxType type = MysteryBoxType.valueOf("NORMAL_MYSTERY_BOX_"+level);
		 // getPlayerManager(p).giveMysteryBoxes(type, null, false, null, amount);
	  }
	
	  public void addCoins(SpleefPlayer sp, int i,boolean multiplier,boolean found) {
	  

			if (multiplier) {
			if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.extreme")) {
				i = (int) ((i*1.5));
			} else if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.epic")) {
				i = (int) ((i*1.25));
			} else if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.vip")) {
				i = (int) ((i*1.1));
			} 
			}
			
			sp.addCoins(i);
			
			if (Bukkit.getOnlinePlayers().contains(sp.getOfflinePlayer())) {
				if (found) {
					sp.sendMessage("§eYou have found §6§l" + i + "&ecoins!");				
			} else {
				sp.sendMessage("§aYou have won §6"+i+" coins");
			}
			}
	  } 
		
		

	  
	 // public PlayerManager getPlayerManager(Player player) {
	//	    PlayerManager playerManager = GadgetsMenuAPI.getPlayerManager(player);
	//		return playerManager;
	//	}
	  
	  
	
}