package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;



public class ForcePlaytoCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forceplayto")){
				final Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (args.length==1 && p.hasPermission("splindux.staff")) {
					
					if (sp.getSpleefArenaSpectating()!=null || sp.isInGame() || sp.isInArena()) {
						Arena arena = null;
						if(sp.getSpleefArenaSpectating()!=null) {
							arena = sp.getSpleefArenaSpectating();
						} else {
							arena = sp.getArena();
						}
						
						if (arena.getGameType().equals(GameType.DUEL)) {
							
							int crumble = 0;

							try {
								crumble = Integer.parseInt(args[0]);
								if (crumble>=1 && crumble<=99) {
									arena.setPlayTo(crumble);
									for (SpleefPlayer viewers : arena.getViewers()) viewers.getPlayer().sendMessage("§cA Staff has changed the play to of the game.");
								} else {
										p.sendMessage("§cThe score must be a number between 1 and 99.");
										return false;
									}
								
							} catch (Exception e) {
								p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
								return false;
							}

						} else { 
							p.sendMessage("§cYou can only execute this command in Duels");	
						}
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				}  else {
					p.sendMessage("/forceplayto <playto>");
				}

	
	  
	
}
		return false;
	}
	
	

}