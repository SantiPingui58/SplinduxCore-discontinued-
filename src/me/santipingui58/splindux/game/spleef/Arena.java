package me.santipingui58.splindux.game.spleef;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.robingrether.idisguise.iDisguise;
import me.santipingui58.fawe.FAWESplinduxAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.ranked.RankedTeam;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.sws.SWSManager;
import me.santipingui58.splindux.task.ArenaStartingCountdownTask;
import me.santipingui58.splindux.task.tasks.DecayTask;
import me.santipingui58.splindux.task.tasks.FFADecayTask;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Arena {

	//Required values of the arena.
	private SpleefType spleeftype;
	private GameType gametype;
	private Location mainspawn;
	private Location lobby;
	private Location arena1;
	private Location arena2;
	private Location spawn1;
	private Location spawn2;
	private String name;
	
	private Location center;
	private Location extremeArena1;
	private Location extremeArena2;
	
	private int shrinkAmount;
	private int crumbleAmount;
	
	
	private SpleefPlayer disconnectedPlayer;
	
	//Min and max amount of players per arena, to determinate the expansion of the arena.
	private int min;
	private int max;
	
	//Amount of players per team this game will have.
	private int teamsize;
	
	//If the game is ranked or not.
	private boolean ranked;
	
	
	//Time until reset in Duels, or finish game in FFA.
	private int time;
	
	//Total time of the arena, only increases.
	private int totaltime;
	
	
	private int radious;
	
	List<Integer[]> pointsHistory = new ArrayList<Integer[]>();
	

	//Current State of the arena.
	private GameState state;
		
	//List of Duel Players per team.
	private List<SpleefPlayer> spleefDuelPlayers1 = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> spleefDuelPlayers2 = new ArrayList<SpleefPlayer>();
	
	//In Team Duels, players will be put in this list temporarily when they die.
	private Set<SpleefPlayer> duelDeadPlayers1 = new HashSet<SpleefPlayer>();
	private Set<SpleefPlayer> duelDeadPlayers2 = new HashSet<SpleefPlayer>();
	
	private Set<UUID> duelTempDisconnectedPlayers1 = new HashSet<UUID>();
	private Set<UUID> duelTempDisconnectedPlayers2 = new HashSet<UUID>();
	private List<SpleefPlayer> duel_queue = new ArrayList<SpleefPlayer>();
	private Set<SpleefPlayer> spectators = new HashSet<SpleefPlayer>();
	
	private int maxSpectators;
	
	//Temp locations when the arena shrinks.
	private Location arena1_1vs1;
	private Location arena2_1vs1;
	private Location spawn1_1vs1;
	private Location spawn2_1vs1;
	
	private Location shrinked_arena1_1vs1;
	private Location shrinked_arena2_1vs1;
	private Location shrinked_spawn1_1vs1;
	private Location shrinked_spawn2_1vs1;
	
	//Item displayed in the DuelMenu.
	private Material item;
	
	
	private int elowinner1;
	
	private int elowinner2;
	
	//Points per player in Duels.
	private int points1;
	private int points2;
	
	//Until when a Duel will be played, default is 7.
	private int playto;
	
	//List of players who requested a reset.
	private List<SpleefPlayer> resetrequest = new ArrayList<SpleefPlayer>();
	
	private List<SpleefPlayer> pauserequest = new ArrayList<SpleefPlayer>();
	
	private List<SpleefPlayer> resumerequest = new ArrayList<SpleefPlayer>();
	
	private List<SpleefPlayer> redorequest = new ArrayList<SpleefPlayer>();
	
	//List of players who requested an endgame.
	private List<SpleefPlayer> endgamerequest = new ArrayList<SpleefPlayer>();
	
	//Custom Request object for crumble and playto.
	private ArenaRequest crumbleRequest;
	private ArenaRequest playtoRequest;
	
	//If the current game is being recorded, not used.
	private boolean isRecording;
	private boolean recordingRequest;
	private GameReplay replay;
	
	//Ranked Teams for Ranked Duels
	private RankedTeam rankedTeam1;
	private RankedTeam rankedTeam2;
	
	private int timed_max_time;
	private boolean tie;
	
	private boolean isGuildGame;

	private int decayRound;

	private int minispleefTimer;
	public int minispleefRound;


		/**
	    * Create the Arena object, where the games will be played. This constructor is used for FFA Arenas.
	    * @param name - code name of the arena.
	    * @param mainspawn - location used for FFA Games, middle point of the arena.
	    * @param lobby - location where players will be teleported after the round/game ends, only used in FFA.
	    * @param arena1 - first corner of the snow field, X and Z values HAVE to be lower than arena2 values.
	    * @param arena2 - second corner of the snow field, X and Z values HAVE to be greater than arena1 values.
	    * @param spleeftype - spleef type of the arena.
	    * @param gametype - game type of the arena.
	    * @return Arena object.
	    */
	public Arena(String name,Location mainspawn,Location lobby,Location arena,SpleefType spleeftype,GameType gametype,int min, int max,int radious) {
		this.name = name;
		this.mainspawn = mainspawn;
		this.lobby = lobby;
		this.arena1 = arena;
		this.spleeftype = spleeftype;
		this.gametype = gametype;
		this.state = GameState.LOBBY;
		this.time = 180;
		this.min = min;
		this.max = max;
		this.radious = radious;
	}

	
	  	/**
	    * Create the Arena object, where the games will be played. This constructor is used for Duel Arenas.
	    * @param name - code name of the arena.
	    * @param mainspawn - location used for FFA Games, middle point of the arena.
	    * @param lobby - location where players will be teleported after the round/game ends, only used in FFA.
	    * @param arena1 - first corner of the snow field, X and Z values HAVE to be lower than arena2 values.
	    * @param arena2 - second corner of the snow field, X and Z values HAVE to be greater than arena1 values.
	    * @param spleeftype - spleef type of the arena.
	    * @param gametype - game type of the arena.
	    * @param item - item displayed in DuelMenu.
	    * @param min - min amount of players that can play in the field.
	    * @param max - max amount of players that can play in the field, based on how much can the arena grow without breaking or getting out of the build.
	    * @return Arena object.
	    */
	public Arena(String name,Location spawn1,Location spawn2,Location arena1, Location arena2, Location lobby,SpleefType spleeftype, GameType gametype,Material item,int min,int max) {
		this.name = name;
		this.spawn1 = spawn1;
		this.spawn2 = spawn2;
		this.arena1 = arena1;
		this.arena2 = arena2;
		this.lobby = lobby;
		this.spleeftype = spleeftype;
		this.gametype = gametype;
		this.state = GameState.LOBBY;
		this.time = 180;
		this.arena1_1vs1 = this.arena1;
		this.arena2_1vs1 = this.arena2;
		this.spawn1_1vs1 = this.spawn1;
		this.spawn2_1vs1 = this.spawn2;
		this.shrinked_arena1_1vs1 = this.arena1;
		this.shrinked_arena2_1vs1 = this.arena2;
		this.shrinked_spawn1_1vs1 = this.spawn1;
		this.shrinked_spawn2_1vs1 = this.spawn2;
		this.playto = 7;
		this.item= item;	
		this.min = min;
		this.max = max;
		this.mainspawn = this.lobby;
		
		this.center = new Location(this.arena1.getWorld(), (arena1.getBlockX()+arena2.getBlockX())/2,
				arena1.getBlockY(),
				(arena1.getBlockZ()+arena2.getBlockZ())/2);
		this.extremeArena1 = this.arena1;
		this.extremeArena2 = this.arena2;
	}
	
	public List<Integer[]> getPointsHistory() {
		return this.pointsHistory;
	}
	
	public Location getExtremeArena1() {
		return this.extremeArena1;
	}
	
	public void setExtremeArena1(Location l) {
		this.extremeArena1 = l;
	}
	
	public void setExtremeArena2(Location l) {
		this.extremeArena2 = l;
	}
	
	public Location getExtremeArena2() {
		return this.extremeArena2;
	}
	public int getRadious() {
		return this.radious;
	}
	 
	
	public int getDecaySpeed() {
		
		switch (this.decayRound) {
		case 1: return 14;
		case 2: return 10;
		case 3: return 6;
		case 4: return 3;
		}
		return 2;
	}

	public boolean isAtMiniSpleef() {
		return this.minispleefRound>0;
	}
	
	
	
	public void setMiniSpleefRound(int i) {
		this.minispleefRound = i;
	}
	
	public void resetMiniSpleefRound() {
		this.minispleefRound = 0;
	}
	

	public void setDecayRound(int i) {
		this.decayRound = i;
	}
	
	public int getDecayRound() {
		return this.decayRound;
	}

	public boolean canTie() {
		return this.tie;
	}
	
	public void setTie(boolean b) {
		this.tie = b;
	}
	
	public void resetMaxSpectators() {
		this.maxSpectators = 0;
	}
	
	public void addMaxSpectators() {
		this.maxSpectators++;
	}
	
	public int getMaxSpectators() {
		return this.maxSpectators;
	}

	public void isGuildGame(boolean guild) {
				this.isGuildGame = guild;
	}
	
	public boolean isGuildGame() {
		return this.isGuildGame;
	}
	
	
	public int getShrinkAmount() {
		return this.shrinkAmount;
	}
	
	public void setShrinkAmount(int i) {
		this.shrinkAmount = i;
	}
	public int getCrumbleAmount() {
		return this.crumbleAmount;
	}
	
	
	public int getEloWinner1() {
		return this.elowinner1;
	}
	
	
	public int getEloWinner2() {
		return this.elowinner2;
	}
	
	public void setEloWinner1(int i) {
		this.elowinner1 = i;
	}
	
	public void setEloWinner2(int i) {
		this.elowinner2 = i;
	}
	
	public void setCrumbleAmount(int i) {
		this.crumbleAmount = i;
	}
	
	public SpleefPlayer getDisconnectedPlayer() {
		return this.disconnectedPlayer;
	}
	
	public void setDisconnectedPlayer(SpleefPlayer s) {
		this.disconnectedPlayer = s;
	}
	
	
	
	public int getTimedMaxTime() {
		return this.timed_max_time;
	}
	
	public void setTimedMaxTime (int i) {
		this.timed_max_time = i;
	}
	
	
	public long getArenaStartingCountdownDelay() {
		if (this.getGameType().equals(GameType.DUEL)) {
			if (this.getSpleefType().equals(SpleefType.TNTRUN)) {
				return 30L;
			} else if (this.getSpleefType().equals(SpleefType.SPLEEF)) {
				if (this.getMiniSpleefRound()>0) {
					return 8L;
				} else {
					return 20L;
				}
			} else {
				return 16L;
			}
		} else {
			if (this.getSpleefType().equals(SpleefType.TNTRUN) || this.getSpleefType().equals(SpleefType.SPLEGG)) {
				return 45L;
			} else {
				return 30L;
			}
		}
	}
	
	public Set<SpleefPlayer> getSpectators() {
		return this.spectators;
	}
	

	
	public void setRankedTeam1(RankedTeam team) {
		this.rankedTeam1 = team;
	}
	
	public RankedTeam getRankedTeam1() {
		return this.rankedTeam1;
	}
	
	public void setRankedTeam2(RankedTeam team) {
		this.rankedTeam2 = team;
	}
	
	public RankedTeam getRankedTeam2() {
		return this.rankedTeam2;
	}
	
		/**
	    * Determine if the current Arena is having a ranked game.
	    * @return TRUE if it is, FALSE otherwise.
	    */
	public boolean isRanked() {
		return this.ranked;
	}
	
		/**
	    * Change ranked value of the arena.
	    * @param b - TRUE if it is, FALSE otherwise.
	    */
	public void ranked(boolean b) {
		this.ranked = b;
	}
	
	
		/**
	    * Set the team size of the next game.
	    * @param i - amount of players per team will the next game have.
	    */
	public void setTeamSize(int i) {
		this.teamsize = i;
	}
	
		/**
	    * Get the amount of players a team has in the current game.
	    * @return Amount of players per team.
	    */
	public int getTeamSize() {
		return this.teamsize;
	}


	public int getMaxPlayersSize() {
		return this.max;
	}
	
	public int getMinPlayersSize() {
		return this.min;
	}
	
	public ArenaRequest getCrumbleRequest() {
		return this.crumbleRequest;
	}
	
	public void setCrumbleRequest(ArenaRequest request) {
		this.crumbleRequest= request;
	}
	
	public ArenaRequest getPlayToRequest() {
		return this.playtoRequest;
	}
	
	public void setPlayToRequest(ArenaRequest request) {
		this.playtoRequest= request;
	}
	
	public GameReplay getReplay() {
		return this.replay;
	}
	
	public void setReplay(GameReplay replay) {
		this.replay = replay;
	}
	
	public boolean getRecordingRequest() {
		return this.recordingRequest;
	}
	
	public void cancelRecordingRequest() {
		this.recordingRequest = false;
	}
	
	public void doRecordingRequest() {
		this.recordingRequest = true;
	}
	public void record() {
		this.isRecording = true;
	}
	
	public void resetRecord() {
		this.isRecording = false;
	}
	
	public boolean isRecording() {
		return this.isRecording;
	}
	 
	public Material getItem() {
		return this.item;
	}

	
	public List<SpleefPlayer> getResetRequest() {
		return this.resetrequest;
	}
	
	public List<SpleefPlayer> getPauseRequest() {
		return this.pauserequest;
	}
	
	public List<SpleefPlayer> getEndGameRequest() {
		return this.endgamerequest;
	}
	
	public List<SpleefPlayer> getResumeRequest() {
		return this.resumerequest;
	}
	
	public List<SpleefPlayer> getRedoRequest() {
		return this.redorequest;
	}
	
	public int getPlayTo() {
		return this.playto;
	}
	
	public void setPlayTo(int i) {
		this.playto = i;
	}
	
	public void resetPlayTo() {
		this.playto = 7;
	}
	

	public Location getDuelArena1() {
		return this.arena1_1vs1;
	}
	
	
	public void setDuelArena1(Location l) {
		this.arena1_1vs1 = l;
	}
	
	public Location getDuelArena2() {
		return this.arena2_1vs1;
	}
	
	public void setDuelArena2(Location l) {
		this.arena2_1vs1 = l;
	}
	
	public Location getDuelSpawn1() {
		return this.spawn1_1vs1;
	}
	
	public void setDuelSpawn1(Location l) {
		this.spawn1_1vs1 = l;
	}
	
	
	public Location getDuelSpawn2() {
		return this.spawn2_1vs1;
	}
	
	public void setDuelSpawn2(Location l) {
		this.spawn2_1vs1 = l;
	}

	public Location getShrinkedDuelArena1() {
		return this.shrinked_arena1_1vs1;
	}
	
	
	public void setShrinkedDuelArena1(Location l) {
		this.shrinked_arena1_1vs1 = l;
	}
	
	public Location getShrinkedDuelArena2() {
		return this.shrinked_arena2_1vs1;
	}
	
	public void setShrinkedDuelArena2(Location l) {
		this.shrinked_arena2_1vs1 = l;
	}
	
	public Location getShrinkedDuelSpawn1() {
		return this.shrinked_spawn1_1vs1;
	}
	
	public void setShrinkedDuelSpawn1(Location l) {
		this.shrinked_spawn1_1vs1 = l;
	}
	
	
	public Location getShrinkedDuelSpawn2() {
		return this.shrinked_spawn2_1vs1;
	}
	
	public void setShrinkedDuelSpawn2(Location l) {
		this.shrinked_spawn2_1vs1 = l;
	}
	
	public int getPoints1() {
		return this.points1;
	}
	
	public void setPoints1(int i) {
		this.points1 = i;
	}
	
	public int getPoints2() {
		return this.points2;
	}

	public void setPoints2(int i) {
		this.points2 = i;
	}
	
	
	public Location getSpawn1() {
		return this.spawn1;
	}
	
	public Location getSpawn2() {
		return this.spawn2;
	}
	
	public void time() {
		this.time = this.time - 1;
		if (this.isAtMiniSpleef()) this.minispleefTimer = this.minispleefTimer+1;
	}
	
	public void resetMiniSpleefTimer() {
		this.minispleefTimer = 0;
	}
	
	public int getMiniSpleefTimer() {
		return this.minispleefTimer;
	}
	
	public Collection<SpleefPlayer> getQueue() {	
			return this.duel_queue;
	}
	
	public void setQueue(Collection<SpleefPlayer> queue) {	
			this.duel_queue = new ArrayList<SpleefPlayer>(queue);
	
	}
	
	public void addTotalTime() {
		this.totaltime++;
	}
	
	public void resetTotalTime() {
		this.totaltime = 0;
	}
	
	public int getTotalTime() {
		return this.totaltime;
	}
	public void resetTimer() {
		if (this.gametype.equals(GameType.FFA)) {
		this.time = 150;
		} else if (this.gametype.equals(GameType.DUEL)) {
			this.time = 180;
		}
	}

	
	public List<SpleefPlayer> getViewers() {	
		List<SpleefPlayer> viewers = new ArrayList<SpleefPlayer>();
		if (this.gametype.equals(GameType.FFA)) {
		for (SpleefPlayer s : GameManager.getManager().getFFAArenaByArena(this).getQueue()) {
			if (s.getOfflinePlayer().isOnline()) viewers.add(s);
		}
		} else if (this.gametype.equals(GameType.DUEL)) {
			for (SpleefPlayer s : getPlayers()) {
				if (s.getOfflinePlayer().isOnline()) viewers.add(s);
			}
			
				for (SpleefPlayer sp : getSpectators()) {
					if (!viewers.contains(sp) && sp.getOfflinePlayer().isOnline())viewers.add(sp);
				}
			
		}
		for (SpleefPlayer s : getQueue()) {
			if (!viewers.contains(s)) {
				if (s.getOfflinePlayer().isOnline()) viewers.add(s);
			}
		}
		return viewers;
	}
	
	
	public List<SpleefPlayer> getDuelPlayers1() {
		return this.spleefDuelPlayers1;
	}
	
	public List<SpleefPlayer> getDuelPlayers2() {
		return this.spleefDuelPlayers2;	
	}
	
	
	public Set<SpleefPlayer> getDeadPlayers1() {
		return this.duelDeadPlayers1;
	}
	
	public Set<SpleefPlayer> getDeadPlayers2() {
		return this.duelDeadPlayers2;	
	}
	
	public Set<UUID> getDuelTempDisconnectedPlayers1() {
		return this.duelTempDisconnectedPlayers1;
	}
	
	public Set<UUID> getDuelTempDisconnectedPlayers2() {
		return this.duelTempDisconnectedPlayers2;	
	}
	
	public int getTime() {
		return this.time;
	}
	
	public void setTime(int i) {
		this.time = i;
	}
	
	public GameState getState() {
		return this.state;
	}
	
	public void setState(GameState gs) {
		this.state = gs;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSchematicName() {
		String n = this.name;
		return n.replaceAll("\\d","");
	}
	
	
	public Location getMainSpawn() {
		return this.mainspawn;
	}
	
	public Location getLobby() {
		return this.lobby;
	}
	public Location getArena1() {
		return this.arena1;
	}
	
	public Location getArena2() {
		return this.arena2;
	}
	
	public SpleefType getSpleefType() {
		return this.spleeftype;
	}
	
	public GameType getGameType() {
		return this.gametype;
	}
	
	private Set<SpleefPlayer> empty = new HashSet<SpleefPlayer>();
	
	public Set<SpleefPlayer> getPlayers() {
		
		 if (this.gametype.equals(GameType.DUEL)) {
			 Set<SpleefPlayer> list = new HashSet<SpleefPlayer>();
			list.addAll(spleefDuelPlayers1);
			list.addAll(spleefDuelPlayers2);
			return list;
		} else if (this.gametype.equals(GameType.FFA)) {
			FFAArena ffa = GameManager.getManager().getFFAArenaByArena(this);
			if (ffa!=null) {
				return ffa.getPlayers();
			}
			return empty;
		}
		
		return null;
	}
	
	public String getTeamName(int i) {
		if (i!=1 && i!=2) return null;
		 List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		 if (i==1) {
			 list.addAll(this.spleefDuelPlayers1);
		 } else if (i==2) {
			 list.addAll(this.spleefDuelPlayers2);
			 }
		 
		 String p = "";
		 for (SpleefPlayer sp : list) {
			if(p.equalsIgnoreCase("")) {
			p = sp.getName();	
			}  else {
				p = p+"-" + sp.getName();
			}
		 }
		 
		return p;
		
	}
	

	 
	 public void addDuelQueue(SpleefPlayer sp, int teamSize,boolean ranked) {
			//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("addDuelQueue in primary thread");
		 if (!this.gametype.equals(GameType.DUEL)) return;
		 this.teamsize= teamSize;
		 this.ranked = ranked;
		 sp.leave(false,false);
		 getQueue().add(sp);
		 sp.giveQueueItems(false,false,false);		
		 sp.getPlayer().sendMessage("§aYou have been added to the queue for " + this.spleeftype.toString()+ " " + this.teamsize + "V" + this.teamsize);
	 }
	 
		public void leaveQueue(SpleefPlayer sp) {		
			sp.leaveSpectate(true,false,false);
			sp.giveLobbyItems();
			if (sp.isInArena()) {
			if (sp.getPlayer().isOnline()) {
				if (Main.arenas.getConfig().contains("mainlobby")) {
					sp.getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}				
			sp.getPlayer().sendMessage("§aYou have left the queue.");		
			sp.setScoreboard(ScoreboardType.LOBBY);
			}
			DataManager.getManager().getLobbyPlayers().add(sp.getUUID());
			DataManager.getManager().getPlayingPlayers().remove(sp.getUUID());
			
			if (getPlayers().contains(sp)) {
				if (!getResetRequest().isEmpty()) {
				List<SpleefPlayer> resetRequest = GameManager.getManager().leftPlayersToSomething(getResetRequest(), this,false);
				if (resetRequest.contains(sp)) {
					if (getResetRequest().size()<=1) {
						GameManager.getManager().resetArenaWithCommand(this);
					} else {
						getResetRequest().remove(sp);
					}}}
				if (!getEndGameRequest().isEmpty()) {
				List<SpleefPlayer> endGameRequest = GameManager.getManager().leftPlayersToSomething(getEndGameRequest(), this,true);
				if (endGameRequest.contains(sp)) {
					if (getEndGameRequest().size()<=1) {
						GameManager.getManager().endGameDuel(this, null,GameEndReason.ENDGAME);
					} else {
						getEndGameRequest().remove(sp);
					}}}			
				sp.getPlayer().teleport(getLobby());
				
				if (getGameType().equals(GameType.DUEL)) {
					if (getDuelPlayers1().contains(sp)) {
						List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
						for (SpleefPlayer s : getDuelPlayers1()) {
							if (!getDeadPlayers1().contains(s)) {
								alive.add(s);
							}}
						if (alive.contains(sp)) {
						if (alive.size()<=1) point(sp);
						}				
						if (getDuelPlayers1().size()<=1) {
							GameManager.getManager().endGameDuel(this,"Team2",GameEndReason.LOG_OFF);
						}} else if (getDuelPlayers2().contains(sp)) {
							List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
							for (SpleefPlayer s : getDuelPlayers2()) {
								if (!getDeadPlayers2().contains(s)) {
									alive.add(s);
								}}					
							if (alive.contains(sp)) {
							if (alive.size()<=1)point(sp);
							} 
							if (getDuelPlayers2().size()<=1) {
								GameManager.getManager().endGameDuel(this,"Team1",GameEndReason.LOG_OFF);
							}}
					getDeadPlayers1().remove(sp);
					getDeadPlayers2().remove(sp);
					getDuelPlayers1().remove(sp);
					getDuelPlayers2().remove(sp);
	
					if (getPlayToRequest()!=null) {
						if (getPlayToRequest().getAcceptedPlayers().size()+1>=getPlayers().size()-1) {
							GameManager.getManager().playToWithCommand(this, getPlayToRequest().getAmount());
						}}	
				if (getCrumbleRequest()!=null) {
					if (getCrumbleRequest().getAcceptedPlayers().size()+1>=getPlayers().size()-1-getDeadPlayers1().size()-getDeadPlayers2().size()) {
						GameManager.getManager().crumbleWithCommand(this, getCrumbleRequest().getAmount());
					}		}} else if (getGameType().equals(GameType.FFA)) {
						FFAArena ffa = GameManager.getManager().getFFAArenaByArena(this);
					List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
					ffa.getPlayers().remove(sp);				
					players.addAll(ffa.getPlayers());					
					if (players.size()<=1) {
						if (getGameType().equals(GameType.FFA)) {
							ffa.endGame(GameEndReason.WINNER);
						}}}} 
			
				getQueue().remove(sp);
				
		}
		}
	 

		public void startGameDuel(boolean sortQueue) {		
		//	if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.startGame in primary thread");
			
			resetMiniSpleefTimer();
			setDecayRound(0);
			resetMiniSpleefRound();
			setState(GameState.STARTING);		
			resetTimer();	
			resetTotalTime();
			resetMaxSpectators();				
			
			
				int i = 0;
				if (sortQueue) Utils.getUtils().newShuffledSet(getQueue());
				int teamSize = getQueue().size()/2;		
				this.teamsize= teamSize;
				for (SpleefPlayer sp : getQueue()) {
					
					Party party = PartyManager.getManager().getParty(sp.getPlayer());
					if (party!=null && party.isLeader(sp.getPlayer()) && !party.isSendingMultipleDuels()) {
						
						Arena arena = this;
						for (UUID u : party.getMembers()) {
							Player player = Bukkit.getPlayer(u);
							SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
							if (!getQueue().contains(splayer)) SpectateManager.getManager().spectateSpleef(splayer, arena);		
						}
					}
					
					
					SpectateManager.getManager().leaveSpectate(sp,false);
					iDisguise.getInstance().getAPI().undisguise(sp.getPlayer());
					if (i<=getQueue().size()) {
						
						if (sp.getOptions().hasNightVision()) {
							sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,Integer.MAX_VALUE));
							}
						
						
						new BukkitRunnable() {
							public void run() {
					if (getSpleefType().equals(SpleefType.SPLEEF)) {
					sp.getPlayer().setGameMode(GameMode.SURVIVAL);				
					} else {
						sp.getPlayer().setGameMode(GameMode.ADVENTURE);
					}
					sp.stopfly();
							}
						}.runTask(Main.get());
						
					sp.setScoreboard(ScoreboardType._1VS1GAME);		
					DataManager.getManager().getLobbyPlayers().remove(sp.getUUID());
					DataManager.getManager().getPlayingPlayers().add(sp.getUUID());
				
					
					if (i<teamSize) {
						getDuelPlayers1().add(sp);
					} else {
						getDuelPlayers2().add(sp);
					}
					i++;
					}}	

				
							resizeArena();
							reset(false,false);

			
				
				for (SpleefPlayer s : getPlayers()) {
					getQueue().remove(s);
					
				}

				List<Player> players = new ArrayList<Player>();
				for (SpleefPlayer sp : getPlayers()) {
					players.add(sp.getPlayer());
					sp.clearGameInventory();
					sp.giveGameItems();
					//GameManager.getManager().generateFakeCuboid(sp, this);	
					sp.addDuelGames(spleeftype);
					EconomyManager.getManager().addCoins(sp, 5, false,false);
					int po = 4+this.teamsize;
					SWSManager.getManager().addPoints(sp,po ,true,true);

				}		
				TextComponent message =null;
								
				String unranked = GameManager.getManager().getGamePrefix(this)+"§aA game between §b" + getTeamName(1) + " §aand §b" + getTeamName(2) + " §ahas started!";
				if (isRanked()) {
					String ranked = GameManager.getManager().getGamePrefix(this)+"§aA game between §b" + getTeamName(1) + "§7(§6"+ rankedTeam1.getELO(getSpleefType()) + "§7)" + " §aand §b" + getTeamName(2)  + "§7(§6"+ rankedTeam2.getELO(getSpleefType()) + "§7)"+ " §ahas started!";		
					 message = new TextComponent(ranked + "§7(Right click to spectate)");
					 DataManager.getManager().broadcast(ranked,false);
					 
				} else {
				 message = new TextComponent(unranked+ " §7(Right click to spectate)");
				 DataManager.getManager().broadcast(unranked,false);
				}
				for (SpleefPlayer spp : getDuelPlayers1()) {
					message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/spectate "+spp.getOfflinePlayer().getName()));
					break;
				}
			
			
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Spectate §a" +getTeamName(1) + " §7-§a " + getTeamName(2)).create()));
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!players.contains(p)) {
						p.spigot().sendMessage(message);
					}}

			for (SpleefPlayer s : getDuelPlayers1()) s.getPlayer().teleport(getShrinkedDuelSpawn1());
			for (SpleefPlayer s : getDuelPlayers2()) s.getPlayer().teleport(getShrinkedDuelSpawn2());
			
			this.setEloWinner1(RankedManager.getManager().calcualteELO(getDuelPlayers1(), getDuelPlayers2(), 5, this.getSpleefType()));
			this.setEloWinner2(RankedManager.getManager().calcualteELO(getDuelPlayers2(), getDuelPlayers1(), 5, this.getSpleefType()));
			
			
			if (getGameType().equals(GameType.FFA) && getSpleefType().equals(SpleefType.SPLEEF)) {
				
			} else {
				new ArenaStartingCountdownTask(this,null);	
			}
		}
		
		
		
		public void crumbleArena(int por) {
			Arena arena = this;
			  new BukkitRunnable() {
				  public void run() {
				setCrumbleRequest(null);
				
			  Location a = getShrinkedDuelArena1();
			  Location b = getShrinkedDuelArena2();	
			  if (gametype.equals(GameType.FFA)) {
				    b = new Location(getArena1().getWorld(),getArena1().getX()+getRadious(),getArena1().getY(),getArena1().getZ()+getRadious());
				  a = new Location(getArena1().getWorld(),getArena1().getX()-getRadious(),getArena1().getY(),getArena1().getZ()-getRadious());
			  }
			  
			  int ax = a.getBlockX();
			  int az = a.getBlockZ();			  
			  int y = a.getBlockY();		  
			  int bx = b.getBlockX();
			  int bz = b.getBlockZ();	
			  
			  List<Location> playerLocations = new ArrayList<Location>();
	  
			  if (gametype.equals(GameType.FFA)) {
				  FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
				  ffa.getPlayers().forEach((p) -> {
				  playerLocations.addAll(Utils.getUtils().getRadioBlocks(p,1));
						  }
						  );
			  } else if (gametype.equals(GameType.DUEL)) {
				  int rad = 1;
				  
				  getDuelPlayers1().forEach((p) -> {
						  playerLocations.addAll(Utils.getUtils().getRadioBlocks(p,rad));
					  });
				  
				  getDuelPlayers2().forEach((p) -> {
					  playerLocations.addAll(Utils.getUtils().getRadioBlocks(p,rad));
				  });
			  }
			   
			  List<Location> spawns = new ArrayList<Location>();
			  final Location aa = a;

			  if (gametype.equals(GameType.FFA)) {
			  	 spawns.add(getMainSpawn());
			  } else if (gametype.equals(GameType.DUEL)) {
				  spawns.add(getShrinkedDuelSpawn1());
				  spawns.add(getShrinkedDuelSpawn2());
			  }
			  
			  
			  playerLocations.add(spawn1);
			  playerLocations.add(spawn2);
		  
			  
			  Set<Location> locations = new HashSet<Location>();
			  for (int x = ax; x <= bx; x++) {
				  for (int z = az; z <= bz; z++) {
					  Location aire = new Location (aa.getWorld(), x, y, z); 
					  
					  if (!isLocationInside(aire,playerLocations)) {
							  int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
							  if (randomNum<por) {
									  if (getSpleefType().equals(SpleefType.SPLEEF)) {
								  if (aire.getBlock().getType().equals(Material.SNOW_BLOCK)) {								  
									  locations.add(aire);
								  }
								  } else if (getSpleefType().equals(SpleefType.SPLEGG)) {
									 if	(aire.getBlock().getType().equals(Material.STAINED_CLAY)) {
										 locations.add(aire);
												  }}}}}}
			  
			  
					  for (Location l : locations) {
						  l.getBlock().setType(Material.AIR);
					  }
			  
			  
			  }
		}.runTaskLater(Main.get(),1L);
		}
		
		
		private boolean isLocationInside(Location location, List<Location> list) {		
			for(Location l : list) {
				if (l==null) continue;
				if (l.getBlockX() 
						==
						location.getBlockX()
						&& 
						l.getBlockY()
						== 
						location.getBlockY() 
						&& 
						l.getBlockZ()
						== 
						location.getBlockZ())
					return true;
			}
			return false;
		}
		
		
		public void clean(Location cc, Location dd) {
			if (this.minispleefRound>1) return;
			if (getGameType().equals(GameType.DUEL)) {
			  	FAWESplinduxAPI.getAPI().placeBlocks(cc, dd, Material.AIR);
			  	//Bukkit.getPlayer("SantiPingui58").sendMessage("cc: " + cc.getBlockX() + ","+ cc.getBlockZ());
			  //	Bukkit.getPlayer("SantiPingui58").sendMessage("dd: " + dd.getBlockX() + ","+ dd.getBlockZ());
			}
		}
		
		
		
		public void reset(boolean point,boolean clean) {		
			//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.reset in primary thread");
 			setPlayToRequest(null);
			getEndGameRequest().clear();
			setCrumbleRequest(null);
			getResetRequest().clear();		
			getPauseRequest().clear();
			getResumeRequest().clear();
			 
			 
			Location a = null;
			Location b = null;		
			Location c = getArena1();
			Location d = getArena2();
			if (getGameType().equals(GameType.FFA)) {
				a = getArena1();
			} else if (getGameType().equals(GameType.DUEL)) {
				if (clean) {
					a = getShrinkedDuelArena1().clone().add(1,0,1);
					b = getShrinkedDuelArena2().clone().add(-1,0,-1);	
				} else {
				a = getShrinkedDuelArena1();
				b = getShrinkedDuelArena2();	
				}
				c = getDuelArena1();
				 d = getDuelArena2();
			}
			
			final Location cc = c;
			final Location dd = d;
			
			final Location aa = a;
			final Location bb = b;
						  
		
					clean(cc,dd);
			
			if (getSpleefType().equals(SpleefType.TNTRUN) || getSpleefType().equals(SpleefType.SPLEGG)) {
				  String name = getGameType().equals(GameType.DUEL) ? getSchematicName() : getName();
				  File file = new File(Main.get().getDataFolder()+"/schematics/"+name+".schematic");
				  FAWESplinduxAPI.getAPI().pasteSchematic(file, getMainSpawn(),false);
			  } else if (getSpleefType().equals(SpleefType.SPLEEF)) {
				  if (getGameType().equals(GameType.DUEL)) {
						FAWESplinduxAPI.getAPI().placeBlocks(aa, bb, Material.SNOW_BLOCK);
				  } else {
					  FAWESplinduxAPI.getAPI().placeCyl(aa, Material.SNOW_BLOCK, getRadious(), 1, false);
				  }
			  }
			
						  
							if (getGameType().equals(GameType.DUEL)) {
							new BukkitRunnable() {
								public void run() {
							//for (SpleefPlayer sp : getPlayers()) {
								//if (sp.getOfflinePlayer().isOnline()) sp.getPlayer().setVelocity(new Vector(0,0,0));
							//}		
				if (getMiniSpleefRound()<=1) {
	  	   	for (SpleefPlayer s : getDuelPlayers1()) {
	  	   		if(!point) {
	  	   		if (getDeadPlayers1().contains(s)) {
	  	   			continue;  	   		
	  	   	}}
	  	   		
	  	   	s.getPlayer().teleport(getShrinkedDuelSpawn1());
	  	   	} 	
	  	   	
	  	   	
	  		for (SpleefPlayer s : getDuelPlayers2()) {
	  			if(!point)
	  			if (getDeadPlayers2().contains(s)) {
	  			continue;
	  		}
	  			s.getPlayer().teleport(getShrinkedDuelSpawn2());
	  		}
	  		
				
				
	  		for (SpleefPlayer sp : getSpectators()) {
	  			Location l = sp.getPlayer().getLocation();
	  			Location newLoc = l;
	  			newLoc.setYaw((float) (newLoc.getYaw()+0.2));
	  			sp.getPlayer().teleport(newLoc);
			}
				}
				
	  		if (point) {
				GameMode mode = GameMode.SPECTATOR;
				if (getSpleefType().equals(SpleefType.SPLEEF)) {
					mode = GameMode.SURVIVAL;
				} else  {
					mode = GameMode.ADVENTURE;
				}
				
			for (SpleefPlayer sp : getDeadPlayers1()) sp.getPlayer().setGameMode(mode);
			for (SpleefPlayer sp : getDeadPlayers2()) sp.getPlayer().setGameMode(mode);	
			getDeadPlayers1().clear();
			getDeadPlayers2().clear();
			}
								}
							}.runTaskLater(Main.get(),1L);
		}
		}
		public void point(SpleefPlayer sp) {
			//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.point in primary thread");
			resetTimer();
			getResetRequest().clear();
			setCrumbleRequest(null);
			setCrumbleRequest(null);
			resetMiniSpleefTimer();
			resetMiniSpleefRound();
			getEndGameRequest().clear();
			setShrinkedDuelArena1(getDuelArena1());
			setShrinkedDuelArena2(getDuelArena2());
			setShrinkedDuelSpawn1(getDuelSpawn1());
			setShrinkedDuelSpawn2(getDuelSpawn2());		
			
			
			
			
		getDeadPlayers1().clear();
		getDeadPlayers2().clear();
		
			
			if (getDecayTask()!=null) getDecayTask().orderLocations();
			
			List<SpleefPlayer> leaveSpect = new ArrayList<SpleefPlayer>();			
			for (SpleefPlayer spect : getSpectators()) {
			if (getPlayers().contains(spect)) leaveSpect.add(spect);		
			}
			
			
			leaveSpect.forEach((n) -> n.leaveSpectate(false, true,false));
			GameMode mode = GameMode.SPECTATOR;
			if (getSpleefType().equals(SpleefType.SPLEEF)) {
				mode = GameMode.SURVIVAL;
			} else  {
				mode = GameMode.ADVENTURE;
			}
			GameMode m = mode;
			
			new BukkitRunnable() {
				public void run() {
			getPlayers().forEach((p -> {
				
				p.getPlayer().setGameMode(m);
				p.giveGameItems();
				p.getPlayer().setAllowFlight(false);
				p.getPlayer().setFlying(false);
			}));
			}
			}.runTask(Main.get());
			
			
			SpectateManager.getManager().doEverything(this);
			
			if (getDuelPlayers1().size()<1) {
				GameManager.getManager().endGameDuel(this,"Team2",GameEndReason.LOG_OFF);
				return;
			} else if (getDuelPlayers2().size()<1) {
				GameManager.getManager().endGameDuel(this,"Team1",GameEndReason.LOG_OFF);
				return;
			}
						
			int i = 0;
				if (getDuelPlayers1().contains(sp)) {
					setPoints2(getPoints2()+1);
					
					GameManager.getManager().sendSyncMessage(getViewers(),"§6"+ sp.getName()+ "§b fell! §6" + getTeamName(2)+ "§b gets a point.");

					
					
					if (getPoints2()>=getPlayTo()) {
						GameManager.getManager().endGameDuel(this,"Team2",GameEndReason.WINNER);
						return;
					} else {
						setState(GameState.STARTING);
						new ArenaStartingCountdownTask(this,null);
					}
				} else if (getDuelPlayers2().contains(sp)) {
					i=1;
					setPoints1(getPoints1()+1);
						GameManager.getManager().sendSyncMessage(getViewers(),"§6"+ sp.getName()+ "§b fell! §6" + getTeamName(1)+ "§b gets a point.");				
					if (getPoints1()>=getPlayTo()) {
						GameManager.getManager().endGameDuel(this,"Team1",GameEndReason.WINNER);
						return;
					} else {
						setState(GameState.STARTING);
						new ArenaStartingCountdownTask(this,null);
					}
				}	
				
				Integer[] pointhistory = {this.totaltime,i};
				this.pointsHistory.add(pointhistory);
				
				getPlayers().forEach((p) -> p.clearGameInventory());
				
				resizeArena();
				reset(true,false);
		}
		
		
		
		public int getAlivePlayersAmount() {
			int i = 0;
			for (SpleefPlayer sp : getPlayers()) {
				if (!this.getDeadPlayers1().contains(sp) && !this.getDeadPlayers2().contains(sp)) {
					i++;
				}
			}
			return i;
		}
		
		
		public int getAlivePlayers(int team) {
			int i = 0;
			List<SpleefPlayer> list = team == 1 ? this.getDuelPlayers1() : this.getDuelPlayers2();
			
			for (SpleefPlayer sp : list) {
				if (!this.getDeadPlayers1().contains(sp) && !this.getDeadPlayers2().contains(sp)) {
					i++;
				}
			}
			return i;
		}
		


		public void resetArena() {
			Arena arena = this;
			//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.resetArena in primary thread");	

			
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer sp : getPlayers()) {
				if (!getDeadPlayers1().contains(sp) && !getDeadPlayers2().contains(sp)) {
					sp.clearGameInventory();
				}
			}
				}
			}.runTask(Main.get());
			
			new ArenaStartingCountdownTask(arena,null);
			 resizeArena();
			reset(false,false);
		
		}
		

		
		
		public void resizeArena() {
			if (!spleeftype.equals(SpleefType.SPLEEF)) return;
			//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.resizeArena in primary thread");	
			
			boolean bo = false;
			Location a = null;
			Location b = null;					
			
			int size = getAlivePlayersAmount()/2; 
			int i =0;
						
			if (size<=getMaxPlayersSize()) { 
				i = (size-1)*2; 
			} else {
				i = getMaxPlayersSize();
			}			
			
			if (i<0) i = 0;
			
			Location oldArena1 = getDuelArena1();
			
			a = new Location(getArena1().getWorld(),
					getArena1().getX()-i,getArena1().getY(),getArena1().getZ()-i);
			 b = new Location(getArena2().getWorld(),
					getArena2().getX()+i,getArena2().getY(),getArena2().getZ()+i);	 
			 
			 
			 bo = a.getBlockX()==oldArena1.getBlockX() && a.getBlockZ()==oldArena1.getBlockZ();
			 
			 if (!bo) clean(getDuelArena1(),getDuelArena2());
			  
				Location spawn1 = new Location(getSpawn1().getWorld(),
						getSpawn1().getX(),getSpawn1().getY(),getSpawn1().getZ()-i);
				spawn1.setDirection(getSpawn1().getDirection());
				 Location spawn2  = new Location(getSpawn2().getWorld(),
						getSpawn2().getX(),getSpawn2().getY(),getSpawn2().getZ()+i);
				 spawn2.setDirection(getSpawn2().getDirection());				 
				 setDuelSpawn1(spawn1);
				 setDuelSpawn2(spawn2);
				 setDuelArena1(a);
				 setDuelArena2(b);
				 setShrinkedDuelSpawn1(spawn1);
				 setShrinkedDuelSpawn2(spawn2);
				 setShrinkedDuelArena1(a);
				 setShrinkedDuelArena2(b);		
				 
				 if (a.distanceSquared(center) > oldArena1.distanceSquared(center)) {
					 extremeArena1 = a;
					 extremeArena2 = b;
				 }
		}
		

		public void ice() {
			Arena arena = this;

					Location  a = new Location(arena1.getWorld(),arena1.getX()+radious,arena1.getY(),arena1.getZ()+radious);
					Location  b = new Location(arena1.getWorld(),arena1.getX()-radious,arena1.getY(),arena1.getZ()-radious);
				  HashMap<Material,Double> materials = new HashMap<Material,Double>();
					  materials.put(Material.AIR, 85.0);
					  materials.put(Material.PACKED_ICE, 15.0);
					FAWESplinduxAPI.getAPI().replace(a, b, Material.AIR, materials);
				
					new BukkitRunnable() {
					int i = 0;	
					public void run() {
						
						if (!arena.getState().equals(GameState.GAME)) return;
						for (SpleefPlayer sp : getViewers()) sp.getPlayer().playSound(sp.getPlayer().getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
						i++;
		
						if (i>=5) cancel();
						
					}
					
					}.runTaskTimer(Main.get(), 0L, 4L);

							new BukkitRunnable() {
								public void run() {
									if (!arena.getState().equals(GameState.GAME)) return;
									 HashMap<Material,Double> materials = new HashMap<Material,Double>();
									  materials.put(Material.SNOW_BLOCK, 100.0);
									FAWESplinduxAPI.getAPI().replace(a, b, Material.PACKED_ICE, materials);		
									
									new BukkitRunnable() {
										int ii = 0;	
										public void run() {
											if (!arena.getState().equals(GameState.GAME)) return;
											
											for (SpleefPlayer sp : getViewers()) sp.getPlayer().playSound(sp.getPlayer().getLocation(), Sound.BLOCK_GLASS_HIT, 1, 1);	
											ii++;		
											if (ii>=5) cancel();							
										}
										
									}.runTaskTimer(Main.get(), 0L, 4L);
								}
							}.runTaskLater(Main.get(), 60L);

}

		/*
		 * Decay I: 12:50
		 * Decay II: 15:00
		 * Decay III: 17:50
		 * Decay IV: 20:00
		 * Decay V: 22:50 
		 */
		
		
		public int getNextDecay() {
			if (this.getSpleefType().equals(SpleefType.SPLEEF) && this.getGameType().equals(GameType.DUEL)) {
			if (getDecayRound()<=0) {
				return 450 - this.totaltime;
			} if (getDecayRound()<=4) {
					//this.totaltime -  600 - (150*(getDecayRound()-1));
					return 450 + (150*getDecayRound()) - this.totaltime;
					
			}
		}
			return -1;
		}


		private DecayTask decayTask;
		
		
		public void stopDecay() {
			if (decayTask==null) return;
			Bukkit.getScheduler().cancelTask(decayTask.getTask());
		}
		
		public void decay(boolean increase) {
			stopDecay();
			
			if (increase) {
			if (this.getDecayRound()==0) {
				setDecayRound(1);
			} else {
				setDecayRound(getDecayRound()+1);
			}		
			}
			
			decayTask = new DecayTask(this, getDecaySpeed());	
		}
		
		
		public DecayTask getDecayTask() {
			return this.decayTask;
		}


		//Quitar 11 de largo, Quitar 6 de ancho
		public void minispleef() {
				if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.miniSpleef in primary thread");	
				Location a = null;
				Location b = null;			
				
				
				int size = getAlivePlayersAmount()/2; 
				int i =0;
							
				if (size<=getMaxPlayersSize()) { 
					i = (size-1)*2; 
				} else {
					i = getMaxPlayersSize();
				}			
				
				if (i<0) i = 0;

				int x= (6+size)-this.getAlivePlayersAmount()+1;
				int z = (10+size)-this.getAlivePlayersAmount()+1;
				
				
				 a = new Location(getShrinkedDuelArena1().getWorld(),
							getShrinkedDuelArena1().getX()+x,getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ()+z);
					 b = new Location(getShrinkedDuelArena2().getWorld(),
							getShrinkedDuelArena2().getX()-x,getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ()-z);
					 
						Location spawn1 = new Location(getShrinkedDuelSpawn1().getWorld(),
								getShrinkedDuelSpawn1().getX(),getShrinkedDuelSpawn1().getY(),getShrinkedDuelSpawn1().getZ()+z);
						spawn1.setDirection(getSpawn1().getDirection());
						 Location spawn2  = new Location(getShrinkedDuelSpawn2().getWorld(),
								getShrinkedDuelSpawn2().getX(),getShrinkedDuelSpawn2().getY(),getShrinkedDuelSpawn2().getZ()-z);
						 

					 spawn1.setDirection(getSpawn1().getDirection());	
					 spawn2.setDirection(getSpawn2().getDirection());				 
					 setShrinkedDuelSpawn1(spawn1);
					 setShrinkedDuelSpawn2(spawn2);
					 setShrinkedDuelArena1(a);
					 setShrinkedDuelArena2(b);	
					 
					 
					 
					 reset(false,true);
					 
					 for (SpleefPlayer p : getPlayers()) {
						 if (!this.getDeadPlayers1().contains(p) && !this.getDeadPlayers2().contains(p))
					 p.giveGameItems();
					 }

							 setMiniSpleefRound(1);
					
						if (getDecayTask()!=null) getDecayTask().orderLocations();
		}


		private int shrinkedRadious;
		private List<Location> decayFFALocations = new ArrayList<Location>();
		
		public List<Location> getDecayFFALocations() {
			return this.decayFFALocations;
		}
		
		private Set<FFADecayTask> ffadecaytask = new HashSet<FFADecayTask>();
		
		public Set<FFADecayTask> getFFADecayTask() {
			return this.ffadecaytask;
		}
		
		public void shrinkFFA() {
			
				ffadecaytask.clear();
				ffadecaytask.add(new FFADecayTask(this));
				ffadecaytask.add(new FFADecayTask(this));
			
			
			Set<Location> playingLocations  = new HashSet<Location>();
			Set<Location> toDeleteLocations  = new HashSet<Location>();
			
			if (shrinkedRadious==0) {
				shrinkedRadious = (int) (radious*0.75);
			} else {
				shrinkedRadious = (int) (shrinkedRadious*0.75);
			}
			
			new BukkitRunnable() {
				public void run() {
					 int cx = arena1.getBlockX();
					    int cy = arena1.getBlockY();
					    int cz = arena1.getBlockZ();
					    World w = arena1.getWorld();
					    int r = shrinkedRadious;
					    for (int x = cx - r; x <= cx +r; x++) {
					        for (int z = cz - r; z <= cz +r; z++) {
					            if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= r*r) {
					               playingLocations.add(w.getBlockAt(x, cy, z).getLocation());
					            }
					        }
					    }
					    
					    r = radious+1;
					    for (int x = cx - r; x <= cx +r; x++) {
					        for (int z = cz - r; z <= cz +r; z++) {
					            if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= r*r) {
					            	if (!playingLocations.contains(w.getBlockAt(x,cy,z).getLocation())) {
					            		toDeleteLocations.add(w.getBlockAt(x, cy, z).getLocation());
					            	}
					            }
					        }
					    }
					    
					    decayFFALocations.clear();
					    decayFFALocations.addAll(toDeleteLocations);    
					    Collections.shuffle(decayFFALocations);
				}
			}.runTaskAsynchronously(Main.get());
		}
		
		
		
		public void deleteOldBlocks() {
			
			Set<Location> toDeleteLocations  = new HashSet<Location>();
			Set<Location> playingLocations  = new HashSet<Location>();
			new BukkitRunnable() {
				public void run () {
					  Location a = getShrinkedDuelArena1();
					  Location b = getShrinkedDuelArena2();	
					  int ax = a.getBlockX();
					  int az = a.getBlockZ();			  
					  int y = a.getBlockY();		  
					  int bx = b.getBlockX();
					  int bz = b.getBlockZ();	
					  for (int x = ax; x <= bx; x++) {
						  for (int z = az; z <= bz; z++) {
							  Location aire = new Location (a.getWorld(), x, y, z); 
											  if (getSpleefType().equals(SpleefType.SPLEEF)) {
										  if (aire.getBlock().getType().equals(Material.SNOW_BLOCK) || aire.getBlock().getType().equals(Material.CONCRETE_POWDER)) {								  
											  playingLocations.add(aire);
										  }
										  }
											  }
						  }
					
					  
					   a = getDuelArena1();
					   b = getDuelArena2();	
					    ax = a.getBlockX();
						   az = a.getBlockZ();			  
						   y = a.getBlockY();		  
						   bx = b.getBlockX();
						   bz = b.getBlockZ();	
					  for (int x = ax; x <= bx; x++) {
						  for (int z = az; z <= bz; z++) {
							  Location aire = new Location (a.getWorld(), x, y, z); 
											  if (getSpleefType().equals(SpleefType.SPLEEF)) {
										  if ((aire.getBlock().getType().equals(Material.SNOW_BLOCK) || aire.getBlock().getType().equals(Material.CONCRETE_POWDER)) && !playingLocations.contains(aire)) {		
											  toDeleteLocations.add(aire);
										  }
										  }
											  }
						  } 
					  
					  new BukkitRunnable() {
						  public void run() {
						for (Location l : toDeleteLocations) 
							FAWESplinduxAPI.getAPI().placeBlocks(l, l, Material.CONCRETE_POWDER, (byte) 14);
						
						
							new BukkitRunnable() {
								public void run() {
									if (getState().equals(GameState.GAME) && getMiniSpleefRound()>0) {
									for (Location l : toDeleteLocations) {
										if (!l.getBlock().getType().equals(Material.AIR)) {
											FAWESplinduxAPI.getAPI().placeBlocks(l, l, Material.CONCRETE_POWDER, (byte) 14);
										}
									}
									
									new BukkitRunnable() {
										public void run() {
											if (getState().equals(GameState.GAME) && getMiniSpleefRound()>0) {
											for (Location l : toDeleteLocations) {				
												FAWESplinduxAPI.getAPI().placeBlocks(l, l, Material.AIR);
											}
										}
										}
									}.runTaskLater(Main.get(), 40L);
								}
								}
							}.runTaskLater(Main.get(), 40L);
				}
					  }.runTaskLater(Main.get(),5L);
				}
				
			}.runTaskAsynchronously(Main.get());
			
			
		
			
			
		}
		
		public void shrink() {
			Location a = null;
			Location b = null;		
			
			
			 a = new Location(getShrinkedDuelArena1().getWorld(),
						getShrinkedDuelArena1().getX()+1,getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ()+1);
				 b = new Location(getShrinkedDuelArena2().getWorld(),
						getShrinkedDuelArena2().getX()-1,getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ()-1);
				 
				 Location spawn1 = new Location(getShrinkedDuelSpawn1().getWorld(),
							getShrinkedDuelSpawn1().getX(),getShrinkedDuelSpawn1().getY(),getShrinkedDuelSpawn1().getZ()+1);
					spawn1.setDirection(getSpawn1().getDirection());
					 Location spawn2  = new Location(getShrinkedDuelSpawn2().getWorld(),
							getShrinkedDuelSpawn2().getX(),getShrinkedDuelSpawn2().getY(),getShrinkedDuelSpawn2().getZ()-1);
				 
					 if (b.getBlockX()>a.getBlockX() && b.getBlockZ() > a.getBlockZ()) {
					 setShrinkedDuelSpawn1(spawn1);
					 setShrinkedDuelSpawn2(spawn2);
					 setShrinkedDuelArena1(a);
					 setShrinkedDuelArena2(b);				
					 }
					 
					 reset(false, false);
					 for (SpleefPlayer p : getPlayers())
						 p.giveGameItems();
					 deleteOldBlocks();
					 
					 if (getDecayTask()!=null) getDecayTask().orderLocations();
		}


		public int getMiniSpleefRound() {
			return this.minispleefRound;
		}


		public void resetShrinkRadious() {
			this.shrinkedRadious = 0;
		}


		public int getShrinkedRadious() {
			return this.shrinkedRadious;
		}


		public void setTotalTime(int crumble) {
			this.totaltime = crumble;
			
		}

		public void pause() {
			this.getPauseRequest().clear();
			this.state = GameState.PAUSE;
			for (SpleefPlayer sp : getViewers()) sp.sendMessage("§cThe game has been paused! To continue the game use §a/resume");
			
			for (SpleefPlayer sp : this.getPlayers()) {
				if (this.getDeadPlayers1().contains(sp) || this.getDeadPlayers2().contains(sp)) continue;
				sp.getPlayer().setWalkSpeed(0.0F);
				sp.getPlayer().getInventory().setItem(7, DataManager.getManager().gameitems()[11]);
				
			}
		}

		public void unpause() {
			for (SpleefPlayer sp : getPlayers()) {
				sp.getPlayer().setWalkSpeed(0.2F);
				sp.getPlayer().getInventory().setItem(7, DataManager.getManager().gameitems()[10]);
			}
			for (SpleefPlayer sp : getViewers()) sp.sendMessage("§aThe game has been resumed.");
		resetArena();
			
		if (getTotalTime()>=450) decay(false);
			setState(GameState.STARTING);	
			
			
		}
		
		public void redo() {
			this.redorequest.clear();
			if (this.state.equals(GameState.PAUSE)) unpause();
			
			if (this.pointsHistory.size()>1) {
			Integer[] oldpointHistory = this.pointsHistory.get(this.pointsHistory.size()-2);
			Integer[] currentpointHistory = this.pointsHistory.get(this.pointsHistory.size()-1);
			this.totaltime = oldpointHistory[0];
			if (currentpointHistory[1]==0) {
				this.points2--;
			} else {
				this.points1--;
			}
			this.pointsHistory.remove(currentpointHistory);
			} else {
				this.points1=0;
				this.points2=0;
				this.totaltime=0;
				this.pointsHistory.clear();
			}
			/*
			int it = 1;
			for (Integer[] i : pointsHistory) {
				Bukkit.broadcastMessage(it + ". time: "+ i[0]);
				it++;
			}
			*/
			for (SpleefPlayer sp : getViewers()) sp.sendMessage("§aThe last point has been discarded!");
			resetArena();
			
		}
		



}
