package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;



public class HoverCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("hover")){
			Player p = (Player) sender;
			 SpleefPlayer sp1 = SpleefPlayer.getSpleefPlayer(p);
			if (args[0].equalsIgnoreCase("accept")) {
				
				Player p2 = Bukkit.getPlayer(args[1]);
				if (Bukkit.getOnlinePlayers().contains(p2)) {
					 SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(p2);
					if (sp2.isDueled(sp1)) {
						if (!GameManager.getManager().isInGame(sp1)) {
							if (!GameManager.getManager().isInGame(sp2)) {
								GameManager.getManager()._1vs1(sp2, sp1, sp2.getDuelByPlayer(sp1).getArena(),sp2.getDuelByPlayer(sp1).getType());
							} else {
								p.sendMessage("§cThis player is already in game!");	
							}
						} else {
							p.sendMessage("§cYou can not execute this command here.");	
						}
					} else {
						p.sendMessage("§cThis duel request has expired or the player didn't send you a duel request!");
					}
				} else {
					p.sendMessage("§cThis duel request has ended since the player isnt online!");
				}
			} else if (args[0].equalsIgnoreCase("deny")) {
				Player p2 = Bukkit.getPlayer(args[1]);
				if (Bukkit.getOnlinePlayers().contains(p2)) {
					 SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(p2);
					if (sp2.isDueled(sp1)) {
						sp1.getPlayer().sendMessage("§cYou have denied the duel request from §b" + sp2.getPlayer().getName() + "§c!");
						sp2.getPlayer().sendMessage("§cThe player §b" + sp1.getPlayer().getName() + "§c has denied your duel request.");
						sp2.getDueledPlayers().remove(sp2.getDuelByPlayer(sp1));
					} else {
						p.sendMessage("§cThis duel request has expired or the player didn't send you a duel request!");
					}
					} else {
						p.sendMessage("§cThis duel request has ended since the player isnt online!");
					}
			}
			}
			

}
		
		
		return false;
	}
	


	
}