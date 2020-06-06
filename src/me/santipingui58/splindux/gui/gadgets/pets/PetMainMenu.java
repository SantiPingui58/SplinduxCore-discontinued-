package me.santipingui58.splindux.gui.gadgets.pets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.shynixn.petblocks.api.PetBlocksApi;
import com.github.shynixn.petblocks.api.business.service.GUIService;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.gadgets.GadgetsMenu;
import me.santipingui58.splindux.utils.ItemBuilder;


public class PetMainMenu extends MenuBuilder {

	
	public PetMainMenu(SpleefPlayer sp) {
		super("§9Pet Main Menu",3);
		
		s(11, new ItemBuilder(Material.EMERALD).setTitle("§2Pet Store").build());
		s(15,new ItemBuilder(Material.BONE).setTitle("§fPet Menu")
				.addLore("§7You can also access this")
				.addLore("§7menu using §b/pet").build());
		s(26, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
					}
	


	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
	if (slot==11) {
		new PetShopMenu(sp).o(sp.getPlayer());
	} else if (slot==15) {
		GUIService service = PetBlocksApi.INSTANCE.resolve(GUIService.class);
		service.open(sp.getPlayer(), "gui.main");
	} else if (slot==26) {
		new GadgetsMenu(sp).o(sp.getPlayer());
	}
	}
	}



