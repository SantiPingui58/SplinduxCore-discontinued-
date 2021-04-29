package me.santipingui58.splindux.vote.timelimit;

import java.util.UUID;

import org.bukkit.OfflinePlayer;


public class TimeLimit {

	private UUID uuid;
	private TimeLimitType type;
	private int leftMinutes;
	private OfflinePlayer player;
	private String[] args;
	public TimeLimit(UUID uuid,OfflinePlayer player,int leftMinutes, TimeLimitType type,String[] args) {
		this.type = type;
		this.leftMinutes = leftMinutes;
		this.uuid = uuid;
		this.player = player;
		this.args = args;
	}
	
	
	public String[] getArgs() {
		return this.args;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public void time() {
		if (this.leftMinutes>=0) {
		this.leftMinutes= this.leftMinutes-1;
	}
	}
	
	public OfflinePlayer getPlayer() {
		return this.player;
	}
	
	public TimeLimitType getType() {
		return this.type;
	}
	
	public int getLeftMinutes() {
		return this.leftMinutes;
	}


	public String getLeftTime() {
		int t = this.leftMinutes;
		int hours = t / 60; 
		int minutes = t % 60;
		return String.format("%dh %02dm",  hours, minutes);
	}
	
}
