package me.santipingui58.splindux.game.ffa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import de.robingrether.idisguise.iDisguise;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationState;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.sws.SWSManager;
import me.santipingui58.splindux.task.ArenaNewStartTask;
import me.santipingui58.splindux.task.ArenaStartingCountdownTask;
import me.santipingui58.splindux.task.tasks.FFADecayTask;
import me.santipingui58.splindux.utils.Utils;

public class FFAArena {

	
	private Arena arena;
	private Set<SpleefPlayer> queue = new HashSet<SpleefPlayer>();
	private Set<SpleefPlayer> players  = new HashSet<SpleefPlayer>();
	private SpleefType spleefType;
	//HashMap contains the current player with the win streak and the amount of wins, only used in FFA.
		private HashMap<SpleefPlayer,Integer> winstreak = new HashMap<SpleefPlayer,Integer>();
		//List of broken blocks in FFA games, used to check the kills.
		private List<BrokenBlock> brokenblocks = new ArrayList<BrokenBlock>();
		//Mutations and FFAEvent.
		private List<GameMutation> mutations = new ArrayList<GameMutation>();
		private FFAEvent event;
		private List<FFABet> currentBets = new ArrayList<FFABet>();
		private List<FFABet> nextBets = new ArrayList<FFABet>();
		private Set<FFATeam> ffateams = new HashSet<FFATeam>();
		private HashMap<SpleefPlayer,Double> ffaProbabilities = new HashMap<SpleefPlayer,Double>();
		
		
		public FFAArena(SpleefType spleefType) {
			this.spleefType = spleefType;
		}
		
		public Arena getArena() {
			return this.arena;
		}
		
		public Set<SpleefPlayer> getQueue() {
			return this.queue;
		}
		
		public List<BrokenBlock> getBrokenBlocks() {
			return this.brokenblocks;
		}
		
		public HashMap<SpleefPlayer,Integer> getWinStreak() {
			return this.winstreak;
		}
		
		public SpleefType getSpleefType() {
			return this.spleefType;
		}
	
		public List<FFABet> getCurrentFFABet() {
			return this.currentBets;
		}
		
		public List<FFABet>getNextFFABet() {
			return this.nextBets;
		}
		
		public boolean hasBetAlready(SpleefPlayer sp) {

			for (FFABet bet : getNextFFABet()) {
				if (bet.getOwner().equals(sp)) return true;
			}
			return false;
		}
		
		
		
		public HashMap<SpleefPlayer,Double> getFFAProbabilities() {
			return this.ffaProbabilities;
		}

		public Set<FFATeam> getTeams() {
			return this.ffateams;
		}
		
		public void setTeams(Set<FFATeam> set) {
			this.ffateams = set;
		}
		
		
		public FFATeam getTeamByPlayer(UUID sp) {
			for (FFATeam team : getTeams()) {
				if (team.getPlayer1().equals(sp) || (team.getPlayer2()!=null && team.getPlayer2().equals(sp))) {
					return team;
				}
			}
			return null;
		}
		
		public Set<FFATeam> getTeamsAlive() {
			Set<FFATeam> set = new HashSet<FFATeam>();
			for (FFATeam team : getTeams()) {
				if (team.isPlayer1Alive() || team.isPlayer2Alive()) {
					set.add(team);
				}
			}
			return set;
		}
		
