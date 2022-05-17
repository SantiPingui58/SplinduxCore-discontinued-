package me.santipingui58.splindux.task.tasks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.parkour.FinishParkourReason;
import me.santipingui58.splindux.game.parkour.ParkourArena;
import me.santipingui58.splindux.game.parkour.ParkourMode;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.Utils;

public class HighMoveTask {
	
	
	
	public HighMoveTask() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {  
		    		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		    		if (sp==null) continue;
		    		if (sp.getPlayer()==null) continue;
		    		if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("parkour")) {
		    			parkour(sp);
		    		} else if (sp.getPlayer().getWorld().getName().equalsIgnoreCase("arenas")) {
		    			
		    			if (sp.isInGame()) {
		    				fellCheck(sp);
		    				pauseCheck(sp);
		    			} 
		    			
		    			if (sp.isInArena()) {
		    				practiceFFA(sp);
		    			}
		    		}
		    	}
			}
		}.runTaskTimer(Main.get(), 0L, 2L);
	}
	
	
	public void pauseCheck(SpleefPlayer sp) {
		if (sp.isInGame()) {
			if (sp.getArena().getState().equals(GameState.PAUSE)) {
				if (sp.getLocation()!=null && sp.getLocation().distanceSquared(sp.getPlayer().getLocation()) > 0.5) {
					sp.teleport(sp.getLocation());
				}
			}
		}
	}
	
	
	private void parkour(SpleefPlayer sp) {
		new BukkitRunnable() {
			public void run() {
		ParkourArena pk_arena = sp.getParkourPlayer().getArena();   
		if (pk_arena!=null) {
			
			
		int dif = Math.abs(pk_arena.getCurrentStart().getBlockY()-sp.getPlayer().getLocation().getBlockY());
		if (sp.getPlayer().isOnGround() && sp.getLocation().getWorld().getName().equalsIgnoreCase("parkour") && pk_arena.getCurrentFinish().distanceSquared(sp.getPlayer().getLocation()) <=1.25) {
			pk_arena.doJump();   				
		} else if (pk_arena.getCurrentStart().getBlockY()>sp.getPlayer().getLocation().getBlockY() && dif>3) {
			
			pk_arena.setFails(pk_arena.getFails()+1);
			
			new BukkitRunnable() {
				public void run() {
			sp.getPlayer().teleport(Utils.getUtils().getCenter(pk_arena.getCurrentStart()));
			sp.fellParkour(true);
			}
			}.runTask(Main.get());
		
					sp.fellParkour(false);
			
			if (pk_arena.getFails()>=3 && pk_arena.getMode().equals(ParkourMode.MOST_JUMPS)) {
				pk_arena.finish(FinishParkourReason.LOST);
			}
		}
	}
			}
		}.runTaskAsynchronously(Main.get());
		
	}
	
	
	private void practiceFFA(SpleefPlayer sp) {
		if (sp.isInQueue()) {
			Arena arena = sp.getArena();		    			
			if (arena.getGameType().equals(GameType.FFA) && arena.getState().equals(GameState.LOBBY)) {
				if (sp.getPlayer().getLocation().getBlockY()<arena.getArena1().getBlockY()) {
					Location l =null;
					if (arena.getSpleefType().equals(SpleefType.TNTRUN)) {
						l = arena.getLobby();
					} else {
					 l = new Location(arena.getMainSpawn().getWorld(),arena.getMainSpawn().getBlockX(),arena.getMainSpawn().getBlockY()+10,arena.getMainSpawn().getBlockZ());
					}
					
					final Location l2 = l;
					sp.getPlayer().teleport(l2);			
				}
			}
		}
		
	}
	
	//Check if the player fell, by checking if their Y value is lower than the arena Y value.
		private void fellCheck(SpleefPlayer sp) {
			Arena arena = sp.getArena();
			
			if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
			
			if (sp.getPlayer().getLocation().getBlockY()<arena.getArena1().getBlockY()) {	 
				if (arena.getState().equals(GameState.GAME) || (arena.isAtMiniSpleef() && arena.getState().equals(GameState.STARTING))) {
					
					LinkedHashMap<DeathReason, SpleefPlayer> reason = new LinkedHashMap<DeathReason,SpleefPlayer>();
					if (arena.getGameType().equals(GameType.FFA)) {
						 reason = GameManager.getManager().getDeathReason(sp);
					}
					final LinkedHashMap<DeathReason, SpleefPlayer> r = reason;
				GameManager.getManager().fell(sp,r);	
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
		

		

		
		public List<Block> blocks(Player player) {
			List<Block> list = new ArrayList<Block>();
			Block standingBlock = player.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock();

            double x = player.getLocation().getX();

            double z = player.getLocation().getZ();

             list.add(standingBlock);

            if (x % 1.0D > 0.5D) {
              if (z % 1.0D > 0.5D) {
                Block blockN = standingBlock.getRelative(BlockFace.EAST);
                 list.add(blockN);
                Block blockNE = standingBlock.getRelative(BlockFace.SOUTH);
                 list.add(blockNE);
                Block blockE = standingBlock.getRelative(BlockFace.SOUTH_EAST);
                 list.add(blockE);
              }
              else {
                Block blockW = standingBlock.getRelative(BlockFace.EAST);
                 list.add(blockW);
                Block blockNW = standingBlock.getRelative(BlockFace.NORTH);
                 list.add(blockNW);
                Block blockN = standingBlock.getRelative(BlockFace.NORTH_EAST);
                 list.add(blockN);
              }

            }
            else if (z % 1.0D > 0.5D) {
              Block blockE = standingBlock.getRelative(BlockFace.WEST);
               list.add(blockE);
              Block blockSE = standingBlock.getRelative(BlockFace.SOUTH);
               list.add(blockSE);
              Block blockS = standingBlock.getRelative(BlockFace.SOUTH_WEST);
               list.add(blockS);
            }
            else {
              Block blockW = standingBlock.getRelative(BlockFace.NORTH);
              list.add(blockW);
              Block blockSW = standingBlock.getRelative(BlockFace.WEST);
               list.add(blockSW);
              Block blockS = standingBlock.getRelative(BlockFace.NORTH_WEST);
               list.add(blockS);
            }
            return list;
		}
		
		
}
