package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.security.SecurityManager;

public class SplinduxLoginCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("splinduxlogin")){
			final Player p = (Player) sender;
			if (p.isOp()) {
				SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (args.length==0) {
					p.sendMessage("§aUse of command: /splinduxlogin <password>");
				} else {
					String password = SecurityManager.getManager().encryptThisString(args[0]);
					if (Main.config.getConfig().getString("passwords."+p.getUniqueId()).equalsIgnoreCase(password)) {					
					p.sendMessage("§aYou have sucessfuly logged in!");
					sp.login();					
				} else {
					sp.getPlayer().kickPlayer("§4Wrong password.");
				}
				}
			}
			}
			

}
		
		
		return false;
	}
	


	
}