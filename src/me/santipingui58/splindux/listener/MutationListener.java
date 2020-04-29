package me.santipingui58.splindux.listener;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.translate.Main;


public class MutationListener implements Listener {

	@EventHandler
	public void onTNTExplode(EntityExplodeEvent e) {
		if (e.getLocation().getWorld().getName().equalsIgnoreCase("arenas")) {
		if (e.getEntityType().equals(EntityType.PRIMED_TNT)) {
			 for (Block block : new ArrayList<Block>(e.blockList()))
		            if(!block.getType().equals(Material.SNOW_BLOCK)) {
		                e.blockList().remove(block);
		            }
		}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType().equals(Material.TNT)) {
			 e.getPlayer().getWorld().spawn(e.getBlock().getLocation(), TNTPrimed.class);
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onPotionArea(ProjectileHitEvent e) {
		if (e.getHitBlock().getLocation().getWorld().getName().equalsIgnoreCase("arenas")) {
		if (e.getEntityType().equals(EntityType.LINGERING_POTION)) {
			new BukkitRunnable() {

				@Override
				public void run() {
					Utils.getUtils().cylinder(e.getHitBlock().getLocation(), Material.AIR, 3);
					
				}
				
			}.runTaskLater(Main.get(), 30L);
		}
		
	}
	
}
	
}
