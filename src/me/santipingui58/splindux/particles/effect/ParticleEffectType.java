package me.santipingui58.splindux.particles.effect;

import org.bukkit.Material;

public enum ParticleEffectType {

	
	DEFAULT,
	POINT,
	POPPER,
	PULSE,
	MOVE,
	FEET,
	TRAIL,  //VIP
	VORTEX,
	CELEBRATION,
	HALO,
	BLOCKBREAK, //VIP
	TELEPORT, //Epic
	BATMAN, //Extreme
	WINGS, //VIP
	INVOCATION; //Extreme
	
	
	
	public Material getMaterial() {
		switch(this) {
		default:break;
		case BATMAN: return Material.FLINT;
		case BLOCKBREAK: return Material.IRON_SPADE;
		case CELEBRATION : return Material.FIREWORK;
		case FEET: return Material.RABBIT_FOOT;
		case HALO: return Material.ENDER_PORTAL_FRAME;
		case INVOCATION: return Material.EYE_OF_ENDER;
		case MOVE: return Material.ARMOR_STAND;
		case DEFAULT: return Material.STONE;
		case POINT: return Material.REDSTONE_TORCH_ON;
		case POPPER: return Material.CHORUS_FRUIT;
		case PULSE: return Material.REDSTONE;
		case TELEPORT: return Material.ENDER_PEARL;
		case TRAIL: return Material.POWERED_RAIL;
		case VORTEX: return Material.NETHER_STAR;
		case WINGS: return Material.ELYTRA;
		}
		return null;
	}
	
	
	public int getPrize() {
		switch(this) {
		default: return 0;
		case BATMAN: return 25000;
		case BLOCKBREAK: return 25000;
		case CELEBRATION : return 20000;
		case FEET: return 15000;
		case HALO: return 20000;
		case INVOCATION: return 35000;
		case MOVE: return 15000;
		case DEFAULT: return 12500;
		case POINT: return 12500;
		case POPPER: return 12500;
		case PULSE: return 12500;
		case TELEPORT: return 25000;
		case TRAIL: return 17500;
		case VORTEX: return 15000;
		case WINGS: return 35000;
		}
	}
	
	public String getName() {
		String name = this.toString().replaceAll("\\d","");
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		switch(this) {
		default: return "";
		case BATMAN: return "§8" +name;
		case BLOCKBREAK: return"§f" +name;
		case CELEBRATION : return "§d" +name;
		case FEET: return "§e" +name;
		case HALO: return "§5" +name;
		case INVOCATION: return"§3" +name;
		case MOVE: return "§2" +name;
		case DEFAULT: return "§7" +name;
		case POINT: return "§c" +name;
		case POPPER: return "§c" +name;
		case PULSE: return "§c" +name;
		case TELEPORT: return "§5" +name;
		case TRAIL: return "§e" +name;
		case VORTEX: return "§5" +name;
		case WINGS: return "§5" +name;
		}
		
	}
}
