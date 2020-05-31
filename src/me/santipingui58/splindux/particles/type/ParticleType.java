package me.santipingui58.splindux.particles.type;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.esophose.playerparticles.particles.ParticleEffect;

public class ParticleType {

	private ParticleTypeSubType type;
	private boolean vip;
	private boolean epic;
	private boolean extreme;
	
	
	public ParticleType(ParticleTypeSubType type, boolean vip, boolean epic, boolean extreme) {
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
	
	public ParticleTypeSubType getType() {
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
		meta.setDisplayName(this.getType().getName() + " Type");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
		return item;
		}  else if (bought) {
			lore.add("§7Right click to select");
		} else {
			if (hasperms) {
				lore.add("§cYou don't have this type");
				lore.add("§7Buy this type for §6" + this.type.getPrize() + " Coins");
			} else {
				String rank = "§a§l[VIP]";
				if (needsEpic()) {
					rank = "§1§l[Epic]";
				} else if (needsExtreme()) {
					rank = "§5§l[Extreme]";
				}
				
				lore.add("§cYou don't have permission to buy this");
				lore.add("§cYou need atleast a " + rank + "§c rank to buy this type");
			}
			
		}
		
		meta.setDisplayName(this.getType().getName() + " Type");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

		
	public ParticleEffect typeToEffect() {
		
		ParticleTypeSubType s = this.getType();
		if (s.equals(ParticleTypeSubType.SLIME) || s.equals(ParticleTypeSubType.SNOWBALL) || s.equals(ParticleTypeSubType.ENCHANTMENT) ||  s.equals(ParticleTypeSubType.HEARTS)) {
			switch(s) {
			case SLIME: return ParticleEffect.ITEM_SLIME;
			case SNOWBALL: return ParticleEffect.ITEM_SNOWBALL;
			case ENCHANTMENT: return ParticleEffect.ENCHANT;
			case HEARTS: return ParticleEffect.HEART;
			default:break;
			}
		} else {
		return ParticleEffect.valueOf(this.getType().toString());
		}
		
		
		return null;
	}
	
}
