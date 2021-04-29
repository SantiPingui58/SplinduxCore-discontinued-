package me.santipingui58.splindux.cosmetics.helmets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class Helmet {

	
	private String id;
	private String name;
	private int price;
	private int permsLevel;
	private Material material;
	private byte b;
	private boolean bold;
	private boolean enchanted;
	public Helmet(String id, String name, int permsLevel,int price,Material material, byte b,String perm, boolean bold, boolean enchanted) {
	this.id = id;
	this.name = name;
	this.permsLevel = permsLevel;
	this.price = price;
	this.material = material;
	this.bold = bold;
	this.enchanted = enchanted;
	this.b = b;
	}
	
	public byte getByte() {
		return this.b;
	}
	
	public boolean isBold() {
		return this.bold;
	}
	
	public boolean isEnchanted() {
		return this.enchanted;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public boolean needsVip() {
		return permsLevel==1;
	}
	public boolean needsEpic() {
		return permsLevel==2;
	}
	public boolean needsExtreme() {
		return permsLevel==3;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	
	public ItemStack getItem(SpleefPlayer sp,boolean wearing) {
		ItemStack item = new ItemStack(material,1,b);
		ItemMeta meta = item.getItemMeta();
		String name = bold ? "§l"+this.name : this.name;
		meta.setDisplayName(name);
		List<String> lore = new ArrayList<String>();
		lore.add("§8"+this.id);
		if (sp.getPlayer().hasPermission("splindux.helmet."+id.toLowerCase())) {
			if (sp.getHelmet()!=null && sp.getHelmet().equals(this)) {
				lore.add("§bHelmet selected!");		
			} else {
				lore.add("§aRight click to select this Helmet");
			}
		}
		else {
			Player p = sp.getPlayer();
			if ((needsEpic() && !p.hasPermission("splindux.epic")) || (needsExtreme() && !p.hasPermission("splindux.extreme"))
					|| (needsVip() && !p.hasPermission("splindux.vip"))) {
				String rank = "§aVIP";
				switch(this.permsLevel) {
				case 2: rank = "§1Epic"; break;
				case 3: rank = "§5Extreme"; break;
				}
				
				lore.add("§cYou don't have permission to buy this");
				lore.add("§cYou need atleast a " + rank + "§c rank to buy this effect");
			} else {
				lore.add("§cYou don't have this helmet");
				lore.add("§7Buy this helmet for §6" + getPrice() + " Coins");
			}	
		}
		
		if (wearing) {
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);	
			item.setItemMeta(meta);		
			if (sp.getHelmet()!=null && sp.getHelmet().equals(this) && sp.getPlayer().hasPermission("splindux.vip")) item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
			
		} else {
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);	
			meta.setLore(lore);
			item.setItemMeta(meta);	
			if (sp.getHelmet()!=null && sp.getHelmet().equals(this)) item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
		
		}
		
		
		
		return item;
	}
	
	
}
