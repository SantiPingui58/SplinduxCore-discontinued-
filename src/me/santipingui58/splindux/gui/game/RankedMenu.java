package me.santipingui58.splindux.gui.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.SpleefRankingPeriod;
import me.santipingui58.splindux.stats.SpleefRankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.utils.ItemBuilder;



public class RankedMenu extends MenuBuilder {
	
	
	public RankedMenu(SpleefPlayer sp) {
		super("§6§lRanked Spleef",3);
		
		new BukkitRunnable() {
		public void run() {
			
			RankingEnum ranking_spleef =  RankingEnum.getRankingEnum(SpleefType.SPLEEF, GameType.DUEL, SpleefRankingPeriod.ALL_TIME, SpleefRankingType.ELO);
			
			HashMap<UUID,Integer> top10Spleef = StatsManager.getManager().getRanking(ranking_spleef);
			
			List<String> spleef10 = StatsManager.getManager().getTop10(top10Spleef, 0, ranking_spleef,sp);
			
			s(15, new ItemBuilder(Material.EGG).setTitle("§cRanked Splegg 1v1")
					.addLore("§7Coming soon")
					.build());
			
			
		s(13, new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§b§lRanked Spleef 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,1,1))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,1,1))
				.build());
		

		

		s(11, new ItemBuilder(Material.TNT).setTitle("§cRanked TNTRun 1v1")
				.addLore("§7Coming soon")
				.build());
		
		
		
		s(22, new ItemBuilder(Material.LADDER).setTitle("§aSpleef Ranked Ranking")
				.addLores(spleef10)
				.build());
		
		
		
		
		new BukkitRunnable() {
		public void run() {
			buildInventory();
		}
		}.runTask(Main.get());
		
		}
		}.runTaskAsynchronously(Main.get());
	}
	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		if (slot==13) {
			List<SpleefPlayer> list= new ArrayList<SpleefPlayer>();
			list.add(sp);
			new BukkitRunnable() {
				public void run() {
			RankedManager.getManager().getRankedQueue(SpleefType.SPLEEF,1).joinQueue(list);		
				}
			}.runTaskAsynchronously(Main.get());
			sp.getPlayer().closeInventory();
		} else {
			sp.getPlayer().closeInventory();
			sp.getPlayer().sendMessage("§cComing soon...");
		}
	}
	
	
	}



