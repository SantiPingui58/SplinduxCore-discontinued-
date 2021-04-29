package me.santipingui58.splindux.game.parkour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.parkour.probability.Probability;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.utils.Utils;

public class ParkourManager {
	private static ParkourManager manager;	
	 public static ParkourManager getManager() {
	        if (manager == null)
	        	manager = new ParkourManager();
	        return manager;
	    }
	 
	 private List<ParkourArena> arenas = new ArrayList<ParkourArena>();
	 private List<Level> levels = new ArrayList<Level>();
	 private List<Jump> jumps = new ArrayList<Jump>();
	 private List<Probability> probabilities = new ArrayList<Probability>();
	 public List<ParkourArena> getArenas() {
		 return this.arenas;
	 }
	 
	 public List<Jump> getJumps() {
		 return this.jumps;
	 }
	 
	 public List<Level> getLevels() {
		 return this.levels;
	 }
	 
	 public List<Probability> getProbabilities() {
		 return this.probabilities;
	 }
	 
	 public void loadProbabilities() {
		 for (Level level : this.levels) {
			 Probability p = new Probability(level);
			 this.probabilities.add(p);
		 }
		 
		 calculateProbabilities();
	 }
	 
	 public void calculateProbabilities() {
		 for (Probability p : this.probabilities) p.calculate();		 
	 }
	 
	 public Probability getProbabilityBy(Level level) {
		 for (Probability p : this.probabilities) {
			 if (p.getLevel().equals(level)) {
				 return p;
			 }
		 }
		 return null;
	 }
	 
	 
	 
	 public void loadLevels() {
		 for (int i = 1;i<=25;i++) {
			 Level level = new Level(i);
			 this.levels.add(level);
		 }
	 }

	 
	 public Level getLevel(int i) {
		if (i<=0) {
			i=1;
		}
		 for (Level level : this.levels) {
			 if (level.getLevel()==i) {
				 return level;
			 }
		 }
		 return null;
	 }
	 
	 public void loadJumps() {
		 
			 if (Main.jumps.getConfig().contains("jumps")) {
			 Set<String> jumps = Main.jumps.getConfig().getConfigurationSection("jumps").getKeys(false);
			for (String j : jumps) {
				String name = j;
				Location start = Utils.getUtils().getLoc(Main.jumps.getConfig().getString("jumps."+j+".start"));
				Location finish = Utils.getUtils().getLoc(Main.jumps.getConfig().getString("jumps."+j+".finish"));
				int difficulty =Main.jumps.getConfig().getInt("jumps."+j+".difficulty");
				Jump jump = new Jump(name,difficulty,start,finish);
				this.jumps.add(jump);				
			
		 }
			Main.get().getLogger().info("Loaded " + jumps.size() + " jumps");
		 
			 }
	 }
	 
	 

	 public void joinLevel(ParkourPlayer pp, Level level,ParkourMode mode) {
		 SpleefPlayer sp = pp.getPlayer();
		 Player p = sp.getPlayer();
		 UUID u = p.getUniqueId();
			p.sendMessage("§aYou are now playing §2Level " + level.getLevel());
			 pp.createArena(level,mode);	 
			p.setGameMode(GameMode.ADVENTURE);
			p.getInventory().clear();
			 sp.setScoreboard(ScoreboardType.PARKOUR);
			 DataManager.getManager().getLobbyPlayers().remove(u);
				DataManager.getManager().getPlayingPlayers().add(u);
			 pp.getPlayer().getPlayer().setFlying(false);
			 pp.getPlayer().getPlayer().setAllowFlight(false);
	 }
	 
	 
	 public int getNextID() {
		 List<Integer> ids = new ArrayList<Integer>();
		 for (ParkourArena arena : this.arenas) {
			 ids.add(arena.getID());
		 }
		 for (int i = 1;i<=1000;i++) {
			 if (!ids.contains(i)) {
				 return i;
			 }
		 }
		 return 0;
	 }
	 

	public Jump getRandomJumpByDifficulty(int j,int recursion,boolean up) {
		
		List<Jump> list = new ArrayList<Jump>();
		list.addAll(this.jumps);
		Collections.shuffle(list);
		for (Jump jump : list) {
			if (jump.getDifficulty()==j) {
				return jump;
			}
		}				
		if (j<=0 || j>100) {
			return null;
		}
		recursion++;
		if (up) {
			return getRandomJumpByDifficulty(j+recursion, recursion,!up);
		} else {
			return getRandomJumpByDifficulty(j-recursion, recursion,!up);
		}	
	}

	public void saveArenas() {
		for (ParkourArena arena : ParkourManager.getManager().getArenas()) {
			arena.remove();
			
		}		
	}	
}
