package me.santipingui58.splindux.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
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
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.scoreboard.PinguiScoreboard;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.stats.RankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.timelimit.TimeLimitManager;
import me.santipingui58.splindux.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;


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
		 timeAsync();
		 timeSync();
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
			}.runTaskTimerAsynchronously(Main.get(), 0, 5);
	 }
	 
	 
	 public void timeSync() {
		 Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			@Override
			public void run() {
				for (Task task : syncTasks) {
					//Bukkit.broadcastMessage(""+task.getCurrentTick());
					if (task.getCurrentTick()>=task.getTick()) {
						task.resetCurrentTick();
						getVoid(task);
					} else {
						task.addCurrentTick();
					}
				}
			}
	          
		 }, 0, 5L);
	
	 }
	 
	 private void getVoid(Task task) {		 
		 switch(task.getType()) {
		default:break;
		case ARENA: arenaTask();return;
		case MINUTE: minuteTask();return;
		case MOVE:moveTask();return;
		case ONLINE: onlineTask();return;
		case SCOREBOARD: scoreboardTask();return;
		case RANKING: rankingTask();return;
		case TAB: tabTask();return;
		 }
		 
	 }
	 
	 private void arenaTask() {
		 for (SpleefArena arena : DataManager.getManager().getArenas()) {
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
	    				}
	    			}
	    			
	    		}

	    		if (arena.getTime()==0) {
	    			if (arena.getGameType().equals(GameType.FFA)) {
	    			GameManager.getManager().endGameFFA(arena,GameEndReason.TIME_OUT);
	    			} else if (arena.getGameType().equals(GameType.DUEL)) {
	    				GameManager.getManager().timeReset1vs1(arena);
	    			}
	    		}
	    	}
	 }
	 
	private void minuteTask() {
		  TimeLimitManager.getManager().time();
   	   AnnouncementManager.getManager().time();
	}
	
	@SuppressWarnings("deprecation")
	private void onlineTask() {
    	for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
    		sp.addOnlineTime();    		
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

            
        }
	}
	
	private void scoreboardTask() {
		for (Player p : Bukkit.getOnlinePlayers()) {	   		    		
    		PinguiScoreboard.getScoreboard().scoreboard(p);	
    		
    		}
	}
	
	private void rankingTask() {
		StatsManager.getManager().updateRankings();
    	DataManager.getManager().savePlayers();
    	boolean b = false;
    	String name = "";
    	 for (Map.Entry<String, Integer> entry : StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_WEEKLY).entrySet()) {
    		 if (!b) {
    			 b = true;
    			 name = entry.getKey();
    		 } else {
    			 break;
    		 }
    	 }
    	 
    	 try {
    	NPC npc = CitizensAPI.getNPCRegistry().getById(0);
    	SkinnableEntity entity = (SkinnableEntity) npc.getEntity();
    	if (!name.isEmpty() && !name.equalsIgnoreCase("")) {
    	entity.setSkinName(name);
    } else {
    	entity.setSkinName("SantiPingui58");
    }
    	}catch(Exception e) {}
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
	
	private void moveTask() {
		  
    	for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {  

    		//Check if an admin is already logged in to move.
    		//Not used.
  		  if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
    			sp.getPlayer().teleport(Main.lobby);
		 } 
  		  //To prevent spectators to get away from players on the game, if they are 40 blocks away from the player they are spectating, they get teleported to them.
    		if (sp.isSpectating()) {
    			if (sp.getSpectating().getPlayer().getLocation().distance(sp.getPlayer().getLocation()) > 40) {
    				sp.getPlayer().teleport(sp.getSpectating().getLocation());
    			}
    		}
    		//Set the old player location
    		if (sp.getLocation()==null) {
    			if (sp.getPlayer().isOnline()) {
    			sp.setLocation(sp.getPlayer().getLocation());
    			}
    		}
    		//World named world is where the Lobby is
    		if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
    			Location spawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
    			//Check if the player is in a 200 blocks radio or in positive Y value, otherwise teleport them to the spawn.
    			if (sp.getPlayer().getLocation().getY() < 0  || sp.getPlayer().getLocation().distance(spawn)>200) {
    				sp.getPlayer().teleport(spawn);
    			}
    			if (sp.getPlayer().getLocation().getY()<115 && sp.isFlying() && !sp.getPlayer().hasPermission("splindux.admin")) {
    			sp.stopfly();
    			}
    			
    		}
    		
    		if (sp.isInGame()) {
    			SpleefArena arena = sp.getArena();
    			//To prevent dead players to go away from alive players, if they are 30 blocks away from any player alive, they get teleported to the closest one.
	    		//Can get glitched if  alive players are more than 30 blocks away from each other.
    			if (arena.getDeadPlayers1().contains(sp) || arena.getDeadPlayers2().contains(sp)) {
    				List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
    				for (SpleefPlayer players : arena.getPlayers()) {
    					if (!arena.getDeadPlayers1().contains(players) && !arena.getDeadPlayers2().contains(players)) {
    						alive.add(players);
    					}
    				}
    				
    				for (SpleefPlayer a : alive) {
    					if (a.getPlayer().getLocation().distance(sp.getPlayer().getLocation()) > 40) {
    						sp.getPlayer().teleport(a.getPlayer());
    						break;
    					}
    				}
    			}
    			//Snow Run Mutation
    			if (arena.getState().equals(GameState.GAME)) {
    			for (GameMutation mutation : arena.getInGameMutations()) {
    				if (mutation.getType().equals(MutationType.SNOW_RUN)) {
    					sp.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);		    					 
    				}
    			}
    			}
    		}
    		
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
    		//Remove fire at any moment
    		sp.getPlayer().setFireTicks(0);
    		
    		
    		//Check if the player in FFA is AFK, currently not being developed.
    		if (sp.isInGame() || sp.isInQueue()) {
    			if (sp.getArena()!=null) {
    			SpleefArena arena = sp.getArena();
    			if (arena.getGameType().equals(GameType.FFA)) {
    			if (sp.getLocation().getYaw()==sp.getPlayer().getLocation().getYaw() && sp.getLocation().getPitch()==sp.getPlayer().getLocation().getPitch()) {
	    			sp.addGameAFKTimer();
	    			if (sp.getGameAFKTimer()>1600) {		    				
	    				sp.leaveQueue(arena,true);
	    				sp.getPlayer().sendMessage("§cYou got off from the game because you were afk too long!");
	    			}
	    		} else {
	    			sp.setGameAFKTimer(0);
	    		}
    		}
    		}
    		}
    		sp.setLocation(sp.getPlayer().getLocation());
    		//Check if the player fell, by checking if their Y value is lower than the arena Y value.
    		if (sp.isInGame()) {
    			SpleefArena arena = sp.getArena();
    			if (arena.getDeadPlayers1().contains(sp) || arena.getDeadPlayers2().contains(sp)) continue;
    			
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
    		
    		if (sp.isInQueue()) {
    			SpleefArena arena = sp.getArena();		    			
    			if (arena.getGameType().equals(GameType.FFA) && arena.getState().equals(GameState.LOBBY)) {
    				if (sp.getPlayer().getLocation().getBlockY()<arena.getArena1().getBlockY()) {	 
    					Location l = new Location(arena.getMainSpawn().getWorld(),arena.getMainSpawn().getBlockX(),arena.getMainSpawn().getBlockY()+15,arena.getMainSpawn().getBlockZ());
    					sp.getPlayer().teleport(l);
    				}
    			}
    		}
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
	}
} 
