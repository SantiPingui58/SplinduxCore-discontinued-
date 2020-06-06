package me.santipingui58.splindux.game.ranked;

import java.util.ArrayList;
import java.util.List;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;

public class RankedQueue {

	private List<RankedTeam> queue;
	private SpleefType spleefType;
	private int teamSize;
	private boolean vip;
	private int time;
	
	public RankedQueue(SpleefType spleefType, int teamSize) {
		this.spleefType= spleefType;
		this.teamSize = teamSize;
		this.queue = new ArrayList<RankedTeam>();
	}

	
	public int getTime() {
		return this.time;
	}
	
	public SpleefType getSpleefType() {
		return this.spleefType;
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
		RankedTeam impar = null;
		list.addAll(this.queue);
		if (this.queue.size()%2!=0) {
			impar = this.queue.get(this.queue.size()-1);
			list.remove(impar);	
		} 
		
		
		for (int i = 0;i<list.size();i=i+2) {
			RankedTeam team1 = list.get(i);
			RankedTeam team2 = list.get(i+1);
			
			SpleefPlayer captain1 = team1.getPlayers().get(0);
			team1.getPlayers().remove(captain1);
			List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
			players.addAll(team1.getPlayers());
			players.addAll(team2.getPlayers());
			List<RankedTeam> rankedTeams = new ArrayList<RankedTeam>();
			rankedTeams.add(team1);
			rankedTeams.add(team2);
			GameManager.getManager().duelGame(captain1, players, null, SpleefType.SPLEEF, getTeamSize(), true,rankedTeams);
		}
		
		if (impar!=null) {
			this.queue.add(impar);
		}
		
		
	}
	
}
