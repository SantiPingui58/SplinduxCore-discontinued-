package me.santipingui58.splindux.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Request;
import me.santipingui58.splindux.game.spleef.RequestType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;



public class CrumbleCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("crumble")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			
			if (sp.isInGame()) {
				SpleefArena arena = sp.getArena();
				if (arena.getGameType().equals(GameType.DUEL)) {
					if (arena.getCrumbleRequest()==null) {
					if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
					if (args.length==0) {
						p.sendMessage("§aUse of command: /crumble <percentage>");
						p.sendMessage("§7(The percentage must be between 10 and 90, the higher will be the less snow in the arena)");
					} else {
						int crumble = 0;
						try {
							crumble = Integer.parseInt(args[0]);
							if (crumble>=10 && crumble<=90) {
								
								Request request = new Request(sp, crumble,RequestType.CRUMBLE);
								arena.setCrumbleRequest(request);
								request.sendMessage();
							} else {
								p.sendMessage("§cThe percentage must be a number between 10 and 90.");
								return false;
							}
						} catch (Exception e) {
							p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
							return false;
						}
							
						}
						
				} else {
					p.sendMessage("§cOnly alive players can request a /crumble.");	
				}
				} else {
					p.sendMessage("§cThere is a crumble request at the moment, cancel it or deny it to make a new one.");	
				}
				}else {
					p.sendMessage("§cYou need to be in a 1v1 game to execute this command.");	
				} 
			
							
			} else {
				p.sendMessage("§cYou can not execute this command here.");		
			}
		} 
		
}
		return false;
		
	}


	
}
