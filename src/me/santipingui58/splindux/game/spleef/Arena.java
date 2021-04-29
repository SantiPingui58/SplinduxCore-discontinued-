package me.santipingui58.splindux.game.spleef;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.robingrether.idisguise.iDisguise;
import me.santipingui58.fawe.FAWESplinduxAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.FFABet;
import me.santipingui58.splindux.game.FFAEvent;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationState;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.ranked.RankedTeam;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.task.ArenaStartingCountdownTask;
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
	
	//HashMap contains the current player with the win streak and the amount of wins, only used in FFA.
	private HashMap<SpleefPlayer,Integer> winstreak = new HashMap<SpleefPlayer,Integer>();
	
	//Time until reset in Duels, or finish game in FFA.
	private int time;
	
	//Total time of the arena, only increases.
	private int totaltime;
	
	
	private int shrinkTime;
	
	//Current State of the arena.
	private GameState state;
	
	//List of FFA Players.
	private List<SpleefPlayer> FFAplayers = new ArrayList<SpleefPlayer>();
	
	//List of Duel Players per team.
	private List<SpleefPlayer> spleefDuelPlayers1 = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> spleefDuelPlayers2 = new ArrayList<SpleefPlayer>();
	
	//In Team Duels, players will be put in this list temporarily when they die.
	private List<SpleefPlayer> duelDeadPlayers1 = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> duelDeadPlayers2 = new ArrayList<SpleefPlayer>();
	
	private List<UUID> duelTempDisconnectedPlayers1 = new ArrayList<UUID>();
	private List<UUID> duelTempDisconnectedPlayers2 = new ArrayList<UUID>();
	//Players queued for the arena, not used in Ranked.
	private List<SpleefPlayer> queue = new ArrayList<SpleefPlayer>();
	
	private List<SpleefPlayer> spectators = new ArrayList<SpleefPlayer>();
	
	private int maxSpectators;
	
	//List of broken blocks in FFA games, used to check the kills.
	private List<BrokenBlock> brokenblocks = new ArrayList<BrokenBlock>();
	
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
	
	//Increases every reset in a round to calculate the shape of the field when it shrinks.
	private int resetround;
	
	//Points per player in Duels.
	private int points1;
	private int points2;
	
	//Until when a Duel will be played, default is 7.
	private int playto;
	
	//List of players who requested a reset.
	private List<SpleefPlayer> resetrequest = new ArrayList<SpleefPlayer>();
	
	//List of players who requested an endgame.
	private List<SpleefPlayer> endgamerequest = new ArrayList<SpleefPlayer>();
	
	//Custom Request object for crumble and playto.
	private ArenaRequest crumbleRequest;
	private ArenaRequest playtoRequest;
	
	//If the current game is being recorded, not used.
	private boolean isRecording;
	private boolean recordingRequest;
	private GameReplay replay;
	
	//Mutations and FFAEvent.
	private List<GameMutation> mutations = new ArrayList<GameMutation>();
	private FFAEvent event;
	
	//Ranked Teams for Ranked Duels
	private RankedTeam rankedTeam1;
	private RankedTeam rankedTeam2;
	
	private int crumbleLevel;
	
	private List<FFABet> currentBets = new ArrayList<FFABet>();
	private List<FFABet> nextBets = new ArrayList<FFABet>();
	
	private int timed_max_time;
	private boolean tie;
	
	private boolean isGuildGame;
	
	private HashMap<SpleefPlayer,Double> ffaProbabilities = new HashMap<SpleefPlayer,Double>();
	
	
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
	public Arena(String name,Location mainspawn,Location lobby,Location arena1, Location arena2,SpleefType spleeftype,GameType gametype,int min, int max) {
		this.name = name;
		this.mainspawn = mainspawn;
		this.lobby = lobby;
		this.arena1 = arena1;
		this.arena2 = arena2;
		this.spleeftype = spleeftype;
		this.gametype = gametype;
		this.state = GameState.LOBBY;
		this.time = 150;
		this.min = min;
		this.max = max;
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
		this.time = 150;
		this.resetround = 5;
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
		this.shrinkTime = 30;
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
	
	public int getShrinkTime() {
		return this.shrinkTime;
	}
	
	public void resetShrinkTime() {
		this.shrinkTime = 30;
	}
	
	public int getCrumbleLevel() {
		return this.crumbleLevel;
	}
	
	public void setCrumbleLevel(int i) {
		this.crumbleLevel = i;
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
	
	public void setCrumbleAmount(int i) {
		this.crumbleAmount = i;
	}
	
	public SpleefPlayer getDisconnectedPlayer() {
		return this.disconnectedPlayer;
	}
	
	public void setDisconnectedPlayer(SpleefPlayer s) {
		this.disconnectedPlayer = s;
	}
	
	public HashMap<SpleefPlayer,Double> getFFAProbabilities() {
		return this.ffaProbabilities;
	}	
	
	public int getTimedMaxTime() {
		return this.timed_max_time;
	}
	
	public void setTimedMaxTime (int i) {
		this.timed_max_time = i;
	}
	
	
	public List<FFABet> getCurrentFFABet() {
		return this.currentBets;
	}
	
	public List<FFABet> getNextFFABet() {
		return this.nextBets;
	}
	
	public boolean hasBetAlready(SpleefPlayer sp) {

		for (FFABet bet : getNextFFABet()) {
			if (bet.getOwner().equals(sp)) return true;
		}
		return false;
	}
	
	
	
	
	
	public long getArenaStartingCountdownDelay() {
		if (this.getGameType().equals(GameType.DUEL)) {
			if (this.getSpleefType().equals(SpleefType.TNTRUN)) {
				return 30L;
			} else if (this.getSpleefType().equals(SpleefType.SPLEEF)) {
				double r = (double) this.resetround;
				double a = (r*4.25)+3.75;
			if (a<8) {
					a = 8L;
				} else if (a>30L) {
					a = 30L;
				}
				return (long)a+6;
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
	
	public List<SpleefPlayer> getSpectators() {
		return this.spectators;
	}
	
	
	public boolean hasSnowRun() {
		boolean arenaFall = false;
		if (getGameType().equals(GameType.FFA)) {
			for (GameMutation mutation : getInGameMutations()) {
				if (getState().equals(GameState.GAME)) {
				if (mutation.getType().equals(MutationType.SNOW_RUN)) {
					arenaFall = true; break;
				}
				}
			}
		}
		return arenaFall;
	}
	
	public boolean hasMutation(MutationType type) {
		for (GameMutation mutation : getInGameMutations()) {
			if (getState().equals(GameState.GAME)) {
			if (mutation.getType().equals(type)) {
				return true;
			}
			}
		}
		return false;
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
	
	
		/**
	    * Give players effects/items of the mutation that is about to start in the next game, also sends a broadcast of the mutations that are currently in the game.
	    */
	public void playMutations() {
			arenaMutations();
			List<String> list = new ArrayList<String>();
		for (GameMutation mutation : getInGameMutations()) {
			list.add(mutation.getType().getTitle());
			for (SpleefPlayer sp : getFFAPlayers()) {			
			mutation.giveMutationItems(sp);		
			}
		}
		
		if (getInGameMutations().size()>0) {
		String mutations = Utils.getUtils().getNamesFromList(list);
		for (SpleefPlayer sp : this.getViewers()) {
			sp.getPlayer().sendMessage("§dMutations for this round: " + mutations);
		} 
		}
	}
	
		/**
	    * Some mutations are more special than others and needs a custom code to work.
	    */
	public void arenaMutations() {
		for (GameMutation mutation : getInGameMutations()) {
			if (mutation.getType().equals(MutationType.JUMP_SPLEEF)) {
				mutation.jumpSpleef();
			} else if (mutation.getType().equals(MutationType.MINI_SPLEEF)) {
				mutation.miniSpleef();
			}  else if (mutation.getType().equals(MutationType.CRUMBLE_SPLEEF)) {
				mutation.crumbleSpleef();
			} else if (mutation.getType().equals(MutationType.CREEPY_SPLEEF)) {
				getFFAPlayers().get(0).getPlayer().playSound(getFFAPlayers().get(0).getPlayer().getLocation(), Sound.RECORD_11, 10F, 1F);
			} else if (mutation.getType().equals(MutationType.LEVITATION_I) 
					|| mutation.getType().equals(MutationType.LEVITATION_II)
							|| mutation.getType().equals(MutationType.LEVITATION_V)
									|| mutation.getType().equals(MutationType.LEVITATION_X)) {
						mutation.levitationSpleef();
					}
			}
		}
	
	
	
	
		/**
	    * Called every round to update the state of the mutations and put them in their list. Also removes used mutations, and cleans the arena for TNT_SPLEEF Mutation
	    */
	public void updateMutations() {
		List<GameMutation> toRemove = new ArrayList<GameMutation>();
		for (GameMutation mutation : this.mutations) {
			if (mutation.getState().equals(MutationState.INGAME)) {
				mutation.setState(MutationState.FINISHED);
				if (mutation.getType().equals(MutationType.TNT_SPLEEF)) {
					mutation.clearTNT();
				}
				toRemove.add(mutation);
			} else if (mutation.getState().equals(MutationState.QUEUE)) {
				mutation.setState(MutationState.INGAME);
			}
		}		
		this.mutations.removeAll(toRemove);
	}
	
	
		/**
	    * Mutations that are currently in the round that is being played.
	    * @return List of Mutations that are currently in the game.
	    */
	public List<GameMutation> getInGameMutations() {
		List<GameMutation> list = new ArrayList<GameMutation>();
		for (GameMutation mutation : this.mutations) {
			if (mutation.getState().equals(MutationState.INGAME)) {
			list.add(mutation);
			}
		}	
		return list;		
	}
	
		/**
	    * Mutations that already won the voting process, and are waiting for the round to end to  
	    * @return List of Mutations that are currently in the game.
	    */
	public List<GameMutation> getQueuedMutations() {
		List<GameMutation> list = new ArrayList<GameMutation>();
		for (GameMutation mutation : this.mutations) {
			if (mutation.getState().equals(MutationState.QUEUE)) {
			list.add(mutation);
			}
		}
		
		return list;
		
	}
	
		/**
	    * Mutations that are in the voting process.
	    * @return List of Mutations that are currently in the voting process.
	    */
	public List<GameMutation> getVotingMutations() {
		List<GameMutation> list = new ArrayList<GameMutation>();
		for (GameMutation mutation : this.mutations) {
			if (mutation.getState().equals(MutationState.VOTING)) {
			list.add(mutation);
			}
		}
		return list;
		
	}
	
	
		/**
	    * @param uuid - UUID of the Mutation.
	    * @return Mutation 
	    */
	public GameMutation getMutationBy(UUID uuid) {
		for (GameMutation mutation : this.mutations) {
			if (mutation.getUUID().compareTo(uuid)==0) {
				return mutation;
			}
		}
		return null;
	}
	
	
		/**
	    * @return Returns all Mutations in the game 
	    */
	public List<GameMutation> getAllMutations() {
		return this.mutations;
	}
	
		/**
	    * @return TRUE if there is a FFAEvent going on, FALSE otherwise.
	    */
	public boolean isInEvent() {
		if (this.event==null) {
			return false;
		}
		return true;
	}
	
	
		/**
	    * @return Returns the current FFAEvent, null if there isn't one.
	    */
	public FFAEvent getEvent() {
		return this.event;
	}
	
	
	
	public void setEvent(FFAEvent event) {
		this.event = event;
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
	
	public List<SpleefPlayer> getEndGameRequest() {
		return this.endgamerequest;
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
	
	public int getResetRound() {
		return this.resetround;
	}
	
	public void setResetRound(int i) {
		this.resetround = i;
	}
	
	public void resetResetRound() {
		int size = getPlayers().size()/2;
		int i =0;
		if (size<=this.max) {
			i = size-1;
		} else {
			i = this.max-1;
		}
		
		this.resetround = 5+i;
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
	public HashMap<SpleefPlayer,Integer> getWinStreak() {
		return this.winstreak;
	}
	
	public List<BrokenBlock> getBrokenBlocks() {
		return this.brokenblocks;
	}
	
	public void time() {
		this.time = this.time - 1;
		if (this.shrinkTime>0) this.shrinkTime = this.shrinkTime-1;
	}
	public List<SpleefPlayer> getQueue() {	
		return this.queue;
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
			this.time = getResetTime();
		}
	}
	
	public int getResetTime() {
		if (this.resetround>=5) {
			return 90;
		} else {
		switch(this.resetround) {
		case 0: return 15;
		case 1: return 30;
		case 2: return 45;
		case 3: return 60;
		case 4: return 75;
		}
		}
		return 0;
	}
	public List<SpleefPlayer> getViewers() {	
		List<SpleefPlayer> viewers = new ArrayList<SpleefPlayer>();
		if (this.gametype.equals(GameType.FFA)) {
		for (SpleefPlayer s : this.FFAplayers) {
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
		for (SpleefPlayer s : this.queue) {
			if (!viewers.contains(s)) {
				if (s.getOfflinePlayer().isOnline()) viewers.add(s);
			}
		}
		return viewers;
	}
	
	public List<SpleefPlayer> getFFAPlayers() {
		return this.FFAplayers;
	}
	
	public List<SpleefPlayer> getDuelPlayers1() {
		return this.spleefDuelPlayers1;
	}
	
	public List<SpleefPlayer> getDuelPlayers2() {
		return this.spleefDuelPlayers2;	
	}
	
	
	public List<SpleefPlayer> getDeadPlayers1() {
		return this.duelDeadPlayers1;
	}
	
	public List<SpleefPlayer> getDeadPlayers2() {
		return this.duelDeadPlayers2;	
	}
	
	public List<UUID> getDuelTempDisconnectedPlayers1() {
		return this.duelTempDisconnectedPlayers1;
	}
	
	public List<UUID> getDuelTempDisconnectedPlayers2() {
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
	
	public List<SpleefPlayer> getPlayers() {
		if (this.gametype.equals(GameType.FFA)) {
			return this.FFAplayers;
		} else if (this.gametype.equals(GameType.DUEL)) {
			List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
			list.addAll(spleefDuelPlayers1);
			list.addAll(spleefDuelPlayers2);
			return list;
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
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("addDuelQueue in primary thread");
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
						GameManager.getManager().resetArenaWithCommand(this,false);
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
					List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
					getFFAPlayers().remove(sp);				
					players.addAll(getFFAPlayers());					
					if (players.size()<=1) {
						if (getGameType().equals(GameType.FFA)) {
							GameManager.getManager().endGameFFA(GameEndReason.WINNER,this.getSpleefType());
						}}}} 
			
				getQueue().remove(sp);
				
		}
		}
	 
		
		public void startGame(boolean sortQueue) {		
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.startGame in primary thread");
			
			setState(GameState.STARTING);		
			resetResetRound();
			resetTimer();	
			resetTotalTime();
			resetMaxSpectators();
			reset(false,true);	
			if (getGameType().equals(GameType.FFA)) {
				new BukkitRunnable() {
					public void run() {
				updateMutations();
				
			Location center = new Location(getMainSpawn().getWorld(),getMainSpawn().getX(),getMainSpawn().getY()+1,getMainSpawn().getZ());
		
			Collections.shuffle(getQueue());
			

			center.getChunk().load();

			
			for (SpleefPlayer sp : getQueue()) {
				if (iDisguise.getInstance().getAPI().isDisguised(sp.getPlayer())) {
				iDisguise.getInstance().getAPI().undisguise(sp.getPlayer());
				}

			
				sp.giveGameItems();
				sp.stopfly();
				SpectateManager.getManager().leaveSpectate(sp,false);
				sp.addFFAGame(getSpleefType());			
				LevelManager.getManager().addLevel(sp, 1);
				EconomyManager.getManager().addCoins(sp, 3, false,false);
				sp.setScoreboard(ScoreboardType.FFAGAME_GAME);
				DataManager.getManager().getLobbyPlayers().remove(sp.getUUID());
				DataManager.getManager().getPlayingPlayers().add(sp.getUUID());
			
				getFFAPlayers().add(sp);
			}
			
			
			
			new BukkitRunnable() {
				public void run () {
					int i = 0;												
			for (SpleefPlayer sp : getFFAPlayers()) {
				Location center_player = new Location(center.getWorld(),center.getX(),center.getY()+1,center.getZ());		
				List<Location> locations = Utils.getUtils().getCircle(center,16, getFFAPlayers().size());
				Location l = locations.get(i);		
				if (getSpleefType().equals(SpleefType.TNTRUN) || getSpleefType().equals(SpleefType.SPLEGG)) {
					sp.getPlayer().setGameMode(GameMode.ADVENTURE);
					sp.getPlayer().teleport(center);
				} else {
				sp.getPlayer().teleport(Utils.getUtils().lookAt(l, center_player).add(0,-1,0));
				sp.getPlayer().setGameMode(GameMode.SURVIVAL);
				}
				i++;
			}
				}
			}.runTaskLater(Main.get(), 5L);
			
			playMutations();
			
					}
				}.runTask(Main.get());
			
			} else if (getGameType().equals(GameType.DUEL)) {
				
				int i = 0;
				if (sortQueue) Collections.shuffle(queue);
				int teamSize = getQueue().size()/2;		
				this.teamsize= teamSize;
				for (SpleefPlayer sp : getQueue()) {
					SpectateManager.getManager().leaveSpectate(sp,false);
					iDisguise.getInstance().getAPI().undisguise(sp.getPlayer());
					if (i<=getQueue().size()) {
						
						new BukkitRunnable() {
						public void run () {
						if (sp.getOptions().hasNightVision()) {
							sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,Integer.MAX_VALUE));
							}
						
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
				
				
				if (this.spleeftype.equals(SpleefType.SPLEEF) || this.spleeftype.equals(SpleefType.SPLEGG)) expandArena();
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
					if (sp.getOptions().hasNightVision()) {
						sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,Integer.MAX_VALUE));
						}
					
					if (isRanked() ) {
						sp.setRankeds(sp.getRankeds()-1);
					}
				}		
				TextComponent message =null;
								
				String unranked = GameManager.getManager().getGamePrefix(this)+"§aA game between §b" + getTeamName(1) + " §aand §b" + getTeamName(2) + " §ahas started!";
				if (isRanked()) {
					String ranked = GameManager.getManager().getGamePrefix(this)+"§6[Ranked] §aA game between §b" + getTeamName(1) + "§7(§6"+ rankedTeam1.getELO() + "§7)" + " §aand §b" + getTeamName(2)  + "§7(§6"+ rankedTeam2.getELO() + "§7)"+ " §ahas started!";		
					 message = new TextComponent(ranked + "§7(Right click to spectate)");
					 DataManager.getManager().broadcast(ranked,false);
					 
				} else {
				 message = new TextComponent(unranked+ " §7(Right click to spectate)");
				 DataManager.getManager().broadcast(unranked,false);
				}
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/spectate "+getDuelPlayers1().get(0).getOfflinePlayer().getName()));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Spectate §a" +getTeamName(1) + " §7-§a " + getTeamName(2)).create()));
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!players.contains(p)) {
						p.spigot().sendMessage(message);
					}}}
			
				
			

			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer s : getDuelPlayers1()) s.getPlayer().teleport(getShrinkedDuelSpawn1());
			for (SpleefPlayer s : getDuelPlayers2()) s.getPlayer().teleport(getShrinkedDuelSpawn2());
			}
			}.runTask(Main.get());
			
			new ArenaStartingCountdownTask(this);	
		}
		
		
		
		public void crumbleArena(int por) {
			
			  new BukkitRunnable() {
				  public void run() {
				setCrumbleRequest(null);
				
			  Location a = getShrinkedDuelArena1();
			  Location b = getShrinkedDuelArena2();	
			  if (gametype.equals(GameType.FFA)) {
				  a = getArena1();
				  b = getArena2();
			  }
			  
			  int ax = a.getBlockX();
			  int az = a.getBlockZ();			  
			  int y = a.getBlockY();		  
			  int bx = b.getBlockX();
			  int bz = b.getBlockZ();	
			  
			  List<Location> playerLocations = new ArrayList<Location>();
	  
			  if (gametype.equals(GameType.FFA)) {
				  getFFAPlayers().forEach((p) -> {
				  playerLocations.addAll(Utils.getUtils().getRadioBlocks(p,1));
						  }
						  );
			  } else if (gametype.equals(GameType.DUEL)) {
				  int rad = resetround>=  2 ? 1 : 0; 
				  
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
		  
			  for (int x = ax; x <= bx; x++) {
				  for (int z = az; z <= bz; z++) {
					  Location aire = new Location (aa.getWorld(), x, y, z); 
					  
					  if (!isLocationInside(aire,playerLocations)) {
							  int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
							  if (randomNum<por) {
									  if (getSpleefType().equals(SpleefType.SPLEEF)) {
								  if (aire.getBlock().getType().equals(Material.SNOW_BLOCK)) {
									  aire.getBlock().setType(Material.AIR);
								  }
								  } else if (getSpleefType().equals(SpleefType.SPLEGG)) {
									 if	(aire.getBlock().getType().equals(Material.STAINED_CLAY)) {
										 aire.getBlock().setType(Material.AIR);
												  }}}}}}
			  }
		}.runTaskLater(Main.get(),1L);
		}
		
		
		private boolean isLocationInside(Location location, List<Location> list) {		
			for(Location l : list) {
				if (l.getBlockX() == location.getBlockX() && l.getBlockY() == location.getBlockY() && l.getBlockZ() == location.getBlockZ()) return true;
			}
			return false;
		}
		
		
		
		public void reset(boolean point,boolean clean) {		
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.reset in primary thread");
			setPlayToRequest(null);
			getEndGameRequest().clear();
			setCrumbleRequest(null);
			getResetRequest().clear();		
			
		
			
			Location a = null;
			Location b = null;		
			Location c = getArena1();
			Location d = getArena2();
			if (getGameType().equals(GameType.FFA)) {
				a = getArena1();
				b = getArena2();
			} else if (getGameType().equals(GameType.DUEL)) {
				if (clean) {
					a = getArena1().clone().add(1,0,1);
					b = getArena2().clone().add(-1,0,-1);	
				} else {
				a = getShrinkedDuelArena1();
				b = getShrinkedDuelArena2();	
				}
				c = getDuelArena1();
				 d = getDuelArena2();
			}
			
			
			if (getGameType().equals(GameType.DUEL)) {
			  	FAWESplinduxAPI.getAPI().placeBlocks(c, d, Material.AIR);
			}
						  if (getSpleefType().equals(SpleefType.SPLEEF)) {
								FAWESplinduxAPI.getAPI().placeBlocks(a, b, Material.SNOW_BLOCK);
						  }  else if (getSpleefType().equals(SpleefType.TNTRUN) || getSpleefType().equals(SpleefType.SPLEGG)) {
							  String name = getGameType().equals(GameType.DUEL) ? this.getSchematicName() : this.getName();
							  File file = new File(Main.get().getDataFolder()+"/schematics/"+name+".schematic");
							  FAWESplinduxAPI.getAPI().pasteSchematic(file, this.getMainSpawn(),false);
						  }
						  
						  
							new BukkitRunnable() {
								public void run() {
							for (SpleefPlayer sp : getPlayers()) {
								if (sp.getOfflinePlayer().isOnline()) sp.getPlayer().setVelocity(new Vector(0,0,0));
							}		
							
						  
						  
			if (getGameType().equals(GameType.DUEL)) {
				
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
	  		for (SpleefPlayer sp : getPlayers()) {
				 sp.getPlayer().setVelocity(new Vector(0,0,0));
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
	 
								}
							}.runTask(Main.get());
		}
		
		public void point(SpleefPlayer sp) {
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.point in primary thread");
			
			setCrumbleLevel(0);
			resetResetRound();
			resetTimer();
			getResetRequest().clear();
			setCrumbleRequest(null);
			resetResetRound();
			setCrumbleRequest(null);
			getEndGameRequest().clear();
			setShrinkedDuelArena1(getDuelArena1());
			setShrinkedDuelArena2(getDuelArena2());
			setShrinkedDuelSpawn1(getDuelSpawn1());
			setShrinkedDuelSpawn2(getDuelSpawn2());		
			resetShrinkTime();
			
		
			List<SpleefPlayer> leaveSpect = new ArrayList<SpleefPlayer>();			
			for (SpleefPlayer spect : getSpectators()) {
			if (getPlayers().contains(spect)) leaveSpect.add(spect);		
			}
			
			
			leaveSpect.forEach((n) -> n.leaveSpectate(false, true,false));

			new BukkitRunnable() {
				public void run() {
			getPlayers().forEach((p -> {
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
						
			
				if (getDuelPlayers1().contains(sp)) {
					setPoints2(getPoints2()+1);
					
					GameManager.getManager().sendSyncMessage(getViewers(),"§6"+ sp.getName()+ "§b fell! §6" + getTeamName(2)+ "§b gets a point.");

					
					
					if (getPoints2()>=getPlayTo()) {
						GameManager.getManager().endGameDuel(this,"Team2",GameEndReason.WINNER);
						return;
					} else {
						setState(GameState.STARTING);
						new ArenaStartingCountdownTask(this);
					}
				} else if (getDuelPlayers2().contains(sp)) {
					setPoints1(getPoints1()+1);
						GameManager.getManager().sendSyncMessage(getViewers(),"§6"+ sp.getName()+ "§b fell! §6" + getTeamName(1)+ "§b gets a point.");				
					if (getPoints1()>=getPlayTo()) {
						GameManager.getManager().endGameDuel(this,"Team1",GameEndReason.WINNER);
						return;
					} else {
						setState(GameState.STARTING);
						new ArenaStartingCountdownTask(this);
					}
				}	
				
				getPlayers().forEach((p) -> p.clearGameInventory());
				
				reset(true,false);
		}
		
		
		
		public int getAlivePlayersAmount() {
			int i = 0;
			for (SpleefPlayer sp : getPlayers()) if (!this.getDeadPlayers1().contains(sp) && !this.getDeadPlayers2().contains(sp)) i++;
			return i;
		}


		public void resetArena(boolean shrink,boolean crumble) {
			Arena arena = this;
			new BukkitRunnable() {
				public void run() {
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.resetArena in primary thread");	

			Location a = null;
			Location b = null;
				
			if (shrink) {
				 resetShrinkTime();
				int x = 0;
				int z = 0;
				int maxSize = getResetRound()+(getAlivePlayersAmount()/2);
			
				Bukkit.broadcastMessage("Max Size :" +maxSize);
				Bukkit.broadcastMessage("Reset Round:" +getResetRound());

				
			if (getResetRound()>5) {
				if (getResetRound()>=maxSize) {
					setResetRound(maxSize-1);
					x= getAlivePlayersAmount()/2;
					z = getAlivePlayersAmount()/2;
				} else {
					x=1;
					z=1;
				}
			} else if (getResetRound()>=1){
				switch (getResetRound()) {
				case 5: x=1;z=1;break;
				case 4: x=2;z=2;break;
				case 3: x=0;z=2;break;
				case 2: x=2;z=2;break;
				case 1: x=2;z=2;break;
				case 0: x=1;z=1;break;
				}
			}
				
			
				
				a = new Location(getShrinkedDuelArena1().getWorld(),
						getShrinkedDuelArena1().getX()+x,getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ()+z);
				 b = new Location(getShrinkedDuelArena2().getWorld(),
						getShrinkedDuelArena2().getX()-x,getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ()-z);
				 
					Location spawn1 = new Location(getShrinkedDuelSpawn1().getWorld(),
							getShrinkedDuelSpawn1().getX(),getShrinkedDuelSpawn1().getY(),getShrinkedDuelSpawn1().getZ()+z);
					spawn1.setDirection(getSpawn1().getDirection());
					 Location spawn2  = new Location(getShrinkedDuelSpawn2().getWorld(),
							getShrinkedDuelSpawn2().getX(),getShrinkedDuelSpawn2().getY(),getShrinkedDuelSpawn2().getZ()-z);
					 spawn2.setDirection(getSpawn2().getDirection());
					 
					 
			 setShrinkedDuelSpawn1(spawn1);
			 setShrinkedDuelSpawn2(spawn2);
			setShrinkedDuelArena1(a);
			setShrinkedDuelArena2(b);
			
			if (getResetRound()>=1) {
			setResetRound(getResetRound()-1);
			}
			}
			
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer sp : getPlayers()) {
				if (!getDeadPlayers1().contains(sp) && !getDeadPlayers2().contains(sp)) {
					sp.clearGameInventory();
				}
			}
				}
			}.runTask(Main.get());
			
			new ArenaStartingCountdownTask(arena);
			reset(false,false);
			
			if (crumble) { 
				int level = getCrumbleLevel()*5 <=75 ? 5+ (5*getCrumbleLevel()) : 5*getCrumbleLevel();
				if (level>7 && getCrumbleLevel()>0) crumbleArena(level);
				
				if (getCrumbleLevel()*5 <=75) setCrumbleLevel(getCrumbleLevel()+1);
					
			}
				}
				}.runTask(Main.get());
		}
		

		public void expandArena() {
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Arena.expandArena in primary thread");	
			Location a = null;
			Location b = null;		
			int size = getPlayers().size()/2;
			int i =0;
			if (size<=getMaxPlayersSize()) {
				i = size-1;
			} else {
				i = getMaxPlayersSize()-1;
			}			
			a = new Location(getArena1().getWorld(),
					getArena1().getX()-i,getArena1().getY(),getArena1().getZ()-i);
			 b = new Location(getArena2().getWorld(),
					getArena2().getX()+i,getArena2().getY(),getArena2().getZ()+i);	 
				Location spawn1 = new Location(getSpawn1().getWorld(),
						getSpawn1().getX(),getSpawn1().getY(),getSpawn1().getZ()-i);
				spawn1.setDirection(getSpawn1().getDirection());
				 Location spawn2  = new Location(getSpawn2().getWorld(),
						getSpawn2().getX(),getSpawn2().getY(),getSpawn2().getZ()+i);
				 spawn2.setDirection(getSpawn2().getDirection());				 
				 resetResetRound();
				 setDuelSpawn1(spawn1);
				 setDuelSpawn2(spawn2);
				 setDuelArena1(a);
				 setDuelArena2(b);
				 setShrinkedDuelSpawn1(spawn1);
				 setShrinkedDuelSpawn2(spawn2);
				 setShrinkedDuelArena1(a);
				 setShrinkedDuelArena2(b);	
		}
		
		
		
		public void calculateFFAProbabilitiess() {
			HashMap<SpleefPlayer,Double> hashmap = new HashMap<SpleefPlayer,Double>();
		int totalWins = 0;
		int size = getPlayers().size();
					for (SpleefPlayer sp : getPlayers()) {
						totalWins = totalWins+sp.getPlayerStats().getMonthlyFFAWins(SpleefType.SPLEEF);
					}
					
					for (SpleefPlayer sp : getPlayers()) {
						int wins = sp.getPlayerStats().getMonthlyFFAWins(SpleefType.SPLEEF);
						if (wins<=0) wins = 1;
						
						double percentage = wins/totalWins*100;
						
						//Bukkit.broadcastMessage("a:" + percentage);
						int value = (1/19*size)-(1/19);
						//Bukkit.broadcastMessage("b:" + value);
						double cuota = 100/percentage*value/10;
						//Bukkit.broadcastMessage("c:" + cuota);
						//7,69 * 
						double probability = 1+cuota;
						//Bukkit.broadcastMessage("d:" + probability);
						if (probability>2) probability =2;
						hashmap.put(sp, probability);
					}
					
					this.ffaProbabilities = hashmap;
					
		}


		public void ice() {
			Arena arena = this;
			new BukkitRunnable() {
				public void run() {
					
					
					
				
					Location  a = getArena1();
					Location b = getArena2();
				  HashMap<Material,Double> materials = new HashMap<Material,Double>();
					  materials.put(Material.AIR, 80.0);
					  materials.put(Material.PACKED_ICE, 20.0);
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
				
			}.runTask(Main.get());

}

}
