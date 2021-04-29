package me.santipingui58.splindux.gui.gadgets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import be.isach.ultracosmetics.menu.Menus;
import be.isach.ultracosmetics.player.UltraPlayer;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.gadgets.helmets.HelmetsMenu;
import me.santipingui58.splindux.gui.gadgets.particles.ParticleEffectsMenu;
import me.santipingui58.splindux.gui.gadgets.particles.ParticleTypesMenu;
import me.santipingui58.splindux.gui.gadgets.pets.PetMainMenu;
import me.santipingui58.splindux.utils.ItemBuilder;

public class GadgetsMenu extends MenuBuilder {

	
	public GadgetsMenu(SpleefPlayer sp) {
		super("§6Gadgets",3);
		
		new BukkitRunnable() {
		public void run() {
			
		if (sp.isInArena()) {
			s(11, new ItemBuilder(Material.GLOWSTONE_DUST).setTitle("§aIn Game Particle Effects").build());
			s(13,new ItemBuilder(Material.GOLD_HELMET).setTitle("§eIn Game Helmets").build());
			s(15, new ItemBuilder(Material.DRAGONS_BREATH).setTitle("§bIn Game Particle Types").build());
		} else {
		s(11, new ItemBuilder(Material.GLOWSTONE_DUST).setTitle("§aIn Game Particle Effects").build());
		s(12, new ItemBuilder(Material.DRAGONS_BREATH).setTitle("§bIn Game Particle Types").build());
		s(13, new ItemBuilder(Material.CHEST).setTitle("§6Lobby Gadgets").build());
		s(14,new ItemBuilder(Material.BONE).setTitle("§fPet Blocks").build());
		s(15,new ItemBuilder(Material.GOLD_HELMET).setTitle("§eIn Game Helmets").build());
	}
		new BukkitRunnable() {
		public void run() {
			buildInventory();
		}
		}.runTask(Main.get());
		
		}
		}.runTaskAsynchronously(Main.get());
	}


	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		
		
		if (sp.isInArena()) {
			if (slot==11) new ParticleEffectsMenu(sp).o(sp.getPlayer());	
			if (slot==13) new HelmetsMenu(sp).o(sp.getPlayer());	
			
			if (slot==15) new ParticleTypesMenu(sp).o(sp.getPlayer());	
		} else {
			if (slot==11) new ParticleEffectsMenu(sp).o(sp.getPlayer());	
			if (slot==12) new ParticleTypesMenu(sp).o(sp.getPlayer());	
			if (slot==13) {
				  Menus menus = Main.ultraCosmetics.getMenus();
			        UltraPlayer ultraPlayer = Main.ultraCosmetics.getPlayerManager().getUltraPlayer(sp.getPlayer());
			        menus.getMainMenu().open(ultraPlayer);
			}
			if (slot==14) new PetMainMenu(sp).o(sp.getPlayer());
			if (slot==15) new HelmetsMenu(sp).o(sp.getPlayer());
		}
	
	}
	
	
	}



