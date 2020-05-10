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
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.BreakReason;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class PlayerListener implements Listener {

	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
	
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		  if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
 			 e.setCancelled(true);
				 return;	 
		 }
		  
		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			if (sp.isInGame()) {
				if (sp.getArena().getState().equals(GameState.GAME)) {
				if (!e.getBlock().getType().equals(Material.SNOW_BLOCK) || e.getBlock().getType().equals(Material.TNT)) {
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
					
					SpleefArena arena = sp.getArena();
					BrokenBlock kill = new BrokenBlock(sp,e.getBlock().getLocation(),BreakReason.SHOVEL);
					arena.getBrokenBlocks().add(kill);
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
		
		  if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
 			 e.setCancelled(true);
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
