package me.santipingui58.splindux.task.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.task.ArenaStartingCountdownTask;
import me.santipingui58.splindux.utils.Utils;

public class ArenaTask {
	
	public ArenaTask() {
		new BukkitRunnable() {
			public void run() {
			 for (Arena arena : DataManager.getManager().getArenas()) {
					boolean arenastart = false;
				 if (arena.getPlayers()!= null && arena.getPlayers().size()==0) continue;
				 

				 
		    		if (arena.getState().equals(GameState.GAME)) {	
		    			if (arena.getGameType().equals(GameType.DUEL)) {
		    				arena.addTotalTime();
		    				/*
		    				if (arena.getTotalTime()==10) {
		    					arena.minispleef();
		    					new ArenaStartingCountdownTask(arena, null);
			    				arenastart = true;
		    				}
		    				*/
		    			}
		    			if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
		    				if (arena.getGameType().equals(GameType.DUEL)) {
		    					if (arena.getTotalTime()>=430) {
		    						arena.time();
		    					}
		    				} else {
		    					arena.time();
		    				}
		    				
		    				if (arena.getGameType().equals(GameType.DUEL)) {
		    					if (arena.getTime()==10) {
		    						GameManager.getManager().sendSyncMessage(arena.getViewers(), "§bMini Spleef in 10 seconds.");
		    					}
		    					
		    				
		    				} else if (arena.getGameType().equals(GameType.FFA) && arena.getSpleefType().equals(SpleefType.SPLEEF)) {
		    					if (arena.getTime()==65) {
		    						GameManager.getManager().sendSyncMessage(arena.getViewers(), "§bAutomatic regeneration of the arena in 5 seconds.");
		    					}
		    					if (arena.getTime()==60) {
		    						arena.ice();
		    				
		    					} 
		    					
		    					if (arena.getTime()<=100 && arena.getTime()%20==0 && arena.getTime()!=0) {
		    						
		    						String text = arena.getShrinkedRadious()==0 ? "§bArena will start shrinking!" : "§bArena shrinking will speed up!";
		    							GameManager.getManager().sendSyncMessage(arena.getViewers(), text);
		    						arena.shrinkFFA();
		    					}
		    				}
		    				
		    				
		    			}
		    			
		    		}
		    		
		    		
		    		
		    		
		    		
					if (arena.getTimedMaxTime()>0) {
						if (arena.getTotalTime()>=60*arena.getTimedMaxTime() && !arena.isAtMiniSpleef()) {
							int diff = arena.getPoints1()-arena.getPoints2();
							if (diff>0) {
								new BukkitRunnable() {
									public void run() {
										GameManager.getManager().endGameDuel(arena, "Team1", GameEndReason.WINNER);
									}
								}.runTask(Main.get());
							
							} else if (diff<0) {
								new BukkitRunnable() {
									public void run() {
										GameManager.getManager().endGameDuel(arena, "Team2", GameEndReason.WINNER);
									}
								}.runTask(Main.get());
							} else {
								if (arena.canTie()) {
									new BukkitRunnable() {
										public void run() {
								GameManager.getManager().endGameDuel(arena, null, GameEndReason.TIE);
									}
									}.runTask(Main.get());
								} else {
									GameManager.getManager().sendSyncMessage(arena.getViewers(), "§6§lGolden Point! §bWinner of this point wins the match.");
									GameManager.getManager().playToWithCommand(arena, arena.getPoints1()+1);
								}
							}
						}
					}
					
				
					
					if (arena.getTime()==0) {
		    			if (arena.getGameType().equals(GameType.FFA)) {
		    				FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
		    				ffa.endGame(GameEndReason.TIME_OUT);
		    			} else if (arena.getGameType().equals(GameType.DUEL)) {

		    				if (arena.getMiniSpleefRound()<=0) {
		    				arena.minispleef();
		    				new ArenaStartingCountdownTask(arena, null);
		    				arenastart = true;
		    				}
		    			} 
		    		}
					
					
    				if ( arena.getGameType().equals(GameType.DUEL) && (arena.getTime()==3  ||  arena.getTime()==2 || arena.getTime()==1)) {
	    				for (SpleefPlayer sp : arena.getViewers()) {
	    					Utils.getUtils().sendTitles(sp,"","§bMinispleef in "+arena.getTime() + "!" , 5, 20, 5);
	    				}
	    			}
					
					if (arena.getNextDecay()==0) {
						arena.decay(true);
					} else if (arena.getNextDecay()==10) {
						String level = "I";
						switch (arena.getDecayRound()) {
						case 1: level = "II"; break;
						case 2: level = "III"; break;
						case 3: level = "IV"; break;
						case 4: level = "V"; break;
						}
						
						GameManager.getManager().sendSyncMessage(arena.getViewers(), "§bDecay " + level + " of the arena in 10 seconds.");
					}
					
					if (arena.isAtMiniSpleef() && arena.getMiniSpleefTimer()==10) {
						arena.resetMiniSpleefTimer();
						arena.setMiniSpleefRound(arena.getMiniSpleefRound()+1);
						if (arena.getMiniSpleefRound()%2==0 || arena.getMiniSpleefRound()>= 10 + (arena.getTeamSize()-1)) {
							arena.reset(false, false);
							if (arena.getDecayTask()!=null) arena.getDecayTask().orderLocations();
							if (!arenastart) new ArenaStartingCountdownTask(arena, null);
						} else {
							arena.shrink();
							if (!arenastart) new ArenaStartingCountdownTask(arena, null);
						}
					} else if (arena.isAtMiniSpleef() && (arena.getMiniSpleefTimer()==7  ||  arena.getMiniSpleefTimer()==8 ||  arena.getMiniSpleefTimer() ==9 ))  {
						for (SpleefPlayer sp : arena.getViewers()) {
							int i = 10 - arena.getMiniSpleefTimer();
	    					Utils.getUtils().sendTitles(sp,"","§bReset in "+i + "!" , 5, 20, 5);
	    				}
						
					}
					
					
					
					

		    		
		    	}

			 
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20L);
	}
}
