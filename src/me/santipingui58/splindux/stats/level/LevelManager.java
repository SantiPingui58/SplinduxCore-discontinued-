package me.santipingui58.splindux.stats.level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class LevelManager {

	
	private static LevelManager manager;	
	 public static LevelManager getManager() {
	        if (manager == null)
	        	manager = new LevelManager();
	        return manager;
	    }
	
	 
	 public SpleefRank getRank(SpleefPlayer sp) {
		 SpleefRank rank = null;
		 for (SpleefRank sr : SpleefRank.values()) {
			 if (rank==null) {
				 rank = sr;
			 } else {
				 if (sr.getRequiredLevel()>rank.getRequiredLevel() && sp.getLevel()>=sr.getRequiredLevel()) {
					 rank = sr;
				 }
			 }
		 }
		 return rank;
	 }
	 
	 
	 public void checkLevel(SpleefPlayer sp, SpleefRank rank ) {
		 if (rank.getNextRank()!=null) {
		 if (sp.getLevel()>=rank.getNextRank().getRequiredLevel()) {
			 levelUp(sp);
			 DataManager.getManager().saveData(sp);
		 }
		 }
	 }
	 
	 
	 public void addLevel(SpleefPlayer sp,int level) {
		 int oldlevel = sp.getLevel();	 
		 SpleefRank oldrank = getRank(sp);
		 sp.setLevel(oldlevel+level);		
		 checkLevel(sp,oldrank);
		 if (sp.getOfflinePlayer().isOnline()) {
		 setExp(sp);
		 }
		 
		 DataManager.getManager().saveData(sp);
	 }
	 
	 
	 public void setLevel(SpleefPlayer sp,int level) {
		 sp.setLevel(level);
		 if (sp.getOfflinePlayer().isOnline()) {
		 setExp(sp);
		 }
		 
		 DataManager.getManager().saveData(sp);
	 }
	 
	 public void levelUp(SpleefPlayer sp) {
		 OfflinePlayer p = sp.getOfflinePlayer();
		 if (getRank(sp).getMainRank().equals(getRank(sp))) {
			 for (Player pa : Bukkit.getOnlinePlayers()) {
						 pa.sendMessage("§e§lSplin§b§ldux  §aCongratulations to §b"+ p.getName() +"§a for level up to " +getRank(sp).getRankName()+ "§a!");
					 
		 } 
		 }
			 
			 new BukkitRunnable() {
					@Override
					public void run() {
						if (p.isOnline()) {
			 	p.getPlayer().sendMessage("§d§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			    p.getPlayer().sendMessage("");
			    p.getPlayer().sendMessage("    §aCongratulations! You have leved up to " + getRank(sp).getRankName() +"§a!");
			    p.getPlayer().sendMessage("");
				p.getPlayer().sendMessage("§d§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					}
					}
				}.runTaskLater(Main.get(), 5L);	
	 
	 }
	 
	 
	 public void setExp(SpleefPlayer sp) {
		 Player p = sp.getPlayer();
		 SpleefRank srank = getRank(sp);
		  p.setExp(0);
		  p.setLevel(0);
		  int current_level = sp.getLevel();
		  
		  
		  int prev_level = 0; 

		  if (srank.getNextRank()==null) {
			  p.setLevel(50);
			  return;
		  }
		  if (srank.getPrevRank()==null) {
			  prev_level = 0; 
		  } else {
			  prev_level = srank.getRequiredLevel(); 
		  }
		  
		  
		  int next_level = srank.getNextRank().getRequiredLevel(); 
	
		  int piso = current_level - prev_level;
		  int max = next_level - prev_level;
		  
		  
		  double resultado = (double) piso/ (double) max;
		  
		  if (resultado >= 1 || resultado <= 0) {
			  resultado = 0;
		  }
		  
		  p.setExp((float) resultado);
		  p.setLevel(srank.getInt());
		  
		  
	  }
	 
	 
}
