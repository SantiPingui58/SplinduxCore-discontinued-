package me.santipingui58.splindux.relationships.guilds;

import java.util.UUID;

import me.santipingui58.splindux.stats.ranking.RankingManager;

public class GuildPlayer {

	private UUID uuid;
	private int salary;
	private Guild guild;
	private boolean admin;
	private boolean mod;
	private int value;
	
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
		return this.salary;
	}
	
	public void setSalary(int i) {		
		this.salary = i;
	}
	
	public int getValue() {		
				
		if (this.value !=0) return this.value;
		
		int ranking = RankingManager.getManager().getRanking().getPosition(this.uuid);
		double playerValue = 0;
		if (ranking!=-1) {
			 playerValue = (1500/Math.sqrt(ranking));
		} 
		
		double guildValue = Math.sqrt(this.guild.getValueWithoutPlayers());
		this.value = (int) ((playerValue+guildValue)*50);
		return this.value;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
		
	}
	
	
	public int getMinSalary() {
		return (int) (getValue()/50*0.7);
	}
	
}
