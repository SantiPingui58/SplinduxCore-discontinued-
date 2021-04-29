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
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


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
			if (sp.isInGame()) {
				Arena arena = sp.getArena(); 
				if (arena.getGameType().equals(GameType.DUEL) && arena.getSpleefType().equals(SpleefType.SPLEEF)) {
					if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
					if (!arena.getResetRequest().contains(sp)) {
						arena.getResetRequest().add(sp);
					if (arena.getResetRequest().size()>=arena.getPlayers().size()-arena.getDeadPlayers1().size()+arena.getDeadPlayers2().size()) {
						GameManager.getManager().resetArenaWithCommand(arena,false);
					} else {
						sendRequest(arena,sp);
					}
				} else {
					p.sendMessage("§cYou already sent a reset request.");	
				}
				} else {
					p.sendMessage("§cOnly alive players can request a /reset.");	
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
		
	List<SpleefPlayer> list = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
		
		if (list.isEmpty()) {
			GameManager.getManager().resetArenaWithCommand(arena,false);
		} else {
		
		for (SpleefPlayer players : arena.getViewers()) {			
			if (!players.equals(sp)) {
				players.getPlayer().sendMessage("§b"+sp.getName() + "§6 has requested a reset of the field. To accept the request do /reset §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				
				
				if (arena.getPlayers().contains(players) && !arena.getResetRequest().contains(players)) {
					if (arena.getDeadPlayers1().contains(players) || arena.getDeadPlayers2().contains(players)) continue;
				TextComponent message = new TextComponent("§bClick here to reset the arena!");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reset"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aReset arena").create()));
					players.getPlayer().spigot().sendMessage(message);
				}
				
			} else {
				players.getPlayer().sendMessage("§6You sent a reset request to your opponent.");
			}
		}
		
		}
	}
	
}
