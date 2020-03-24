package me.santipingui58.splindux.replay;

import java.util.UUID;

import org.bukkit.Location;

public class BrokenBlock {
	
	private UUID uuid;
	private int time;
	private Location location;
	
	public BrokenBlock(UUID uuid,int time, Location location) {
		this.uuid = uuid;
		this.time = time;
		this.location = location;
	}
	

	public UUID getUUID() {
		return this.uuid;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	
	 
}
