package me.santipingui58.splindux.gui.gadgets.pets;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.cosmetics.petshop.PetShopManager;
import me.santipingui58.splindux.cosmetics.petshop.SplinduxPet;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class PetShopMenu extends MenuBuilder {

	
	public PetShopMenu(SpleefPlayer sp) {
		super("§9Pet Store",6);
		
		new BukkitRunnable() {
		public void run() {
			
		int pos = 0;
		for (SplinduxPet pet : PetShopManager.getManager().getPets()) {
			s(pos,pet.getItem(sp));
			pos++;
		}
		
		s(49, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
		s(53, new ItemBuilder(Material.ARROW).setTitle("§cPage 2")
		.addLore("§cLOCKED")
		.addLore("§7The next page will be unlocked when")
		.addLore("§7all current pets are bought atleast once for")
		.addLore("§7any player of the community.")
		.addLore("")
		.addLore("§bCommunity Goal: §a" + PetShopManager.getManager().getBoughPets().size()+"/"+PetShopManager.getManager().getPets().size())
		.addLore(PetShopManager.getManager().getCommunityGoalPercentage())
		.addLore(PetShopManager.getManager().getBar())
		.build());
		
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
		Player p  = sp.getPlayer();
		if (slot==49) {
			new PetMainMenu(sp).o(p);
			return;
		}
		
		
		
		if (slot==53) return;
		
			SplinduxPet pet = PetShopManager.getManager().getPetByName(stack.getItemMeta().getDisplayName());
			
		if (p.getPlayer().hasPermission("splindux.pet."+pet.getID())) {
			
			
		} else if ( (pet.needsVip() && !p.hasPermission("splindux.vip")) ||   
				(pet.needsEpic() && !p.hasPermission("splindux.epic")) ||
				(pet.needsExtreme() && !p.hasPermission("splindux.extreme"))) {
	
			p.closeInventory();
			p.sendMessage("§cYou don't have permission to purchase this pet.");
			 p.sendMessage("§aVisit the store for more info: §bhttp://store.splindux.com/");	
		} else {
			if (sp.getCoins()>=pet.getPrice()) {
				sp.removeCoins(pet.getPrice());
				PetShopManager.getManager().boughtPet(pet.getID());
				p.sendMessage("§aYou have bought the pet " + pet.getName()+"§a!");
				
				if (!p.hasPermission("splindux.pet")) {
					PermissionsEx.getUser(sp.getPlayer()).addPermission("splindux.pet");
				}
				PermissionsEx.getUser(sp.getPlayer()).addPermission("splindux.pet."+pet.getID());
				new PetShopMenu(sp).o(p);
			} else {
				p.closeInventory();
				p.sendMessage("§cYou don't have enough coins to purchase this pet.");
			}
		}
	
	
	}
	}



