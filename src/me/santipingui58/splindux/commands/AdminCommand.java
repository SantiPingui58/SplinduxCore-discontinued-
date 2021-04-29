package me.santipingui58.splindux.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.stats.ranking.RankingManager;


public class AdminCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
	
		
		if(cmd.getName().equalsIgnoreCase("admin")){
			final CommandSender p = sender;
			if (p.hasPermission("*")) {
				if (args[0].equalsIgnoreCase("resetholograms")) {
					for (Hologram h : HologramsAPI.getHolograms(Main.get())) {
						h.delete();
					}
					NPCManager.getManager().updateNPCs();
				}else if (args[0].equalsIgnoreCase("pvp")) {
					Main.pvp = !Main.pvp;
				}  else if (args[0].equalsIgnoreCase("givemutations")) {
					DataManager.getManager().giveMutationTokens();
					p.sendMessage("Gave mutation tokens");
				}else if (args[0].equalsIgnoreCase("resetelo")) {
					DataManager.getManager().giveRankeds();
					DataManager.getManager().resetELO();
				} else if (args[0].equalsIgnoreCase("resetweekly")) {
					DataManager.getManager().resetWeeklyStats();
				} else if (args[0].equalsIgnoreCase("resetmonthly")) {
					DataManager.getManager().resetMonthlyStats();
				} else if (args[0].equalsIgnoreCase("test")) {
				Player player = (Player) sender;
				SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(player);
				HikariAPI.getManager().saveData(sp);
				
				}else if (args[0].equalsIgnoreCase("calculateranking")) {
					p.sendMessage("Calculating ranking...");
					RankingManager.getManager().calculate();
					p.sendMessage("Ranking calculation done!");
				} else if (args[0].equalsIgnoreCase("remove0ranking")) {
					p.sendMessage("Calculating ranking...");
					RankingManager.getManager().remove0();;
					p.sendMessage("Ranking calculation done!");
				}else if (args[0].equalsIgnoreCase("queue")) {
					DataManager.getManager().queues(DataManager.getManager().areQueuesClosed());
					
					String s = "";
					if (DataManager.getManager().areQueuesClosed()) {
						s = "§a§lOPEN";
					} else {
						s = "§c§lCLOSED";
					}
					
						DataManager.getManager().queues(!DataManager.getManager().areQueuesClosed());
					for (Player pl : Bukkit.getOnlinePlayers()) {
						
						pl.sendMessage("§b§lQueues are now " + s);
					}
				} else if (args[0].equalsIgnoreCase("finishgames")) {
					for (Arena arena : DataManager.getManager().getArenas()) {
						if (arena.getGameType().equals(GameType.DUEL)) {
							GameManager.getManager().endGameDuel(arena, null, GameEndReason.ENDGAME);
						} else if (arena.getGameType().equals(GameType.FFA)) {
							List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
							list.addAll(arena.getQueue());
							list.addAll(arena.getPlayers());
							GameManager.getManager().endGameFFA(GameEndReason.ENDGAME,arena.getSpleefType());
							list.forEach((sp) -> sp.leave(true,true));				
						}
					}
				} else if (args[0].equalsIgnoreCase("fixplayer")) {
					Player p2 = Bukkit.getPlayer(args[1]);
					p2.setWalkSpeed(0.2F);
					p2.setHealth(20);
					p2.setFoodLevel(20);
				    
				} else if (args[0].equalsIgnoreCase("giveranked")) {
					@SuppressWarnings("deprecation")
					OfflinePlayer p1 = Bukkit.getOfflinePlayer(args[1]);
					if (!p1.hasPlayedBefore()) p.sendMessage("§cPlayer not found.");	
					 new SpleefPlayer(p1.getUniqueId());
					p.sendMessage("§7Loading player data...");
					HikariAPI.getManager().loadData(p1.getUniqueId());
					new BukkitRunnable() {
						public void run() {		
							SpleefPlayer sp1 = SpleefPlayer.getSpleefPlayer(p1);
							sp1.setRankeds(sp1.getRankeds()+1);
				}
						}.runTaskLater(Main.get(), 2L);
					
				}else if (args[0].equalsIgnoreCase("giverankeds")) {
					  DataManager.getManager().giveRankeds();
				}  else if (args[0].equalsIgnoreCase("loadedplayers")) {
					p.sendMessage("Players loaded:" + DataManager.getManager().getPlayers().size());
					
					
				} else if (args[0].equalsIgnoreCase("trasspassplayer")) {
					@SuppressWarnings("deprecation")
					OfflinePlayer p1 = Bukkit.getOfflinePlayer(args[1]);
					@SuppressWarnings("deprecation")
					OfflinePlayer p2 = Bukkit.getOfflinePlayer(args[2]);
					if (!p1.hasPlayedBefore() || !p2.hasPlayedBefore()) p.sendMessage("§cPlayer not found.");	
						 new SpleefPlayer(p1.getUniqueId());
						 new SpleefPlayer(p2.getUniqueId());
						p.sendMessage("§7Loading player data...");
						HikariAPI.getManager().loadData(p1.getUniqueId());
						HikariAPI.getManager().loadData(p2.getUniqueId());
						new BukkitRunnable() {
							public void run() {		
								dod(p1,p2);
					}
							}.runTaskLater(Main.get(), 2L);
			} else if (args[0].equalsIgnoreCase("loadedarenas")) {
			for (Arena arena : DataManager.getManager().getArenas()) {
				p.sendMessage(arena.getName() + " " +arena.getGameType().toString() + " " + arena.getSpleefType().toString());
			}
			} else if (args[0].equalsIgnoreCase("games")) {
				for (Arena arena : DataManager.getManager().getArenas()) {
					if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.STARTING)) {
					p.sendMessage(arena.getName() + " " +arena.getGameType().toString() + " " + arena.getSpleefType().toString() + " " + arena.getTotalTime() + " " + arena.getState().toString());
					}
				}
				}else if (args[0].equalsIgnoreCase("reload")) {
					p.sendMessage("Reloading plugin...");
					DataManager.getManager().saveData();
					new BukkitRunnable() {
						public void run() {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman reload SplinduxCore");
						}
					}.runTaskLater(Main.get(), 60L);
					}
			

		}
			
}
		
		
		return false;
	}
	
	private void dod(OfflinePlayer p1,OfflinePlayer p2) {
		SpleefPlayer sp1 = SpleefPlayer.getSpleefPlayer(p1);
		SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(p2);
		
		
		sp2.setPlayerStats(sp1.getPlayerStats());
		sp2.setRegisterDate(sp1.getRegisterDate());
		sp2.setTotalOnlineTIme(sp1.getTotalOnlineTime()+sp2.getTotalOnlineTime());
		
		sp2.getPlayerStats().setELO(SpleefType.SPLEEF, 1091);
		sp2.setLevel(sp1.getLevel()+sp2.getLevel());
		
		HikariAPI.getManager().saveData(sp2);
	}
		 
	
}