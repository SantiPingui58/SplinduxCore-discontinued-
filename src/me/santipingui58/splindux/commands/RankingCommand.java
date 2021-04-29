package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import me.santipingui58.splindux.stats.ranking.RankingManager;

public class RankingCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {

		if(cmd.getName().equalsIgnoreCase("ranking")) {
			if (args.length==0) {
				sender.sendMessage("§aUse of command: /ranking <page>");
			} else {			
				RankingManager rm = RankingManager.getManager();
				rm.sendRanking(args,sender);
			}
	}
		return false;
	

	}
	
}