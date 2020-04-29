package me.santipingui58.splindux.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import me.jumper251.replay.api.ReplayAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.death.BreakReason;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Request;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.task.ArenaNewStartTask;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GameManager {

	private static GameManager manager;	
	 public static GameManager getManager() {
	        if (manager == null)
	        	manager = new GameManager();
	        return manager;
	    }
	
	 
	 public int getAvailableArenasFor(String s) {
		 int i = 0;
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {

			 if (!arena.getPlayers().isEmpty() && arena.getGameType().equals(GameType.DUEL)) {
			 if (Utils.getUtils().containsIgnoreCase(arena.getName(), s)) {
				 i++;
			 }
		 }
		 }
		 return i;
	 }
 
	 public void spectate(SpleefPlayer sp1, SpleefPlayer sp2) {
		 sp1.leave(false);
		 sp1.getPlayer().setGameMode(GameMode.SPECTATOR);
		 sp1.getPlayer().teleport(sp2.getPlayer());		 
		 sp1.setScoreboard(sp2.getScoreboard());		 
		 sp1.setSpectate(sp2);
	 }
	 	
		public void duelGame(SpleefPlayer challenger, List<SpleefPlayer> sp2,String ar,SpleefType type,int teamSize) {
			
				challenger.getDuels().remove(challenger.getDuelByDueledPlayer(sp2.get(0)));
			
			SpleefArena arena = findArena(ar,type);
					
			if (arena!=null) {
				 challenger.leave(false);
				for (SpleefPlayer sp_2 : sp2) sp_2.leave(false);
				
				arena.getQueue().add(challenger);
				arena.getQueue().addAll(sp2);
				arena.setTeamSize(teamSize);
			arena.startGame();			
			} else {
				challenger.getPlayer().sendMessage("§cCouldn't find a map to play! Duel cancelled.");
				for (SpleefPlayer sp_2 : sp2)sp_2.getPlayer().sendMessage("§cCouldn't find a map to play! Duel cancelled.");
				return;
			}
					
			
			
			
			for (SpleefPlayer sp : arena.getPlayers()) {
			if (sp.getPlayer().isOp()) {
				TextComponent message = new TextComponent("§2Would you like to record this game? [CLICK HERE]");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/hover record"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Record this game §a").create()));
					sp.getPlayer().spigot().sendMessage(message);
			}
			}
			
			arena.doRecordingRequest();
			new BukkitRunnable() {
				public void run() {
					arena.cancelRecordingRequest();
				}
			}.runTaskLater(Main.get(),20L*10);
		}
		
		public SpleefArena findArena(String ar,SpleefType type) {
			if (ar==null) {	
				return findAnyPlayableArena(type);
		
			} else {
				 return findPlayableArenaBy(ar,type);		
				}	
		}
		
		
		public SpleefArena findPlayableArenaBy(String s,SpleefType type) {
			List<SpleefArena> arenas = DataManager.getManager().getArenas();
			Collections.shuffle(arenas);
			for (SpleefArena a : arenas) {
				if (a.getGameType().equals(GameType.DUEL) && a.getSpleefType().equals(type)) {
				if (a.getDuelPlayers1().isEmpty() && a.getDuelPlayers2().isEmpty() && a.getQueue().isEmpty()) {
					if (Utils.getUtils().containsIgnoreCase(a.getName(), s))
					return a;
				}
				} 
			}
			return null;
		}
		
		public SpleefArena findAnyPlayableArena(SpleefType type) {
			List<SpleefArena> arenas = DataManager.getManager().getArenas();
			Collections.shuffle(arenas);
			for (SpleefArena a : arenas) {
				if (a.getGameType().equals(GameType.DUEL) && a.getSpleefType().equals(type)) {
				if (a.getPlayers().isEmpty() && a.getQueue().isEmpty()) {
					return a;
				}
				} 
			}
			return null;
		}


		public List<SpleefPlayer> leftPlayersToSomething(List<SpleefPlayer> something,SpleefArena arena,boolean canBeDead) {
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
		
		
		
		public void resetArenaWithCommand(SpleefArena arena) {
			for (SpleefPlayer players : arena.getViewers()) {
				players.getPlayer().sendMessage("§6The arena has been reset.");						
		}
			arena.setState(GameState.STARTING);
			if (arena.getSpleefType().equals(SpleefType.SPLEEF))
		arena.shrink();
		}
		
		public void playToWithCommand(SpleefArena g,int crumble) {
			g.setPlayTo(crumble);
			g.setPlayToRequest(null);
			
			for (SpleefPlayer p2 : g.getViewers()) {
					p2.getPlayer().sendMessage("§6The arena playto has been set to: §6" + crumble);
			}
		}
		
		public void crumbleWithCommand(SpleefArena g,int crumble) {
			g.crumbleArena(crumble);
			g.setCrumbleRequest(null);	
			for (SpleefPlayer p2 : g.getViewers()) {
					p2.getPlayer().sendMessage("§6The arena has been crumbled with a percentage: §a" + crumble);
				
			}			
		}
		
	
		public void addDuelQueue(SpleefPlayer sp, int teamSize,String map,SpleefType type) {
			SpleefArena arena = null;
			for (SpleefArena a : DataManager.getManager().getArenas()) {
				if (a.getState().equals(GameState.LOBBY) && a.getTeamSize()==teamSize) {
					if (a.getQueue().size()+1>=teamSize*2) {
						arena = a;
						break;
					}
				}
			}		
			if (arena!=null) {
				arena.addDuelQueue(sp,teamSize);
				arena.startGame();
			} else {
				arena = findArena(map, type);
				
				if (arena!=null) {
					arena.addDuelQueue(sp,teamSize);
					
				} else {
					sp.getPlayer().sendMessage("§cCouldn't find any map to play! Try again later.");
				}
			}
		}
		
		
		
		public void endGameFFA(SpleefArena arena,GameEndReason reason) {			
			int s = 5;
			if (arena.isInEvent()) {
				
				s = 15;
			}		
			arena.setState(GameState.FINISHING);
			arena.updateMutations();
			arena.resetTimer();
			arena.getBrokenBlocks().clear();
			if (reason.equals(GameEndReason.WINNER)) {
						
				SpleefPlayer winner = arena.getFFAPlayers().get(0);
				if (arena.isInEvent()) {
				arena.getEvent().addPoint(winner, 7,false);
				}
				if (arena.isInEvent()) {
					FFAEvent event = arena.getEvent();
					event.finishRound();	
				}
				
				winner.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
				LevelManager.getManager().addLevel(winner, 2);
				EconomyManager.getManager().addCoins(winner, 15, true);
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
			
			for (SpleefPlayer players : DataManager.getManager().getOnlinePlayers()) {
				players.getPlayer().sendMessage("§b"+winner.getPlayer().getName()+" §bhas won Spleef!");
				if (arena.getQueue().size()>=3) {
				players.getPlayer().sendMessage("§bSpleef is starting in "+ s + " seconds.");
			}
				if (isover) {
					if (wins>1) {
					players.getPlayer().sendMessage("§bWin Streak of " + winstreaker.getOfflinePlayer().getName() + " is over! Total wins: " + wins);
				}
				}
				
			}
			
			winner.addFFAWin();
			winner.getPlayer().teleport(arena.getLobby());
			winner.giveQueueItems();
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
					arena.getEvent().addPoint(alive, 1,false);
					}	
			    
			    arena.getEvent().finishRound();
			    }
				for (SpleefPlayer players : DataManager.getManager().getOnlinePlayers()) {
					players.getPlayer().sendMessage("§bNobody has won because the game took too long!");
					if (arena.getQueue().size()>=3) {
						players.getPlayer().sendMessage("§bSpleef is starting in "+ s + " seconds.");
					
					}  
					
					if (wins>1) {
						players.getPlayer().sendMessage("§bWin Streak of " + winstreaker.getPlayer().getName() + " is over! Total wins: " + wins);
					}
					
					
				}
				arena.getWinStreak().clear();
				
				
				for (SpleefPlayer p : arena.getFFAPlayers()) {
					p.getPlayer().teleport(arena.getLobby());
					p.giveLobbyItems();
				}
			
			} else if (reason.equals(GameEndReason.LOG_OFF)) {
				
				SpleefPlayer winstreaker = null;
				int wins = 0;
				Iterator<Entry<SpleefPlayer, Integer>> it = arena.getWinStreak().entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
			         winstreaker = pair.getKey();		  
			        wins = pair.getValue();
			    }
				for (SpleefPlayer players : DataManager.getManager().getOnlinePlayers()) {
					players.getPlayer().sendMessage("§bNobody has won Spleef.");
					if (arena.getQueue().size()>=3) {
						players.getPlayer().sendMessage("§bSpleef is starting in "+ s + " seconds.");
					
					}  
					
					if (wins>1) {
						players.getPlayer().sendMessage("§bWin Streak of " + winstreaker.getPlayer().getName() + " is over! Total wins: " + wins);
					}
				}
			}
			
			if (arena.isInEvent()) {
				arena.getEvent().getRoundPoints().clear();
			}

			
			arena.getFFAPlayers().clear();
			
			if (arena.getQueue().size()>=3) {
				new ArenaNewStartTask(arena);
				arena.setState(GameState.STARTING);
			} else {
				arena.setState(GameState.LOBBY);
			}
		}
		
		public void timeReset1vs1(SpleefArena arena) {
			arena.setState(GameState.STARTING);
			arena.shrink();
			arena.resetTimer();
		}
		
		public int getPlayingSize(SpleefType spleeftype, GameType gametype, int player_size) {
			int i = 0;
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (gametype.equals(GameType.FFA)) {
					if (arena.getSpleefType().equals(spleeftype) && arena.getGameType().equals(gametype)) {
					List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
					for (SpleefPlayer sp : arena.getFFAPlayers()) {
						if (!list.contains(sp)) {
							list.add(sp);
						}
					}
					
					for (SpleefPlayer sp : arena.getQueue()) {
						if (!list.contains(sp)) {
							list.add(sp);
						}
					}
					return list.size();
					}
			} else {
				
				if (arena.getSpleefType().equals(spleeftype) && arena.getGameType().equals(gametype) && arena.getTeamSize()==player_size) {
					i = i + arena.getPlayers().size();
				}
			}
			}
			return i;
		}
		
		public int getQueueSize(SpleefType spleeftype, GameType gametype, int player_size) {
			int i = 0;
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getGameType().equals(GameType.DUEL)) {
			if (arena.getSpleefType().equals(spleeftype) && arena.getGameType().equals(gametype) && arena.getTeamSize()==player_size) {
				i = i+arena.getQueue().size();
			}
		}	
		}
			return i;
		}
		
		public void endGameDuel(SpleefArena arena,String w,GameEndReason reason) {	
			arena.setTeamSize(0);
			arena.reset(false, true);
			arena.resetResetRound();
			arena.resetTotalTime();
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
			arena.resetPlayTo();
			List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
			players.addAll(arena.getDuelPlayers1());
			players.addAll(arena.getDuelPlayers2());
			for (SpleefPlayer sp : players) {
				sp.setScoreboard(ScoreboardType.LOBBY);
				for (SpleefPlayer spect : sp.getSpectators()) {
					spect.leaveSpectate(true);
				}
				sp.getSpectators().clear();
			}
			
			arena.resetTimer();
			if (reason.equals(GameEndReason.WINNER)) {
				List<SpleefPlayer> winners = new ArrayList<SpleefPlayer>();
				List<SpleefPlayer> losers = new ArrayList<SpleefPlayer>();
				if (w.equalsIgnoreCase("Team1")) {
					winners.addAll(arena.getDuelPlayers1());
					losers.addAll(arena.getDuelPlayers2());
				} else {
					winners.addAll(arena.getDuelPlayers2());
					losers.addAll(arena.getDuelPlayers1());
				}
				for (SpleefPlayer loser : losers) LevelManager.getManager().addLevel(loser, 1);
				for (SpleefPlayer winner : winners) LevelManager.getManager().addLevel(winner, 3);
				for (SpleefPlayer winner : winners)EconomyManager.getManager().addCoins(winner, 20, true);
				if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) for (SpleefPlayer winner : winners) winner.add1vs1Wins();
			arena.setState(GameState.FINISHING);
			
			String p1 = arena.getTeamName(1);
			String p2 = arena.getTeamName(2);
				for (SpleefPlayer online : DataManager.getManager().getOnlinePlayers()) {
					if (winners.equals(arena.getDuelPlayers1())) {
					online.getPlayer().sendMessage("§b" +p1 +" won against " + p2 +" §7(§a" + arena.getPoints1() + "§7-§a" + arena.getPoints2()+"§7)");
				} else {
					online.getPlayer().sendMessage("§b" +p2 +" won against " + p1 +" §7(§a" + arena.getPoints2() + "§7-§a" + arena.getPoints1()+"§7)");
				}
				}
				
			
			
				for (SpleefPlayer winner : winners) winner.getPlayer().teleport(Main.lobby);
				for (SpleefPlayer loser : losers) loser.getPlayer().teleport(Main.lobby);
				for (SpleefPlayer winner : winners)winner.giveLobbyItems();
			for (SpleefPlayer loser : losers)loser.giveLobbyItems();
		
			} else if (reason.equals(GameEndReason.TIME_OUT)) {
				for (SpleefPlayer online : arena.getViewers()) {
					online.getPlayer().sendMessage("§bThe 1vs1 between " + arena.getTeamName(1)+ " §band" + arena.getTeamName(2)  + "§bfinished in a draw!");
				}
			} else if (reason.equals(GameEndReason.LOG_OFF)) {
				List<SpleefPlayer> winners = new ArrayList<SpleefPlayer>();
				if (w.equalsIgnoreCase("Team1")) {
					winners.addAll(arena.getDuelPlayers1());
				} else {
					winners.addAll(arena.getDuelPlayers2());
				}
				 for (SpleefPlayer winner : winners) winner.getPlayer().teleport(Main.lobby);
				 for (SpleefPlayer winner : winners) winner.getPlayer().teleport(Main.lobby); 
				 for (SpleefPlayer winner : winners)winner.giveLobbyItems();
				 for (SpleefPlayer online : DataManager.getManager().getOnlinePlayers()) {
					 int i = 0;
					 if (winners==arena.getDuelPlayers1()) {
						 i = 1;
					 } else i = 2;
						online.getPlayer().sendMessage("§b" +arena.getTeamName(i) +" won the game!");
					}
			
			} else if (reason.equals(GameEndReason.ENDGAME)) {
				for (SpleefPlayer sp : arena.getPlayers()) {
					sp.getPlayer().teleport(Main.lobby);
					sp.giveLobbyItems();
				}
			}
			arena.getDuelPlayers1().clear();
			arena.getDuelPlayers2().clear();
			
			if (arena.getQueue().size()>=2) {
				new ArenaNewStartTask(arena);
				arena.setState(GameState.STARTING);
			} else {
				arena.setState(GameState.LOBBY);
			}
			
			if (arena.isRecording()) {
			
				ReplayAPI.getInstance().stopReplay(arena.getReplay().getName(), true);
				arena.setReplay(null);
			}
			
			arena.resetRecord();
			arena.setPoints1(0);
			arena.setPoints2(0);
		}
		
		
		
		public void removeBrokenBlocksAtDead(SpleefPlayer sp,SpleefArena arena) {
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
		
		public void fell(SpleefPlayer sp,LinkedHashMap <DeathReason,SpleefPlayer> r) {		
			SpleefArena arena = sp.getArena();
						
			List<SpleefPlayer> resetRequest = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
			if (resetRequest.contains(sp)) {
				if (resetRequest.size()<=1) {
					GameManager.getManager().resetArenaWithCommand(arena);
				} else {
					arena.getResetRequest().remove(sp);
				}
			}
			
			if (arena.getCrumbleRequest()!=null) {
				Request request = arena.getCrumbleRequest();
				if (request.getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
					GameManager.getManager().crumbleWithCommand(arena, request.getAmount());
				} 
			}
			
			
			
			removeBrokenBlocksAtDead(sp,arena);
			if (arena.getGameType().equals(GameType.FFA)) {
				sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
			//if (sp!=killer) {
			//	killer.addFFAKill();
			//}
			sp.getPlayer().playSound(arena.getLobby(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 0.9F);
		//	killer.getPlayer().playSound(killer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.75F);
			arena.getFFAPlayers().remove(sp);
			if (arena.isInEvent()) {
			for (SpleefPlayer players : arena.getFFAPlayers()) {
					arena.getEvent().addPoint(players, 1,false);
					}
			}
			
			sp.getPlayer().teleport(arena.getLobby());
			sp.giveQueueItems();
			
			DeathReason reason = (new ArrayList<DeathReason>(r.keySet())).get(0);	
			SpleefPlayer killer = (new ArrayList<SpleefPlayer>(r.values())).get(0);	
			
		
			
		//	Iterator<Entry<DeathReason, SpleefPlayer>> it = r.entrySet().iterator();
		//    while (it.hasNext()) {
		//        Map.Entry<DeathReason,SpleefPlayer> pair = (Map.Entry<DeathReason,SpleefPlayer>)it.next();
		  //      reason = pair.getKey();
		  //      if (pair.getValue()!=null) {
		  //      killer = pair.getValue();
		 //       }
		 //   }
			
		    if (killer!=null) {
		    	if (!killer.equals(sp)) {
		    		killer.addFFAKill();
		    		if (arena.isInEvent()) {
		    			arena.getEvent().addPoint(sp, 3,false);
		    		}
		    	}
		    }
		    
			for (SpleefPlayer players : arena.getViewers()) {
				players.getPlayer().sendMessage(reason.getDeathMessage(killer, sp, arena));
			}
					
				
			if (arena.getFFAPlayers().size()<=1) {		
				endGameFFA(arena,GameEndReason.WINNER);
			}
			
		
			} else if (arena.getGameType().equals(GameType.DUEL)) {
				
				
				if (arena.getDuelPlayers1().contains(sp) && !arena.getDeadPlayers1().contains(sp)) {
					arena.getDeadPlayers1().add(sp);
					if (arena.getDeadPlayers1().size()<arena.getDuelPlayers1().size()) {
						for (SpleefPlayer players : arena.getViewers()) { 
							players.getPlayer().sendMessage("§6"+ sp.getPlayer().getName()+ "§b fell!");							
							}
						sp.getPlayer().setGameMode(GameMode.SPECTATOR);
						return;
					}
				} else if (arena.getDuelPlayers2().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
					arena.getDeadPlayers2().add(sp);
					if (arena.getDeadPlayers2().size()<arena.getDuelPlayers2().size()) {
						for (SpleefPlayer players : arena.getViewers()) { 
							players.getPlayer().sendMessage("§6"+ sp.getPlayer().getName()+ "§b fell!");							
							}
						sp.getPlayer().setGameMode(GameMode.SPECTATOR);
						return;
					}
				} 
						
				arena.point(sp);
				
		
			}
		}
		
		
	
		

		public SpleefArena getArenaByName(String s) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getName().equalsIgnoreCase(s)) {
					return arena;
				}
			}
			return null;
		}
		
		
		public LinkedHashMap<DeathReason,SpleefPlayer> getDeathReason(SpleefPlayer sp) {
			LinkedHashMap<DeathReason,SpleefPlayer> hashmap = new LinkedHashMap <DeathReason,SpleefPlayer>();
			SpleefArena arena = sp.getArena();
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
		

		 public BrokenBlock getNearestBlockIn(SpleefPlayer sp, int i,SpleefArena arena) {
			 BrokenBlock block = null;
			 for (BrokenBlock broken : arena.getBrokenBlocks()) {
				 if (broken.isAlive()) {
				 if (block==null) {
					 block = broken;
				 }
				 try {
				 if (broken.getLocation().distance(sp.getPlayer().getLocation()) <i) {
					 if (broken.getLocation().distance(sp.getPlayer().getLocation()) < block.getLocation().distance(sp.getPlayer().getLocation())) {
						 if (broken.getPlayer().equals(sp)) {
							 if (broken.getLocation().distance(sp.getPlayer().getLocation())<2) {
								 block = broken;
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
}


