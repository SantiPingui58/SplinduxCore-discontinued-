package me.santipingui58.splindux.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.game.DuelMenu;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.utils.Utils;



public class DuelCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
} else if(cmd.getName().equalsIgnoreCase("duel")){
	
	if (DataManager.getManager().areQueuesClosed() && ! sender.hasPermission("splindux.staff")) {
		sender.sendMessage("§cQueues are currently closed.");
		return false;
	}
	final Player p = (Player) sender;
	
	Party party = PartyManager.getManager().getParty(p);
	if (party!=null && !party.isLeader(p)) {
		sender.sendMessage("§cOnly the party leader can join a game.");
		return false;
	}
	

	
	 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	 
		if (sp.isInGame() || sp.getParkourPlayer().getArena()!=null) {
			sender.sendMessage("§cYou cant use this command in game.");
			return false;
		}
		
	if (args.length==0) {
		sender.sendMessage("§aUse of command: /duel <Players>");
	} else {
		
	
		
		StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < args.length; i++)
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
		
		
		if (list.size()+1>=6 && !p.hasPermission("splindux.vip")) {
			p.sendMessage("§cYou do not have permission to duel players for a 3V3, you can use the NPCs at lobby instead.");
            p.sendMessage("§aYou need a §a§l[VIP] §aRank or higher to use this, visit the store for more info: §bhttp://store.splindux.com/");	
			return false;	
		}
		if (list.size()+1>=8 && !p.hasPermission("splindux.epic")) {
			p.sendMessage("§cYou do not have permission to duel players for a 4V4, you can use the NPCs at lobby instead.");
            p.sendMessage("§aYou need a §1§l[Epic] §aRank or higher to use this, visit the store for more info: §bhttp://store.splindux.com/");	
			return false;
			
		}
		if (list.size()+1>=10 && !p.hasPermission("splindux.extreme")) {
            p.sendMessage("§aYou need a §5§l[Extreme] §aRank or higher to use this, visit the store for more info: §bhttp://store.splindux.com/");	
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
					  if (!dueled.isInGame() && !dueled.isinParkour() && PartyManager.getManager().getParty(sp.getPlayer())==null) {
						  sp2.add(dueled);
				  } else {
						sender.sendMessage("§cThis player §b"+ dueled.getName() +" is already in game.");
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
	  
	  new DuelMenu(sp,sp2,null).o(p);
	  
	}
}
		return false;
	}
	
	

}