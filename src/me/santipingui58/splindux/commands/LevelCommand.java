package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.level.LevelManager;

public class LevelCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("level")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
			Player p = (Player) sender;
			if (p.hasPermission("splindux.admin")) {
				if (args.length==0) {
					p.sendMessage("§aUse of command: /level add <player> <amount>");
					p.sendMessage("§aUse of command: /level set <player> <amount>");
				} else if (args[0].equalsIgnoreCase("add")) {
					if (args.length==3) {
						@SuppressWarnings("deprecation")
						OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
						if (pa.hasPlayedBefore()) {
						SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(pa);
						int level = 0;
						try {
							level = Integer.parseInt(args[2]);
						} catch (Exception e) {
							p.sendMessage("§a"+ args[3]+ " §cisnt a valid number.");
							return false;
						}
						
						LevelManager.getManager().addLevel(splayer, level);
						} else {
							p.sendMessage("§cThis player doesn't exist");
						}
					} else {
						p.sendMessage("§aUse of command: /level add <player>");
					}
				} 
			}
			}
}
			return false;
			
		}
}