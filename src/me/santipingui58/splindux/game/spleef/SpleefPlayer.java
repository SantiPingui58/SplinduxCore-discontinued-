package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.utils.GetCountry;

public class SpleefPlayer {

	private UUID uuid;
	private int FFA_wins;
	private int FFA_games;
	private int FFA_kills;
	private int ELO;
	private int _1vs1_wins;
	private int _1vs1_games;	
	
	private int monthly_FFA_wins;
	private int monthly_FFA_games;
	private int monthly_FFA_kills;

	
	private int weekly_FFA_wins;
	private int weekly_FFA_games;
	private int weekly_FFA_kills;

	private int dailywinlimit;
	private int onlinetime;
	
	private Date lastlogin;
	private Date registerdate;
	private int coins;
	private ScoreboardType scoreboard;
	private boolean isafk;
	private int afktimer;
	
	private int afkgametimer;
	private boolean fly;
	private String ip;
	private String country;

	
	private String nick;
	private Location location;
	
	private int parkourtimer;
	private boolean parkour;
	private SpleefPlayer spectate;
	private List<SpleefPlayer> dueled = new ArrayList<SpleefPlayer>();
	
	
	private boolean adminlogin;
	private boolean islogged;
	private boolean register;
	public SpleefPlayer(UUID uuid) {
		this.uuid = uuid;
		this.scoreboard = ScoreboardType.LOBBY;
	}
	
	
	public Date getRegisterDate() {
		return this.registerdate;
	}
	
	public void setRegisterDate(Date d) {
		this.registerdate = d;
	}
	
	public Date getLastLogin() {
		return this.lastlogin;
	}
	
	public void setLastLogin(Date d) {
		this.lastlogin = d;
	}
	
