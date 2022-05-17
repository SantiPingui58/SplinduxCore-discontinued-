package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.game.BetMenu;

public class BetCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
			Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("bet")) {
			if (args.length==0) {
				sender.sendMessage("§aUse of command: /bet <amount>");
			} else {			
				SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (sp.isInArena()) {
					Arena arena = sp.getArena();
					if (arena.getGameType().equals(GameType.FFA)) {					
							if (!GameManager.getManager().getFFAArenaByArena(arena).hasBetAlready(sp)) {
					int amount = 0;

					try {
						amount = Integer.parseInt(args[0]);
					}  catch (Exception e) {
						p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
						return false;
					}
					
					
					
					if (amount <= sp.getCoins()*0.15) {
					
						
						
						new BetMenu(sp,amount).o(p);
						
				
					
					} else {
						p.sendMessage("§cYou can only bet a 15% of the total of your current coins.");
					}
						} else {
							p.sendMessage("§cYou can only bet once per round.");
						}
					
					
				
				
				} else {
					p.sendMessage("§cYou need to be in a FFA Game to do this.");
				}
			
				} else {
					p.sendMessage("§cYou need to be in a FFA Game to do this.");
				}
			}
			}
			

}
		
		
		return false;
	}
	

	
}