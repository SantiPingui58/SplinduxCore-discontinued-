package me.santipingui58.splindux.npc;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefType;

public enum NPCType {

	FFA,
	RANKED_SPLEEF_1V1,
	SPLEEF_1V1,
	SPLEEF_2V2,
	SPLEEF_3V3,
	SPLEGG_1V1,
	SPLEGG_2V2,
	SPLEGG_3V3,
	VOTES,
	FISHING;
	
	
	public int getId() {
		if (this.equals(FFA)) {
			return 0;
		} else if (this.equals(SPLEEF_1V1)) {
			return 173;
		}else if (this.equals(SPLEEF_2V2)) {
			return 174;
		}else if (this.equals(SPLEEF_3V3)) {
			return 175;
		}else if (this.equals(SPLEGG_1V1)) {
			return 176;
		}else if (this.equals(SPLEGG_2V2)) {
			return 177;
		}else if (this.equals(SPLEGG_3V3)) {
			return 178;
		} else if (this.equals(VOTES)) {
			return 179;
		} else if (this.equals(FISHING)) {
			return 181;
		} else if (this.equals(RANKED_SPLEEF_1V1)) {
			return 192;
		}
		return 0;
	}
 	
	public SpleefType getSpleefType() {
		if (this.equals(SPLEGG_1V1) || this.equals(SPLEGG_2V2) || this.equals(SPLEGG_3V3)) {
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
	
	public int getAmount() {
		if (this.equals(SPLEEF_1V1) || this.equals(SPLEGG_1V1) || this.equals(RANKED_SPLEEF_1V1)) {
			return 1;
		} else if (this.equals(SPLEEF_2V2) || this.equals(SPLEGG_2V2)) {
			return 2;
		}  else if (this.equals(SPLEEF_3V3) || this.equals(SPLEGG_3V3)) {
			return 3;
		} else {
			return 0;
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
