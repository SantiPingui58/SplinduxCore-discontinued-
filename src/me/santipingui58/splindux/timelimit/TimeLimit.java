package me.santipingui58.splindux.timelimit;

import java.util.UUID;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class TimeLimit {

	private UUID uuid;
	private TimeLimitType type;
	private int leftMinutes;
	private SpleefPlayer sp;
	
	public TimeLimit(UUID uuid,SpleefPlayer sp,int leftMinutes, TimeLimitType type) {
		this.type = type;
		this.leftMinutes = leftMinutes;
		this.uuid = uuid;
		this.sp = sp;
	}
	
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public void time() {
		if (this.leftMinutes>=0) {
		this.leftMinutes= this.leftMinutes-1;
	}
	}
	
	public SpleefPlayer getPlayer() {
		return this.sp;
	}
	
	public TimeLimitType getType() {
		return this.type;
	}
	
	public int getLeftMinutes() {
		return this.leftMinutes;
	}


	public String getLeftTime() {
		int t = this.leftMinutes;
		int hours = t / 60; //since both are ints, you get an int
		int minutes = t % 60;
		return String.format("%dh %02dm",  hours, minutes);
	}
	
}
