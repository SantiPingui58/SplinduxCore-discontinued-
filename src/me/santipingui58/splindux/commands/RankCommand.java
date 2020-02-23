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
			String name = args[0];
			String rank = args[1];
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage("§3§lJ§c§lH§d§lSpleef §eCongratulations to §b§l" + name + " §efor bought " + 	getRank(rank) + " §erank!");
				}
			
			if (!PermissionsEx.getUser(name).has("jhspleef.staff")) {
				if (rank.equalsIgnoreCase("hero")) {
					rank = rank+"_";
				}
				PermissionsEx.getUser(name).addGroup(rank);
			}
			
			} else {
				sender.sendMessage("§cYou are not allowed to execute this command.");
			}
		
			
}
			return false;
			
		}
		
		
		private String getRank(String rank) {
			if (rank.equalsIgnoreCase("donator")) {
				return "§6[Donator]";
			} else if (rank.equalsIgnoreCase("vip")) {
				return "§a[VIP]";
			} else if (rank.equalsIgnoreCase("premium")) {
				return "§b[Premium]";
			} else if (rank.equalsIgnoreCase("legend")) {
				return "§d[Legend]";
			} else if (rank.equalsIgnoreCase("hero")) {
				return "§c[Hero]";
			} else if (rank.equalsIgnoreCase("veteran")) {
				return "§4[Veteran]";
			} else if (rank.equalsIgnoreCase("herobrine")) {
				return "§f§l[Hero§c§lBrine]";
			} else if (rank.equalsIgnoreCase("god")) {
				return "§f§l[God]";
			} 
			return null;
		}
}