package me.santipingui58.splindux.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class RideCommand implements CommandExecutor {

	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("ride")){
			final Player p = (Player) sender;
			
			if (p.hasPermission("splindux.ride")) {
				if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
					SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (!sp.isInGame() && !sp.isSpectating()) {
			if (args.length == 1) {
				final Player pa = Bukkit.getServer().getPlayer(args[0]);
				
				if (Bukkit.getOnlinePlayers().contains(pa)) {
					if (!pa.equals(p)) {
						SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
						if (!sp2.isInGame() && !sp2.isSpectating()) {						
							pa.setPassenger(p);
						} else {
								p.sendMessage("§cYou cannot ride this player right now.");							
						}
					} else {
							p.sendMessage("§cYou cannot ride yourself..");						
					}
				} else {
						p.sendMessage("§cThe player §b" + args[0] + "§cdoesn't exists or is not online.");
					
				}
			}
			} else {
					p.sendMessage("§cYou can't execute this command while playing a match.");				
			}
		} else {
				p.sendMessage("§cYou can't execute this command in spectator mode.");
			
		}
				} else {
				p.sendMessage("§cYou don't have permission to execute this command.");
				p.sendMessage("§aYou need a rank "
						+ "§5§l[Extreme] §ato use this, visit the store for more info: §bhttp://store.splindux.net/");
			
			
		}
			}
		}
	
	return false;
}
}