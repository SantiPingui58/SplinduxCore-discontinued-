package me.santipingui58.splindux.game.parkour;



import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class ParkourPlayer  {

	private UUID uuid;
	private int currentLevel;
	private ParkourArena arena;
	private PlayerStats stats;
	public ParkourPlayer(UUID player,int currentLevel,PlayerStats stats) {
	this.uuid = player;
	if (currentLevel==0) currentLevel=1;
	this.currentLevel = currentLevel;
	this.stats = stats;
	}

	public PlayerStats getStats() {
		return this.stats;
	}
	
	public UUID getPlayer() {
		return this.uuid;
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
		this.arena = new ParkourArena(this,ParkourManager.getManager().getNextID(),level,mode);	
	}
	
	public void leaveArena() {
		new BukkitRunnable() {
			public void run() {
				if (getPlayer()!=null && Bukkit.getOfflinePlayer(getPlayer()).isOnline()) {
					SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(getPlayer());
		sp.getPlayer().teleport(Main.lobby);
		sp.giveLobbyItems();
		}
			}
	}.runTask(Main.get());
	ParkourManager.getManager().getArenas().remove(arena);
		this.arena = null;
	}
	public int getRecord(Level level) {
		return getStats().getRecordByLevel(level);
	}

	public void setNewRecord(Level level, int jumpsMade) {
		getStats().setRecordByLevel(level, jumpsMade);
		
	}
	
}
