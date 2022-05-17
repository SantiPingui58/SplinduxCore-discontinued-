package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.nicknamer.api.NickNamerAPI;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import me.neznamy.tab.api.TABAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.cosmetics.helmets.Helmet;
import me.santipingui58.splindux.cosmetics.helmets.HelmetManager;
import me.santipingui58.splindux.cosmetics.particles.effect.ParticleEffectType;
import me.santipingui58.splindux.cosmetics.particles.type.ParticleTypeSubType;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.PlayerOptions;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.ffa.FFATeam;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.parkour.FinishParkourReason;
import me.santipingui58.splindux.game.parkour.ParkourPlayer;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.friends.Friendship;
import me.santipingui58.splindux.scoreboard.PinguiScoreboard;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.ranking.RankingManager;
import me.santipingui58.splindux.utils.ActionBarAPI;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.splindux.vote.Rewarded;
import me.santipingui58.splindux.vote.VoteClaims;
import net.archangel99.dailyrewards.DailyRewards;
import net.archangel99.dailyrewards.manager.objects.DUser;

public class SpleefPlayer {

	//UUID of the Player
	private UUID uuid;
	
	//Player stats
	private PlayerStats playerStats;
	
	
	//Parkour Player of the SpleefPlayer
	private ParkourPlayer parkourPlayer;
	
	private int linked;
	
	private int SWSState;
		
	//time in minutes that the server was online. 0 when the player is not online.
	private int onlinetime;
	
	//Total time the player was online on the Server.
	private int totalonlinetime;
	
	//Increases every minute and depends if the player was playing or not, or if he was AFK.
	private int splinboxpoints;
	
	//Last login of the player
	private Date lastlogin;
	
	//Date that the player joined the server for first time
	private Date registerdate;
	
	//ScoreboardType of the Player, changes in certain conditions.
	private ScoreboardType scoreboard;
	
	//If Player is AFK or not.
	private boolean isafk;
	
	//Time the player was not moving, when reaching a certain amount, the player becomes AFK.
	private int afktimer;
	
	//Timer for AFK in Game, not working properly at the moment.
	private int afkgametimer;
	
	//If the player is able to fly.
	private boolean fly;
	
	//IP of the player
	private String ip;
	
	//2 chars Code of the player's country.
	private String country;

	//Location a few ticks ago of the player.
	private Location location;

	private boolean isHidingSpectators;
	
	//List of Duels the player sent.
	private List<SpleefDuel> duels = new ArrayList<SpleefDuel>();

	
	//Spleef EXP amount
	private int level;
	
	//Amount of Mutation Tokens the player has.
	private int mutationTokens;
	
	//auxiliar variable of the Duel Page of the player. Not used currently since there aren't enough maps.
	private int duelpage;
	
	//Amount of rankeds the player has.
	private int rankeds;
	
	//Options of the player.
	private PlayerOptions options;
	
	//Particles
	private ParticleEffectType particleEffect;
	private ParticleTypeSubType particleType;
	
	private VoteClaims voteClaims;
	
	private int rankingPosition;
	private List<String> rankingRecords;
	
	private String helmet;
	
	private UUID tip;
	
	private boolean ismoving;
	
	private boolean fellParkour;
	
	private int totalVotes;
	
	private int coins;
	
	
	
	public SpleefPlayer(UUID uuid) {
		this.uuid = uuid;
		this.scoreboard = ScoreboardType.LOBBY;
		this.getPlayerStats().setELO(SpleefType.SPLEEF, 1000);
		this.getPlayerStats().setELO(SpleefType.TNTRUN, 1000);
		this.getPlayerStats().setELO(SpleefType.SPLEGG, 1000);
		options = new PlayerOptions();	
		if (this.rankingRecords ==null) this.rankingRecords = new ArrayList<String>();
		 DataManager.getManager().addPlayer(uuid, this);
		
	}
	
	public int getSWSState() {
		return this.SWSState;
	}
	
	
	/*
	 * 0 null
	 * 1 qualified
	 * 2 national
	 * 3 continental
	 * 4 world
	 */
	public void setSWSState(int i) {
		this.SWSState = i;
	}
	
	
	public boolean isLinked() {
		if (this.linked==0) {
			try {
			 String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(getUUID());
		        if (discordId != null) {
		        	  User user = DiscordUtil.getJda().getUserById(discordId);
		        	   if (user != null)  this.linked=2;
		        } else {
		        	 this.linked=1;
		        }    
			} catch (Exception ex) {}
		}
		
		return this.linked==2;
	}
	
	
	
