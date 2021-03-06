package me.santipingui58.splindux.commands;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.ffa.FFAEvent;
import me.santipingui58.splindux.game.ffa.FFATeam;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;



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
								+ "§1§l[Epic] §ato use this, visit the store for more info: §bhttp://store.splindux.com/");
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
							Arena arena = sp.getArena();
							if (arena.getGameType().equals(GameType.FFA)) {
								FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
								if (arena.getState().equals(GameState.LOBBY) || arena.getState().equals(GameState.STARTING)) {
									
									if (p.hasPermission("splindux.admin")) {
										if (args.length==3) {
								FFAEvent event = new FFAEvent(arena.getSpleefType(),Integer.valueOf(args[1]),Boolean.parseBoolean(args[2]));
								ffa.setEvent(event);
								event.sendBroadcast();
										} else {
											sender.sendMessage("§aUse of command: /ffaevent start <Rounds> <Prizes? true/false>");
										}
									} else if (p.hasPermission("splindux.extreme") || p.hasPermission("splindux.epic")) {
										if (!TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, TimeLimitType.FFAEVENTDELAY)) {
										Integer rounds = 0;
										try {
											rounds = Integer.valueOf(args[1]);
											
											int maxrounds = p.hasPermission("splindux.extreme") ? 15 : p.hasPermission("splindux.epic") ? 10 : 0;
											
											if (rounds>maxrounds) {
												if (rounds>15) {
												p.sendMessage("§cYou can't host ffa events larger than 15 rounds.");	
												} else {
													p.sendMessage("§aYou need atleast a rank "
															+ "§5§l[Extreme] §ato host FFAEvents with more than 10 rounds, visit the store for more info: §bhttp://store.splindux.com/");
											
												}
												return false;
											}
											

										} catch(Exception e) {
											p.sendMessage("§a"+ args[1]+ " §cisnt a valid number.");
											return false;
										}
										if (!ffa.isInEvent()) {
										FFAEvent event = new FFAEvent(arena.getSpleefType(),Integer.valueOf(args[1]),false);
										if (p.hasPermission("splindux.extreme")) {
										TimeLimitManager.getManager().addTimeLimit(sp, 1140, TimeLimitType.FFAEVENTDELAY,null);
										} else if (p.hasPermission("splindux.epic")) {
											TimeLimitManager.getManager().addTimeLimit(sp, 4320, TimeLimitType.FFAEVENTDELAY,null);
										}
										ffa.setEvent(event);
										event.sendBroadcast();
										
										} else {
											p.sendMessage("§cThere is a FFAEvent already going on.");	
										}
									} else {
										p.sendMessage("§cYou need to wait §b"  + TimeLimitManager.getManager().getTimeLimitBy(sp, TimeLimitType.FFAEVENTDELAY).get(0).getLeftTime() +  " §cto use this command again.");	
									}
									} else {
										 p.sendMessage("§cYou do not have permission to execute this command.");
							                p.sendMessage("§aYou need a §1§l[Epic] §aRank or higher to use this, visit the store for more info: §bhttp://store.splindux.com/");	
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
					//ffaevent createteam SantiPingui58 hikarilof
				} else if (args[0].equalsIgnoreCase("createteam") && sender.isOp()) {
					@SuppressWarnings("deprecation")
					UUID p1 = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
					@SuppressWarnings("deprecation")
					UUID p2 = Bukkit.getOfflinePlayer(args[2]).getUniqueId();
					FFATeam team = new FFATeam(p1, p2);
					
					Arena arena = sp.getArena();
					FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
					if (ffa.getTeamByPlayer(p1)!=null) {
						ffa.getTeamByPlayer(p1).broadcast("§cTeam deleted");
						ffa.getTeams().remove(ffa.getTeamByPlayer(p1));
					}
					
					if (ffa.getTeamByPlayer(p2)!=null) {
						ffa.getTeamByPlayer(p2).broadcast("§cTeam deleted");
						ffa.getTeams().remove(ffa.getTeamByPlayer(p2));
					}
					
					
					ffa.getTeams().add(team);
					sp.sendMessage("§b" + team.getName() + " §ateam created");
				}else if (args[0].equalsIgnoreCase("addpoints") && sender.isOp()) {
				
					UUID p1 = Bukkit.getPlayer(args[1]).getUniqueId();
					UUID uuid = null;
					Arena arena = sp.getArena();
					FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
					FFAEvent event = ffa.getEvent();
					if (Main.ffa2v2) {
					uuid = ffa.getTeamByPlayer(p1).getUUID();
					if (event.getTotalPoints2v2().containsKey(uuid)) event.getTotalPoints2v2().put(uuid, 0);
					event.getTotalPoints2v2().put(uuid,Integer.valueOf(args[2]));
					
					sender.sendMessage("Added " + args[2] + " points to " + ffa.getTeamByPlayer(p1).getName() + " team");
					
					} else {
						uuid = p1;
						if (event.getTotalPoints().containsKey(uuid)) event.getTotalPoints().put(uuid, 0);
						event.getTotalPoints().put(uuid,Integer.valueOf(args[2]));		
						sender.sendMessage("Added " + args[2] + " points to " + Bukkit.getPlayer(p1).getName());
					}
					
					
					
				} else if (args[0].equalsIgnoreCase("forceend")) {
					SpleefPlayer spp = SpleefPlayer.getSpleefPlayer(Bukkit.getPlayer(args[1]).getUniqueId());
					Arena arena = sp.getArena();
					FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
					ffa.getPlayers().clear();
					ffa.getPlayers().add(spp);
					ffa.endGame(GameEndReason.WINNER);
				}
}
		return false;
	}
	
	

}