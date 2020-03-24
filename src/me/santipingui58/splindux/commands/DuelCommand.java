package me.santipingui58.splindux.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.DuelMenu;



public class DuelCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
} else if(cmd.getName().equalsIgnoreCase("duel")){
	final Player p = (Player) sender;
	 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	
	if (args.length<2) {
		sender.sendMessage("§aUse of command: /duel <Player> Spleef/Splegg");
	} else {
		SpleefType type = null;
		if (args[1].equalsIgnoreCase("Spleef")) {
			type = SpleefType.SPLEEF1VS1;
		} else if (args[1].equalsIgnoreCase("Splegg")) {
			type = SpleefType.SPLEGG1VS1;
		} else {
			sender.sendMessage("§aUse of command: /duel <Player> Spleef/Splegg");
			return false;
		}
		
		Player op = Bukkit.getPlayer(args[0]);
		if (!op.hasPlayedBefore()) {
			sender.sendMessage("§cThe player §b" + args[0] + "§c does not exist or is not online.");
		}
		if (SpleefPlayer.getSpleefPlayer(op)==null) {
			sender.sendMessage("§cThe player §b" + args[0] + "§c does not exist or is not online.");
			return false;
		}
		
		 SpleefPlayer dueled = SpleefPlayer.getSpleefPlayer(op);
		if (Bukkit.getOnlinePlayers().contains(op)) {
			if (op!=p) {
				if (!sp.isDueled(dueled)) {
					
					if (!GameManager.getManager().isInGame(dueled)) {
			new DuelMenu(sp,dueled,type).o(p);
					} else {
						sender.sendMessage("§cThis player is already in game.");
					}
			
				
			
				} else {
					sender.sendMessage("§cYou have already sent a duel request to this player!");
				}
		} 	else {
			sender.sendMessage("§cYou cant duel yourself...");
		}
		} else {
			sender.sendMessage("§cThe player §b" + args[0] + "§c does not exist or is not online.");
		}
	}
}
		return false;
	}
	
	

}