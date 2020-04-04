package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.santipingui58.splindux.game.SpleefPlayer;

public class SpleefDuel {

	private String arena;
	private SpleefPlayer challenger;
	private List<SpleefPlayer> dueledPlayers;
	private List<SpleefPlayer> acceptedPlayers;
	private SpleefType type;
	private UUID uuid;
	
	public SpleefDuel(SpleefPlayer challenger, List<SpleefPlayer> sp2, String arena,SpleefType type) {
		uuid = UUID.randomUUID();
		this.challenger = challenger;
		this.dueledPlayers = sp2;
		this.arena = arena;
		this.type = type;
		this.acceptedPlayers = new ArrayList<SpleefPlayer>();
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	public SpleefPlayer getChallenger() {
		return this.challenger;
	}
	
	public List<SpleefPlayer> getDueledPlayers() {
		return this.dueledPlayers;
	}
	public List<SpleefPlayer> getAcceptedPlayers() {
		return this.acceptedPlayers;
	}
	
	public List<SpleefPlayer> getAllPlayers() {
		List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		list.addAll(dueledPlayers);
		list.add(challenger);
		return list;
	}
	
	public String getArena() {
		return this.arena;
	}
	
	public SpleefType getType() {
		return this.type;
	}
}
