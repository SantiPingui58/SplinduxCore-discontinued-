package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;




public class RankCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			//rank SantiPingui58 donator
			if(cmd.getName().equalsIgnoreCase("rank")) {
			if(!(sender instanceof Player)) {
				if (args[0].equalsIgnoreCase("add")) {
			String name = args[1];
			String rank = args[2];
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage("§e§lSplin§b§ldux §eCongratulations to §b§l" + name + " §efor bought " + 	getRank(rank) + " §erank!");
				}
			
			if (!PermissionsEx.getUser(name).has("splindux.staff")) {
				PermissionsEx.getUser(name).addGroup(rank);
			}
			
				} else if (args[0].equalsIgnoreCase("remove")) {
					String name = args[1];
					String rank = args[2];
					PermissionsEx.getUser(name).removeGroup(rank);
				} 
			} else {
				sender.sendMessage("§cYou are not allowed to execute this command.");
			}
		
			
}
			return false;
			
		}
		
		
		private String getRank(String rank) {
		 if (rank.equalsIgnoreCase("vip")) {
				return "§a§l[VIP]";
			} else if (rank.equalsIgnoreCase("epic")) {
				return "§2§l[Epic]";
			} else if (rank.equalsIgnoreCase("legend")) {
				return "§5§l[Extreme]";
			}
			return null;
		}
}