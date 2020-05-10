package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.FFAEvent;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.timelimit.TimeLimitManager;
import me.santipingui58.splindux.timelimit.TimeLimitType;



public class FFAEventCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("ffaevent")){
				final Player p = (Player) sender;
				 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				 if (!p.hasPermission("splindux.epic") && !p.hasPermission("splindux.extreme")) {
					 p.sendMessage("§cYou don't have permission to execute this command.");
						p.sendMessage("§aYou need atleast a rank "
								+ "§1§l[Epic] §ato use this, visit the store for more info: §bhttp://store.splindux.net/");
						return false;
				 }
				if (args.length==0) {
					if (p.hasPermission("splindux.admin")) {
					sender.sendMessage("§aUse of command: /ffaevent start <Rounds> <Prizes? true/false>");
					} else if (p.hasPermission("splindux.epic") || p.hasPermission("splindux.extreme")){
						sender.sendMessage("§aUse of command: /ffaevent start <Rounds> ");
					} else {		
					}
				} else if (args[0].equalsIgnoreCase("start")) {
					if (args.length>=2) {
					
						if (sp.isInArena()) {
							SpleefArena arena = sp.getArena();
							if (arena.getGameType().equals(GameType.FFA)) {
								if (arena.getState().equals(GameState.LOBBY) || arena.getState().equals(GameState.STARTING)) {
									
									if (p.hasPermission("splindux.admin")) {
										if (args.length==3) {
								FFAEvent event = new FFAEvent(Integer.valueOf(args[1]),Boolean.getBoolean(args[2]));
								arena.setEvent(event);
								event.sendBroadcast();
										} else {
											sender.sendMessage("§aUse of command: /ffaevent start <Rounds> <Prizes? true/false>");
										}
									} else if (p.hasPermission("splindux.extreme") || p.hasPermission("splindux.epic")) {
										Integer rounds = 0;
										try {
											rounds = Integer.valueOf(args[1]);
											if (rounds>10 && !p.hasPermission("splindux.extreme")) {
												if (rounds>15) {
													p.sendMessage("§cYou can't host ffa events larger than 15 rounds.");	
												} else {
												p.sendMessage("§aYou need atleast a rank "
														+ "§1§l[Extreme] §ato have more than 10 rounds, visit the store for more info: §bhttp://store.splindux.net/");
												return false;
											} 
											}
										} catch(Exception e) {
											p.sendMessage("§a"+ args[1]+ " §cisnt a valid number.");
											return false;
										}
										
										FFAEvent event = new FFAEvent(Integer.valueOf(args[1]),false);
										if (p.hasPermission("splindux.extreme")) {
										TimeLimitManager.getManager().addTimeLimit(sp, 1140, TimeLimitType.FFAEVENTDELAY);
										} else if (p.hasPermission("splindux.epic")) {
											TimeLimitManager.getManager().addTimeLimit(sp, 4320, TimeLimitType.FFAEVENTDELAY);
										}
										arena.setEvent(event);
										event.sendBroadcast();
									} else {
										 p.sendMessage("§cYou do not have permission to execute this command.");
							                p.sendMessage("§aYou need a §1§l[Epic] §aRank or higher to use this, visit the store for more info: §bhttp://store.splindux.net/");	
									}
								} else {
									p.sendMessage("§cThe game is already started, the game should be finishing or not started to do this.");	
								}
							} else {
								p.sendMessage("§cYou need to be in a FFA game to execute this command.");	
							}
						} else {
							p.sendMessage("§cYou need to be in a FFA game to execute this command.");	
						}
					
				}
				}

	
	  
	
}
		return false;
	}
	
	

}