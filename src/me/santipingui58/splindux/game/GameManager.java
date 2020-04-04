package me.santipingui58.splindux.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.task.ArenaNewStartTask;
import me.santipingui58.splindux.task.ArenaStartingCountdownTask;
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

			 if (!arena.getFFAPlayers().isEmpty() && arena.getGameType().equals(GameType.DUEL)) {
			 if (Utils.getUtils().containsIgnoreCase(arena.getName(), s)) {
				 i++;
			 }
		 }
		 }
		 return i;
	 }
	 
	 
	 public boolean isInGame(SpleefPlayer sp) {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
			 if (arena.getPlayers().contains(sp)) {
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public boolean isInArena(SpleefPlayer sp) {
		 if (isInGame(sp)) {
			 return true;
		 }
		 if (isInQueue(sp)) {
			 return true;
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
	 
	 public void leave(SpleefPlayer sp) {
		 sp.getPlayer().setGameMode(GameMode.ADVENTURE);
		leaveSpectate(sp);
		GameManager.getManager().leaveQueue(sp, GameManager.getManager().getArenaByPlayer(sp));    	
		
	 }
	 
	 public void leaveSpectate(SpleefPlayer sp) {
		 sp.setSpectate(null);
		 sp.setScoreboard(ScoreboardType.LOBBY);
		 if (sp.getOfflinePlayer().isOnline()) {
 		 sp.getPlayer().teleport(Main.lobby);
		 sp.getPlayer().setGameMode(GameMode.ADVENTURE);
	 }
	 }
	 
	 public void spectate(SpleefPlayer sp1, SpleefPlayer sp2) {
		 GameManager.getManager().leave(sp1);
		 sp1.getPlayer().setGameMode(GameMode.SPECTATOR);
		 sp1.getPlayer().teleport(sp2.getPlayer());		 
		 sp1.setScoreboard(sp2.getScoreboard());		 
		 sp1.setSpectate(sp2);
	 }
	 
	 public void addQueue(SpleefPlayer sp,SpleefArena arena) {
		leave(sp);
			arena.getQueue().add(sp);
			if (arena.getGameType().equals(GameType.FFA)) {
				
				sp.getPlayer().teleport(arena.getLobby());
			sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
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
			
			}
			
			DataManager.getManager().giveQueueItems(sp);
			
		
		}
	 
	 
		public void leaveQueue(SpleefPlayer sp, SpleefArena arena) {
			
			GameManager.getManager().leaveSpectate(sp);
			DataManager.getManager().giveLobbyItems(sp);
			if (GameManager.getManager().isInArena(sp)) {
			if (sp.getPlayer().isOnline()) {
				if (Main.arenas.getConfig().contains("mainlobby")) {
					sp.getPlayer().teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}				
			sp.getPlayer().sendMessage("§aYou have left the queue.");		
			sp.setScoreboard(ScoreboardType.LOBBY);
			}
			if (arena.getPlayers().contains(sp)) {
				if (!arena.getResetRequest().isEmpty()) {
				List<SpleefPlayer> resetRequest = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
				if (resetRequest.contains(sp)) {
					if (arena.getResetRequest().size()<=1) {
						GameManager.getManager().resetArenaWithCommand(arena);
					} else {
						arena.getResetRequest().remove(sp);
					}
				}
				}
				if (!arena.getEndGameRequest().isEmpty()) {
				List<SpleefPlayer> endGameRequest = GameManager.getManager().leftPlayersToSomething(arena.getEndGameRequest(), arena,true);
				if (endGameRequest.contains(sp)) {
					if (arena.getEndGameRequest().size()<=1) {
						GameManager.getManager().endGameDuel(arena, null,GameEndReason.ENDGAME);
					} else {
						arena.getEndGameRequest().remove(sp);
					}
				}
				}
				
				sp.getPlayer().teleport(arena.getLobby());
				
				if (arena.getGameType().equals(GameType.DUEL)) {
					if (arena.getDuelPlayers1().contains(sp)) {
						List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
						for (SpleefPlayer s : arena.getDuelPlayers1()) {
							if (!arena.getDeadPlayers1().contains(s)) {
								alive.add(s);
							}
						}
						if (alive.contains(sp)) {
						if (alive.size()<=1) GameManager.getManager().point(arena,sp);
						}
						
						if (arena.getDuelPlayers1().size()<=1) {
							GameManager.getManager().endGameDuel(arena,"Team2",GameEndReason.LOG_OFF);
						}
					} else if (arena.getDuelPlayers2().contains(sp)) {
							List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
							for (SpleefPlayer s : arena.getDuelPlayers2()) {
								if (!arena.getDeadPlayers2().contains(s)) {
									alive.add(s);
								}
							}					
							if (alive.contains(sp)) {
							if (alive.size()<=1) GameManager.getManager().point(arena,sp);
							} 
							if (arena.getDuelPlayers2().size()<=1) {
								GameManager.getManager().endGameDuel(arena,"Team1",GameEndReason.LOG_OFF);
							}						
					}
					arena.getDeadPlayers1().remove(sp);
					arena.getDeadPlayers2().remove(sp);
					arena.getDuelPlayers1().remove(sp);
					arena.getDuelPlayers2().remove(sp);
					
					if (arena.getPlayToRequest()!=null) {
						
						if (arena.getPlayToRequest().getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1) {
							GameManager.getManager().playToWithCommand(arena, arena.getPlayToRequest().getAmount());
						}		
				}
					
				if (arena.getCrumbleRequest()!=null) {
					if (arena.getCrumbleRequest().getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
						GameManager.getManager().crumbleWithCommand(arena, arena.getCrumbleRequest().getAmount());
					}		
			}
				
				} else if (arena.getGameType().equals(GameType.FFA)) {
					List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
					arena.getFFAPlayers().remove(sp);				
					players.addAll(arena.getFFAPlayers());
					
					if (players.size()<=1) {
						if (arena.getGameType().equals(GameType.FFA)) {
							GameManager.getManager().endGameFFA(arena, GameEndReason.WINNER);
						}					
						} 
				}
				
				
			} 
				arena.getQueue().remove(sp);
				
		}
		}
		
		public void duelGame(SpleefPlayer challenger, List<SpleefPlayer> sp2,String ar,SpleefType type) {
			
				challenger.getDuels().remove(challenger.getDuelByDueledPlayer(sp2.get(0)));
			
			SpleefArena arena = findArena(ar,type);
					
			if (arena!=null) {
				 leave(challenger);
				for (SpleefPlayer sp_2 : sp2) leave(sp_2);
				
				arena.getQueue().add(challenger);
				arena.getQueue().addAll(sp2);
			startGame(arena);				
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
		
		
		public void startGame(SpleefArena arena) {
			
			arena.setState(GameState.STARTING);
			
			arena.resetTimer();
			resetArena(arena);
			if (arena.getGameType().equals(GameType.FFA)) {
			Location center = new Location(arena.getMainSpawn().getWorld(),arena.getMainSpawn().getX(),arena.getMainSpawn().getY()+1,arena.getMainSpawn().getZ());
			Location center_player = new Location(center.getWorld(),center.getX(),center.getY()+1,center.getZ());
			
			List<Location> locations = Utils.getUtils().getCircle(center,16, arena.getQueue().size());
			int i = 0;
			for (SpleefPlayer sp : arena.getQueue()) {
				DataManager.getManager().giveGameItems(sp);
				sp.addFFAGame();			
				LevelManager.getManager().addLevel(sp, 1);
				Location l = locations.get(i);		
				sp.getPlayer().teleport(Utils.getUtils().lookAt(l, center_player));
				sp.getPlayer().setGameMode(GameMode.SURVIVAL);
				sp.setScoreboard(ScoreboardType.FFAGAME_GAME);
				sp.stopfly();
				arena.getFFAPlayers().add(sp);
				i++;
			}
			} else if (arena.getGameType().equals(GameType.DUEL)) {
				int i = 0;
				
				int teamSize = arena.getQueue().size()/2;
				
				for (SpleefPlayer sp : arena.getQueue()) {
					if (i<=arena.getQueue().size()) {
					if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
					sp.getPlayer().setGameMode(GameMode.SURVIVAL);
					} else if (arena.getSpleefType().equals(SpleefType.SPLEGG)) {
						sp.getPlayer().setGameMode(GameMode.ADVENTURE);
					}
					sp.setScoreboard(ScoreboardType._1VS1GAME);
					
					sp.stopfly();
					
					if (i<teamSize) {
						arena.getDuelPlayers1().add(sp);
					} else {
						arena.getDuelPlayers2().add(sp);
					}
					
					
					i++;
				} 
					
					}
				
				
				for (SpleefPlayer s : arena.getPlayers()) {
					arena.getQueue().remove(s);
				}
				for (SpleefPlayer sp :arena.getDuelPlayers1()) DataManager.getManager().giveGameItems(sp);
				for (SpleefPlayer sp :arena.getDuelPlayers2()) DataManager.getManager().giveGameItems(sp);
				List<Player> players = new ArrayList<Player>();
				for (SpleefPlayer sp : arena.getPlayers()) {
					players.add(sp.getPlayer());
				}
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!players.contains(p)) {
					TextComponent message = new TextComponent("§aA game between §b" + arena.getTeamName(1) + " §aand §b" + arena.getTeamName(2) + " §ahas started! §7(Right click to spectate)");
					message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/spectate "+arena.getDuelPlayers1().get(0).getOfflinePlayer().getName()));
					message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Spectate §a" +arena.getTeamName(1) + " §7-§a " + arena.getTeamName(1)).create()));
						p.spigot().sendMessage(message);
					}
				}
			}
			
			for (SpleefPlayer s : arena.getPlayers()) {
				EconomyManager.getManager().addCoins(s, 2, true);
				s.add1vs1Games();
			}
			
			
			for (SpleefPlayer s : arena.getDuelPlayers1()) s.getPlayer().teleport(arena.getSpawn1_1vs1());
			for (SpleefPlayer s : arena.getDuelPlayers2()) s.getPlayer().teleport(arena.getSpawn2_1vs1());
			new ArenaStartingCountdownTask(arena,false);
			
		}
		
		
		public void crumbleArena(SpleefArena arena,int por) {
			arena.setCrumbleRequest(null);
			  Location a = arena.getArena1_1vs1();
			  Location b = arena.getArena2_1vs1();
			  
			  int ax = a.getBlockX();
			  int az = a.getBlockZ();
			  
			  int y = a.getBlockY();
			  
			  int bx = b.getBlockX();
			  int bz = b.getBlockZ();
			  

			  SpleefPlayer p1 = arena.getDuelPlayers1().get(0);
			  SpleefPlayer p2 = arena.getDuelPlayers2().get(0);
			  
			  Location p1block = new Location (p1.getPlayer().getWorld(), p1.getLocation().getBlockX(), p1.getLocation().getBlockY()-1,
					  p1.getLocation().getBlockZ());
			  
			  Location p2block = new Location (p2.getPlayer().getWorld(), p2.getLocation().getBlockX(), p2.getLocation().getBlockY()-1,
					  p2.getLocation().getBlockZ());
			 
			  Location spawn1 = new Location (arena.getSpawn1_1vs1().getWorld(),arena.getSpawn1_1vs1().getBlockX(),
					  arena.getSpawn1_1vs1().getBlockY()-1, arena.getSpawn1_1vs1().getBlockZ());
			  
			  Location spawn2 = new Location (arena.getSpawn2_1vs1().getWorld(),arena.getSpawn2_1vs1().getBlockX(),
					  arena.getSpawn2_1vs1().getBlockY()-1, arena.getSpawn2_1vs1().getBlockZ());
			  
			  for (int x = ax; x <= bx; x++) {
				  for (int z = az; z <= bz; z++) {
					  Location aire = new Location (a.getWorld(), x, y, z); 
					  
					  if ((p1block.getBlockX() == aire.getBlockX() && p1block.getBlockY() == aire.getBlockY() 
							  && p1block.getBlockZ() == aire.getBlockZ()) || (p2block.getBlockX() == aire.getBlockX() && p2block.getBlockY() == aire.getBlockY() 
							  && p2block.getBlockZ() == aire.getBlockZ()) || (spawn1.getBlockX() == aire.getBlockX() && spawn1.getBlockY() == aire.getBlockY() 
							  && spawn1.getBlockZ() == aire.getBlockZ()) ||(spawn2.getBlockX() == aire.getBlockX() && spawn2.getBlockY() == aire.getBlockY() 
							  && spawn2.getBlockZ() == aire.getBlockZ())) {
					  } else {

							  int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
							  if (randomNum<por) {
								  for (SpleefPlayer sp : arena.getPlayers()) {
									  if (sp.getPlayer().getLocation().distance(aire)<4) continue;
									  if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
								  if (aire.getBlock().getType().equals(Material.SNOW_BLOCK)) {
									  aire.getBlock().setType(Material.AIR);
								  }
								  } else if (arena.getSpleefType().equals(SpleefType.SPLEGG)) {
									 if	(aire.getBlock().getType().equals(Material.STAINED_CLAY)) {
										 aire.getBlock().setType(Material.AIR);
												  }
											  
								  }
								  }
							  }
							  
					  }
				  }
			  }
			  
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
		GameManager.getManager().arenaShrink(arena,true);
		}
		
		public void playToWithCommand(SpleefArena g,int crumble) {
			g.setPlayTo(crumble);
			g.setPlayToRequest(null);
			
			for (SpleefPlayer p2 : g.getViewers()) {
					p2.getPlayer().sendMessage("§6The arena playto has been set to: §6" + crumble);
			}
		}
		
		public void crumbleWithCommand(SpleefArena g,int crumble) {
			GameManager.getManager().crumbleArena(g, crumble);
			g.setCrumbleRequest(null);	
			for (SpleefPlayer p2 : g.getViewers()) {
					p2.getPlayer().sendMessage("§6The arena has been crumbled with a percentage: §a" + crumble);
				
			}			
		}
		
		
		
		
		@SuppressWarnings("deprecation")
		public void resetArena(SpleefArena arena) {		
			
			arena.setPlayToRequest(null);
			arena.getEndGameRequest().clear();
			arena.setCrumbleRequest(null);
			arena.getResetRequest().clear();
			
			Location a = null;
			Location b = null;
			
			Location c = arena.getArena1();
			Location d = arena.getArena2();
			if (arena.getGameType().equals(GameType.FFA)) {
				a = arena.getArena1();
				b = arena.getArena2();
			} else if (arena.getGameType().equals(GameType.DUEL)) {
				a = arena.getArena1_1vs1();
				b = arena.getArena2_1vs1();		
			}
			
			if (arena.getGameType().equals(GameType.DUEL)) {
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
						  if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
					  aire.getBlock().setType(Material.SNOW_BLOCK); 
						  } else if (arena.getSpleefType().equals(SpleefType.SPLEGG)) {
							  aire.getBlock().setType(Material.STAINED_CLAY);
							  aire.getBlock().setData((byte) 1);
						  }
					  }
				  }
			  }
	       	   
			if (arena.getGameType().equals(GameType.DUEL)) {	  	   	
	  	   	for (SpleefPlayer s : arena.getDuelPlayers1()) s.getPlayer().teleport(arena.getSpawn1_1vs1());
	  		for (SpleefPlayer s : arena.getDuelPlayers2()) s.getPlayer().teleport(arena.getSpawn2_1vs1());
	  	   	
	  	   	}
	 
		}
		public void endGameFFA(SpleefArena arena,GameEndReason reason) {		
			arena.setState(GameState.FINISHING);
			arena.resetTimer();
			arena.getBrokenBlocks().clear();
			if (reason.equals(GameEndReason.WINNER)) {
						
				SpleefPlayer winner = arena.getFFAPlayers().get(0);
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
				players.getPlayer().sendMessage("§bSpleef is starting in 5 seconds.");
				
			}
				if (isover) {
					if (wins>1) {
					players.getPlayer().sendMessage("§bWin Streak of " + winstreaker.getOfflinePlayer().getName() + " is over! Total wins: " + wins);
				}
				}
				
			}
			
			winner.addFFAWin();
			winner.getPlayer().teleport(arena.getLobby());
			DataManager.getManager().giveQueueItems(winner);
			arena.getFFAPlayers().remove(winner);
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
				
				
				for (SpleefPlayer p : arena.getFFAPlayers()) {
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
			
			arena.getFFAPlayers().clear();
			
			if (arena.getQueue().size()>=3) {
				new ArenaNewStartTask(arena);
				arena.setState(GameState.STARTING);
			} else {
				arena.setState(GameState.LOBBY);
			}
		}
		
		public void timeReset1vs1(SpleefArena arena) {
			arenaShrink(arena,true);
			arena.resetTimer();
		}
		
		
		public void endGameDuel(SpleefArena arena,String w,GameEndReason reason) {	
			resetArena(arena);
			arena.resetResetRound();
			arena.resetTotalTime();
			arena.getResetRequest().clear();
			arena.setArena1_1vs1(arena.getArena1());
			arena.setArena2_1vs1(arena.getArena2());
			arena.setSpawn1_1vs1(arena.getSpawn1());
			arena.setSpawn2_1vs1(arena.getSpawn2());
			arena.getDeadPlayers1().clear();
			arena.getDeadPlayers2().clear();
			arena.resetPlayTo();
			List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
			players.addAll(arena.getDuelPlayers1());
			players.addAll(arena.getDuelPlayers2());
			for (SpleefPlayer sp : players) {
				sp.setScoreboard(ScoreboardType.LOBBY);
				for (SpleefPlayer spect : sp.getSpectators()) {
					leaveSpectate(spect);
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
				for (SpleefPlayer winner : winners)DataManager.getManager().giveLobbyItems(winner);
			for (SpleefPlayer loser : losers)DataManager.getManager().giveLobbyItems(loser);
		
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
				 for (SpleefPlayer winner : winners) DataManager.getManager().giveLobbyItems(winner);
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
					 DataManager.getManager().giveLobbyItems(sp);
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
			List<BrokenBlock> blocks = new ArrayList<BrokenBlock>();
			for (BrokenBlock block : arena.getBrokenBlocks()) {
				if (block.getPlayer().equals(sp)) {
					blocks.add(block);
				}
			}
			
			for (BrokenBlock block : blocks) {
				arena.getBrokenBlocks().remove(block);
			}
		}
		
		public void fell(SpleefPlayer sp,HashMap<DeathReason,SpleefPlayer> r) {		
			SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
						
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
			sp.getPlayer().teleport(arena.getLobby());
			DataManager.getManager().giveQueueItems(sp);
			
			DeathReason reason = null;
			SpleefPlayer killer = null;
			
			Iterator<Entry<DeathReason, SpleefPlayer>> it = r.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<DeathReason,SpleefPlayer> pair = (Map.Entry<DeathReason,SpleefPlayer>)it.next();
		        reason = pair.getKey();
		        if (pair.getValue()!=null) {
		        killer = pair.getValue();
		        }
		    }
			
		    if (killer!=null) {
		    	if (!killer.equals(sp)) {
		    		killer.addFFAKill();
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
						
				point(arena,sp);
				
		
			}
		}
		
		
		public void point(SpleefArena arena, SpleefPlayer sp) {

			arena.resetResetRound();
			arena.resetTimer();
			arena.getResetRequest().clear();
			arena.setCrumbleRequest(null);
			arena.resetResetRound();
			arena.setCrumbleRequest(null);
			arena.getEndGameRequest().clear();
			arena.setArena1_1vs1(arena.getArena1());
			arena.setArena2_1vs1(arena.getArena2());
			arena.setSpawn1_1vs1(arena.getSpawn1());
			arena.setSpawn2_1vs1(arena.getSpawn2());		
			if (arena.getDuelPlayers1().size()<1) {
				endGameDuel(arena,"Team2",GameEndReason.LOG_OFF);
				return;
			} else if (arena.getDuelPlayers2().size()<1) {
				endGameDuel(arena,"Team1",GameEndReason.LOG_OFF);
				return;
			}
				if (arena.getDuelPlayers1().contains(sp)) {
					arena.setPoints2(arena.getPoints2()+1);
					for (SpleefPlayer players : arena.getViewers()) { 
					players.getPlayer().sendMessage("§6"+ sp.getPlayer().getName()+ "§b fell! §6" + arena.getTeamName(2)+ "§b gets a point.");			
					}
					if (arena.getPoints2()>=arena.getPlayTo()) {
						endGameDuel(arena,"Team2",GameEndReason.WINNER);
						return;
					} else {
						arena.setState(GameState.STARTING);
						new ArenaStartingCountdownTask(arena,false);
					}
				} else if (arena.getDuelPlayers2().contains(sp)) {
					arena.setPoints1(arena.getPoints1()+1);
					for (SpleefPlayer players : arena.getViewers()) { 
						players.getPlayer().sendMessage("§6"+ sp.getPlayer().getName()+ "§b fell! §6" + arena.getTeamName(1)+ "§b gets a point.");							
						}
					if (arena.getPoints1()>=arena.getPlayTo()) {
						endGameDuel(arena,"Team1",GameEndReason.WINNER);
						return;
					} else {
						arena.setState(GameState.STARTING);
						new ArenaStartingCountdownTask(arena,false);
					}
				}	
				
				GameManager.getManager().resetArena(arena);
		}
		
		
		
		public void arenaShrink(SpleefArena arena,boolean keepDeadPlayers) {
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
			new ArenaStartingCountdownTask(arena,keepDeadPlayers);
			resetArena(arena);
		}
		
		
		


		public SpleefArena getArenaByName(String s) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getName().equalsIgnoreCase(s)) {
					return arena;
				}
			}
			return null;
		}
		
		
		public HashMap<DeathReason,SpleefPlayer> getDeathReason(SpleefPlayer sp) {
			HashMap<DeathReason,SpleefPlayer> hashmap = new HashMap<DeathReason,SpleefPlayer>();
			SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
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
			
				BrokenBlock broken2 = getNearestBlockIn(sp,5,arena);
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
				 if (block==null) {
					 block = broken;
				 }
				 
				 if (broken.getLocation().distance(sp.getPlayer().getLocation()) <i) {
					 if (broken.getLocation().distance(sp.getPlayer().getLocation()) < block.getLocation().distance(sp.getPlayer().getLocation())) {
						 block = broken;
					 }
				 }
			 }
			 return block;
		 }
}


