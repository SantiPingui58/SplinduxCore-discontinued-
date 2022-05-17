package me.santipingui58.splindux.task.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.Arena;
public class DecayTask {

	
	private int speed;
	private Arena arena;
	private List<Location> list = new ArrayList<Location>();
	private int task;
	
	public DecayTask(Arena arena,int speed) {
		orderLocations();
		new BukkitRunnable() {
			public void run() {
				task();
			}
		}.runTaskLater(Main.get(), 5L);
		
		this.speed = speed;
		this.arena = arena;
	}
	
	public int getTask() {
		return this.task;
	}
	
	@SuppressWarnings("deprecation")
	public void task() {
		
		
		
				task = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Main.get(), new Runnable() {	
					public void run() {  
						if (arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.LOBBY) || arena.getState().equals(GameState.PAUSE)) {
							Bukkit.getServer().getScheduler().cancelTask(task);
						}
						
						if (list.isEmpty()) orderLocations();
						
						if (!list.isEmpty()) {
						Location l = list.get(0);
						list.remove(l);
						new BukkitRunnable() {
							public void run() {
								if (arena.getState().equals(GameState.GAME) && !l.getBlock().getType().equals(Material.AIR) && !l.getBlock().getType().equals(Material.CONCRETE_POWDER)) {
									l.getBlock().setType(Material.CONCRETE_POWDER);
									l.getBlock().setData((byte)14);
								new BukkitRunnable() {
									public void run() {
										if (arena.getState().equals(GameState.GAME) && l.getBlock().getType().equals(Material.CONCRETE_POWDER))
											l.getBlock().setType(Material.AIR);
									}
								}.runTaskLater(Main.get(), 20L);
								}
								
								
							}
						}.runTask(Main.get());
					}
 					}
				}, 0, speed);
				

		
	}
	
	
	
	public void orderLocations() {
		new BukkitRunnable() {
			public void run () {
				list.clear();
		  Location a = arena.getShrinkedDuelArena1();
		  Location b = arena.getShrinkedDuelArena2();	

		  int ax = a.getBlockX();
		  int az = a.getBlockZ();			  
		  int y = a.getBlockY();		  
		  int bx = b.getBlockX();
		  int bz = b.getBlockZ();	
		
		  for (int x = ax; x <= bx; x++) {
			  for (int z = az; z <= bz; z++) {
				  Location location = new Location (a.getWorld(), x, y, z); 
			
				  if (location.getBlock().getType().equals(Material.SNOW_BLOCK)) {
				  list.add(location);
				  }
				  
				  }
			  }
		  try {
		  Collections.shuffle(list);
		  } catch (Exception ex) {
			  cancel();
		  }
	}
		}.runTaskAsynchronously(Main.get());
	}
}

