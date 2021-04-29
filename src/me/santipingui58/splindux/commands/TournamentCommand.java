package me.santipingui58.splindux.commands;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.ranking.RankingManager;
import me.santipingui58.splindux.utils.Utils;

public class TournamentCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("tournament")) {
				CommandSender p = sender;
			if (p.hasPermission("splindux.admin")) {
				if (args.length==0) {
					p.sendMessage("§aUse of command: /tournament ffaevent <edition> <players>");
					p.sendMessage("§aUse of command: /tournament weekly <edition> <league> <players> ");
					p.sendMessage("§aUse of command: /tournament official <name> <teams?> <players> ");
				} else {
					
				
					String name = "";
					int initialArg = 0;
					boolean teams = false;
					boolean weekly =false;
					String league = "";
					boolean ffa = false;
					switch(args[0].toLowerCase()) {
					case "ffaevent": ffa = true; teams = false;  weekly = true; initialArg = 2;name = "FFA_Event_"+args[1]; break;
					case "weekly": ffa = false; teams = false; weekly = true; initialArg = 3; name = "Weekly_Tournament_" + args[1]+"_"+args[2]; league = args[2]; break;
					case "official": ffa = false; teams = Boolean.parseBoolean(args[2]); initialArg=3; name = args[1]; break;
					}
					
					List<SpleefPlayer> players = getPlayers(sender,args,initialArg);
					if (players==null) return false;
					RankingManager rm = RankingManager.getManager();
					rm.saveTournament(name, players, teams, weekly, ffa,league);
					sender.sendMessage("§aTournament §b" + name + " §asaved!");
				}
					
			}
			
}
			return false;
			
		}
		
		
		//ffa 2
		private List<SpleefPlayer> getPlayers(CommandSender sender,String[] args, int initialArg) {
			StringBuilder builder = new StringBuilder();
		    for (int i = initialArg; i < args.length; i++)
		    {
		      builder.append(args[i]).append(" ");
		    }
		    
		  String message = builder.toString();
		  List<String> list = new ArrayList<String>(Arrays.asList(message.split(" ")));
			List<SpleefPlayer> sp2 = new ArrayList<SpleefPlayer>();	

			if (Utils.getUtils().hasDuplicate(list)) {
				sender.sendMessage("§cYou can only duel the same player once...");
				return null;
			}
			
			for (String s : list) {
				@SuppressWarnings("deprecation")
				OfflinePlayer p = Bukkit.getOfflinePlayer(s);
				
				if (!p.hasPlayedBefore()) {
					sender.sendMessage("§cPlayer §b" + s + "§c does not exist.");
					return null;
				}
					
				
				SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(p);
				if (temp==null) {
					 new SpleefPlayer(p.getUniqueId());
					HikariAPI.getManager().loadData(p.getUniqueId());
				}
				
						if (p.hasPlayedBefore()) {
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
							sp2.add(sp);
						} else {
							sender.sendMessage("§cPlayer §b" + s + "§c does not exist.");
						}

				
				
			}
			
			return sp2;
		}
}