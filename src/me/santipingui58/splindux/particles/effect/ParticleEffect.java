package me.santipingui58.splindux.particles.effect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;

public class ParticleEffect {

	private ParticleEffectType type;
	private boolean vip;
	private boolean epic;
	private boolean extreme;
	
	public ParticleEffect(ParticleEffectType type,boolean vip, boolean epic, boolean extreme) {
		this.type = type;
		this.vip = vip;
		this.epic = epic;
		this.extreme = extreme;
	}
	
	 
	public boolean needsVip() {
		return this.vip;
	}
	
	public boolean needsEpic() {
		return this.epic;
	}
	
	public boolean needsExtreme() {
		return this.extreme;
	}
	
	public ParticleEffectType getType() {
		return this.type;
	}


	public ItemStack getItem(boolean selected, boolean bought, boolean hasperms) {
		ItemStack item = new ItemStack(this.type.getMaterial());
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add("§8"+this.getType().toString());
		if (selected) {
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		lore.add("§aSelected!");
		meta.setDisplayName(this.getType().getName() + " Particle");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
		return item;
		}  else if (bought) {
			lore.add("§7Right click to select");
		} else {
			if (hasperms) {
				lore.add("§cYou don't have this effect");
				lore.add("§7Buy this effect for §6" + this.type.getPrize() + " Coins");
			} else {
				String rank = "§a§l[VIP]";
				if (needsEpic()) {
					rank = "§1§l[Epic]";
				} else if (needsExtreme()) {
					rank = "§5§l[Extreme]";
				}
				
				lore.add("§cYou don't have permission to buy this");
				lore.add("§cYou need atleast a " + rank + "§c rank to buy this effect");
			}
			
		}
		
		
		meta.setDisplayName(this.getType().getName() + " Particle");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}


	public ParticleStyle effectToDefaultStyles() {
		switch(getType()) {
		case DEFAULT: return DefaultStyles.NORMAL;
		case POINT: return DefaultStyles.POINT;
		case POPPER: return DefaultStyles.POPPER;
		case PULSE: return DefaultStyles.PULSE;
		case MOVE: return DefaultStyles.MOVE;
		case FEET: return DefaultStyles.FEET;
		case TRAIL: return DefaultStyles.TRAIL;
		case VORTEX: return DefaultStyles.CELEBRATION;
		case HALO: return DefaultStyles.HALO;
		case BLOCKBREAK: return DefaultStyles.BLOCKBREAK;
		case TELEPORT: return DefaultStyles.TELEPORT;
		case BATMAN: return DefaultStyles.BATMAN;
		case WINGS: return DefaultStyles.WINGS;
		case INVOCATION: return DefaultStyles.INVOCATION;
		default:break;		
		}
		return null;
	}
	
}
