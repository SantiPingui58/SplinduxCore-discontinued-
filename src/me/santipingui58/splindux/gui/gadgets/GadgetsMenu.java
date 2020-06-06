package me.santipingui58.splindux.gui.gadgets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.gadgets.particles.ParticleEffectsMenu;
import me.santipingui58.splindux.gui.gadgets.particles.ParticleTypesMenu;
import me.santipingui58.splindux.gui.gadgets.pets.PetMainMenu;
import me.santipingui58.splindux.utils.ItemBuilder;

public class GadgetsMenu extends MenuBuilder {

	
	public GadgetsMenu(SpleefPlayer sp) {
		super("§6Gadgets",3);
		if (sp.isInArena()) {
			s(11, new ItemBuilder(Material.GLOWSTONE_DUST).setTitle("§aIn Game Particle Effects").build());
			s(15, new ItemBuilder(Material.DRAGONS_BREATH).setTitle("§bIn Game Particle Types").build());
		} else {
		s(10, new ItemBuilder(Material.GLOWSTONE_DUST).setTitle("§aIn Game Particle Effects").build());
		s(12, new ItemBuilder(Material.DRAGONS_BREATH).setTitle("§bIn Game Particle Types").build());
		s(14, new ItemBuilder(Material.CHEST).setTitle("§6Lobby Gadgets").build());
		s(16,new ItemBuilder(Material.BONE).setTitle("§fPet Blocks").build());
	}
	}


	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
	if (slot==10 || slot==11) {
		new ParticleEffectsMenu(sp).o(sp.getPlayer());	
		
	}else if (slot==12 || slot==15) {
		new ParticleTypesMenu(sp).o(sp.getPlayer());	
		
	} else if (slot==14) {
		GadgetsMenuAPI.goBackToMainMenu(sp.getPlayer());
	} else if (slot==16) {
		new PetMainMenu(sp).o(sp.getPlayer());
		
	}
	
	}
	
	
	}



