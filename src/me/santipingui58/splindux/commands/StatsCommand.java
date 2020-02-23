package me.santipingui58.splindux.commands;


import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
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
			p.sendMessage("§aFFA Wins: §b"+sp.getFFAWins() + "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WINS) + "° Pos.)");
			p.sendMessage("§aFFA Games: §b"+sp.getFFAGames()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_GAMES) + "° Pos.)");
			p.sendMessage("§aFFA Kills: §b"+sp.getFFAKills()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KILLS) + "° Pos.)");
			p.sendMessage("§aKills/Games Ratio: §b"+sp.getKillGameRatio()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KG) + "° Pos.)");
			p.sendMessage("§aWins/Games Ratio: §b"+sp.getWinGameRatio()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WG) + "° Pos.)");
			p.sendMessage("§6-=SPLEEF 1VS1=-");
			p.sendMessage("§aFFA Wins: §b"+sp.get1vs1Wins()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_WINS) + "° Pos.)");
			p.sendMessage("§aFFA Games: §b"+sp.get1vs1Games()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_GAMES) + "° Pos.)");
			p.sendMessage("§aFFA Kills: §b"+sp.getELO()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_ELO) + "° Pos.)");
			p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
			
		} else {
			
			Player pa = Bukkit.getPlayer(args[0]);
			if (Bukkit.getOnlinePlayers().contains(pa) && p.isOp()) {
				SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
				p.sendMessage(" ");
				p.sendMessage(" ");
				p.sendMessage("§6-=-=-=-[§a§lStats§6]-=-=-=-");
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				p.sendMessage("§aRegister date: §b" + format.format(sp2.getRegisterDate()));
				p.sendMessage("§6-=FFA SPLEEF=-");
				p.sendMessage("§aFFA Wins: §b"+sp2.getFFAWins() + "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WINS) + "° Pos.)");
				p.sendMessage("§aFFA Games: §b"+sp2.getFFAGames()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_GAMES) + "° Pos.)");
				p.sendMessage("§aFFA Kills: §b"+sp2.getFFAKills()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KILLS) + "° Pos.)");
				p.sendMessage("§aKills/Games Ratio: §b"+sp2.getKillGameRatio()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_KG) + "° Pos.)");
				p.sendMessage("§aWins/Games Ratio: §b"+sp2.getWinGameRatio()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEFFFA_WG) + "° Pos.)");
				p.sendMessage("§6-=SPLEEF 1VS1=-");
				p.sendMessage("§aFFA Wins: §b"+sp2.get1vs1Wins()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_WINS) + "° Pos.)");
				p.sendMessage("§aFFA Games: §b"+sp2.get1vs1Games()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_GAMES) + "° Pos.)");
				p.sendMessage("§aFFA Kills: §b"+sp2.getELO()+ "(§7" + StatsManager.getManager().getRankingPosition(RankingType.SPLEEF1VS1_ELO) + "° Pos.)");
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
			p.sendMessage("§aUse of command: /stats top ffaspleef <page>");
			p.sendMessage("§aUse of command: /stats top spleef1vs1 <page>");
			
		} 
		}
		}
		}
		return false;
	}

}
