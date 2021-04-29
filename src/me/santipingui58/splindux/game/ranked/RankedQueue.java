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
	
	public boolean isPlayerInQueue(SpleefPlayer sp) {
		for (RankedTeam team : this.queue) {
			for (SpleefPlayer s : team.getPlayers()) {
				if (s.equals(sp)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void joinQueue(List<SpleefPlayer> list) {
		for (SpleefPlayer sp : list) {
			if (isPlayerInQueue(sp)) {				
				sp.getPlayer().sendMessage("§cYou are already in a queue.");
				return;
			}	else if (sp.getRankeds()<=0) {
				sp.getPlayer().sendMessage("§cYou don't have rankeds left.");
				return;
			}
		}
		
		
		RankedTeam team = new RankedTeam(list);
		for (SpleefPlayer sp : list) {
			sp.giveQueueItems(false, false,false);
			sp.getPlayer().sendMessage("§6[Ranked] §aYou joined the Ranked Queue. Current ELO: §b" + team.getELO());
		}
		
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
	
	public void leaveQueue(SpleefPlayer sp) {
		RankedTeam remove = null;
		for (RankedTeam team : this.queue) {
			if (team.getPlayers().contains(sp)) {
				remove = team;
				break;
			}
		}
		this.queue.remove(remove);
		
	}
	public void checkQueue() {
		if (this.queue.size()>=2) {
			startGames();
				}
	}
	

	public void startGames() {
		List<RankedTeam> list = new ArrayList<RankedTeam>();
		list.addAll(this.queue);
		if (this.queue.size()%2!=0) {
			this.queue.remove(this.queue.size()-1);
		} 
	
		final RankedQueue r = this;
		for (int i = 0;i<list.size();i=i+2) {
			RankedTeam team1 = list.get(i);
			RankedTeam team2 = list.get(i+1);
			SpleefPlayer captain1 = team1.getPlayers().get(0);	
			List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
			players.addAll(team1.getPlayers());
			players.remove(captain1);
			players.addAll(team2.getPlayers());
			List<RankedTeam> rankedTeams = new ArrayList<RankedTeam>();
			rankedTeams.add(team1);
			rankedTeams.add(team2);
			queue.remove(team1);
			queue.remove(team2);
			String arena = RankedManager.getManager().getRankedMap(r);
		
			GameManager.getManager().duelGame(captain1, players, arena, SpleefType.SPLEEF, getTeamSize(), true,rankedTeams,false,false,-1);
		}
		this.vip=false;
		
	}
	
}
