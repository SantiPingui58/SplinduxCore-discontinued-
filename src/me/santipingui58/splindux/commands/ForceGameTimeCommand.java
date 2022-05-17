package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;



public class ForceGameTimeCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forcegametime")){
				final Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (p.hasPermission("splindux.admin")) {
					if (sp.getSpleefArenaSpectating()!=null || sp.isInGame() || sp.isInArena()) {
						Arena arena = null;
						if(sp.getSpleefArenaSpectating()!=null) {
							arena = sp.getSpleefArenaSpectating();
						} else {
							arena = sp.getArena();
						}
						
						int crumble = 0;

						try {
							crumble = Integer.parseInt(args[0]);
								
								arena.setTotalTime(crumble);
								
								if (arena.getTotalTime()>=420) {
									if (arena.getTotalTime()>=1070) {
										arena.setDecayRound(4);
									} else if (arena.getTotalTime()>=900) {
										arena.setDecayRound(3);
									}  else if (arena.getTotalTime()>=750) {
										arena.setDecayRound(2);
									}  else if (arena.getTotalTime()>=600) {
										arena.setDecayRound(1);
									} 
									
									arena.decay(true);
								}
								
								for (SpleefPlayer viewers : arena.getViewers()) viewers.getPlayer().sendMessage("§cA Staff has changed the Arena Total Time.");
							
						} catch (Exception e) {
							p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
							return false;
						}
						
						
						
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				} 

	
	  
	
}
		return false;
	}
	
	

}