	public int getTotalVotes() {
		return this.totalVotes;
	}
	
	public void  setTotalVotes(int i) {
		this.totalVotes = i;
	}
	
	public boolean isMoving() {
		return this.ismoving;
	}
	
	public void moving(boolean b) {
		this.ismoving = b;
	}
	 
	public PlayerStats getPlayerStats() {
		if (this.playerStats==null) this.playerStats = new PlayerStats();
		return this.playerStats;
	}
	
	public void setPlayerStats(PlayerStats playerStats) {
		this.playerStats = playerStats;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public Helmet getHelmet() {
		return HelmetManager.getManager().getHelmetByName(helmet);
	}
	
	public UUID getTippedPlayer() {
		return this.tip;
	}
	
	public void setTipPlayer(UUID uuid) {
		this.tip = uuid;
	}

	
	public int getRankingPosition() {
		if (this.rankingPosition==0)
			this.rankingPosition = RankingManager.getManager().getRanking().getPosition(getUUID());	
		
		return this.rankingPosition;
	}
	
	
	public List<String> getRankingRecords() {
		return this.rankingRecords;
	}
	
	public boolean isHidingSpectators() {
		return this.isHidingSpectators;
	}
	
	public void hideSpectators(boolean b) {
		this.isHidingSpectators = b;
	}
	

	public VoteClaims getVoteClaims() {
		return this.voteClaims;
	}
	
	public void setVoteClaims (VoteClaims voteClaims) {
		this.voteClaims = voteClaims;
	}
	
	public void loadParkourPlayer(int i,  me.santipingui58.splindux.game.parkour.PlayerStats stats) {
		this.parkourPlayer = new ParkourPlayer(getUUID(),i,stats);
	}
	
	
	public ParkourPlayer getParkourPlayer() {
	return this.parkourPlayer;
	}
	
	public static SpleefPlayer getSpleefPlayer(UUID uuid) {
		return getSpleefPlayer(Bukkit.getOfflinePlayer(uuid));
	}
	
	
	public static SpleefPlayer getSpleefPlayer(OfflinePlayer p) {
		return DataManager.getManager().getPlayer(p.getUniqueId());
		
	}
	
	
	public int getRankeds() {
		return this.rankeds;
	}
	
	public void setRankeds(int i) {
		this.rankeds = i;
	}
	
	
	
	public void applyParticles() {
		if (this.getParticleEffect()!=null && this.getParticleType() !=null) {
			if (getOfflinePlayer().isOnline()) {
		PlayerParticlesAPI.getInstance().resetActivePlayerParticles(getPlayer());
		PlayerParticlesAPI.getInstance().addActivePlayerParticle(getPlayer(), this.particleType.typeToEffect(),this.particleEffect.effectToDefaultStyles());
		}
		}
	}
	
	public ParticleEffectType getParticleEffect() {
		return this.particleEffect;	
	}
	
	
	public void setParticleEffect(ParticleEffectType type) {
		this.particleEffect = type;
		applyParticles();
	}
	
	public ParticleTypeSubType getParticleType() {
		return this.particleType;	
	}
	
	
	public void setParticleTypeSubType(ParticleTypeSubType type) {
		this.particleType = type;
		applyParticles();
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

	  public void addCoins(SpleefPlayer sp, int i,boolean multiplier,boolean found) {
		  EconomyManager.getManager().addCoins(sp, i, multiplier, found);
	  }
	
	
	public void teleportToLobby() {		
		new BukkitRunnable() {
			public void run() {
				if (getOfflinePlayer().isOnline()) {
					if (getPlayer().hasPermission("splindux.fly")) {
						getPlayer().setAllowFlight(true);
						getPlayer().setFlying(true);
						
					} else {
						getPlayer().setAllowFlight(false);
						getPlayer().setFlying(false);	
					}
					getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
			}
		}
	}.runTaskLater(Main.get(), 2L);
	}
	
	
	 public boolean isInGame() {
		 Arena[] arenas = DataManager.getManager().getArenas().toArray(new Arena[0]);
 		 for (Arena arena : arenas) {
 			 if (arena.getGameType().equals(GameType.FFA)) {
 				 FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
 				 if (ffa!=null) {
 				 if (ffa.getPlayers().contains(this)) {
 					 return true;
 				 }
 				 }
 			 } else {
			 if (arena.getPlayers().contains(this)) {
				 return true;
			 }
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
		 for (Arena arena : DataManager.getManager().getArenas()) {
			 if (arena.getGameType().equals(GameType.FFA)) {
 				 FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
 				 if (ffa!=null) {
 				 if (ffa.getQueue().contains(this)) {
 					 return true;
 				 }
 				 }
 			 } else {
			 if (arena.getQueue().contains(this)) {
				 return true;
			 }
 			 }
		 }
		 return false;
	 }
	 
	 

 	 
	 public Arena getArena() {
		 for (Arena arena : DataManager.getManager().getArenas()) {
			 if (arena.getGameType().equals(GameType.FFA)) {
				 FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
				 if (ffa!=null) {
					 if (ffa.getPlayers().contains(this) || ffa.getQueue().contains(this)) {
						 return arena;
					 }
				 }
			 } else {
			 if (arena.getPlayers().contains(this) || arena.getQueue().contains(this)) {
				 return arena;
			 }
			 }
		 }
		 return null;
	 }
	 
	public void leave(boolean teleport,boolean giveLobbyItems) {
		new BukkitRunnable() {
			public void run() {
		if (getOfflinePlayer().isOnline()) {
			getPlayer().setWalkSpeed(0.2F);
			getPlayer().setGameMode(GameMode.ADVENTURE);
			getPlayer().eject();
			ActionBarAPI.sendActionBar(getPlayer(), "");
		}
		}
		}.runTask(Main.get());
		
		leaveQueue(getArena(),teleport,giveLobbyItems);    	
		leaveSpectate(teleport,false,true);
		leaveParkour();
	
	 }
	 
	 
	 public void leaveParkour() {
		 if (getParkourPlayer()!=null && getParkourPlayer().getArena()!=null) {
			 getParkourPlayer().getArena().finish(FinishParkourReason.DISCONNECTED);
		 }
	 }
	 
		public void leaveQueue(Arena arena,boolean teleport,boolean giveLobbyItems) {
			
			setScoreboard(ScoreboardType.LOBBY);
			DataManager.getManager().getLobbyPlayers().add(getUUID());
			DataManager.getManager().getPlayingPlayers().remove(getUUID());
			if (getOfflinePlayer().isOnline() && giveLobbyItems) giveLobbyItems();
			
			
			new BukkitRunnable() {
				public void run() {
					if (getOfflinePlayer().isOnline())
			 updateScoreboard();	
				}
			}.runTaskLater(Main.get(), 5L);
			
			

			
			if (getOfflinePlayer().isOnline() && teleport) {
				if (Main.arenas.getConfig().contains("mainlobby")) {
					getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}				
			sendMessage("§aYou have left the queue.");		
			
			}
			
			
			this.getDuels().clear();
			
			if (isInArena()) {
				SpleefPlayer player = this;				
				FFAArena ffa = null;
				if (arena.getGameType().equals(GameType.FFA)) {
					ffa = GameManager.getManager().getFFAArena(arena.getSpleefType());
				}
			if (arena.getPlayers().contains(player) || (ffa!=null && ffa.getQueue().contains(player))) {
				if (!arena.getResetRequest().isEmpty()) {
				List<SpleefPlayer> resetRequest = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
				if (resetRequest.contains(player) && arena.getPlayers().size()>=1) {
					if (arena.getResetRequest().size()<1) {
						GameManager.getManager().resetArenaWithCommand(arena);
					} else {
						arena.getResetRequest().remove(player);
					}
				}
				}
				if (!arena.getEndGameRequest().isEmpty()) {
				List<SpleefPlayer> endGameRequest = GameManager.getManager().leftPlayersToSomething(arena.getEndGameRequest(), arena,true);
				if (endGameRequest.contains(player)) {
					if (arena.getEndGameRequest().size()<=1) {
						GameManager.getManager().endGameDuel(arena, null,GameEndReason.ENDGAME);
					} else {
						arena.getEndGameRequest().remove(player);
					}
				}
				}
				
				
				/*
				if (arena.getGameType().equals(GameType.FFA) && teleport) {
				getPlayer().teleport(arena.getLobby());
				}
				*/
				
				if (arena.getGameType().equals(GameType.DUEL)) {
					
					if (arena.getDuelPlayers1().contains(player)) {
						if (arena.getDuelPlayers1().size()<=1) {
							arena.getDeadPlayers1().remove(player);
							arena.getDeadPlayers2().remove(player);
							arena.getDuelPlayers1().remove(player);
							arena.getDuelPlayers2().remove(player);
							arena.setDisconnectedPlayer(player);
							GameManager.getManager().endGameDuel(arena,"Team2",GameEndReason.LOG_OFF);
						}
						
						List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
						for (SpleefPlayer s : arena.getDuelPlayers1()) {
							if (!arena.getDeadPlayers1().contains(s)) {
								if (s.getOfflinePlayer().isOnline()) alive.add(s);
							}
						}
						
						if (alive.contains(player)) {
							if (alive.size()<=1)  {
								arena.point(player);
							}
						}
						
						
					} else if (arena.getDuelPlayers2().contains(player)) {
						if (arena.getDuelPlayers2().size()<=1) {
							arena.getDeadPlayers1().remove(player);
							arena.getDeadPlayers2().remove(player);
							arena.getDuelPlayers1().remove(player);
							arena.getDuelPlayers2().remove(player);
							arena.setDisconnectedPlayer(player);
							GameManager.getManager().endGameDuel(arena,"Team1",GameEndReason.LOG_OFF);
						}	
							List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
							for (SpleefPlayer s : arena.getDuelPlayers2()) {
								if (!arena.getDeadPlayers2().contains(s)) {
									alive.add(s);
								}
							}					
							if (alive.contains(player)) {
							if (alive.size()<=1)  {
								arena.point(player);
							
							}
							} 
					
					}
					arena.getDeadPlayers1().remove(player);
					arena.getDeadPlayers2().remove(player);
					arena.getDuelPlayers1().remove(player);
					arena.getDuelPlayers2().remove(player);
					
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
					if (Main.ffa2v2) {
						FFATeam team = ffa.getTeamByPlayer(getUUID());
						team.killPlayer(getUUID());
					}

					List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
					ffa.getPlayers().remove(player);		
					ffa.getQueue().remove(player);
					players.addAll(ffa.getPlayers());
					
					
					boolean condition = Main.ffa2v2 ? ffa.getTeamsAlive().size()<=1 : players.size()<=1;
					
					if (condition) {
						if (arena.getGameType().equals(GameType.FFA) && (arena.getState().equals(GameState.STARTING)|| arena.getState().equals(GameState.GAME)) ) {
							ffa.endGame(GameEndReason.WINNER);
						}					
						} 
				}
				
				
			} 
			
				arena.getQueue().remove(player);		
}
		}
	 
	 public void leaveSpectate(boolean teleport, boolean wentBackToPlay,boolean fromMasterLeave) {
		 

		 if (getSpleefArenaSpectating()!=null || getParkourArenaSpectating()!=null) {	
 
		 if (!wentBackToPlay || fromMasterLeave) {

		 setScoreboard(ScoreboardType.LOBBY);		 
			DataManager.getManager().getLobbyPlayers().add(getUUID());
			DataManager.getManager().getPlayingPlayers().remove(getUUID());
			new BukkitRunnable() {
				public void run() {
		 if (getOfflinePlayer().isOnline() && teleport) {
 		 getPlayer().teleport(Main.lobby);
		 getPlayer().setGameMode(GameMode.ADVENTURE);
		 giveLobbyItems();
		 }
		 }
			}.runTask(Main.get());
		 }
		 
		 SpectateManager.getManager().leaveSpectate(this,false);
	 }
	 }


	public Arena getSpleefArenaSpectating() {
		for (Arena arena : DataManager.getManager().getArenas()) {
			if (arena.getSpectators().contains(this)) {
				return arena;
			}
		}
		return null;
	}
	
	
	public Arena getParkourArenaSpectating() {
		for (Arena arena : DataManager.getManager().getArenas()) {
			if (arena.getSpectators().contains(this)) {
				return arena;
			}
		}
		return null;
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(this.uuid);
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

 	public String getCountry() {		
		return this.country;
	}
	
	public void setCountry(String s) {
		this.country = s;
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
			this.coinspoints = this.coinspoints + 15;
		} else {
			this.splinboxpoints = this.splinboxpoints+10;
			this.coinspoints = this.coinspoints + 10;
		}
	}
	
	
	private int coinspoints;
	
	public void resetCoinsPoints() {
		this.coinspoints = 0;
	}
	
	public int getCoinsPoints() {
		return this.coinspoints;
	}
	
	public void addOnlineTime() {
		splinboxPoints();
		this.onlinetime++;
		this.totalonlinetime++;
		
	}
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
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
		if (!Main.fly) return;
		this.fly = true;
		new BukkitRunnable() {
			public void run() {
		getPlayer().setAllowFlight(true);
		getPlayer().setFlying(true);
		}
		}.runTask(Main.get());
	}
	public void stopfly() {
		this.fly = false;
	
		new BukkitRunnable() {
			public void run() {
		getPlayer().setAllowFlight(false);
		getPlayer().setFlying(false);
		}
		}.runTask(Main.get());
	}
	

	public boolean isFlying() {
		return this.fly;
	}
	public void afk() {
		this.isafk = true;
		if (getOfflinePlayer().isOnline()) {
		if (getPlayer().hasPermission("splindux.vip")) {
			UUID uuid = getPlayer().getUniqueId();
		TABAPI.setAboveNameTemporarily(uuid, "");
		TABAPI.setTabPrefixTemporarily(uuid, "§7§oAFK ");
		TABAPI.setTagPrefixTemporarily(uuid, "§7§oAFK ");
		TABAPI.setBelowNameTemporarily(uuid, "");
	}
		}
		}
	
	public void back() {
		this.isafk = false;
		if (getOfflinePlayer().isOnline()) {
		if (getPlayer().hasPermission("splindux.vip")) {
			UUID uuid = getPlayer().getUniqueId();
			TABAPI.removeTemporaryAboveName(uuid);
			TABAPI.removeTemporaryBelowName(uuid);
			TABAPI.removeTemporaryTabPrefix(uuid);
			TABAPI.removeTemporaryTagPrefix(uuid);
		}
	}
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
	
	public boolean hasUnclaimedRewards() {
		
		for (Rewarded rewarded : Rewarded.values()) {
			try {
		if (getVoteClaims().hasClaimed(this, rewarded)) return true;
		} catch(Exception ex) {
			continue;
		}
			}
		
		try {
			DUser duser = DailyRewards.getInstance().getUserManager().getOrLoadUser(getPlayer());
			return duser.hasActiveReward();
			} catch(Exception ex) {
				return false;
			}
			
	
	}
	
	public int getCoins() {
		return this.coins;
	}
	public void setCoins(int i) {
		this.coins = i;
	}
	
	public void addCoins(int i) {
		this.coins = this.coins + i;
	}
	
	
	public void removeCoins(int i) {
		this.coins = this.coins - i;
	}
	
	
	public void giveShovel() {
		
		SpleefPlayer sp = this;
		new BukkitRunnable() {
			public void run() {
		Arena arena = getArena();
		if (!getOfflinePlayer().isOnline()) return;
		if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
		if (getPlayer().hasPermission("splindux.diamondshovel")) {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[1]);
		} else {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[0]);
		}
		} else if (arena.getSpleefType().equals(SpleefType.SPLEGG)) {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[9]);
		} else if (arena.getSpleefType().equals(SpleefType.TNTRUN)) return;
		
		if (arena.getGameType().equals(GameType.FFA) && arena.getSpleefType().equals(SpleefType.SPLEEF)) {
			FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
			if (!ffa.isInEvent()) {
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
			
			for (GameMutation mutation : ffa.getInGameMutations()) {
				mutation.giveMutationItems(sp);
			}
		}
		if (arena.getGameType().equals(GameType.DUEL)) {
			getPlayer().getInventory().setItem(7, DataManager.getManager().gameitems()[10]);
		}
			}
		}.runTask(Main.get());
	}
	
	public void giveGameItems() {
		SpleefPlayer sp = this;
		new BukkitRunnable() {
			public void run() {
		if (!getOfflinePlayer().isOnline()) return;
		for(PotionEffect effect : getPlayer().getActivePotionEffects())	{
		    getPlayer().removePotionEffect(effect.getType());
		}
		if (getOptions().hasNightVision()) {
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1));
		}
		
		clearGameInventory();
		Arena arena = getArena();
		if (arena==null) return;
		if (getHelmet()!=null) {
		getPlayer().getInventory().setHelmet(getHelmet().getItem(sp, true));
		}
		
			if (Main.ffa2v2 && arena.getGameType().equals(GameType.FFA)) {
				FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
				FFATeam team = ffa.getTeamByPlayer(uuid);	
				ItemStack blueflag = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
				LeatherArmorMeta bluemeta =(LeatherArmorMeta) blueflag.getItemMeta();
				bluemeta.setColor(Color.fromRGB(team.getRGB()[0], team.getRGB()[1], team.getRGB()[2]));
				blueflag.setItemMeta(bluemeta);
				getPlayer().getInventory().setChestplate(blueflag);
			}
		
		
		 if (arena.getGameType().equals(GameType.DUEL)) {

				getPlayer().getInventory().setItem(7, DataManager.getManager().gameitems()[10]);
				if (arena.getDuelPlayers1().size()>=2 && arena.getDuelPlayers2().size()>=2) {
			if (arena.getDuelPlayers1().contains(sp)) {
				getPlayer().getInventory().setChestplate(DataManager.getManager().gameitems()[7]);
				getPlayer().getInventory().setItem(8, new ItemStack(Material.INK_SACK,1,(byte) 4));
			} else if (arena.getDuelPlayers2().contains(sp)) {
				getPlayer().getInventory().setChestplate(DataManager.getManager().gameitems()[8]);
				getPlayer().getInventory().setItem(8, new ItemStack(Material.INK_SACK,1,(byte) 1));
			
			}
			}
		 }
			}
		 }.runTask(Main.get());
		
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void giveLobbyItems() {
		new BukkitRunnable() {
			public void run() {
				if (!getOfflinePlayer().isOnline()) return;
		for(PotionEffect effect : getPlayer().getActivePotionEffects())	{
		    getPlayer().removePotionEffect(effect.getType());
		}
		getPlayer().playEffect(getPlayer().getLocation(), Effect.RECORD_PLAY, 0);
		getPlayer().setGameMode(GameMode.ADVENTURE);
		if (!getPlayer().getWorld().getName().equalsIgnoreCase("world") || !getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {	
		getPlayer().getInventory().clear();
		}
		if (!getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
		//getPlayer().getInventory().setItem(3, DataManager.getManager().lobbyitems()[1]);		
		getPlayer().getInventory().setItem(4, DataManager.getManager().lobbyitems()[0]);	
		getPlayer().getInventory().setItem(8, DataManager.getManager().lobbyitems()[2]);	
		getPlayer().getInventory().setItem(0, DataManager.getManager().lobbyitems()[4]);
		getPlayer().getInventory().setItem(7, DataManager.getManager().lobbyitems()[5]);
		//getPlayer().getInventory().setItem(6, DataManager.getManager().lobbyitems()[6]);
	}
			}
		}.runTask(Main.get());
	}
	
	@SuppressWarnings("deprecation")
	public void giveQueueItems(boolean giveShovel,boolean giveMutations, boolean giveSpectate) {
	new BukkitRunnable() {
		public void run() {
		clearGameInventory();
		if (giveShovel) {
			getPlayer().setGameMode(GameMode.SURVIVAL);
		if (getPlayer().hasPermission("splindux.diamondshovel")) {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[1]);
		} else {
			getPlayer().getInventory().setItem(0, DataManager.getManager().gameitems()[0]);
		}
		} else {
			getPlayer().setGameMode(GameMode.ADVENTURE);
		}
		
		if (giveSpectate) 	getPlayer().getInventory().setItem(6, DataManager.getManager().spectateitems()[0]);
		
		for(PotionEffect effect : getPlayer().getActivePotionEffects())	{
		    getPlayer().removePotionEffect(effect.getType());
		}
		getPlayer().playEffect(getPlayer().getLocation(), Effect.RECORD_PLAY, 0);
		
		if (giveMutations) {
		getPlayer().getInventory().setItem(7, DataManager.getManager().queueitems()[0]);	
		}
		getPlayer().getInventory().setItem(8, DataManager.getManager().queueitems()[1]);
		getPlayer().getInventory().setItem(4, DataManager.getManager().lobbyitems()[0]);	
	}
	}.runTask(Main.get());
	}

