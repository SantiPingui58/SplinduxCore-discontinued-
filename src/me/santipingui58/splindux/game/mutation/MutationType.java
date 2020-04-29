package me.santipingui58.splindux.game.mutation;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import me.santipingui58.splindux.utils.Utils;

public enum MutationType {
	
	//User 1/0
	//VIP 1/1
	//Epic 2/3
	//Extreme 3/5
	//Valor de voto/Mutations por d§a	
	SPEED_I, //5
	SPEED_II, //7
	SPEED_V, //10
	SPEED_X, //15
	INVISIBILITY, //7
	JUMP_I, //3
	JUMP_II, //5
	JUMP_V, //7
	JUMP_X, //10
	SLOWNESS_I, //5
	SLOWNESS_II, //7
	SLOWNESS_V, //10
	SLOWNESS_X, //12
	BLINDNESS, //7
	LEVITATION_I, //5
	LEVITATION_II, //7
	LEVITATION_V, //10
	LEVITATION_X, //12
	TNT_SPLEEF, //10 Spleef con tnts
	GLOWING, //3
	NAUSEA, //10
	SPLEFF, //5
	SNOW_RUN, //5
	FISHING_ROD, //3
	ENDER_SPLEEF, //3
	BOW_SPLEEF, //3
	EXPERIENCE_SPLEEF, //3
	POT_SPLEEF, //5 //Spleef con pociones de area y normales	
	MINI_SPLEEF, //7  Lingote de hierro, mini spleef, solo 2 bolas de nieve, se rompe con la mano
	CRUMBLE_SPLEEF, //3  Semillas, crumblear la arena
	KOHI_SPLEEF, //5 Spleef con pvp y bolas de nieve con knocback
	JUMP_SPLEEF, //3 Crumble de la arena + jump boost
	CREEPY_SPLEEF; //7 Cegera, Disco 11, invisibilidad con cabezas de herobrine
	
	
	
	public int getRequiredVotes() {
		if (this.equals(JUMP_I) || this.equals(GLOWING) || this.equals(FISHING_ROD) || this.equals(ENDER_SPLEEF) 
				|| this.equals(BOW_SPLEEF) || this.equals(EXPERIENCE_SPLEEF) || this.equals(CRUMBLE_SPLEEF) || this.equals(KOHI_SPLEEF) || this.equals(CREEPY_SPLEEF)) {
			return 3;
		} else if (this.equals(SPEED_I) || this.equals(JUMP_II) || this.equals(SLOWNESS_I) || this.equals(LEVITATION_I) || this.equals(SPLEFF) || this.equals(SNOW_RUN)||
				this.equals(POT_SPLEEF) || this.equals(KOHI_SPLEEF)) {
			return 5;
		} else if (this.equals(SPEED_II) || this.equals(INVISIBILITY) || this.equals(SLOWNESS_II) || this.equals(BLINDNESS) || this.equals(LEVITATION_II) || this.equals(MINI_SPLEEF) 
				|| this.equals(CREEPY_SPLEEF)) {
			return 7;
		} else if (this.equals(SPEED_V) || this.equals(JUMP_X) || this.equals(SLOWNESS_V) || this.equals(LEVITATION_V)||this.equals(TNT_SPLEEF)) {
			return 10;
		} else if (this.equals(SLOWNESS_X) || this.equals(LEVITATION_X)) {
			return 12;
		} else if (this.equals(SPEED_X)) {
			return 15;
		}
		return 0;
	}
	
	
	public PotionType getPotionType() {
		if (this.equals(SPEED_I) || this.equals(SPEED_II) || this.equals(SPEED_V) || this.equals(SPEED_X)) {
			return PotionType.SPEED;
		} else if (this.equals(JUMP_I) || this.equals(JUMP_II) || this.equals(JUMP_V) || this.equals(JUMP_X)) {
			return PotionType.JUMP;
		} else if (this.equals(SLOWNESS_I) || this.equals(SLOWNESS_II) || this.equals(SLOWNESS_V) || this.equals(SLOWNESS_X)) {
			return PotionType.SLOWNESS;
		}  else if (this.equals(SLOWNESS_I) || this.equals(SLOWNESS_II) || this.equals(SLOWNESS_V) || this.equals(SLOWNESS_X)) {
			return PotionType.SLOWNESS;
		} else if (this.equals(LEVITATION_I) || this.equals(LEVITATION_II) || this.equals(LEVITATION_V) || this.equals(LEVITATION_X)) {
			return PotionType.SLOWNESS;
		} else if (this.equals(INVISIBILITY)) {
			return PotionType.INVISIBILITY;
		}
		return null;
	}
	
	public int getLevel() {
		if (this.equals(SPEED_I) || this.equals(JUMP_I) || this.equals(SLOWNESS_I) || this.equals(LEVITATION_I) || this.equals(INVISIBILITY)) { 
			return 1;
		} else if (this.equals(SPEED_II) || this.equals(JUMP_II) || this.equals(SLOWNESS_II) || this.equals(LEVITATION_II)) { 
			return 2;
		} else if (this.equals(SPEED_V) || this.equals(JUMP_V) || this.equals(SLOWNESS_V) || this.equals(LEVITATION_V)) { 
			return 5;
		}else if (this.equals(SPEED_X) || this.equals(JUMP_X) || this.equals(SLOWNESS_X) || this.equals(LEVITATION_X)) { 
			return 10;
		}
		return 0;
	}
	
