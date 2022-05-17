package me.santipingui58.splindux.task.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.Arena;
public class FFADecayTask {

	
	private int speed;
	private Arena arena;
	private int task;
	
	public FFADecayTask(Arena arena) {
		new BukkitRunnable() {
			public void run() {
				task();
			}
		}.runTaskLater(Main.get(), 2L);
		
		this.speed = 1;
		this.arena = arena;
	}
	
	public int getTask() {
		return this.task;
	}
	
	@SuppressWarnings("deprecation")
	public void task() {
				task = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Main.get(), new Runnable() {	
					public void run() {
						if (arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.LOBBY) || arena.getShrinkedRadious()==0) {
							Bukkit.getServer().getScheduler().cancelTask(task);
						}

						if (!arena.getDecayFFALocations().isEmpty()) {
						Location l = arena.getDecayFFALocations().get(0);
						arena.getDecayFFALocations().remove(l);
						new BukkitRunnable() {
							public void run() {
								try {
								if (arena.getState().equals(GameState.GAME) && !l.getBlock().getType().equals(Material.AIR) && !l.getBlock().getType().equals(Material.CONCRETE_POWDER)) {
							//	FAWESplinduxAPI.getAPI().placeBlocks(l, l, Material.CONCRETE_POWDER, (byte) 14);
									l.getBlock().setType(Material.CONCRETE_POWDER);
									l.getBlock().setData((byte)14);
								new BukkitRunnable() {
									public void run() {
										if (arena.getState().equals(GameState.GAME) && l.getBlock().getType().equals(Material.CONCRETE_POWDER))
											//FAWESplinduxAPI.getAPI().placeBlocks(l, l, Material.AIR);
											l.getBlock().setType(Material.AIR);
									}
								}.runTaskLater(Main.get(), 40L);
								}
								} catch(Exception ex) {}
								
							}
						}.runTask(Main.get());
					}
 					}
				}, 0, speed);
	}
	
	
	
}

