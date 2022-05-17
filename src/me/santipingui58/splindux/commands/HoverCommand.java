package me.santipingui58.splindux.commands;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationState;
import me.santipingui58.splindux.game.spleef.ArenaRequest;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.RelationshipRequestType;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.vote.VoteManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimit;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;



public class HoverCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
			
		if(cmd.getName().equalsIgnoreCase("hover")){
			Player p = (Player) sender;
			if (args.length==0) return false;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);			 
			 switch(args[0]) {
			 case "duelaccept": duelAccept(sp,args);
			 return true;
			 case "dueldeny": duelDeny(sp,args);
			 return true;
			 case "duelcancel": duelCancel(sp,args);
			 return true;
			 case "record": record(sp,args,sender);
			 return true;
			 case "crumblecancel": crumbleCancel(sp,args);
			 return true;
			 case "crumbledeny": crumbleDeny(sp,args);
			 return true;
			 case "crumbleaccept": crumbleAccept(sp,args);
			 return true;
			 case "playtocancel": playtoCancel(sp,args);
			 return true;
			 case "playtodeny": playtoDeny(sp,args);
			 return true;
			 case "playtoaccept": playtoAccept(sp,args);
			 return true;
			 case "mutationaccept": mutationAccept(sp,args);
			 return true;
			 case "votenamemc": VoteManager.getManager().voteNameMC(sp);
			 return true;
			 case "frienddeny": friendDeny(sp,args);
			 return true;
			 case "friendaccept": friendAccept(sp,args);
			 return true;
			 case "joinguildplayeraccept": joinGuildPlayerAccept(sp,args);
			 return true;
			 case "joinguildmemberaccept": joinGuildMemberAccept(sp,args);
			 return true;
			 case "renegociateguildaccept": renegociateGuildAccept(sp,args);
			 return true;
			 case "joinguildplayerdeny": joinGuildPlayerDeny(sp,args);
			 return true;
			 case "joinguildmemberdeny": joinGuildMemberDeny(sp,args);
			 return true;
			 case "renegociateguilddeny": renegociateGuildDeny(sp,args);
			 return true;
			 case "guildduelaccept": duelGuildAccept(sp,args);
			 return true;
			 case "guilddueldeny": duelGuildDeny(sp,args);
			 return true;
			 case "buyplayeraccept": buyPlayerAccept(sp,args);
			 return true;
			 case "buyplayerdeny": buyPlayerDeny(sp,args);
			 return true;
			 case "gift": gift(sp,args);
			 return true;
			 }		 
			} 			
}	
		return false;
	}

	


	private void gift(SpleefPlayer sp, String[] args) {
		UUID uuid2 = UUID.fromString(args[1]);
		if (TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, TimeLimitType.GIFT)) {
			List<TimeLimit> timelimits = TimeLimitManager.getManager().getTimeLimitBy(sp, TimeLimitType.GIFT);
			for (TimeLimit tl : timelimits) {
			UUID uuid1 = UUID.fromString(tl.getArgs()[0]);
		
			if (uuid1.compareTo(uuid2)==0) {
				sp.sendMessage("§cYou already sent a gift to this player. You need to wait: §e" +  tl.getLeftTime());
				return;
			}
			}
		}
		
		String[] argss = {uuid2.toString()};
		TimeLimitManager.getManager().addTimeLimit(sp, 1440, TimeLimitType.GIFT, argss);

		OfflinePlayer pa = Bukkit.getOfflinePlayer(uuid2);
		sp.sendMessage("§aYou sent a gift to §b" +  pa.getName());
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(pa);
					splayer.addCoins(25);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	}




	private void buyPlayerDeny(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.BUY_PLAYER,sp2.getUUID(), sp.getUUID());
					List<UUID> list = new ArrayList<UUID>();
					list.addAll(request.getReceptor());
					list.addAll(request.getSender());
					for (UUID u : list) {
						OfflinePlayer off = Bukkit.getOfflinePlayer(u);
						if (off.isOnline()) off.getPlayer().sendMessage("§b"+sp.getOfflinePlayer() + "§c has denied the request.");
					}
					
					GuildsManager.getManager().getBuyPlayerRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	
		
		
		
	}




	private void buyPlayerAccept(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.BUY_PLAYER,sp2.getUUID(), sp.getUUID());
					if (request!=null) {
					request.acceptRequest(null);
					} else {
						sp.sendMessage("§This request has expired.");
					}
					GuildsManager.getManager().getBuyPlayerRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	
		
		
		
	}




	private void duelGuildDeny(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.GUILD_DUEL,sp2.getUUID(), sp.getUUID());
					GuildsManager.getManager().getDuelRequests().remove(request);
					List<UUID> list = new ArrayList<UUID>();
					list.addAll(request.getReceptor());
					list.addAll(request.getSender());
					for (UUID u : list) {
						OfflinePlayer off = Bukkit.getOfflinePlayer(u);
						if (off.isOnline()) off.getPlayer().sendMessage("§b"+sp.getOfflinePlayer().getName() + "§c has denied the request.");
					}
					
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
	}



	private void duelGuildAccept(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.GUILD_DUEL,sp2.getUUID(), sp.getUUID());
					if (request!=null) {
					Player player = Bukkit.getPlayer(args[1]);
					String[] argss = {sp.getUUID().toString(), player.getUniqueId().toString()};
					request.acceptRequest(argss);
					} else {
						sp.getPlayer().sendMessage("§cThis request has expired.");
					}
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	}




	private void renegociateGuildDeny(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.RENEGOCIATE_GUILD,sp2.getUUID(), sp.getUUID());
					List<UUID> list = new ArrayList<UUID>();
					list.addAll(request.getReceptor());
					list.addAll(request.getSender());
					for (UUID u : list) {
						OfflinePlayer off = Bukkit.getOfflinePlayer(u);
						if (off.isOnline()) off.getPlayer().sendMessage("§b"+sp.getOfflinePlayer() + "§c has denied the request.");
					}
					GuildsManager.getManager().getRenegociateRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
		
	}



	private void joinGuildMemberDeny(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.JOIN_GUILD_AS_MEMBER,sp2.getUUID(), sp.getUUID());
					List<UUID> list = new ArrayList<UUID>();
					list.addAll(request.getReceptor());
					list.addAll(request.getSender());
					for (UUID u : list) {
						OfflinePlayer off = Bukkit.getOfflinePlayer(u);
						if (off.isOnline()) off.getPlayer().sendMessage("§b"+sp.getOfflinePlayer() + "§c has denied the request.");
					}
					GuildsManager.getManager().getJoinGuildMembersRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
		
		
	}



	private void joinGuildPlayerDeny(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.JOIN_GUILD_AS_PLAYER,sp2.getUUID(), sp.getUUID());
					String[] args = {pa.getName(),pa.getUniqueId().toString()};
					request.acceptRequest(args);
					GuildsManager.getManager().getJoinGuildPlayersRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
		
		
	}



	private void renegociateGuildAccept(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.RENEGOCIATE_GUILD,sp2.getUUID(), sp.getUUID());
					request.acceptRequest(null);
					GuildsManager.getManager().getJoinGuildPlayersRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	
		
		
		
	}



	private void joinGuildMemberAccept(SpleefPlayer sp, String[] args) {

		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.JOIN_GUILD_AS_MEMBER,sp2.getUUID(), sp.getUUID());
					request.acceptRequest(null);
					GuildsManager.getManager().getJoinGuildPlayersRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	
		
		
	}



	private void joinGuildPlayerAccept(SpleefPlayer sp, String[] args) {

		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.JOIN_GUILD_AS_PLAYER,sp2.getUUID(), sp.getUUID());
					request.acceptRequest(null);
					GuildsManager.getManager().getJoinGuildPlayersRequests().remove(request);
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	
		
		
	}



	private void friendAccept(SpleefPlayer sp, String[] args) {	

		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
		
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa);
					RelationshipRequest request = FriendsManager.getManager().getFriendRequest(sp2.getUUID(), sp.getUUID());
					if (request!=null) {
					request.acceptRequest(null);
					FriendsManager.getManager().getFriendRequests().remove(request);
					if (pa.isOnline()) pa.getPlayer().sendMessage("§b" + sp.getOfflinePlayer().getName() +  " §ahas accepted your friend request!");
					if (sp.getOfflinePlayer().isOnline()) sp.getPlayer().sendMessage("§aYou have added §b" + sp2.getOfflinePlayer().getName() +  " §ato your friends list!");
					} else {
						if (sp.getOfflinePlayer().isOnline()) sp.getPlayer().sendMessage("§cThis request has expired.");
					}
				
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
		
	
	}
	
	
	
	private void friendDeny(SpleefPlayer sp, String[] args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer pa = Bukkit.getOfflinePlayer(args[1]);
		if (pa.isOnline()) pa.getPlayer().sendMessage("§b" + sp.getOfflinePlayer().getName() +  " §chas denied your friend request.");
		if (sp.getOfflinePlayer().isOnline()) sp.getPlayer().sendMessage("§aYou have denied §b" + args[1] +  " §cfriend request.");
		RelationshipRequest request = FriendsManager.getManager().getFriendRequest(pa.getUniqueId(), sp.getUUID());
		FriendsManager.getManager().getFriendRequests().remove(request);

	}




	private void mutationAccept(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame() || sp.isInQueue()) {
			UUID uuid = UUID.fromString(args[1]);
			GameMutation mutation = GameManager.getManager().getFFAArenaByArena(sp.getArena()).getMutationBy(uuid);
			if (!mutation.getOwner().equals(sp)) {
			if (mutation!=null && mutation.getState().equals(MutationState.VOTING)) {
				mutation.voteMutation(sp);
			} else {
				p.sendMessage("§cThis mutation voting has finished or you are not in a game.");
			}
			}
	} else {
		p.sendMessage("§cYou need to be in a FFA game to execute this command.");	
	}
		
	}



	private void playtoAccept(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (arena.getPlayToRequest()!=null) {
				ArenaRequest request = arena.getPlayToRequest();
				if (!request.getAcceptedPlayers().contains(sp)) {
					request.playtoAccept(sp);
				} else {
					p.sendMessage("§cYou already accepted this request.");
				}
			}else {
				p.sendMessage("§cThis crumble request has expired.");
			}
	
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}
		
	}



	private void playtoDeny(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (arena.getPlayToRequest()!=null) {
			for (SpleefPlayer dueled : arena.getPlayers()) {
				dueled.getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has denied the request! Playto cancelled.");
			}
			
			arena.setPlayToRequest(null);
			
			} else {
				p.sendMessage("§cThis crumble request has expired.");
			}
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}
		
	}



	private void playtoCancel(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			ArenaRequest request = arena.getPlayToRequest();		
		if (request!=null) {
			if (request.getChallenger().equals(sp)) {
		for (SpleefPlayer dueled : arena.getViewers()) {
			dueled.getPlayer().sendMessage("§b" + sp.getName() + "§c has cancelled the  playto request.");
		}
		arena.setPlayToRequest(null);
			} else {
				p.sendMessage("§cThis duel request has expired.");
			}
		} else {
			p.sendMessage("§cThis duel request has expired.");
		}
	} else {
		p.sendMessage("§cThis crumble request has expired.");
	}
		
	}



	private void crumbleAccept(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
			if (arena.getCrumbleRequest()!=null) {
				ArenaRequest request = arena.getCrumbleRequest();
				if (!request.getAcceptedPlayers().contains(sp)) {
					request.crumbleAccept(sp);
				} else {
					p.sendMessage("§cYou already accepted this request.");
				}
			}else {
				p.sendMessage("§cThis crumble request has expired.");
			}
	} else {
		p.sendMessage("§cOnly alive players can execute this command.");
	}
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}
		
	}



	private void crumbleDeny(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (arena.getCrumbleRequest()!=null) {
			for (SpleefPlayer dueled : arena.getPlayers()) {
				dueled.getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has denied the request! Crumble cancelled.");
			}
			
			arena.setCrumbleRequest(null);
			
			} else {
				p.sendMessage("§cThis crumble request has expired.");
			}
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}		
	}



	private void crumbleCancel(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			ArenaRequest request = arena.getCrumbleRequest();		
		if (request!=null) {
			if (request.getChallenger().equals(sp)) {
		for (SpleefPlayer dueled : arena.getViewers()) {
			dueled.getPlayer().sendMessage("§b" + sp.getName() + "§c has cancelled the  crumble request.");
		}
		
			arena.setCrumbleRequest(null);
			} else {
				p.sendMessage("§cThis duel request has expired.");
			}
		} else {
			p.sendMessage("§cThis duel request has expired.");
		}
	} else {
		p.sendMessage("§cThis crumble request has expired.");
	}	
	}



	private void record(SpleefPlayer sp, String[] args,CommandSender sender) {
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
				if (arena.getRecordingRequest()) {
					arena.record();
					arena.cancelRecordingRequest();
					List<Player> list = new ArrayList<Player>();
					List<SpleefPlayer> playerss = new ArrayList<SpleefPlayer>();
							playerss.addAll(arena.getDuelPlayers1());
					playerss.addAll(arena.getDuelPlayers2());
					for (SpleefPlayer spp : playerss) {
						list.add(spp.getPlayer());
					}							
					 //Player[] myArray = new Player[list.size()];
					// GameReplay replay = ReplayManager.getManager().createReplay(ReplayManager.getManager().getDuelName(arena));
					//ReplayAPI.getInstance().recordReplay(replay.getName(), sender,  list.toArray(myArray));
					sp.getPlayer().sendMessage("§aYou are now recording this game!");
				}
		}	
	}



	private void duelCancel(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		SpleefDuel duel = sp.getDuelByUUID(UUID.fromString(args[1]));		
		if (duel!=null) {
			for (SpleefPlayer dueled : duel.getDueledPlayers()) {
				dueled.getPlayer().sendMessage("§b" + sp.getName() + "§c has cancelled the duel request.");
			}
			duel.getChallenger().getPlayer().sendMessage("§cYou have cancelled the duel request.");
			duel.getChallenger().getDuels().remove(duel);
			} else {
				p.sendMessage("§cThis duel request has expired.");
			}
		
	}



	private void duelAccept(SpleefPlayer sp,String[] args) {	
		Player p = sp.getPlayer();
		Player p2 = Bukkit.getPlayer(args[1]);
		if (Bukkit.getOnlinePlayers().contains(p2)) {
			 SpleefPlayer challenger = SpleefPlayer.getSpleefPlayer(p2);
			if (challenger.hasDueled(sp)) {
				SpleefDuel duel = challenger.getDuelByDueledPlayer(sp);
				if (!sp.isInGame() && !sp.isinParkour() && PartyManager.getManager().getParty(sp.getPlayer())==null) {
					if (!challenger.isInGame()) {
						duel.acceptDuel(sp);
						} else {
						p.sendMessage("§cThis player is already in game!");	
					}
				} else {
					p.sendMessage("§cYou can not execute this command here.");	
				}
			} else {
				p.sendMessage("§cThis duel request has expired or the player didn't send you a duel request!");
			}
		} else {
			p.sendMessage("§cThis duel request has ended since the player isnt online!");
		}
		
	}
	
	
	private void duelDeny(SpleefPlayer sp, String[] args) {
		Player p = sp.getPlayer();
		Player p2 = Bukkit.getPlayer(args[1]);
		if (Bukkit.getOnlinePlayers().contains(p2)) {
			 SpleefPlayer challenger = SpleefPlayer.getSpleefPlayer(p2);
			if (challenger.hasDueled(sp)) {
				SpleefDuel duel = challenger.getDuelByDueledPlayer(sp);
				sp.getPlayer().sendMessage("§cYou have denied the duel request from §b" + challenger.getName() + "§c!");
				
				for (SpleefPlayer dueled : duel.getAllPlayers()) {
					if (dueled!=sp)
						dueled.getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has denied the request! Duel cancelled.");
				}
				challenger.getDuels().remove(duel);
			} else {
				p.sendMessage("§cThis duel request has expired or the player didn't send you a duel request!");
			}
			} else {
				p.sendMessage("§cThis duel request has ended since the player isnt online!");
			}
		
	}

	
}