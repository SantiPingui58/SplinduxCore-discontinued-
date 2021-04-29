package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.level.LevelManager;

public class LevelCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("level")) {
				CommandSender p = sender;
			if (p.hasPermission("splindux.admin")) {
				if (args.length==0) {
					p.sendMessage("§aUse of command: /level add <player> <amount>");
					p.sendMessage("§aUse of command: /level set <player> <amount>");
				} else if (args[0].equalsIgnoreCase("add")) {
					new BukkitRunnable() {
						public void run() {
					if (args.length==3) {
						@SuppressWarnings("deprecation")
						OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
												
						if (!pa.hasPlayedBefore()) p.sendMessage("§cPlayer not found.");
							
						
						SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
						if (temp==null) {
							 new SpleefPlayer(pa.getUniqueId());
							HikariAPI.getManager().loadData(pa.getUniqueId());						
						}
						new BukkitRunnable() {
							public void run() {	
								SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(pa);
								int level = 0;
								try {
									level = Integer.parseInt(args[2]);
								} catch (Exception e) {
									p.sendMessage("§a"+ args[3]+ " §cisnt a valid number.");
								}
								
								LevelManager.getManager().addLevel(splayer, level);
					}
							}.runTaskLater(Main.get(), 2L);

					} else {
						p.sendMessage("§aUse of command: /level add <player>");
					}
						}
				}.runTaskAsynchronously(Main.get());
				}
			}
			
}
			return false;
			
		}
}