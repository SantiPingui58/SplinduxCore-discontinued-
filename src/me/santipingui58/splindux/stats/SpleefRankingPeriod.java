package me.santipingui58.splindux.stats;

import java.util.HashMap;


public enum SpleefRankingPeriod {

	ALL_TIME,
	MONTHLY,
	WEEKLY;
	
	
	public HashMap<SpleefRankingPeriod, SpleefRankingPeriod> getPrevAndNext() {
		HashMap<SpleefRankingPeriod,SpleefRankingPeriod> hashmap = new HashMap<SpleefRankingPeriod,SpleefRankingPeriod>();
		switch(this) {
		case ALL_TIME:
			hashmap.put(MONTHLY, WEEKLY);
			break;
		case MONTHLY:
			hashmap.put(WEEKLY, ALL_TIME);
			break;
		case WEEKLY:
			hashmap.put(ALL_TIME, MONTHLY);
			break;
		default:
			break;
		
		}
		
		return hashmap;
		
	}
}

