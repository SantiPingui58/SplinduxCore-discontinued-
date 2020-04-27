package me.santipingui58.splindux.game.death;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.translate.Main;

public class BrokenBlock {


	private SpleefPlayer sp;
	private Location location;
	private BreakReason reason;
	private boolean alive;
	public BrokenBlock(SpleefPlayer sp,Location l,BreakReason r) {
		this.location = l;
		this.sp = sp;
		this.reason = r;
		this.alive = true;
		new BukkitRunnable() {

			@Override
			public void run() {
				alive = false;
			}
			
		}.runTaskLater(Main.get(), 20L*10);
	}
	
	
	public boolean isAlive() {
		return this.alive;
	}
	public BreakReason getReason() {
		return this.reason;
	}
	
	public SpleefPlayer getPlayer() {
		return this.sp;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	
	
}
