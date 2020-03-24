package me.santipingui58.splindux.replay;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;

public class ReplayManager {

	private static ReplayManager manager;	
	 public static ReplayManager getManager() {
	        if (manager == null)
	        	manager = new ReplayManager();
	        return manager;
	    }
	 
	public String getName(SpleefArena arena) {
		 Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		String string = format.format(now);
		return string + "_" + arena.getPlayers().get(0).getOfflinePlayer().getName()+"_vs_" + arena.getPlayers().get(1).getOfflinePlayer().getName();
	}
	
	
	
	public GameReplay createReplay(String name) {
		GameReplay replay = new GameReplay(name);
		DataManager.getManager().getReplays().add(replay);
		return replay;
	}
}
