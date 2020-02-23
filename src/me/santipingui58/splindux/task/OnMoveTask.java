package me.santipingui58.splindux.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
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
		    			if (sp.getSpectating().getPlayer().getLocation().distance(sp.getPlayer().getLocation()) > 100) {
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
		    			
		    			if (sp.getPlayer().getLocation().getY() < 0 ) {
		    				sp.getPlayer().teleport(spawn);
		    			}
		    			
		    		}
		    		
		    		if (sp.getPlayer().hasPermission("splindux.afk")) {
		    		if (sp.isAfk()) {
		    			if (!sp.getLocation().equals(sp.getPlayer().getLocation())) {
		    			sp.back();
		    			sp.setAFKTimer(0);
		    			sp.getPlayer().sendMessage("§7You are not longer AFK");	
		    			}
		    			
		    		} else {
		    			if (sp.getLocation().equals(sp.getPlayer().getLocation())) {
		    			sp.setAFKTimer(sp.getAFKTimer()+1);
		    			if (sp.getAFKTimer()>=350) {
		    				sp.afk();
		    				sp.getPlayer().sendMessage("§7You are now AFK");	
		    			}
		    			} else {
		    				sp.setAFKTimer(0);
		    			}
		    		}
		    		
		    		}
		    		
		    		
		    	
		    		sp.getPlayer().setFireTicks(0);
		    		
		    		if (GameManager.getManager().isInGame(sp) || GameManager.getManager().isInQueue(sp)) {
		    			SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
		    			if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
		    			if (sp.getLocation().getYaw()==sp.getPlayer().getLocation().getYaw() && sp.getLocation().getPitch()==sp.getPlayer().getLocation().getPitch()) {
			    			sp.addGameAFKTimer();
			    			if (sp.getGameAFKTimer()>400) {		    				
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
		    			
		    			if (arena.getState().equals(GameState.GAME)) {
		    			if (sp.getPlayer().getLocation().getBlockY()<arena.getArena1().getBlockY()) {	    				
		    				Location death_block = GameManager.getManager().getNearest(sp.getPlayer().getLocation(), arena.getKills());
		    			SpleefPlayer killer = GameManager.getManager().getKillerByLocation(arena, death_block);
		    				GameManager.getManager().fell(sp,killer,GameManager.getManager().getReasonByKiller(arena, killer));
		    				
		    			}
		    			}
		    		}
		    	}
		    }
		    }, 20, 8L);
	}
}
