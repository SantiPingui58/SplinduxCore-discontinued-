package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.SpleefPlayer;


public class AFKCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("afk")){
			final Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			
			if (p.hasPermission("splindux.afk")) {
				if (sp.isAfk()) {
					sp.back();
					p.sendMessage("§7You are not longer AFK");				
				} else {
					sp.afk();
					p.sendMessage("§7You are now AFK");
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