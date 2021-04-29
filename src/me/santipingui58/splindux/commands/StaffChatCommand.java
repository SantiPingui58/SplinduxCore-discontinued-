package me.santipingui58.splindux.commands;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.translate.Language;
import me.santipingui58.translate.TranslateAPI;





public class StaffChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("staffchat")){
			new BukkitRunnable() {
				public void run() {
			List<Player> list = new ArrayList<Player>();
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online.hasPermission("splindux.staff")) {
					list.add(online);
					}
			}
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			StringBuilder builder = new StringBuilder();
		    for (int i = 0; i < args.length; i++)
		    {
		      builder.append(args[i]).append(" ");
		    }
		  String message = builder.toString();
		  
		  
		  List<Player> english = new ArrayList<Player>();
		  List<Player> spanish = new ArrayList<Player>();
		  List<Player> russian = new ArrayList<Player>();
		  List<Player> withoutTranslate = new ArrayList<Player>();
		  
		  for (Player players : list) {
			  SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(players);
			  if (!splayer.getOptions().getLanguage().equals(sp.getOptions().getLanguage()) && splayer.getOptions().hasTranslate()) {
				 
				  switch(splayer.getOptions().getLanguage()) {
				case ENGLISH:
					english.add(splayer.getPlayer());
					break;
				case RUSSIAN:
					russian.add(splayer.getPlayer());
					break;
				case SPANISH:
					spanish.add(splayer.getPlayer());
					break;
				  }
				  
			  } else {
				  
				  withoutTranslate.add(splayer.getPlayer());
			  }
		  }
		  
		  String prefix = !p.hasPermission("splindux.staff") ? "§c[Staff] §7[User] §f" : "§c[Staff] §f";
		  
		  for (Player player : withoutTranslate) {
			  player.sendMessage(prefix + p.getName() + "§8: §e" + message);
		  }
		  
		  String englishMsg = "";
		  String spanishMsg = "";
		  String russianMsg = "";
		  
		  try {
			 englishMsg = english.size()>0 ?  TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.ENGLISH, message) : "";
		} catch (IOException e) {
		}
		  try {
			 spanishMsg = spanish.size()>0 ?  TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.SPANISH, message) : "";
		} catch (IOException e) {
		}
		  try {
			 russianMsg = russian.size()>0 ?  TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.RUSSIAN, message) : "";
		} catch (IOException e) {
		}
		  
		 for (Player pl : english) 	  pl.sendMessage(prefix + p.getName() + "§8: §e" + englishMsg);
		 for (Player pl : spanish) pl.sendMessage(prefix + p.getName() + "§8: §e" + spanishMsg);
		 for (Player pl : russian) pl.sendMessage(prefix + p.getName() + "§8: §e" + russianMsg);
		  
				}
		
	}.runTaskAsynchronously(Main.get());

		}

		}
		return false;
}

}