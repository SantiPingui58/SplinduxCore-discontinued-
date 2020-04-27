package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.FFAEvent;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;



public class FFAEventCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("ffaevent")){
				final Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (args.length==0) {
					
					
				} else if (args[0].equalsIgnoreCase("start")) {
					if (p.hasPermission("splindux.admin")) {
						if (sp.isInArena()) {
							SpleefArena arena = sp.getArena();
							if (arena.getGameType().equals(GameType.FFA)) {
								FFAEvent event = new FFAEvent(Integer.valueOf(args[1]),true);
								arena.setEvent(event);
								event.sendBroadcast();
							}
						} else {
							
						}
					}
				}

	
	  
	
}
		return false;
	}
	
	

}