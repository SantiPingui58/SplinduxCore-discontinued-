package me.santipingui58.splindux.relationships.parties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class Party {

	private UUID leader;
	private List<UUID> members = new ArrayList<UUID>();
	private Set<UUID> invited = new HashSet<UUID>();
	private Set<UUID> disconnectedPlayers = new HashSet<UUID>();
	private PartyMode partyMode;
	
	
	
	private boolean isSendingMultipleDuels;
	
	public Party(UUID leader, PartyMode partyMode) {
		this.leader = leader;
		this.partyMode = partyMode;
		PartyManager.getManager().getParties().add(this);
	}
	
	public boolean isSendingMultipleDuels() {
		return this.isSendingMultipleDuels;
	}
	
	
	//Santi
	//Luna
	
	public void sendDuel(SpleefType type) {
			
		int teamSize = getAllMembers().size()/2;
		//1
		
		
		List<SpleefPlayer> sp2 = new ArrayList<SpleefPlayer>();
		
		for (UUID u :  getMembers()) {
			sp2.add(SpleefPlayer.getSpleefPlayer(u));
		}
		
		
		Collections.shuffle(sp2);
		//1
		
		
	if (getAllMembers().size()%2!=0) 
		sp2.remove(sp2.size()-1);
		GameManager.getManager().duelGame(SpleefPlayer.getSpleefPlayer(getLeader()), sp2, null, type, teamSize, false, false, -1);

	
	}
	
	
	public void sendMultipleDuels(int teamSize, SpleefType type) {
		this.isSendingMultipleDuels = true;
		new BukkitRunnable() {
			public void run() {
		List<SpleefPlayer> sp2 = new ArrayList<SpleefPlayer>();
		
		for (UUID u :  getAllMembers()) {
			sp2.add(SpleefPlayer.getSpleefPlayer(u));
		}
		
		Collections.shuffle(sp2);
		if (sp2.size()+1%2!=0) sp2.remove(sp2.size()-1);
		
		//   
		//Dueler: patata
		//spp: endr
		//i: 0
		//teamSize 2
		
		int i =0;
		SpleefPlayer dueler = null;
		List<SpleefPlayer> spp = new ArrayList<SpleefPlayer>();
		List<SpleefPlayer> playing = new ArrayList<SpleefPlayer>();
		for (SpleefPlayer sp : sp2) {
			if (dueler==null) {
				dueler = sp;
			} else {
				if (i<=teamSize) {
					spp.add(sp);
					i++;
				} else {
					playing.add(dueler);
					playing.addAll(spp);
					GameManager.getManager().duelGame(dueler, spp, null, type, teamSize, false, false, -1);
					i = 0;
					dueler = null;
					spp.clear();
				}
			}
		}
		spp.removeAll(playing);
		if (!spp.isEmpty()) {
			int size  = spp.size()/2;
			dueler = spp.get(0);
			spp.remove(dueler);
			GameManager.getManager().duelGame(dueler, spp, null, type, size, false, false, -1);
		}
		}
		}.runTaskAsynchronously(Main.get());
		
		
		new BukkitRunnable() {
			public void run () {
				isSendingMultipleDuels = false;
			}
		}.runTaskLaterAsynchronously(Main.get(), 60L);
	}
	
	public UUID getLeader() {
		return this.leader;
	}
	
	public Set<UUID> getInvited() {
		return this.invited;
	}
	
	public List<UUID> getMembers() {
		return this.members;
	}
	
	public PartyMode getPartyMode() {
		return this.partyMode;
	}

	public void setPartMode(PartyMode partyMode) {
		this.partyMode = partyMode;
	}

	
	
	public void broadcast() {
		OfflinePlayer leader = Bukkit.getOfflinePlayer(this.leader);
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(leader);
		if (TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, TimeLimitType.PARTY_BROADCAST)) return;
		
		TimeLimitManager.getManager().addTimeLimit(sp, 5, TimeLimitType.PARTY_BROADCAST, null);
			TextComponent msg1 = new TextComponent("[JOIN PARTY]");
			msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
			msg1.setBold( true );
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + leader.getName()));	
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept duel request").create()));
			ComponentBuilder cb = new ComponentBuilder(msg1);
		if (this.partyMode.equals(PartyMode.PUBLIC)) {
			DataManager.getManager().broadcast("§5§l[Party] §b§l" +  leader.getName() + " §e§lis playing in a party. Join them doing §b§l/party join " + leader.getName(), true);
			new BukkitRunnable() {
				public void run() {
			for (Player p : Bukkit.getOnlinePlayers()) p.spigot().sendMessage(cb.create());
				}
			}.runTaskLater(Main.get(), 3L);
			
		} else if (this.partyMode.equals(PartyMode.ONLY_FRIENDS)) {
			sp.sendMessage("§5§l[Party] §b§l" +  leader.getName() + " §e§lis playing in a party. Join them doing §b§l/party join " + leader.getName());
			for (UUID u : sp.getFriends()) {
				if (Bukkit.getOfflinePlayer(u).isOnline()) {
					Bukkit.getPlayer(u).sendMessage("§5§l[Party] §b§l" +  leader.getName() + " §e§lis playing in a party. Join them doing §b§l/party join " + leader.getName());
				new BukkitRunnable() {
					public void run() {
						Bukkit.getPlayer(u).spigot().sendMessage(cb.create());
					}
				}.runTaskLater(Main.get(), 3L);
			}
			}
		}
		
	}

	public void kickOfflinePlayer() {
		List<UUID> toRemove = new ArrayList<UUID>();
		for (UUID u : members) {
				if (!Bukkit.getOfflinePlayer(u).isOnline()) 
					toRemove.add(u);
		}
		for (UUID u : members) 
			if (Bukkit.getOfflinePlayer(u).isOnline()) Bukkit.getPlayer(u).sendMessage("§5[Party] §cOffline players were removed from the party");
		members.removeAll(toRemove);
		
	}

	public void disband() {
		partyMessage("§cThe party has been disbanded.");
		PartyManager.getManager().getParties().remove(this);
	}
	
	
	public void partyMessage(String msg) {
		for (UUID u : getAllMembers()) 
			if (Bukkit.getOfflinePlayer(u).isOnline()) Bukkit.getPlayer(u).sendMessage("§5[Party] "+ msg);
	}

	public void invite(List<SpleefPlayer> sp2) {
		OfflinePlayer leader = Bukkit.getOfflinePlayer(this.leader);
		for (SpleefPlayer sp : sp2) {
			this.invited.add(sp.getUUID());
			sp.sendMessage("§5§l[Party] §b§l" +  leader.getName() + " §e§lhas invited you to their party. Join them doing §b§l/party join " + leader.getName());
			partyMessage("§b"+sp.getName() + " §ahas been invited to the party!");
		}
		

		
		
	}
	
	public int getMaxSize() {
		
		PermissionUser s = PermissionsEx.getUser(Bukkit.getPlayer(this.leader));
		if (s.has("splindux.extreme")) {
			 return 100;
		} else if (s.has("splindux.epic")) {
			return 20;
		} else if (s.has("splindux.vip")) {
			return 10;
		} else {
			return 5;
		}
	}
	
	public void join(Player player) {
		if (this.members.size()<getMaxSize()) {
		this.members.add(player.getUniqueId());
		this.invited.remove(player.getUniqueId());
		partyMessage("§b"+player.getName()+" §ahas joined the party!");
		} else {
			player.sendMessage("§cThis party is full.");
		}
	}
	
	public void leave(Player p) {
		partyMessage("§b"+p.getName()+" §chas left the party.");
		this.members.remove(p.getUniqueId());
		this.invited.remove(p.getUniqueId());
	}

	public boolean isLeader(OfflinePlayer player) {
		return this.leader.compareTo(player.getUniqueId())==0 ? true : false;
	}

	public void kick(Player p) {
		partyMessage("§b"+p.getName()+" §chas been kicked from the the party.");
		this.members.remove(p.getUniqueId());
		this.invited.remove(p.getUniqueId());
	}
	
	public void disconnected(OfflinePlayer p) {
		if (!this.isLeader(p)) {
		this.members.remove(p.getUniqueId());
		this.invited.remove(p.getUniqueId());
		this.disconnectedPlayers.remove(p.getUniqueId());
		partyMessage("§b"+p.getName()+" §chas been kicked from the the party for being offline for more than 5 minutes.");
		} else {
			partyMessage("§b"+p.getName()+" §chas been kicked from the the party for being offline for more than 5 minutes.");
			
			if (this.members.size()<=0) {
				disband();
			} else {
			transferLeadership(Bukkit.getPlayer(this.members.get(0)));
			}
			
			this.members.remove(p.getUniqueId());
		}
	}

	public void transferLeadership(Player p) {
		this.leader = p.getUniqueId();
		this.members.add(p.getUniqueId());	
		partyMessage("§b"+p.getName()+" §ais now the leader of the party!");
	}
	
	
	public void warp(Player player) {
		SpleefPlayer leader = SpleefPlayer.getSpleefPlayer(this.leader);
		

		
		if (player==null) {
			for (UUID u : this.members) {
				warp(Bukkit.getPlayer(u));
			}
			return;
		}
		
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(player);
		sp.leave(false, false);
		
		
		new BukkitRunnable() {
			public void run() {
		if (leader.isInArena()) {
			Arena arena = leader.getArena();	
			if (sp.getArena()!=null && sp.getArena().equals(arena)) return;
			if (arena.getGameType().equals(GameType.FFA)) {
				GameManager.getManager().addFFAQueue(sp, arena.getSpleefType());
			} else {
				new BukkitRunnable() {
					public void run() {
				SpectateManager.getManager().spectateSpleef(sp, arena);
				}
				}.runTaskLater(Main.get(),10L);
			}
		} else if (leader.isSpectating()) {
			Arena spectating = leader.getSpleefArenaSpectating();
			if (sp.isSpectating() && sp.getSpleefArenaSpectating().equals(spectating)) return;
			SpectateManager.getManager().spectateSpleef(sp, spectating);
	} 
			}
		}.runTaskLater(Main.get(),20L);
		
		
		
	}

	public Set<UUID> getAllMembers() {
		Set<UUID> set = new HashSet<UUID>();
		set.addAll(this.getMembers());
		set.add(this.leader);
		return set;
	}

	public void disconnect(Player p) {
		this.disconnectedPlayers.add(p.getUniqueId());
		this.members.remove(p.getUniqueId());
		
		new BukkitRunnable() {
			int i = 0;
			public void run() {
				if (!disconnectedPlayers.contains(p.getUniqueId())) {
					cancel();
				} else {
					if (i>=60*5) {
						OfflinePlayer off = Bukkit.getOfflinePlayer(p.getUniqueId());
						if (!off.isOnline()) { 
							disconnected(p);
							cancel();
						}
					} else {
						i++;
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20L);
	}	
	
	
	
	public void reconnect(Player p) {
		if (!this.isLeader(p)) {
			this.members.add(p.getUniqueId());
			this.disconnectedPlayers.remove(p.getUniqueId());
		}
		
		partyMessage("§b"+p.getName()+" §ahas reconnected!");
		
	}

	public Set<UUID> getDisconnectedPlayers() {
		return this.disconnectedPlayers;
	}
	
	
	
	
}
