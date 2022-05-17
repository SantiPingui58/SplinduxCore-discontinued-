package me.santipingui58.splindux.game.spleef;

public class PlayerStats {

	//FFA, ELO and Duels all time stats
	private int[] FFA_wins = {0,0,0};
	private int[] FFA_games= {0,0,0};
	private int[] FFA_kills= {0,0,0};
	private int[] ELO= {0,0,0};
	private int[] duel_wins= {0,0,0};
	private int[] duel_games= {0,0,0};
	
	//FFA and Duels monthly stats
	private int[] monthly_FFA_wins= {0,0,0};
	private int[] monthly_FFA_games= {0,0,0};
	private int[] monthly_FFA_kills= {0,0,0};

	//FFA and Duels monthly stats
	private int[] weekly_FFA_wins= {0,0,0};
	private int[] weekly_FFA_games= {0,0,0};
	private int[] weekly_FFA_kills= {0,0,0};
	
	
	
	private int getSpleefType(SpleefType type) {
		switch (type) {
		default:break;
		case SPLEEF: return 0;
		case TNTRUN: return 1;
		case SPLEGG: return 2;
		}
		return -1;	
	}
	
	public int getFFAWins(SpleefType type) {
		return this.FFA_wins[getSpleefType(type)];
	} 
	
	public void setFFAWins(SpleefType type,int i) {
		this.FFA_wins[getSpleefType(type)] = i; 
	}
	
	public int getFFAGames(SpleefType type) {
		return this.FFA_games[getSpleefType(type)];
	} 
	public void setFFAGames(SpleefType type,int i) {
		this.FFA_games[getSpleefType(type)] = i; 
	}
	
	public int getFFAKills(SpleefType type) {
		return this.FFA_kills[getSpleefType(type)];
	} 
	public void setFFAKills(SpleefType type,int i) {
		this.FFA_kills[getSpleefType(type)] = i; 
	}
	
	public int getELO(SpleefType type) {
		return this.ELO[getSpleefType(type)];
	} 
	public void setELO(SpleefType type,int i) {
		this.ELO[getSpleefType(type)] = i; 
	}
	
	public int getDuelGames(SpleefType type) {
		return this.duel_games[getSpleefType(type)];
	} 
	public void setDuelGames(SpleefType type,int i) {
		this.duel_games[getSpleefType(type)] = i; 
	}
	
	public int getDuelWins(SpleefType type) {
		return this.duel_wins[getSpleefType(type)];
	} 
	public void setDuelWins(SpleefType type,int i) {
		this.duel_wins[getSpleefType(type)] = i; 
	}
	
	
	public int getMonthlyFFAWins(SpleefType type) {
		return this.monthly_FFA_wins[getSpleefType(type)];
	} 
	
	public void setMonthlyFFAWins(SpleefType type,int i) {
		this.monthly_FFA_wins[getSpleefType(type)] = i; 
	}
	
	public int getMonthlyFFAGames(SpleefType type) {
		return this.monthly_FFA_games[getSpleefType(type)];
	} 
	public void setMonthlyFFAGames(SpleefType type,int i) {
		this.monthly_FFA_games[getSpleefType(type)] = i; 
	}
	
	public int getMonthlyFFAKills(SpleefType type) {
		return this.monthly_FFA_kills[getSpleefType(type)];
	} 
	public void setMonthlyFFAKills(SpleefType type,int i) {
		this.monthly_FFA_kills[getSpleefType(type)] = i; 
	}
	
	public int getWeeklyFFAWins(SpleefType type) {
		return this.weekly_FFA_wins[getSpleefType(type)];
	} 
	
	public void setWeeklyFFAWins(SpleefType type,int i) {
		this.weekly_FFA_wins[getSpleefType(type)] = i; 
	}
	
	public int getWeeklyFFAGames(SpleefType type) {
		return this.weekly_FFA_games[getSpleefType(type)];
	} 
	public void setWeeklyFFAGames(SpleefType type,int i) {
		this.weekly_FFA_games[getSpleefType(type)] = i; 
	}
	
	public int getWeeklyFFAKills(SpleefType type) {
		return this.weekly_FFA_kills[getSpleefType(type)];
	} 
	public void setWeeklyFFAKills(SpleefType type,int i) {
		this.weekly_FFA_kills[getSpleefType(type)] = i; 
	}
	
	
	public double getWinGameRatio(SpleefType type) {
		if (getFFAGames(type)==0) {
			return (double) getFFAWins(type);
		} else {
			return (double) getFFAWins(type)/getFFAGames(type);
		}
	}
	
	public double getKillGameRatio(SpleefType type) {
		if (getFFAGames(type)==0) {
			return (double) getFFAKills(type);
		} else {
			return (double) getFFAKills(type)/getFFAGames(type);
		}
	}

	public int getGlobalFFAWins() {
		int i =0;
		i = i + getFFAWins(SpleefType.SPLEEF);
		i = i + getFFAWins(SpleefType.SPLEGG);
		i = i + getFFAWins(SpleefType.TNTRUN);
		return i;
	}
	
	public int getGlobalELO() {
		int i =0;
		i = i + getELO(SpleefType.SPLEEF);
		i = i + getELO(SpleefType.SPLEGG);
		i = i + getELO(SpleefType.TNTRUN);
		return i/3;
	}
}
