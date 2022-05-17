package me.santipingui58.splindux.listener;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.vote.Rewarded;
import me.santipingui58.splindux.vote.ServerListName;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;

public class VotifierListener implements Listener {

	@EventHandler
	public void onPlayerVote(VotifierEvent e) {
		Vote v = e.getVote();
		@SuppressWarnings("deprecation")
		OfflinePlayer p = Bukkit.getOfflinePlayer(e.getVote().getUsername());
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		sp.setTotalVotes(sp.getTotalVotes()+1);
		ServerListName sln = ServerListName.fromString(v.getServiceName());
		TimeLimitType tlt = TimeLimitType.valueOf(sln.toString());
		TimeLimitManager tlm = TimeLimitManager.getManager();
		if (!tlm.hasActiveTimeLimitBy(sp, tlt)) {
			tlm.addTimeLimit(sp, 1440, tlt,null);
			Rewarded rewarded = Rewarded.valueOf(sln.toString());
			rewarded.getRewards().giveRewards(sp);
			sp.sendMessage("§aThanks for "+rewarded.getClaimMessage());
		
		} 
		}
		
	
}
