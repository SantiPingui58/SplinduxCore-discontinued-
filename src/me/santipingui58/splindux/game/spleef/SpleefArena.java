package me.santipingui58.splindux.game.spleef;

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
import org.bukkit.util.Vector;

import de.robingrether.idisguise.iDisguise;
import me.santipingui58.fawe.FAWESplinduxAPI;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.FFAEvent;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationState;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.task.ArenaNewStartTask;
import me.santipingui58.splindux.task.ArenaStartingCountdownTask;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SpleefArena {

	private SpleefType spleeftype;
	private GameType gametype;
	private Location mainspawn;
	private Location lobby;
	private Location arena1;
	private Location arena2;
	private Location spawn1;
	private Location spawn2;
	private String name;
	private int min;
	private int max;
	private int teamsize;
	private boolean ranked;
	private HashMap<SpleefPlayer,Integer> winstreak = new HashMap<SpleefPlayer,Integer>();
	private int time;
	private int totaltime;
	private GameState state;
	private List<SpleefPlayer> FFAplayers = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> spleefDuelPlayers1 = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> spleefDuelPlayers2 = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> duelDeadPlayers1 = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> duelDeadPlayers2 = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> queue = new ArrayList<SpleefPlayer>();
	private List<BrokenBlock> brokenblocks = new ArrayList<BrokenBlock>();
	
	private Location arena1_1vs1;
	private Location arena2_1vs1;
	private Location spawn1_1vs1;
	private Location spawn2_1vs1;
	
	private Location shrinked_arena1_1vs1;
	private Location shrinked_arena2_1vs1;
	private Location shrinked_spawn1_1vs1;
	private Location shrinked_spawn2_1vs1;
	
	private Material item;
	private int resetround;
	private int points1;
	private int points2;
	private int playto;
	private List<SpleefPlayer> resetrequest = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> endgamerequest = new ArrayList<SpleefPlayer>();
	
	
	
	
	
	private Request crumbleRequest;
	private Request playtoRequest;
	private boolean isRecording;
	private boolean recordingRequest;
	private GameReplay replay;
	private List<GameMutation> mutations = new ArrayList<GameMutation>();
	private FFAEvent event;
	
	public SpleefArena(String name,Location mainspawn,Location lobby,Location arena1, Location arena2,SpleefType spleeftype,GameType gametype) {
		this.name = name;
		this.mainspawn = mainspawn;
		this.lobby = lobby;
		this.arena1 = arena1;
		this.arena2 = arena2;
		this.spleeftype = spleeftype;
		this.gametype = gametype;
		this.state = GameState.LOBBY;
		this.time = 150;
		
		
	}

	public SpleefArena(String name,Location spawn1,Location spawn2,Location arena1, Location arena2,SpleefType spleeftype, GameType gametype,Material item,int min,int max) {
		this.name = name;
		this.spawn1 = spawn1;
		this.spawn2 = spawn2;
		this.arena1 = arena1;
		this.arena2 = arena2;
		this.spleeftype = spleeftype;
		this.gametype = gametype;
		this.state = GameState.LOBBY;
		this.time = 150;
		this.resetround = 12;
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
	}
	
	
	public boolean isRanked() {
		return this.ranked;
	}
	
	public void ranked(boolean b) {
		this.ranked = b;
	}
	
	public void setTeamSize(int i) {
		this.teamsize = i;
	}
	
	public int getTeamSize() {
		return this.teamsize;
	}
	
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
	public List<GameMutation> getInGameMutations() {
		List<GameMutation> list = new ArrayList<GameMutation>();
		for (GameMutation mutation : this.mutations) {
			if (mutation.getState().equals(MutationState.INGAME)) {
			list.add(mutation);
			}
		}
		
		return list;
		
	}
	
	public List<GameMutation> getQueuedMutations() {
		List<GameMutation> list = new ArrayList<GameMutation>();
		for (GameMutation mutation : this.mutations) {
			if (mutation.getState().equals(MutationState.QUEUE)) {
			list.add(mutation);
			}
		}
		
		return list;
		
	}
	
	public List<GameMutation> getVotingMutations() {
		List<GameMutation> list = new ArrayList<GameMutation>();
		for (GameMutation mutation : this.mutations) {
			if (mutation.getState().equals(MutationState.VOTING)) {
			list.add(mutation);
			}
		}
		
		return list;
		
	}
	
	public GameMutation getMutationBy(UUID uuid) {
		for (GameMutation mutation : this.mutations) {
			if (mutation.getUUID().compareTo(uuid)==0) {
				return mutation;
			}
		}
		return null;
	}
	
	public List<GameMutation> getAllMutations() {
		return this.mutations;
	}
	
	public boolean isInEvent() {
		if (this.event==null) {
			return false;
		}
		return true;
	}
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
	
	public Request getCrumbleRequest() {
		return this.crumbleRequest;
	}
	
	public void setCrumbleRequest(Request request) {
		this.crumbleRequest= request;
	}
	
	public Request getPlayToRequest() {
		return this.playtoRequest;
	}
	
	public void setPlayToRequest(Request request) {
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
		this.resetround = 12+i;
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
			if (this.resetround>=12) {
				this.time = 180;
			} else if (this.resetround==11){
				this.time=165;
			}else if (this.resetround==10){
				this.time=150;
			}else if (this.resetround==9){
				this.time=135;
			}else if (this.resetround==8){
				this.time=120;
			}else if (this.resetround==7){
				this.time=105;
			}else if (this.resetround==6){
				this.time=90;
			}else if (this.resetround==5){
				this.time=75;
			}else if (this.resetround==4){
				this.time=60;
			}else if (this.resetround==3){
				this.time=45;
			}else if (this.resetround==2){
				this.time=30;
			}else if (this.resetround==1){
				this.time=30;
			}
			
		}
	}
	public List<SpleefPlayer> getViewers() {	
		List<SpleefPlayer> viewers = new ArrayList<SpleefPlayer>();
		if (this.gametype.equals(GameType.FFA)) {
		for (SpleefPlayer s : this.FFAplayers) {
			viewers.add(s);
		}
		} else if (this.gametype.equals(GameType.DUEL)) {
			for (SpleefPlayer s : getPlayers()) {
				viewers.add(s);
			}
			
			for (SpleefPlayer s : getPlayers()) {
				for (SpleefPlayer sp : s.getSpectators()) {
					if (!viewers.contains(sp))
					viewers.add(sp);
				}
			}
		}
		for (SpleefPlayer s : this.queue) {
			if (!viewers.contains(s)) {
			viewers.add(s);
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
	
	public Location getMainSpawn() {
		return this.mainspawn;
	}
	
	public Location getLobby() {
		if (this.lobby==null) {
			return Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);			
		}
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
	
	
	
	 public void addFFAQueue(SpleefPlayer sp) {
		 if (!this.gametype.equals(GameType.FFA)) return;
		sp.leave(false);
			getQueue().add(sp);		
				
			sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
			for (GameMutation mutation : this.getVotingMutations()) {
				mutation.mutationMessage(sp);
			}
			if (getState().equals(GameState.GAME) || getState().equals(GameState.FINISHING) || getState().equals(GameState.STARTING)) {
				sp.getPlayer().sendMessage("§aYou have been added to the queue, you will play when the next game starts!");
				sp.getPlayer().teleport(getLobby());
				sp.giveQueueItems(false,true);	
			} else {
				for (SpleefPlayer p : getQueue()) {
					p.getPlayer().sendMessage("§6" + sp.getName() + " §ahas joined the queue! Total: " + getQueue().size());
				}			
				sp.giveQueueItems(true,true);	
				
				sp.getPlayer().teleport(getMainSpawn());
				if (getQueue().size()>=3) {
					new ArenaNewStartTask(this);
					setState(GameState.STARTING);
					int s = 5;
					if (isInEvent()) {
						s= 15;
					}
					for (SpleefPlayer p : getQueue()) {					
						p.getPlayer().sendMessage("§bSpleef is starting in " + s +" seconds.");
					}}}
			
			
			
			
		}
	 
	 
	 public void addDuelQueue(SpleefPlayer sp, int teamSize,boolean ranked) {
		 if (!this.gametype.equals(GameType.DUEL)) return;
		 this.teamsize= teamSize;
		 this.ranked = ranked;
		 sp.leave(false);
		 getQueue().add(sp);
		 sp.giveQueueItems(false,false);		
		 sp.getPlayer().sendMessage("§aYou have been added to the queue for " + this.spleeftype.toString()+ " " + this.teamsize + "V" + this.teamsize);
	 }
	 
		public void leaveQueue(SpleefPlayer sp) {		
			sp.leaveSpectate(true);
			sp.giveLobbyItems();
			if (sp.isInArena()) {
			if (sp.getPlayer().isOnline()) {
				if (Main.arenas.getConfig().contains("mainlobby")) {
					sp.getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}				
			sp.getPlayer().sendMessage("§aYou have left the queue.");		
			sp.setScoreboard(ScoreboardType.LOBBY);
			}
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
					List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
					getFFAPlayers().remove(sp);				
					players.addAll(getFFAPlayers());					
					if (players.size()<=1) {
						if (getGameType().equals(GameType.FFA)) {
							GameManager.getManager().endGameFFA(this, GameEndReason.WINNER);
						}}}} 
			
				getQueue().remove(sp);
				
		}
		}
	 
		
		public void startGame(boolean sortQueue) {		
			setState(GameState.STARTING);		
			resetTimer();	
			if (getGameType().equals(GameType.FFA)) {
				
				updateMutations();
				reset(false,true);
				
				
			Location center = new Location(getMainSpawn().getWorld(),getMainSpawn().getX(),getMainSpawn().getY()+1,getMainSpawn().getZ());
			Location center_player = new Location(center.getWorld(),center.getX(),center.getY()+1,center.getZ());		
			List<Location> locations = Utils.getUtils().getCircle(center,16, getQueue().size());
			int i = 0;
			for (SpleefPlayer sp : getQueue()) {
				iDisguise.getInstance().getAPI().undisguise(sp.getPlayer());
				sp.giveGameItems();
				sp.addFFAGame();			
				LevelManager.getManager().addLevel(sp, 1);
				Location l = locations.get(i);		
				sp.getPlayer().teleport(Utils.getUtils().lookAt(l, center_player));
				sp.getPlayer().setGameMode(GameMode.SURVIVAL);
				sp.setScoreboard(ScoreboardType.FFAGAME_GAME);
				sp.stopfly();
				getFFAPlayers().add(sp);
				i++;
			}
			
			playMutations();
			} else if (getGameType().equals(GameType.DUEL)) {
				
				int i = 0;
				if (sortQueue) Collections.shuffle(queue);
				int teamSize = getQueue().size()/2;		
				this.teamsize= teamSize;
				for (SpleefPlayer sp : getQueue()) {
					iDisguise.getInstance().getAPI().undisguise(sp.getPlayer());
					if (i<=getQueue().size()) {
					if (getSpleefType().equals(SpleefType.SPLEEF)) {
					sp.getPlayer().setGameMode(GameMode.SURVIVAL);
					} else if (getSpleefType().equals(SpleefType.SPLEGG)) {
						sp.getPlayer().setGameMode(GameMode.ADVENTURE);
					}
					sp.setScoreboard(ScoreboardType._1VS1GAME);				
					sp.stopfly();
					
					if (i<teamSize) {
						getDuelPlayers1().add(sp);
					} else {
						getDuelPlayers2().add(sp);
					}
					i++;
					}}				
				expandArena();
				reset(false,false);
				
				for (SpleefPlayer s : getPlayers()) {
					getQueue().remove(s);
				}
				for (SpleefPlayer sp :getDuelPlayers1()) sp.giveGameItems();
				for (SpleefPlayer sp :getDuelPlayers2()) sp.giveGameItems();
				List<Player> players = new ArrayList<Player>();
				for (SpleefPlayer sp : getPlayers()) {
					players.add(sp.getPlayer());
				}		
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!players.contains(p)) {
					TextComponent message = new TextComponent("§aA game between §b" + getTeamName(1) + " §aand §b" + getTeamName(2) + " §ahas started! §7(Right click to spectate)");
					message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/spectate "+getDuelPlayers1().get(0).getOfflinePlayer().getName()));
					message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Spectate §a" +getTeamName(1) + " §7-§a " + getTeamName(2)).create()));
						p.spigot().sendMessage(message);
					}}}
			
			for (SpleefPlayer s : getPlayers()) {
				EconomyManager.getManager().addCoins(s, 2, true,false);
				s.add1vs1Games();
			}
			for (SpleefPlayer s : getDuelPlayers1()) s.getPlayer().teleport(getShrinkedDuelSpawn1());
			for (SpleefPlayer s : getDuelPlayers2()) s.getPlayer().teleport(getShrinkedDuelSpawn2());
			new ArenaStartingCountdownTask(this);	
		}
		
		
		
		public void crumbleArena(int por) {
				setCrumbleRequest(null);
			
			  Location a = getShrinkedDuelArena1();
			  Location b = getShrinkedDuelArena2();	
			  if (this.gametype.equals(GameType.FFA)) {
				  a = getArena1();
				  b = getArena2();
			  }
			  
			  int ax = a.getBlockX();
			  int az = a.getBlockZ();			  
			  int y = a.getBlockY();		  
			  int bx = b.getBlockX();
			  int bz = b.getBlockZ();	
			  SpleefPlayer p1 = null;
			  SpleefPlayer p2 = null;
			  if (this.gametype.equals(GameType.FFA)) {
			  	  p1 = getFFAPlayers().get(0);
			  	  p2 = getFFAPlayers().get(0);
			  } else if (this.gametype.equals(GameType.DUEL)) {
				   p1 = getDuelPlayers1().get(0);
				   p2 = getDuelPlayers2().get(0);	
			  }
			  
			  
			  Location p1block = new Location (p1.getPlayer().getWorld(), p1.getLocation().getBlockX(), p1.getLocation().getBlockY()-1,
					  p1.getLocation().getBlockZ());			  
			  Location p2block = new Location (p2.getPlayer().getWorld(), p2.getLocation().getBlockX(), p2.getLocation().getBlockY()-1,
					  p2.getLocation().getBlockZ());	
			  
			  
			  Location spawn1 = null;
			  Location spawn2 = null;
			  
			  if (this.gametype.equals(GameType.FFA)) {
			  	  spawn1 = getMainSpawn();
			  	  spawn2 = getMainSpawn();
			  } else if (this.gametype.equals(GameType.DUEL)) {
				   spawn1 = new Location (getShrinkedDuelSpawn1().getWorld(),getShrinkedDuelSpawn1().getBlockX(),
						  getShrinkedDuelSpawn1().getBlockY()-1, getShrinkedDuelSpawn1().getBlockZ());			  
				   spawn2 = new Location (getShrinkedDuelSpawn2().getWorld(),getShrinkedDuelSpawn2().getBlockX(),
						  getShrinkedDuelSpawn2().getBlockY()-1, getShrinkedDuelSpawn2().getBlockZ());
			  }
			  
			  for (int x = ax; x <= bx; x++) {
				  for (int z = az; z <= bz; z++) {
					  Location aire = new Location (a.getWorld(), x, y, z); 
					  
					  if ((p1block.getBlockX() == aire.getBlockX() && p1block.getBlockY() == aire.getBlockY() 
							  && p1block.getBlockZ() == aire.getBlockZ()) || (p2block.getBlockX() == aire.getBlockX() && p2block.getBlockY() == aire.getBlockY() 
							  && p2block.getBlockZ() == aire.getBlockZ()) || (spawn1.getBlockX() == aire.getBlockX() && spawn1.getBlockY() == aire.getBlockY() 
							  && spawn1.getBlockZ() == aire.getBlockZ()) ||(spawn2.getBlockX() == aire.getBlockX() && spawn2.getBlockY() == aire.getBlockY() 
							  && spawn2.getBlockZ() == aire.getBlockZ()) ) {
					  } else {

							  int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
							  if (randomNum<por) {
								  for (SpleefPlayer sp : getPlayers()) {
									  if (sp.getPlayer().getLocation().distance(aire)<4) continue;
									  if (getSpleefType().equals(SpleefType.SPLEEF)) {
								  if (aire.getBlock().getType().equals(Material.SNOW_BLOCK)) {
									  aire.getBlock().setType(Material.AIR);
								  }
								  } else if (getSpleefType().equals(SpleefType.SPLEGG)) {
									 if	(aire.getBlock().getType().equals(Material.STAINED_CLAY)) {
										 aire.getBlock().setType(Material.AIR);
												  }}}}}}}
		}
		
		
		public void reset(boolean point,boolean clean) {		
		
			setPlayToRequest(null);
			getEndGameRequest().clear();
			setCrumbleRequest(null);
			getResetRequest().clear();		
			for (SpleefPlayer sp : getPlayers()) {
				 sp.getPlayer().setVelocity(new Vector(0,0,0));
			}		
			Location a = null;
			Location b = null;		
			Location c = getArena1();
			Location d = getArena2();
			if (getGameType().equals(GameType.FFA)) {
				a = getArena1();
				b = getArena2();
			} else if (getGameType().equals(GameType.DUEL)) {
				if (clean) {
					a = getArena1();
					b = getArena2();	
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
						  } else if (getSpleefType().equals(SpleefType.SPLEGG)) {
							  FAWESplinduxAPI.getAPI().placeBlocks(a, b, Material.STAINED_CLAY);
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
				} else if (getSpleefType().equals(SpleefType.SPLEGG)) {
					mode = GameMode.ADVENTURE;
				}
			for (SpleefPlayer sp : getDeadPlayers1()) sp.getPlayer().setGameMode(mode);
			for (SpleefPlayer sp : getDeadPlayers2()) sp.getPlayer().setGameMode(mode);	
			getDeadPlayers1().clear();
			getDeadPlayers2().clear();
			}
	  		
	  	   	}
	 
		}
		
		public void point(SpleefPlayer sp) {

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
			if (getDuelPlayers1().size()<1) {
				GameManager.getManager().endGameDuel(this,"Team2",GameEndReason.LOG_OFF);
				return;
			} else if (getDuelPlayers2().size()<1) {
				GameManager.getManager().endGameDuel(this,"Team1",GameEndReason.LOG_OFF);
				return;
			}
				if (getDuelPlayers1().contains(sp)) {
					setPoints2(getPoints2()+1);
					for (SpleefPlayer players : getViewers()) { 
					players.getPlayer().sendMessage("§6"+ sp.getName()+ "§b fell! §6" + getTeamName(2)+ "§b gets a point.");			
					}
					if (getPoints2()>=getPlayTo()) {
						GameManager.getManager().endGameDuel(this,"Team2",GameEndReason.WINNER);
						return;
					} else {
						setState(GameState.STARTING);
						new ArenaStartingCountdownTask(this);
					}
				} else if (getDuelPlayers2().contains(sp)) {
					setPoints1(getPoints1()+1);
					for (SpleefPlayer players : getViewers()) { 
						players.getPlayer().sendMessage("§6"+ sp.getName()+ "§b fell! §6" + getTeamName(1)+ "§b gets a point.");							
						}
					if (getPoints1()>=getPlayTo()) {
						GameManager.getManager().endGameDuel(this,"Team1",GameEndReason.WINNER);
						return;
					} else {
						setState(GameState.STARTING);
						new ArenaStartingCountdownTask(this);
					}
				}	
				
				reset(true,false);
		}
		
		public void shrink() {
			Location a = null;
			Location b = null;
			if (getResetRound()>=1) {
			if (getResetRound()==12) {
				//COSTADO 2
			 a = new Location(getShrinkedDuelArena1().getWorld(),
					getShrinkedDuelArena1().getX()+2,getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ());
			 b = new Location(getShrinkedDuelArena2().getWorld(),
					getShrinkedDuelArena2().getX()-2,getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ());
			} else if (getResetRound()==11) {
				a = new Location(getShrinkedDuelArena1().getWorld(),
						getShrinkedDuelArena1().getX()+1,getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ()+2);
				 b = new Location(getShrinkedDuelArena2().getWorld(),
						getShrinkedDuelArena2().getX()-1,getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ()-2);
				 
					Location spawn1 = new Location(getShrinkedDuelSpawn1().getWorld(),
							getShrinkedDuelSpawn1().getX(),getShrinkedDuelSpawn1().getY(),getShrinkedDuelSpawn1().getZ()+2);
					spawn1.setDirection(getSpawn1().getDirection());
					 Location spawn2  = new Location(getShrinkedDuelSpawn2().getWorld(),
							getShrinkedDuelSpawn2().getX(),getShrinkedDuelSpawn2().getY(),getShrinkedDuelSpawn2().getZ()-2);
					 spawn2.setDirection(getSpawn2().getDirection());
					 setShrinkedDuelSpawn1(spawn1);
					 setShrinkedDuelSpawn2(spawn2);
					 //VUELTA
			} else if (getResetRound()==10 || getResetRound()==8|| getResetRound()==6|| getResetRound()==4 || getResetRound()>12) {
				a = new Location(getShrinkedDuelArena1().getWorld(),
						getShrinkedDuelArena1().getX()+1,getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ()+1);
				 b = new Location(getShrinkedDuelArena2().getWorld(),
						getShrinkedDuelArena2().getX()-1,getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ()-1);
				 
					Location spawn1 = new Location(getShrinkedDuelSpawn1().getWorld(),
							getShrinkedDuelSpawn1().getX(),getShrinkedDuelSpawn1().getY(),getShrinkedDuelSpawn1().getZ()+1);
					spawn1.setDirection(getSpawn1().getDirection());
					 Location spawn2  = new Location(getShrinkedDuelSpawn2().getWorld(),
							getShrinkedDuelSpawn2().getX(),getShrinkedDuelSpawn2().getY(),getShrinkedDuelSpawn2().getZ()-1);
					 spawn2.setDirection(getSpawn2().getDirection());
					 setShrinkedDuelSpawn1(spawn1);
					 setShrinkedDuelSpawn2(spawn2);
			} else if (getResetRound()==2) {
				//COSTADO 1
				 a = new Location(getShrinkedDuelArena1().getWorld(),
							getShrinkedDuelArena1().getX()+1,getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ());
					 b = new Location(getShrinkedDuelArena2().getWorld(),
							getShrinkedDuelArena2().getX()-1,getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ());
					//LARGO
			}else {
				 a = new Location(getShrinkedDuelArena1().getWorld(),
							getShrinkedDuelArena1().getX(),getShrinkedDuelArena1().getY(),getShrinkedDuelArena1().getZ()+1);
					 b = new Location(getShrinkedDuelArena2().getWorld(),
							getShrinkedDuelArena2().getX(),getShrinkedDuelArena2().getY(),getShrinkedDuelArena2().getZ()-1);
					 
					 Location spawn1 = new Location(getShrinkedDuelSpawn1().getWorld(),
								getShrinkedDuelSpawn1().getX(),getShrinkedDuelSpawn1().getY(),getShrinkedDuelSpawn1().getZ()+1);
					 spawn1.setDirection(getSpawn1().getDirection());
						 Location spawn2  = new Location(getShrinkedDuelSpawn2().getWorld(),
								getShrinkedDuelSpawn2().getX(),getShrinkedDuelSpawn2().getY(),getShrinkedDuelSpawn2().getZ()-1);
						 spawn2.setDirection(getSpawn2().getDirection());
						 setShrinkedDuelSpawn1(spawn1);
						 setShrinkedDuelSpawn2(spawn2);
			}		
			setShrinkedDuelArena1(a);
			setShrinkedDuelArena2(b);
			
			if (getResetRound()>=1) {
			setResetRound(getResetRound()-1);
			}
			}
			new ArenaStartingCountdownTask(this);
			reset(false,false);
		}
		

		public void expandArena() {
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
		
}
