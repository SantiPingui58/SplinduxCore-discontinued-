package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;



public class ForceDecayCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forcedecay")){
				final Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (args.length==0 && p.hasPermission("splindux.staff")) {
					
					if (sp.getSpleefArenaSpectating()!=null || sp.isInGame() || sp.isInArena()) {
						Arena arena = null;
						if(sp.getSpleefArenaSpectating()!=null) {
							arena = sp.getSpleefArenaSpectating();
						} else {
							arena = sp.getArena();
						}
						
						if (arena.getGameType().equals(GameType.DUEL)) {
							arena.setDecayRound(arena.getDecayRound()+1);
							for (SpleefPlayer viewers : arena.getViewers()) viewers.getPlayer().sendMessage("§cA Staff has forced decay.");
						} 
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				} 

	
	  
	
}
		return false;
	}
	
	

}