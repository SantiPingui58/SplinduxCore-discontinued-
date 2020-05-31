package me.santipingui58.splindux.game.ranked;

import java.util.List;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class RankedTeam {

	
	private List<SpleefPlayer> players;
	
	public RankedTeam(List<SpleefPlayer> players) {
		this.players = players;
	}
	
	public List<SpleefPlayer> getPlayers() {
		return this.players;
	}
	
	
	public int getELO() {
		int elo = 0;
		for (SpleefPlayer sp : this.players) {
			elo = elo + sp.getELO();
		}
		elo = elo/this.players.size();
		return elo;
	}
}
