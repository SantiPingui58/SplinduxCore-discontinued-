package me.santipingui58.splindux.task;

import java.util.ArrayList;
import java.util.HashMap;
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
		  		  if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
		    			sp.getPlayer().teleport(Main.lobby);
				 }
		    		
		    		if (sp.isSpectating()) {
		    			if (sp.getSpectating().getPlayer().getLocation().distance(sp.getPlayer().getLocation()) > 40) {
		    				sp.getPlayer().teleport(sp.getSpectating().getLocation());
		    			}
		    		}
		    		
		    		
		    		
		    	
		    		
		    		if (sp.getLocation()==null) {
		    			if (sp.getPlayer().isOnline()) {
		    			sp.setLocation(sp.getPlayer().getLocation());
		    			}
		    		}
		    		
		    		if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {

		    			Location spawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
		    			
		    			if (sp.getPlayer().getLocation().getY() < 0  || sp.getPlayer().getLocation().distance(spawn)>200) {
		    				sp.getPlayer().teleport(spawn);
		    			}
		    			
		    		}
		    		
		    		if (GameManager.getManager().isInGame(sp)) {
		    			SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
		    			if (arena.getDeadPlayers1().contains(sp) || arena.getDeadPlayers2().contains(sp)) {
		    				List<SpleefPlayer> alive = new ArrayList<SpleefPlayer>();
		    				for (SpleefPlayer players : arena.getPlayers()) {
		    					if (!arena.getDeadPlayers1().contains(players) && !arena.getDeadPlayers2().contains(players)) {
		    						alive.add(players);
		    					}
		    				}
		    				
		    				for (SpleefPlayer a : alive) {
		    					if (a.getPlayer().getLocation().distance(sp.getPlayer().getLocation()) > 30) {
		    						sp.getPlayer().teleport(a.getPlayer());
		    						break;
		    					}
		    				}
		    			}
		    		}
		    		
		    		
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
		    		
		    		
		    	
		    		sp.getPlayer().setFireTicks(0);
		    		
		    		if (GameManager.getManager().isInGame(sp) || GameManager.getManager().isInQueue(sp)) {
		    			SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
		    			if (arena.getGameType().equals(GameType.FFA)) {
		    			if (sp.getLocation().getYaw()==sp.getPlayer().getLocation().getYaw() && sp.getLocation().getPitch()==sp.getPlayer().getLocation().getPitch()) {
			    			sp.addGameAFKTimer();
			    			if (sp.getGameAFKTimer()>1600) {		    				
			    				GameManager.getManager().leaveQueue(sp, arena);
			    				sp.getPlayer().sendMessage("§cYou got off from the game because you were afk too long!");
			    			}
			    		} else {
			    			sp.setGameAFKTimer(0);
			    		}
		    		}
		    		}
		    		sp.setLocation(sp.getPlayer().getLocation());
		    		
		    		if (GameManager.getManager().isInGame(sp)) {
		    			SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
		    			if (arena.getDeadPlayers1().contains(sp) || arena.getDeadPlayers2().contains(sp)) continue;
		    			
		    			if (sp.getPlayer().getLocation().getBlockY()<arena.getArena1().getBlockY()) {	 
		    				if (arena.getState().equals(GameState.GAME)) {
		    					
		    					HashMap<DeathReason, SpleefPlayer> reason = new HashMap<DeathReason,SpleefPlayer>();
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
		    }, 20, 2L);
	}
}
