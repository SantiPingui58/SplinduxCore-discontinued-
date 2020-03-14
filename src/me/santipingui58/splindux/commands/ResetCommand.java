package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;



public class ResetCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("reset")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (GameManager.getManager().isInGame(sp)) {
				SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
				if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
					if (!arena.getResetRequest().contains(sp)) {
					if (arena.getResetRequest().size()>=arena.getPlayers().size()-1) {
						for (SpleefPlayer players : arena.getPlayers()) {
								players.getPlayer().sendMessage("§6The arena has been reset.");						
						}
						GameManager.getManager().arenaShrink(arena);
						arena.getPlayToRequest().clear();
						arena.getEndGameRequest().clear();
						arena.getCrumbleRequest().clear();
						arena.getResetRequest().clear();
					} else {
						sendRequest(arena,sp);
					}
				} else {
					p.sendMessage("§cYou already sent a reset request.");	
				}
				} else {
					p.sendMessage("§cYou can not execute this command here.");		
				}
			} else {
				p.sendMessage("§cYou need to be in a 1v1 game to execute this command.");	
			}
		}
		}
		return false;
	}

	
	
	private void sendRequest(SpleefArena arena,SpleefPlayer sp) {
		for (SpleefPlayer players : arena.getPlayers()) {
			if (!players.equals(sp)) {
				players.getPlayer().sendMessage("§6Your opponent has requested a reset of the field. To accept the request do /reset");
			} else {
				players.getPlayer().sendMessage("§6You sent a reset request to your opponent.");
			}
		}
		
		arena.getResetRequest().add(sp);
	}
}
