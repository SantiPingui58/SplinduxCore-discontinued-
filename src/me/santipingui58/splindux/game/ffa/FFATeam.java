package me.santipingui58.splindux.game.ffa;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;

public class FFATeam {

	private UUID uuid;
	
	private UUID player1;
	private boolean alive1;
	
	private UUID player2;
	private boolean alive2;
	private Integer[] rgb = {ThreadLocalRandom.current().nextInt(0, 255 + 1)
			,ThreadLocalRandom.current().nextInt(0, 255 + 1)
			,ThreadLocalRandom.current().nextInt(0, 255 + 1)};
	
	public FFATeam(UUID player1, UUID player2) {
		this.uuid = UUID.randomUUID();
		this.player1 = player1;
		this.player2 = player2;
		this.alive1=true;
		this.alive2=player2==null ? false : true;
	}
	
	public Integer[] getRGB() {
		return this.rgb;
	}
 	
	public void killPlayer(UUID uuid) {
		if (this.player1.equals(uuid)) {
			this.alive1 = false;
		} else if (this.player2.equals(uuid)) {
			this.alive2 = false;
		}
	}
	public void revive() {
			SpleefPlayer sp1 = SpleefPlayer.getSpleefPlayer(player1);
			this.alive1 =sp1!=null && sp1.getOfflinePlayer().isOnline() && sp1.getArena()!=null && GameManager.getManager().getFFAArena(SpleefType.SPLEEF).equals(sp1.getArena()) ? true : false;
			
			if (player2!=null) {
			SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(player2);
			this.alive2 =sp2!=null && sp2.getOfflinePlayer().isOnline() && sp2.getArena()!=null && GameManager.getManager().getFFAArena(SpleefType.SPLEEF).equals(sp2.getArena()) ? true : false;
			} else {
				this.alive2 = false;
			}
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public UUID getPlayer1() {
		return this.player1;
	}
	
	public boolean isPlayer1Alive() {
		return this.alive1;
	}
	
	public void alivePlayer1(boolean b) {
		this.alive1 = b;
	} 
	
	public boolean isPlayer2Alive() {
		return this.alive2;
	}
	
	public void alivePlayer2(boolean b) {
		this.alive2 = b;
	} 
	
	
	public UUID getPlayer2() {
		return this.player2;
	}
	
	public String getName() {
		if (this.player2==null) {
			return Bukkit.getOfflinePlayer(getPlayer1()).getName();
		} else {
		return Bukkit.getOfflinePlayer(getPlayer1()).getName()+"-"+Bukkit.getOfflinePlayer(getPlayer2()).getName();
	}
	}

	public Set<UUID> getPlayers() {
		Set<UUID> set = new HashSet<UUID>();
		set.add(player1);
		if (player2!=null)set.add(player2);
		return set;
	}

	public boolean everyoneAlive() {
		return this.alive1 && this.alive2;
	}

	public void setPlayer2(UUID uuid2) {
		this.player2 = uuid2;
		
	}

	public void broadcast(String string) {
		for (UUID u : this.getPlayers()) SpleefPlayer.getSpleefPlayer(u).sendMessage(string);		
	} 
	
}

