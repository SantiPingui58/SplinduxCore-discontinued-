package me.santipingui58.splindux.relationships.parties;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;



public class PartyManager {
	private static PartyManager manager;	
	 public static PartyManager getManager() {
	        if (manager == null)
	        	manager = new PartyManager();
	        return manager;
	    }
	 
	
	private List<Party> parties = new ArrayList<Party>();
	
	
	public List<Party> getParties() {
		return this.parties;
	}
	
	public Party getParty(Player p) {
		for (Party party : this.parties) {
			if (party.getLeader().compareTo(p.getUniqueId())==0 || party.getMembers().contains(p.getUniqueId()) || party.getDisconnectedPlayers().contains(p.getUniqueId()))
				return party;
		}
		return null;
	}

	
	
	
}