	public ItemStack getItem() {	
		ItemStack item = null;
		if (this.equals(SPEED_I) || this.equals(SPEED_II) || this.equals(SPEED_V) || this.equals(SPEED_X)
				|| this.equals(INVISIBILITY) 
				|| this.equals(JUMP_I) || this.equals(JUMP_II) || this.equals(JUMP_V) || this.equals(JUMP_X) 
				|| this.equals(LEVITATION_I) || this.equals(LEVITATION_II) || this.equals(LEVITATION_V) || this.equals(LEVITATION_X) 
				|| this.equals(SLOWNESS_I) || this.equals(SLOWNESS_II) || this.equals(SLOWNESS_V) || this.equals(SLOWNESS_X)) {
			item = Utils.getUtils().getTippedArrow(getPotionType(), getLevel());
		} else {
		item = new ItemStack(getMaterial());
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getTitle());
		List<String> lore = getDescription();
		lore.add("");
		lore.add("§aRequiered votes to add mutation: §b" + getRequiredVotes());
		meta.setLore(getDescription());
		item.setItemMeta(meta);
		return item;
		
	}
	
	
	public int getSlot() {
		switch(this) {
		case SPEED_I: return 0;
		case SPEED_II:return 1;
		case SPEED_V: return 2;
		case SPEED_X: return 3;
		case INVISIBILITY: return 4;
		case JUMP_I: return 5;
		case JUMP_II: return 6;
		case JUMP_V: return 7;
		case JUMP_X: return 8;
		case SLOWNESS_I: return 9;
		case SLOWNESS_II: return 10;
		case SLOWNESS_V: return 11;
		case SLOWNESS_X: return 12;
		case BLINDNESS: return 13;
		case LEVITATION_I: return 14;
		case LEVITATION_II: return 15;
		case LEVITATION_V: return 16;
		case LEVITATION_X: return 17;
		case TNT_SPLEEF: return 18;
		case GLOWING: return 19;
		case NAUSEA: return 20;
		case SPLEFF: return 21;
		case SNOW_RUN: return 22;
		case FISHING_ROD: return 23;
		case ENDER_SPLEEF: return 24;
		case BOW_SPLEEF: return 25;
		case EXPERIENCE_SPLEEF: return 30;
		case POT_SPLEEF: return 26;
		case MINI_SPLEEF:return 33;
		case CRUMBLE_SPLEEF: return 29;
		case KOHI_SPLEEF: return 31;
		case JUMP_SPLEEF:return 32;
		case CREEPY_SPLEEF:return 40;	
		default: return 0;
		}
	}
	
	public List<String> getDescription() {
		List<String> list = new ArrayList<String>();		
		switch(this) {		
		case SPEED_I: list.add("§7Gives §bSpeed I §7effect to all players on the Arena"); return list;
		case SPEED_II:list.add("§7Gives §bSpeed II §7effect to all players on the Arena");return list;
		case SPEED_V:list.add("§7Gives §bSpeed V §7effect to all players on the Arena");return list;
		case SPEED_X:list.add("§7Gives §bSpeed X §7effect to all players on the Arena");return list;
		case INVISIBILITY:list.add("§7Gives §8Invisibility §7effect to all players on the Arena");return list;
		case JUMP_I: list.add("§7Gives §aJump I §7effect to all players on the Arena");return list;
		case JUMP_II:list.add("§7Gives §aJump II §7effect to all players on the Arena");return list;
		case JUMP_V:list.add("§7Gives §aJump V §7effect to all players on the Arena");return list;
		case JUMP_X:list.add("§7Gives §aJump X §7effect to all players on the Arena");return list;
		case SLOWNESS_I: list.add("§7Gives §8Slowness I §7effect to all players on the Arena");return list;
		case SLOWNESS_II:list.add("§7Gives §8Slowness II §7effect to all players on the Arena");return list;
		case SLOWNESS_V:list.add("§7Gives §8Slowness V §7effect to all players on the Arena");return list;
		case SLOWNESS_X:list.add("§7Gives §8Slowness X §7effect to all players on the Arena");return list;
		case BLINDNESS:list.add("§7Gives §8Slowness §7effect to all players on the Arena");return list;
		case LEVITATION_I: 
			list.add("§7Gives §fLevitation I §7effect for 3 seconds");
						   list.add("§7every 10 seconds to all players on the Arena");return list;
		case LEVITATION_II: 
			list.add("§7Gives §fLevitation II §7effect for 3 seconds");
		   list.add("§7every 10 seconds to all players on the Arena");return list;
		case LEVITATION_V: list.add("§7Gives §fLevitation V §7effect for 3 seconds");
		   list.add("§7every 10 seconds to all players on the Arena");return list;
		case LEVITATION_X: list.add("§7Gives §fLevitation X §7effect for 3 seconds");
		   list.add("§7every 10 seconds to all players on the Arena");return list;
		case TNT_SPLEEF:list.add("§7Tnt blocks will explode the arena.");return list;
		case GLOWING:list.add("§7Gives §eGlowing §7effect to all players on the Arena");return list;
		case NAUSEA:list.add("§7Gives §8Nausea §7effect to all players on the Arena");return list;
		case SPLEFF:list.add("§7No Shovel - Gives snowballs to all players");return list;
		case SNOW_RUN:list.add("§7Snow melts when you walk on it");return list;
		case FISHING_ROD:list.add("§7Fishing Rod instead of Shovel");return list;
		case ENDER_SPLEEF:list.add("§7Ender Pearls instead of Shovel");return list;
		case BOW_SPLEEF:list.add("§7Bow and Arrows instead of Shovel");return list;
		case EXPERIENCE_SPLEEF:list.add("§7EXP Bottles instead of Shovel");return list;
		case POT_SPLEEF:list.add("§7Potions instead of Shovel");return list;
		case MINI_SPLEEF:list.add("§7No Shovel, smaller arena");return list;
		case CRUMBLE_SPLEEF:list.add("§7Crumbled Arena");return list;
		case KOHI_SPLEEF:list.add("§7Snow blocks drop snowballs, and they do knockback");return list;
		case JUMP_SPLEEF:list.add("§7Jump Boost VII effect and arena crumbled");return list;
		case CREEPY_SPLEEF:list.add("§7Blindness and Invisibility effects, all players have Herobrina head, while Disc 11 is played");return list;
		default: break;
		
		}

		
		
		return list;
	}
	
