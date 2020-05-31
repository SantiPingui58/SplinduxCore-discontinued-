package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;


public class AdCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("ad")){
			final Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			
			if (p.hasPermission("splindux.vip")) {
				
				if (args.length==0) {
					sender.sendMessage("§aUse of command: /ad on/off");
				} else {
					if (args[0].equalsIgnoreCase("on")) {
						sp.getOptions().ads(true);			
						p.sendMessage("§aAds set to: §b" + sp.getOptions().hasAds()+"§a!");
					} else if (args[0].equalsIgnoreCase("off")) {
						sp.getOptions().ads(false);
						p.sendMessage("§aAds set to: §b" + sp.getOptions().hasAds()+"§a!");
					}
				
				}
				
			} else {
					p.sendMessage("§cYou don't have permission to execute this command.");
					p.sendMessage("§aYou need a §a§l[VIP] §arank or higher to use this, visit the store for more info: §bhttp://store.splindux.net/");			
				}
			}
			

}
		
		
		return false;
	}
	
	

	
}