package me.santipingui58.splindux.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.npc.NPCManager;


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
				if (args[0].equalsIgnoreCase("resetweekly")) {
					p.sendMessage(ChatColor.GREEN+"week reset");
					for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
						Main.data.getConfig().set("players."+s+".stats.weekly.FFA_kills", 0);
						Main.data.getConfig().set("players."+s+".stats.weekly.FFA_games", 0);
						Main.data.getConfig().set("players."+s+".stats.weekly.FFA_wins", 0);
					}
					Main.data.saveConfig();
					for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
						sp.setWeeklyFFAGames(0);
						sp.setWeeklyFFAKills(0);
						sp.setWeeklyFFAWins(0);
					}
				} else if (args[0].equalsIgnoreCase("resetmonthly")) {
					p.sendMessage(ChatColor.GREEN+"week reset");
					for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
						Main.data.getConfig().set("players."+s+".stats.monthly.FFA_kills", 0);
						Main.data.getConfig().set("players."+s+".stats.monthly.FFA_games", 0);
						Main.data.getConfig().set("players."+s+".stats.monthly.FFA_wins", 0);
					}
					Main.data.saveConfig();
					for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
						sp.setMonthlyFFAGames(0);
						sp.setMonthlyFFAKills(0);
						sp.setMonthlyFFAWins(0);
					}
				} else if (args[0].equalsIgnoreCase("tp")) {
					World world = Bukkit.getWorld(args[1]);
					p.teleport(world.getSpawnLocation());
				} else if (args[0].equalsIgnoreCase("test")) {
					for (Hologram h : HologramsAPI.getHolograms(Main.get())) {
						h.delete();
					}
					NPCManager.getManager().updateNPCs();
				} else if (args[0].equalsIgnoreCase("pvp")) {
					Main.pvp = !Main.pvp;
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