	public void giveSpectateItems() {
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setItem(8, DataManager.getManager().spectateitems()[0]);
	}
	
	
	
	
	public String getName() {
		 if (NickNamerAPI.getNickManager().isNicked(getOfflinePlayer().getUniqueId())) {
			 return "~"+ NickNamerAPI.getNickManager().getNick(getOfflinePlayer().getUniqueId());
		 }
		 return getOfflinePlayer().getName();
	}

	
	public boolean isSpectating() {
		return this.getSpleefArenaSpectating()!=null;
	}

	public void sendMessage(String message) {
		if (getOfflinePlayer().isOnline()) {
		if (Bukkit.isPrimaryThread()) {
			getPlayer().sendMessage(message);
		} else {
			new BukkitRunnable() {
				public void run() {
					if (getOfflinePlayer().isOnline())
					getPlayer().sendMessage(message);
				}
			}.runTask(Main.get());
		}
		
			
		}
		
	}
	

	public void setHelmet(String string) {
		this.helmet = string;
		
	}

	public void updateScoreboard() {
		 PinguiScoreboard.getScoreboard().scoreboard(this);	
	}

	public void clearGameInventory() {

		if (!getOfflinePlayer().isOnline()) return;
		Player player = getPlayer();	
		ItemStack[] armorContents = player.getInventory().getArmorContents().clone(); //Clone instance of ItemStack[]
		player.getInventory().clear(); //Clear inventory
		player.getInventory().setArmorContents(armorContents); //Set armor using the clone instance.
		player.updateInventory(); //Update... but its not necesary all time.
				
	}



