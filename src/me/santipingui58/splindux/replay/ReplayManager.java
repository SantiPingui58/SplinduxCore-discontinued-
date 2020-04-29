package me.santipingui58.splindux.replay;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class ReplayManager {

	private static ReplayManager manager;	
	 public static ReplayManager getManager() {
	        if (manager == null)
	        	manager = new ReplayManager();
	        return manager;
	    }
	 
	 
	 //Manager to handle the Replay System.
	 //Not being developed yet
	 
	public String getName(SpleefArena arena) {
		 Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		String string = format.format(now);
		return string + "_" + teamName(arena)[0]+"_vs_" + teamName(arena)[1];
	}
	
	
	public String[] teamName(SpleefArena arena) {
		String p1 = null;
		 String p2 = null;
		 for (SpleefPlayer sp1 : arena.getDuelPlayers1()) {
			if(p1==null) {
			p1 = sp1.getOfflinePlayer().getName();	
			}  else {
				p1 = "_" + sp1.getOfflinePlayer().getName();
			}
		 }
		 
		 for (SpleefPlayer sp1 : arena.getDuelPlayers2()) {
				if(p1==null) {
				p1 = sp1.getOfflinePlayer().getName();	
				}  else {
					p1 = "_" + sp1.getOfflinePlayer().getName();
				}
			 }
		 String[] r = {p1,p2};
		return r;
	}
	public GameReplay createReplay(String name) {
		GameReplay replay = new GameReplay(name);
		DataManager.getManager().getReplays().add(replay);
		
		return replay;
	}
}
