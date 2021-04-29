package me.santipingui58.splindux.task.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.StatsManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;

public class RankingTask {
	
	
	public RankingTask() {
				
				Main.get().getLogger().info("Saving player data...");
				new BukkitRunnable() {
					public void run() {
						StatsManager.getManager().updateRankings();
						GuildsManager.getManager().saveGuilds();
						
						FriendsManager.getManager().saveFriendships();
						new BukkitRunnable() {
							public void run() {
		    	DataManager.getManager().getPlayers().forEach((sp) -> HikariAPI.getManager().saveData(sp));
		    	DataManager.getManager().getToUnloadSet().forEach((sp) -> {HikariAPI.getManager().saveData(sp); DataManager.getManager().getPlayers().remove(sp);});
						}
						}.runTaskLaterAsynchronously(Main.get(), 5L);
		    	DataManager.getManager().getToUnloadSet().clear();
		    	DataManager.getManager().unloadOfflinePlayers();
		    	Main.get().getLogger().info("Player data saved!");
				
				
		    	String ffa = "";
		    	String ranked = "";
		    	
		    	 StatsManager sm = StatsManager.getManager();
		    	 ffa = sm.getTop10Names(sm.getRanking(RankingEnum.SPLEEFFFA_WINS_WEEKLY), 0,RankingEnum.SPLEEFFFA_WINS_WEEKLY).get(0);
		    	 ranked = sm.getTop10Names(sm.getRanking(RankingEnum.SPLEEF1VS1_ELO), 0,RankingEnum.SPLEEF1VS1_ELO).get(0);
		    	NPC npc = CitizensAPI.getNPCRegistry().getById(173);
		    	SkinnableEntity entity = (SkinnableEntity) npc.getEntity();
		    	try {
		    	if (ffa!=null && !ffa.equalsIgnoreCase("")) {
		    	entity.setSkinName(ffa);
		    } else {
		    	entity.setSkinName("SantiPingui58");
		    }
		    	} catch(Exception ex) {}
		    	 
		    	    	 npc = CitizensAPI.getNPCRegistry().getById(0);
		    	    	 entity = (SkinnableEntity) npc.getEntity();
		    	    	 try {
		    	    	if (ranked!=null && !ranked.equalsIgnoreCase("")) {
		    	    	entity.setSkinName(ranked);
		    	    } else {
		    	    	entity.setSkinName("SantiPingui58");
		    	    }
		    	    	 } catch(Exception ex) {}
		       	
			}
			
		}.runTaskTimerAsynchronously(Main.get(),20*60*10L, 20*60*10L);
	}
}
