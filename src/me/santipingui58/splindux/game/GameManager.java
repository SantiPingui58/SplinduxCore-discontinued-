package me.santipingui58.splindux.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.ranked.RankedTeam;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.ArenaRequest;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.relationships.guilds.GuildDuel;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.level.LevelManager;
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
	
	private Arena SpleefFFAArena;
	private Arena TNTRunFFAArena;
	private Arena SpleggFFAArena;
	 	
	 public Arena getFFAArena(SpleefType type) {
		 
		 switch(type) {
		 case SPLEEF: return SpleefFFAArena;
		 case TNTRUN: return TNTRunFFAArena;
		 case SPLEGG: return SpleggFFAArena;
		default:break;
		 }
		 return null;
	 }
	 
	 public void setFFAArena(Arena arena,SpleefType type) {
		 switch(type) {
		 case SPLEEF: this.SpleefFFAArena = arena; break;
		 case TNTRUN: this.TNTRunFFAArena = arena; break;
		 case SPLEGG: this.SpleggFFAArena = arena; break;
		default:break;
		 }
		 
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
 
	 
	 
	 

		public Arena duelGame(SpleefPlayer challenger, List<SpleefPlayer> sp2,String ar,SpleefType type,int teamSize,boolean ranked,List<RankedTeam> teams,boolean cantie,boolean guild,int time) {
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("GameManager.DuelGame in primary thread");
			
			challenger.getDuels().remove(challenger.getDuelByDueledPlayer(sp2.get(0)));
			Arena arena = findArena(ar,type,teamSize);
					
			if (arena!=null) {
				 challenger.leave(false,false);
				for (SpleefPlayer sp_2 : sp2) sp_2.leave(false,false);
				
				arena.getQueue().add(challenger);
				arena.getQueue().addAll(sp2);
				arena.setTeamSize(teamSize);
				arena.ranked(ranked);			
				if (ranked) {
				arena.setRankedTeam1(teams.get(0));
				arena.setRankedTeam2(teams.get(1));
				}
				arena.setTimedMaxTime(time);
				arena.setTie(cantie);
				arena.isGuildGame(guild);
				arena.startGame(false);		
			
				
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
		
		
		
		public void resetArenaWithCommand(Arena arena,boolean forceshrink) {
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

			boolean shrink = false;
			if (arena.getShrinkTime()==0 || forceshrink) shrink = true;
			
			arena.resetArena(shrink, true);
			
			boolean b = true;
			if (b) return;
	
			arena.setState(GameState.STARTING);
			if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
				
				//boolean shrink = false;
				boolean crumble = false;
				
				if (arena.getShrinkAmount()>=1) {
					shrink = true;
					arena.setCrumbleAmount(arena.getCrumbleAmount()+1);
					arena.setShrinkAmount(0);	
				}
				
				if (arena.getCrumbleAmount()>=1) {
					arena.setCrumbleAmount(0);		
					arena.setShrinkAmount(arena.getShrinkAmount()+1);
					crumble = true;
				}		
				
			if (arena.getTime() <=  arena.getResetTime()/2) {
				shrink = true;
				arena.setCrumbleAmount(arena.getCrumbleAmount()+1);
			}	else  {
				crumble = true;
				arena.setShrinkAmount(arena.getShrinkAmount()+1);
			}
			
			arena.resetArena(shrink,crumble);
			}		
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
		
	
		
		public void endGameFFA(GameEndReason reason,SpleefType type) {		
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("EndGameFFA in primary thread");
			Arena arena = getFFAArena(type);
			int s = 5;
			if (arena.isInEvent()) {
				
				s = 15;
			}		
			arena.setState(GameState.FINISHING);
			arena.resetTimer();
			arena.getBrokenBlocks().clear();
			
			
			if (reason.equals(GameEndReason.WINNER)) {
						
				SpleefPlayer winner = arena.getFFAPlayers().get(0);
				winner.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
				
			for (FFABet bet : arena.getCurrentFFABet()) {
				if (bet.getBet().equals(winner)) {
					int amount = (int) (bet.getAmount()*arena.getFFAProbabilities().get(winner));
					bet.getOwner().addCoins(amount);
					sendSyncMessage(arena.getViewers(),"§b"+bet.getOwner().getName()+"§ahas won §6" + amount+ " Coins §a for betting for §b§l " + bet.getBet().getOfflinePlayer().getName()+"§a!");
				}
			}
				
				arena.getCurrentFFABet().clear();
				arena.getCurrentFFABet().addAll(arena.getNextFFABet());
				arena.getNextFFABet().clear();
	
				if (arena.isInEvent()) {
				arena.getEvent().addPoint(winner.getUUID(), 15,false);
				}
				
				if (arena.isInEvent()) {
					FFAEvent event = arena.getEvent();
					event.finishRound();	
				}
				
				winner.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
				LevelManager.getManager().addLevel(winner, 2);
				EconomyManager.getManager().addCoins(winner, 15, true,false);
				SpleefPlayer winstreaker = null;
				int wins = 0;
				boolean isover = false;
			winner.getPlayer().playSound(arena.getLobby(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
			if (arena.getWinStreak().isEmpty()) {
				arena.getWinStreak().put(winner, 1);
			} else {
				if (arena.getWinStreak().containsKey(winner)) {
					int w = arena.getWinStreak().get(winner)+1;
					 arena.getWinStreak().put(winner, w);
				} else {
					isover = true;
					Iterator<Entry<SpleefPlayer, Integer>> it = arena.getWinStreak().entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
				         winstreaker = pair.getKey();		  
				        wins = pair.getValue();
				    }
				    arena.getWinStreak().clear();
					 arena.getWinStreak().put(winner, 1);
				}
			}
			
			
			
				DataManager.getManager().broadcast(getGamePrefix(arena)+"§b"+winner.getName()+" §bhas won the game!",true);
				if (arena.getQueue().size()>=3) {
					DataManager.getManager().broadcast(getGamePrefix(arena)+"§bGame is starting in "+ s + " seconds.",true);
			}
				if (isover) {
					if (wins>1) {
						DataManager.getManager().broadcast(getGamePrefix(arena)+"§bWin Streak of " + winstreaker.getName() + " is over! Total wins: " + wins,true);
				}
				}
				
			
			
			
			
			winner.addFFAWin(arena.getSpleefType());
			winner.giveQueueItems(false,true,true);
			
			arena.getFFAPlayers().remove(winner);
			arena.reset(false,false);
			
			} else if (reason.equals(GameEndReason.TIME_OUT)) {
				
				SpleefPlayer winstreaker = null;
				int wins = 0;
				Iterator<Entry<SpleefPlayer, Integer>> it = arena.getWinStreak().entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
			         winstreaker = pair.getKey();		  
			        wins = pair.getValue();
			    }
			    if (arena.isInEvent()) {
			    for (SpleefPlayer alive : arena.getFFAPlayers()) {		   
					arena.getEvent().addPoint(alive.getUUID(), 1,false);
					}	
			    
			    arena.getEvent().finishRound();
			    }

			    
			    DataManager.getManager().broadcast(getGamePrefix(arena)+"§bNobody has won because the game took too long!",true);
					if (arena.getQueue().size()>=3) {
						DataManager.getManager().broadcast(getGamePrefix(arena)+"§bGame is starting in "+ s + " seconds.",true);
					
					}  
					
					if (wins>1) {
						DataManager.getManager().broadcast(getGamePrefix(arena)+"§bWin Streak of " + winstreaker.getName() + " is over! Total wins: " + wins,true);
					}
					
					
				
				arena.getWinStreak().clear();
				
				new BukkitRunnable() {
					public void run() {
				for (SpleefPlayer p : arena.getFFAPlayers()) {
					p.getPlayer().teleport(arena.getLobby());
					p.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
					p.giveQueueItems(false,true,true);						
				}
				}
				}.runTask(Main.get());
				
				arena.getFFAPlayers().clear();
				arena.reset(false,false);
			
			} else if (reason.equals(GameEndReason.LOG_OFF)) {
				
				SpleefPlayer winstreaker = null;
				int wins = 0;
				Iterator<Entry<SpleefPlayer, Integer>> it = arena.getWinStreak().entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
			         winstreaker = pair.getKey();		  
			        wins = pair.getValue();
			    }
			    
			    final int ss = s;
			    final int winss = wins;
			    final SpleefPlayer winstreakers = winstreaker;
			    new BukkitRunnable() {
			    	public void run () {
				for (SpleefPlayer players : DataManager.getManager().getPlayers()) {
					players.getPlayer().sendMessage(getGamePrefix(arena)+"§bNobody has won the game.");
					if (arena.getQueue().size()>=3) {
						players.getPlayer().sendMessage(getGamePrefix(arena)+"§bGame  is starting in "+ ss + " seconds.");
					
					}  
					
					if (winss>1) {
						players.getPlayer().sendMessage(getGamePrefix(arena)+"§bWin Streak of " + winstreakers.getName() + " is over! Total wins: " + winss);
					}
				}
			    }
			    }.runTask(Main.get());
			}
			
			if (arena.isInEvent()) {
				arena.getEvent().getRoundPoints().clear();
			}

		
			if (type.equals(SpleefType.SPLEEF)) {
				checkSpleefFFAArena();
				} else {
					Arena newarena = null;
					DataManager.getManager().getFFAArenas(arena.getSpleefType()).remove(arena);
					if (DataManager.getManager().getFFAArenas(arena.getSpleefType()).size()==0) {
						DataManager.getManager().loadFFAArenasRotate(type,arena.getQueue().size());
					} 
					
					newarena = DataManager.getManager().getFFAArenas(type).get(DataManager.getManager().getFFAArenas(type).size()-1);
				
					
					traspassFFAArena(arena,newarena);
					
				
					this.setFFAArena(newarena, type);
					
					if (newarena.getQueue().size()>=3) {
						new ArenaNewStartTask(newarena);
						newarena.setState(GameState.STARTING);
					} else {
						newarena.setState(GameState.LOBBY);
					}
					
					
				}
			
		
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
		
		public void checkSpleefFFAArena() {
			Arena arena = this.SpleefFFAArena;
			Arena original = arena;
			int max = arena.getMaxPlayersSize();
			int min = arena.getMinPlayersSize();
			if (arena.getQueue().size()>=max) {
				 arena = DataManager.getManager().loadFFAArenaNoRotate(SpleefType.SPLEEF, 1,original);
				 traspassFFAArena(original,arena);
			} else if (arena.getQueue().size()<min) {
				 arena = DataManager.getManager().loadFFAArenaNoRotate(SpleefType.SPLEEF, 2,original);
				 traspassFFAArena(original,arena);
			}
			this.SpleefFFAArena = arena;
			this.SpleefFFAArena.getFFAPlayers().clear();		
			if (this.SpleefFFAArena.getQueue().size()>=3) {
				new ArenaNewStartTask(this.SpleefFFAArena);
				this.SpleefFFAArena.setState(GameState.STARTING);
			} else {
				this.SpleefFFAArena.setState(GameState.LOBBY);
			}
		}
		
		
		
		public void traspassFFAArena(Arena arena1, Arena arena2) {
			if (arena1!=arena2) {
			arena1.getAllMutations().forEach((m) -> arena2.getAllMutations().add(m));
			arena2.setEvent(arena1.getEvent());
			arena2.getQueue().addAll(arena1.getQueue());
			arena2.getFFAPlayers().addAll(arena1.getFFAPlayers());
			arena1.getFFAPlayers().clear();
			arena1.getQueue().clear();
			arena1.getAllMutations().clear();
			arena1.setState(GameState.LOBBY);
			}
		}
		
		public void timeReset1vs1(Arena arena) {
			resetArenaWithCommand(arena,false);
			arena.resetTimer();
		}
		
		
		

		public int getPlayingSize(SpleefType spleeftype, GameType gametype,int teamSize,int ranked) {	
			int i = 0;
			if (gametype.equals(GameType.FFA)) {
				if (spleeftype!=null) {
				Arena arena = GameManager.getManager().getFFAArena(spleeftype);
				return arena.getQueue().size();
				} else {
					for (SpleefType t : SpleefType.values()) {
						Arena arena = GameManager.getManager().getFFAArena(t);
						if (arena!=null) {
							i = i + arena.getQueue().size();
						}
					}
				}
			} else {
				if (spleeftype==null) {
					int a = 0;
					for (SpleefType type : SpleefType.values()) {
						a = a + getPlayingSize(type,gametype,teamSize,ranked);
					}
					return a;
				}
				
				
				for (Arena arena : DataManager.getManager().getArenas()) {
				if (arena.getSpleefType().equals(spleeftype) && arena.getGameType().equals(gametype)) {
					if (ranked!=-1) {
						if (ranked==0 && arena.isRanked()) continue;
						if (ranked==1 && !arena.isRanked()) continue;
					}
				
					if (teamSize!=0 && arena.getTeamSize()!=teamSize) continue;
					int d = arena.getPlayers().size();
					i = i + d;
				}
			
				}
			}

			return i;
		}
		
		public int getQueueSize(SpleefType spleeftype, GameType gametype, int teamSize,int ranked) {
			
			if (spleeftype==null) {
				int a = 0;
				for (SpleefType type : SpleefType.values()) {
					a = a + getQueueSize(type,gametype,teamSize,ranked);
				}
				return a;
			}
			int i = 0;
			if (ranked==1) {
				return RankedManager.getManager().getRankedQueue(spleeftype, teamSize).getQueue().size();
			} else {
			for (Arena arena : DataManager.getManager().getArenas()) {
				if (arena.getGameType().equals(GameType.DUEL)) {
			if (arena.getSpleefType().equals(spleeftype) && arena.getGameType().equals(gametype)) {
				if (ranked!=-1) {
					if (ranked==0 && arena.isRanked()) continue;
					if (ranked==1 && !arena.isRanked()) continue;
				}
				
				if (teamSize!=0 && arena.getTeamSize()!=teamSize) continue;					
				int d = arena.getQueue().size();
				i = i + d;
			}
				}
		}	
		}
			return i;
		}
		
		
		
		
		
		
		public void endGameDuel(Arena arena,String w,GameEndReason reason) {	
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("EndGameDuel in primary thread");
			arena.resetShrinkTime();
			arena.setTie(false);
			arena.setTeamSize(0);
			arena.setShrinkAmount(0);
			arena.resetShrinkTime();
			arena.setCrumbleAmount(0);
			arena.reset(false, true);
			arena.resetResetRound();
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
			List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
			players.addAll(arena.getDuelPlayers1());
			players.addAll(arena.getDuelPlayers2());
			
			for (SpleefPlayer sp : arena.getViewers()) {
				ActionBarAPI.sendActionBar(sp.getPlayer(), "");
			}
					
			players.forEach((p) -> {
				p.setScoreboard(ScoreboardType.LOBBY);
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
			if (reason.equals(GameEndReason.WINNER)) {
			if (w.equalsIgnoreCase("Team1")) {
				winners.addAll(arena.getDuelPlayers1());
				losers.addAll(arena.getDuelPlayers2());
			} else {
				winners.addAll(arena.getDuelPlayers2());
				losers.addAll(arena.getDuelPlayers1());
				
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
				} else {
					winners.addAll(arena.getDuelPlayers2());
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
				
			
			int elo = 0;
			
			if (arena.isRanked()) {
				if (!reason.equals(GameEndReason.ENDGAME)) {
				elo = RankedManager.getManager().calcualteELO(winners, losers, arena.getTeamSize());
				if (arena.getRankedTeam1().getPlayers().equals(winners)) {
				arena.getRankedTeam1().newELO(elo);
				arena.getRankedTeam2().newELO(-elo);
				} else {
					arena.getRankedTeam1().newELO(-elo);
					arena.getRankedTeam2().newELO(elo);
				}
			}
			}
			
			if (reason.equals(GameEndReason.WINNER)) {	
				for (SpleefPlayer loser : losers) LevelManager.getManager().addLevel(loser, 1);
				for (SpleefPlayer winner : winners) LevelManager.getManager().addLevel(winner, 3);
				for (SpleefPlayer winner : winners)EconomyManager.getManager().addCoins(winner, 20, true,false);
				if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) for (SpleefPlayer winner : winners) 			winner.getPlayerStats().setDuelWins(arena.getSpleefType(), winner.getPlayerStats().getDuelWins(arena.getSpleefType())+1);;
			
			
			String p1 = arena.getTeamName(1);
			String p2 = arena.getTeamName(2);
					if (winners.equals(arena.getDuelPlayers1())) {					
						if (arena.isRanked()) {
							DataManager.getManager().broadcast(endGameMessage(p1,p2,arena.getPoints1(),arena.getPoints2(),true,elo,arena.getRankedTeam1().getELO(),arena.getRankedTeam2().getELO(),false, arena),true);
						} else {
							DataManager.getManager().broadcast(endGameMessage(p1,p2,arena.getPoints1(),arena.getPoints2(),false,0,0,0,false,arena),true);
						}											
				} else {					
					if (arena.isRanked()) {
						DataManager.getManager().broadcast(endGameMessage(p2,p1,arena.getPoints2(),arena.getPoints1(),true,elo,arena.getRankedTeam2().getELO(),arena.getRankedTeam1().getELO(),false,arena),true);
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
							online.getPlayer().sendMessage(endGameMessage(p1,arena.getDisconnectedPlayer().getOfflinePlayer().getName(),arena.getPoints1(),arena.getPoints2(),true,elo,arena.getRankedTeam1().getELO(),arena.getRankedTeam2().getELO(),false,arena));
							} else {
								online.getPlayer().sendMessage(endGameMessage(p1,arena.getDisconnectedPlayer().getOfflinePlayer().getName(),arena.getPoints1(),arena.getPoints2(),false,0,0,0,false,arena));
							}											
					} else {					
						if (arena.isRanked()) {
							online.getPlayer().sendMessage(endGameMessage(p2,arena.getDisconnectedPlayer().getOfflinePlayer().getName(),arena.getPoints2(),arena.getPoints1(),true,elo,arena.getRankedTeam2().getELO(),arena.getRankedTeam1().getELO(),false,arena));
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
		
		
		
		public void removeBrokenBlocksAtDead(SpleefPlayer sp,Arena arena) {
			
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
			if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Fell in primary thread");
			
			Arena arena = sp.getArena();
			if (!arena.getState().equals(GameState.GAME)) return;			

			if (arena.getGameType().equals(GameType.FFA)) {
				removeBrokenBlocksAtDead(sp,arena);
				sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
				arena.getFFAPlayers().remove(sp);
				if (arena.isInEvent()) {
					for (SpleefPlayer players : arena.getFFAPlayers()) {
							arena.getEvent().addPoint(players.getUUID(), 1,false);
							}
					
					}
				SpectateManager.getManager().spectateSpleef(sp, arena);
				
				DeathReason reason = (new ArrayList<DeathReason>(r.keySet())).get(0);	
				SpleefPlayer killer = (new ArrayList<SpleefPlayer>(r.values())).get(0);	
				
				 if (killer!=null) {
					 new BukkitRunnable() {
						 public void run() {
				    	killer.getPlayer().playSound(killer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.75F);
					 }
					 }.runTask(Main.get());
					 
				    	if (!killer.equals(sp)) {
				    		killer.addFFAKill(arena.getSpleefType());
				    		if (arena.isInEvent()) {
				    			arena.getEvent().addPoint(killer.getUUID(), 7,false);
				    			try {
				    			if (arena.getEvent().getLastWinner().equals(sp.getOfflinePlayer())) arena.getEvent().addPoint(killer.getUUID(), 8, false);
				    			} catch(Exception ex) {}
				    		}
				    	}
				    }
				 
				 if (arena.getFFAPlayers().size()==1) {		
						endGameFFA(GameEndReason.WINNER,arena.getSpleefType());
					}
				 
				
				new BukkitRunnable() {
				  public void run() {
					  Player p = sp.getPlayer();
		p.playSound(arena.getLobby(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 0.9F);
			sp.giveQueueItems(false,arena.getSpleefType().equals(SpleefType.SPLEEF),true);
			if (sp.getHelmet()!=null) {
				p.getInventory().setHelmet(sp.getHelmet().getItem(sp, true));
				}
			if (arena.getFFAPlayers().size()>0)
			for (SpleefPlayer players : arena.getViewers()) {
				players.getPlayer().sendMessage(getGamePrefix(arena)+reason.getDeathMessage(killer, sp, arena));
			}
			
				}
				}.runTask(Main.get());
			
	

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
			
						GameManager.getManager().resetArenaWithCommand(arena,false);
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
					
					new BukkitRunnable() {
						public void run() {
					for (SpleefPlayer players : arena.getViewers()) { 
						players.getPlayer().sendMessage("§6"+ sp.getName()+ "§b fell!");							
						}				
					}
					}.runTask(Main.get());
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
				BrokenBlock broken = getNearestBlockIn(sp,3,arena);
				if (broken!=null) {
					if (broken.getPlayer().equals(sp)) {
						hashmap.put(DeathReason.THEMSELF, null);
						return hashmap;
					} else {
						if (broken.getReason().equals(BreakReason.SHOVEL)) {
					hashmap.put(DeathReason.SPLEEFED, broken.getPlayer());
					return hashmap;
					} else {
						hashmap.put(DeathReason.SNOWBALLED, broken.getPlayer());
						return hashmap;
					}
					}
				
				} else {	
				BrokenBlock broken2 = getNearestBlockIn(sp,6,arena);
				if (broken2!=null) {
					hashmap.put(DeathReason.PLAYING_FAILED, broken2.getPlayer());
					return hashmap;
				}
			
				}
				
				
			hashmap.put(DeathReason.PK_FAILED, null);
			return hashmap;
			
		
		}
		

		 public BrokenBlock getNearestBlockIn(SpleefPlayer sp, int i,Arena arena) {
			 BrokenBlock block = null;
			 for (BrokenBlock broken : arena.getBrokenBlocks()) {
				 if (broken.isAlive()) {
				 if (block==null) {
					 block = broken;
				 }
				 try {
					 Location spLocation = sp.getPlayer().getLocation();
				 if (broken.getLocation().distanceSquared(spLocation) <i*i) {
					 if (broken.getLocation().distanceSquared(spLocation) < block.getLocation().distanceSquared(spLocation)) {
						 if (broken.getPlayer().equals(sp)) {
							 if (broken.getLocation().distanceSquared(sp.getPlayer().getLocation())<7*7) {
								 if (broken.getPlayer().getOfflinePlayer().isOnline()) {
									 if (broken.getPlayer().getPlayer().getWorld().getName().equalsIgnoreCase(sp.getPlayer().getWorld().getName())) {
										 if (broken.getPlayer().getPlayer().getLocation().distanceSquared(sp.getPlayer().getLocation()) <10*10) {
											 block = broken;
										 }
									 }
							
								 }
							 }
						 } else {
						 block = broken;
						 }
					 }
				 }
			 } catch(Exception ex) {}
			 }
			 }
			 return block;
		 }

		 
		 
		 
		 public String endGameMessage(String winner, String loser, int winner_points, int loser_points, boolean ranked,int elodif, int elo1, int elo2, boolean tie,Arena arena) {
			 String prefix = " §6[Ranked] " + getGamePrefix(arena);
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
			 
				if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("addDuelQueue in primary thread");
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
						Collections.shuffle(arena.getQueue());
						arena.startGame(true);
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
			sp.leave(false,false);
			
			
			
			Arena arena = GameManager.getManager().getFFAArena(type);
			arena.getQueue().add(sp);	
			if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.STARTING)) {
				sp.getPlayer().sendMessage(getGamePrefix(arena)+"§aYou have been added to the queue, you will play when the next game starts!");
				SpectateManager.getManager().spectateSpleef(sp, arena);								
				sp.giveQueueItems(false,true,true);	
			} else {
				for (SpleefPlayer p : arena.getQueue()) {							
					p.getPlayer().sendMessage(getGamePrefix(arena)+"§6" + sp.getName() + " §ahas joined the queue! Total: " + arena.getQueue().size());						
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
				
				if (arena.getQueue().size()>=3 && !arena.getState().equals(GameState.STARTING)) {
					new ArenaNewStartTask(arena);
					arena.setState(GameState.STARTING);
					int s = 5;
					if (arena.isInEvent()) {
						s= 10;
					}
					for (SpleefPlayer p : arena.getQueue()) {					
						p.getPlayer().sendMessage(getGamePrefix(arena)+"§bThe game is starting in " + s +" seconds.");
					}}}
			
				
						
					sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
					DataManager.getManager().getLobbyPlayers().remove(sp.getUUID());
					DataManager.getManager().getPlayingPlayers().add(sp.getUUID());
					
					if (type.equals(SpleefType.SPLEEF)) {
					for (GameMutation mutation : arena.getVotingMutations()) {
						mutation.mutationMessage(sp);
					}
					}
					
			
		}
			

		
		public void sendSyncMessage(List<SpleefPlayer> s, String string) {
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer ss  : s) {
				ss.sendMessage(string);
  			}
			}
			}.runTask(Main.get());
			
		}

		
		
}


