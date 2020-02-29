package me.santipingui58.splindux.stats.level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
	 
	 
	 public void checkLevel(SpleefPlayer sp) {
		 if (getRank(sp).getNextRank()!=null) {
		 if (sp.getLevel()>=getRank(sp).getNextRank().getRequiredLevel()) {
			 levelUp(sp);
		 }
		 }
	 }
	 
	 
	 public void addLevel(SpleefPlayer sp,int level) {
		 int oldlevel = sp.getLevel();
		 sp.setLevel(oldlevel+level);	
		 checkLevel(sp);
		 setExp(sp);
	 }
	 
	 public void setLevel(SpleefPlayer sp,int level) {
		 sp.setLevel(level);
		 setExp(sp);
	 }
	 
	 public void levelUp(SpleefPlayer sp) {
		 Player p = sp.getPlayer();
		 if (getRank(sp).getMainRank().equals(getRank(sp))) {
			 for (Player pa : Bukkit.getOnlinePlayers()) {
						 pa.sendMessage("§e§lSplin§b§ldux  §aCongratulations to §b"+ p.getName() +"§a for level up to " +getRank(sp).getRankName()+ "§a!");
					 
		 } 
			 
			 
			 new BukkitRunnable() {
					@Override
					public void run() {
			 	p.sendMessage("§d§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			    p.sendMessage("");
			    p.sendMessage("    §aCongratulations! You have leved up to " + getRank(sp).getRankName() +"§a!");
			    p.sendMessage("");
				p.sendMessage("§d§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					}
				}.runTaskLater(Main.get(), 5L);	
	 }
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
			  prev_level = srank.getPrevRank().getRequiredLevel(); 
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