		public FFATeam getTeam(UUID sp) {
			for (FFATeam team : getTeams()) {
				if (team.getUUID().equals(sp)) {
					return team;
				}
			}
			return null;
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
	
	public boolean hasSnowRun() {
		boolean arenaFall = false;
			if (arena.getState().equals(GameState.GAME)) {
				return this.hasMutation(MutationType.SNOW_RUN);
			}
		
		return arenaFall;
	}
	
	public boolean hasMutation(MutationType type) {
		for (GameMutation mutation : getInGameMutations()) {
			if (mutation.getType().equals(type)) {
				return true;
			}
		}
		return false;
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
	
	public Set<SpleefPlayer> getViewers(){
		Set<SpleefPlayer> viewers = new HashSet<SpleefPlayer>();
		viewers.addAll(this.players);
		viewers.addAll(this.queue);
		return viewers;
	}
	
	
	
	/**
    * Give players effects/items of the mutation that is about to start in the next game, also sends a broadcast of the mutations that are currently in the game.
    */
public void playMutations() {
		arenaMutations();
		List<String> list = new ArrayList<String>();
	for (GameMutation mutation : getInGameMutations()) {
		list.add(mutation.getType().getTitle());
		for (SpleefPlayer sp : this.players) {			
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
		} else if (mutation.getType().equals(MutationType.LEVITATION_I) 
				|| mutation.getType().equals(MutationType.LEVITATION_II)
						|| mutation.getType().equals(MutationType.LEVITATION_V)
								|| mutation.getType().equals(MutationType.LEVITATION_X)) {
					mutation.levitationSpleef();
				}
		}
	}



public void calculateFFAProbabilitiess() {
	HashMap<SpleefPlayer,Double> hashmap = new HashMap<SpleefPlayer,Double>();
int totalWins = 0;
int size = this.players.size();
			for (SpleefPlayer sp : this.players) {
				totalWins = totalWins+sp.getPlayerStats().getMonthlyFFAWins(SpleefType.SPLEEF);
			}
			
			for (SpleefPlayer sp : this.players) {
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


public void startGame() {
	arena.setState(GameState.STARTING);	
	arena.resetTimer();	
	arena.resetTotalTime();
	arena.reset(false,true);	
	HashMap<SpleefPlayer, Location> teleporting = new HashMap<SpleefPlayer,Location>();
	List<ArmorStand> armorstands = new ArrayList<ArmorStand>();
	
	this.queue = Utils.getUtils().newShuffledSet(this.queue);
	updateMutations();		
	playMutations();
	if (this.getInGameMutations().size()==0) {
		for (SpleefPlayer sp : this.queue)
		SWSManager.getManager().addPoints(sp, 1, true,true);
	}
	Location center = new Location(arena.getMainSpawn().getWorld(),arena.getMainSpawn().getX(),arena.getMainSpawn().getY()+1,arena.getMainSpawn().getZ());	

	Bukkit.getScheduler().runTask(Main.get(), () -> {
		
		
		List<SpleefPlayer> queue = new ArrayList<SpleefPlayer>(this.queue);
		
		for(SpleefPlayer sp : queue) {
			Party party = PartyManager.getManager().getParty(sp.getPlayer());
			if (party!=null && party.isLeader(sp.getPlayer())) {
				party.warp(null);
			}
		}

		
		center.getChunk().load();
		List<SpleefPlayer> toRemove = new ArrayList<SpleefPlayer>();
		for (SpleefPlayer sp : this.queue) {
			if (!sp.getOfflinePlayer().isOnline()) {
				toRemove.add(sp);
				continue;
			}
			
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


		        
		        if (getSpleefType().equals(SpleefType.TNTRUN) || getSpleefType().equals(SpleefType.SPLEGG)) {
					sp.getPlayer().setGameMode(GameMode.ADVENTURE);
					sp.getPlayer().teleport(center);
					this.players.add(sp);
				}
		        
		}
		
		
		
		if (getSpleefType().equals(SpleefType.SPLEEF)) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.get(), () -> {
			List<Location> locations = Utils.getUtils().getCircle(center,arena.getName().equalsIgnoreCase("ffa") ?16 : 24, Main.ffa2v2 ? getTeamsAlive().size() : this.queue.size());
			int i = 0;	
			if (Main.ffa2v2) {
				for (SpleefPlayer sp : this.queue) {						
					this.players.add(sp);	
					if (teleporting.containsKey(sp)) continue;
					Location l = null;
							try {
						l = locations.get(i);
					}  catch(Exception ex) {
						i = i-1;
						
					}
						l=locations.get(i);
						try {
					Location location = Utils.getUtils().lookAt(l, center).add(0,-1,0);
					FFATeam team = getTeamByPlayer(sp.getUUID());
					teleporting.put(SpleefPlayer.getSpleefPlayer(team.getPlayer1()), location);	
					if (team.getPlayer2()!=null) teleporting.put(SpleefPlayer.getSpleefPlayer(team.getPlayer2()), location);	
					i++;
				}catch(Exception ex) {
					
				}
				} 
				} else {
			for (SpleefPlayer sp : this.queue) {
				this.players.add(sp);
				Location l = locations.get(i);		
					Location location = Utils.getUtils().lookAt(l, center).add(0,-1,0);
				teleporting.put(sp, location);	
				i++;
			}
			
				}
			
			new BukkitRunnable() {
				public void run() {	
						for (SpleefPlayer sp1 : players) {
							
							if (!hasMutation(MutationType.MINI_SPLEEF)) {
							sp1.teleport(teleporting.get(sp1));
							ArmorStand armorstand = (ArmorStand) center.getWorld().spawnEntity(teleporting.get(sp1).clone().add(0,-1,0), EntityType.ARMOR_STAND);
					        armorstand.setGravity(false);
					        armorstand.addPassenger(sp1.getPlayer());
					        armorstand.setCustomNameVisible(false);
					        armorstand.setInvulnerable(true);
					        armorstand.setVisible(false);
					        armorstands.add(armorstand);		
							} else {
								sp1.teleport(arena.getMainSpawn());
							}
							
							
					    	sp1.getPlayer().setGameMode(GameMode.SURVIVAL);	
					}		
						
						new ArenaStartingCountdownTask(arena,armorstands);	
						cancel();
				}
			}.runTaskTimer(Main.get(), 2L, 2L);	
		});
		} else  {
			for (SpleefPlayer sp : this.queue) {
				this.players.add(sp);
				sp.teleport(arena.getMainSpawn());
			}
			new ArenaStartingCountdownTask(arena,null);	
		}
		});
	
	

}


public void endGame(GameEndReason reason) {		
	if (this.players.size()<=0) reason = GameEndReason.LOG_OFF;
	//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("EndGameFFA in primary thread");
	int s = 5;
	if (isInEvent()) {
		s = 15;
	}		
	
	arena.setState(GameState.FINISHING);
	arena.resetTimer();
	
	getBrokenBlocks().clear();
	arena.resetShrinkRadious();
	
	for (FFATeam team : getTeams()) {
		team.revive();
	}
	
	for (FFADecayTask task : arena.getFFADecayTask()) 	Bukkit.getScheduler().cancelTask(task.getTask());
	
	

	if (reason.equals(GameEndReason.WINNER)) {
		SpleefPlayer winner = null;
		for (SpleefPlayer spp : this.players) {
			winner = spp;
			break;
		}
	
		
		//winner.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
		
	for (FFABet bet : getCurrentFFABet()) {
		if (bet.getBet().equals(winner)) {
			int amount = (int) (bet.getAmount()*getFFAProbabilities().get(winner));
			bet.getOwner().addCoins(amount);
			GameManager.getManager().sendSyncMessage(arena.getViewers(),"§b"+bet.getOwner().getName()+"§ahas won §6" + amount+ " Coins §a for betting for §b§l " + bet.getBet().getOfflinePlayer().getName()+"§a!");
		}
	}
		
		getCurrentFFABet().clear();
		getCurrentFFABet().addAll(getNextFFABet());
		getNextFFABet().clear();

		if (isInEvent()) {
		if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
			if (Main.ffa2v2) {
				getEvent().addPoint2v2(winner.getUUID(), 15,false,true);
		} else {
			getEvent().addPoint(winner.getUUID(), 15,false);
		}
		}
		}
		
		
		winner.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
		LevelManager.getManager().addLevel(winner, 2);
		SWSManager.getManager().addPoints(winner, this.queue.size()<=34 ? 3+(this.queue.size()/2) : 20, true,true);
		EconomyManager.getManager().addCoins(winner, 15, true,false);
		SpleefPlayer winstreaker = null;
		int wins = 0;
		boolean isover = false;
	winner.getPlayer().playSound(arena.getLobby(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
	if (getWinStreak().isEmpty()) {
		getWinStreak().put(winner, 1);
	} else {
		if (getWinStreak().containsKey(winner)) {
			int w = getWinStreak().get(winner)+1;
			 getWinStreak().put(winner, w);
		} else {
			isover = true;
			Iterator<Entry<SpleefPlayer, Integer>> it = getWinStreak().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
		         winstreaker = pair.getKey();		  
		        wins = pair.getValue();
		    }
		    getWinStreak().clear();
			 getWinStreak().put(winner, 1);
		}
	}
	
	
		String name = Main.ffa2v2 ? getTeamByPlayer(winner.getUUID()).getName() : winner.getName();
		DataManager.getManager().broadcast(GameManager.getManager().getGamePrefix(arena)+"§b"+name+" §bhas won the game!",true);
		if (getQueue().size()>=3) {
			DataManager.getManager().broadcast(GameManager.getManager().getGamePrefix(arena)+"§bGame is starting in "+ s + " seconds.",true);
	}
		if (isover) {
			if (wins>1) {
				DataManager.getManager().broadcast(GameManager.getManager().getGamePrefix(arena)+"§bWin Streak of " + winstreaker.getName() + " is over! Total wins: " + wins,true);
		}
		}
		
	
	
	
	
	winner.addFFAWin(arena.getSpleefType());
	winner.giveQueueItems(false,true,true);
	
	this.players.remove(winner);
	arena.reset(false,false);
	
	} else if (reason.equals(GameEndReason.TIME_OUT)) {
		
		SpleefPlayer winstreaker = null;
		int wins = 0;
		Iterator<Entry<SpleefPlayer, Integer>> it = getWinStreak().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
	         winstreaker = pair.getKey();		  
	        wins = pair.getValue();
	    }
	    if (isInEvent()) {
	    for (SpleefPlayer alive : getPlayers()) {		   
			getEvent().addPoint(alive.getUUID(), 1,false);
			}	
	    }

	    
	    DataManager.getManager().broadcast(GameManager.getManager().getGamePrefix(arena)+"§bNobody has won because the game took too long!",true);
			if (arena.getQueue().size()>=3) {
				DataManager.getManager().broadcast(GameManager.getManager().getGamePrefix(arena)+"§bGame is starting in "+ s + " seconds.",true);
			
			}  
			
			if (wins>1) {
				DataManager.getManager().broadcast(GameManager.getManager().getGamePrefix(arena)+"§bWin Streak of " + winstreaker.getName() + " is over! Total wins: " + wins,true);
			}
			
			
		
			getWinStreak().clear();
		
		new BukkitRunnable() {
			public void run() {
		for (SpleefPlayer p : getPlayers()) {
			p.getPlayer().teleport(arena.getLobby());
			p.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
			p.giveQueueItems(false,true,true);						
		}
		}
		}.runTask(Main.get());
		
		getPlayers().clear();
		arena.reset(false,false);
	
	} else if (reason.equals(GameEndReason.LOG_OFF)) {
		
		SpleefPlayer winstreaker = null;
		int wins = 0;
		Iterator<Entry<SpleefPlayer, Integer>> it = getWinStreak().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
	         winstreaker = pair.getKey();		  
	        wins = pair.getValue();
	    }
	    
	    final int ss = s;
	    final int winss = wins;
	    final SpleefPlayer winstreakers = winstreaker;
	    new BukkitRunnable() {
	    	public void run () {
		for (SpleefPlayer players : DataManager.getManager().getPlayers()) {
			players.getPlayer().sendMessage(GameManager.getManager().getGamePrefix(arena)+"§bNobody has won the game.");
			if (arena.getQueue().size()>=3) {
				players.getPlayer().sendMessage(GameManager.getManager().getGamePrefix(arena)+"§bGame  is starting in "+ ss + " seconds.");
			
			}  
			
			if (winss>1) {
				players.getPlayer().sendMessage(GameManager.getManager().getGamePrefix(arena)+"§bWin Streak of " + winstreakers.getName() + " is over! Total wins: " + winss);
			}
		}
	    }
	    }.runTask(Main.get());
	}
	
	


	if (spleefType.equals(SpleefType.SPLEEF)) {
		GameManager.getManager().checkSpleefFFAArena(this);
		} else {
			Arena newarena = null;
			DataManager.getManager().getFFAArenas(arena.getSpleefType()).remove(arena);
			if (DataManager.getManager().getFFAArenas(arena.getSpleefType()).size()==0) {
				DataManager.getManager().loadFFAArenasRotate(spleefType,getQueue().size());
			} 
			
			newarena = DataManager.getManager().getFFAArenas(spleefType).get(DataManager.getManager().getFFAArenas(spleefType).size()-1);
		
			this.setArena(newarena);
			if (getQueue().size()>=3) {
				new ArenaNewStartTask(newarena);
				newarena.setState(GameState.STARTING);
			} else {
				newarena.setState(GameState.LOBBY);
			}
			
			
		}
	
	if (isInEvent()) {
		FFAEvent event = getEvent();
		if (event!=null) {
		event.finishRound();
		getEvent().getRoundPoints().clear();
		getEvent().getRoundPoints2v2().clear();
		}
	}
	
	

}

public Set<SpleefPlayer> getPlayers() {
	return this.players;
}

public void setArena(Arena arena2) {
	this.arena = arena2;
}
		
}
