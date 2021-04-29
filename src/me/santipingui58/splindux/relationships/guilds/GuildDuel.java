package me.santipingui58.splindux.relationships.guilds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefType;

public class GuildDuel {

	
	private UUID creator;
	private UUID receiver;
	private Guild guild;
	private Guild duelGuild;
	private SpleefType type;
	private List<UUID> players1;
	private List<UUID> players2;
	private int coins;
	private List<Arena> arenas;
	
	
	
	private int score1;
	private int score2;
	
	private int subscore1;
	private int subscore2;
	public GuildDuel(UUID creator,Guild guild, Guild duelGuild) {
		this.creator = creator;
		this.guild = guild;
		this.duelGuild = duelGuild;
		this.players1 = new ArrayList<UUID>();
		this.players2 = new ArrayList<UUID>();
		arenas = new ArrayList<Arena>();
		GuildsManager.getManager().getGuildDuels().add(this);
	}
	
	
	public int getCoins() {
		return this.coins;
	}
	
	public void setCoins(int i) {
		this.coins = i;
	}
	
	public int getScore1() {
		return this.score1;
	}
	
	public int getScore2() {
		return this.score2;
	}
	
	public int getSubScore1() {
		return this.subscore1;
	}
	
	public int getSubScore2() {
		return this.subscore2;
	}
	
	public void setScore1(int i) {
		this.score1 = i;
	}
	
	public void setScore2(int i) {
		this.score2 = i;
	}
	
	public void setSubScore1(int i) {
		this.subscore1 = i;
	}
	
	public void setSubScore2(int i) {
		this.subscore2 = i;
	}
	
	public List<Arena> getArenas() {
		return this.arenas;
	}
	
	public UUID getCreator() {
		return this.creator;
	}
	
	public UUID getReceiver() {
		return this.receiver;
	}
	public void setReceiver(UUID u) {
		this.receiver = u;
	}
	
	public Guild getGuild() {
		return this.guild;
	}
	
	public Guild getDuelGuild() {
		return this.duelGuild;
	}
	
	public SpleefType getType() {
		return this.type;
	}
	
	public void setType(SpleefType type) {
		this.type = type;
	}
	
	public List<UUID> getPlayers1() {
		return this.players1;
	}
	
	
	public List<UUID> getPlayers2() {
		return this.players2;
	}


	public void finishDuel() {
		Guild winner = null;
		Guild loser = null;
		int winnerScore = 0;
		int winnerSubScore = 0;
		int loserScore = 0;
		int loserSubScore = 0;
		if (this.score1!=this.score2) {			
		if (this.score1>this.score2) {
			winner = this.guild;
			winnerScore = this.score1;
			winnerSubScore = this.subscore1;
			loserScore = this.score2;
			loserSubScore = this.subscore2;
			loser = this.duelGuild;
		} else if (this.score2>this.score1) {
			winner = this.duelGuild;
			loser = this.guild;
			
			winnerScore = this.score2;
			winnerSubScore = this.subscore2;
			loserScore = this.score1;
			loserSubScore = this.subscore1;
		}
		} else {
			if (this.subscore1>this.score2) {
				winner = this.guild;
				loser = this.duelGuild;
				winnerScore = this.score1;
				winnerSubScore = this.subscore1;
				loserScore = this.score2;
				loserSubScore = this.subscore2;
			} else if (this.subscore2>this.score1) {
				winner = this.duelGuild;
				loser = this.guild;
				winnerScore = this.score2;
				winnerSubScore = this.subscore2;
				loserScore = this.score1;
				loserSubScore = this.subscore1;
			}			
		}
		
	
		if (winner!=null) {
			int w = (int) (this.coins*0.7);
			int l = (int) (this.coins*0.3);
			winner.setCoins(winner.getCoins()+w);
			winner.addEarning("GUILD DUEL", w);
			loser.setCoins(winner.getCoins()+l);
			loser.addEarning("GUILD DUEL", l);
		DataManager.getManager().broadcast(GameManager.getManager().getGuildDuelPrefix(this.type) + "§6§l"+winner.getName() + " §bwon against §6§l"+ loser.getName() + "§7(§e§l"+winnerScore + "§7-§e§l" + loserScore +"§7) [" + 
		"§a"+winnerSubScore + "§7-§a" + loserSubScore+"§7]", true);
		} else {
			DataManager.getManager().broadcast(GameManager.getManager().getGuildDuelPrefix(this.type) + "§6§l"+getGuild().getName() + " §btied against §6§l"+ getDuelGuild().getName() + "§7(§e§l"+this.score1 + "§7-§e§l" + this.score1 +"§7) [" + 
					"§a"+this.subscore1 + "§7-§a" + this.subscore1+"§7]", true);
			getGuild().setCoins(getGuild().getCoins()+(this.coins/2));
			getGuild().addEarning("GUILD DUEL", this.coins/2);
			getDuelGuild().setCoins(getDuelGuild().getCoins()+(this.coins/2));
			getDuelGuild().addEarning("GUILD DUEL", this.coins/2);
		}
		
		
		GuildsManager.getManager().getGuildDuels().remove(this);
	}
	
	
}
