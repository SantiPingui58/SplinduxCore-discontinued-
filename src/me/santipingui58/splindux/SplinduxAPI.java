package me.santipingui58.splindux;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;

public class SplinduxAPI {

	private static SplinduxAPI api;	
	
	 public static SplinduxAPI getAPI() {
	        if (api == null)
	        	api = new SplinduxAPI();
	        return api;
	    }
	
	public SpleefPlayer getPlayer(Player p) {
		return SpleefPlayer.getSpleefPlayer(p);
	}
	
	public Plugin getInstance() {
		return Main.get();
	}
	
	
	public String getMatchesAsString() {
		String string = "";
		
		
		for (SpleefArena arena : DataManager.getManager().getArenas()) {
			if (arena.getGameType().equals(GameType.DUEL)) {
				 if (!arena.getDuelPlayers1().isEmpty() && !arena.getDuelPlayers2().isEmpty()) {
				String p1 = arena.getTeamName(1);
				String p2 = arena.getTeamName(2);
				int puntos1 = arena.getPoints1();
				int puntos2 = arena.getPoints2();
				int tiempo = arena.getTotalTime();
				String map = arena.getName();			
					string = string + "\n " + "§a" + p1 + " §b" + puntos1 + "§7-§b" + puntos2 + "§a " + p2 + " §7[§e" + map + "§7]" + "§7(§6" + Utils.getUtils().time(tiempo)+ "§7)";			
				}
					}
			}
				 
		if (string.equalsIgnoreCase("")) {
			return "There are not games at the moment.";
		}
		return string;
	}
	
}
