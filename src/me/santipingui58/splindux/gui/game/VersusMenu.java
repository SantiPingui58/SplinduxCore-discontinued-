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



public class VersusMenu extends MenuBuilder {
	
	
	public VersusMenu(SpleefPlayer sp) {
		super("§a§lVersus Menu",4);
		
		
		new BukkitRunnable() {
		public void run() {
			
		
		RankingEnum ranking_spleef =  RankingEnum.getRankingEnum(SpleefType.SPLEEF, GameType.DUEL, SpleefRankingPeriod.ALL_TIME, SpleefRankingType.WINS);
		RankingEnum ranking_splegg =RankingEnum.getRankingEnum(SpleefType.SPLEGG, GameType.DUEL, SpleefRankingPeriod.ALL_TIME, SpleefRankingType.WINS);
		RankingEnum ranking_tntrun = RankingEnum.getRankingEnum(SpleefType.TNTRUN, GameType.DUEL, SpleefRankingPeriod.ALL_TIME, SpleefRankingType.WINS);
		
		HashMap<UUID,Integer> top10Spleef = StatsManager.getManager().getRanking(ranking_spleef);
		HashMap<UUID,Integer> top10Splegg = StatsManager.getManager().getRanking(ranking_splegg);
		HashMap<UUID,Integer> top10TNTRun = StatsManager.getManager().getRanking(ranking_tntrun);
		
		List<String> spleef10 = StatsManager.getManager().getTop10(top10Spleef, 0, ranking_spleef,sp);
		List<String> splegg10 = StatsManager.getManager().getTop10(top10Splegg, 0, ranking_splegg,sp);
		List<String> tntrun10 = StatsManager.getManager().getTop10(top10TNTRun, 0, ranking_tntrun,sp);
		
		
		String s1 =	StatsManager.getManager().getAmountByPeriod(SpleefRankingPeriod.ALL_TIME);
		String s2 =	"Wins";
		
		
		int playing_spleef = GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL, 1, 0);
		int playing_splegg = GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL, 1, 0);	
		int playing_tntrun = GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL, 1, 0);
	
		
		
		s(11, new ItemBuilder(Material.TNT)
				.setTitle("§c§lTNTRun 1vs1")
				.setAmount(playing_tntrun == 0 ? 1 : playing_tntrun)
				.addLore("§7Playing: §a" + playing_tntrun)
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,1,0))
				.build());
		

		s(20, new ItemBuilder(Material.LADDER).setTitle("§aTNTRun 1vs1 Ranking")
				.addLore("§a"+s1+" - "+s2)
				.addLores(tntrun10)
				.build());
			
		s(13, new ItemBuilder(Material.DIAMOND_SPADE)
				.setTitle("§b§lSpleef 1vs1")
				.setAmount(playing_spleef == 0 ? 1 : playing_spleef)
				.addLore("§7Playing: §a" + playing_spleef)
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,1,0))
				.build());
		
		s(22, new ItemBuilder(Material.LADDER).setTitle("§aSpleef 1vs1 Ranking")
				.addLore("§a"+s1+" - "+s2)
				.addLores(spleef10)
				.build());
		
		s(15, new ItemBuilder(Material.EGG)
				.setTitle("§e§lSplegg 1vs1")
				.setAmount(playing_splegg == 0 ? 1 : playing_splegg)
				.addLore("§7Playing: §a" + playing_splegg)
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,1,0))
				.build());
		
		
		s(24, new ItemBuilder(Material.LADDER).setTitle("§aSplegg 1vs1 Ranking")
				.addLore("§a"+s1+" - "+s2)
				.addLores(splegg10)
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
		if (slot==11) {
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.TNTRUN, false, null);
			}
			}.runTaskAsynchronously(Main.get());
			p.closeInventory();
		} else if (slot==13) { 
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.SPLEEF, false, null);
		}
	}.runTaskAsynchronously(Main.get());
			p.closeInventory();
		} else if (slot==15) {
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.SPLEGG, false, null);
		}
	}.runTaskAsynchronously(Main.get());
			p.closeInventory();
		} 
		}
	
	
	}



