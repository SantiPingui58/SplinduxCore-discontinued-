package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.PlayerOptions;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.utils.GetCountry;
import me.santipingui58.splindux.utils.Utils;
import net.archangel99.dailyrewards.DailyRewards;
import net.archangel99.dailyrewards.manager.objects.DUser;

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
	private int totalonlinetime;
	private int splinboxpoints;
	
	private Date lastlogin;
	private Date registerdate;
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
	private List<SpleefDuel> duels = new ArrayList<SpleefDuel>();

	
	private boolean adminlogin;
	private boolean islogged;
	private boolean register;
	
	private int level;
	private int mutationTokens;
	private int duelpage;
	
	private PlayerOptions options;
	
	public SpleefPlayer(UUID uuid) {
		this.uuid = uuid;
		this.scoreboard = ScoreboardType.LOBBY;
		options = new PlayerOptions();
		
	}
	

	
	public PlayerOptions getOptions() {
		return this.options;
	}
	
	public int getMutationTokens() {
		return this.mutationTokens;
	}
	
	public void setMutationTokens(int i) {
		this.mutationTokens = i;
	}
	public void setOptions(PlayerOptions l) {
		this.options = l;
	}
	 
	
	public int getDuelPage() {
		return this.duelpage;
	}
	
	public void setDuelPage(int i) {
		this.duelpage = i;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int i) {
		this.level = i;
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

		if (!p.hasPlayedBefore() && !(p.getPlayer()==null)) {
			return null;
		}
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
	
	
	 public boolean isInGame() {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
			 if (arena.getPlayers().contains(this)) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public boolean isInArena() {
		 if (isInGame()) {
			 return true;
		 }
		 if (isInQueue()) {
			 return true;
		 }
		 return false;
	 }
	 
	 public boolean isInQueue() {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
			 if (arena.getQueue().contains(this)) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 

 	 
	 public SpleefArena getArena() {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
			 if (arena.getPlayers().contains(this) || arena.getQueue().contains(this)) {
				 return arena;
			 }
		 }
		 return null;
	 }
	 
	 public void leave(boolean teleport) {
		 getPlayer().setGameMode(GameMode.ADVENTURE);
		leaveSpectate(teleport);
		leaveQueue(getArena(),teleport);    	
	 }
	 
	 
		public void leaveQueue(SpleefArena arena,boolean teleport) {
			giveLobbyItems();
			if (isInArena()) {
			if (getPlayer().isOnline() && teleport) {
				if (Main.arenas.getConfig().contains("mainlobby")) {
					getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}				
			getPlayer().sendMessage("§aYou have left the queue.");		
			setScoreboard(ScoreboardType.LOBBY);
			}
			if (arena.getPlayers().contains(this)) {
				if (!arena.getResetRequest().isEmpty()) {
				List<SpleefPlayer> resetRequest = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
				if (resetRequest.contains(this)) {
					if (arena.getResetRequest().size()<=1) {
						GameManager.getManager().resetArenaWithCommand(arena);
					} else {
						arena.getResetRequest().remove(this);
					}
				}
				}
				if (!arena.getEndGameRequest().isEmpty()) {
				List<SpleefPlayer> endGameRequest = GameManager.getManager().leftPlayersToSomething(arena.getEndGameRequest(), arena,true);
				if (endGameRequest.contains(this)) {
					if (arena.getEndGameRequest().size()<=1) {
						GameManager.getManager().endGameDuel(arena, null,GameEndReason.ENDGAME);
					} else {
						arena.getEndGameRequest().remove(this);
					}
				}
				}
				if (arena.getGameType().equals(GameType.FFA) && teleport) {
				getPlayer().teleport(arena.getLobby());
				}
				if (arena.getGameType().equals(GameType.DUEL)) {
					if (arena.getDuelPlayers1().contains(this)) {
						List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
						for (SpleefPlayer s : arena.getDuelPlayers1()) {
							if (!arena.getDeadPlayers1().contains(s)) {
								alive.add(s);
							}
						}
						if (alive.contains(this)) {
						if (alive.size()<=1) arena.point(this);
						}
						
						if (arena.getDuelPlayers1().size()<=1) {
							GameManager.getManager().endGameDuel(arena,"Team2",GameEndReason.LOG_OFF);
						}
					} else if (arena.getDuelPlayers2().contains(this)) {
							List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
							for (SpleefPlayer s : arena.getDuelPlayers2()) {
								if (!arena.getDeadPlayers2().contains(s)) {
									alive.add(s);
								}
							}					
							if (alive.contains(this)) {
							if (alive.size()<=1) arena.point(this);
							} 
							if (arena.getDuelPlayers2().size()<=1) {
								GameManager.getManager().endGameDuel(arena,"Team1",GameEndReason.LOG_OFF);
							}						
					}
					arena.getDeadPlayers1().remove(this);
					arena.getDeadPlayers2().remove(this);
					arena.getDuelPlayers1().remove(this);
					arena.getDuelPlayers2().remove(this);
					
					if (arena.getPlayToRequest()!=null) {
						
						if (arena.getPlayToRequest().getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1) {
							GameManager.getManager().playToWithCommand(arena, arena.getPlayToRequest().getAmount());
						}		
				}
					
				if (arena.getCrumbleRequest()!=null) {
					if (arena.getCrumbleRequest().getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
						GameManager.getManager().crumbleWithCommand(arena, arena.getCrumbleRequest().getAmount());
					}		
			}
				
				} else if (arena.getGameType().equals(GameType.FFA)) {
					List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
					arena.getFFAPlayers().remove(this);				
					players.addAll(arena.getFFAPlayers());
					
					if (players.size()<=1) {
						if (arena.getGameType().equals(GameType.FFA)) {
							GameManager.getManager().endGameFFA(arena, GameEndReason.WINNER);
						}					
						} 
				}
				
				
			} 
				arena.getQueue().remove(this);
				
		}
		}
	 
	 public void leaveSpectate(boolean teleport) {
		 setSpectate(null);
		 setScoreboard(ScoreboardType.LOBBY);
		 if (getOfflinePlayer().isOnline() && teleport) {
 		 getPlayer().teleport(Main.lobby);
		 getPlayer().setGameMode(GameMode.ADVENTURE);
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
	
	public boolean isGameDead() {
		if (isInGame()) {
		if (getArena().getDeadPlayers1().contains(this) ||
				getArena().getDeadPlayers2().contains(this)) return true;
	}	
		return false;
	}
	public List<SpleefDuel> getDuels() {
		return this.duels;
	}
	
	public SpleefDuel getDuelByUUID(UUID uuid) {
		for (SpleefDuel d : this.duels) {
			if (d.getUUID().equals(uuid)) {
				return d;
			}
		}
		return null;
	}
	
	public boolean hasDueled(SpleefPlayer sp) {
		for (SpleefDuel sd : this.duels) {
			if (sd.getDueledPlayers().contains(sp)) {
				return true;
			}
		}
		return false;
	}
	
	public SpleefDuel getDuelByDueledPlayer(SpleefPlayer sp) {
		for (SpleefDuel sd : this.duels) {
			if (sd.getDueledPlayers().contains(sp)) {
				return sd;
			}
		}
		return null;
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
	
	public int getTotalOnlineTime() {
		return this.totalonlinetime;
	}
	
	public void setTotalOnlineTIme(int i) {
		this.totalonlinetime = i;
	}
	
	public int getOnlineTime() {
		return this.onlinetime;
	}
	
	public int getSplinboxPoints() {
		return this.splinboxpoints;
	}
	
	public void addSplinboxPoints(int i) {
		this.splinboxpoints = this.splinboxpoints+i;
	}
	
	
	public void resetSplinboxPoints() {
		this.splinboxpoints = 0;
	}
	
	public void splinboxPoints() {
		if (this.isafk) {
			return;
		} else if (isInGame()) {
			this.splinboxpoints = this.splinboxpoints+15;
		} else {
			this.splinboxpoints = this.splinboxpoints+10;
		}
	}
	
	public void addOnlineTime() {
		splinboxPoints();
		this.onlinetime++;
		if (this.onlinetime%60==0) {
		this.totalonlinetime++;
		}
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
	
	public void add1vs1Games() {
		this._1vs1_games = this._1vs1_games+1;
	}
	public int get1vs1Wins() {
		return this._1vs1_wins;
	}
	
	public void set1vs1Wins(int i) {
		this._1vs1_wins = i;
	}
	
	public void add1vs1Wins() {
		this._1vs1_wins++;
	}
	
	public int getTotalGames() {
	return	this._1vs1_games + this.FFA_games;
	}
	

	public boolean hasUnclaimedRewards() {
		DUser duser = DailyRewards.getInstance().getUserManager().getOrLoadUser(getPlayer());
		return duser.hasActiveReward();
	}
	
	public int getCoins() {
		return	(int) Main.econ.getBalance(Bukkit.getOfflinePlayer(uuid));
	}
	
	public void addCoins(int i) {
		 Main.econ.depositPlayer(Bukkit.getOfflinePlayer(uuid), i);
	}
	
	
	public void removeCoins(int i) {
		 Main.econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), i);
	}
	
	public void giveGameItems() {
		for(PotionEffect effect : getPlayer().getActivePotionEffects())	{
		    getPlayer().removePotionEffect(effect.getType());
		}
		getPlayer().getInventory().clear();
		SpleefArena arena = getArena();
		if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
		if (getPlayer().hasPermission("splindux.diamondshovel")) {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[1]);
		} else {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[0]);
		}
		} else if (arena.getSpleefType().equals(SpleefType.SPLEGG)) {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[9]);
		}
		
		if (arena.getGameType().equals(GameType.FFA) && arena.getSpleefType().equals(SpleefType.SPLEEF)) {
			if (!arena.isInEvent()) {
		if (getPlayer().hasPermission("splindux.x10snowballs")) {
			getPlayer().getInventory().setItem(1, DataManager.getManager().gameitems()[6]);
		} else if (getPlayer().hasPermission("splindux.x8snowballs")) {
			getPlayer().getInventory().setItem(1, DataManager.getManager().gameitems()[5]);
		}else if (getPlayer().hasPermission("splindux.x6snowballs")) {
			getPlayer().getInventory().setItem(1, DataManager.getManager().gameitems()[4]);
		}else if (getPlayer().hasPermission("splindux.x4snowballs")) {
			getPlayer().getInventory().setItem(1, DataManager.getManager().gameitems()[3]);
		} else {
			getPlayer().getInventory().setItem(1, DataManager.getManager().gameitems()[2]);
		}
			} else {
				getPlayer().getInventory().setItem(1, DataManager.getManager().gameitems()[2]);
			}
			
			for (GameMutation mutation : arena.getInGameMutations()) {
				mutation.giveMutationItems(this);
			}
		} else if (arena.getGameType().equals(GameType.DUEL) && arena.getDuelPlayers1().size()>=2 && arena.getDuelPlayers2().size()>=2) {

			if (arena.getDuelPlayers1().contains(this)) {
				getPlayer().getInventory().setHelmet(DataManager.getManager().gameitems()[7]);
				getPlayer().getInventory().setItem(8, new ItemStack(Material.INK_SACK,1,(byte) 4));
			} else if (arena.getDuelPlayers2().contains(this)) {
				getPlayer().getInventory().setHelmet(DataManager.getManager().gameitems()[8]);
				getPlayer().getInventory().setItem(8, new ItemStack(Material.INK_SACK,1,(byte) 1));
			}
		
		}
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void giveLobbyItems() {
		for(PotionEffect effect : getPlayer().getActivePotionEffects())	{
		    getPlayer().removePotionEffect(effect.getType());
		}
		getPlayer().playEffect(getPlayer().getLocation(), Effect.RECORD_PLAY, 0);
		getPlayer().setGameMode(GameMode.ADVENTURE);
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setItem(4, DataManager.getManager().lobbyitems()[0]);	
		getPlayer().getInventory().setItem(5, DataManager.getManager().lobbyitems()[1]);
	}
	
	@SuppressWarnings("deprecation")
	public void giveQueueItems() {
		for(PotionEffect effect : getPlayer().getActivePotionEffects())	{
		    getPlayer().removePotionEffect(effect.getType());
		}
		getPlayer().playEffect(getPlayer().getLocation(), Effect.RECORD_PLAY, 0);
		getPlayer().setGameMode(GameMode.ADVENTURE);
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setItem(7, DataManager.getManager().queueitems()[0]);	
		getPlayer().getInventory().setItem(8, DataManager.getManager().queueitems()[1]);
	}
	
	
}
