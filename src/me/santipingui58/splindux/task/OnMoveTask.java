package me.santipingui58.splindux.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.utils.Utils;


public class OnMoveTask {

	public OnMoveTask() {
		task();
	}
	
	private void task() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			
		    public void run() {
		    			  
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
		    		
		    		
		    		
		    		//To prevent dead players to go away from alive players, if they are 30 blocks away from any player alive, they get teleported to the closest one.
		    		//Can get glitched if  alive players are more than 30 blocks away from each other.
		    		if (sp.isInGame()) {
		    			SpleefArena arena = sp.getArena();
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
		    	}
		    }
		    }, 20, 4L);
	}
}
