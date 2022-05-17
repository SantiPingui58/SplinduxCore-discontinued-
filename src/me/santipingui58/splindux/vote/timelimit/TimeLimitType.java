package me.santipingui58.splindux.vote.timelimit;

import me.santipingui58.splindux.vote.Rewarded;

public enum TimeLimitType {

	FFAEVENTDELAY,
	MINECRAFTSERVERS_DOT_ORG,
	PLANETMINECRAFT_DOT_COM,
	MINECRAFT_HYPHEN_MP_DOT_COM,
	TOPG_DOT_ORG,
	MINECRAFT_HYPHEN_SERVER_DOT_NET,
	YOUTUBE_VIDEO,
	GIFT,
	PARTY_BROADCAST,
	STREAM_DISCORD,
	STREAM_MC;
	
	public static TimeLimitType fromRewarded(Rewarded rewarded) {
		for (TimeLimitType type : TimeLimitType.values()) {
			if (type.toString().equalsIgnoreCase(rewarded.toString())) {
				return type;
			}
		}
		return null;
	}
	
	public boolean isServerList() {
		if (this.equals(FFAEVENTDELAY) || this.equals(GIFT) || this.equals(PARTY_BROADCAST) || this.equals(STREAM_DISCORD) || this.equals(STREAM_MC)) {
			return false;
		} else {
			return true;
		}
	}
}
