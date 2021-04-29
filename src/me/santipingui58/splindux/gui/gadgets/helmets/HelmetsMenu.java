package me.santipingui58.splindux.gui.gadgets.helmets;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.cosmetics.helmets.Helmet;
import me.santipingui58.splindux.cosmetics.helmets.HelmetManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.gadgets.GadgetsMenu;
import me.santipingui58.splindux.utils.ItemBuilder;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class HelmetsMenu extends MenuBuilder {

	
	public HelmetsMenu(SpleefPlayer sp) {
		super("§6§lIn Game Helmets",6);
		
		
		new BukkitRunnable() {
		public void run() {
			
		
		int i = 0;
		for (Helmet helmet : HelmetManager.getManager().getHelmets()) {
			s(i,helmet.getItem(sp, false));
			i++;			
		}
		
		s(53, new ItemBuilder(Material.ARROW).setTitle("§cGo Back").build());
		if (sp.getHelmet()==null) {
			s(49,new ItemBuilder(Material.GLASS).setTitle("§cNone").addEnchantment(Enchantment.ARROW_FIRE, 1).build());
			} else {
				s(49,new ItemBuilder(Material.GLASS).setTitle("§cNone").build());
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
		Player p = sp.getPlayer();
		if (slot==53) {
			new GadgetsMenu(sp).o(p);
			return;
		} 
		
		if (slot==49) {
			sp.setHelmet(null);
			sp.getPlayer().sendMessage("§aYou have selected the helmet §cNONE");
			if (sp.isInArena()) {
				sp.getPlayer().getInventory().setHelmet(null);
			}
			new HelmetsMenu(sp).o(sp.getPlayer());
		}
	if (stack.getItemMeta().getLore()!=null) {
		Helmet helmet = HelmetManager.getManager().getHelmetByName(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
		if (sp.getPlayer().hasPermission("splindux.helmet."+helmet.getId().toString().toLowerCase())) {
			sp.setHelmet(helmet.getId());
			sp.getPlayer().sendMessage("§aYou have selected the helmet " + helmet.getName());
			if (sp.isInArena()) {
				sp.getPlayer().getInventory().setHelmet(helmet.getItem(sp, true));
			}
			new HelmetsMenu(sp).o(sp.getPlayer());
		} else if (helmet.needsEpic() || helmet.needsExtreme() || helmet.needsVip()) {
			if ((!p.hasPermission("splindux.extreme") && helmet.needsExtreme()) || 
			(!p.hasPermission("splindux.epic") && helmet.needsEpic())|| 
			(!p.hasPermission("splindux.vip") && helmet.needsVip())) {
				p.closeInventory();
				p.sendMessage("§cYou don't have permission to purchase this helmet.");
				 p.sendMessage("§aVisit the store for more info: §bhttp://store.splindux.com/");	
			} else {
				buy(sp,helmet);
			}
		} else {
			buy(sp,helmet);
		}
	}
	}
	
	private void buy(SpleefPlayer sp,Helmet helmet) {
		Player p = sp.getPlayer();
		if (sp.getCoins()>=helmet.getPrice()) {
			sp.removeCoins(helmet.getPrice());
			p.sendMessage("§aYou have bought the effect " + helmet.getName() +"§a!");
			sp.setHelmet(helmet.getId());
			PermissionsEx.getUser(sp.getPlayer()).addPermission("splindux.helmet."+helmet.getId().toString().toLowerCase());
			new HelmetsMenu(sp).o(p);
		} else {
			p.closeInventory();
			p.sendMessage("§cYou don't have enough coins to purchase this item.");
		}
	}
	
	
	}



