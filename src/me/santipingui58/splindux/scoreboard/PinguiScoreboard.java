package me.santipingui58.splindux.scoreboard;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.nametagedit.plugin.NametagEdit;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.stats.level.LevelManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class PinguiScoreboard {
	
	private static PinguiScoreboard scoreboard;	
	
	 public static PinguiScoreboard getScoreboard() {
	        if (scoreboard == null)
	        	scoreboard = new PinguiScoreboard();
	        return scoreboard;
	    }
	
	public void scoreboard(SpleefPlayer sp) {
				String[] data = null;
				List<String> cache = new ArrayList<String>();
				DecimalFormat df = new DecimalFormat("0.00");
				String displayname = "§e§lSplin§b§lDux";
				if (sp.getScoreboard().equals(ScoreboardType.LOBBY)) {
					cache.add(displayname);
					cache.add("§f§f§f");
					cache.add("§fName: §6" + sp.getPlayer().getName());
					cache.add("§fRank: " + prefix(sp));
					cache.add("§fLevel: " + LevelManager.getManager().getRank(sp).getRankName());
					cache.add("§fOnline players: §a" + Bukkit.getOnlinePlayers().size());
					cache.add("§fCoins: §6" + sp.getCoins());
					cache.add("§f");
					cache.add("§aFFA Wins: §e" +sp.getFFAWins());
					cache.add("§aFFA Games: §e" + sp.getFFAGames());
					cache.add("§a1vs1 Wins: §e" + sp.get1vs1Wins());
					cache.add("§a1vs1 Games: §e" + sp.get1vs1Games());
					cache.add("§f§f");
					cache.add("   §7mc.splindux.com");
					
				} else if (sp.getScoreboard().equals(ScoreboardType.FFAGAME_LOBBY) || sp.getScoreboard().equals(ScoreboardType.FFAGAME_GAME)) {
					SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
					cache.add(displayname);
					cache.add("§f");
					cache.add("§2Players left: §a" + arena.getPlayers().size());
					cache.add("§f§f§f§f§f");
					if (arena.getState().equals(GameState.GAME)) {
					cache.add("§2Time left: §e" + time(arena.getTime()));
					cache.add("§f§f");
					} else if (arena.getState().equals(GameState.STARTING)) {
						cache.add("§2Game starting...");
						cache.add("§f§f");
					} 			
					
					cache.add("§2FFA Wins: §e" +sp.getFFAWins());
					cache.add("§2FFA Games: §e" + sp.getFFAGames());
					cache.add("§2FFA Kills: §e" + sp.getFFAKills());
					if (sp.getScoreboard().equals(ScoreboardType.FFAGAME_LOBBY)) {
					cache.add("§2M/W Wins: §e" +sp.getMonthlyFFAWins() +"/" + sp.getWeeklyFFAWins());
					cache.add("§2M/W Games: §e" + sp.getMonthlyFFAGames()+"/" + sp.getWeeklyFFAGames());
					cache.add("§2M/W Kills: §e" + sp.getMonthlyFFAKills()+"/"+sp.getWeeklyFFAKills());
					cache.add("§2W/G: §e" + df.format(sp.getWinGameRatio()));
					cache.add("§2K/G: §e" + df.format(sp.getKillGameRatio()));
					}
					cache.add("§f§f§f");
					cache.add("   §7mc.splindux.com");
				} else if (sp.getScoreboard().equals(ScoreboardType._1VS1GAME)) {
					SpleefArena arena = null;
					if (sp.isSpectating()) {
						arena = GameManager.getManager().getArenaByPlayer(sp.getSpectating());
					} else {
					 arena = GameManager.getManager().getArenaByPlayer(sp);
					}
					
					cache.add(displayname);
					cache.add("§f");
					if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.STARTING)) {
						cache.add("§2Reset in: §e" + time(arena.getTime()));
						cache.add("§2Total Time: §e" + time(arena.getTotalTime()));
					if (arena.getPoints1()>=arena.getPoints2()) {
					cache.add("§2"+ arena.getPlayers().get(0).getPlayer().getName() + ": §e" + arena.getPoints1());
					cache.add("§2"+ arena.getPlayers().get(1).getPlayer().getName() + ": §e" + arena.getPoints2());
					} else {				
						cache.add("§2"+ arena.getPlayers().get(1).getPlayer().getName() + ": §e" + arena.getPoints2());
						cache.add("§2"+ arena.getPlayers().get(0).getPlayer().getName() + ": §e" + arena.getPoints1());
					}
					cache.add("§f§f§f§f§f");
				} else {
					cache.add("§2ELO: §e" +sp.getELO());
					cache.add("§21vs1 Wins: §e" +sp.get1vs1Wins());
					cache.add("§21vs1 Games: §e" +sp.get1vs1Games());
					cache.add("§f§f");
					cache.add("§cGame not started");
					cache.add("§f§f§f");
					cache.add("   §7mc.splindux.com");
				}
					
				} 
				
				
				for(int i = 0; i < cache.size(); i++) {
					data = cache.toArray(new String[i]);
				}
				
				BoardAPI.ScoreboardUtil.unrankedSidebarDisplay(sp.getPlayer(), data);	
	}

	
	private String time(int s) {
		
			int minutes = s / 60;
			int seconds = s % 60;

			return String.format("%02d:%02d",  minutes, seconds);
		  }
	
	public void setTags() {
		
		for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
		Player p = sp.getPlayer();
		if (sp.isAfk() && sp.getPlayer().hasPermission("splindux.afk")) {	
			NametagEdit.getApi().setPrefix(p, "§7§oAFK ");
		} else {
			String prefix = PermissionsEx.getUser(p).getPrefix();
			if (!prefix.isEmpty()) {
			NametagEdit.getApi().setPrefix(p, PermissionsEx.getUser(p).getPrefix());
			} else {
				NametagEdit.getApi().setPrefix(p, "§7");
			}
		}
	}
	}
	
	private String prefix(SpleefPlayer sp) {
		if (PermissionsEx.getUser(sp.getPlayer()).getPrefix().equalsIgnoreCase("")) {
			return "§3User";
		} else {
		return ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sp.getPlayer()).getPrefix());
		}
	}

	
}
	

