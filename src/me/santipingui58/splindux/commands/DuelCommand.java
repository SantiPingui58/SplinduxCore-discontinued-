package me.santipingui58.splindux.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.DuelMenu;
import me.santipingui58.splindux.utils.Utils;



public class DuelCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
} else if(cmd.getName().equalsIgnoreCase("duel")){
	final Player p = (Player) sender;
	 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	if (args.length==0) {
		sender.sendMessage("§aUse of command: /duel Spleef/Splegg <Players>");
	} else {
		SpleefType type = null;
		if (args[0].equalsIgnoreCase("Spleef")) {
			type = SpleefType.SPLEEF;
		} else if (args[0].equalsIgnoreCase("Splegg")) {
			type = SpleefType.SPLEGG;
		} else {
			sender.sendMessage("§aUse of command: /duel Spleef/Splegg <Players>");
			return false;
		} 
		
		if (args.length<2) {
			sender.sendMessage("§aUse of command: /duel Spleef/Splegg <Players>");
			return false;
		}
		
		StringBuilder builder = new StringBuilder();
	    for (int i = 1; i < args.length; i++)
	    {
	      builder.append(args[i]).append(" ");
	    }
	    
	  String message = builder.toString();
	  List<String> list = new ArrayList<String>(Arrays.asList(message.split(" ")));
		List<SpleefPlayer> sp2 = new ArrayList<SpleefPlayer>();
		if ((list.size()+1)%2!=0) {
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
			  if (!op.equals(p)) {
				  players.add(op.getName());
					 SpleefPlayer dueled = SpleefPlayer.getSpleefPlayer(op);
				  if (!sp.hasDueled(dueled)) {
					  if (!dueled.isInGame()) {
						  sp2.add(dueled);
				  } else {
						sender.sendMessage("§cThis player §b"+ op.getName() +" is already in game.");
						return false;
					}
			  } else {
					sender.sendMessage("§cYou have already sent a duel request to this player!");
					  return false;
				}
			  } else {
				  sender.sendMessage("§cYou cant duel yourself...");
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
	  
	  new DuelMenu(sp,sp2,type).o(p);
	  
	}
}
		return false;
	}
	
	

}