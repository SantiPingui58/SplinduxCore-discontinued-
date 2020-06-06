package me.santipingui58.splindux.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.npc.NPCManager;


public class AdminCommand implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
	
		
		if(cmd.getName().equalsIgnoreCase("admin")){
			final CommandSender p = sender;
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
				} else if (args[0].equalsIgnoreCase("resetholograms")) {
					for (Hologram h : HologramsAPI.getHolograms(Main.get())) {
						h.delete();
					}
					NPCManager.getManager().updateNPCs();
				} else if (args[0].equalsIgnoreCase("addmutations")) {
					for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
						Main.data.getConfig().set("players."+s+".mutation_tokens", 0);
					}
					Main.data.saveConfig();
				} else if (args[0].equalsIgnoreCase("pvp")) {
					Main.pvp = !Main.pvp;
				}  else if (args[0].equalsIgnoreCase("givemutations")) {
					DataManager.getManager().giveMutationTokens();
					p.sendMessage("Gave mutation tokens");
				}else if (args[0].equalsIgnoreCase("test")) {

				}
				
			}
			

}
		
		
		return false;
	}
	


	
}