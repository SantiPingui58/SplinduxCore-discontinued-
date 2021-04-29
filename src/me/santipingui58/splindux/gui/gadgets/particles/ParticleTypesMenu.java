package me.santipingui58.splindux.gui.gadgets.particles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.cosmetics.particles.ParticleManager;
import me.santipingui58.splindux.cosmetics.particles.type.ParticleType;
import me.santipingui58.splindux.cosmetics.particles.type.ParticleTypeSubType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.gadgets.GadgetsMenu;
import me.santipingui58.splindux.utils.ItemBuilder;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ParticleTypesMenu extends MenuBuilder {

	
	public ParticleTypesMenu(SpleefPlayer sp) {
		super("§2§lIn Game Particle Types",4);
		
		new BukkitRunnable() {
		public void run() {
			
		List<String> names = new ArrayList<String>();
		
		for (ParticleType type : ParticleManager.getManager().getTypes()) {			
			names.add(type.getType().toString());
		}
		
		
		int i = 0;
		for (String s : names) {
			ParticleType type = ParticleManager.getManager().getTypeBySubType(ParticleTypeSubType.valueOf(s));
			boolean selected = false;
			boolean bought = false;
			boolean hasperms = true;
			
			if (sp.getParticleType()!=null && type.getType().equals(sp.getParticleType())) {
			selected = true;			
			}
			if (sp.getPlayer().hasPermission("splindux.type."+type.getType().toString().toLowerCase())) {
				bought = true;
			}
			if ((type.needsEpic() && !sp.getPlayer().hasPermission("splindux.epic")) || (type.needsExtreme() && !sp.getPlayer().hasPermission("splindux.extreme"))
					|| (type.needsVip() && !sp.getPlayer().hasPermission("splindux.vip"))) {
				hasperms =false;
			}
			
			s(i,type.getItem(selected, bought, hasperms));
			i++;
			}
		
		s(35, new ItemBuilder(Material.ARROW).setTitle("§cGo Back").build());
		if (sp.getParticleType()==null) {
		s(31,new ItemBuilder(Material.GLASS).setTitle("§cNone").addEnchantment(Enchantment.ARROW_FIRE, 1).build());
		} else {
			s(31,new ItemBuilder(Material.GLASS).setTitle("§cNone").build());
		}
		
		s(27,new ItemBuilder(Material.REDSTONE_TORCH_ON).setTitle("§aInformation")
				.addLore("§7You need to have a §bParticle Type ")
				.addLore("§cAND §7a §aParticle Effect §7to able to")
				.addLore("§7display your particles.")
				.addLore("")
				.addLore("§7Particles will only be")
				.addLore("§7displayed in FFA & Duel Games.").build());
		
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
		if (slot==27) return;
		if (slot==35) {
			new GadgetsMenu(sp).o(p);
			return;
		} 
		if (slot==31) {
			sp.setParticleTypeSubType(null);
			sp.getPlayer().sendMessage("§aYou have selected the type §cNONE");
			new ParticleTypesMenu(sp).o(sp.getPlayer());
		}
	if (stack.getItemMeta().getLore()!=null) {
		ParticleTypeSubType subtype = ParticleTypeSubType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
		ParticleType type = ParticleManager.getManager().getTypeBySubType(subtype);
		if (sp.getPlayer().hasPermission("splindux.type."+type.getType().toString().toLowerCase())) {
			sp.setParticleTypeSubType(type.getType());
			sp.getPlayer().sendMessage("§aYou have selected the type " + subtype.getName());
			new ParticleTypesMenu(sp).o(sp.getPlayer());
		} else if (type.needsEpic() || type.needsExtreme() || type.needsVip()) {
			if ((!p.hasPermission("splindux.extreme") && type.needsExtreme()) || 
			(!p.hasPermission("splindux.epic") && type.needsEpic())|| 
			(!p.hasPermission("splindux.vip") && type.needsVip())) {
				p.closeInventory();
				p.sendMessage("§cYou don't have permission to purchase this type.");
				 p.sendMessage("§aVisit the store for more info: §bhttp://store.splindux.com/");	
			}
		} else {
			if (sp.getCoins()>=type.getType().getPrize()) {
				sp.removeCoins(type.getType().getPrize());
				p.sendMessage("§aYou have bought the type " + subtype.getName() +"§a!");
				sp.setParticleTypeSubType(type.getType());
				PermissionsEx.getUser(sp.getPlayer()).addPermission("splindux.type."+type.getType().toString().toLowerCase());
				new ParticleTypesMenu(sp).o(p);
			} else {
				p.closeInventory();
				p.sendMessage("§cYou don't have enough coins to purchase this item.");
			}
		}
	}
	}
	
	
	}



