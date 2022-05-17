package me.santipingui58.splindux.game.ranked;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;

public class RankedTeam {

	
	private List<UUID> players = new ArrayList<UUID>();
	
	public RankedTeam(List<SpleefPlayer> players) {
		for(SpleefPlayer p : players)
		this.players.add(p.getUUID());
	}
	
	public List<UUID> getPlayers() {
		return this.players;
	}
	
	public List<SpleefPlayer> getOnlinePlayers() {
		List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		for (UUID u : this.players) {
			Player p = Bukkit.getPlayer(u);
			if (Bukkit.getOnlinePlayers().contains(p)) {
				SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(u);
				list.add(sp);
			}
		}
		return list;
	}
	
	
	public int getELO(SpleefType type) {
		int elo = 0;
		for (SpleefPlayer sp : getOnlinePlayers()) {
			elo = elo + sp.getPlayerStats().getELO(type);
		}
		
		elo = this.players.size() >0 ? elo/this.players.size() : elo;
		return elo;
	}

	public void newELO(int elo,SpleefType type) {
		for (UUID uuid : this.players) {
			SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(uuid);
			if (temp==null) {
				 new SpleefPlayer(uuid);
					HikariAPI.getManager().loadData(uuid);
					new BukkitRunnable() {
						public void run () {
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(uuid);
							sp.getPlayerStats().setELO(type,sp.getPlayerStats().getELO(type)+elo);
						}
					}.runTaskLater(Main.get(), 5L);
			} else {
				SpleefPlayer sp = temp;
				sp.getPlayerStats().setELO(type,sp.getPlayerStats().getELO(type)+elo);
			}
		
		}
		
	}
}
