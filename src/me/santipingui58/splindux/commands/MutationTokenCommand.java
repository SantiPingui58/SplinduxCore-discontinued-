package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.SpleefPlayer;

public class MutationTokenCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("mutationtoken")) {
			Player p = (Player) sender;
				if (args.length==0) {
					p.sendMessage("§aUse of command: /mutationtoken give <player> <amount>");
				} else if (args[0].equalsIgnoreCase("give")) {
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
						
						splayer.setMutationTokens(splayer.getMutationTokens()+level);
						} else {
							p.sendMessage("§cThis player doesn't exist");
						}
					} else {
						p.sendMessage("§aUse of command: /mutationtoken give <player> <amount>");
					}
				} 
			
			
}
			return false;
			
		}
}