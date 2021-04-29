package me.santipingui58.splindux.vote;

import java.util.HashMap;
import java.util.Map.Entry;

import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.level.LevelManager;

public class VoteRewards {

	private int coins;
	private int exp;
	private HashMap<Integer,Integer> splinboxes;
	private Rewarded network;
	
	public VoteRewards(Rewarded red, int coins, int exp, HashMap<Integer,Integer> splinboxes) {
		this.coins = coins;
		this.exp = exp;
		this.splinboxes = splinboxes;
		this.network = red;
	}
	
	public int getCoins() {
		return this.coins;
	}
	
	public int getExp() {
		return this.exp;
	}
	
	public HashMap<Integer,Integer> getSplinboxes() {
		return this.splinboxes;
	}
	
	
	
	public void giveRewards(SpleefPlayer sp) {
		EconomyManager.getManager().addCoins(sp, coins, true, false);
		LevelManager.getManager().addLevel(sp, exp);
		if (this.splinboxes!=null) {
			int amount =  0;
			int level = 0;
			for (Entry<Integer, Integer> entry : this.splinboxes.entrySet()) {
			    amount= entry.getKey();
			    level = entry.getValue();
			    break;
			}
			
			EconomyManager.getManager().addSplinboxes(sp, amount, level);
		}
		
		sp.sendMessage("§e§lSplin§b§lDux§5§lVotes §aYou have claimed your rewards for: §d" + network.getClaimMessage());
	} 
}
