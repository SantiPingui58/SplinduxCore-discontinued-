package me.santipingui58.splindux.relationships.guilds;

import java.util.UUID;

public class GuildPlayer {

	private UUID uuid;
	private int salary;
	private Guild guild;
	private boolean admin;
	private boolean mod;
	
	public GuildPlayer(UUID uuid, int salary) {
		this.uuid = uuid;
		this.salary = salary;
	}

	
	public boolean isAdmin() {
		return admin;
	}
	
	public boolean isMod() {
		return this.mod;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public int getSalary() {
		int div = guild.isTransferable(this) ? 2 : 1;
		return this.salary/div;
	}
	
	public void setSalary(int i) {		
		this.salary = i;
	}
	
	public int getValue() {		
		return this.salary*50;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
		
	}
	
	
}
