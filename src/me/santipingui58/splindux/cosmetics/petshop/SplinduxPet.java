package me.santipingui58.splindux.cosmetics.petshop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;

public class SplinduxPet {

	private String id;
	private String name;
	private PetSpawnType type;
	private int price;
	private boolean vip;
	private boolean epic;
	private boolean extreme;
	private String spawn;
	public SplinduxPet(String id, String name,PetSpawnType type, int price, boolean vip, boolean epic, boolean extreme, String spawn) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
		this.vip = vip;
		this.epic = epic;
		this.extreme = extreme;
		this.spawn = spawn;
	}
	
	public String getID() {
		return this.id;
	}
	public String getName() {
		return ChatColor.translateAlternateColorCodes('&', this.name);
	}
	
	public PetSpawnType getType() {
		return this.type;
	}
	
	public int getPrice() {
		return this.price;
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
	
	public ItemStack getItem(SpleefPlayer sp) {
		Player p = sp.getPlayer();
		ItemStack item = new ItemStack(Material.STONE);
		if (type.equals(PetSpawnType.HEAD)) {
			 item = Utils.getUtils().getSkull(spawn,"");     
		} else if (type.equals(PetSpawnType.BLOCK)) {
			Material material = Material.valueOf(spawn);
			ItemStack item2 = new ItemStack(material);
			ItemMeta meta = item2.getItemMeta();
		    item2.setItemMeta(meta);
			item = item2;
		} else if (type.equals(PetSpawnType.EGG)) {
			ItemStack item2 = new ItemStack(Material.MONSTER_EGG);
			SpawnEggMeta meta = (SpawnEggMeta) item2.getItemMeta();			
			meta.setSpawnedType(EntityType.valueOf(spawn));
			item2.setItemMeta(meta);
			item = item2;
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		List<String> lore = new ArrayList<String>();
		if (sp.getPlayer().hasPermission("splindux.pet."+this.id)) {
			lore.add("§aYou already have " + getName() + " §a!");
			lore.add("§7Use §b/pet §7to choose this pet");
		} else if ( (needsVip() && !p.hasPermission("splindux.vip")) ||   
					(needsEpic() && !p.hasPermission("splindux.epic")) ||
					(needsExtreme() && !p.hasPermission("splindux.extreme"))) {
			String rank = "§a§l[VIP]";
			if (needsEpic()) {
				rank = "§1§l[Epic]";
			} else if (needsExtreme()) {
				rank = "§5§l[Extreme]";
			}		
			lore.add("§cYou don't have permission to buy this");
			lore.add("§cYou need atleast a " + rank + "§c rank to buy this pet");
		} else {
			lore.add("§cYou don't have this pet");
			lore.add("§7Buy this type for §6" + this.getPrice() + " Coins");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
