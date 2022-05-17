package me.santipingui58.splindux.hologram;

import java.util.HashMap;

public enum HologramSubType {

	FFA,
	DUELS,
	COINS,
	EXP,
	GUILD,
	TIME,
	RANKING,
	VOTES;
	
	
	
	public HashMap<HologramSubType, HologramSubType> getPrevAndNext() {
		HashMap<HologramSubType,HologramSubType> hashmap = new HashMap<HologramSubType,HologramSubType>();
		switch(this) {
		case COINS:
			hashmap.put(RANKING, EXP);
			break;
		case DUELS:
			hashmap.put(null, FFA);
			break;
		case EXP:
			hashmap.put(COINS, GUILD);
			break;
		case FFA:
			hashmap.put(null, DUELS);
			break;
		case GUILD:
			hashmap.put(EXP, TIME);
			break;
		case RANKING:
			hashmap.put(VOTES, COINS);
			break;
		case TIME:
			hashmap.put(GUILD, VOTES);
			break;
		case VOTES:
			hashmap.put(TIME, RANKING);
			break;
		default:break;
		}
		
		return hashmap;
		
	}
}
