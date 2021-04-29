package me.santipingui58.splindux.gui.game.parkour;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.parkour.Level;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.parkour.ParkourMode;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.stats.ParkourRanking;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.utils.ItemBuilder;

public class SelectParkourModeMenu extends MenuBuilder {

	
	public SelectParkourModeMenu(SpleefPlayer sp,Level level) {
		super("§2§lSelect mode",3);
		
		new BukkitRunnable() {
		public void run() {
			
		
		s(11, new ItemBuilder(Material.DIAMOND).setTitle("§aUnlimited Parkour")
				.addLore("§aLevel " + level.getLevel())
				.addLore("")
				.addLore("§7Once you complete a map you can")
				.addLore("§7take the challenge to see how many jumps")
				.addLore("§7you can do with only §c3 lives§7!")
				.build());
		
		StatsManager sm = StatsManager.getManager();
		
		ParkourRanking a = sm.getParkourRanking(level);
		HashMap<UUID, Integer> b= a.getRanking();
		List<String> ranking = sm.getTop10(b, 0, RankingEnum.PARKOUR);
		int pos = sm.getParkourRankingPosition(level, sp);
		if (pos>10) {
			ranking.add("§6§l"+pos+". §a§l" + sp.getOfflinePlayer().getName() + "§7§l: §b§l" + a.getRanking().get(sp.getOfflinePlayer().getUniqueId()));
		}
		
		s(13, new ItemBuilder(Material.LADDER).setTitle("§6Ranking Level " + level.getLevel()).addLores(ranking).build());
		
		
		s(15, new ItemBuilder(Material.EMERALD).setTitle("§aPlay Parkour Again").addLore("§aLevel " + level.getLevel()).build());
		
		s(26, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
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
		if (slot==26) {
			new ParkourMenu(sp).o(sp.getPlayer());
		} else if (stack.getItemMeta().getLore()!=null) {
		ParkourManager gm = ParkourManager.getManager();
		String str = ChatColor.stripColor(stack.getItemMeta().getLore().get(0));
		 str = str.replaceAll("\\D+","");
		 int level = Integer.valueOf(str);
		if (slot==11) {			
			gm.joinLevel(sp.getParkourPlayer(), gm.getLevel(level), ParkourMode.MOST_JUMPS);
		} else if (slot==15) {	
			gm.joinLevel(sp.getParkourPlayer(), gm.getLevel(level), ParkourMode.BEAT_LEVEL);
		}  
	}
	}
	
	
	}



