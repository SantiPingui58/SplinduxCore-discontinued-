package me.santipingui58.splindux.hologram;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.SpleefRankingPeriod;
import me.santipingui58.splindux.stats.SpleefRankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.utils.Utils;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class HologramManager {

	private static HologramManager manager;	
	 public static HologramManager getManager() {
	        if (manager == null)
	        	manager = new HologramManager();
	        return manager;
	    }
	
	 private HashMap <UUID,Set<Hologram>> staticHolograms = new HashMap<UUID,Set<Hologram>>();
	 
	 private RankingHologram game_stats;
	 private RankingHologram player_stats;
	 
	 public List<RankingHologram> getHolograms() {
		 RankingHologram[] holograms = {game_stats,player_stats};
 		 List<RankingHologram> s = Arrays.asList(holograms);
 		 return s;
	 }
	 

	 public void loadHolograms() {
		 if (Main.arenas.getConfig().contains("holograms")) {
			Location location1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("holograms.game_stats.location"));
			Location location2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("holograms.player_stats.location"));
			game_stats = new RankingHologram(location1,HologramType.GAME_STATS);
			 player_stats = new RankingHologram(location2,HologramType.PLAYER_STATS);
		
	 } else {
		 game_stats = new RankingHologram(new Location(Bukkit.getWorld("world"),0,0,0),HologramType.GAME_STATS);
		 player_stats = new RankingHologram(new Location(Bukkit.getWorld("world"),0,0,0),HologramType.PLAYER_STATS);
		 saveHolograms();
	 }
		 
		// createHologram(StaticRankingType.SPLEEF_ELO, new Location(Bukkit.getWorld("world"),0,144,0));
		 }
	 
	 public void saveHolograms() {
		 for (RankingHologram h : getHolograms()) 
			 Main.arenas.getConfig().set("holograms."+h.getType().toString().toLowerCase()+".location", Utils.getUtils().setLoc(h.getLocation(), false));
			 Main.arenas.saveConfig();
	 }
	 
		 
	 
	 public RankingHologram getHologram(HologramType type) {
		 if (type.equals(HologramType.GAME_STATS)) return this.game_stats;
		 return this.player_stats;
	 }
	 
	 public void moveHologram(SpleefPlayer sp, RankingHologram hologram) {
		 hologram.setLocation(sp.getLocation());
		 updateHolograms();
		 saveHolograms();
	 }
	 
	 public void updateHolograms() {
		 new BukkitRunnable() {
			 public void run() {
		 for (Player p : Bukkit.getOnlinePlayers()) {
			 for (RankingHologram h : getHolograms()) {
				 updateHologram(p.getUniqueId(),h);
				 }
		 }
			 }
	 }.runTaskAsynchronously(Main.get());
		 
	 }
	 
	 
	 
	 public void updateHologram(UUID sp, RankingHologram hologram) {
		 HologramViewer viewer = HologramViewer.getHologramViewer(sp, hologram.getType());
		 hologram.update(viewer);
	 }
	 
	 public void showHolograms(UUID sp) {
		 new BukkitRunnable() {
			 public void run() {
		 for (RankingHologram h : getHolograms()) {
			 HologramViewer viewer = HologramViewer.getHologramViewer(sp, h.getType());
			 h.update(viewer);
			 }
			 }
	 }.runTaskAsynchronously(Main.get());
	 }
	 
	 public void hideHolograms(UUID sp) {
		 for (RankingHologram h : getHolograms()) {
			 HologramViewer viewer = HologramViewer.getHologramViewer(sp, h.getType());
			 h.delete(viewer);
			 }
			 }
	 
	 
	 
	 
	 
	 
	 
	 public void primaryButton(HologramViewer viewer,RankingHologram hologram) {	 
		 HologramSubType type = null;
		 for (Map.Entry<HologramSubType,HologramSubType> entry : viewer.getHologramSubType().getPrevAndNext().entrySet()) {
			 type = entry.getValue();
			 break;
		 }		 
		viewer.setHologramSubType(type);
		if (type.equals(HologramSubType.DUELS))  {
			viewer.setSpleefRankingType(SpleefRankingType.ELO);
		} else if (type.equals(HologramSubType.FFA)) {
			viewer.setSpleefRankingType(SpleefRankingType.WINS);
		}
		updateHologram(viewer.getPlayer(),hologram);
	 }
	 
	 
	 public void secondaryButton(HologramViewer viewer,RankingHologram hologram) {	 
		 SpleefRankingPeriod type = null;	 
		 for (Map.Entry<SpleefRankingPeriod,SpleefRankingPeriod> entry : viewer.getPeriod().getPrevAndNext().entrySet()) {
			 type = entry.getValue();
			 break;
		 }		 
		viewer.setPeriod(type);
		updateHologram(viewer.getPlayer(),hologram);
	 }
	 
	 
	 public void cleanHologramCache(UUID sp) {
		 for (RankingHologram rh : this.getHolograms())  {
			 rh.getViewersCache().remove(sp);
		 }
	 }
	 
	 

	 public void deleteStaticHolograms(SpleefPlayer sp) {
		 for (Hologram h : staticHolograms.get(sp.getUUID())) {
			 h.delete();
			 Bukkit.broadcastMessage("a");
		 }
		 staticHolograms.get(sp.getUUID()).clear();
		
	 }
	 
	 public void addHologramTo(SpleefPlayer sp, Hologram hologram) {
		 	if (!staticHolograms.containsKey(sp.getUUID())) staticHolograms.put(sp.getUUID(), new HashSet<Hologram>());
		 	Set<Hologram> holograms = staticHolograms.get(sp.getUUID());
		 	holograms.add(hologram);
		 	 staticHolograms.put(sp.getUUID(), holograms);
		 	 Bukkit.broadcastMessage("b");
	 }
	 
	 public void createHologram(SpleefPlayer sp, StaticRankingType type,Location location) {
		 Hologram hologram = HologramsAPI.createHologram(Main.get(), location);
		 Bukkit.broadcastMessage("dsadsa");
		addHologramTo(sp,hologram);
	
		 VisibilityManager visibilityManager = hologram.getVisibilityManager();
		 visibilityManager.showTo(sp.getPlayer());
		 visibilityManager.setVisibleByDefault(false);
		 hologram.appendTextLine(sp.getName());
		
		 int i = 1;
		 if (type.equals(StaticRankingType.SPLEEF_ELO) || type.equals(StaticRankingType.YT_ELO)) {
			 LinkedHashMap<UUID,Integer> hashmap = new LinkedHashMap<UUID,Integer>(StatsManager.getManager().getRanking(RankingEnum.SPLEEF1VS1_ELO));
				LinkedHashMap<UUID, Integer> it = StatsManager.getManager().getTop20HashMap(hashmap, 0,  RankingEnum.SPLEEF1VS1_ELO);
				if (type.equals(StaticRankingType.YT_ELO)) {
					 hologram.appendTextLine("§b§lCLASIFICATORIAS - RANKING YOUTUBERS");
					 HashMap<UUID,Integer> toAdd = new HashMap<UUID,Integer>();
					 for (Entry<UUID, Integer> entry : it.entrySet()) {
						try {
							if (PermissionsEx.getUser(Bukkit.getOfflinePlayer(entry.getKey()).getName()).has("splindux.media")) toAdd.put(entry.getKey(), entry.getValue());
						} catch(Exception ex) {}
					 }
					 it.clear();
					 it.putAll(toAdd);
					 
				} else {
					 hologram.appendTextLine("§b§lCLASIFICATORIAS - RANKING");
				}
					for (Entry<UUID, Integer> entry : it.entrySet()) {
					    UUID key = entry.getKey();
					    int value = entry.getValue();	
					   String  prefix = "";
					    try {
					    	prefix = PermissionsEx.getUser(Bukkit.getOfflinePlayer(entry.getKey()).getName()).getPrefix();
					    } catch(Exception ex) {}

					   	 hologram.appendTextLine("§6"+i+" " +prefix +" §b"+Bukkit.getOfflinePlayer(key).getName()+" §8- §e" + value + " ELO");
					    i++;
				}
	
					
					
		 }
	 }
}
