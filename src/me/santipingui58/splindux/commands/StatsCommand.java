package me.santipingui58.splindux.commands;


import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.utils.Utils;




public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("stats")){
			CommandSender p = (Player) sender;
		if (args.length==0) {
			sender.sendMessage("§aUso of command: /stats <SPLEEF/TNTRUN/SPLEGG>");
		} else {
			if(args[0].equalsIgnoreCase("spleef") || args[0].equalsIgnoreCase("tntrun") || args[0].equalsIgnoreCase("splegg")) {
				
				SpleefType type = args[0].equalsIgnoreCase("spleef") ? SpleefType.SPLEEF : args[0].equalsIgnoreCase("splegg") ? SpleefType.SPLEGG : SpleefType.TNTRUN;
				
				if (args.length==1) {
					new BukkitRunnable() {
						public void run() {
							if (sender instanceof Player) {
						sendStats(p,args,(Player) p,sender,type);
							}
						}
					}.runTaskAsynchronously(Main.get());
				} else if (p.isOp()) {
					@SuppressWarnings("deprecation")
					OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
					if (!pa.hasPlayedBefore()) p.sendMessage("§cPlayer not found.");	
					SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
					if (temp==null) {
						 new SpleefPlayer(pa.getUniqueId());
						p.sendMessage("§7Loading player data...");
						HikariAPI.getManager().loadData(pa.getUniqueId());
						new BukkitRunnable() {
							public void run() {		
								sendStats(p, args, pa, sender,type);
					}
							}.runTaskLaterAsynchronously(Main.get(), 20L);
					} else {
						
						new BukkitRunnable() {
							public void run() {		
								sendStats(p, args, pa, sender,type);
					}
							}.runTaskAsynchronously(Main.get());
						
					} 
					
				
				} else {
					sender.sendMessage("§aUso of command: /stats <SPLEEF/TNTRUN/SPLEGG>");
				}
				
			} else {
				sender.sendMessage("§aUso of command: /stats <SPLEEF/TNTRUN/SPLEGG>");
			}
		}
		}
		}
		return false;
	}

	private void sendStats(CommandSender p,String[] args,OfflinePlayer pa,CommandSender sender,SpleefType type) {
		SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
		if (sp2!=null) {
			p.sendMessage(" ");
			p.sendMessage(" ");
			p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			p.sendMessage("§aCountry: §b" + sp2.getCountry());
			p.sendMessage("§aRanking: §b" + sp2.getRankingPosition());
			p.sendMessage("§aRegister date: §b" + format.format(sp2.getRegisterDate()));
			p.sendMessage("§aTotal Online Time: §b" + Utils.getUtils().minutesToDate(sp2.getTotalOnlineTime()));
			p.sendMessage("§aCurrent Online Time: §b" + Utils.getUtils().secondsToDate(sp2.getOnlineTime()));
			p.sendMessage("§aSpleef Rank: §b" + LevelManager.getManager().getRank(sp2).getRankName() + " §7(" + sp2.getLevel() + " EXP / " + LevelManager.getManager().getPercentage(sp2) +")");
			p.sendMessage("§aParkour Level: §b" + sp2.getParkourPlayer().getCurrentLevel()+"/25");
			p.sendMessage(" ");
			p.sendMessage("§6-=FFA=-");
			p.sendMessage("§aWins: §b"+sp2.getPlayerStats().getFFAWins(type) + " (§7" + StatsManager.getManager().getRankingPosition(RankingEnum.SPLEEFFFA_WINS, sp2) + "§ Pos.)");
			p.sendMessage("§aGames: §b"+sp2.getPlayerStats().getFFAGames(type)+ " (§7" + StatsManager.getManager().getRankingPosition(RankingEnum.SPLEEFFFA_GAMES, sp2) + "§ Pos.)");
			p.sendMessage("§aKills: §b"+sp2.getPlayerStats().getFFAKills(type)+ " (§7" + StatsManager.getManager().getRankingPosition(RankingEnum.SPLEEFFFA_KILLS,sp2) + "§ Pos.)");
			p.sendMessage("§6-=DUELS=-");
			p.sendMessage("§aWins: §b"+sp2.getPlayerStats().getDuelWins(type)+ " (§7" + StatsManager.getManager().getRankingPosition(RankingEnum.SPLEEF1VS1_WINS,sp2) + "§ Pos.)");
			p.sendMessage("§aGames: §b"+sp2.getPlayerStats().getDuelGames(type)+ " (§7" + StatsManager.getManager().getRankingPosition(RankingEnum.SPLEEF1VS1_GAMES,sp2) + "§ Pos.)");
			p.sendMessage("§aELO: §b"+sp2.getPlayerStats().getELO(type)+ " (§7" + StatsManager.getManager().getRankingPosition(RankingEnum.SPLEEF1VS1_ELO,sp2 ) + "§ Pos.)");
			p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
			
		} else if (args[0].equalsIgnoreCase("help")) {
		p.sendMessage("§aUse of command: /stats ");
		p.sendMessage("§aUse of command: /stats monthly");
		p.sendMessage("§aUse of command: /stats weekly");
		if (p.isOp()) {
			p.sendMessage("§aUse of command: /stats <Player>");
			p.sendMessage("§aUse of command: /stats <Player> monthly");
			p.sendMessage("§aUse of command: /stats <Player> weekly");
		}
		p.sendMessage("§aUse of command: /stats top ffaspleef <wins/games/kills/KG/WG> <page>");
		p.sendMessage("§aUse of command: /stats top spleefDuel <ELO/games> <page>");
		
	} else if (args[0].equalsIgnoreCase("ffaspleef")) {
		if (args.length==2 || args.length==3) {
			
			int page = 0;
			if (args.length==3) {
				try {
					page = Integer.parseInt(args[2]);
					if (page<=0) {
						sender.sendMessage("§a"+ args[2]+ " §cisnt a valid number.");
					}
					page=page-1;
				} catch (Exception e) {
					sender.sendMessage("§a"+ args[3]+ " §cisnt a valid number.");
				}
			} else if (args.length==2) {
				try {
					page = Integer.parseInt(args[1]);
					if (page<=0) {
						sender.sendMessage("§a"+ args[1]+ " §cisnt a valid number.");
					}
					page=page-1;
				} catch (Exception e) {
					sender.sendMessage("§a"+ args[2]+ " §cisnt a valid number.");
				}
			}
			
			StatsManager sm = StatsManager.getManager();
			if (args[1].equalsIgnoreCase("wins")) {
				sm.sendRanking(page,RankingEnum.SPLEEFFFA_WINS,sender);
			} else if (args[1].equalsIgnoreCase("kills")) {
				sm.sendRanking(page,RankingEnum.SPLEEFFFA_KILLS,sender);
			} else if (args[1].equalsIgnoreCase("games")) {
				sm.sendRanking(page,RankingEnum.SPLEEFFFA_GAMES,sender);
			}
		
		}
	} else if (args[0].equalsIgnoreCase("monthly")) {
		
	}else {
		p.sendMessage("§cPlayer not found.");
	}
		
	}

	
	
	

}
