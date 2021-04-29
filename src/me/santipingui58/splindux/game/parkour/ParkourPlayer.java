package me.santipingui58.splindux.game.parkour;



import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class ParkourPlayer  {

	private SpleefPlayer sp;
	private int currentLevel;
	private ParkourArena arena;
	private PlayerStats stats;
	public ParkourPlayer(SpleefPlayer sp,int currentLevel,PlayerStats stats) {
	this.sp = sp;
	if (currentLevel==0) currentLevel=1;
	this.currentLevel = currentLevel;
	this.stats = stats;
	}

	public PlayerStats getStats() {
		return this.stats;
	}
	
	public SpleefPlayer getPlayer() {
		return this.sp;
	}

	public int getCurrentLevel() {
	return this.currentLevel;
}
	
	public void beatLevel() {
		if (this.currentLevel<=25) {
		this.currentLevel++;
		}
	}
	
	public boolean hasBeatedLevel(Level level) {
		int i = this.currentLevel;
		int d = level.getLevel();
		return (i>d);
		
	}
	
	public ParkourArena getArena() {
		return this.arena;
	}
	
	public void createArena(Level level,ParkourMode mode) {
		this.arena = new ParkourArena(getPlayer().getPlayer(),ParkourManager.getManager().getNextID(),level,mode);	
	}
	
	public void leaveArena() {
		new BukkitRunnable() {
			public void run() {
				if (getPlayer()!=null && getPlayer().getOfflinePlayer().isOnline()) {
		getPlayer().getPlayer().teleport(Main.lobby);
		getPlayer().giveLobbyItems();
		}
			}
	}.runTask(Main.get());
		this.arena = null;
	}
	public int getRecord(Level level) {
		return getStats().getRecordByLevel(level);
	}

	public void setNewRecord(Level level, int jumpsMade) {
		getStats().setRecordByLevel(level, jumpsMade);
		
	}
	
}
