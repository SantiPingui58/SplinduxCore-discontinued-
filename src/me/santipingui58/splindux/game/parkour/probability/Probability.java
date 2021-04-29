package me.santipingui58.splindux.game.parkour.probability;

import java.util.ArrayList;
import java.util.List;
import me.santipingui58.splindux.game.parkour.Level;
import me.santipingui58.splindux.game.parkour.ParkourPlayer;

public class Probability {

	private double probability;
	private List<ParkourPlayer> playersThatTried;
	private List<ParkourPlayer> playersThatPassed;
	private Level level;
	
	public Probability(Level level) {
		this.level = level;
		this.playersThatTried = new ArrayList<ParkourPlayer>();
		this.playersThatPassed = new ArrayList<ParkourPlayer>();
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public String getProbability() {
		return String.format("%.2f", probability);
	}
	
	public int getAmountOfPassed() {
		return this.playersThatPassed.size();
	}
	
	public int getAmountOfTried() {
		return this.playersThatTried.size();
	}
	
	 public String calculate() {
		 String s = null;
		 playersThatTried.clear();
		 playersThatPassed.clear();
		 
		// new BukkitRunnable() {
//		@Override
	//	public void run() {
			
			// for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			//	 ParkourPlayer pp = sp.getParkourPlayer();
			//	 if (pp.getCurrentLevel()>=level.getLevel()) playersThatTried.add(pp);
			//	 if (pp.getCurrentLevel()>level.getLevel()) playersThatPassed.add(pp);	 
		//}

			
			 
			 double dividendo = 1;
			 if (playersThatTried.size()>0) dividendo = playersThatTried.size();
			 probability = playersThatPassed.size()/dividendo*100;
	
	// }
		// }.runTaskAsynchronously(Main.get());
		 return s;
		 
	 }
	
	
}
