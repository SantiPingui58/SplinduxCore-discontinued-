package me.santipingui58.splindux.listener;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
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
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.ffa.FFATeam;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.hologram.StaticRankingType;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerConnectListener implements Listener {


	@EventHandler
	public void onPreJoin (AsyncPlayerPreLoginEvent e) {
		if (!Main.canJoin) {
		e.setKickMessage("Server loading! Please try again in a few seconds");
		e.setLoginResult(Result.KICK_OTHER);
		e.setKickMessage("Server loading! Please try again in a few seconds");
	}
	}
	
	
	@EventHandler (priority=EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null); 
		e.getPlayer().setWalkSpeed(0.2F);
		e.getPlayer().setGameMode(GameMode.ADVENTURE);

		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		if (sp==null) {
		 sp = new SpleefPlayer(uuid);	
		}
		Utils.getUtils().sendTitles(sp, "", "", 1, 1, 1);
		sp.setScoreboard(ScoreboardType.LOBBY);	
		DataManager.getManager().getLobbyPlayers().add(uuid);
		sp.giveLobbyItems();
		
		new BukkitRunnable() {
			public void run() {
		if (Main.arenas.getConfig().contains("mainlobby")) {
			if (p.hasPermission("splindux.media")) {
				e.getPlayer().teleport(Main.tournament_lobby);
				} else {
					e.getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}
		}
}
		}.runTask(Main.get());
		
	
		
		if (e.getPlayer().hasPermission("splindux.fly")) {
			sp.fly();
		}
		
		
		SpleefPlayer spp = sp;

		new BukkitRunnable() {
			public void run() {
			
				PartyManager pm = PartyManager.getManager();
				Party party = pm.getParty(p);
				if (party!=null) {
					party.reconnect(p);
				}
				
				HikariAPI.getManager().createData(p.getUniqueId());
				HikariAPI.getManager().loadFriends(p.getUniqueId());
				spp.updateScoreboard();
				
		if (e.getPlayer().hasPermission("splindux.join") && spp.getOptions().joinMessageEnabled()) {
			for (Player o : Bukkit.getOnlinePlayers()) {	
				String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(e.getPlayer()).getPrefix());
				o.sendMessage(prefix+  spp.getName() + " §ahas joined the server!");
			}
		}

		for (Arena arena : DataManager.getManager().getArenas()) {
			if (arena.getDuelTempDisconnectedPlayers1().contains(uuid) || arena.getDuelTempDisconnectedPlayers2().contains(uuid)) {
				for (SpleefPlayer s : arena.getViewers()) {
					s.getPlayer().sendMessage(ChatColor.GOLD + spp.getName() + " §arejoined the match!");
				}
				
				spp.setScoreboard(ScoreboardType._1VS1GAME);
				if (arena.getDuelTempDisconnectedPlayers1().contains(uuid)) {
					arena.getDuelTempDisconnectedPlayers1().remove(uuid);
					arena.getDuelPlayers1().add(spp);
					arena.getDeadPlayers1().add(spp);
					new BukkitRunnable() {
						public void run() {
					SpectateManager.getManager().spectateSpleef(spp, arena);
				}
					}.runTaskLater(Main.get(), 10L);
				} 
				if (arena.getDuelTempDisconnectedPlayers2().contains(uuid)) { 
					arena.getDuelTempDisconnectedPlayers2().remove(uuid);
					arena.getDuelPlayers2().add(spp);
					arena.getDeadPlayers2().add(spp);
					new BukkitRunnable() {
						public void run() {
					SpectateManager.getManager().spectateSpleef(spp, arena);
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
				HologramManager.getManager().createHologram(spp, StaticRankingType.SPLEEF_ELO, new Location(Bukkit.getWorld("tournament"),-105,122,217));
				HologramManager.getManager().createHologram(spp, StaticRankingType.YT_ELO, new Location(Bukkit.getWorld("tournament"),-116,122,217));
				 Bukkit.broadcastMessage("hoooooool");
				for (UUID u : spp.getFriends()) {
					OfflinePlayer player = Bukkit.getOfflinePlayer(u);
					if (player.isOnline()) player.getPlayer().sendMessage("§a[+]§e "+p.getName() + " joined");
				}
				
				if (p.hasPermission("splindux.pet")) {
					PetService petMetaService = PetBlocksApi.INSTANCE.resolve(PetService.class);
					petMetaService.getOrSpawnPetFromPlayer(p);
					}
			}
		}.runTaskLater(Main.get(), 3L);
		
		new BukkitRunnable() {
			public void run() {
				LevelManager.getManager().setExp(spp);
		HologramManager.getManager().showHolograms(p.getUniqueId());
			}
		}.runTaskLaterAsynchronously(Main.get(), 30L);

		Guild guild = GuildsManager.getManager().getGuild(sp);
		
		if (guild!=null) {
			if (guild.hasTabTag()) {
				TABAPI.setTabSuffixTemporarily(p.getUniqueId(), " §6["+guild.getAchronym()+"]");
			}
			
			if (guild.hasHeadTag()) {
				TABAPI.setAboveNameTemporarily(p.getUniqueId(), " §6["+guild.getAchronym()+"] " + TABAPI.getOriginalAboveName(p.getUniqueId()));
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
		HologramManager.getManager().deleteStaticHolograms(sp);
		if (sp==null) return;
		
		new BukkitRunnable() {
			public void run() {
				
				HologramManager.getManager().cleanHologramCache(u);
				PartyManager pm = PartyManager.getManager();
				Party party = pm.getParty(p);
				if (party!=null) {
					party.disconnect(p);
				}
				
				for (UUID u : sp.getFriends()) {
					OfflinePlayer player = Bukkit.getOfflinePlayer(u);
					if (player.isOnline()) player.getPlayer().sendMessage("§c[-]§e "+p.getName() + " left");
				}
				
				if (sp.isInGame()) {
					Arena arena = sp.getArena();
					
					if (Main.ffa2v2) {
						if (arena.getGameType().equals(GameType.FFA)) {
							FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
						FFATeam team = ffa.getTeamByPlayer(sp.getUUID());
						team.killPlayer(sp.getUUID());
					}
					}
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
		}.runTaskLaterAsynchronously(Main.get(),10L);
		
	}
	
	
}

	

