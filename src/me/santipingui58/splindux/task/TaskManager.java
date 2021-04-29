package me.santipingui58.splindux.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.anouncements.AnnouncementManager;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.GameEndReason;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.parkour.FinishParkourReason;
import me.santipingui58.splindux.game.parkour.ParkourArena;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.parkour.ParkourMode;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.task.tasks.ActionBarTask;
import me.santipingui58.splindux.task.tasks.ArenaTask;
import me.santipingui58.splindux.task.tasks.HighMoveTask;
import me.santipingui58.splindux.task.tasks.LowMoveTask;
import me.santipingui58.splindux.task.tasks.MinuteTask;
import me.santipingui58.splindux.task.tasks.OnlineTask;
import me.santipingui58.splindux.task.tasks.RankingTask;
import me.santipingui58.splindux.task.tasks.ScoreboardTask;
import me.santipingui58.splindux.task.tasks.TabTask;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.utils.Utils;


public class TaskManager {

	private static TaskManager manager;	
	 public static TaskManager getManager() {
	        if (manager == null)
	        	manager = new TaskManager();
	        return manager;
	    }
	 
	 private List<Task> asyncTasks = new ArrayList<Task>();	
	 private List<Task> syncTasks = new ArrayList<Task>();	
	 public void task() {
		 loadTasks();
		// timeAsync();
		 //timeSync();
		 new ArenaTask();
		 new HighMoveTask();
		 new LowMoveTask();
		 new MinuteTask();
		 new OnlineTask();
		 new RankingTask();
		 new ScoreboardTask();
		 new TabTask();
		 new ActionBarTask();
	 }
	 
	 private void loadTasks() {	 
		 for (TaskType type : TaskType.values()) {
			 Task task = new Task(type,type.getTicks());
			 if (type.async()) {
				 this.asyncTasks.add(task);
			 } else {
				 this.syncTasks.add(task);
			 }
 }
		 
	 }
	 	 
