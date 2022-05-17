package me.santipingui58.splindux.game.ranked;

import java.util.List;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;


public class RankedManager {
	private static RankedManager manager;	
	 public static RankedManager getManager() {
	        if (manager == null)
	        	manager = new RankedManager();
	        return manager;
	    }
	

	  private  RankedQueue spleefQueue = new RankedQueue(SpleefType.SPLEEF);
	  private RankedQueue spleggQueue = new RankedQueue(SpleefType.SPLEGG);
	  private RankedQueue tntrunQueue = new RankedQueue(SpleefType.TNTRUN);
	
	 
	  public RankedQueue getSpleefQueue() {
		  return this.spleefQueue;
	  }
	  
	  public RankedQueue getSpleggQueue() {
		  return this.spleggQueue;
	  }
	  
	  public RankedQueue getTNTRunQueue() {
		  return this.tntrunQueue;
	  }
	 
	  public RankedQueue getRankedQueue(SpleefType  type) {
		  switch(type) {
		case BOWSPLEEF:
			break;
		case POTSPLEEF:
			break;
		case SPLEEF:
			return spleefQueue;
		case SPLEGG:
			return spleggQueue;
		case TNTRUN:
			return tntrunQueue;
		default:
			break;
		  }
		return null;
	  }
	
	public int calcualteELO(List<SpleefPlayer> winner, List<SpleefPlayer> loser, int difpuntos,SpleefType spleefType) {
		int p1 = 0;
		int p2 = 0;
		
		for (SpleefPlayer sp : winner) p1 = p1 + sp.getPlayerStats().getELO(spleefType);
		for (SpleefPlayer sp : loser) p2 = p2 + sp.getPlayerStats().getELO(spleefType);
		
		
		int a = winner.size();
		int b = winner.size();
		if (a==0) a = 1;
		if (b==0) b=1;
		
		p1 = p1/a;
		p2 = p2/b;
		
		int newELO = elo(p1,p2);	
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


	
}
