package me.santipingui58.splindux.npc;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefType;

public enum NPCType {

	FFA,
	RANKED_SPLEEF_1V1,
	SPLEEF_DUELS,
	SPLEGG_DUELS,
	VOTES,
	FISHING;
	
	
	public int getId() {
		if (this.equals(FFA)) {
			return 0;
		} else if (this.equals(SPLEEF_DUELS)) {
			return 173;
		}else if (this.equals(SPLEGG_DUELS)) {
			return 174;
		}else if (this.equals(VOTES)) {
			return 179;
		} else if (this.equals(FISHING)) {
			return 181;
		} else if (this.equals(RANKED_SPLEEF_1V1)) {
			return 192;
		}
		return 0;
	}
 	
	public SpleefType getSpleefType() {
		if (this.equals(SPLEGG_DUELS)) {
			return SpleefType.SPLEGG;
		} else if (this.equals(VOTES) || this.equals(FISHING)) {
			return null;
		} else {
			return SpleefType.SPLEEF;
		}
	}
	
	public GameType getGameType() {
		if (this.equals(FFA)) {
			return GameType.FFA;
		} else if (this.equals(VOTES) || this.equals(FISHING)) {
			return null;
		} else {
			return GameType.DUEL;
		}
	}
	
	
	public boolean isRanked() {
		if (this.equals(RANKED_SPLEEF_1V1)) {
			return true;
		} else {
			return false;
		}
	}
}
