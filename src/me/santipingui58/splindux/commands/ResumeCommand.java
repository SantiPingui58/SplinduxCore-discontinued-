package me.santipingui58.splindux.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class ResumeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("resume")){
			Player p = (Player) sender;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (sp.isInGame()) {
				Arena arena = sp.getArena(); 
				if (arena.getGameType().equals(GameType.DUEL)) {
					
					if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
						
						if (arena.getState().equals(GameState.PAUSE)) {
							if (!arena.getResumeRequest().contains(sp)) {
								arena.getResumeRequest().add(sp);
							if (arena.getResumeRequest().size()  >=  arena.getPlayers().size()-arena.getDeadPlayers1().size()+arena.getDeadPlayers2().size()) {
								GameManager.getManager().unpauseWithCommand(arena);
							} else {
								sendRequest(arena,sp);
							}
						} else {
							arena.getResumeRequest().remove(sp);
							List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getResumeRequest(), arena,false);
							
							for (SpleefPlayer players : arena.getViewers()) {
								if (list.isEmpty()) {
									players.getPlayer().sendMessage("§b"+sp.getName()+" §chas cancelled their resume request.");
								} else {
									players.getPlayer().sendMessage("§b"+sp.getName()+" §chas cancelled their resume request §7(Left to accept: " + 
								Utils.getUtils().getPlayerNamesFromList(list) + ")");				
								}				
							}
						}
						} else {
							p.sendMessage("§cThis game is not paused");	
						}

				} else {
					p.sendMessage("§cOnly alive players can request a /resume.");	
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
		
	List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getResumeRequest(), arena,false);
		
		if (list.isEmpty()) {
			GameManager.getManager().unpauseWithCommand(arena);
		} else {
		
		for (SpleefPlayer players : arena.getViewers()) {			
			if (!players.equals(sp)) {
				players.getPlayer().sendMessage("§b"+sp.getName() + "§9 has requested to resume the match. To accept the request do /resume §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				
				
				if (arena.getPlayers().contains(players) && !arena.getResumeRequest().contains(players)) {
					if (arena.getDeadPlayers1().contains(players) || arena.getDeadPlayers2().contains(players)) continue;
				TextComponent message = new TextComponent("§bClick here to resume the arena!");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/resume"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aRresume arena").create()));
					players.getPlayer().spigot().sendMessage(message);
				}
				
			} else {
				players.getPlayer().sendMessage("§9You sent a resume request to your opponent.");
			}
		}
		
		}
	}
	
}
