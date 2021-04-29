package me.santipingui58.splindux.commands;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.level.LevelManager;

public class TipCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
			Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("tip")) {
			
			if (args.length==0) {
				sender.sendMessage("§aUse of command: /tip <player>");
			} else {			
				SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (sp.getTippedPlayer()!=null) {
				@SuppressWarnings("deprecation")
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
				if (player.hasPlayedBefore()) {
					
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(player);
					sp.setTipPlayer(sp2.getUUID());
					sp2.addCoins(sp2, 500, true, false);
					LevelManager.getManager().addLevel(sp2, 50);
				} else {
					sender.sendMessage("§cThe player §b"+ player.getName() + " §cdoesn't exist.");
				}
				} else {
					sender.sendMessage("§cYou already tipped someone before.");
				}
			}
			}
			

}
		
		
		return false;
	}
	

	
}