package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.hologram.HologramType;



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
					p.sendMessage("§aUse of command: /hologram move <GAME_STATS/PLAYER_STATS>");
				} else if (args[0].equalsIgnoreCase("move")) {
						HologramType type = HologramType.valueOf(args[1].toUpperCase());
						HologramManager hm = HologramManager.getManager();
						hm.moveHologram(sp, hm.getHologram(type));
						p.sendMessage("§aHologram moved!");	
				} 	
				}
		}
		}
		return false;
	}

}
