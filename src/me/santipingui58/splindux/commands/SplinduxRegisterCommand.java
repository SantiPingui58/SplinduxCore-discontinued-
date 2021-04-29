package me.santipingui58.splindux.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.security.SecurityManager;

public class SplinduxRegisterCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("splinduxregister")){
			final Player p = (Player) sender;
			if (p.isOp()) {
				SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (args.length==0) {
					p.sendMessage("§aUse of command: /splinduxregister <password>");
				} else {
					String password = SecurityManager.getManager().encryptThisString(args[0]);
					Main.config.getConfig().set("passwords."+sp.getPlayer().getUniqueId(), password);
					p.sendMessage("§aYou have sucessfuly registered!");				
				}
			}
			}
			

}
		
		
		return false;
	}
	


	
}