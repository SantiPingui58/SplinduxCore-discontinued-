package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.utils.Utils;

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

	public SpleefArena(String name,Location spawn1,Location spawn2,Location lobby,Location arena1, Location arena2,SpleefType spleeftype, GameType gametype,Material item) {
		this.name = name;
		this.spawn1 = spawn1;
		this.spawn2 = spawn2;
		this.lobby = lobby;
		this.arena1 = arena1;
		this.arena2 = arena2;
		this.spleeftype = spleeftype;
		this.gametype = gametype;
		this.state = GameState.LOBBY;
		this.time = 150;
		this.resetround = 0;
		this.arena1_1vs1 = this.arena1;
		this.arena2_1vs1 = this.arena2;
		this.spawn1_1vs1 = this.spawn1;
		this.spawn2_1vs1 = this.spawn2;
		this.playto = 7;
		this.item= item;	
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
		this.resetround = 0;
	}
	public Location getArena1_1vs1() {
		return this.arena1_1vs1;
	}
	
	
	public void setArena1_1vs1(Location l) {
		this.arena1_1vs1 = l;
	}
	
	public Location getArena2_1vs1() {
		return this.arena2_1vs1;
	}
	
	public void setArena2_1vs1(Location l) {
		this.arena2_1vs1 = l;
	}
	
	public Location getSpawn1_1vs1() {
		return this.spawn1_1vs1;
	}
	
	public void setSpawn1_1vs1(Location l) {
		this.spawn1_1vs1 = l;
	}
	
	
	public Location getSpawn2_1vs1() {
		return this.spawn2_1vs1;
	}
	
	public void setSpawn2_1vs1(Location l) {
		this.spawn2_1vs1 = l;
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
			if (this.resetround==0) {
				this.time = 180;
			} else if (this.resetround==1){
				this.time=165;
			}else if (this.resetround==2){
				this.time=150;
			}else if (this.resetround==3){
				this.time=135;
			}else if (this.resetround==4){
				this.time=120;
			}else if (this.resetround==5){
				this.time=105;
			}else if (this.resetround==6){
				this.time=90;
			}else if (this.resetround==7){
				this.time=75;
			}else if (this.resetround==8){
				this.time=60;
			}else if (this.resetround==9){
				this.time=45;
			}else if (this.resetround==10){
				this.time=30;
			}else if (this.resetround==11){
				this.time=30;
			}else if (this.resetround==12){
				this.time=15;
			}else if (this.resetround==13){
				this.time=15;
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
			p = sp.getOfflinePlayer().getName();	
			}  else {
				p = p+"-" + sp.getOfflinePlayer().getName();
			}
		 }
		 
		return p;
		
	}
}
