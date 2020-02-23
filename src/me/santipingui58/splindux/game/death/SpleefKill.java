package me.santipingui58.splindux.game.death;

import org.bukkit.Location;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class SpleefKill {

	
	private DeathReason reason;
	private SpleefPlayer killer;
	private Location location;
	public SpleefKill(Location l,SpleefPlayer killer, DeathReason reason) {
		this.location = l;
		this.killer = killer;
		this.reason = reason;
	}
	
	public DeathReason getReason() {
		return this.reason;
	}

	public SpleefPlayer getKiller() {
		return this.killer;
	}
	
	public Location getLocation() {
		return this.location;
	}
}
