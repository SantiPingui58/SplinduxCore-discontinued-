package me.santipingui58.splindux.game.ranked;

import java.util.ArrayList;
import java.util.List;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;

public class RankedTeam {

	
	private List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
	
	public RankedTeam(List<SpleefPlayer> players) {
		this.players = players;
	}
	
	public List<SpleefPlayer> getPlayers() {
		return this.players;
	}
	
	
	public int getELO() {
		int elo = 0;
		for (SpleefPlayer sp : this.players) {
			elo = elo + sp.getPlayerStats().getELO(SpleefType.SPLEEF);
		}
		elo = elo/this.players.size();
		return elo;
	}

	public void newELO(int elo) {
		for (SpleefPlayer sp : players) {
			sp.getPlayerStats().setELO(SpleefType.SPLEEF,sp.getPlayerStats().getELO(SpleefType.SPLEEF)+elo);
		}
		
	}
}
