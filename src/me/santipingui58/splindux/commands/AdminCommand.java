package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;


public class AdminCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("admin")){
			final Player p = (Player) sender;
			if (p.hasPermission("*")) {
				if (args[0].equalsIgnoreCase("resetkills")) {
					p.sendMessage(ChatColor.GREEN+"Kills reset");
					for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
						Main.data.getConfig().set("players."+s+".stats.FFA_kills", 0);
						Main.data.getConfig().set("players."+s+".stats.monthly,FFA_kills", 0);
						Main.data.getConfig().set("players."+s+".stats.weekly.FFA_kills", 0);
						
					}
					Main.data.saveConfig();
					for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
						sp.setFFAKills(0);
						sp.setMonthlyFFAKills(0);
						sp.setWeeklyFFAKills(0);
					}
				} else if (args[0].equalsIgnoreCase("tp")) {
					World world = Bukkit.getWorld(args[1]);
					p.teleport(world.getSpawnLocation());
				} 
			}
			}
			

}
		
		
		return false;
	}
	


	
}