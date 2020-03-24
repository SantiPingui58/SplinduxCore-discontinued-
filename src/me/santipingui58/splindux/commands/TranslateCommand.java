package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.translate.Language;


public class TranslateCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("translate")){
			final Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			
			if (args.length==0) {
				help(p);
			} else if (args[0].equalsIgnoreCase("setlang") && args.length==2) {
				if (args[1].equalsIgnoreCase("SPANISH") || args[1].equalsIgnoreCase("ENGLISH")) {
					Language l = Language.valueOf(args[1]);
					sp.getOptions().setLanguage(l);
					p.sendMessage("§aLanguage set to: §b" + sp.getOptions().getLanguage().toString()+"§a!");
				} else {
					help(p);
				}
			} else if (args[0].equalsIgnoreCase("translate") && args.length==1) {
				sp.getOptions().translate(!sp.getOptions().hasTranslate());
				p.sendMessage("§aAutomatic translate set to: §b" + sp.getOptions().hasTranslate()+"§a!");
			} else {
				help(p);
			}
			}
			

}
		
		
		return false;
	}
	
	

	private void help(Player p) {
		p.sendMessage("§aUse of command: /translate setlang <SPANISH/ENGLISH>");
		p.sendMessage("§aUse of command: /translate translate");
	}
	
}