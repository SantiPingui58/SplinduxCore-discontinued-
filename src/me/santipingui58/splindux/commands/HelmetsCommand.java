package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.gadgets.helmets.HelmetsMenu;

public class HelmetsCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("helmets")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
            Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				 new HelmetsMenu(sp).o(p);
				 
			}
		
}
			return false;
			
		}
}