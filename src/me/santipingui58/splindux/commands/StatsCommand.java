package me.santipingui58.splindux.commands;


import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.RankingType;
import me.santipingui58.splindux.stats.StatsManager;



public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("stats")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		if (args.length==0) {
			p.sendMessage(" ");
			p.sendMessage(" ");
			p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			p.sendMessage("§aRegister date: §b" + format.format(sp.getRegisterDate()));
			p.sendMessage("§6-=FFA SPLEEF=-");
			p.sendMessage("§aFFA Wins: §b"+sp.getFFAWins() + " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WINS) + "° Pos.)");
			p.sendMessage("§aFFA Games: §b"+sp.getFFAGames()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_GAMES) + "° Pos.)");
			p.sendMessage("§aFFA Kills: §b"+sp.getFFAKills()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KILLS) + "° Pos.)");
			p.sendMessage("§aKills/Games Ratio: §b"+String.format("%.00f", sp.getKillGameRatio())+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KG) + "° Pos.)");
			p.sendMessage("§aWins/Games Ratio: §b"+String.format("%.00f", sp.getWinGameRatio())+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WG) + "° Pos.)");
			p.sendMessage("§6-=SPLEEF 1VS1=-");
			p.sendMessage("§a1vs1 Wins: §b"+sp.get1vs1Wins()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_WINS) + "° Pos.)");
			p.sendMessage("§a1vs1 Games: §b"+sp.get1vs1Games()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_GAMES) + "° Pos.)");
			p.sendMessage("§a1vs1 ELO: §b"+sp.getELO()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_ELO) + "° Pos.)");
			p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
			
		} else {
			OfflinePlayer pa = Bukkit.getPlayer(args[0]);
			SpleefPlayer sp2 = null;
			try {
				sp2 = SpleefPlayer.getSpleefPlayer(pa);
			} catch(Exception e) {}
			if (sp2!=null && p.isOp()) {
				if (args.length==1) {
				p.sendMessage(" ");
				p.sendMessage(" ");
				p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				p.sendMessage("§aRegister date: §b" + format.format(sp2.getRegisterDate()));
				p.sendMessage("§6-=FFA SPLEEF=-");
				p.sendMessage("§aFFA Wins: §b"+sp2.getFFAWins() + " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WINS) + "° Pos.)");
				p.sendMessage("§aFFA Games: §b"+sp2.getFFAGames()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_GAMES) + "° Pos.)");
				p.sendMessage("§aFFA Kills: §b"+sp2.getFFAKills()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KILLS) + "° Pos.)");
				p.sendMessage("§aKills/Games Ratio: §b"+String.format("%.00f", sp2.getKillGameRatio())+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KG) + "° Pos.)");
				p.sendMessage("§aWins/Games Ratio: §b"+String.format("%.00f", sp2.getWinGameRatio())+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WG) + "° Pos.)");
				p.sendMessage("§6-=SPLEEF 1VS1=-");
				p.sendMessage("§a1vs1 Wins: §b"+sp2.get1vs1Wins()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_WINS) + "° Pos.)");
				p.sendMessage("§a1vs1 Games: §b"+sp2.get1vs1Games()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_GAMES) + "° Pos.)");
				p.sendMessage("§a1vs1 ELO: §b"+sp2.getELO()+ " (§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_ELO) + "° Pos.)");
				p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
				} else if (args[1].equalsIgnoreCase("monthly")) {
					
				} 
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
			p.sendMessage("§aUse of command: /stats top spleef1vs1 <ELO/games> <page>");
			
		} else if (args[0].equalsIgnoreCase("top")) {
			if (args.length==3 || args.length==4) {
			if (args[1].equalsIgnoreCase("ffaspleef")) {
				if (args[2].equalsIgnoreCase("wins")) {
					sendRanking(args,RankingType.SPLEEFFFA_WINS,sp);
				} else if (args[2].equalsIgnoreCase("kills")) {
					sendRanking(args,RankingType.SPLEEFFFA_KILLS,sp);
				} else if (args[2].equalsIgnoreCase("games")) {
					sendRanking(args,RankingType.SPLEEFFFA_GAMES,sp);
				} else if (args[2].equalsIgnoreCase("kg")) {
					sendRanking(args,RankingType.SPLEEFFFA_KG,sp);
				} else if (args[2].equalsIgnoreCase("wg")) {
					sendRanking(args,RankingType.SPLEEFFFA_WG,sp);
				} 
			}
			}
		}
		}
		}
		}
		return false;
	}

	
	private boolean sendRanking(String[] args,RankingType type,SpleefPlayer sp) {
		Player p = sp.getPlayer();
		int page = 0;
		if (args.length==4) {
			try {
				page = Integer.parseInt(args[3]);
				if (page<=0) {
					p.sendMessage("§a"+ args[3]+ " §cisnt a valid number.");
					return false;
				}
				page=page-1;
			} catch (Exception e) {
				p.sendMessage("§a"+ args[3]+ " §cisnt a valid number.");
				return false;
			}
		}
		int pag = page+1;
		HashMap<String,Integer> hashmap = StatsManager.getManager().getRanking(type);
		String title = StatsManager.getManager().getTitleByType(type);
		int total = (hashmap.size()/10)+1;		
		if (page<=total-1) {
		p.sendMessage("§6-=-=-=-[§a§l"+title +" Top ("+pag+"/"+total+")§6]-=-=-=-");
		StatsManager.getManager().sendRanking(sp, page,type);
		p.sendMessage("§6-=-=-=-[§a§l"+title +" Top ("+pag+"/"+total+")§6]-=-=-=-");
	} else {
		p.sendMessage("§cPage not found.");
	}
		return true; 
	}
}
