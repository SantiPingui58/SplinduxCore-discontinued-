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
			}else if (args[0].equalsIgnoreCase("setmin")) {
				if (args.length==3) {
					int min = 0;
					try {
						min = Integer.parseInt(args[2]);			
					} catch (Exception e) {
						p.sendMessage("§a"+ args[2]+ " §cisnt a valid number.");
						return false;
					}
					
					p.sendMessage("§aYou have set the the min amount of players on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".min_size", min);
					Main.arenas.saveConfig();
				} else {
					
				}
			}else if (args[0].equalsIgnoreCase("setmax")) {
				if (args.length==3) {
					int max = 0;
					try {
						max = Integer.parseInt(args[2]);			
					} catch (Exception e) {
						p.sendMessage("§a"+ args[2]+ " §cisnt a valid number.");
						return false;
					}
					
					p.sendMessage("§aYou have set the the max amount of players on arena §b"+ args[1]);
					Main.arenas.getConfig().set("arenas."+args[1]+".max_size", max);
					Main.arenas.saveConfig();
				} else {
					
				}
			}    else if (args[0].equalsIgnoreCase("setlobby")) {
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
					if (Main.arenas.getConfig().contains("arenas."+args[1]+".gametype")) {
						GameType gametype = GameType.valueOf(Main.arenas.getConfig().getString("arenas."+args[1]+".gametype"));
						
						
						if (gametype.equals(GameType.DUEL)) {
							if (!Main.arenas.getConfig().contains("arenas."+args[1]+".arena1") 
								|| !Main.arenas.getConfig().contains("arenas."+args[1]+".arena2") 
								|| !Main.arenas.getConfig().contains("arenas."+args[1]+".item") 
								|| !Main.arenas.getConfig().contains("arenas."+args[1]+".spleeftype")
								|| !Main.arenas.getConfig().contains("arenas."+args[1]+".lobby")
								|| !Main.arenas.getConfig().contains("arenas."+args[1]+".min_size")
								|| !Main.arenas.getConfig().contains("arenas."+args[1]+".max_size")
								) {
								p.sendMessage("§cThe arena §b" + args[1] + " §cdoesnt have all locations set.");
								return false;
							} else {}
							
						} 
						
						
						if (gametype.equals(GameType.FFA)) {
							if (!Main.arenas.getConfig().contains("arenas."+args[1]+".arena1") 
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".arena2") 
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".gametype")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".spleeftype")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".lobby")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".mainspawn")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".min_size")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".max_size")
									){
								p.sendMessage("§cThe arena §b" + args[1] + " §cdoesnt have all locations set.");
								return false;
							}
						}
						
						
						
						if (gametype.equals(GameType.CATF)) {
							if (!Main.arenas.getConfig().contains("arenas."+args[1]+".arena1") 
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".arena2") 
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".gametype")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".spleeftype")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".lobby")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".mainspawn")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".flag1")
									|| !Main.arenas.getConfig().contains("arenas."+args[1]+".flag2")
									){
								p.sendMessage("§cThe arena §b" + args[1] + " §cdoesnt have all locations set.");
								return false;
							}
						}
						
						SpleefType spleeftype = SpleefType.valueOf(Main.arenas.getConfig().getString("arenas."+args[1]+".spleeftype"));
						Location arena1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".arena1"));
						Location arena2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".arena2"));	
						Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".lobby"));
						int min = Main.arenas.getConfig().getInt("arenas."+args[1]+".spawn1");
						int max = Main.arenas.getConfig().getInt("arenas."+args[1]+".spawn2");
						
						if (gametype.equals(GameType.FFA)) {	
							Location mainspawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".mainspawn"));
						DataManager.getManager().loadArena(args[1], mainspawn,null,null, lobby, arena1, arena2,spleeftype,gametype,null,min,max,null,null);
						} else if (gametype.equals(GameType.DUEL)) {
							Material item = Material.valueOf(Main.arenas.getConfig().getString("arenas."+args[1]+".item"));
							Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".spawn1"));
							Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".spawn2"));
					
							DataManager.getManager().loadArena(args[1], null,spawn1,spawn2,lobby, arena1, arena2,spleeftype,gametype,item,min,max,null,null);
						} else if (gametype.equals(GameType.CATF)) {
							Location flag1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".flag1"));
							Location flag2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".flag2"));
							Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".spawn1"));
							Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas."+args[1]+".spawn2"));
							Material item = Material.valueOf(Main.arenas.getConfig().getString("arenas."+args[1]+".item"));
							DataManager.getManager().loadArena(args[1], null,spawn1,spawn2,lobby, arena1, arena2,spleeftype,gametype,item,min,max,flag1,flag2);
						}
						
						p.sendMessage("§aThe arena §b" + args[1] + " §ahas been created succesfully!.");
					} else {
						p.sendMessage("§cThe arena §b" + args[1] + " §cdoesnt have all locations set.");
					}
				} else {
					p.sendMessage("§cThe arena §b" + args[1] + " §cdoesnt have all locations set.");
				}				
			}
				
		}
			}
		
}
			return false;
			
		}
}