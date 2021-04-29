package me.santipingui58.splindux.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.BreakReason;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.translate.Main;

public class PlayerListener implements Listener {

	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();  
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			 if (sp.isSpectating()) e.setCancelled(true);
			if (sp.isInQueue() || sp.isInGame()) {
				if (sp.getArena().getState().equals(GameState.GAME)) {
				if (!e.getBlock().getType().equals(Material.SNOW_BLOCK) && !e.getBlock().getType().equals(Material.TNT)) {
					e.setCancelled(true);
				} else {
					
					if (sp.getArena().getGameType().equals(GameType.FFA)) {
						for (GameMutation mutation : sp.getArena().getInGameMutations()) {
							if (mutation.getType().equals(MutationType.KOHI_SPLEEF)) {
								sp.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
								break;
							}
						}
					}
					
					Arena arena = sp.getArena();
					BrokenBlock kill = new BrokenBlock(sp,e.getBlock().getLocation(),BreakReason.SHOVEL);
					arena.getBrokenBlocks().add(kill);
				}
				} else if (sp.getArena().getState().equals(GameState.LOBBY) && sp.getArena().getGameType().equals(GameType.FFA)){
					if (!e.getBlock().getType().equals(Material.SNOW_BLOCK) && !e.getBlock().getType().equals(Material.TNT)) {
						e.setCancelled(true);
					} else {
					new BukkitRunnable() {
						public void run() {
							e.getBlock().setType(Material.SNOW_BLOCK);
						}
					}.runTaskLater(Main.get(), 20L*20);
					}
				} else {
					e.setCancelled(true);
				}
			} else {
				e.setCancelled(true);
			}
		} 
	}
	
	@EventHandler
	public void onHand(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(e.getPlayer());	
		  if (e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("arenas") && (e.getBlock().getType().equals(Material.TNT))) {
			 try {
				for (GameMutation mutation : sp.getArena().getInGameMutations()) {
					if (mutation.getType().equals(MutationType.TNT_SPLEEF)) {
						mutation.getTNT().add(e.getBlock().getLocation());
					}
				}
			 } catch (Exception ex) {}
			return;  
		  }


		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			e.setCancelled(true);
		} else {
			for (ItemStack i : DataManager.getManager().lobbyitems()) {
				if (i.equals(e.getPlayer().getItemInHand())) {	
					e.setCancelled(true);
					return;
				}
			}
			
			for (ItemStack i : DataManager.getManager().queueitems()) {
				if (i.equals(e.getPlayer().getItemInHand())) {
			e.setCancelled(true);
			return;
				}
			}
		}
	}
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {	 
		if (e.getFrom().getWorld().getName().equalsIgnoreCase("arenas")) {
			if ((e.getFrom().getX() == e.getTo().getX()) && (e.getFrom().getZ() == e.getTo().getZ())) {
		        return;
		      }
		 if ((e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) || (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))) {
		        return;
		      }
		 
		// if (!e.getPlayer().isOnGround()) return;
		 
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(e.getPlayer());
			if (sp.isSpectating()) return;
			if (sp.isInGame()) {				
				boolean condition = (sp.getArena().getSpleefType().equals(SpleefType.TNTRUN) && sp.getArena().getState().equals(GameState.GAME) || sp.getArena().hasSnowRun());
				
				if (condition) {					
					final Block blockStanding = e.getFrom().subtract(0.0D, 1.0D, 0.0D).getBlock();
					if (blockStanding==null) {
						return;
					}
					final Block blockBelow_1 = blockStanding.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock();
					      final Block blockBelow_2 = blockStanding.getLocation().subtract(0.0D, 2.0D, 0.0D).getBlock();
					      Material m1 = blockBelow_1.getType();
					      Material m2 = blockBelow_2.getType();
					      Material m3 =  blockStanding.getType();
					      List<Material> allowedMaterials = new ArrayList<Material>();
					      allowedMaterials.add(Material.AIR);
					      allowedMaterials.add(Material.SNOW);
					      allowedMaterials.add(Material.SAND);
					      allowedMaterials.add(Material.GRAVEL);
					      allowedMaterials.add(Material.TNT);
					
					      if (!allowedMaterials.contains(m1))  return;
					      if (!allowedMaterials.contains(m2))  return;
					      if (!allowedMaterials.contains(m3))  return;
					      
				        Bukkit.getServer().getScheduler().runTaskLater(Main.get(), new Runnable()
				        {
				          public void run() {
				        	  if (sp.isInGame() && sp.getArena().getState().equals(GameState.GAME)) {
				        		  if (!allowedMaterials.contains(m1))  return;
							      if (!allowedMaterials.contains(m2))  return;
							      if (!allowedMaterials.contains(m3))  return;
				        	  blockStanding.setType(Material.AIR);
				        	  blockBelow_1.setType(Material.AIR);
				        	  blockBelow_2.setType(Material.AIR);
				        	  }
				          }
				        }
				        , 5L);
				      
				}
			}
		}
	}
	
	
}
