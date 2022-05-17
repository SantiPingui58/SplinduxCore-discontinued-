package me.santipingui58.splindux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
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
	
	public SpleefPlayer getPlayer(UUID p) {
		return SpleefPlayer.getSpleefPlayer(p);
	}
	
	public Plugin getInstance() {
		return Main.get();
	}
	

	
	public String getMatchesAsString() {
		String string = "";
		List<Arena> spleefArenas = new ArrayList<Arena>();
		List<Arena> tntRunArenas = new ArrayList<Arena>();
		List<Arena> spleggArenas = new ArrayList<Arena>();
		
		for (Arena arena : DataManager.getManager().getArenas()) {
			if (arena.getGameType().equals(GameType.DUEL)) {
				 if (!arena.getDuelPlayers1().isEmpty() && !arena.getDuelPlayers2().isEmpty()) {
			switch(arena.getSpleefType()) {
			default:break;
			case SPLEEF: spleefArenas.add(arena); break;
			case SPLEGG: spleggArenas.add(arena);break;
			case TNTRUN: tntRunArenas.add(arena);break;
			}
				 }
			}
		}
		
		
		string = string + "\n§5Spleef Duels";
		string = string + getMatches(spleefArenas);
		string = string + "\n§5TNTRun Duels";
		string = string +getMatches(tntRunArenas);
		string = string + "\n§5Splegg Duels";
		string = string +getMatches(spleggArenas);
		
		return string;
				 
	}
	
	private String getMatches(List<Arena> arenas) {
		String string = "";
		for (Arena arena : arenas) {
		String p1 = arena.getTeamName(1);
		p1 = p1.replace("_", "\\_");
		String p2 = arena.getTeamName(2);
		p2 = p2.replace("_", "\\_");
		int puntos1 = arena.getPoints1();
		int puntos2 = arena.getPoints2();
		int tiempo = arena.getTotalTime();
		String map = arena.getName();					
			string = string + "\n " + "§a" + p1 + " §b" + puntos1 + "§7-§b" + puntos2 + "§a " + p2 + " §7[§e" + map + "§7]" + "§7(§6" + Utils.getUtils().time(tiempo)+ "§7)";			
		}
	return string;
	}
	
}
