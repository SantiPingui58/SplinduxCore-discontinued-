package me.santipingui58.splindux.cosmetics.particles.effect;

import org.bukkit.Material;

import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;

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
	
	public ParticleStyle effectToDefaultStyles() {
		switch(this) {
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
	public int getPrize() {
		switch(this) {
		default: return 0;
		case BATMAN: return 7500;
		case BLOCKBREAK: return 7500;
		case CELEBRATION : return 8000;
		case FEET: return 5000;
		case HALO: return 7500;
		case INVOCATION: return 12500;
		case MOVE: return 5000;
		case DEFAULT: return 4500;
		case POINT: return 4500;
		case POPPER: return 4500;
		case PULSE: return 4500;
		case TELEPORT: return 10000;
		case TRAIL: return 6000;
		case VORTEX: return 5000;
		case WINGS: return 12500;
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
