package me.santipingui58.splindux.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationState;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.hologram.HologramType;
import me.santipingui58.splindux.hologram.HologramViewer;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.stats.ranking.RankingManager;
import me.santipingui58.splindux.sws.SWSCountry;
import me.santipingui58.splindux.sws.SWSManager;
import me.santipingui58.splindux.utils.Utils;


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
				}else if (args[0].equalsIgnoreCase("guilds")) {
					GuildsManager.getManager().payPlayers();
				} else if (args[0].equalsIgnoreCase("givemutations")) {
					DataManager.getManager().giveMutationTokens();
					p.sendMessage("Gave mutation tokens");
				}else if (args[0].equalsIgnoreCase("resetelo")) {
					DataManager.getManager().giveRankeds();
					DataManager.getManager().resetELO();
				} else if (args[0].equalsIgnoreCase("resetweekly")) {
					DataManager.getManager().resetWeeklyStats();
				} else if (args[0].equalsIgnoreCase("resetmonthly")) {
					DataManager.getManager().resetMonthlyStats();
				} else if (args[0].equalsIgnoreCase("testarena")) {
					Player player = (Player) sender;
					SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(player);
					SpleefType spleefType  = SpleefType.valueOf(args[3]);
					List<SpleefPlayer> sp2 = new ArrayList<SpleefPlayer>();
					sp2.add(SpleefPlayer.getSpleefPlayer(Bukkit.getPlayer(args[1])));
					GameManager.getManager().duelGame(sp,sp2, args[2],spleefType , 1, false, false, -1);
				}else if (args[0].equalsIgnoreCase("test")) {
					Player player = (Player) sender;
					HologramType type = HologramManager.getManager().getHologram(HologramType.GAME_STATS).getType();
					HologramViewer viewer = HologramViewer.getHologramViewer(player.getUniqueId(), type);
					player.teleport(viewer.getHologramsIdPrimaryButton(type).get(0).getValue().getBukkitEntity());
					
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
						//GameManager.getManager().endGameFFA(GameEndReason.ENDGAME,arena.getSpleefType());
							
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
					} else if(args[0].equalsIgnoreCase("reloadarenas")) {
						
						
						new BukkitRunnable() {
							public void run() {
						DataManager.getManager().getArenas().clear();
						
						new BukkitRunnable() {
							public void run () {
								Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
								 		new BukkitRunnable() {
								 			public void run() {
											 for (Player p : Bukkit.getOnlinePlayers()) {										
												 p.setFlying(false);
														SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(p);
														splayer.giveLobbyItems();	
														splayer.updateScoreboard();
														if (p.getWorld().getName().equalsIgnoreCase("arenas") || p.getWorld().getName().equalsIgnoreCase("parkour")) {
															p.teleport(lobby);
														}
										 }
								 		}
								 		}.runTask(Main.get());


							}
						}.runTaskLaterAsynchronously(Main.get(),40L);
					} 
							}.runTaskAsynchronously(Main.get());
					} else if (args[0].equalsIgnoreCase("savedata")) {
						DataManager.getManager().saveData();
					} else if (args[0].equalsIgnoreCase("elodecay")) {
						HikariAPI.getManager().eloDecay();
					} else if (args[0].equalsIgnoreCase("addmutation")) {
						try {
							
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer((Player) sender);
							GameMutation mutation = new GameMutation(sp, MutationType.valueOf(args[1]));				
							mutation.setArena(GameManager.getManager().getFFAArenaByArena(sp.getArena()));
							mutation.setState(MutationState.QUEUE);
							for (SpleefPlayer players : sp.getArena().getViewers()) {
								players.getPlayer().sendMessage("§aAn Admin added the next mutation: " + mutation.getType().getTitle());
							}
							
						} catch(Exception ex) {
						ex.printStackTrace();
						p.sendMessage("Error: /admin addmutation <mutation>");
						}
					} else if (args[0].equalsIgnoreCase("fly")) {
						Main.fly = !Main.fly;
						String s = null;
						
						if (Main.fly) {
							s = "§a§lENABLED";
						} else {
							s = "§c§lDISABLED";
						}
						for (Player pl : Bukkit.getOnlinePlayers()) {
							if ((pl.isFlying() || pl.getAllowFlight()) && !pl.hasPermission("splindux.staff")) {
								pl.setAllowFlight(false);
								pl.setFlying(false);
							}
							
							pl.sendMessage("§b§lFly is now " + s);
						}
						
						
					} else if (args[0].equalsIgnoreCase("canbreak")) {
						Main.canBreak = !Main.canBreak;
						String s = null;
						
						if (Main.canBreak) {
							s = "§a§lENABLED";
						} else {
							s = "§c§lDISABLED";
						}
						for (Player pl : Bukkit.getOnlinePlayers()) {
							pl.sendMessage("§b§lBlock breaking and placing is now " + s);
						}
					} else if (args[0].equalsIgnoreCase("sws")) {
						SWSManager sws = SWSManager.getManager();
						if (args[1].equalsIgnoreCase("setswc")) {
							SWSCountry country = sws.getCountry(args[2]);
							country.setSWCPlayOffsAmount(Integer.valueOf(args[3]));
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #sws-logs The country :flag_"+country.getCode()+": **"
									+ DataManager.getManager().getCountryString(country.getCode()).toUpperCase()+":flag_"+country.getCode()+":** has now **"+args[2]+" SWC PLAYOFFS SPOTS!**");
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #sws-logs _(El pais :flag_"+country.getCode()+": **"
									+ DataManager.getManager().getCountryString(country.getCode()).toUpperCase()+"**:flag_+"+country.getCode()+ ": ahora tiene **"+args[2]+" posiciones de SWC PlAYOFFS!)_**");
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #sws-logs _(страна :flag_"+country.getCode()+": **"
									+ DataManager.getManager().getCountryString(country.getCode()).toUpperCase()+"**:flag_+"+country.getCode()+ ": теперь у тебя есть **"+args[2]+" позиции SWC PlAYOFFS!)_**");
							sws.saveCountries();
						} else if (args[1].equalsIgnoreCase("setcontinental")) {
							SWSCountry country = sws.getCountry(args[2]);
							country.setContinentalPlayOffsAmount(Integer.valueOf(args[3]));
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #sws-logs The country :flag_"+country.getCode()+": **"
									+ DataManager.getManager().getCountryString(country.getCode()).toUpperCase()+":flag_"+country.getCode()+":** has now **"+args[2]+" CONTINENTAL PLAYOFFS SPOTS!**");
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #sws-logs _(El pais :flag_"+country.getCode()+": **"
									+ DataManager.getManager().getCountryString(country.getCode()).toUpperCase()+"**:flag_+"+country.getCode()+ ": ahora tiene **"+args[2]+" posiciones de CONTINENTAL PlAYOFFS!)_**");
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #sws-logs _(страна :flag_"+country.getCode()+": **"
									+ DataManager.getManager().getCountryString(country.getCode()).toUpperCase()+"**:flag_+"+country.getCode()+ ": теперь у тебя есть **"+args[2]+" позиции CONTINENTAL PlAYOFFS!)_**");
							sws.saveCountries();
						} 
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