package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefType;



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
			
			if (GameManager.getManager().isInGame(sp)) {
				SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
				if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
					if (args.length==0) {
						p.sendMessage("§aUse of command: /crumble <percentage>");
						p.sendMessage("§7(The percentage must be between 10 and 90, the higher will be the less snow in the arena)");
					} else {
						int crumble = 0;
						try {
							crumble = Integer.parseInt(args[0]);
							if (crumble>=10 && crumble<=90) {
								
							} else {
								p.sendMessage("§cThe percentage must be a number between 10 and 90.");
								return false;
							}
						} catch (Exception e) {
							p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
							return false;
						}
						
						sendRequest(arena,sp,crumble);
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


	private void sendRequest(SpleefArena g,SpleefPlayer sp,int crumble) {
		if (!g.getCrumbleRequest().containsKey(sp)) {
		g.getCrumbleRequest().put(sp, crumble);
		}
		
		if (g.getPlayers().size() <= g.getCrumbleRequest().size()) {
			
			for (Integer i : g.getCrumbleRequest().values()) {
				if (!(crumble == i)) {
					break;
				}
				
				GameManager.getManager().crumbleArena(g, crumble);
				g.getCrumbleRequest().clear();
				

				for (SpleefPlayer p2 : g.getPlayers()) {
						p2.getPlayer().sendMessage("§6 The arena has been crumbled with a percentage : §a" + crumble);
					
				}
				
				return;
				
			}
			
			
				sendMessage(g,sp,crumble);
				g.getCrumbleRequest().put(sp, crumble);
			
				
			
			
		} else {
			sendMessage(g,sp,crumble);
				
		}
	}
	
	private void sendMessage(SpleefArena arena,SpleefPlayer sp,int playto) {
		for (SpleefPlayer players : arena.getPlayers()) {
			if (!players.equals(sp)) {
				players.getPlayer().sendMessage("§6Your opponent has requested to crumble the arena with " + playto+ "%. To accept the request do /crumble " + playto);
			} else {
				players.getPlayer().sendMessage("§6You sent a crumble request to your opponent.");
			}
		}
		
	}
}