	public void addDuelGames(SpleefType type) {
		getPlayerStats().setDuelGames(type, getPlayerStats().getDuelGames(type)+1);
		
	}


	public void addFFAKill(SpleefType type) {
		getPlayerStats().setFFAKills(type, getPlayerStats().getFFAKills(type)+1);
		getPlayerStats().setWeeklyFFAKills(type, getPlayerStats().getWeeklyFFAKills(type)+1);
		getPlayerStats().setMonthlyFFAKills(type, getPlayerStats().getMonthlyFFAKills(type)+1);
		
	}
	
	public void addFFAGame(SpleefType type) {
		getPlayerStats().setFFAGames(type, getPlayerStats().getFFAGames(type)+1);
		getPlayerStats().setWeeklyFFAGames(type, getPlayerStats().getWeeklyFFAGames(type)+1);
		getPlayerStats().setMonthlyFFAGames(type, getPlayerStats().getMonthlyFFAGames(type)+1);
	}
	
	public void addFFAWin(SpleefType type) {
		getPlayerStats().setFFAWins(type, getPlayerStats().getFFAWins(type)+1);
		getPlayerStats().setWeeklyFFAWins(type, getPlayerStats().getWeeklyFFAWins(type)+1);
		getPlayerStats().setMonthlyFFAWins(type, getPlayerStats().getMonthlyFFAWins(type)+1);
	}
	
