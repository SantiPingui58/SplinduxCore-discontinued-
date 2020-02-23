package me.santipingui58.splindux.game;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.death.SpleefKill;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.task.ArenaNewStartTask;
import me.santipingui58.splindux.task.ArenaStartingCountdownTask;
import me.santipingui58.splindux.utils.Utils;

public class GameManager {

	private static GameManager manager;	
	 public static GameManager getManager() {
	        if (manager == null)
	        	manager = new GameManager();
	        return manager;
	    }
	
	 
	 
	 public boolean isInGame(SpleefPlayer sp) {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
			 if (arena.getPlayers().contains(sp)) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public boolean isInQueue(SpleefPlayer sp) {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
			 if (arena.getQueue().contains(sp)) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public SpleefArena getArenaByPlayer(SpleefPlayer sp) {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
			 if (arena.getPlayers().contains(sp) || arena.getQueue().contains(sp)) {
				 return arena;
			 }
		 }
		 return null;
	 }
	 
	 
	 public void leaveSpectate(SpleefPlayer sp) {
		 sp.setSpectate(null);
		 sp.setScoreboard(ScoreboardType.LOBBY);
		 sp.getPlayer().teleport(Main.lobby);
	 }
	 
	 public void spectate(SpleefPlayer sp1, SpleefPlayer sp2) {
		 GameManager.getManager().leaveSpectate(sp1);
		 sp1.getPlayer().setGameMode(GameMode.SPECTATOR);
		 sp1.getPlayer().teleport(sp2.getPlayer());		 
		 sp1.setScoreboard(sp2.getScoreboard());		 
		 sp1.setSpectate(sp2);
	 }
	 
	 public void addQueue(SpleefPlayer sp,SpleefArena arena) {
			arena.getQueue().add(sp);
			if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
				
				sp.getPlayer().teleport(arena.getLobby());
			sp.setScoreboard(ScoreboardType.FFAGAME);
			if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.STARTING)) {
				sp.getPlayer().sendMessage("§aYou have been added to the queue, you will play when the next game starts!");
			} else {
				for (SpleefPlayer p : arena.getQueue()) {
					p.getPlayer().sendMessage("§6" + sp.getPlayer().getName() + " §ahas joined the queue! Total: " + arena.getQueue().size());
				}
				
				if (arena.getQueue().size()>=3) {
					new ArenaNewStartTask(arena);
					arena.setState(GameState.STARTING);
					for (SpleefPlayer p : arena.getQueue()) {
						p.getPlayer().sendMessage("§bSpleef is starting in 5 seconds.");
					}
				
				
			}
			}
			
			} else if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
				sp.setScoreboard(ScoreboardType._1VS1GAME);
				if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.FINISHING) || arena.getState().equals(GameState.STARTING)) {
					sp.getPlayer().sendMessage("§aYou have been added to the queue, you will play when this game is finished!");
				} else {
					if (arena.getQueue().size()>=2) {
						new ArenaNewStartTask(arena);
						for (SpleefPlayer p : DataManager.getManager().getOnlinePlayers()) {
							String player1 = arena.getQueue().get(0).getPlayer().getName();
							String player2 = arena.getQueue().get(1).getPlayer().getName();
							p.getPlayer()
							.sendMessage("§bA new 1vs1 game starting between §a"+player1+" §band §a"+ player2+"§b!");
						}
					} else {
						sp.getPlayer().sendMessage("§aYou have been added to the queue.");
					}
				}
			}
			
			DataManager.getManager().giveQueueItems(sp);
			
		
		}
	 
	 
	 
		
		public void leaveQueue(SpleefPlayer sp, SpleefArena arena) {
			GameManager.getManager().leaveSpectate(sp);
			DataManager.getManager().giveLobbyItems(sp);		
			if (sp.getPlayer().isOnline()) {
				if (Main.arenas.getConfig().contains("mainlobby")) {
					sp.getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}				
			sp.getPlayer().sendMessage("§aYou have left the queue.");		
			sp.setScoreboard(ScoreboardType.LOBBY);
			}
			if (arena.getPlayers().contains(sp)) {
				arena.getPlayers().remove(sp);
				sp.getPlayer().teleport(arena.getLobby());
				if (arena.getPlayers().size()<=1) {
					if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
					GameManager.getManager().endGameFFA(arena, GameEndReason.WINNER);
				} else if (arena.getType().equals(SpleefType.SPLEEF1VS1)){
					GameManager.getManager().endGame1vs1(arena,null,null ,GameEndReason.LOG_OFF);
				}
				}
			}		
				arena.getQueue().remove(sp);
				
		}
		
		public void _1vs1(SpleefPlayer sp1, SpleefPlayer sp2,SpleefArena arena) {
			sp2.getDueledPlayers().remove(sp1);
			sp1.getDueledPlayers().remove(sp2);
			if (arena==null) {
				
				List<SpleefArena> arenas = DataManager.getManager().getArenas();
				Collections.shuffle(arenas);
				for (SpleefArena a : arenas) {
					if (a.getType().equals(SpleefType.SPLEEF1VS1)) {
					if (a.getPlayers().isEmpty() && a.getQueue().isEmpty()) {
						arena = a;
						break;
					}
					} 
				}
				
				if (arena==null) {
				sp1.getPlayer().sendMessage("§cCouldn't find a map to play! Duel cancelled.");
				sp2.getPlayer().sendMessage("§cCouldn't find a map to play! Duel cancelled.");
				return;
				} else {				 
				arena.getQueue().add(sp1);
				arena.getQueue().add(sp2);
				startGame(arena);
				}
			}
			
		}
		
		
		public void startGame(SpleefArena arena) {
			
			arena.setState(GameState.STARTING);
			
			arena.resetTimer();
			resetArena(arena);
			if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
			Location center = new Location(arena.getMainSpawn().getWorld(),arena.getMainSpawn().getX(),arena.getMainSpawn().getY()+1,arena.getMainSpawn().getZ());
			Location center_player = new Location(center.getWorld(),center.getX(),center.getY()+1,center.getZ());
			
			List<Location> locations = Utils.getUtils().getCircle(center,16, arena.getQueue().size());
			int i = 0;
			for (SpleefPlayer sp : arena.getQueue()) {
				DataManager.getManager().giveGameItems(sp);
				sp.addFFAGame();			
				Location l = locations.get(i);		
				sp.getPlayer().teleport(Utils.getUtils().lookAt(l, center_player));
				sp.getPlayer().setGameMode(GameMode.SURVIVAL);
				sp.setScoreboard(ScoreboardType.FFAGAME);
				sp.stopfly();
				arena.getPlayers().add(sp);
				i++;
			}
			} else if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
				int i = 0;
				for (SpleefPlayer sp : arena.getQueue()) {
					if (i<=arena.getQueue().size()) {
					DataManager.getManager().giveGameItems(sp);
					sp.getPlayer().setGameMode(GameMode.SURVIVAL);
					sp.setScoreboard(ScoreboardType._1VS1GAME);
					sp.stopfly();
					arena.getPlayers().add(sp);
					if (i==0) {
						sp.getPlayer().teleport(arena.getSpawn1());					
					} else {
						sp.getPlayer().teleport(arena.getSpawn2());		
					}
					i++;
				} 
					
					}
				
				for (SpleefPlayer s : arena.getPlayers()) {
					arena.getQueue().remove(s);
				}
			}
			
			
			new ArenaStartingCountdownTask(arena);
			
		}
		
		public void resetArena(SpleefArena arena) {
			Location a = null;
			Location b = null;
			
			Location c = arena.getArena1();
			Location d = arena.getArena2();
			if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
				a = arena.getArena1();
				b = arena.getArena2();
			} else if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
				a = arena.getArena1_1vs1();
				b = arena.getArena2_1vs1();		
			}
			
			if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
				 int ax = c.getBlockX();
				  int az = c.getBlockZ();	  
				  int y = c.getBlockY();		  
				  int bx = d.getBlockX();
				  int bz = d.getBlockZ();
			  	   for (int x = ax; x <= bx; x++) {
						  for (int z = az; z <= bz; z++) {
							  Location aire = new Location (c.getWorld(), x, y, z);
							  aire.getBlock().setType(Material.AIR); 
							  
						  }
					  }
			}
		  int ax = a.getBlockX();
		  int az = a.getBlockZ();	  
		  int y = a.getBlockY();		  
		  int bx = b.getBlockX();
		  int bz = b.getBlockZ();
	  	   for (int x = ax; x <= bx; x++) {
				  for (int z = az; z <= bz; z++) {
					  Location aire = new Location (a.getWorld(), x, y, z);
					  if (aire.getBlock().getType().equals(Material.AIR)) {
					  aire.getBlock().setType(Material.SNOW_BLOCK); 
					  }
				  }
			  }
	       	   
	  	   	if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
	  	   	int i = 1;		
			for (SpleefPlayer s : arena.getPlayers()) {					
				
			if (i==1) {
				s.getPlayer().teleport(arena.getSpawn1_1vs1());					
			} else {
				s.getPlayer().teleport(arena.getSpawn2_1vs1());		
			}
			i++;
			}
	  	   	}
	 
		}
		public void endGameFFA(SpleefArena arena,GameEndReason reason) {		
			arena.setState(GameState.FINISHING);
			arena.resetTimer();
			arena.getKills().clear();
			if (reason.equals(GameEndReason.WINNER)) {
						
				SpleefPlayer winner = arena.getPlayers().get(0);
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
				players.getPlayer().sendMessage("§bSpleef is starting in 5 seconds.");
				
			}
				if (isover) {
					if (wins>1) {
					players.getPlayer().sendMessage("§bWin Streak of " + winstreaker.getPlayer().getName() + " is over! Total wins: " + wins);
				}
				}
				
			}
			
			winner.addFFAWin();
			winner.getPlayer().teleport(arena.getLobby());
			DataManager.getManager().giveQueueItems(winner);
			arena.getPlayers().remove(winner);
			resetArena(arena);
			
			} else if (reason.equals(GameEndReason.TIME_OUT)) {
				
				SpleefPlayer winstreaker = null;
				int wins = 0;
				Iterator<Entry<SpleefPlayer, Integer>> it = arena.getWinStreak().entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<SpleefPlayer,Integer> pair = (Map.Entry<SpleefPlayer,Integer>)it.next();
			         winstreaker = pair.getKey();		  
			        wins = pair.getValue();
			    }
			    
				for (SpleefPlayer players : DataManager.getManager().getOnlinePlayers()) {
					players.getPlayer().sendMessage("§bNobody has won because the game took too long!");
					if (arena.getQueue().size()>=3) {
					players.getPlayer().sendMessage("§bSpleef is starting in 5 seconds.");
					
					}  
					
					if (wins>1) {
						players.getPlayer().sendMessage("§bWin Streak of " + winstreaker.getPlayer().getName() + " is over! Total wins: " + wins);
					}
					
					
				}
				arena.getWinStreak().clear();
				
				
				for (SpleefPlayer p : arena.getPlayers()) {
					p.getPlayer().teleport(arena.getLobby());
					DataManager.getManager().giveLobbyItems(p);
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
					players.getPlayer().sendMessage("§bSpleef is starting in 5 seconds.");
					
					}  
					
					if (wins>1) {
						players.getPlayer().sendMessage("§bWin Streak of " + winstreaker.getPlayer().getName() + " is over! Total wins: " + wins);
					}
				}
			}
			
			arena.getPlayers().clear();
			
			if (arena.getQueue().size()>=3) {
				new ArenaNewStartTask(arena);
				arena.setState(GameState.STARTING);
			} else {
				arena.setState(GameState.LOBBY);
			}
		}
		
		public void timeReset1vs1(SpleefArena arena) {
			arenaShrink(arena);
			arena.resetResetRound();
			arena.resetTimer();
		}
		
		
		public void endGame1vs1(SpleefArena arena,SpleefPlayer winner,SpleefPlayer loser,GameEndReason reason) {
			resetArena(arena);
			arena.resetResetRound();
			arena.resetTotalTime();
			arena.getResetRequest().clear();
			arena.setArena1_1vs1(arena.getArena1());
			arena.setArena2_1vs1(arena.getArena2());
			arena.setSpawn1_1vs1(arena.getSpawn1());
			arena.setSpawn2_1vs1(arena.getSpawn2());
			
			for (SpleefPlayer sp : arena.getPlayers()) {
				sp.setScoreboard(ScoreboardType.LOBBY);
				for (SpleefPlayer spect : sp.getSpectators()) {
					spect.setSpectate(null);
					spect.getPlayer().teleport(Main.lobby);
				}
			}
			
			
			arena.resetTimer();
			if (reason.equals(GameEndReason.WINNER)) {
			arena.setState(GameState.FINISHING);
			if (winner!=null) {
			if (arena.getPlayers().get(0).equals(winner)) {
				for (SpleefPlayer online : DataManager.getManager().getOnlinePlayers()) {
					online.getPlayer().sendMessage("§b" +winner.getPlayer().getName() +" won against " + loser.getPlayer().getName() +" §7(§a" + arena.getPoints1() + "§7-§a" + arena.getPoints2()+"§7)");
				}
				
			} else if (arena.getPlayers().get(1).equals(winner)) {
				for (SpleefPlayer online : DataManager.getManager().getOnlinePlayers()) {
					online.getPlayer().sendMessage("§b" +winner.getPlayer().getName() +" won against " + loser.getPlayer().getName() +" §7(§a" + arena.getPoints2() + "§7-§a" + arena.getPoints1()+"§7)");
				}
			}
			
			arena.setPoints1(0);
			arena.setPoints2(0);
			winner.getPlayer().teleport(Main.lobby);
			loser.getPlayer().teleport(Main.lobby);
			DataManager.getManager().giveLobbyItems(winner);
			DataManager.getManager().giveLobbyItems(loser);
		}
			} else if (reason.equals(GameEndReason.TIME_OUT)) {
				for (SpleefPlayer online : arena.getViewers()) {
					online.getPlayer().sendMessage("§bThe 1vs1 between " + arena.getPlayers().get(0)+ " §band" + arena.getPlayers().get(1)  + "§bfinished in a draw!");
				}
			} else if (reason.equals(GameEndReason.LOG_OFF)) {
				arena.setPoints1(0);
				arena.setPoints2(0);
				 winner = arena.getPlayers().get(0);
				 winner.getPlayer().teleport(Main.lobby);
				 DataManager.getManager().giveLobbyItems(winner);
				 for (SpleefPlayer online : DataManager.getManager().getOnlinePlayers()) {
						online.getPlayer().sendMessage("§b" +winner.getPlayer().getName() +" won the game!");
					}
			}
			arena.getPlayers().clear();
			if (arena.getQueue().size()>=2) {
				new ArenaNewStartTask(arena);
				arena.setState(GameState.STARTING);
			} else {
				arena.setState(GameState.LOBBY);
			}
		}
		public void fell(SpleefPlayer sp,SpleefPlayer killer,DeathReason reason) {
			
			SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
			if (arena.getType().equals(SpleefType.SPLEEFFFA)) {

			if (sp!=killer) {
				killer.addFFAKill();
			}
			sp.getPlayer().playSound(arena.getLobby(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 0.9F);
			killer.getPlayer().playSound(killer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.75F);
			arena.getPlayers().remove(sp);
			sp.getPlayer().teleport(arena.getLobby());
			DataManager.getManager().giveQueueItems(sp);
			if (arena.getPlayers().size()>1) {
				for (SpleefPlayer players : arena.getQueue()) {
					if (!sp.equals(killer)) {
						if (reason.equals(DeathReason.SPLEEFED)) {
					players.getPlayer().sendMessage("§6"+ sp.getPlayer().getName() + "§b was spleefed by §6"+ killer.getPlayer().getName() + "! §a" + arena.getPlayers().size()+" §bplayers left!");
						} else if (reason.equals(DeathReason.SNOWBALLED)) {
							players.getPlayer().sendMessage("§6"+ sp.getPlayer().getName() + "§b was snowballed by §6"+ killer.getPlayer().getName() + "! §a" + arena.getPlayers().size()+" §bplayers left!");
						}
						
					
						} else {
					players.getPlayer().sendMessage("§6"+ sp.getPlayer().getName() + " §bspleefed themself! §a" + arena.getPlayers().size()+" §bplayers left!");
				}
				}
				
				
			} else {			
				endGameFFA(arena,GameEndReason.WINNER);
				
			}
		
			} else if (arena.getType().equals(SpleefType.SPLEEF1VS1)) {
				arena.resetResetRound();
				arena.resetTimer();
				arena.setArena1_1vs1(arena.getArena1());
				arena.setArena2_1vs1(arena.getArena2());
				arena.setSpawn1_1vs1(arena.getSpawn1());
				arena.setSpawn2_1vs1(arena.getSpawn2());
				GameManager.getManager().resetArena(arena);
					arena.setState(GameState.STARTING);
					if (arena.getPlayers().get(0).equals(sp)) {
						arena.setPoints2(arena.getPoints2()+1);
						if (arena.getPoints2()>=7) {
							endGame1vs1(arena,arena.getPlayers().get(1),arena.getPlayers().get(0),GameEndReason.WINNER);
							return;
						}
					} else if (arena.getPlayers().get(1).equals(sp)) {
						arena.setPoints1(arena.getPoints1()+1);
						if (arena.getPoints1()>=7) {
							endGame1vs1(arena,arena.getPlayers().get(0),arena.getPlayers().get(1),GameEndReason.WINNER);
							return;
						}
					}						
					new ArenaStartingCountdownTask(arena);
				}
			}
		
		
		
		public void arenaShrink(SpleefArena arena) {
			Bukkit.getPlayer("SantiPingui58").sendMessage(""+arena.getSpawn1_1vs1());
			if (arena.getResetRound()<12) {
			arena.setResetRound(arena.getResetRound()+1);
			Location a = null;
			Location b = null;
			if (arena.getResetRound()==1) {
				//COSTADO 2
			 a = new Location(arena.getArena1_1vs1().getWorld(),
					arena.getArena1_1vs1().getX()+2,arena.getArena1_1vs1().getY(),arena.getArena1_1vs1().getZ());
			 b = new Location(arena.getArena2_1vs1().getWorld(),
					arena.getArena2_1vs1().getX()-2,arena.getArena2_1vs1().getY(),arena.getArena2_1vs1().getZ());
			
			
			} else if (arena.getResetRound()==2) {
				a = new Location(arena.getArena1_1vs1().getWorld(),
						arena.getArena1_1vs1().getX()+1,arena.getArena1_1vs1().getY(),arena.getArena1_1vs1().getZ()+2);
				 b = new Location(arena.getArena2_1vs1().getWorld(),
						arena.getArena2_1vs1().getX()-1,arena.getArena2_1vs1().getY(),arena.getArena2_1vs1().getZ()-2);
				 
					Location spawn1 = new Location(arena.getSpawn1_1vs1().getWorld(),
							arena.getSpawn1_1vs1().getX(),arena.getSpawn1_1vs1().getY(),arena.getSpawn1_1vs1().getZ()+2);
					spawn1.setDirection(arena.getSpawn1().getDirection());
					 Location spawn2  = new Location(arena.getSpawn2_1vs1().getWorld(),
							arena.getSpawn2_1vs1().getX(),arena.getSpawn2_1vs1().getY(),arena.getSpawn2_1vs1().getZ()-2);
					 spawn2.setDirection(arena.getSpawn2().getDirection());
					 arena.setSpawn1_1vs1(spawn1);
					 arena.setSpawn2_1vs1(spawn2);
					 
					 //VUELTA
			} else if (arena.getResetRound()==3 || arena.getResetRound()==5|| arena.getResetRound()==7|| arena.getResetRound()==9) {
				a = new Location(arena.getArena1_1vs1().getWorld(),
						arena.getArena1_1vs1().getX()+1,arena.getArena1_1vs1().getY(),arena.getArena1_1vs1().getZ()+1);
				 b = new Location(arena.getArena2_1vs1().getWorld(),
						arena.getArena2_1vs1().getX()-1,arena.getArena2_1vs1().getY(),arena.getArena2_1vs1().getZ()-1);
				 
					Location spawn1 = new Location(arena.getSpawn1_1vs1().getWorld(),
							arena.getSpawn1_1vs1().getX(),arena.getSpawn1_1vs1().getY(),arena.getSpawn1_1vs1().getZ()+1);
					spawn1.setDirection(arena.getSpawn1().getDirection());
					 Location spawn2  = new Location(arena.getSpawn2_1vs1().getWorld(),
							arena.getSpawn2_1vs1().getX(),arena.getSpawn2_1vs1().getY(),arena.getSpawn2_1vs1().getZ()-1);
					 spawn2.setDirection(arena.getSpawn2().getDirection());
					 
					 arena.setSpawn1_1vs1(spawn1);
					 arena.setSpawn2_1vs1(spawn2);
			} else if (arena.getResetRound()==11) {
				//COSTADO 1
				 a = new Location(arena.getArena1_1vs1().getWorld(),
							arena.getArena1_1vs1().getX()+1,arena.getArena1_1vs1().getY(),arena.getArena1_1vs1().getZ());
					 b = new Location(arena.getArena2_1vs1().getWorld(),
							arena.getArena2_1vs1().getX()-1,arena.getArena2_1vs1().getY(),arena.getArena2_1vs1().getZ());
					
					//LARGO
			}else {
				 a = new Location(arena.getArena1_1vs1().getWorld(),
							arena.getArena1_1vs1().getX(),arena.getArena1_1vs1().getY(),arena.getArena1_1vs1().getZ()+1);
					 b = new Location(arena.getArena2_1vs1().getWorld(),
							arena.getArena2_1vs1().getX(),arena.getArena2_1vs1().getY(),arena.getArena2_1vs1().getZ()-1);
					 
					 Location spawn1 = new Location(arena.getSpawn1_1vs1().getWorld(),
								arena.getSpawn1_1vs1().getX(),arena.getSpawn1_1vs1().getY(),arena.getSpawn1_1vs1().getZ()+1);
					 spawn1.setDirection(arena.getSpawn1().getDirection());
						 Location spawn2  = new Location(arena.getSpawn2_1vs1().getWorld(),
								arena.getSpawn2_1vs1().getX(),arena.getSpawn2_1vs1().getY(),arena.getSpawn2_1vs1().getZ()-1);
						 spawn2.setDirection(arena.getSpawn2().getDirection());
						 arena.setSpawn1_1vs1(spawn1);
						 arena.setSpawn2_1vs1(spawn2);
			}
			
			arena.setArena1_1vs1(a);
			arena.setArena2_1vs1(b);
			
		}
			
			resetArena(arena);
			
			Bukkit.getPlayer("SantiPingui58").sendMessage(""+arena.getSpawn1_1vs1());
		}
		
		
		
		public Location getNearest(Location l, List<SpleefKill> kills) {
			Location nearest = null;
		
			for (SpleefKill kill : kills) {
				if (nearest==null) {
					nearest = kill.getLocation();
				} else {
					if (l.distance(kill.getLocation()) < l.distance(nearest)) {
						nearest = kill.getLocation();
					}
				}
			}
		
		    
		    return nearest;
		}
		
		public SpleefPlayer getKillerByLocation(SpleefArena arena, Location l) {
			for (SpleefKill kill : arena.getKills()) {
				if (kill.getLocation().equals(l)) {
					return kill.getKiller();
				}
			}
			return null;
		}
		
		public DeathReason getReasonByKiller(SpleefArena arena,SpleefPlayer killer) {
			for (SpleefKill kill : arena.getKills()) {
				if (kill.getKiller().equals(killer)) {
					return kill.getReason();
				}
			}
			return null;
		}
		
}


