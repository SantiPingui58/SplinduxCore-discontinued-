package me.santipingui58.splindux.commands;


import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.parkour.Jump;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.utils.Utils;



public class ParkourSetupCommand implements CommandExecutor {
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("parkoursetup")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
				Player p = (Player) sender;
				if (p.hasPermission("splindux.staff")) {
			if (args.length==0) {		
				p.sendMessage("/parkour setstart <jump>");
				p.sendMessage("/parkour setfinish <jump>");
				p.sendMessage("/parkour create <jump>");
				p.sendMessage("/parkour setdifficult <jump> <difficulty>");
				
			} else if (args[0].equalsIgnoreCase("setdifficulty"))  {
				if (args.length==3) {
					int i = 0;
					try {
						i = Integer.parseInt(args[2]);			
					} catch (Exception e) {
						p.sendMessage("§a"+ args[2]+ " §cisnt a valid number.");
						return false;
					}
					p.sendMessage("§aYou have set the the difficulty of Jump §b"+ args[1]);
					Main.jumps.getConfig().set("jumps."+args[1]+".difficulty", i);
					Main.jumps.saveConfig();
				}
			
			} else if (args[0].equalsIgnoreCase("setstart"))  {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of the start of Jump §b"+ args[1]);
					Main.jumps.getConfig().set("jumps."+args[1]+".start", Utils.getUtils().setLoc(p.getLocation(), false));
					Main.jumps.saveConfig();
				}
			
			}else if (args[0].equalsIgnoreCase("setfinish"))  {
				if (args.length==2) {
					p.sendMessage("§aYou have set the the location of the end of Jump §b"+ args[1]);
					Location start =Utils.getUtils().getLoc(Main.jumps.getConfig().getString("jumps."+args[1]+".start"));
					Location e = p.getLocation();
					Location end = new Location(p.getWorld(),e.getBlockX()-start.getBlockX(), e.getBlockY()-start.getBlockY(),e.getBlockZ()-start.getBlockZ());
					Main.jumps.getConfig().set("jumps."+args[1]+".finish", Utils.getUtils().setLoc(end, false));
					Main.jumps.saveConfig();
				}
			
			}else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 2) {

							if (!Main.jumps.getConfig().contains("jumps."+args[1]+".difficulty") 
									|| !Main.jumps.getConfig().contains("jumps."+args[1]+".start") 
									|| !Main.jumps.getConfig().contains("jumps."+args[1]+".finish")) {
								p.sendMessage("§cThe jump §b" + args[1] + " §cdoesnt have all locations set.");
								return false;
							}
						
							Location start =Utils.getUtils().getLoc(Main.jumps.getConfig().getString("jumps."+args[1]+".start"));
							Location finish =Utils.getUtils().getLoc(Main.jumps.getConfig().getString("jumps."+args[1]+".finish"));
							int difficulty =Main.jumps.getConfig().getInt("jumps."+args[1]+".difficulty");
							Jump jump = new Jump(args[1],difficulty,start,finish);
						ParkourManager.getManager().getJumps().add(jump);
						p.sendMessage("§aThe jump §b" + args[1] + " §ahas been created succesfully!.");
					
				} 			
			} else if (args[0].equalsIgnoreCase("test")) {
				ParkourManager.getManager().calculateProbabilities();
			}
				
		}
			}
		
}
			return false;
			
		}
}