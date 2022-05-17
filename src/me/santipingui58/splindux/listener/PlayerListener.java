package me.santipingui58.splindux.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.BreakReason;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class PlayerListener implements Listener {

	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();  
		if (p.getWorld().getName().equalsIgnoreCase("event")) {
			if (p.hasPermission("splindux.staff") && p.getGameMode().equals(GameMode.CREATIVE)) return;
			if  (!Main.canBreak) {
				e.setCancelled(true);
				return;
			} else {
				return;
			}
		} 
		
		
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			 if (sp.isSpectating()) e.setCancelled(true);
			if (sp.isInQueue() || sp.isInGame()) {
				if (sp.getArena().getState().equals(GameState.GAME)) {
				if (!e.getBlock().getType().equals(Material.SNOW_BLOCK) && !e.getBlock().getType().equals(Material.TNT) && !e.getBlock().getType().equals(Material.CONCRETE_POWDER)) {
					e.setCancelled(true);
				} else {
					if (sp.getArena().getGameType().equals(GameType.FFA)) {
						FFAArena ffa = GameManager.getManager().getFFAArenaByArena(sp.getArena());
						for (GameMutation mutation : ffa.getInGameMutations()) {
							if (mutation.getType().equals(MutationType.KOHI_SPLEEF)) {
								sp.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
								break;
							}
						}
						BrokenBlock kill = new BrokenBlock(sp,e.getBlock().getLocation(),BreakReason.SHOVEL);
						ffa.getBrokenBlocks().add(kill);
					}
					
				
					
				}
				} else if (sp.getArena().getState().equals(GameState.LOBBY) && sp.getArena().getGameType().equals(GameType.FFA)){
					if (!e.getBlock().getType().equals(Material.SNOW_BLOCK) && !e.getBlock().getType().equals(Material.TNT)) {
						e.setCancelled(true);
					} else {
					new BukkitRunnable() {
						public void run() {
							e.getBlock().setType(Material.SNOW_BLOCK);
						}
					}.runTaskLater(Main.get(), 20L*7);
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
		Player p = e.getPlayer();
		
		if (p.getWorld().getName().equalsIgnoreCase("event")) {
			if (p.hasPermission("splindux.staff") && p.getGameMode().equals(GameMode.CREATIVE)) return;
			if  (!Main.canBreak) {
				e.setCancelled(true);
				return;
			} else {
				return;
			}
		} 
		
		
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(e.getPlayer());	
		
		  if (e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("arenas") && (e.getBlock().getType().equals(Material.TNT))) {
			 try {
				 if (sp.getArena().getGameType().equals(GameType.FFA)) {
				 FFAArena ffa = GameManager.getManager().getFFAArenaByArena(sp.getArena());
				for (GameMutation mutation : ffa.getInGameMutations()) {
					if (mutation.getType().equals(MutationType.TNT_SPLEEF)) {
						mutation.getTNT().add(e.getBlock().getLocation());
					}
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
	
}

		
	