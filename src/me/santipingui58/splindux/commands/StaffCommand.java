package me.santipingui58.splindux.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;




public class StaffCommand implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("staff")){
			final Player p = (Player) sender;
			if (p.hasPermission("splindux.staff")) {
				if (args[0].equalsIgnoreCase("tp")) {
					World world = Bukkit.getWorld(args[1]);
					p.teleport(world.getSpawnLocation());
				}
			} 
			
			if (p.hasPermission("splindux.builder")) {
				 if (args[0].equalsIgnoreCase("build")) {
					World world = Bukkit.getWorld("construccion");
					p.teleport(world.getSpawnLocation());
				} 
			}
			}
			

}
		
		
		return false;
	}
	


	
}