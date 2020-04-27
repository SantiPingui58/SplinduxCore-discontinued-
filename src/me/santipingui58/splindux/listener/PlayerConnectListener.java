package me.santipingui58.splindux.listener;



import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.security.SecurityManager;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.scoreboard.hologram.HologramManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerConnectListener implements Listener {

	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null); 
		
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
		Player p = e.getPlayer();
		
		SpleefPlayer sp = null;
		if (!Main.data.getConfig().contains("players."+p.getUniqueId().toString())) {
		 DataManager.getManager().createSpleefPlayer(p);
		 return;
		} 
		
		sp = SpleefPlayer.getSpleefPlayer(p);
		
		DataManager.getManager().saveIP(e.getPlayer());
		SecurityManager.getManager().adminLogin(sp);
		sp.setScoreboard(ScoreboardType.LOBBY);
		LevelManager.getManager().setExp(sp);
		sp.giveLobbyItems();
		if (e.getPlayer().hasPermission("splindux.join")) {
			for (Player o : Bukkit.getOnlinePlayers()) {	
				String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(e.getPlayer()).getPrefix());
				o.sendMessage(prefix+  e.getPlayer().getName() + " §ahas joined the server!");
			}
		}
		if (Main.arenas.getConfig().contains("mainlobby")) {
		e.getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
	}
		
		if (e.getPlayer().hasPermission("splindux.fly")) {
			sp.fly();
		}
		
		
		sp.setLastLogin(new Date());
		
		new BukkitRunnable() {
			public void run() {
		HologramManager.getManager().sendHolograms(SpleefPlayer.getSpleefPlayer(p));
			}
		}.runTaskLater(Main.get(), 30L);
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player p = e.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);	
		if (sp==null) return;
		
	
		if (sp.isInGame()) {
			SpleefArena arena = sp.getArena();
			sp.leaveQueue(arena,false);
				for (SpleefPlayer s : arena.getViewers()) {
					s.getPlayer().sendMessage(ChatColor.GOLD + e.getPlayer().getName() + " §chas left the server!");
				}
			}	
		for (SpleefPlayer spect : sp.getSpectators()) {
			spect.leaveSpectate(true);
		}

		
		for (SpleefPlayer online : DataManager.getManager().getOnlinePlayers()) {
			if (online.getDuelByDueledPlayer(sp)!=null) {
				SpleefDuel duel = online.getDuelByDueledPlayer(sp);
				if (duel.getDueledPlayers().contains(sp)) {
					for (SpleefPlayer dueled : duel.getDueledPlayers()) {
					dueled.getPlayer().sendMessage("§cThe player §b" + sp.getPlayer().getName() + "§c has left the Server! Duel cancelled.");
					}
					duel.getChallenger().getDuels().remove(duel);
				}
			}
		}
		
		sp.leave(false);
		DataManager.getManager().saveData(sp);
		}
	
	
}

	

