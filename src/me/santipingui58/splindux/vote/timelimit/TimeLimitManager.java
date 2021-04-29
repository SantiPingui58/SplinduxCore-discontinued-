package me.santipingui58.splindux.vote.timelimit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
			 if (tl.getPlayer().getUniqueId().toString().equals(sp.getOfflinePlayer().getUniqueId().toString())) {
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
				 OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(Main.timelimit.getConfig().getString("timelimit."+u+".player")));
				 String[] args = null;
				 	if (Main.timelimit.getConfig().contains("timelimit."+u+".args"))  args =Main.timelimit.getConfig().getString("timelimit."+u+".args").split(",");
  				 TimeLimit tl = new TimeLimit(UUID.fromString(u),player,time,type,args);
				 this.timelimits.add(tl);
			 }
		 } 
		 
	 }
	 
	 public void saveTimeLimit() {
		 for (TimeLimit tl : this.timelimits) {
			 UUID uuid = tl.getUUID();
			 Main.timelimit.getConfig().set("timelimit."+uuid+".type" , tl.getType().toString());
			 Main.timelimit.getConfig().set("timelimit."+uuid+".time" , tl.getLeftMinutes());
			 Main.timelimit.getConfig().set("timelimit."+uuid+".player" , tl.getPlayer().getUniqueId().toString());
			 
			 String args = "";
			 if (tl.getArgs()!=null) {
				 for (String s : tl.getArgs()) {
				 if (args.equalsIgnoreCase("")) {
					 args = s;
				 } else {
				 args = args + ","+s;			 
			 }
				 }
				 Main.timelimit.getConfig().set("timelimit."+uuid+".args" , args);
			 }
			
			 
		 }
		 
		 Main.timelimit.saveConfig();
	 }
	 
	 
	 public boolean hasActiveTimeLimitBy(SpleefPlayer sp,TimeLimitType type) {
		 if (type==null)  return false;
		 

		 for (TimeLimit tl : getTimeLimitListByPlayer(sp)) {
			 if (tl.getType().equals(type)) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 
	 
	 public int getAmountOfActiveServerLists(SpleefPlayer sp) {
		 int amount = 0;
		 for (TimeLimit tl : getTimeLimitListByPlayer(sp)) {
			 if (tl.getType().isServerList()) amount++;
		 }
		 return amount;
	 }
	 
	 public List<TimeLimit> getTimeLimitBy(SpleefPlayer sp, TimeLimitType type) {
		 List<TimeLimit> list = new ArrayList<TimeLimit>(); 
		 for (TimeLimit tl : getTimeLimitListByPlayer(sp)) {
			 if (tl.getType().equals(type)) {
				list.add(tl);
			 }
		 }
		 return list;
	 }
	 
	 
	 public void time() {
		 List<TimeLimit> toRemove = new ArrayList<TimeLimit>(); 
		 for (TimeLimit tl : this.timelimits) {
			 tl.time();
			 if (tl.getLeftMinutes()<=0) {
				 toRemove.add(tl);
				 Main.timelimit.getConfig().set("timelimit."+tl.getUUID(), null);
				 Main.timelimit.saveConfig();
			 }
		 }
		 
		 
		 this.timelimits.removeAll(toRemove);
		 
		 
	 }
	 
	 
	 public void addTimeLimit(SpleefPlayer sp,int time,TimeLimitType type,String[]args) { 
		 TimeLimit tl = new TimeLimit(UUID.randomUUID(),sp.getOfflinePlayer(),time,type,args);
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


