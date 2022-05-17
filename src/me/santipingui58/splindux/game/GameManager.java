package me.santipingui58.splindux.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.death.BreakReason;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.ffa.FFAEvent;
import me.santipingui58.splindux.game.ffa.FFATeam;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.ranked.RankedTeam;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.ArenaRequest;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.relationships.guilds.GuildDuel;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.sws.SWSManager;
import me.santipingui58.splindux.task.ArenaNewStartTask;
import me.santipingui58.splindux.utils.ActionBarAPI;
import me.santipingui58.splindux.utils.Utils;

public class GameManager {

	private static GameManager manager;	
	 public static GameManager getManager() {
	        if (manager == null)
	        	manager = new GameManager();
	        return manager;
	    }
	
	private FFAArena SpleefFFA;
	private FFAArena TNTRunFFA;
	private FFAArena SpleggFFA;
	 	
	private HashMap<SpleefPlayer,Long> interact = new HashMap<SpleefPlayer,Long>();
	
	private HashMap<SpleefPlayer,BukkitRunnable> runnables = new HashMap<SpleefPlayer,BukkitRunnable>();
	
	public HashMap<SpleefPlayer,Long> getInteract() {
		return this.interact;
	}
	
	public HashMap<SpleefPlayer,BukkitRunnable>getRunnables() {
		return this.runnables;
	}
	
	 public FFAArena getFFAArena(SpleefType type) {
		 
		 switch(type) {
		 case SPLEEF: return SpleefFFA;
		 case TNTRUN: return TNTRunFFA;
		 case SPLEGG: return SpleggFFA;
		default:break;
		 }
		 return null;
	 }
	 
	 
	 public FFAArena getFFAArenaByArena(Arena arena) {
		 if (this.SpleefFFA==null) return null;
		 if(this.SpleefFFA.getArena()!=null &&  this.SpleefFFA.getArena().equals(arena)) return this.SpleefFFA;
		 if(this.TNTRunFFA.getArena()!=null && this.TNTRunFFA.getArena().equals(arena)) return this.TNTRunFFA;
		 if(this.SpleggFFA.getArena()!=null&& this.SpleggFFA.getArena().equals(arena)) return this.SpleggFFA;
		 return null;
	 }
	 
	 public int getAvailableArenasFor(String s) {
		 int i = 0;
		 for (Arena arena : DataManager.getManager().getArenas()) {

			 if (!arena.getPlayers().isEmpty() && arena.getGameType().equals(GameType.DUEL)) {
			 if (Utils.getUtils().containsIgnoreCase(arena.getName(), s)) {
				 i++;
			 }
		 }
		 }
		 return i;
	 }
 
	 
	 
	 

		public Arena duelGame(SpleefPlayer challenger, List<SpleefPlayer> sp2,String ar,SpleefType type,int teamSize,boolean cantie,boolean guild,int time) {
			challenger.getDuels().remove(challenger.getDuelByDueledPlayer(sp2.get(0)));
			
			Arena arena = findArena(ar,type,teamSize);
					
			if (arena!=null) {
				 challenger.leave(false,false);
				for (SpleefPlayer sp_2 : sp2) sp_2.leave(false,false);
				
				arena.getQueue().add(challenger);
				arena.getQueue().addAll(sp2);
				arena.setTeamSize(teamSize);
				arena.ranked(true);			
				
				
				int i=0;
				List<SpleefPlayer> t1 = new ArrayList<SpleefPlayer>();
				List<SpleefPlayer> t2 = new ArrayList<SpleefPlayer>();
				for (SpleefPlayer sp : new ArrayList<>(arena.getQueue())) {
					if (i<arena.getQueue().size()/2) {
						t1.add(sp);
					} else {
						t2.add(sp);
					}
					i++;
				}
				
				
				
				
				
				RankedTeam team1 = new RankedTeam(t1);
				RankedTeam team2 = new RankedTeam(t2);
				
				arena.setRankedTeam1(team1);
				arena.setRankedTeam2(team2);
				
				arena.setTimedMaxTime(time);
				arena.setTie(cantie);
				arena.isGuildGame(guild);
				arena.startGameDuel(false);		
			
				
			} else {
				new BukkitRunnable() {
					public void run() {
				challenger.getPlayer().sendMessage("§cCouldn't find a map to play! Duel cancelled.");
				for (SpleefPlayer sp_2 : sp2)sp_2.getPlayer().sendMessage("§cCouldn't find a map to play! Duel cancelled.");
				}
				}.runTask(Main.get());
				
				return null;
			}
			
			/*
			new BukkitRunnable() {
				public void run() {
			
			for (SpleefPlayer sp : arena.getPlayers()) {
			if (sp.getPlayer().isOp()) {
				TextComponent message = new TextComponent("§2Would you like to record this game? [CLICK HERE]");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/hover record"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Record this game §a").create()));
					sp.getPlayer().spigot().sendMessage(message);
			}
			}
				}
				
			}.runTask(Main.get());
			
			arena.doRecordingRequest();
			new BukkitRunnable() {
				public void run() {
					arena.cancelRecordingRequest();
				}
			}.runTaskLater(Main.get(),20L*10);
			*/
			
			return arena;
		}
		