	public static SpleefPlayer getSpleefPlayerOnRestart(OfflinePlayer p) {
		for (SpleefPlayer rp : DataManager.getManager().getPlayers()) {
			if (rp.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())) {		
				DataManager.getManager().getPlayersCache().put(p, rp);
				return rp;
			}
		}	
		return null;
	
	}
	
	public static SpleefPlayer getSpleefPlayer(OfflinePlayer p) {
		if (DataManager.getManager().getPlayersCache().containsKey(p)) {
			return DataManager.getManager().getPlayersCache().get(p);
		} else {
		for (SpleefPlayer rp : DataManager.getManager().getPlayers()) {
			if (rp.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())) {		
				DataManager.getManager().getPlayersCache().put(p, rp);
				return rp;
			}
		}	
		return null;
	}
	}
	public boolean needsToRegisterQuestionmark() {
		return this.register;
	}
	
	public void needsToRegister() {
		this.register = true;
	}
	
	public boolean needsAdminLoginQuestionmark() {
		return this.adminlogin;
	}
	
	
	public void needsAdminLogin() {
		this.adminlogin = true;
	}
	
	public boolean isLogged() {
		return this.islogged;
	}
	
	public void login() {
		this.islogged = true;
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(this.uuid);
	}
	
	public List<SpleefPlayer> getSpectators() {
		List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			if (sp.isSpectating()) {
				if (sp.getSpectating().equals(this)) {
					list.add(sp);
				}
			}
		}
		return list;
	}
	public void setSpectate(SpleefPlayer spectate) {
		this.spectate = spectate;
	}
	
	public SpleefPlayer getSpectating() {
		return this.spectate;
	}
	
	public boolean isSpectating() {
		if (this.spectate==null) {
			return false;
		} 
		return true;
	}
	public List<SpleefPlayer> getDueledPlayers() {
		return this.dueled;
	}
	
	public int getParkourTimer() {
		return this.parkourtimer;
	}
	
	public void resetParkourTimer() {
		this.parkourtimer = 0;
	}
	
	public void addParkourTimer() {
		this.parkourtimer = this.parkourtimer + 1;
	}
	
	public boolean isInParkour() {
		return this.parkour;
	}
	
	public void joinParkour() {
		resetParkourTimer();
		this.parkour = true;
	}
	
	public void leaveParkour() {
		this.parkour = false;
		resetParkourTimer();
	}
 	public String getCountry() {
		if (this.country==null) {
			new GetCountry(this);
		}
		
		return this.country;
	}
	
	public void setCountry(String s) {
		this.country = s;
	}
	public int getMonthlyFFAWins() {
		return this.monthly_FFA_wins;
	}
	public void setMonthlyFFAWins(int i) {
		this.monthly_FFA_wins = i;
	}
	
	public int getMonthlyFFAGames() {
		return this.monthly_FFA_games;
	}
	public void setMonthlyFFAGames(int i) {
		this.monthly_FFA_games = i;
	}
	
	public int getMonthlyFFAKills() {
		return this.monthly_FFA_kills;
	}
	public void setMonthlyFFAKills(int i) {
		this.monthly_FFA_kills = i;
	}
	
	public void addGameAFKTimer() {
		this.afkgametimer = this.afkgametimer+1;
	}
	public void setGameAFKTimer(int i) {
		this.afkgametimer = i;
	}
	
	public int getGameAFKTimer() {
		return this.afkgametimer;
	}
	
	public int getAFKTimer() {
		return this.afktimer;
	}
	
	public void setAFKTimer(int i) {
		this.afktimer = i;
	}
	
	public int getWeeklyFFAWins() {
		return this.weekly_FFA_wins;
	}
	public void setWeeklyFFAWins(int i) {
		this.weekly_FFA_wins = i;
	}
	
	public int getWeeklyFFAGames() {
		return this.weekly_FFA_games;
	}
	public void setWeeklyFFAGames(int i) {
		this.weekly_FFA_games = i;
	}
	
	public int getWeeklyFFAKills() {
		return this.weekly_FFA_kills;
	}
	public void setWeeklyFFAKills(int i) {
		this.weekly_FFA_kills = i;
	}
	public int getOnlineTime() {
		return this.onlinetime;
	}
	
	public void addOnlineTime() {
		this.onlinetime = this.onlinetime + 1;
	}
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getNick() {
		return this.nick;
	}
	
	public boolean hasNick() {
		if (this.nick!=null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void deleteNick() {
		this.nick = null;
	}
	public boolean isAfk() {
		return this.isafk;
	}
	
	public String getIP() {
		return this.ip;
	}
	
	public void setIP(String ip) {
		this.ip = ip;
	}
	public void fly() {
		this.fly = true;
		getPlayer().setAllowFlight(true);
		getPlayer().setFlying(true);
	}
	public void stopfly() {
		this.fly = false;
		getPlayer().setAllowFlight(false);
		getPlayer().setFlying(false);
		
	}
	

	public boolean isFlying() {
		return this.fly;
	}
	public void afk() {
		this.isafk = true;
	}
	
	public void back() {
		this.isafk = false;
	}
	public ScoreboardType getScoreboard() {
		return this.scoreboard;
	}

	public void setScoreboard(ScoreboardType type) {
		this.scoreboard = type;
	}
	
	
	public Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}
	
	public double getWinGameRatio() {
		if (this.FFA_games==0) {
			return (double) this.FFA_wins;
		} else {
			return (double) this.FFA_wins/this.FFA_games;
		}
	}
	
	public double getKillGameRatio() {
		if (this.FFA_games==0) {
			return (double) this.FFA_kills;
		} else {
			return (double) this.FFA_kills/this.FFA_games;
		}
	}
	public int getFFAWins() {
		return this.FFA_wins;
	}
	
	public void setFFAWins(int i) {
		this.FFA_wins = i;
	}
	
	
	public void setDailyWinLimit(int i) {
		this.dailywinlimit = i;
	}
	
	public int getDailyWinLimit() {
		return this.dailywinlimit;
	}
	public void addFFAWin() {
		this.FFA_wins = this.FFA_wins+1;
		this.monthly_FFA_wins = this.monthly_FFA_wins+1;
		this.weekly_FFA_wins = this.weekly_FFA_wins+1;	
	}
	public void addFFAKill() {
		this.FFA_kills = this.FFA_kills+1;
		this.monthly_FFA_kills = this.monthly_FFA_kills+1;
		this.weekly_FFA_kills = this.weekly_FFA_kills+1;
	}
	
	public void addFFAGame() {
		this.FFA_games = this.FFA_games+1;
		this.monthly_FFA_games = this.monthly_FFA_games+1;
		this.weekly_FFA_games = this.weekly_FFA_games+1;
	}
	public int getFFAGames() {
		return this.FFA_games;
	}
	
	public int getFFAKills() {
		return this.FFA_kills;
	}
	public void setFFAKills(int i) {
		this.FFA_kills = i;
	}
	public void setFFAGames(int i) {
		this.FFA_games = i;
	}
	
	public int getELO() {
		return this.ELO;
	}
	
	public void setELO(int i) {
		this.ELO = i;
	}
	
	public int get1vs1Games() {
		return this._1vs1_games;
	}
	
	public void set1vs1Games(int i) {
		this._1vs1_games = i;
	}
	
	public int get1vs1Wins() {
		return this._1vs1_wins;
	}
	
	public void set1vs1Wins(int i) {
		this._1vs1_wins = i;
	}
	
	public int getTotalGames() {
	return	this._1vs1_games + this.FFA_games;
	}
	

	
	public int getCoins() {
		return this.coins;
	}
	
	public void setCoins(int i) {
		this.coins = i;
	}
}
