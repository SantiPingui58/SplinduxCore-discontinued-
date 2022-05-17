package me.santipingui58.splindux.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class RedoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("redo")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (sp.isInGame()) {
				Arena arena = sp.getArena(); 
				if (arena.getPointsHistory().size()==0) {
					p.sendMessage("§cYou can not use this command if no points have been scored yet.");
					return false;
				}
				if (arena.getGameType().equals(GameType.DUEL) && !arena.isAtMiniSpleef()) {
					
					if (!arena.getRedoRequest().contains(sp)) {
						arena.getRedoRequest().add(sp);
					if (arena.getRedoRequest().size()  >=  arena.getPlayers().size()) {
						GameManager.getManager().redoWithCommand(arena);
					} else {
						sendRequest(arena,sp);
					}
				} else {
					arena.getRedoRequest().remove(sp);
					List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getRedoRequest(), arena,true);
					
					for (SpleefPlayer players : arena.getViewers()) {
						if (list.isEmpty()) {
							players.getPlayer().sendMessage("§b"+sp.getName()+" §chas cancelled their redo request.");
						} else {
							players.getPlayer().sendMessage("§b"+sp.getName()+" §chas cancelled their redo request §7(Left to accept: " + 
						Utils.getUtils().getPlayerNamesFromList(list) + ")");				
						}				
					}
				}
				
				} else {
					p.sendMessage("§cYou can not execute this command here.");		
				}
			} else {
				p.sendMessage("§cYou need to be in a duel game to execute this command.");	
			}
		}
		}
		return false;
	}

	
	
	private void sendRequest(Arena arena,SpleefPlayer sp) {	
		
	List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getRedoRequest(), arena,true);
		
		if (list.isEmpty()) {
			GameManager.getManager().redoWithCommand(arena);
		} else {
		
		for (SpleefPlayer players : arena.getViewers()) {			
			if (!players.equals(sp)) {
				players.getPlayer().sendMessage("§b"+sp.getName() + "§9 has requested to re do the last point of the the match. To accept the request do /redo §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				
				
				if (arena.getPlayers().contains(players) && !arena.getRedoRequest().contains(players)) {
					if (arena.getDeadPlayers1().contains(players) || arena.getDeadPlayers2().contains(players)) continue;
				TextComponent message = new TextComponent("§bClick here to redo the last point!");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/redo"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aRedo the last point").create()));
					players.getPlayer().spigot().sendMessage(message);
				}
				
			} else {
				players.getPlayer().sendMessage("§9You sent a redo request to your opponent.");
			}
		}
		
		}
	}
	
}
