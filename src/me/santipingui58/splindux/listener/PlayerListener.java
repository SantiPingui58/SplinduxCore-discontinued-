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
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.death.SpleefKill;
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
			if (GameManager.getManager().isInGame(sp)) {
				if (GameManager.getManager().getArenaByPlayer(sp).getState().equals(GameState.GAME)) {
				if (!e.getBlock().getType().equals(Material.SNOW_BLOCK)) {
					e.setCancelled(true);
				} else {
					SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
					arena.getKills().add(new SpleefKill(e.getBlock().getLocation(),sp,DeathReason.SPLEEFED));
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