	public void hidePlayer(SpleefPlayer sp) {
		if (getOfflinePlayer().isOnline() && sp.getOfflinePlayer().isOnline()) getPlayer().hidePlayer(Main.get(), sp.getPlayer());
	}
	public void showPlayer(SpleefPlayer sp) {
		if (getOfflinePlayer().isOnline() && sp.getOfflinePlayer().isOnline()) getPlayer().showPlayer(Main.get(), sp.getPlayer());
	}


	public List<UUID> getFriends() {
		List<UUID> friends = new ArrayList<UUID>();
		for (Friendship fr : FriendsManager.getManager().getFriendships()) {
			if (fr.getPlayer1().compareTo(this.uuid)==0) friends.add(fr.getPlayer2());
			if (fr.getPlayer2().compareTo(this.uuid)==0) friends.add(fr.getPlayer1());
		}
		return friends;
	}

	public boolean isinParkour() {
		if (this.getParkourPlayer()!=null) {
			return this.getParkourPlayer().getArena()!=null;
		} else {
			return false;
		}
	}

	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}

	public void teleport(Location l) {
		if (getOfflinePlayer().isOnline()) {
		if (Bukkit.isPrimaryThread()) {
			getPlayer().teleport(l);
		} else {
			new BukkitRunnable() {
				public void run() {
					getPlayer().teleport(l);
				}
			}.runTask(Main.get());
		}
		}
		
	}

	public void fellParkour(boolean b) {
		this.fellParkour = b;		
	}
	
	public boolean fellParkour() {
		return this.fellParkour;
	}


}

