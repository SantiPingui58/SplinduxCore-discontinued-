package me.santipingui58.splindux.game.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.fawe.FAWESplinduxAPI;
import me.santipingui58.fawe.Main;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.replay.ReplayManager;
import me.santipingui58.splindux.scoreboard.PinguiScoreboard;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.StatsManager;

public class ParkourArena {

	private int id;
	private Level level;
	private Location currentStart;
	private Location currentFinish;
	private int fails;
	private int jumpsMade;
	private List<UUID> jumpsSchematics;
	private ParkourMode mode;
	private String replayname;
	
	private List<SpleefPlayer> spectators;
	
	public ParkourArena(Player player,int id,Level level,ParkourMode mode) {
		this.level = level;
		this.mode = mode;
		this.jumpsSchematics = new ArrayList<UUID>();
		this.id = id;
		ParkourManager.getManager().getArenas().add(this);
		setup();
		//Player[] players = {player};
		this.replayname = ReplayManager.getManager().getParkourName(player.getName(), this);	
		new BukkitRunnable() {
			public void run() {
		//ReplayAPI.getInstance().recordReplay(replayname, Bukkit.getConsoleSender(), players);
			}
		}.runTaskLater(Main.get(), 10L);
		
	}
	
	public List<SpleefPlayer> getSpectators() {
		return this.spectators;
	}
	
	public String getReplayName() {
		return this.replayname;
	}
	
	public ParkourMode getMode() {
		return this.mode;
	}
	
	public int getJumpsMade() {
		return this.jumpsMade;
	}
	
	public List<UUID> getJumpSchematics() {
		return this.jumpsSchematics;
	}
	public void setJumpsMade(int i) {
		this.jumpsMade = i;
	}
	
	public int getFails() {
		return this.fails;
	}
	
	public void setFails(int i) {
		this.fails = i;
	}
	public Location getCurrentStart() {
		return this.currentStart;
	}
	
	public void setCurrentStart(Location l) {
		this.currentStart = l;
	}
	
	public Location getCurrentFinish() {
		return this.currentFinish;
	}
	
	public void setCurrentFinish(Location l) {
		this.currentFinish = l;
	}
	
	public int getID() {
		return this.id;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public ParkourPlayer getPlayer() {
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			ParkourPlayer pp = sp.getParkourPlayer();
			if (pp.getArena()!=null) {
			if (pp.getArena().equals(this)) {
				return pp;
			}
		}
		}
		return null;
	}
	
	
	public void finish(FinishParkourReason reason) {
		
		ParkourPlayer pp = getPlayer();
		SpleefPlayer sp = pp.getPlayer();
		
		if (reason.equals(FinishParkourReason.WINNER)) {
		if (!pp.hasBeatedLevel(getLevel())) {
			pp.beatLevel();
			EconomyManager.getManager().addCoins(sp, 20*this.level.getLevel(), true, false);
		}
		sp.getPlayer().sendMessage("§aYou have completed §bLevel " + getLevel().getLevel()+"§a!");
	
		} else if (reason.equals(FinishParkourReason.LOST)) {
			
			sp.getPlayer().sendMessage("§cGame finished");
			sp.getPlayer().sendMessage("§aYou made §6§l" + this.jumpsMade + " §aJumps!");
			if (pp.getRecord(level)<this.jumpsMade) {
				sp.getPlayer().sendMessage("§aCongratulations! You set your new personal record at Level" + level.getLevel() + " with " + this.jumpsMade + " Jumps!");
				pp.setNewRecord(level,this.jumpsMade);
				StatsManager.getManager().updateRankings();
			}
		}
		
	//	ReplayAPI.getInstance().stopReplay(this.replayname, true);
		pp.leaveArena();
		sp.setScoreboard(ScoreboardType.LOBBY);
		DataManager.getManager().getLobbyPlayers().add(sp.getUUID());
		DataManager.getManager().getPlayingPlayers().remove(sp.getUUID());
		PinguiScoreboard.getScoreboard().scoreboard(sp);
		remove();
		
		
	}
	
	public void doJump() {
		this.currentStart = this.currentFinish;
		ParkourPlayer pp = getPlayer();
		SpleefPlayer sp = pp.getPlayer();
		this.jumpsMade++;
		
		if (mode.equals(ParkourMode.BEAT_LEVEL)) {
			sp.getPlayer().sendMessage("§aYou made a jump! §7(§6" + this.jumpsMade+"/25§7)");
			if (this.jumpsMade>=25) {
				finish(FinishParkourReason.WINNER);
				return;
			} 			
		} else {
			sp.getPlayer().sendMessage("§aYou made a jump! §7(§6" + this.jumpsMade+"§7)");
		}
			
		Jump jump = level.loadJump(this,getPlayer(),this.currentStart);		
		this.currentFinish = jump.getFinishAt(this.currentStart);	
		
	}
	
	
	private void setup() {
		int a = 500+(500*(this.id-1));
		int x = a;
		int z = a;	
		Location start = new Location(Bukkit.getWorld("parkour"),x,100,z);
		this.currentStart = start;
		
		Location corner1 = new Location(start.getWorld(),start.getBlockX()+500,start.getBlockY()+100,start.getBlockZ());
		Location corner2 = new Location(start.getWorld(),start.getBlockX()-500,50,start.getBlockZ()+500);	
		FAWESplinduxAPI.getAPI().placeBlocks(corner1, corner2, Material.AIR);
		Jump jump = level.loadJump(this,getPlayer(),start);		
		this.currentFinish = jump.getFinishAt(start);
		new BukkitRunnable() {
			public void run() {
		ParkourPlayer pp = getPlayer();
		SpleefPlayer sp = pp.getPlayer();
		sp.getPlayer().teleport(start);
			}
		}.runTaskLater(Main.get(), 1L);
	}

	public void remove() {
		for (UUID uuid : this.jumpsSchematics) {
			FAWESplinduxAPI.getAPI().undoSchematic(uuid);
		}

	}
	
}