	 public void timeAsync() {
		 new BukkitRunnable() {
				public void run() {
					for (Task task : asyncTasks) {
						if (task.getCurrentTick()>=task.getTick()) {
							task.resetCurrentTick();
							getVoid(task);
						} else {
							task.addCurrentTick();
						}
					}
			}
			}.runTaskTimerAsynchronously(Main.get(), 0, 4L);
	 }
	 
	 
	 public void timeSync() {
		 Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			@Override
			public void run() {
				for (Task task : syncTasks) {					
					if (task.getCurrentTick()>=task.getTick()) {
						task.resetCurrentTick();
						getVoid(task);
					} else {
						task.addCurrentTick();
					}
				}
			}
	          
		 }, 0, 4L);
	
	 }
	 
	 private void getVoid(Task task) {		 
		 switch(task.getType()) {
		default:break;
		case ARENA: arenaTask();return;
		case MINUTE: minuteTask();return;
		case HIGH_MOVE:highPriorityMoveTask();return;
		case LOW_MOVE:lowPriorityMoveTask();return;
		case ONLINE: onlineTask();return;
		case TAB: tabTask();return;
		 }
		 
	 }
	 
	 private void arenaTask() {
		 for (Arena arena : DataManager.getManager().getArenas()) {
	    		if (arena.getState().equals(GameState.GAME)) {	
	    			if (arena.getGameType().equals(GameType.DUEL)) {
	    				arena.addTotalTime();
	    			}
	    			if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
	    				arena.time();
	    				if (arena.getGameType().equals(GameType.DUEL)) {
	    					if (arena.getTime()==10) {
	    						for (SpleefPlayer sp : arena.getViewers()) sp.getPlayer().sendMessage("§bAutomatic reset of the arena in 10 seconds.");
	    					} else if (arena.getTime()==3 || arena.getTime()==2 || arena.getTime()==1) {
	    						for (SpleefPlayer sp : arena.getViewers()) sp.getPlayer().sendMessage("§bAutomatic reset of the arena in " +arena.getTime() +"!");
	    					}
	    					
	    					
	    					if (arena.getTimedMaxTime()>0) {
	    						if (arena.getTotalTime()==20*arena.getTimedMaxTime()) {
	    							int diff = arena.getPoints1()-arena.getPoints2();
	    							if (diff>0) {
	    								GameManager.getManager().endGameDuel(arena, "Team1", GameEndReason.WINNER);
	    							} else if (diff<0) {
	    								GameManager.getManager().endGameDuel(arena, "Team2", GameEndReason.WINNER);
	    							} else {
	    								if (arena.canTie()) {
	    								GameManager.getManager().endGameDuel(arena, null, GameEndReason.TIE);
	    								} else {
	    									GameManager.getManager().sendSyncMessage(arena.getViewers(), "§6§lGold Point! &bWinner of this point wins the match.");
	    									GameManager.getManager().playToWithCommand(arena, arena.getPoints1()+1);
	    								}
	    							}
	    						}
	    					}
	    				}
	    				
	    				
	    			}
	    			
	    		}

	    		if (arena.getTime()==0) {
	    			if (arena.getGameType().equals(GameType.FFA)) {
	    			GameManager.getManager().endGameFFA(GameEndReason.TIME_OUT,arena.getSpleefType());
	    			} else if (arena.getGameType().equals(GameType.DUEL)) {
	    				GameManager.getManager().timeReset1vs1(arena);
	    			}
	    		}
	    	}
	 }
	 
	private void minuteTask() {
		  TimeLimitManager.getManager().time();
   	   AnnouncementManager.getManager().time();
   	   RankedManager.getManager().updateQueues();
   	  ParkourManager.getManager().calculateProbabilities();
	}
	
	@SuppressWarnings("deprecation")
	private void onlineTask() {
    	for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
    		if (sp.getOfflinePlayer().isOnline()) {
    		sp.addOnlineTime();    		
    	}
    	}
    	EconomyManager.getManager().checkSplinboxes();
    	Date date = new Date();
        if (date.getHours() == 6 && date.getMinutes() == 0 && date.getSeconds()==0) {
        	if (date.getDay()==0) {
        		DataManager.getManager().resetWeeklyStats();
        		}
        	
        	
        	
            if (date.getDate()==1){
            	DataManager.getManager().resetMonthlyStats();                	
            }
                            
            DataManager.getManager().giveMutationTokens();
            DataManager.getManager().giveRankeds();
            
        }
	}
	
	
	
	private int holograms;
	private void tabTask() {
    	   NPCManager.getManager().updateNPCs();   	   
    	   holograms++;
    	   if (holograms>=40) {
    		   holograms =0;
    		   HologramManager.getManager().updateHolograms(false);
    	   } else {
    		   HologramManager.getManager().updateHolograms(true);
    		   }	   
    	 
	}
	

	
	
	private void lowPriorityMoveTask() {
		for (Player p : Bukkit.getOnlinePlayers()) {			
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		spectatorDistance(sp);
		setLocation(sp);
		if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
			lobbyDistance(sp);
			
		} else if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("arenas")) {
			if (sp.isInGame()) {
			deadPlayerDistance(sp);		
		}
		}
		
		
		afkCheck(sp);
		
		//Remove fire at any moment
		sp.getPlayer().setFireTicks(0);
		sp.setLocation(sp.getPlayer().getLocation());
		potions(sp);
	}
	}
	


	private void highPriorityMoveTask() {
    	for (Player p : Bukkit.getOnlinePlayers()) {  
    		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
    		if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("parkour")) {
    			parkour(sp);
    		} else if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("arenas")) {
    			
    			if (sp.isInGame()) {
    				//deleteFloorTimer(sp);
    				//snowRunMutation(sp);
    				fellCheck(sp);
    			} 
    			
    			if (sp.isInArena()) {
    				practiceFFA(sp);
    			}
    		}
    	}
	}
	
	
	private void practiceFFA(SpleefPlayer sp) {
		if (sp.isInQueue()) {
			Arena arena = sp.getArena();		    			
			if (arena.getGameType().equals(GameType.FFA) && arena.getState().equals(GameState.LOBBY)) {
				if (sp.getPlayer().getLocation().getBlockY()<arena.getArena1().getBlockY()) {	 
					Location l = new Location(arena.getMainSpawn().getWorld(),arena.getMainSpawn().getBlockX(),arena.getMainSpawn().getBlockY()+15,arena.getMainSpawn().getBlockZ());
					sp.getPlayer().teleport(l);
				}
			}
		}
		
	}

	//Check if the player fell, by checking if their Y value is lower than the arena Y value.
	private void fellCheck(SpleefPlayer sp) {
		Arena arena = sp.getArena();
		if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
		
		if (sp.getPlayer().getLocation().getBlockY()<arena.getArena1().getBlockY()) {	 
			if (arena.getState().equals(GameState.GAME)) {
				
				LinkedHashMap<DeathReason, SpleefPlayer> reason = new LinkedHashMap<DeathReason,SpleefPlayer>();
				if (arena.getGameType().equals(GameType.FFA)) {
					 reason = GameManager.getManager().getDeathReason(sp);
				}

			GameManager.getManager().fell(sp,reason);
			
		}else  {
			if (arena.getGameType().equals(GameType.DUEL)) {
				if (arena.getDuelPlayers2().contains(sp)) {
					sp.getPlayer().teleport(arena.getShrinkedDuelSpawn2());
				} else {
					sp.getPlayer().teleport(arena.getShrinkedDuelSpawn1());
				}
			}
		}
		} 		
		}
	}

	


	private void potions(SpleefPlayer sp) {
		if (sp.getPlayer().getActivePotionEffects().size()>0) {
			for (PotionEffect effect : sp.getPlayer().getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
					PlayerParticlesAPI.getInstance().togglePlayerParticleVisibility(sp.getPlayer(),true);
					new BukkitRunnable() {	    			
				            @Override
				            public void run() {
				            	boolean b = true;
				            	if (sp.getPlayer().getActivePotionEffects().size()>0) {
					    			for (PotionEffect effect : sp.getPlayer().getActivePotionEffects()) {
					    				if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
					    					b = false;
					    				}
					    			}
					    				}
				            	
				            	if (b) {
				            		PlayerParticlesAPI.getInstance().togglePlayerParticleVisibility(sp.getPlayer(),false);
				            		cancel();
				            	}
				            }
				        }.runTaskTimer(Main.get(), 0L, 5);
				}
			
			}
		}
		
	}

	private void deadPlayerDistance(SpleefPlayer sp) {
		Arena arena = sp.getArena();
		//To prevent dead players to go away from alive players, if they are 30 blocks away from any player alive, they get teleported to the closest one.
		//Can get glitched if  alive players are more than 30 blocks away from each other.
		if (arena.getDeadPlayers1().contains(sp) || arena.getDeadPlayers2().contains(sp)) {
			//To prevent spectators to get away from players on the game, if they are 40 blocks away from the player they are spectating, they get teleported to them.
			if (sp.isSpectating()) {    	
				if (arena.getLobby().getWorld().getName().equalsIgnoreCase(sp.getPlayer().getLocation().getWorld().getName())) {
				if (arena.getLobby().distanceSquared(sp.getPlayer().getLocation()) > 40*40) {
					sp.getPlayer().teleport(arena.getLobby());
				}
				} else {
				}
			}
		}
		
	}

	private void lobbyDistance(SpleefPlayer sp) {
		Location spawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
		//Check if the player is in a 200 blocks radio or in positive Y value, otherwise teleport them to the spawn.
		
		if (sp.getPlayer().getLocation().getY() < 0  || sp.getPlayer().getLocation().distanceSquared(spawn)>200*200) {
			
			sp.getPlayer().teleport(spawn);
		}
		
	}
	
	private void parkour(SpleefPlayer sp) {
		ParkourArena pk_arena = sp.getParkourPlayer().getArena();   
		if (pk_arena!=null) {
		int dif = Math.abs(pk_arena.getCurrentStart().getBlockY()-sp.getPlayer().getLocation().getBlockY());
		if (sp.getPlayer().isOnGround() && pk_arena.getCurrentFinish().distanceSquared(sp.getPlayer().getLocation()) <=1.25) {
			pk_arena.doJump();   				
		} else if (pk_arena.getCurrentStart().getBlockY()>sp.getPlayer().getLocation().getBlockY() && dif>3) {
			sp.getPlayer().teleport(Utils.getUtils().getCenter(pk_arena.getCurrentStart()));
			
			
			sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP,10,127));
			pk_arena.setFails(pk_arena.getFails()+1);
			sp.getPlayer().setWalkSpeed(0);
			new BukkitRunnable() {

				@Override
				public void run() {
					sp.getPlayer().setWalkSpeed(0.2F);
					
				}
				
			}.runTaskLater(Main.get(), 10L);
			
			if (pk_arena.getFails()>=3 && pk_arena.getMode().equals(ParkourMode.MOST_JUMPS)) {
				pk_arena.finish(FinishParkourReason.LOST);
			}
		}
	}
		
		
		
	}
	
	private void spectatorDistance(SpleefPlayer sp) {
		//To prevent spectators to get away from players on the game, if they are 40 blocks away from the player they are spectating, they get teleported to them.
				if (sp.isSpectating()) {    	
					Arena arena = sp.getSpleefArenaSpectating();
					if (arena.getLobby().getWorld().getName().equalsIgnoreCase(sp.getPlayer().getLocation().getWorld().getName())) {
					if (arena.getLobby().distanceSquared(sp.getPlayer().getLocation()) > 40*40) {
						sp.getPlayer().teleport(arena.getLobby());
					}
					} else {
					}
				}
	}

	private void setLocation(SpleefPlayer sp) {
		//Set the old player location
				if (sp.getLocation()==null) {
					if (sp.getPlayer().isOnline()) {
					sp.setLocation(sp.getPlayer().getLocation());
					}
				}
	}
	
	
	private void afkCheck(SpleefPlayer sp) {
		//AFK System
				if (sp.isAfk()) {
					if (!sp.getLocation().equals(sp.getPlayer().getLocation())) {
					sp.back();
					sp.setAFKTimer(0);
					if (sp.getPlayer().hasPermission("splindux.afk")) {
					sp.getPlayer().sendMessage("§7You are not longer AFK");	
					}
					}
					
				} else {
					if (sp.getLocation().equals(sp.getPlayer().getLocation())) {
					sp.setAFKTimer(sp.getAFKTimer()+1);
					if (sp.getAFKTimer()>=3500) {
						sp.afk();
						if (sp.getPlayer().hasPermission("splindux.afk")) {
						sp.getPlayer().sendMessage("§7You are now AFK");	
						}
					}
					} else {
						sp.setAFKTimer(0);
					}
				}
	}
	
	
	
	

	
	    
		  
	

	
	/* private void deleteFloorTimer(SpleefPlayer sp) {
			if (sp.isInGame() && sp.getArena().getState().equals(GameState.GAME)) {
			            Player player = sp.getPlayer();

			            if ((!player.getGameMode().equals(GameMode.SPECTATOR)) && (!player.getGameMode().equals(GameMode.CREATIVE))) {
			           
			            	getBlockUnderPlayer(player.getLocation().getBlockY() + 1,player.getLocation()).setType(Material.AIR);
			              //FAWESplinduxAPI.getAPI().placeBlocks(standingBlock.getLocation(), standingBlock.getLocation(), Material.AIR);
			            }
			            }		        
		}
		
		*/

}
