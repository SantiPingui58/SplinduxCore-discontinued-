package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.scoreboard.hologram.HologramManager;
import me.santipingui58.splindux.scoreboard.hologram.HologramType;



public class HologramCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("hologram")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (p.isOp()) {
					if (args.length==0) {
					p.sendMessage("§aUse of command: /hologram create");
					p.sendMessage("§aUse of command: /hologram delete");
					p.sendMessage("§aUse of command: /hologram list");
				} else if (args[0].equalsIgnoreCase("create")) {
						if (args[1].equalsIgnoreCase("spleefranking")) {
						HologramManager.getManager().createHologram(sp,HologramType.SPLEEFRANKING); 
						} else if (args[1].equalsIgnoreCase("onlinetime")) {
							HologramManager.getManager().createHologram(sp,HologramType.ONLINETIME); 
						}
						
						p.sendMessage("§aHologram created!");
					
				} else if (args[0].equalsIgnoreCase("delete")) {
					HologramManager.getManager().deleteHologram(sp);
					p.sendMessage("§cHologram deleted!");
				} 
					
				}
		}
		}
		return false;
	}

}
