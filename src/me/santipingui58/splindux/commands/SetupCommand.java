package me.santipingui58.splindux.commands;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.Utils;


public class SetupCommand implements CommandExecutor {
	
	

		@SuppressWarnings("deprecation")
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("setup")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
				Player p = (Player) sender;
				if (p.hasPermission("splindux.admin")) {
			if (args.length==0) {
				
			} else if (args[0].equalsIgnoreCase("setmainlobby"))  {
				if (args.length==1) {
					Main.arenas.getConfig().set("mainlobby", Utils.getUtils().setLoc(p.getLocation(), true));
					Main.arenas.saveConfig();
				}
			} else if (args[0].equalsIgnoreCase("setarena1")) {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of Arena #1 on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".arena1", Utils.getUtils().setLoc(p.getLocation(), false));
					Main.arenas.saveConfig();
				} else {
					
				}
			} else if (args[0].equalsIgnoreCase("setarena2")) {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of Arena #2 on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".arena2", Utils.getUtils().setLoc(p.getLocation(), false));
					Main.arenas.saveConfig();
				} else {
					
				}
			} else if (args[0].equalsIgnoreCase("setspawn1")) {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of Spawn #1 on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".spawn1", Utils.getUtils().setLoc(p.getLocation(), true));
					Main.arenas.saveConfig();
				} else {
					
				}
			} else if (args[0].equalsIgnoreCase("setspawn2")) {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of Spawn #2 on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".spawn2", Utils.getUtils().setLoc(p.getLocation(), true));
					Main.arenas.saveConfig();
				} else {
					
				}
			}  else if (args[0].equalsIgnoreCase("setlobby")) {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of Lobby on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".lobby", Utils.getUtils().setLoc(p.getLocation(), false));
					Main.arenas.saveConfig();
				} else {
					
				}
			}  else if (args[0].equalsIgnoreCase("setitem")) {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the item to §b"+p.getItemInHand().getType()+ "§a on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".item", p.getItemInHand().getType().toString());
					Main.arenas.saveConfig();
				} else {
					
				}
			} else if (args[0].equalsIgnoreCase("setmainspawn")) {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of Main Spawn on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".mainspawn", Utils.getUtils().setLoc(p.getLocation(), false));
					Main.arenas.saveConfig();
				} else {
					
				}
			}else if (args[0].equalsIgnoreCase("setspleeftype")) {
				if (args.length==3) {
					try {
						SpleefType.valueOf(args[2]);
					p.sendMessage("§aYou have set the the location of Type on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".spleeftype", args[2]);
					Main.arenas.saveConfig();
				} catch(Exception e) {
					p.sendMessage("§cThe Spleef Type §b" + args[2] + " §cdoes not exist.");
				} 
					
			} else {
				}
			} else if (args[0].equalsIgnoreCase("setgametype")) {
				if (args.length==3) {
					try {
						GameType.valueOf(args[2]);
					p.sendMessage("§aYou have set the the location of Type on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".gametype", args[2]);
					Main.arenas.saveConfig();
				} catch(Exception e) {
					p.sendMessage("§cThe Game Type §b" + args[2] + " §cdoes not exist.");
				} 
					
			} else {
				}
			}else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 2) {
					if (Main.arenas.getConfig().contains("arenas."+args[1]+".arena1") && 
							Main.arenas.getConfig().contains("arenas."+args[1]+".arena2") &&
							Main.arenas.getConfig().contains("arenas."+args[1]+".item") &&
							Main.arenas.getConfig().contains("arenas."+args[1]+".gametype") &&
							Main.arenas.getConfig().contains("arenas."+args[1]+".spleeftype")) {
						
						Location arena1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".arena1"));
						Location arena2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".arena2"));		
						Material item = Material.valueOf(Main.arenas.getConfig().getString("arenas."+args[1]+".item"));
						SpleefType spleeftype = SpleefType.valueOf(Main.arenas.getConfig().getString("arenas."+args[1]+".spleeftype"));
						GameType gametype = GameType.valueOf(Main.arenas.getConfig().getString("arenas."+args[1]+".gametype"));
						if (gametype.equals(GameType.FFA)) {
							Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".lobby"));
							Location mainspawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".mainspawn"));
						DataManager.getManager().loadArena(args[1], mainspawn,null,null, lobby, arena1, arena2,spleeftype,gametype,null);
						} else if (gametype.equals(GameType.DUEL)) {
							Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".spawn1"));
							Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".spawn2"));
							DataManager.getManager().loadArena(args[1], null,spawn1,spawn2, Main.lobby, arena1, arena2,spleeftype,gametype,item);
						}
						
						p.sendMessage("§aThe arena §b" + args[1] + " §ahas been created succesfully!.");
					} else {
						p.sendMessage("§cThe arena §b" + args[1] + " §cdoesnt have all locations set.");
					}
				} else {
					
				}
			}
				
		}
			}
		
}
			return false;
			
		}
}