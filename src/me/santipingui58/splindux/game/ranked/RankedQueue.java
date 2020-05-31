package me.santipingui58.splindux.game.ranked;

import java.util.ArrayList;
import java.util.List;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;

public class RankedQueue {

	private List<RankedTeam> queue;
	private SpleefType spleefType;
	private GameType gameType;
	private int teamSize;
	private boolean vip;
	private int time;
	
	public RankedQueue(SpleefType spleefType, GameType gameType, int teamSize) {
		this.spleefType= spleefType;
		this.gameType = gameType;
		this.teamSize = teamSize;
		this.queue = new ArrayList<RankedTeam>();
	}

	
	public int getTime() {
		return this.time;
	}
	
	public SpleefType getSpleefType() {
		return this.spleefType;
	}
	
	public GameType getGameType() {
		return this.gameType;
	}
	
	public List<RankedTeam> getQueue() {
		return this.queue;
	}
	
	public int getTeamSize() {
		return this.teamSize;
	}
	
	
	
	public void joinQueue(List<SpleefPlayer> list) {
		RankedTeam team = new RankedTeam(list);
		this.queue.add(team);
		if (this.vip && this.queue.size()>=2) {
			startGames();
		}
		for (SpleefPlayer sp : team.getPlayers()) {
		if (sp.getPlayer().hasPermission("splindux.extreme") || sp.getPlayer().hasPermission("splindux.epic")) {
			this.vip=true;
			checkQueue();
			break;
		}
	}
	}
	//Cada 45 segundos
	public void checkQueue() {
		if (this.queue.size()>=2) {
			startGames();
		}
	}
	

	public void startGames() {
		List<RankedTeam> list = new ArrayList<RankedTeam>();
		list.addAll(this.queue);
		if (this.queue.size()%2!=0) {
			list.remove(this.queue.get(this.queue.size()-1));		
		} 
		
		
	}
	
}
