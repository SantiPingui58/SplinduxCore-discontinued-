package me.santipingui58.splindux.game.spleef;

import org.bukkit.Material;

public enum SpleefType {
SPLEEF,SPLEGG,TNTRUN,POTSPLEEF,BOWSPLEEF;
	
 public String getName() {
	 switch (this) {
	default:break;
	case SPLEEF: return "§b§lSpleef";
	case SPLEGG: return "§e§lSplegg";
	case TNTRUN: return "§c§lTNT Run";
	
	 }

	 return null;
 }

public int getId() {
	switch (this) {
	case BOWSPLEEF:
		break;
	case POTSPLEEF:
		break;
	case SPLEEF:
		return 178;
	case SPLEGG:
		return 174;
	case TNTRUN:
		return 173;
	default:
		break;
	
	}
	return -1;
}

public Material getMaterial(boolean ffa) {
	Material material = Material.STONE;
	switch (this) {
	case BOWSPLEEF:
		break;
	case POTSPLEEF:
		break;
	case SPLEEF:
		material = ffa ? Material.SNOW_BLOCK : Material.DIAMOND_SPADE;
		break;
	case SPLEGG:
		material = ffa ? Material.CAKE : Material.EGG;
		break;
	case TNTRUN:
		material = ffa ? Material.TNT : Material.SULPHUR;
		break;
	default:
		break;
	}
	return material;
}

}