		public Arena findArena(String ar,SpleefType type,int teamSize) {
			if (ar==null) {	
				return findAnyPlayableArena(type,teamSize);
		
			} else {
				 return findPlayableArenaBy(ar,type);		
				}	
		}
		
		
		public Arena findPlayableArenaBy(String s,SpleefType type) {
			List<Arena> arenas = DataManager.getManager().getArenas();
			Collections.shuffle(arenas);
			for (Arena a : arenas) {
				if (a.getGameType().equals(GameType.DUEL) && a.getSpleefType().equals(type)) {
				if (a.getDuelPlayers1().isEmpty() && a.getDuelPlayers2().isEmpty() && a.getQueue().isEmpty()) {
					if (Utils.getUtils().containsIgnoreCase(a.getName(), s))
					return a;
				}
				} 
			}
			return null;
		}
		
		public Arena findAnyPlayableArena(SpleefType type,int teamSize) {
			List<Arena> arenas = DataManager.getManager().getArenas();
			Collections.shuffle(arenas);
	
			for (Arena a : arenas) {
				if (a.getGameType().equals(GameType.DUEL) && a.getSpleefType().equals(type) && a.getMinPlayersSize()<=teamSize && a.getMaxPlayersSize()>=teamSize) {
				if (a.getPlayers().isEmpty() && a.getQueue().isEmpty()) {
					return a;
				}
				} 
			}
			return null;
		}


		public List<SpleefPlayer> leftPlayersToSomething(List<SpleefPlayer> something,Arena arena,boolean canBeDead) {
			List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
			for (SpleefPlayer s : arena.getPlayers()) {
				if (!canBeDead) {
				if (arena.getDeadPlayers1().contains(s) || arena.getDeadPlayers2().contains(s)) {
				} else {
				if (!something.contains(s)) {
					list.add(s);
				}
				}
				} else {
					if (!something.contains(s)) {
						list.add(s);
					}
				}
			}
			return list;
		}
		
		
		
		public void resetArenaWithCommand(Arena arena) {
			new BukkitRunnable() {
				public void run() {
 			if (!arena.getState().equals(GameState.GAME)) return;
			
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer players : arena.getViewers()) {
				if (players.getOfflinePlayer().isOnline()) {
				players.getPlayer().sendMessage("§6The arena has been reset.");						
		}
			}
			}
			}.runTask(Main.get());
			