	public String getTitle() {
		switch(this) {
		case SPEED_I: return "§bSpeed I Mutation";
		case SPEED_II:return "§bSpeed II Mutation";
		case SPEED_V: return"§bSpeed V Mutation";
		case SPEED_X: return "§bSpeed X Mutation";
		case INVISIBILITY: return "§8Invisibility Mutation";
		case JUMP_I: return "§aJump I Mutation";
		case JUMP_II: return "§aJump II Mutation";
		case JUMP_V: return "§aJump V Mutation";
		case JUMP_X: return "§aJump X Mutation";
		case SLOWNESS_I: return "§aJump I Mutation";
		case SLOWNESS_II: return "§aJump I Mutation";
		case SLOWNESS_V: return "§aJump I Mutation";
		case SLOWNESS_X: return "§8Slowness X Mutation";
		case BLINDNESS: return "§8Blindness Mutation";
		case LEVITATION_I: return "§eLevitation I Mutation";
		case LEVITATION_II: return "§eLevitation II Mutation";
		case LEVITATION_V: return "§eLevitation V Mutation";
		case LEVITATION_X: return "§eLevitation X Mutation";
		case TNT_SPLEEF: return "§cTNT Spleef Mutation";
		case GLOWING: return"§eGlowing Mutation";
		case NAUSEA: return "§8Nausea Mutation";
		case SPLEFF: return "§fSpleff Mutation";
		case SNOW_RUN: return "§eSnow Run Mutation";
		case FISHING_ROD: return "§fFishing Rod Mutation";
		case ENDER_SPLEEF: return "§3Ender Spleef Mutation";
		case BOW_SPLEEF: return "§6Bow Spleef Mutation";
		case EXPERIENCE_SPLEEF: return "§eExperience Spleef Mutation";
		case POT_SPLEEF: return "§dPot Spleef Mutation";
		case MINI_SPLEEF:return "§7Mini Spleef Mutation";
		case CRUMBLE_SPLEEF: return "§7Crumble Mutation";
		case KOHI_SPLEEF: return "§aKohi Spleef Mutation";
		case JUMP_SPLEEF:return "§9Jump Spleef Mutation";
		case CREEPY_SPLEEF:return "§0Creepy Spleef Mutation";	
		default: return null;
		}
		
	}
	
	public Material getMaterial() {
		switch(this) {
		case BLINDNESS: return Material.INK_SACK;
		case TNT_SPLEEF: return Material.TNT;
		case GLOWING:return Material.SPECTRAL_ARROW;
		case NAUSEA:return Material.ROTTEN_FLESH;
		case SPLEFF:return Material.EGG;
		case SNOW_RUN: return Material.SAND;
		case FISHING_ROD: return Material.FISHING_ROD;
		case ENDER_SPLEEF:return Material.ENDER_PEARL;
		case BOW_SPLEEF:return Material.BOW;
		case EXPERIENCE_SPLEEF: return Material.EXP_BOTTLE;
		case POT_SPLEEF: return Material.LINGERING_POTION;
		case MINI_SPLEEF: return Material.IRON_NUGGET;
		case CRUMBLE_SPLEEF: return Material.BEETROOT_SEEDS;
		case KOHI_SPLEEF: return Material.DIAMOND_SWORD;
		case JUMP_SPLEEF: return Material.SNOW_BLOCK;
		case CREEPY_SPLEEF: return Material.PUMPKIN;
		default:return null;
		}
	}
	
	
	
	
}
