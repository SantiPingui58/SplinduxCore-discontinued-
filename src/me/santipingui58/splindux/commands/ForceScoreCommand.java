package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;



public class ForceScoreCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forcescore")){
				final Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (args.length==2 && p.hasPermission("splindux.staff")) {
					
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
								crumble = Integer.parseInt(args[1]);
								if (crumble>=0 && crumble<=arena.getPlayTo()) {
									
									for (SpleefPlayer spo : arena.getPlayers()) {
										if (spo.getOfflinePlayer().getName().equalsIgnoreCase(args[0])) {
											
											if (arena.getDuelPlayers1().contains(spo)) {
											arena.setPoints1(crumble);
											
											} else if (arena.getDuelPlayers2().contains(spo)) {
												arena.setPoints2(crumble);
											}
											for (SpleefPlayer viewers : arena.getViewers()) viewers.getPlayer().sendMessage("§cA Staff has changed the score of the game.");
											break;	
										}
									}			
								} else {
										p.sendMessage("§cThe score must be a number between 0 and " + arena.getPlayTo() + ".");
										return false;
									}
								
							} catch (Exception e) {
								p.sendMessage("§a"+ args[1]+ " §cisnt a valid number.");
								return false;
							}

						} else { 
							p.sendMessage("§cYou can only execute this command in Duels");	
						}
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				}  else {
					p.sendMessage("/forcescore <player> <score>");
				}

	
	  
	
}
		return false;
	}
	
	

}