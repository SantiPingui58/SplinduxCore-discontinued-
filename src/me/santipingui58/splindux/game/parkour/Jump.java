package me.santipingui58.splindux.game.parkour;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.fawe.FAWESplinduxAPI;
import me.santipingui58.splindux.Main;

public class Jump {

	private String name;
	private int difficulty;
	private Location start;
	private Location finish;
	public Jump(String name,int difficulty,Location start, Location finish) {
		this.name = name;
		this.difficulty = difficulty;
		this.start = start;
		this.finish = finish;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public int getDifficulty() {
		return this.difficulty;
	}
	
	public Location getStart() {
		Location location = new Location(Bukkit.getWorld("parkour"),start.getBlockX(),start.getBlockY(),start.getBlockZ());
		return location;
	}
	
	public Location getFinish() {
		Location location = new Location(Bukkit.getWorld("parkour"),start.getBlockX()+finish.getBlockX(),start.getBlockY()+finish.getBlockY(),start.getBlockZ()+finish.getBlockZ());
		return location;
	}
	
	public Location getFinishAt(Location l) {
		int x = finish.getBlockX();
		int y = finish.getBlockY();
		int z = finish.getBlockZ();
		Location location = new Location(Bukkit.getWorld("parkour"),l.getBlockX()+x,l.getBlockY()+y,l.getBlockZ()+z);
		return location;
	}
	
	
	public void load(ParkourArena arena,ParkourPlayer pp,Location location) {
		new BukkitRunnable() {
			public void run() {
		 File file = new File( Main.get().getDataFolder(),"schematics/"+ name+".schematic");
		UUID uuid = FAWESplinduxAPI.getAPI().pasteSchematic(file, location,true);
		arena.getJumpSchematics().add(uuid);
		}
		}.runTask(Main.get());

		}
	
}
