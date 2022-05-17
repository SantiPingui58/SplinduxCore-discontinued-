package me.santipingui58.splindux.sws;

public class MatchHistoryManager {
	
	private static MatchHistoryManager manager;	
	
	 public static MatchHistoryManager getManager() {
	        if (manager == null)
	        	manager = new MatchHistoryManager();
	        return manager;
	    }
	 
}
