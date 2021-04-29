package me.santipingui58.splindux.vote;


import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;

public class VoteClaims {

	private boolean hasClaimedNameMC;
	private boolean hasClaimedDiscord;
	private boolean hasClaimedTwitch;
	private boolean hasClaimedYoutube;
	public boolean hasClaimedTwitter;
	
	public VoteClaims(boolean namemc, boolean discord, boolean twitch, boolean youtube, boolean twitter) {
		
		this.hasClaimedNameMC = namemc;
		this.hasClaimedDiscord = discord;
		this.hasClaimedTwitch = twitch;
		this.hasClaimedYoutube = youtube;
		this.hasClaimedTwitter = twitter;

	}
	
	
	
	public boolean hasClaimed(SpleefPlayer sp,Rewarded network) {
		
		if (!network.isDailyClaimeable()) {
		switch(network) {
		default:return false;
		case NAMEMC: return this.hasClaimedNameMC;
		case DISCORD: return this.hasClaimedDiscord;
		case TWITCH: return this.hasClaimedTwitch;
		case YOUTUBE: return this.hasClaimedYoutube;
		case TWITTER: return this.hasClaimedTwitter;
		}
		
		} else {
			TimeLimitType type = TimeLimitType.fromRewarded(network);
			return (TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, type));
		}
	}
	
	public void claim(Rewarded network,boolean b) {
		if (!network.isDailyClaimeable()) {
		switch(network) {
		default: break;
		case NAMEMC: this.hasClaimedNameMC = b; return;
		case DISCORD: this.hasClaimedDiscord = b; return;
		case TWITCH: this.hasClaimedTwitch = b; return;
		case YOUTUBE: this.hasClaimedYoutube = b; return;
		case TWITTER: this.hasClaimedTwitter = b; return;
		}
		}
		
	}
	

	
}
