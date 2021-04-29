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
import me.santipingui58.splindux.cosmetics.particles.effect.ParticleEffect;
import me.santipingui58.splindux.cosmetics.particles.effect.ParticleEffectType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.gadgets.GadgetsMenu;
import me.santipingui58.splindux.utils.ItemBuilder;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ParticleEffectsMenu extends MenuBuilder {

	
	public ParticleEffectsMenu(SpleefPlayer sp) {
		super("§2§lIn Game Particle Effects",3);
		
		new BukkitRunnable() {
		public void run() {
			
		List<String> names = new ArrayList<String>();
		
		for (ParticleEffect effect : ParticleManager.getManager().getEffects()) {			
			names.add(effect.getType().toString());
		}
		
		
		int i = 0;
		for (String s : names) {
			ParticleEffect effect = ParticleManager.getManager().getEffectByType(ParticleEffectType.valueOf(s));
			boolean selected = false;
			boolean bought = false;
			boolean hasperms = true;
			
			if (sp.getParticleEffect()!=null && effect.getType().equals(sp.getParticleEffect())) {
			selected = true;			
			}
			if (sp.getPlayer().hasPermission("splindux.effect."+effect.getType().toString().toLowerCase())) {
				bought = true;
			}
			if ((effect.needsEpic() && !sp.getPlayer().hasPermission("splindux.epic")) || (effect.needsExtreme() && !sp.getPlayer().hasPermission("splindux.extreme"))
					|| (effect.needsVip() && !sp.getPlayer().hasPermission("splindux.vip"))) {
				hasperms =false;
			}
			
			s(i,effect.getItem(selected, bought, hasperms));
			if (i==8 || i==12) {
				i = i+2; 
			} else {
			i++;
			}
		}
		
		s(26, new ItemBuilder(Material.ARROW).setTitle("§cGo Back").build());
		if (sp.getParticleEffect()==null) {
			s(22,new ItemBuilder(Material.GLASS).setTitle("§cNone").addEnchantment(Enchantment.ARROW_FIRE, 1).build());
			} else {
				s(22,new ItemBuilder(Material.GLASS).setTitle("§cNone").build());
			}
		s(18,new ItemBuilder(Material.REDSTONE_TORCH_ON).setTitle("§aInformation")
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
		if (slot==18) return;
		if (slot==26) {
			new GadgetsMenu(sp).o(p);
			return;
		} 
		
		if (slot==22) {
			sp.setParticleEffect(null);
			sp.getPlayer().sendMessage("§aYou have selected the effect §cNONE");
			new ParticleEffectsMenu(sp).o(sp.getPlayer());
		}
	if (stack.getItemMeta().getLore()!=null) {
		ParticleEffectType type = ParticleEffectType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
		ParticleEffect effect = ParticleManager.getManager().getEffectByType(type);
		if (sp.getPlayer().hasPermission("splindux.effect."+effect.getType().toString().toLowerCase())) {
			sp.setParticleEffect(effect.getType());
			sp.getPlayer().sendMessage("§aYou have selected the effect " + type.getName());
			new ParticleEffectsMenu(sp).o(sp.getPlayer());
		} else if (effect.needsEpic() || effect.needsExtreme() || effect.needsVip()) {
			if ((!p.hasPermission("splindux.extreme") && effect.needsExtreme()) || 
			(!p.hasPermission("splindux.epic") && effect.needsEpic())|| 
			(!p.hasPermission("splindux.vip") && effect.needsVip())) {
				p.closeInventory();
				p.sendMessage("§cYou don't have permission to purchase this effect.");
				 p.sendMessage("§aVisit the store for more info: §bhttp://store.splindux.com/");	
			} else {
				buy(sp,effect,type);
			}
		} else {
			buy(sp,effect,type);
		}
	}
	}
	
	private void buy(SpleefPlayer sp,ParticleEffect effect,ParticleEffectType type) {
		Player p = sp.getPlayer();
		if (sp.getCoins()>=effect.getType().getPrize()) {
			sp.removeCoins(effect.getType().getPrize());
			p.sendMessage("§aYou have bought the effect " + type.getName() +"§a!");
			sp.setParticleEffect(type);
			PermissionsEx.getUser(sp.getPlayer()).addPermission("splindux.effect."+effect.getType().toString().toLowerCase());
			new ParticleEffectsMenu(sp).o(p);
		} else {
			p.closeInventory();
			p.sendMessage("§cYou don't have enough coins to purchase this item.");
		}
	}
	
	
	}



