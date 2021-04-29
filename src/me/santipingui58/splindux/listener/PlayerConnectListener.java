package me.santipingui58.splindux.listener;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.shynixn.petblocks.api.PetBlocksApi;
import com.github.shynixn.petblocks.api.business.service.PetService;

import me.neznamy.tab.api.TABAPI;
import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerConnectListener implements Listener {


	@EventHandler (priority=EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null); 
		
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		SpleefPlayer sp = new SpleefPlayer(uuid);				
		sp.setScoreboard(ScoreboardType.LOBBY);	
		DataManager.getManager().getLobbyPlayers().add(uuid);
		sp.giveLobbyItems();
		
		new BukkitRunnable() {
			public void run() {
		if (Main.arenas.getConfig().contains("mainlobby")) {
		e.getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
	}
		
		sp.updateScoreboard();
			}
		}.runTaskLater(Main.get(), 2L);
		
		if (e.getPlayer().hasPermission("splindux.fly")) {
			sp.fly();
		}
		
		Main.scoreboardUpdate = true;
		
		if (p.hasPermission("splindux.pet")) {
		PetService petMetaService = PetBlocksApi.INSTANCE.resolve(PetService.class);
		petMetaService.getOrSpawnPetFromPlayer(p);
		}

		new BukkitRunnable() {
			public void run() {
				
				HikariAPI.getManager().createData(p.getUniqueId());
				HikariAPI.getManager().loadFriends(p.getUniqueId());
				
				
		if (e.getPlayer().hasPermission("splindux.join") && sp.getOptions().joinMessageEnabled()) {
			for (Player o : Bukkit.getOnlinePlayers()) {	
				String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(e.getPlayer()).getPrefix());
				o.sendMessage(prefix+  sp.getName() + " §ahas joined the server!");
			}
		}

		for (Arena arena : DataManager.getManager().getArenas()) {
			if (arena.getDuelTempDisconnectedPlayers1().contains(uuid) || arena.getDuelTempDisconnectedPlayers2().contains(uuid)) {
				for (SpleefPlayer s : arena.getViewers()) {
					s.getPlayer().sendMessage(ChatColor.GOLD + sp.getName() + " §arejoined the match!");
				}
				
				sp.setScoreboard(ScoreboardType._1VS1GAME);
				if (arena.getDuelTempDisconnectedPlayers1().contains(uuid)) {
					arena.getDuelTempDisconnectedPlayers1().remove(uuid);
					arena.getDuelPlayers1().add(sp);
					arena.getDeadPlayers1().add(sp);
					new BukkitRunnable() {
						public void run() {
					SpectateManager.getManager().spectateSpleef(sp, arena);
				}
					}.runTaskLater(Main.get(), 10L);
				} 
				if (arena.getDuelTempDisconnectedPlayers2().contains(uuid)) { 
					arena.getDuelTempDisconnectedPlayers2().remove(uuid);
					arena.getDuelPlayers2().add(sp);
					arena.getDeadPlayers2().add(sp);
					new BukkitRunnable() {
						public void run() {
					SpectateManager.getManager().spectateSpleef(sp, arena);
				}
					}.runTaskLater(Main.get(), 10L);
				}
				
				return;
			} 
		}
			}
		}.runTaskAsynchronously(Main.get());
		
	
		new BukkitRunnable() {
			public void run() {
				sp.updateScoreboard();
				for (UUID u : sp.getFriends()) {
					OfflinePlayer player = Bukkit.getOfflinePlayer(u);
					if (player.isOnline()) player.getPlayer().sendMessage("§a[+]§e "+p.getName() + " joined");
				}
			}
		}.runTaskLater(Main.get(), 3L);
		
		new BukkitRunnable() {
			public void run() {
				LevelManager.getManager().setExp(sp);
		HologramManager.getManager().sendHolograms(SpleefPlayer.getSpleefPlayer(p),false);	
			}
		}.runTaskLaterAsynchronously(Main.get(), 30L);

		Guild guild = GuildsManager.getManager().getGuild(sp);
		
		if (guild!=null) {
			if (guild.hasTabTag()) {
				TABAPI.setTabSuffixTemporarily(p.getUniqueId(), " §7["+guild.getAchronym()+"]");
			}
			
			if (guild.hasHeadTag()) {
				TABAPI.setAboveNameTemporarily(p.getUniqueId(), " §7["+guild.getAchronym()+"] " + TABAPI.getOriginalAboveName(p.getUniqueId()));
			}
		} 
		
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player p = e.getPlayer();
		
		UUID u = p.getUniqueId();
		DataManager.getManager().getLobbyPlayers().remove(u);
		DataManager.getManager().getPlayingPlayers().remove(u);
		
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		if (sp==null) return;
		
		new BukkitRunnable() {
			public void run() {
				for (UUID u : sp.getFriends()) {
					OfflinePlayer player = Bukkit.getOfflinePlayer(u);
					if (player.isOnline()) player.getPlayer().sendMessage("§c[-]§e "+p.getName() + " left");
				}
				
				if (sp.isInGame()) {
					Arena arena = sp.getArena();
						for (SpleefPlayer s : arena.getViewers()) {
							s.getPlayer().sendMessage(ChatColor.GOLD + sp.getName() + " §chas left the server!");
						}	
						
						if (arena.getGameType().equals(GameType.DUEL) && arena.getTeamSize()>=2) {
						
							new BukkitRunnable() {
								public void run() {
									sp.leaveSpectate(false, false, false);
								}
							}.runTask(Main.get());
							if (arena.getDuelPlayers1().contains(sp)) {
								arena.getDuelTempDisconnectedPlayers1().add(sp.getUUID());
							} 
							if (arena.getDuelPlayers2().contains(sp)) {
								arena.getDuelTempDisconnectedPlayers2().add(sp.getUUID());

							}
							
						}	
					}
				
				new BukkitRunnable() {
					public void run() {
				sp.leave(false,false);
					}
				}.runTask(Main.get());

				
				for (SpleefPlayer online : DataManager.getManager().getPlayers()) {
					online.updateScoreboard();
					if (online.getDuelByDueledPlayer(sp)!=null) {
						SpleefDuel duel = online.getDuelByDueledPlayer(sp);
						if (duel.getDueledPlayers().contains(sp)) {
							for (SpleefPlayer dueled : duel.getDueledPlayers()) {
							if (dueled.isOnline()) dueled.getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has left the Server! Duel cancelled.");
							}
							
							duel.getChallenger().getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has left the Server! Duel cancelled.");
							duel.getChallenger().getDuels().remove(duel);
						}
					} else if (sp.getDuels().size()>0) {
						for (SpleefDuel duel : sp.getDuels()) {
							for (SpleefPlayer dueled : duel.getDueledPlayers()) {
								if (dueled.isOnline()) dueled.getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has left the Server! Duel cancelled.");
								}											
						}
						sp.getDuels().clear();
					}
				}
				
				for (RelationshipRequest rr : FriendsManager.getManager().getAllFriendRequest(p.getUniqueId())) {
					FriendsManager.getManager().getFriendRequests().remove(rr);

					List<UUID> players = new ArrayList<UUID>();
					players.addAll(rr.getReceptor());
					players.addAll(rr.getSender());
					for (UUID  u: players) {
						OfflinePlayer p = Bukkit.getOfflinePlayer(u);
						if (p.isOnline())  p.getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has left the Server! Friend request cancelled.");
					}					
				}			
			}
		}.runTaskAsynchronously(Main.get());
		
		
		
		//DataManager.getManager().getToUnloadSet().add(sp);
		new BukkitRunnable() {
			public void run() {
		HikariAPI.getManager().saveData(sp); 
		DataManager.getManager().removePlayer(sp.getUUID());				
		}
		}.runTaskAsynchronously(Main.get());
		
		Main.scoreboardUpdate = true;
	}
	
	
}

	

