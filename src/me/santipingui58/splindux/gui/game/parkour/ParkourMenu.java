package me.santipingui58.splindux.gui.game.parkour;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.parkour.Level;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.parkour.ParkourMode;
import me.santipingui58.splindux.game.parkour.ParkourPlayer;
import me.santipingui58.splindux.game.parkour.probability.Probability;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.utils.ItemBuilder;

public class ParkourMenu extends MenuBuilder {

	
	public ParkourMenu(SpleefPlayer sp) {
		super("§2§lParkour",6);
		
		new BukkitRunnable() {
		public void run() {
			
		int i = 0;
		ParkourPlayer pp = sp.getParkourPlayer();
		ParkourManager gm = ParkourManager.getManager();
		for (Level level : gm.getLevels()) {		
			
			

			Level anterior = gm.getLevel(level.getLevel()-1);
			if (pp.hasBeatedLevel(gm.getLevel(level.getLevel()-1)) || level.getLevel()==1) {
				Probability p = gm.getProbabilityBy(level);
				s(i,new ItemBuilder(Material.PAPER).setTitle("§f§lLevel " + level.getLevel())
						.addLore("§aRight click to join")
						.addLore("")
						.addLore("§9Players that tried this level: §a" +p.getAmountOfTried())
						.addLore("§9Players that completed this level: §a" +p.getAmountOfPassed())
						.addLore("§9Successful attempts: §a"+p.getProbability()+ "%").build()); 
			} else {
			s(i,new ItemBuilder(Material.BARRIER).setTitle("§cLevel " + level.getLevel())
					.addLore("§7You need to beat §cLevel " + anterior.getLevel())
					.addLore("§7to play this Level.")
					.build());
		
			}
			i++;
		}
		
		StatsManager sm = StatsManager.getManager();
		List<String> ranking = sm.getTop10(sm.getTotalParkourRankingHashMap(), 0, null);
		int pos = sm.getRankingPosition(RankingEnum.PARKOUR, sp);
		if (pos>10) {
			ranking.add("§6§l"+pos+". §a§l" + sp.getOfflinePlayer().getName() + "§7§l: §b§l" + sm.getTotalParkourRankingHashMap().get(sp.getOfflinePlayer().getUniqueId())+"");
		}
		s(40, new ItemBuilder(Material.LADDER).setTitle("§e§lParkour Ranking")
				.addLores(ranking)
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
		if (stack.getType().equals(Material.BARRIER)) return;
	if (slot<=24) {
		ParkourManager gm = ParkourManager.getManager();
		if (sp.getParkourPlayer().hasBeatedLevel(gm.getLevel(slot+1))) {
			new SelectParkourModeMenu(sp,gm.getLevel(slot+1)).o(sp.getPlayer());
		} else {
		gm.joinLevel(sp.getParkourPlayer(), gm.getLevel(slot+1), ParkourMode.BEAT_LEVEL);
		
		}
	
	}
	}
	
	
	}



