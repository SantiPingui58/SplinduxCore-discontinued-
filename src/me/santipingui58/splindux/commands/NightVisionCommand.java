package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class NightVisionCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("nightvision")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
            Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (sp.getOptions().hasNightVision()) {
					sp.getOptions().nightVision(false);
					p.sendMessage("§cNight vision is now disabled!");
					p.removePotionEffect(PotionEffectType.NIGHT_VISION);
				} else {
					sp.getOptions().nightVision(true);		
					p.sendMessage("§aNight vision is now enabled!");
					if (sp.isInArena()) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1));
					}
				}
				 
			}
		
}
			return false;
			
		}
}