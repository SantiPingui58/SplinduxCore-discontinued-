package me.santipingui58.splindux.particles.type;

import org.bukkit.Material;

public enum ParticleTypeSubType {

	
	BLOCK,
	DUST,
	DRAGON_BREATH,
	CLOUD,
	DRIPPING_LAVA,
	DRIPPING_WATER,
	FALLING_DUST,
	SLIME,
	SNOWBALL,
	CRIT,
	ENCHANTMENT,
	END_ROD,
	DAMAGE_INDICATOR,
	ENCHANTED_HIT,
	LARGE_SMOKE,
	SPIT,
	SWEEP_ATTACK,
	ANGRY_VILLAGER,
	FIREWORK,
	SMOKE,
	HAPPY_VILLAGER,
	PORTAL,
	FLAME
	,HEARTS,
	NOTE,
	TOTEM_OF_UNDYING,
	LAVA;
	
	
	
	public Material getMaterial() {
		switch(this) {
		default:break;
		case ANGRY_VILLAGER: return Material.WOOD_DOOR;
		case BLOCK: return Material.STONE;
		case CLOUD : return Material.WOOL;
		case CRIT: return Material.IRON_SWORD;
		case DAMAGE_INDICATOR: return Material.SKULL_ITEM;
		case DRIPPING_LAVA: return Material.LAVA_BUCKET;
		case DRIPPING_WATER: return Material.WATER_BUCKET;
		case DUST: return Material.REDSTONE;
		case ENCHANTMENT: return Material.ENCHANTMENT_TABLE;
		case ENCHANTED_HIT: return Material.DIAMOND_SWORD;
		case END_ROD: return Material.END_ROD;
		case FALLING_DUST: return Material.SAND;
		case FIREWORK: return Material.FIREWORK;
		case FLAME: return Material.BLAZE_POWDER;
		case HAPPY_VILLAGER: return Material.EMERALD;
		case HEARTS: return Material.RED_ROSE;
		case SLIME: return Material.SLIME_BALL;
		case SNOWBALL: return Material.SNOW_BALL;
		case LARGE_SMOKE: return Material.WEB;
		case LAVA: return Material.MAGMA;
		case NOTE: return Material.NOTE_BLOCK;
		case PORTAL: return Material.OBSIDIAN;
		case SMOKE: return Material.FIREWORK_CHARGE;
		case SPIT: return Material.PUMPKIN_SEEDS;
		case SWEEP_ATTACK: return Material.GOLD_SWORD;
		case TOTEM_OF_UNDYING: return Material.TOTEM;
		case DRAGON_BREATH: return Material.DRAGONS_BREATH;
		}
		return null;
	}
	
	
	public int getPrize() {
		switch(this) {
		default: return 0;
		case BLOCK: return 2500;
		case DUST: return 2500;
		case DRAGON_BREATH: return 3500;
		case CLOUD: return 3500;
		case DRIPPING_LAVA: return 3500;
		case DRIPPING_WATER: return 3500;
		case FALLING_DUST: return 3500;
		case SLIME: return 3500;
		case SNOWBALL: return 3500;
		case CRIT: return 5000;
		case ENCHANTMENT: return 5000;
		case END_ROD: return 5000;
		case  DAMAGE_INDICATOR: return 7500;
		case ENCHANTED_HIT: return 7500;
		case LARGE_SMOKE: return 7500;
		case SPIT: return 7500;
		case SWEEP_ATTACK: return 7500;
		case ANGRY_VILLAGER: return 10000;
		case FIREWORK: return 10000;
		case SMOKE: return 10000;
		case HAPPY_VILLAGER: return 12500;
		case PORTAL: return 12500;
		case FLAME: return 15000;
		case HEARTS: return 15000;
		case NOTE: return 15000;
		case TOTEM_OF_UNDYING: return 15000;
		case LAVA: return 20000;
	}
	}
	
	public String getName() {
		String name = this.toString().replaceAll("\\d","");
		name = name.toLowerCase();
		name = name.replace("_"," ");
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		switch(this) {
		default: return "";
		case BLOCK: return "§8" +name;
		case DUST: return "§e" +name;
		case CLOUD: return "§f" +name;
		case DRAGON_BREATH: return "§d" +name;
		case DRIPPING_LAVA: return "§c" +name;
		case DRIPPING_WATER: return "§9" +name;
		case FALLING_DUST: return "§7" +name;
		case SLIME:return "§a" +name;
		case SNOWBALL: return "§7" +name;
		case CRIT: return "§5" +name;
		case ENCHANTMENT:return "§d" +name;
		case END_ROD: return "§e" +name;
		case  DAMAGE_INDICATOR: return "§8" +name;
		case ENCHANTED_HIT: return "§d" +name;
		case LARGE_SMOKE:return "§8" +name;
		case SPIT: return "§3" +name;
		case SWEEP_ATTACK:return "§3" +name;
		case ANGRY_VILLAGER: return "§c" +name;
		case FIREWORK: return "§7" +name;
		case SMOKE: return "§7" +name;
		case HAPPY_VILLAGER:return "§2" +name;
		case PORTAL: return "§5" +name;
		case FLAME: return "§4" +name;
		case HEARTS: return "§c" +name;
		case NOTE: return "§6" +name;
		case TOTEM_OF_UNDYING: return "§6" +name;
		case LAVA: return "§4" +name;
		}
		
	}
}
