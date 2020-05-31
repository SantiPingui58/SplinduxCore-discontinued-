package me.santipingui58.splindux.game.ranked;

import java.util.ArrayList;
import java.util.List;

import me.santipingui58.splindux.game.spleef.GameType;
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
	
	
	
	public RankedQueue getRankedQueue(SpleefType spleef, GameType game, int teamSize) {
		for (RankedQueue q : this.queues) {
			if (q.getGameType().equals(game) && q.getSpleefType().equals(game) && q.getTeamSize()==teamSize) {
				return q;
			}
		}
		return null;
	}
	
	public void loadRankedQueues() {
		for (SpleefType spleef : SpleefType.values()) {
				for (int i = 1;i<=3;i++) {
					RankedQueue queue = new RankedQueue(spleef, GameType.DUEL, i);
					this.queues.add(queue);
				}
			
		}
	}
	
	
	
	public int calcualteELO(List<SpleefPlayer> winner, List<SpleefPlayer> loser, int difpuntos) {
		int p1 = 0;
		int p2 = 0;
		for (SpleefPlayer sp : winner) p1 = p1 + sp.getELO();
		for (SpleefPlayer sp : loser) p2 = p2 + sp.getELO();
		p1 = p1/winner.size();
		p2 = p2/winner.size();
		
		int newELO = elo(p1,p2,difpuntos);
		
		for (SpleefPlayer sp : winner) {
			sp.setELO(sp.getELO()+newELO);
		}
		for (SpleefPlayer sp : winner) {
			sp.setELO(sp.getELO()-newELO);
		}
		
		return newELO;
	}
	
	private int elo(int elo1, int elo2, int difpuntos) {
		int k = 48;
		  double p1 = ((double) elo1/ (double) 400); //2
		  double p1_ = Math.pow(10, p1); //100

		  double p2 = ((double)elo2/(double)400); 
		  double p2_ = Math.pow(10, p2);

		  double d = p1_ + p2_;

		  double ex1 = (double)p1_/(double)d;   
		  double a1 = ((double)k*(1-(double)ex1));
		  return (int) a1;
	}
}
