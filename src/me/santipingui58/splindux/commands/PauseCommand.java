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


public class PauseCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("pause")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (sp.isInGame()) {
				Arena arena = sp.getArena(); 
				if (arena.getGameType().equals(GameType.DUEL) && !arena.isAtMiniSpleef()) {
					
					if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
					if (!arena.getPauseRequest().contains(sp)) {
						arena.getPauseRequest().add(sp);
					if (arena.getPauseRequest().size()  >=  arena.getPlayers().size()-arena.getDeadPlayers1().size()+arena.getDeadPlayers2().size()) {
						GameManager.getManager().pauseWithCommand(arena);
					} else {
						sendRequest(arena,sp);
					}
				} else {
					arena.getPauseRequest().remove(sp);
					List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getPauseRequest(), arena,false);
					
					for (SpleefPlayer players : arena.getViewers()) {
						if (list.isEmpty()) {
							players.getPlayer().sendMessage("§b"+sp.getName()+" §chas cancelled their pause request.");
						} else {
							players.getPlayer().sendMessage("§b"+sp.getName()+" §chas cancelled their pause request §7(Left to accept: " + 
						Utils.getUtils().getPlayerNamesFromList(list) + ")");				
						}				
					}
				}
				} else {
					p.sendMessage("§cOnly alive players can request a /pause.");	
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
		
	List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getPauseRequest(), arena,false);
		
		if (list.isEmpty()) {
			GameManager.getManager().pauseWithCommand(arena);
		} else {
		
		for (SpleefPlayer players : arena.getViewers()) {			
			if (!players.equals(sp)) {
				players.getPlayer().sendMessage("§b"+sp.getName() + "§9 has requested to pause the match. To accept the request do /pause §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				
				
				if (arena.getPlayers().contains(players) && !arena.getPauseRequest().contains(players)) {
					if (arena.getDeadPlayers1().contains(players) || arena.getDeadPlayers2().contains(players)) continue;
				TextComponent message = new TextComponent("§bClick here to pause the arena!");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/pause"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aPause arena").create()));
					players.getPlayer().spigot().sendMessage(message);
				}
				
			} else {
				players.getPlayer().sendMessage("§9You sent a pause request to your opponent.");
			}
		}
		
		}
	}
	
}
