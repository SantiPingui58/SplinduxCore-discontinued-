package me.santipingui58.splindux.scoreboard;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.parkour.ParkourMode;
import me.santipingui58.splindux.game.parkour.ParkourPlayer;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
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
					cache.add("§fName: §6" + sp.getName());
					cache.add("§fRank: " + prefix(sp));
					cache.add("§fLevel: " + LevelManager.getManager().getRank(sp).getRankName());
					cache.add("§fOnline players: §a" + Bukkit.getOnlinePlayers().size());
					cache.add("§fCoins: §6" + sp.getCoins());
					cache.add("§f");
					cache.add("§aFFA Wins: §e" +sp.getPlayerStats().getFFAWins(SpleefType.SPLEEF));
					cache.add("§aFFA Games: §e" + sp.getPlayerStats().getFFAGames(SpleefType.SPLEEF));
					cache.add("§aRanked ELO: §e" + sp.getPlayerStats().getELO(SpleefType.SPLEEF));
					cache.add("§aRankeds Left: §e" + sp.getRankeds());
					cache.add("§f§f");
					cache.add("   §7mc.splindux.com");
					
				} else if (sp.getScoreboard().equals(ScoreboardType.FFAGAME_LOBBY)) {
					Arena arena = sp.getArena();
					boolean spleef = sp.getArena().getSpleefType().equals(SpleefType.SPLEEF) ? true : false;
					if (sp.getArena().getState().equals(GameState.LOBBY)) {
					
						
						cache.add(displayname);
						cache.add("§f");
						cache.add("§7Waiting for players..");
					
						if (spleef) {
							cache.add("§f§f");
							cache.add("§5Mutation Tokens: §d" + sp.getMutationTokens());
						}
						cache.add("§f§f§f");
					} else if (sp.getArena().getState().equals(GameState.GAME)) {
						cache.add(displayname);
						cache.add("§f");
						cache.add("§2Players left: §a" + arena.getFFAPlayers().size());
						if (spleef) cache.add("§2Time left: §e" + time(arena.getTime()));
						if (spleef) cache.add("§5Mutation Tokens: §d" + sp.getMutationTokens());
						cache.add("§f§f§f");
					} else if (sp.getArena().getState().equals(GameState.STARTING) || sp.getArena().getState().equals(GameState.FINISHING)) {
						cache.add(displayname);
						cache.add("§f");
						cache.add("§7Game Starting...");
						if (spleef) {
							cache.add("§f§f");
							cache.add("§5Mutation Tokens: §d" + sp.getMutationTokens());
						}
					
						cache.add("§f§f§f");
					}

					cache.add("§2FFA Wins: §e" +sp.getPlayerStats().getFFAWins(arena.getSpleefType()));
					cache.add("§2FFA Games: §e" + sp.getPlayerStats().getFFAGames(arena.getSpleefType()));
					if (spleef) cache.add("§2FFA Kills: §e" + sp.getPlayerStats().getFFAKills(arena.getSpleefType()));			
					if (sp.getScoreboard().equals(ScoreboardType.FFAGAME_LOBBY)) {
						cache.add("§2M/W Wins: §e" +sp.getPlayerStats().getMonthlyFFAWins(arena.getSpleefType()) +"/" + sp.getPlayerStats().getWeeklyFFAWins(arena.getSpleefType()));
						cache.add("§2M/W Games: §e" + sp.getPlayerStats().getMonthlyFFAGames(arena.getSpleefType())+"/" + sp.getPlayerStats().getWeeklyFFAGames(arena.getSpleefType()));
						if (spleef) cache.add("§2M/W Kills: §e" + sp.getPlayerStats().getMonthlyFFAKills(arena.getSpleefType())+"/"+sp.getPlayerStats().getWeeklyFFAKills(arena.getSpleefType()));
						cache.add("§2W/G: §e" + df.format(sp.getPlayerStats().getWinGameRatio(arena.getSpleefType())));
						if (spleef) cache.add("§2K/G: §e" + df.format(sp.getPlayerStats().getKillGameRatio(arena.getSpleefType())));
						}
					cache.add("§f§f§f§f");
					cache.add("   §7mc.splindux.com");
				} else if (sp.getScoreboard().equals(ScoreboardType.FFAGAME_GAME)) {
					Arena arena = sp.getArena();
					boolean spleef = sp.getArena().getSpleefType().equals(SpleefType.SPLEEF) ? true : false;
				cache.add(displayname);
				cache.add("§f");
				cache.add("§2Players left: §a" + arena.getFFAPlayers().size());
				if (spleef) cache.add("§2Time left: §e" + time(arena.getTime()));
				cache.add("§f§f§f");
				cache.add("§2FFA Wins: §e" +sp.getPlayerStats().getFFAWins(arena.getSpleefType()));
				cache.add("§2FFA Games: §e" + sp.getPlayerStats().getFFAGames(arena.getSpleefType()));
				if (spleef) cache.add("§2FFA Kills: §e" + sp.getPlayerStats().getFFAKills(arena.getSpleefType()));	
				cache.add("§f§f§f§f");
				cache.add("   §7mc.splindux.com");
				}else if (sp.getScoreboard().equals(ScoreboardType._1VS1GAME)) {
					
					Arena arena = null;
					if (sp.isSpectating()) {
						arena = sp.getSpleefArenaSpectating();
					} else {
					 arena = sp.getArena();
					}
					
					cache.add(displayname);
					cache.add("§f");
					if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.STARTING)) {
						if (arena.getPoints1()>=arena.getPoints2()) {
							if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) {
						cache.add("§2"+ arena.getDuelPlayers1().get(0).getName() + ": §e" + arena.getPoints1());
						cache.add("§2"+ arena.getDuelPlayers2().get(0).getName() + ": §e" + arena.getPoints2());
							} else {
								cache.add("§9Blue Team: §e" + arena.getPoints1());
								cache.add("§cRed Team: §e" + arena.getPoints2());
							}
						} else {				
							if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) {
								cache.add("§2"+ arena.getDuelPlayers2().get(0).getName() + ": §e" + arena.getPoints2());
								cache.add("§2"+ arena.getDuelPlayers1().get(0).getName() + ": §e" + arena.getPoints1());
									} else {
										cache.add("§cRed Team: §e" + arena.getPoints2());
										cache.add("§9Blue Team: §e" + arena.getPoints1());
									}
						}
						cache.add("§f§f");
						cache.add("§2Time: §e" + time(arena.getTotalTime()));
						if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
						cache.add("§2Reset: §b" + arena.getTime() +"s");
						cache.add("§2Shrink: §b" + arena.getShrinkTime()+"s");
						cache.add("§f§f§f§f§f§f§f§f§f§f§f");
						}
					
					
					cache.add("§f§f§f§f§f");
				} else {
					cache.add("§2ELO: §e" +sp.getPlayerStats().getELO(arena.getSpleefType()));
					cache.add("§21vs1 Wins: §e" +sp.getPlayerStats().getDuelWins(arena.getSpleefType()));
					cache.add("§21vs1 Games: §e" +sp.getPlayerStats().getDuelGames(arena.getSpleefType()));
					cache.add("§f§f");
					cache.add("§cGame not started");
					cache.add("§f§f§f");
					cache.add("   §7mc.splindux.com");
				}
				} else if (sp.getScoreboard().equals(ScoreboardType.PARKOUR)) {
					cache.add(displayname);
					ParkourPlayer pp = sp.getParkourPlayer();
					 cache.add("§f§f§f");
					if (pp.getArena().getMode().equals(ParkourMode.MOST_JUMPS)) {
					String string = "";
					 int lives = 3-pp.getArena().getFails();
					 for (int i = 1; i<=3;i++) {
						 if (lives<i) {
						 string = string + "§7❤";		 
					 } else {
						 string = string + "§c§l❤";		 
					 }
						 }	
				
					 cache.add("§aLives left:" + string);
					} else {
					 cache.add("§aFails: " + pp.getArena().getFails());
					}
					 
					 cache.add("§a");
					cache.add("§6Playing Level: §b" +pp.getArena().getLevel().getLevel());	
					if (pp.getArena().getMode().equals(ParkourMode.MOST_JUMPS)) {
						cache.add("§aJumps: §b" + pp.getArena().getJumpsMade());
					} else {
						cache.add("§aJumps: §b" + pp.getArena().getJumpsMade()+"/25");
					}
					cache.add("§a§a");
					cache.add("§aTotal Levels §b" + pp.getCurrentLevel()+"/25");
					cache.add("§f§f§f§f");
					cache.add("   §7mc.splindux.com");
				}
				
				for(int i = 0; i < cache.size(); i++) {
					data = cache.toArray(new String[i]);
				}
				
				final String[] data2 = data;
				new BukkitRunnable() {
					public void run() {
						if (sp!=null & sp.getPlayer()!=null)
						BoardAPI.ScoreboardUtil.unrankedSidebarDisplay(sp.getPlayer(), data2);	
					}
				}.runTask(Main.get());
 						
	}
	
	private String time(int s) {
		
			int minutes = s / 60;
			int seconds = s % 60;

			return String.format("%02d:%02d",  minutes, seconds);
		  }
	

	
	private String prefix(SpleefPlayer sp) {
		if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).getPrefix().equalsIgnoreCase("")) {
			return "§3User";
		} else {
		return ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(sp.getOfflinePlayer().getName()).getPrefix());
		}
	}

	
	
}
	

