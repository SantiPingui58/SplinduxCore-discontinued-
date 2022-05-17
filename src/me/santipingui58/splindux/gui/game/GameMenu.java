package me.santipingui58.splindux.gui.game;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.SpleefRankingPeriod;
import me.santipingui58.splindux.stats.SpleefRankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.utils.ItemBuilder;



public class GameMenu extends MenuBuilder {
	
	private SpleefType spleefType;
	
	public GameMenu(SpleefPlayer sp,SpleefType spleefType) {
		super( spleefType.getName() + " Menu",6);
		this.spleefType = spleefType;
		new BukkitRunnable() {
		public void run() {
			
		String spleefName = spleefType.getName();
		s(13, new ItemBuilder(spleefType.getMaterial(true)).setTitle(spleefName + " FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(spleefType, GameType.FFA,1))
				.build());
		
		s(20,new ItemBuilder(spleefType.getMaterial(false)).setTitle( spleefName + " 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(spleefType, GameType.DUEL,1))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(spleefType, GameType.DUEL,1))
			
				.build());
		s(22,new ItemBuilder(spleefType.getMaterial(false)).setTitle(spleefName +" 2v2")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(spleefType, GameType.DUEL,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(spleefType, GameType.DUEL,2))
			
				.build());
		s(24,new ItemBuilder(spleefType.getMaterial(false)).setTitle(spleefName +" 3v3")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(spleefType, GameType.DUEL,3))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(spleefType, GameType.DUEL,3))
				.build());
		
		
		RankingEnum ranking = RankingEnum.getRankingEnum(spleefType, GameType.FFA, SpleefRankingPeriod.WEEKLY, SpleefRankingType.WINS);
		HashMap<UUID,Integer> top10 = StatsManager.getManager().getRanking(ranking);
		List<String> names10 = StatsManager.getManager().getTop10(top10, 0, ranking,sp);
		
		RankingEnum ranking_spleef =  RankingEnum.getRankingEnum(spleefType, GameType.DUEL, SpleefRankingPeriod.ALL_TIME, SpleefRankingType.ELO);
		HashMap<UUID,Integer> top10Spleef = StatsManager.getManager().getRanking(ranking_spleef);
		List<String> spleef10 = StatsManager.getManager().getTop10(top10Spleef, 0, ranking_spleef,sp);
		

		s(41, new ItemBuilder(Material.LADDER).setTitle(spleefName + " FFA Ranking - Weekly")
				.addLores(names10)
				.build());
		
		
		s(39, new ItemBuilder(Material.LADDER).setTitle( spleefName + " 1vs1 Ranking")
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
		Player p = sp.getPlayer();
		sp.leave(false, false);
		new BukkitRunnable() {
			public void run() {
		if (slot==13) {
			
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addFFAQueue(sp,spleefType);
				}
			}.runTask(Main.get());
		} else if (slot==20) {
			GameManager.getManager().addDuelQueue(sp, 1, null, spleefType, false,null);
			p.closeInventory();
		} else if (slot==22) {
			GameManager.getManager().addDuelQueue(sp, 2, null, spleefType, false,null);
			p.closeInventory();
		} else if (slot==24) {
			GameManager.getManager().addDuelQueue(sp, 3, null, spleefType, false,null);
			p.closeInventory();
		}
			}
		}.runTaskLater(Main.get(),5L);
		}
	
	
	}



