package me.santipingui58.splindux.timelimit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;


import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class TimeLimitManager {
	private static TimeLimitManager manager;	
	 public static TimeLimitManager getManager() {
	        if (manager == null)
	        	manager = new TimeLimitManager();
	        return manager;
	    }
	 
	 private List<TimeLimit> timelimits = new ArrayList<TimeLimit>();
	 
	 
	 public List<TimeLimit> getTimeLimitList() {
		 return this.timelimits;
	 }
	 
	 public List<TimeLimit> getTimeLimitListByPlayer(SpleefPlayer sp) {
		 
		 List<TimeLimit> list = new ArrayList<TimeLimit>();
		 for (TimeLimit tl : this.timelimits) {
			 if (tl.getPlayer().equals(sp)) {
				 list.add(tl);
			 }
		 }
		 
		 return list;
	 }
 	 
	 
	 
	 public void loadTimeLimit() {
		 if (Main.timelimit.getConfig().contains("timelimit")) {
			 Set<String> uuid = Main.timelimit.getConfig().getConfigurationSection("timelimit").getKeys(false);
			 for (String u : uuid) {
				 TimeLimitType type = TimeLimitType.valueOf(Main.timelimit.getConfig().getString("timelimit."+u+".type"));
				 int time = Main.timelimit.getConfig().getInt("timelimit."+u+".time");
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(Bukkit.getOfflinePlayer(UUID.fromString(Main.timelimit.getConfig().getString("timelimit."+u+".player"))));
				 TimeLimit tl = new TimeLimit(UUID.fromString(u),sp,time,type);
				 this.timelimits.add(tl);
			 }
		 } 
		 
	 }
	 
	 public void saveTimeLimit() {
		 for (TimeLimit tl : this.timelimits) {
			 UUID uuid = tl.getUUID();
			 Main.timelimit.getConfig().set("timelimit."+uuid+".type" , tl.getType().toString());
			 Main.timelimit.getConfig().set("timelimit."+uuid+".time" , tl.getLeftMinutes());
			 Main.timelimit.getConfig().set("timelimit."+uuid+".player" , tl.getPlayer().getOfflinePlayer().getUniqueId().toString());
		 }
		 
		 Main.timelimit.saveConfig();
	 }
	 
	 
	 public boolean hasActiveTimeLimitBy(SpleefPlayer sp,TimeLimitType type) {
		 for (TimeLimit tl : getTimeLimitListByPlayer(sp)) {
			 if (tl.getType().equals(type)) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public TimeLimit getTimeLimitBy(SpleefPlayer sp, TimeLimitType type) {
		 for (TimeLimit tl : getTimeLimitListByPlayer(sp)) {
			 if (tl.getType().equals(type)) {
				 return tl;
			 }
		 }
		 return null;
	 }
	 
	 
	 public void time() {
		 for (TimeLimit tl : this.timelimits) {
			 tl.time();
			 if (tl.getLeftMinutes()<=0) {
				 this.timelimits.remove(tl);
				 Main.timelimit.getConfig().set("timelimit."+tl.getUUID(), null);
				 Main.timelimit.saveConfig();
			 }
		 }
	 }
	 
	 
	 public void addTimeLimit(SpleefPlayer sp,int time,TimeLimitType type) {
		 if (hasActiveTimeLimitBy(sp,type)) {
			 removeTimeLimit(sp, type);
		 }
		 
		 TimeLimit tl = new TimeLimit(UUID.randomUUID(),sp,time,type);
		 this.timelimits.add(tl);
	 }
	
	 
	 public void removeTimeLimit(SpleefPlayer sp, TimeLimitType type) {
		 List<TimeLimit> toRemove = new ArrayList<TimeLimit>();
		 for (TimeLimit tl : getTimeLimitListByPlayer(sp)) {
			 if (tl.getType().equals(type)) {
			 toRemove.add(tl);
			 }
		 }
		 this.timelimits.removeAll(toRemove);
	 }
	 
	 
}


