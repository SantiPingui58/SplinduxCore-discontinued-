package me.santipingui58.splindux.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.santipingui58.splindux.utils.Utils;

public class TitleCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("title")) {
				CommandSender p = sender;
			if (p.hasPermission("splindux.staff")) {
				if (args.length==0) {
					p.sendMessage("§aUse of command: /title title <text> subtitle <text>");
				} else if (args[0].equalsIgnoreCase("title") || args[0].equalsIgnoreCase("subtitle")) {
					StringBuilder builder = new StringBuilder();
					
					int left = 0;
				    for (int i = 1; i < args.length; i++)
				    {
				    	if (!args[i].equalsIgnoreCase("subtitle")) {
				      builder.append(args[i]).append(" ");
				      left = i;
				    	} else {
				    		break;
				    	}
				    }
				    
				  String title = builder.toString();
				  
					StringBuilder builder2 = new StringBuilder();
				  for (int i = left+2; i < args.length; i++)
				    {
				      builder2.append(args[i]).append(" ");
				    }
				  
				  String subtitle = builder2.toString();
				  

				  Utils.getUtils().sendTitles(Bukkit.getOnlinePlayers(),ChatColor.translateAlternateColorCodes('&', title),
						  ChatColor.translateAlternateColorCodes('&', subtitle), 5, 100, 5);
				}
			}
			
}
			return false;
			
		}
}