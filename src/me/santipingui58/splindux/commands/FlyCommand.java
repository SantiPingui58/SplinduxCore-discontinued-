package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.santipingui58.splindux.game.SpleefPlayer;

public class FlyCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("fly")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
			Player p = (Player) sender;
			if (p.hasPermission("splindux.fly")) {
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				 if (!sp.isInGame()) {
				if (sp.isFlying()) {
					sp.stopfly();
					p.sendMessage("�cFly is now disabled!");
				} else {
					if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
					if (sp.getPlayer().getLocation().getY()<115 && !sp.getPlayer().hasPermission("splindux.admin")) {
						 p.sendMessage("�cFly is not allowed here.");		
						 return false;
		    			}
					}
					
					sp.fly();			
					p.sendMessage("�aFly is now enabled!");
				}
				 } else {
					 p.sendMessage("�cYou can not execute this command here.");		
				 }
			} else {
                p.sendMessage("�cYou do not have permission to execute this command.");
                p.sendMessage("§aYou need a §1§l[Epic] §aRank or higher to use this, visit the store for more info: §bhttp://store.splindux.net/");	
			}
			}
		
}
			return false;
			
		}
}