package me.santipingui58.splindux.commands;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.translate.Language;
import me.santipingui58.translate.TranslateAPI;





public class PartyChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("partychat")) {
			new BukkitRunnable() {
				public void run() {

			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			Party party = PartyManager.getManager().getParty(p);
			if (party==null) {
				sender.sendMessage("§cYou need to be in a Party to do that.");
				return;
			}
			
			
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
		  	
		  for (UUID players : party.getAllMembers()) {		  
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
		  
		  for (Player player : withoutTranslate) {
			  player.sendMessage("§9[Party] §f" + p.getName() + "§8: §e" + message);
		  }

			try {
				TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.ENGLISH, message)
				  .thenAccept(text -> { 
					  for (Player pl : english) {
					  pl.sendMessage("§9[Party] §f" + p.getName() + "§8: §e" + text);
					  }
				  });
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.SPANISH, message)
				  .thenAccept(text -> { 
					  for (Player pl : spanish) {
					  pl.sendMessage("§9[Party] §f" + p.getName() + "§8: §e" + text);
					  }
				  });
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			try {
				TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.RUSSIAN, message)
				  .thenAccept(text -> { 
					  for (Player pl : russian) {
					  pl.sendMessage("§9[Party] §f" + p.getName() + "§8: §e" + text);
					  }
				  });
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
			}.runTaskAsynchronously(Main.get());
		}


		}
		return false;
}

}