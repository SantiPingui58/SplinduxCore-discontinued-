package me.santipingui58.splindux.game.death;

import org.bukkit.Location;

import me.santipingui58.splindux.game.SpleefPlayer;

public class BrokenBlock {


	private SpleefPlayer sp;
	private Location location;
	private BreakReason reason;
	public BrokenBlock(SpleefPlayer sp,Location l,BreakReason r) {
		this.location = l;
		this.sp = sp;
		this.reason = r;
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
