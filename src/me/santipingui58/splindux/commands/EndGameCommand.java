package me.santipingui58.splindux.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;



public class EndGameCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("endgame")){
			Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (GameManager.getManager().isInGame(sp)) {
				SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
				if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
					if (!arena.getEndGameRequest().contains(sp)) {
					if (arena.getEndGameRequest().size()>=arena.getPlayers().size()-1) {
						GameManager.getManager().endGame1vs1(arena, null, null, GameEndReason.ENDGAME);
						arena.getEndGameRequest().clear();
					} else {
						sendRequest(arena,sp);
					}
				} else {
					p.sendMessage("§cYou already sent a endgame request.");	
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
				players.getPlayer().sendMessage("§6Your opponent has requested end the current game. To accept the request do /endgame");
			} else {
				players.getPlayer().sendMessage("§6You sent a endgame request to your opponent.");
			}
		}
		
		arena.getEndGameRequest().add(sp);
	}
}
