package me.santipingui58.splindux.game.ffa;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class FFABet {

	
	private SpleefPlayer owner;
	private SpleefPlayer bet;
	private int amount;
	
	public FFABet(SpleefPlayer owner, SpleefPlayer bet, int amount) {
		this.owner = owner;
		this.bet = bet;
		this.amount = amount;
	}
	
	public SpleefPlayer getOwner() {
		return this.owner;
	}
	
	public SpleefPlayer getBet() {
		return this.bet;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
}
