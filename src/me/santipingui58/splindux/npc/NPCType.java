package me.santipingui58.splindux.npc;

import me.santipingui58.splindux.game.spleef.GameType;

public enum NPCType {

	RANKED,
	VERSUS,
	FFA,
	TEAMS,
	VOTES,
	FISHING,
	PARKOUR;
	
	
	public int getId() {
		if (this.equals(VERSUS)) {
			return 173;
		} else if (this.equals(FFA)) {
			return 174;
		}else if (this.equals(TEAMS)) {
			return 178;
		}else if (this.equals(VOTES)) {
			return 179;
		} else if (this.equals(FISHING)) {
			return 194;
		} else if (this.equals(RANKED)) {
			return 0;
		} else if (this.equals(PARKOUR)) {
			return 176;
		}
		return 0;
	}
 	
	
	public GameType getGameType() {
		 if (this.equals(VOTES) || this.equals(FISHING) || this.equals(PARKOUR)) {
			return null;
		} else if (this.equals(FFA)) {
			return GameType.FFA;
		} else {
			return GameType.DUEL;
		}
	}
	
	
	public boolean isRanked() {
		if (this.equals(RANKED)) {
			return true;
		} else {
			return false;
		}
	}
}
