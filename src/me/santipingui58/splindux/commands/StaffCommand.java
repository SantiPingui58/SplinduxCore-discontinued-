package me.santipingui58.splindux.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.Utils;




public class StaffCommand implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
	
		if(cmd.getName().equalsIgnoreCase("staff")){

			if (sender.hasPermission("splindux.staff")) {
				if (args[0].equalsIgnoreCase("tp")) {
					Player p = (Player) sender;
					World world = Bukkit.getWorld(args[1]);
					SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
					sp.leave(false,false);
					p.teleport(world.getSpawnLocation());
				} else if (args[0].equalsIgnoreCase("duel")) {
					//staff duel <type> <players>
					
					SpleefType type = SpleefType.valueOf(args[1].toUpperCase());
					boolean tie = false;
					try {
					tie = Boolean.getBoolean(args[3]);
					} catch (Exception ex) {
						sender.sendMessage("§cThe value of tie can only be true or false.");
						return false;
					}
					
					int time = 0;
					try {
					time = Integer.valueOf(args[2]);
					}catch (Exception ex) {
						sender.sendMessage("§cThe value of time can only be numbers.");
						return false;
					}
					if (args.length<5) {
						sender.sendMessage("§aUse of command: /staff duel <type> <time in minutes> <can tie?> <players>");
						return false;
					}
					
					StringBuilder builder = new StringBuilder();
				    for (int i = 4; i < args.length; i++)
				    {
				      builder.append(args[i]).append(" ");
				    }
				    
				  String message = builder.toString();
				  List<String> list = new ArrayList<String>(Arrays.asList(message.split(" ")));
					List<SpleefPlayer> sp2 = new ArrayList<SpleefPlayer>();
					if ((list.size())%2!=0) {
						sender.sendMessage("§cYou can only duel an odd amount of players (1,3,5,7,etc.)");
						return false;
					}
					
					
					List<String> players = new ArrayList<String>();
					

					if (Utils.getUtils().hasDuplicate(list)) {
						sender.sendMessage("§cYou can only duel the same player once...");
						return false;
					}
					
					
				  for (String s : list) {
					  Player op = Bukkit.getPlayer(s);
					  if (Bukkit.getOnlinePlayers().contains(op)) {
							  players.add(op.getName());
								 SpleefPlayer dueled = SpleefPlayer.getSpleefPlayer(op);
								  if (!dueled.isInGame()) {
									  sp2.add(dueled);
							  } else {
									sender.sendMessage("§cThis player §b"+ dueled.getName() +" is already in game.");
									return false;
								}
						  
						 
					  } else {
						  sender.sendMessage("§cThe player §b" + s + "§c does not exist or is not online.");
						  return false;
					  }
				  }
				  
				  if (Utils.getUtils().hasDuplicate(players)) {
						sender.sendMessage("§cYou can only duel the same player once...");
						return false;
					}
				  
				  int size = sp2.size();
				  
				  SpleefPlayer challenger = sp2.get(0);
				  sp2.remove(challenger);
				  boolean t = tie;
				  int ti = time;
				  new BukkitRunnable() {
 
					  public void run() {
						  GameManager.getManager().duelGame(challenger, sp2,null,type,size,false,null,t,false,ti);
					  }
				  }.runTaskAsynchronously(Main.get());
				 	
				}
			} 
			
			
}
		
		
		return false;
	}
	


	
}