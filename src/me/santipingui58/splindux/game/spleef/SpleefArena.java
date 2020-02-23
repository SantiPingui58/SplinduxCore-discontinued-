package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.SpleefKill;
import me.santipingui58.splindux.utils.Utils;

public class SpleefArena {

	private SpleefType type;
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
	private List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> queue = new ArrayList<SpleefPlayer>();
	private List<SpleefKill> kills = new ArrayList<SpleefKill>();
	
	private Location arena1_1vs1;
	private Location arena2_1vs1;
	private Location spawn1_1vs1;
	private Location spawn2_1vs1;
	
	
	private int resetround;
	private int points1;
	private int points2;
	
	private List<SpleefPlayer> resetrequest = new ArrayList<SpleefPlayer>();
	private List<SpleefPlayer> endgamerequest = new ArrayList<SpleefPlayer>();
	
	public SpleefArena(String name,Location mainspawn,Location lobby,Location arena1, Location arena2,SpleefType type) {
		this.name = name;
		this.mainspawn = mainspawn;
		this.lobby = lobby;
		this.arena1 = arena1;
		this.arena2 = arena2;
		this.type = type;
		this.state = GameState.LOBBY;
		this.time = 150;
		
	}

	public SpleefArena(String name,Location spawn1,Location spawn2,Location lobby,Location arena1, Location arena2,SpleefType type) {
		this.name = name;
		this.spawn1 = spawn1;
		this.spawn2 = spawn2;
		this.lobby = lobby;
		this.arena1 = arena1;
		this.arena2 = arena2;
		this.type = type;
		this.state = GameState.LOBBY;
		this.time = 150;
		this.resetround = 0;
		this.arena1_1vs1 = this.arena1;
		this.arena2_1vs1 = this.arena2;
		this.spawn1_1vs1 = this.spawn1;
		this.spawn2_1vs1 = this.spawn2;
		
	}
	
	public List<SpleefPlayer> getResetRequest() {
		return this.resetrequest;
	}
	
	public List<SpleefPlayer> getEndGameRequest() {
		return this.endgamerequest;
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
	
	public List<SpleefKill> getKills() {
		return this.kills;
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
		if (this.type.equals(SpleefType.SPLEEFFFA)) {
		this.time = 150;
		} else if (this.type.equals(SpleefType.SPLEEF1VS1)) {
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
		for (SpleefPlayer s : this.players) {
			viewers.add(s);
		}
		for (SpleefPlayer s : this.queue) {
			if (!viewers.contains(s)) {
			viewers.add(s);
			}
		}
		return viewers;
	}
	
	public List<SpleefPlayer> getPlayers() {
		return this.players;
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
	
	public SpleefType getType() {
		return this.type;
	}
}
