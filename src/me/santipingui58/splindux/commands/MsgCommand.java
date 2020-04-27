package me.santipingui58.splindux.commands;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class MsgCommand implements CommandExecutor {
	
	private static HashMap<CommandSender,CommandSender> respond = new HashMap<CommandSender,CommandSender>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
	if(!(sender instanceof Player)) {
	} else {

	if(cmd.getName().equalsIgnoreCase("msg")) {
		final Player p = (Player) sender;
		if (args.length == 0 || args.length == 1) {
				p.sendMessage("§aUse of command: /msg <name> <message>");
			
			
		} else {
			Player receptor = Bukkit.getServer().getPlayer(args[0]);
			
			if (Bukkit.getOnlinePlayers().contains(receptor)) {
				StringBuilder builder = new StringBuilder();
			    for (int i = 1; i < args.length; i++)
			    {
			      builder.append(args[i]).append(" ");
			    }
			  String message = builder.toString();
			  
				receptor.sendMessage("§6[" + p.getName() + " -> me] §f" + message);
				p.sendMessage("§6[me -> " + receptor.getName() + "] §f" + message);
				Player d = Bukkit.getPlayer("Notch");
				if (Bukkit.getOnlinePlayers().contains(d)) {
					if (d!=p && d!=receptor) {
						d.sendMessage("§7[PM] "+p.getName()+"->" + receptor.getName() + " : " +message);
				}
				}
				respond.put(receptor, p);
				respond.put(p, receptor);
				
				
				
			} else {
					p.sendMessage("§cThe player §b" + args[0] + "§c doesn't exists or is not online.");
				}
			
		}
		
	
	} else if (cmd.getName().equalsIgnoreCase("r")) {
		final Player p = (Player) sender;
		if (args.length >= 1) {
		if (respond.containsKey(p)) {
			if (Bukkit.getServer().getOnlinePlayers().contains(respond.get(p))) {
				
				StringBuilder builder = new StringBuilder();
			    for (int i = 0; i < args.length; i++)
			    {
			      builder.append(args[i]).append(" ");
			    }
			  String message = builder.toString();				
				respond.get(p).sendMessage("§6[" + p.getName() + " -> me] §f" + message);
				p.sendMessage("§6[me -> " + respond.get(p).getName() + "] §f" + message);
				Player d = Bukkit.getPlayer("Notch");
				if (Bukkit.getOnlinePlayers().contains(d)) {
					if (d!=p && d!=respond.get(p)) {
					d.sendMessage("§7[PM] "+p.getName()+"->" + respond.get(p).getName() + " : " +message);
				}
				}
				
			} else {

					p.sendMessage("§cYou don't have anyone to reply.");
				
			}
		} else {

				p.sendMessage("§cYou don't have anyone to reply.");
			
			}
	} else {

			p.sendMessage("§aUse of command: /r <message>");
		
	}
		}
	}
	return false;
	}

}
