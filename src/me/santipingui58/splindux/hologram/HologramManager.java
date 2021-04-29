package me.santipingui58.splindux.hologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.SpleefRankingPeriod;
import me.santipingui58.splindux.stats.SpleefRankingType;
import me.santipingui58.splindux.utils.Utils;

public class HologramManager {

	private static HologramManager manager;	
	 public static HologramManager getManager() {
	        if (manager == null)
	        	manager = new HologramManager();
	        return manager;
	    }
	
	 private List<Hologram> holograms = new ArrayList<Hologram>();
	 private HashMap<SpleefPlayer,Boolean> oldHasRewards = new HashMap<SpleefPlayer,Boolean>();
	 public List<Hologram> getHolograms() {
		 return this.holograms;
	 }
	 
	 public void deleteHologram(SpleefPlayer sp) {
		 Hologram hologram = null;
		 for (Hologram h : this.holograms) {
			 if (h.getLocation().getWorld().equals(sp.getPlayer().getLocation().getWorld())) {
				 if (h.getLocation().distance(sp.getPlayer().getLocation())<=3) {
					 hologram = h;
					 break;
				 }
			 }
		 }
		 
		 if (hologram!=null) {
			 for (SpleefPlayer spl : DataManager.getManager().getPlayers()) {
			 hologram.delete(spl);
			 Main.arenas.getConfig().set("holograms."+hologram.getUUID().toString(), null);
			 }		 
			 this.holograms.remove(hologram);
			 
		 } else {
			 sp.getPlayer().sendMessage("§cCouldnt find an hologram in 10 blocks.");
		 }
	 }
	 
	 public void createHologram(SpleefPlayer sp,HologramType type) {
		 Hologram hologram = new Hologram(UUID.randomUUID(), sp.getPlayer().getLocation(), type);
		 this.holograms.add(hologram);
		saveHolograms();	
		updateHolograms(false);
	 }
	 
	 public void loadHolograms() {
		 if (Main.arenas.getConfig().contains("holograms")) {
		 Set<String> holograms = Main.arenas.getConfig().getConfigurationSection("holograms").getKeys(false);
		for (String h : holograms) {
			UUID uuid = UUID.fromString(h);
			Location location = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("holograms."+h+".location"));
			HologramType type = HologramType.valueOf(Main.arenas.getConfig().getString("holograms."+h+".type"));
			if (type.equals(HologramType.SPLEEF_RANKED) || type.equals(HologramType.SPLEEF_FFA_RANKING)) continue;
			Hologram hologram = new Hologram(uuid, location, type);
			this.holograms.add(hologram);
		}
	 }
		 }
	 
	 public void saveHolograms() {
		 for (Hologram h : holograms) {
			 if (h.getLocation()!=null) {
			 Main.arenas.getConfig().set("holograms."+h.getUUID().toString()+".location", Utils.getUtils().setLoc(h.getLocation(), false));
			 }
			 Main.arenas.getConfig().set("holograms."+h.getUUID().toString()+".type", h.getType().toString());
		
		 }
			 Main.arenas.saveConfig();
	 }
	 
		 
	 
	 public void updateHolograms(boolean onlyVotes) {
		 for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			 if (sp.getOfflinePlayer().isOnline()) sendHolograms(sp,onlyVotes);
		 }
	 }
	 public void sendHolograms(SpleefPlayer sp,boolean onlyVotes) {
		 for (Hologram h : this.holograms) {
			 if (onlyVotes) {
				 if (!h.getType().equals(HologramType.VOTES)) continue;
				 if (!this.oldHasRewards.containsKey(sp)) {
					 this.oldHasRewards.put(sp, sp.hasUnclaimedRewards());
				 }	 
				 if (this.oldHasRewards.get(sp)!=sp.hasUnclaimedRewards()) {
					 this.oldHasRewards.put(sp, sp.hasUnclaimedRewards());
					 h.spawn(sp);
				 }			 
			 } else { 
			 h.spawn(sp);
		 }
			 }
	 }
	 
	 public void removeHolograms(SpleefPlayer sp) {
		 for (Hologram h : this.holograms) {
			 h.delete(sp);
		 }
	 }
	 
	 public void changeChangeType(SpleefPlayer sp,int id) {
		 try {
		 Hologram hologram = getHologramByID(id,sp);
		 
		 if (hologram.getChangeType().get(sp).equals(SpleefRankingType.WINS)) {
			 hologram.getChangeType().put(sp, SpleefRankingType.KILLS);
			 sp.getPlayer().sendMessage("§aChanged to: §bSpleefFFA KILLS");
		 } else  if (hologram.getChangeType().get(sp).equals(SpleefRankingType.KILLS)) {
			 hologram.getChangeType().put(sp, SpleefRankingType.GAMES);
			 sp.getPlayer().sendMessage("§aChanged to: §bSpleefFFA GAMES");
		 } else  if (hologram.getChangeType().get(sp).equals(SpleefRankingType.GAMES)) {
			 hologram.getChangeType().put(sp, SpleefRankingType.WINS);
			 sp.getPlayer().sendMessage("§aChanged to: §bSpleefFFA WINS");
		 } 
		 sendHolograms(sp,false);
		 } catch(Exception ex) {}
	 }
	 
	 
	 public void changeChangePeriod(SpleefPlayer sp,int id) {
		 try {
		 Hologram hologram = getHologramByID(id,sp);
		 
		 if (hologram.getChangePeriod().get(sp).equals(SpleefRankingPeriod.ALL_TIME)) {
			 hologram.getChangePeriod().put(sp, SpleefRankingPeriod.MONTHLY);
			 sp.getPlayer().sendMessage("§aChanged to: §bSpleefFFA MONTHLY");
		 } else  if (hologram.getChangePeriod().get(sp).equals(SpleefRankingPeriod.MONTHLY)) {
			 hologram.getChangePeriod().put(sp, SpleefRankingPeriod.WEEKLY);
			 sp.getPlayer().sendMessage("§aChanged to: §bSpleefFFA WEEKLY");
		 } else  if (hologram.getChangePeriod().get(sp).equals(SpleefRankingPeriod.WEEKLY)) {
			 hologram.getChangePeriod().put(sp, SpleefRankingPeriod.ALL_TIME);
			 sp.getPlayer().sendMessage("§aChanged to: §bSpleefFFA ALL TIME");
		 } 
		 sendHolograms(sp,false);
		 } catch (Exception ex) {}
	 }
	 
	 
	 
	 public Hologram getHologramByID(int id,SpleefPlayer sp) {
		 for (Hologram h : this.holograms) {
			 for (int i : h.getIDList().get(sp)) {
				 if (i==id) {
					 return h;
				 }
			 }
		 }
		 return null;
	 }
}
