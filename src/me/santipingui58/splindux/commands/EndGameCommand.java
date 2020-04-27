package me.santipingui58.splindux.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


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
			if (sp.isInGame()) {
				SpleefArena arena =sp.getArena(); 
				if (arena.getGameType().equals(GameType.DUEL)) {
					if (!arena.getEndGameRequest().contains(sp)) {
						arena.getEndGameRequest().add(sp);
					if (arena.getEndGameRequest().size()>=arena.getPlayers().size()-arena.getDeadPlayers1().size()+arena.getDeadPlayers2().size()) {
						GameManager.getManager().endGameDuel(arena, null, GameEndReason.ENDGAME);
					} else {
						sendRequest(arena,sp);
					}
				} else {
					p.sendMessage("§cYou already sent a end game request.");	
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
		
	List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getEndGameRequest(), arena,false);
		
		if (list.isEmpty()) {
			GameManager.getManager().endGameDuel(arena, null, GameEndReason.ENDGAME);
		} else {
		
		for (SpleefPlayer players : arena.getViewers()) {			
			if (!players.equals(sp)) {
				players.getPlayer().sendMessage("§b"+sp.getOfflinePlayer().getName() + "§6 has requested to end the current game. To accept the request do /endgame §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				
				
				if (arena.getPlayers().contains(players) && !arena.getEndGameRequest().contains(players)) {
				TextComponent message = new TextComponent("§bClick here to end the current game!");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/endgame"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aEnd the current game").create()));
					players.getPlayer().spigot().sendMessage(message);
				}
				
			} else {
				players.getPlayer().sendMessage("§6You sent a end game request to your opponent.");
			}
		}
		
		}
	}
	
}
