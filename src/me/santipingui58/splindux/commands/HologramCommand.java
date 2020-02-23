package me.santipingui58.splindux.commands;


import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.scoreboard.hologram.Hologram;



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
					p.sendMessage("§aUse of command: /hologram create <type>");
					p.sendMessage("§aUse of command: /hologram delete");
					p.sendMessage("§aUse of command: /hologram list");
				} else {
					if (args[0].equalsIgnoreCase("create")) {
						UUID uuid = UUID.randomUUID();
						
						Hologram hologram = new Hologram(uuid, null, null);
						    hologram.getLocation();
						    sp.get1vs1Games();
					}
				}
				}
		}
		}
		return false;
	}

}
