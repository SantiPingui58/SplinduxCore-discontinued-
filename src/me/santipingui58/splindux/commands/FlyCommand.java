package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

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
				if (sp.isFlying()) {
					sp.stopfly();
					p.sendMessage("§cFly is now disabled!");
				} else {
					sp.fly();			
					p.sendMessage("§aFly is now enabled!");
				}
				
			} else {
				p.sendMessage("§cYou do not have permission to execute this command.");
			}
			}
		
}
			return false;
			
		}
}