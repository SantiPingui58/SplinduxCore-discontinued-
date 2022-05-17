package me.santipingui58.splindux.gui.game;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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



public class FFAMenu extends MenuBuilder {
	
	
	public FFAMenu(SpleefPlayer sp, SpleefRankingPeriod periodtype,SpleefRankingType type) {
		super("§f§lFFA Menu",5);
		
		
		if (type==null) {
			periodtype = SpleefRankingPeriod.WEEKLY;
			type = SpleefRankingType.WINS;
		}
	
		RankingEnum ranking_spleef = RankingEnum.getRankingEnum(SpleefType.SPLEEF, GameType.FFA, periodtype, type);
		RankingEnum ranking_splegg = RankingEnum.getRankingEnum(SpleefType.SPLEGG, GameType.FFA, periodtype, type);
		RankingEnum ranking_tntrun = RankingEnum.getRankingEnum(SpleefType.TNTRUN, GameType.FFA, periodtype, type);
		
		
		final SpleefRankingPeriod srp = periodtype;
		final SpleefRankingType srt = type;
		
		new BukkitRunnable() {
	
			public void run() {
		
		HashMap<UUID,Integer> top10Spleef = StatsManager.getManager().getRanking(ranking_spleef);
		HashMap<UUID,Integer> top10Splegg = StatsManager.getManager().getRanking(ranking_splegg);
		HashMap<UUID,Integer> top10TNTRun = StatsManager.getManager().getRanking(ranking_tntrun);
		
		List<String> spleef10 = StatsManager.getManager().getTop10(top10Spleef, 0, ranking_spleef,sp);
		List<String> splegg10 = StatsManager.getManager().getTop10(top10Splegg, 0, ranking_splegg,sp);
		List<String> tntrun10 = StatsManager.getManager().getTop10(top10TNTRun, 0, ranking_tntrun,sp);
		
		
		String s1 =	StatsManager.getManager().getAmountByPeriod(srp);
		String s2 =	StatsManager.getManager().getAmountByType(ranking_spleef);
		
		
		int playing_spleef = GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.FFA, 1);
		
		int playing_splegg = GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.FFA, 1);
		
		int playing_tntrun = GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.FFA, 1);
		
		
		s(11, new ItemBuilder(Material.TNT)
				.setTitle("§c§lTNTRun FFA")
				.setAmount(playing_tntrun == 0 ? 1 : playing_tntrun)
				.addLore("§7Playing: §a" + playing_tntrun)
				.build());
		

		s(29, new ItemBuilder(Material.LADDER).setTitle("§aTNTRun FFA Ranking")
				.addLore("§a"+s1+" - "+s2)
				.addLores(tntrun10)
				.build());
			
		s(13, new ItemBuilder(Material.DIAMOND_SPADE)
				.setTitle("§b§lSpleef FFA")
				.setAmount(playing_spleef == 0 ? 1 : playing_spleef)
				.addLore("§7Playing: §a" + playing_spleef)
				.build());
		
		s(31, new ItemBuilder(Material.LADDER).setTitle("§aSpleef FFA Ranking")
				.addLore("§a"+s1+" - "+s2)
				.addLores(spleef10)
				.build());
		
		s(15, new ItemBuilder(Material.EGG)
				.setTitle("§e§lSplegg FFA")
				.setAmount(playing_splegg == 0 ? 1 : playing_splegg)
				.addLore("§7Playing: §a" + playing_splegg)
				.build());
		
		
		s(33, new ItemBuilder(Material.LADDER).setTitle("§aSplegg FFA Ranking")
				.addLore("§a"+s1+" - "+s2)
				.addLores(splegg10)
				.build());
		
		s(39, new ItemBuilder(Material.EYE_OF_ENDER).setTitle("§dChange Period")	
				.addLore("§8"+srp.name())
				.addLore("§8"+srt.name())
				.build());
		
		s(41, new ItemBuilder(Material.EYE_OF_ENDER).setTitle("§dChange Type")	
				.addLore("§8"+srp.name())
				.addLore("§8"+srt.name())
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
		
		if (slot==11) {
			GameManager.getManager().addFFAQueue(sp, SpleefType.TNTRUN);
		} else if (slot==13) {
			GameManager.getManager().addFFAQueue(sp, SpleefType.SPLEEF);
		} else if (slot==15) {
			GameManager.getManager().addFFAQueue(sp, SpleefType.SPLEGG);
		} else if (slot==39) {
			SpleefRankingPeriod period = SpleefRankingPeriod.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
			SpleefRankingType type = SpleefRankingType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(1)));
			
			SpleefRankingPeriod newperiod = null;
			
			switch(period) {
			case ALL_TIME:newperiod = SpleefRankingPeriod.WEEKLY;
				break;
			case MONTHLY:
				newperiod = SpleefRankingPeriod.ALL_TIME;
				break;
			case WEEKLY:
				newperiod = SpleefRankingPeriod.MONTHLY;
				break;
			default:break;
			
			}		
			new FFAMenu(sp, newperiod, type).o(sp.getPlayer());
		}else if (slot==41) {
			SpleefRankingPeriod period = SpleefRankingPeriod.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
			SpleefRankingType type = SpleefRankingType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(1)));
			
			SpleefRankingType newtype = null;
			
			switch(type) {
			case ELO:
				break;
			case GAMES:
				newtype = SpleefRankingType.KILLS;
				break;
			case KILLS:
				newtype = SpleefRankingType.WINS;
				break;
			case WINS:
				newtype = SpleefRankingType.GAMES;
				break;
			default:
				break;
			
			}		
			new FFAMenu(sp, period, newtype).o(sp.getPlayer());
		}
		
		
		}
	
	
	}



