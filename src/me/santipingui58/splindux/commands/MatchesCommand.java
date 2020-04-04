package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MatchesCommand implements CommandExecutor{


	
	
	@Override
	public boolean onCommand(CommandSender p, Command cmd, String label, final String[] args) {
		if(cmd.getName().equalsIgnoreCase("matches")){
				p.sendMessage("§6-=-=-=-[§a§lMatches§6]-=-=-=-");
			p.sendMessage("§5Spleef Duels:");
			
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getGameType().equals(GameType.DUEL)) {
					 if (!arena.getDuelPlayers1().isEmpty() && !arena.getDuelPlayers2().isEmpty()) {
					String p1 = arena.getTeamName(1);
					String p2 = arena.getTeamName(2);
					int puntos1 = arena.getPoints1();
					int puntos2 = arena.getPoints2();
					int tiempo = arena.getTotalTime();
					String map = arena.getName();			
						if (p instanceof Player) {
							Player player = (Player) p;
							TextComponent message = new TextComponent("§7UNRANKED " + "§a" + p1 + " §b" + puntos1 + "§7-§b" + puntos2 + "§a " + p2 + " §7[§e" + map + "§7]" + "§7(§6" + Utils.getUtils().time(tiempo)+ "§7)");
							message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/spectate "+arena.getDuelPlayers1().get(0).getOfflinePlayer().getName()));
							message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Espectar §a" +p1 + " §7-§a " + p2 ).create()));
								player.spigot().sendMessage(message);
						
					}else {
						p.sendMessage("§7UNRANKED " + "§a" + p1 + " §b" + puntos1 + "§7-§b" + puntos2 + "§a " + p2 + " §7[§e" + map + "§7]" + "§7(§6" + Utils.getUtils().time(tiempo)+ "§7)");			
					}
						}
				}
					 
			}
			
			
			
			
		}
		
		return false;
	}
	
}