			arena.resetArena();
			arena.setState(GameState.STARTING);		
			
				}
		}.runTaskAsynchronously(Main.get());
		}
		
		public void playToWithCommand(Arena g,int crumble) {
			g.setPlayTo(crumble);
			g.setPlayToRequest(null);
			
			for (SpleefPlayer p2 : g.getViewers()) {
					p2.getPlayer().sendMessage("§6The arena playto has been set to: §6" + crumble);

			}
			
			
		}
		
		public void crumbleWithCommand(Arena g,int crumble) {
			g.crumbleArena(crumble);
			g.setCrumbleRequest(null);	
			
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer p2 : g.getViewers()) {
					p2.getPlayer().sendMessage("§6The arena has been crumbled with a percentage: §a" + crumble);
			}			
			}
		}.runTask(Main.get());
		}
		
	


		
		public String getGamePrefix(Arena arena) {
			
			String preprefix = null;
			switch(arena.getSpleefType()) {
			default:break;
			case SPLEEF: preprefix = "Spleef"; break;
			case TNTRUN: preprefix = "TntRun"; break;
			case SPLEGG: preprefix = "Splegg"; break;
			}
			
			
				if (arena.getGameType().equals(GameType.DUEL)) {
					return "§2["+ preprefix +" Duels] ";
				} else {
					return "§2["+ preprefix +" FFA] ";
				}
		}
		
		public String getGuildDuelPrefix(SpleefType type) {
			
			String preprefix = null;
			switch(type) {
			default:break;
			case SPLEEF: preprefix = "Spleef"; break;
			case TNTRUN: preprefix = "TntRun"; break;
			case SPLEGG: preprefix = "Splegg"; break;
			}
			
					return "§2["+ preprefix +" Guild Duel] ";
		}
		
		public void checkSpleefFFAArena(FFAArena ffa) {
			Arena arena =ffa.getArena();
			Arena original = arena;
			int max = arena.getMaxPlayersSize();
			int min = arena.getMinPlayersSize();
			if (arena.getQueue().size()>=max) {
				 arena = DataManager.getManager().loadFFAArenaNoRotate(SpleefType.SPLEEF, 1,original);
			} else if (arena.getQueue().size()<min) {
				 arena = DataManager.getManager().loadFFAArenaNoRotate(SpleefType.SPLEEF, 2,original);
			}
	
			ffa.setArena(arena);
			if (ffa.getQueue().size()>=3) {
				new ArenaNewStartTask(arena);
				arena.setState(GameState.STARTING);
			} else {
				arena.setState(GameState.LOBBY);
			}
		}


		public int getPlayingSize(SpleefType spleeftype, GameType gametype,int teamSize) {	
			int i = 0;
			if (gametype.equals(GameType.FFA)) {
				if (spleeftype!=null) {
				FFAArena ffa = GameManager.getManager().getFFAArena(spleeftype);
				return ffa.getQueue().size();
				} else {
					for (SpleefType t : SpleefType.values()) {
						Arena arena = GameManager.getManager().getFFAArena(t).getArena();
						if (arena!=null) {
							i = i + arena.getQueue().size();
						}
					}
				}
			} else {
				if (spleeftype==null) {
					int a = 0;
					for (SpleefType type : SpleefType.values()) {
						a = a + getPlayingSize(type,gametype,teamSize);
					}
					return a;
				}
				
				
				for (Arena arena : DataManager.getManager().getArenas()) {
				if (arena.getSpleefType().equals(spleeftype) && arena.getGameType().equals(gametype)) {
	
				
					if (teamSize!=0 && arena.getTeamSize()!=teamSize) continue;
					int d = arena.getPlayers().size();
					i = i + d;
				}
			
				}
			}

			return i;
		}
		
		public int getQueueSize(SpleefType spleeftype, GameType gametype, int teamSize) {
			

			
			if (spleeftype==null) {
				int a = 0;
				for (SpleefType type : SpleefType.values()) {
					a = a + getQueueSize(type,gametype,teamSize);
				}
				return a;
			}
			
			
			int i = 0;
			for (Arena arena : DataManager.getManager().getArenas()) {
				if (arena.getGameType().equals(GameType.DUEL)) {
			if (arena.getSpleefType().equals(spleeftype)) {					
				if (teamSize!=0 && arena.getTeamSize()==teamSize) {
				int d = arena.getQueue().size();
				i = i + d;
				}
			}
				}
		}	
		
			return i;
		}
		
		
		
		
		
		
		public void endGameDuel(Arena arena,String w,GameEndReason reason) {	
			//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("EndGameDuel in primary thread");
			arena.setTie(false);
			arena.setTeamSize(0);
			arena.setShrinkAmount(0);
			arena.setCrumbleAmount(0);
			arena.resetMiniSpleefRound();
			arena.resetMiniSpleefTimer();
			arena.setDecayRound(0);
			arena.reset(false, true);
			arena.resetTotalTime();
			arena.setState(GameState.FINISHING);
			arena.getResetRequest().clear();
			arena.setShrinkedDuelArena1(arena.getArena1());
			arena.setShrinkedDuelArena2(arena.getArena2());
			arena.setShrinkedDuelSpawn1(arena.getSpawn1());
			arena.setShrinkedDuelSpawn2(arena.getSpawn2());	
			arena.setDuelArena1(arena.getArena1());
			arena.setDuelArena2(arena.getArena2());
			arena.setDuelSpawn1(arena.getSpawn1());
			arena.setDuelSpawn2(arena.getSpawn2());		
			arena.getDeadPlayers1().clear();
			arena.getDeadPlayers2().clear();		
			arena.getDuelTempDisconnectedPlayers1().clear();
			arena.getDuelTempDisconnectedPlayers2().clear();
			arena.resetPlayTo();
			arena.clean(arena.getExtremeArena1(), arena.getExtremeArena2());
			arena.setExtremeArena1(arena.getArena1());
			arena.setExtremeArena2(arena.getArena2());
			List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
			players.addAll(arena.getDuelPlayers1());
			players.addAll(arena.getDuelPlayers2());
			
			for (SpleefPlayer sp : arena.getViewers()) {
				ActionBarAPI.sendActionBar(sp.getPlayer(), "");
			}
					
			players.forEach((p) -> {
				p.setScoreboard(ScoreboardType.LOBBY);
				p.getPlayer().setWalkSpeed(0.2F);
				DataManager.getManager().getLobbyPlayers().add(p.getUUID());
				DataManager.getManager().getPlayingPlayers().remove(p.getUUID());			
			}); 		
			 

					
					
			List<SpleefPlayer> spectators = new ArrayList<SpleefPlayer>();
			spectators.addAll(arena.getSpectators());				
			spectators.forEach((p)-> p.leaveSpectate(true,false,false));		
			arena.getSpectators().clear();
			
			
			arena.resetTimer();
						
			List<SpleefPlayer> winners = new ArrayList<SpleefPlayer>();
			List<SpleefPlayer> losers = new ArrayList<SpleefPlayer>();
			int elo = 0;
			
			if (reason.equals(GameEndReason.WINNER)) {
			if (w.equalsIgnoreCase("Team1")) {
				winners.addAll(arena.getDuelPlayers1());
				elo = arena.getEloWinner1();
				losers.addAll(arena.getDuelPlayers2());
			} else {
				winners.addAll(arena.getDuelPlayers2());
				losers.addAll(arena.getDuelPlayers1());
				elo = arena.getEloWinner2();
				
			}

			
			if (arena.isGuildGame()) {
				arena.isGuildGame(false);
				GuildDuel duel = GuildsManager.getManager().getGuildDuelByArena(arena);
				duel.setCoins(duel.getCoins()+(arena.getMaxSpectators()*20));
				if (w.equalsIgnoreCase("Team1")) {		
						duel.setScore1(duel.getScore1()+1);
				} else {
					duel.setScore2(duel.getScore2()+1);
					
				}
				duel.setSubScore1(duel.getSubScore1()+arena.getPoints1());
				
				duel.setSubScore2(duel.getSubScore2()+arena.getPoints2());
				

				duel.getArenas().remove(arena);
				if (duel.getArenas().size()==0) {
					new BukkitRunnable() {
						public void run() {
					duel.finishDuel();
						}
					}.runTaskLater(Main.get(), 10L);
				}
			}
			
			
			}
			
			
			
			if (reason.equals(GameEndReason.LOG_OFF)) {
				if (w.equalsIgnoreCase("Team1")) {
					winners.addAll(arena.getDuelPlayers1());	
					elo = arena.getEloWinner1();
				} else {
					winners.addAll(arena.getDuelPlayers2());
					elo = arena.getEloWinner2();
				}
				
				losers.add(arena.getDisconnectedPlayer());
				
				
				
				if (arena.isGuildGame()) {
					arena.isGuildGame(false);
					GuildDuel duel = GuildsManager.getManager().getGuildDuelByArena(arena);
					if (w.equalsIgnoreCase("Team1")) {
							duel.setScore1(duel.getScore1()+1);
					} else {
						duel.setScore2(duel.getScore2()+1);
					}
					duel.setSubScore1(duel.getSubScore1()+arena.getPoints1());
					duel.setSubScore2(duel.getSubScore1()+arena.getPoints2());
					duel.getArenas().remove(arena);
					if (duel.getArenas().size()==0) duel.finishDuel();
				}
				
				
				
				}
				
			
	
			
			if (arena.isRanked()) {
				if (!reason.equals(GameEndReason.ENDGAME)) {
					try {
				//elo = RankedManager.getManager().calcualteELO(winners, losers, arena.getTeamSize(),arena.getSpleefType());
				if (arena.getRankedTeam1().getPlayers().contains(winners.get(0).getUUID())) {
				arena.getRankedTeam1().newELO(elo,arena.getSpleefType());
				arena.getRankedTeam2().newELO(-elo,arena.getSpleefType());
				} else {
					arena.getRankedTeam1().newELO(-elo, arena.getSpleefType());
					arena.getRankedTeam2().newELO(elo,arena.getSpleefType());
				}
					} catch(Exception ex) {}
			}
			}
			
			if (reason.equals(GameEndReason.WINNER)) {	
				for (SpleefPlayer loser : losers) LevelManager.getManager().addLevel(loser, 1);
				for (SpleefPlayer winner : winners) { 
					LevelManager.getManager().addLevel(winner, 3);
					EconomyManager.getManager().addCoins(winner, 20, true,false);
					SWSManager.getManager().addPoints(winner, 20+(arena.getTeamSize()*4), true,true);
				}
			
				if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) for (SpleefPlayer winner : winners) 			winner.getPlayerStats().setDuelWins(arena.getSpleefType(), winner.getPlayerStats().getDuelWins(arena.getSpleefType())+1);;
			
			
			String p1 = arena.getTeamName(1);
			String p2 = arena.getTeamName(2);
					if (winners.equals(arena.getDuelPlayers1())) {					
						if (arena.isRanked()) {
							DataManager.getManager().broadcast(endGameMessage(p1,p2,arena.getPoints1(),arena.getPoints2(),true,elo,arena.getRankedTeam1().getELO(arena.getSpleefType()),arena.getRankedTeam2().getELO(arena.getSpleefType()),false, arena),true);
						} else {
							DataManager.getManager().broadcast(endGameMessage(p1,p2,arena.getPoints1(),arena.getPoints2(),false,0,0,0,false,arena),true);
						}											
				} else {					
					if (arena.isRanked()) {
						DataManager.getManager().broadcast(endGameMessage(p2,p1,arena.getPoints2(),arena.getPoints1(),true,elo,arena.getRankedTeam2().getELO(arena.getSpleefType()),arena.getRankedTeam1().getELO(arena.getSpleefType()),false,arena),true);
						} else {
							DataManager.getManager().broadcast(endGameMessage(p2,p1,arena.getPoints2(),arena.getPoints1(),false,0,0,0,false,arena),true);
						}
				}
				

				for (SpleefPlayer winner : winners)winner.giveLobbyItems();
			for (SpleefPlayer loser : losers)loser.giveLobbyItems();
			for (SpleefPlayer winner : winners) winner.teleportToLobby();	
			for (SpleefPlayer loser : losers) loser.teleportToLobby();

			
			
			} else if (reason.equals(GameEndReason.LOG_OFF)) {
				
				 for (SpleefPlayer winner : arena.getPlayers()) {
					 winner.giveLobbyItems();
					 winner.teleportToLobby();
				 }
				 
				 for (SpleefPlayer online : DataManager.getManager().getPlayers()) {
					 if (!online.getOfflinePlayer().isOnline()) continue;
						String p1 = arena.getTeamName(1);
						String p2 = arena.getTeamName(2);						
						if (w.equalsIgnoreCase("Team1")) {								
							if (arena.isRanked()) {
							online.getPlayer().sendMessage(endGameMessage(p1,arena.getDisconnectedPlayer().getOfflinePlayer().getName(),arena.getPoints1(),arena.getPoints2(),true,elo,arena.getRankedTeam1().getELO(arena.getSpleefType()),arena.getRankedTeam2().getELO(arena.getSpleefType()),false,arena));
							} else {
								online.getPlayer().sendMessage(endGameMessage(p1,arena.getDisconnectedPlayer().getOfflinePlayer().getName(),arena.getPoints1(),arena.getPoints2(),false,0,0,0,false,arena));
							}											
					} else {					
						if (arena.isRanked()) {
							online.getPlayer().sendMessage(endGameMessage(p2,arena.getDisconnectedPlayer().getOfflinePlayer().getName(),arena.getPoints2(),arena.getPoints1(),true,elo,arena.getRankedTeam2().getELO(arena.getSpleefType()),arena.getRankedTeam1().getELO(arena.getSpleefType()),false,arena));
							} else {
								online.getPlayer().sendMessage(endGameMessage(p2,arena.getDisconnectedPlayer().getOfflinePlayer().getName(),arena.getPoints2(),arena.getPoints1(),false,0,0,0,false,arena));
							}
					}
					}
			
			} else if (reason.equals(GameEndReason.ENDGAME)) {
				
				if (arena.isGuildGame()) {
					arena.isGuildGame(false);
					GuildDuel duel = GuildsManager.getManager().getGuildDuelByArena(arena);
					duel.setSubScore1(duel.getSubScore1()+arena.getPoints1());
					duel.setSubScore2(duel.getSubScore1()+arena.getPoints2());
					duel.getArenas().remove(arena);
					if (duel.getArenas().size()==0) duel.finishDuel();
				}
				
				for (SpleefPlayer sp : arena.getPlayers()) {
					sp.giveLobbyItems();				
					sp.teleportToLobby();			
				}
			} else if (reason.equals(GameEndReason.TIE)) {
				String p1 = arena.getTeamName(1);
				String p2 = arena.getTeamName(2);	
				DataManager.getManager().broadcast(endGameMessage(p1,p2,arena.getPoints1(),arena.getPoints2(),false,0,0,0,true,arena),true);
				
				for (SpleefPlayer sp : arena.getPlayers()) {
					sp.giveLobbyItems();				
					sp.teleportToLobby();			
				}
			}	
			
			arena.getDuelPlayers1().clear();
			arena.getDuelPlayers2().clear();
			arena.setDisconnectedPlayer(null);
			arena.setState(GameState.LOBBY);			
			
			
			
			
			
			if (arena.isRecording()) {
				//ReplayAPI.getInstance().stopReplay(arena.getReplay().getName(), true);
				arena.setReplay(null);
			}
			
			arena.resetRecord();
			arena.setPoints1(0);
			arena.setPoints2(0);
			arena.setRankedTeam1(null);
			arena.setRankedTeam2(null);
			
		}
		
		
		
		public void removeBrokenBlocksAtDead(SpleefPlayer sp,FFAArena arena) {
			
			new BukkitRunnable() {

			@Override
			public void run() {
				
			try {
			List<BrokenBlock> blocks = new ArrayList<BrokenBlock>();
			for (BrokenBlock block : arena.getBrokenBlocks()) {
				if (block.getPlayer().equals(sp)) {
					blocks.add(block);
				}
			}
			
				arena.getBrokenBlocks().removeAll(blocks);
			
			} catch(Exception ex) {}
			}
		}.runTaskLaterAsynchronously(Main.get(), 5*20L);
		}
		
		public void fell(SpleefPlayer sp,LinkedHashMap <DeathReason,SpleefPlayer> r) {	
			//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Fell in primary thread");
			
			Arena arena = sp.getArena();
			if (!arena.getState().equals(GameState.GAME)) return;			

			if (arena.getGameType().equals(GameType.FFA)) {
				FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
				removeBrokenBlocksAtDead(sp,ffa);
				sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
				ffa.getPlayers().remove(sp);
				if (ffa.isInEvent()) {		
					if (Main.ffa2v2) ffa.getTeamByPlayer(sp.getUUID()).killPlayer(sp.getUUID());
					for (SpleefPlayer players : ffa.getPlayers()) {
						if (Main.ffa2v2) {
							ffa.getEvent().addPoint2v2(players.getUUID(), 1,false,false);
							} else {
								ffa.getEvent().addPoint(players.getUUID(), 1,false);
							}
					}
					
					}
				SpectateManager.getManager().spectateSpleef(sp, arena);
				
				DeathReason reason = (new ArrayList<DeathReason>(r.keySet())).get(0);	
				SpleefPlayer killer = (new ArrayList<SpleefPlayer>(r.values())).get(0);	
				
				 if (killer!=null) {
				    	killer.getPlayer().playSound(killer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.75F);					 
				    	if (!killer.equals(sp)) {
				    		killer.addFFAKill(arena.getSpleefType());
				    		if (ffa.isInEvent() && !arena.getSpleefType().equals(SpleefType.TNTRUN)) {
				    			if (Main.ffa2v2) {
				    				if (!ffa.getTeamByPlayer(killer.getUUID()).getUUID().equals(ffa.getTeamByPlayer(sp.getUUID()).getUUID())) {
				    				ffa.getEvent().addPoint2v2(killer.getUUID(), 5,false,true);
				    				}
				    			} else {
				    				ffa.getEvent().addPoint(killer.getUUID(), 5,false);
				    			}
				    			
				    			try {
				    				FFAEvent event = ffa.getEvent();
				    				UUID lastWinner = event.getLastWinner();
				    				if (Main.ffa2v2) {
				    					FFATeam team = ffa.getTeam(sp.getUUID());
				    					if (team.getPlayer1().equals(lastWinner) || team.getPlayer2().equals(lastWinner))
				    						event.addPoint2v2(killer.getUUID(), 12, false,true);
				    				} else {
				    			if (lastWinner.equals(sp.getOfflinePlayer().getUniqueId())) event.addPoint(killer.getUUID(), 12, false);
				    				}
				    			} catch(Exception ex) {}
				    		}
				    	}
				    }
				 if (Main.ffa2v2) {
					 FFATeam team = ffa.getTeamByPlayer(sp.getUUID());
					 team.killPlayer(sp.getUUID());
				 }
				 	
				 boolean condition = Main.ffa2v2 ? ffa.getTeamsAlive().size()<=1 : ffa.getPlayers().size()<=1;
				 if (condition) {		 
						ffa.endGame(GameEndReason.WINNER);
					}
				 
					  Player p = sp.getPlayer();
		p.playSound(arena.getLobby(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 0.9F);
			sp.giveQueueItems(false,arena.getSpleefType().equals(SpleefType.SPLEEF),true);
			if (sp.getHelmet()!=null) {
				p.getInventory().setHelmet(sp.getHelmet().getItem(sp, true));
				}
			if (arena.getPlayers().size()>0)
			for (SpleefPlayer players : arena.getViewers()) {
				players.getPlayer().sendMessage(getGamePrefix(arena)+reason.getDeathMessage(killer, sp, arena));
			}

			} else if (arena.getGameType().equals(GameType.DUEL)) {
				
				boolean point = true;
				boolean reset = false;				
			
				arena.getResetRequest().remove(sp);
				
				List<SpleefPlayer> resetRequest = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
				
				if (resetRequest.size()<=1) {
					if (arena.getDuelPlayers1().size()<1 && arena.getDuelPlayers2().size()<1) {
						point = true;
					} else {									
						SpectateManager.getManager().spectateSpleef(sp, arena);		
						reset = true;
			
						GameManager.getManager().resetArenaWithCommand(arena);
						new BukkitRunnable() {
							public void run() {
						for (SpleefPlayer players : arena.getViewers()) { 
							players.getPlayer().sendMessage("§6"+ sp.getName()+ "§b fell!");							
							}	
						}
					}.runTask(Main.get());
					
					
				}
				}
				
				
	
				
				if (arena.getCrumbleRequest()!=null) {
					ArenaRequest request = arena.getCrumbleRequest();
					if (request.getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
						GameManager.getManager().crumbleWithCommand(arena, request.getAmount());
					} 
				}
							
			
					
				if (arena.getDuelPlayers1().contains(sp) && !arena.getDeadPlayers1().contains(sp)) {
					arena.getDeadPlayers1().add(sp);
					if (arena.getDeadPlayers1().size()<arena.getDuelPlayers1().size()) {
					point =false;
					}
				} else if (arena.getDuelPlayers2().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
					arena.getDeadPlayers2().add(sp);
					if (arena.getDeadPlayers2().size()<arena.getDuelPlayers2().size()) {
						point = false;
					}
				} 
				if (arena.getState().equals(GameState.GAME)) {
				if (point) {
							arena.point(sp);				 
				} else if (!reset){			
					

					for (SpleefPlayer players : arena.getViewers()) { 
						players.getPlayer().sendMessage("§6"+ sp.getName()+ "§b fell!");							
						}				
					SpectateManager.getManager().spectateSpleef(sp, arena);
							
				} else {
					arena.reset(false, true);
				}
				}
			
			}
		}
		
		
	
		

		public Arena getArenaByName(String s) {
			for (Arena arena : DataManager.getManager().getArenas()) {
				if (Utils.getUtils().containsIgnoreCase(arena.getName(), s))
					return arena;
				}
			return null;
		}
		
		
		public LinkedHashMap<DeathReason,SpleefPlayer> getDeathReason(SpleefPlayer sp) {
			LinkedHashMap<DeathReason,SpleefPlayer> hashmap = new LinkedHashMap <DeathReason,SpleefPlayer>();
			Arena arena = sp.getArena();
				BrokenBlock broken = getNearestBlockIn(sp,arena);
				if (broken!=null) {
						if (broken.getReason().equals(BreakReason.SHOVEL)) {
					hashmap.put(DeathReason.SPLEEFED, broken.getPlayer());
					return hashmap;
					} else {
						hashmap.put(DeathReason.SNOWBALLED, broken.getPlayer());
						return hashmap;
					}
					
				} else {	
					hashmap.put(DeathReason.THEMSELF, null);
				}

			return hashmap;
		}
		
		
		

		 public BrokenBlock getNearestBlockIn(SpleefPlayer sp,Arena arena) {
			 BrokenBlock block = null;
			 FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
			List<BrokenBlock> list= new ArrayList<BrokenBlock>();
			list.addAll(ffa.getBrokenBlocks());
			for (BrokenBlock broken : list) {
				 if (broken==null) continue;
				 if (broken.getPlayer().equals(sp)) continue;
				 if (!broken.isAlive()) continue;
				 
				 if (!broken.getPlayer().getOfflinePlayer().isOnline()) continue;
				 
				 if (!broken.getPlayer().getPlayer().getWorld().getName().equalsIgnoreCase(sp.getPlayer().getWorld().getName())) continue;
				 
					 Location deathPlayerLocation = sp.getPlayer().getLocation();
					 Location killerPlayerLocation = broken.getPlayer().getLocation();
					 Location blockLocation = broken.getLocation();
					 
						 double playerReach = 0;
						 if (broken.getReason().equals(BreakReason.SNOWBALL)) {
							 playerReach = 2000;
						 } else {
						 switch (arena.getSpleefType()) {
						case BOWSPLEEF:
							break;
						case POTSPLEEF:
							break;
						case SPLEEF:
							playerReach = 15; break;
						case SPLEGG:
							playerReach = 50; break;
						case TNTRUN:
							playerReach = 8;break;
						default:
							break;
						 } 
						 }
						 
						 if  (blockLocation.distanceSquared(killerPlayerLocation)<=playerReach*playerReach) {
							 if (block==null) {
								 block = broken;
							 } else {
								 if (blockLocation.distanceSquared(deathPlayerLocation) < block.getLocation().distanceSquared(deathPlayerLocation)) {
									 block = broken;
								 }
							 }							 
						 }	 
					 
			 }
 
			 return block;
		 }

		 
		 
		 
		 public String endGameMessage(String winner, String loser, int winner_points, int loser_points, boolean ranked,int elodif, int elo1, int elo2, boolean tie,Arena arena) {
			 String prefix = getGamePrefix(arena);
			 String string_elo1 = "§7(§e"+elo1+ "§7)";
			 String string_elo2 = "§7(§e"+elo2+ "§7)";
			 String string_pos_elodif = "§a(+"+elodif+"§a)";
			 String string_neg_elodif = "§c(-"+elodif+"§c)";
			 String what_happened = "§b won against ";
			 
			 
			 	if (tie) what_happened= "§b tied against ";
				if (!ranked) {prefix = getGamePrefix(arena); string_elo1 = ""; string_elo2 = ""; string_pos_elodif = "";string_neg_elodif = "";}
				
					return prefix +"§b" +winner +string_elo1 + string_pos_elodif+ what_happened
							+ loser + string_elo2 + string_neg_elodif+ " §7(§e§l" + winner_points + "§7-§e§l" + loser_points+"§7)";			
		 }
		 

		 public void addDuelQueue(SpleefPlayer sp, int teamSize,String map,SpleefType type,boolean ranked,List<RankedTeam> rankedTeams) {
			// PlayerJoinMatchEvent event = new PlayerJoinMatchEvent(sp,arena);
			//	Bukkit.getPluginManager().callEvent(event);
				ranked = true;
			 Party party = PartyManager.getManager().getParty(sp.getPlayer());
			 
				if (party!=null && party.isLeader(sp.getPlayer())) {
					for (UUID u : party.getMembers()) {
						Player player = Bukkit.getPlayer(u);
						SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
						addDuelQueue(splayer,teamSize,map,type,ranked,rankedTeams);	
					}
				}
			 
				//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("addDuelQueue in primary thread");
				sp.leave(false, false);
			
				Arena arena = null;
				for (Arena a : DataManager.getManager().getArenas()) {
					if (a.getState().equals(GameState.LOBBY) && a.getTeamSize()==teamSize && a.getQueue().size()>0 && ranked==a.isRanked()) {			
						arena = a;
						break;
						
					}
				}		
				if (arena!=null) {
					arena.addDuelQueue(sp,teamSize,ranked);
					if (arena.getQueue().size()>=teamSize*2) {
						arena.setQueue(Utils.getUtils().newShuffledSet(arena.getQueue()));
						
						int i=0;
						List<SpleefPlayer> t1 = new ArrayList<SpleefPlayer>();
						List<SpleefPlayer> t2 = new ArrayList<SpleefPlayer>();
						for (SpleefPlayer spp : new ArrayList<>(arena.getQueue())) {
							if (i<arena.getQueue().size()/2) {
								t1.add(spp);
							} else {
								t2.add(spp);
							}
							i++;
						}
						RankedTeam team1 = new RankedTeam(t1);
						RankedTeam team2 = new RankedTeam(t2);
						arena.setRankedTeam1(team1);
						arena.setRankedTeam2(team2);
						Arena a = arena;
						a.startGameDuel(true);
					} 
				} else {
					arena = findArena(map, type,teamSize);
					
					if (arena!=null) {
						arena.addDuelQueue(sp,teamSize,ranked);
						
					} else {
						sp.getPlayer().sendMessage("§cCouldn't find any map to play! Try again later.");
					}
				}
		 }
				
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 

		public void addFFAQueue(SpleefPlayer sp,SpleefType type) {
			FFAArena ffa = GameManager.getManager().getFFAArena(type);
			Arena arena = ffa.getArena();
			//PlayerJoinMatchEvent event = new PlayerJoinMatchEvent(sp,arena);
			//Bukkit.getPluginManager().callEvent(event);
			
			sp.leave(false,false);
			
			Party party = PartyManager.getManager().getParty(sp.getPlayer());
			if (party !=null && party.isLeader(sp.getPlayer())) {
				for (UUID u : party.getMembers()) {
					Player player = Bukkit.getPlayer(u);
					SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
					arena.getQueue().add(splayer);	
				}
			}
			
			
			if (Main.ffa2v2) {
				 if (ffa.getTeamByPlayer(sp.getUUID())==null) {
					 boolean foundTeam = false;
					 for (FFATeam team : ffa.getTeams()) {
						 if (team.getPlayer2()==null) {
							 team.setPlayer2(sp.getUUID());
							 boolean alive = arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.STARTING);
							 team.alivePlayer2(!alive);
							 team.broadcast("§b" + sp.getName() + "§a has joined the team");
							 foundTeam = true;
							 break;
							 
						 }
					 }
					 
					 if (!foundTeam) {
						 FFATeam team = new FFATeam(sp.getUUID(),null);
						 boolean alive = arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.STARTING);
						team.alivePlayer1(!alive);					 
						 ffa.getTeams().add(team);
						 team.broadcast("§aYou have been added to a new team");
					 }
				 }
			}
			
			ffa.getQueue().add(sp);	
			if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.STARTING)) {
				sp.getPlayer().sendMessage(getGamePrefix(arena)+"§aYou have been added to the queue, you will play when the next game starts!");
				SpectateManager.getManager().spectateSpleef(sp, arena);								
				sp.giveQueueItems(false,true,true);	
			} else {
				for (SpleefPlayer p : ffa.getQueue()) {							
					p.getPlayer().sendMessage(getGamePrefix(arena)+"§6" + sp.getName() + " §ahas joined the queue! Total: " + ffa.getQueue().size());						
				}			

				if (type.equals(SpleefType.SPLEEF))  {
					sp.giveQueueItems(true,true,true);					
					if (sp.getHelmet()!=null) {
						sp.getPlayer().getInventory().setHelmet(sp.getHelmet().getItem(sp, true));
						}
					sp.getPlayer().teleport(arena.getMainSpawn());
				} else {				
					SpectateManager.getManager().spectateSpleef(sp, arena);
					sp.giveQueueItems(false,false,true);	
				}
				
				if (ffa.getQueue().size()>=3 && !arena.getState().equals(GameState.STARTING)) {
					new ArenaNewStartTask(arena);
					arena.setState(GameState.STARTING);
					int s = 5;
					if (ffa.isInEvent()) {
						s= 10;
					}
					for (SpleefPlayer p : ffa.getQueue()) {					
						p.getPlayer().sendMessage(getGamePrefix(arena)+"§bThe game is starting in " + s +" seconds.");
					}}}
			
				
						
					sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
					DataManager.getManager().getLobbyPlayers().remove(sp.getUUID());
					DataManager.getManager().getPlayingPlayers().add(sp.getUUID());
					
					if (type.equals(SpleefType.SPLEEF)) {
					for (GameMutation mutation : ffa.getVotingMutations()) {
						mutation.mutationMessage(sp);
					}
					}
					
			
		}
			

		
		public void sendSyncMessage(Collection<SpleefPlayer> s, String string) {
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer ss  : s) {
				ss.sendMessage(string);
  			}
			}
			}.runTask(Main.get());
			
		}

		public void pauseWithCommand(Arena arena) {
			arena.pause();
			
		}
		
		public void redoWithCommand(Arena arena) {
			arena.redo();
			
		}
		
		public void unpauseWithCommand(Arena arena) {
			arena.unpause();
			
		}

		public void loadFFAArenas() {
			this.SpleefFFA = new FFAArena(SpleefType.SPLEEF);
			this.SpleggFFA = new FFAArena(SpleefType.SPLEGG);
			this.TNTRunFFA = new FFAArena(SpleefType.TNTRUN);
			
		}

		
}


