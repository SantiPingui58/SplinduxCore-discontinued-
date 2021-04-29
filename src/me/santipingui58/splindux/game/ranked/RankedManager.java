package me.santipingui58.splindux.game.ranked;

import java.util.ArrayList;
import java.util.List;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;


public class RankedManager {
	private static RankedManager manager;	
	 public static RankedManager getManager() {
	        if (manager == null)
	        	manager = new RankedManager();
	        return manager;
	    }
	
	private List<RankedQueue> queues = new ArrayList<RankedQueue>();
	
	
	public List<RankedQueue> getQueues() {
		return this.queues;
	}
	
	
	
	public RankedQueue getRankedQueue(SpleefType spleef, int teamSize) {
		for (RankedQueue q : this.queues) {
			if (q.getSpleefType().equals(spleef) && q.getTeamSize()==teamSize) {
				return q;
			}
		}
		return null;
	}
	
	public void loadRankedQueues() {
		for (SpleefType spleef : SpleefType.values()) {
				for (int i = 1;i<=3;i++) {
					RankedQueue queue = new RankedQueue(spleef, i);
					this.queues.add(queue);
				}
			
		}
	}
	
	
	
	public int calcualteELO(List<SpleefPlayer> winner, List<SpleefPlayer> loser, int difpuntos) {
		int p1 = 0;
		int p2 = 0;
		for (SpleefPlayer sp : winner) p1 = p1 + sp.getPlayerStats().getELO(SpleefType.SPLEEF);
		for (SpleefPlayer sp : loser) p2 = p2 + sp.getPlayerStats().getELO(SpleefType.SPLEEF);
		
		int a = winner.size();
		int b = winner.size();
		if (a==0) a = 1;
		if (b==0) b=1;
		
		p1 = p1/a;
		p2 = p2/b;
		
		int newELO = elo(p1,p2);
		
		for (SpleefPlayer sp : winner) {
			sp.getPlayerStats().setELO(SpleefType.SPLEEF,sp.getPlayerStats().getELO(SpleefType.SPLEEF)+newELO);
		}
		for (SpleefPlayer sp : winner) {
			sp.getPlayerStats().setELO(SpleefType.SPLEEF,sp.getPlayerStats().getELO(SpleefType.SPLEEF)-newELO);
		}
		
		return newELO;
	}
	
	private int elo(int elo1, int elo2) {
		int k = 48;
		  double p1 = ((double) elo1/ (double) 400); 
		  double p1_ = Math.pow(10, p1); 

		  double p2 = ((double)elo2/(double)400); 
		  double p2_ = Math.pow(10, p2);

		  double d = p1_ + p2_;

		  double ex1 = (double)p1_/(double)d;   
		  double a1 = ((double)k*(1-(double)ex1));
		  return (int) a1;
	}



	public void updateQueues() {
		for (RankedQueue queue : this.queues) {
			queue.checkQueue();
		}	
	}
	
	public String getRankedMap(RankedQueue queue) {
		
		for (RankedTeam team : queue.getQueue()) {
			for (SpleefPlayer sp : team.getPlayers()) {
				if (sp.getOptions().getRankedArena()!=null) {
					return GameManager.getManager().getArenaByName(sp.getOptions().getRankedArena()).getName();
				}
			}
		}
		
		return null;
	}
	
}
