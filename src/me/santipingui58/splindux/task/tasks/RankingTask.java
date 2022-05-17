package me.santipingui58.splindux.task.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.hologram.HologramType;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.sws.SWSManager;

public class RankingTask {
	
	
	public RankingTask() {
				
				Main.get().getLogger().info("Saving player data...");
				new BukkitRunnable() {
					public void run() {
						int h = 0;
						for (HologramType type : HologramType.values()) h= h+ HologramManager.getManager().getHologram(type).getViewers().size();
						
						Bukkit.getLogger().info("Holograms loaded: " + h);
						
						StatsManager.getManager().updateRankings();
						GuildsManager.getManager().saveGuilds();
						FriendsManager.getManager().saveFriendships();
						HologramManager.getManager().updateHolograms();
						SWSManager.getManager().saveCountries();
						SWSManager.getManager().saveData();
						SWSManager.getManager().updateRankings();
						
						new BukkitRunnable() {
							public void run() {
		    	DataManager.getManager().getPlayers().forEach((sp) -> HikariAPI.getManager().saveData(sp));
		    	DataManager.getManager().getToUnloadSet().forEach((sp) -> {HikariAPI.getManager().saveData(sp); DataManager.getManager().getPlayers().remove(sp);});
						}
						}.runTaskLaterAsynchronously(Main.get(), 5L);
		    	DataManager.getManager().getToUnloadSet().clear();
		    	DataManager.getManager().unloadOfflinePlayers();
		    	Main.get().getLogger().info("Player data saved!");
			}
			
		}.runTaskTimerAsynchronously(Main.get(),20*60*10L, 20*60*10L);
	}
}
