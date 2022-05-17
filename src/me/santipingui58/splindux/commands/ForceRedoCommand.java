package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;



public class ForceRedoCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forceredo")){
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
							if (arena.getState().equals(GameState.PAUSE)) {
								sp.sendMessage("§cYou cannot execute this command while the game is paused.");
								return false;
							}
							if (arena.getPointsHistory().size()==0) {
								p.sendMessage("§cYou can not use this command if no points have been scored yet.");
								return false;
							}
							arena.redo();
							for (SpleefPlayer viewers : arena.getViewers()) viewers.getPlayer().sendMessage("§cA Staff has discarded the last point.");
						} else {
							p.sendMessage("§cYou can only execute this command in Duels");	
						}
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				} 

	
	  
	
}
		return false;
	}
	
	

}