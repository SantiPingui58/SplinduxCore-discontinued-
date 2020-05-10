package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
public class CoinsCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			if(cmd.getName().equalsIgnoreCase("coins")) {

			if (sender.isOp()) {
				if (args.length==0) {
					sender.sendMessage("§aUse of command: /coins add <player> <amount>");
					sender.sendMessage("§aUse of command: /coins set <player> <amount>");
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
							sender.sendMessage("§a"+ args[3]+ " §cisnt a valid number.");
							return false;
						}
					
						splayer.addCoins(level);
						} else {
							sender.sendMessage("§cThis player doesn't exist");
						}
					} else {
						sender.sendMessage("§aUse of command: /coins add <player>");
					}
				} 
			}
			}

			return false;
			
		}
}