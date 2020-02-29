package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;



public class SpectateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("spectate")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	
			if (args.length==0) {
				sender.sendMessage("§aUso of command: /spectate <Player>");
			} else {
				Player spect = Bukkit.getPlayer(args[0]);
				if (Bukkit.getOnlinePlayers().contains(spect)) {
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(spect);
					if (GameManager.getManager().isInGame(sp2)) {
						if (GameManager.getManager().getArenaByPlayer(sp2).getType().equals(SpleefType.SPLEEF1VS1)) {
						GameManager.getManager().spectate(sp,sp2);
						} else {
							sender.sendMessage("§cThe player §b" + args[0] + "§c is not in a 1vs1 game.");
						}
					} else {
						sender.sendMessage("§cThe player §b" + args[0] + "§c is not in game.");
					}
				} else {
					sender.sendMessage("§cThe player §b" + args[0] + "§c does not exist or is not online.");
				}
			}
		}
		}
		return false;
	}

}
