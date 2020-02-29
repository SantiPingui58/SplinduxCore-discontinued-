package me.santipingui58.splindux.stats.level;

import org.bukkit.ChatColor;

public enum SpleefRank {

	
	BEGGINER_I,
	BEGGINER_II,
	BEGGINER_III,
	BEGGINER_IV,
	BEGGINER_V,
	AMATEUR_I,
	AMATEUR_II,
	AMATEUR_III,
	AMATEUR_IV,
	AMATEUR_V,
	MEDIUM_I,
	MEDIUM_II,
	MEDIUM_III,
	MEDIUM_IV,
	MEDIUM_V,
	ADVANCED_I,
	ADVANCED_II,
	ADVANCED_III,
	ADVANCED_IV,
	ADVANCED_V,
	ELITE_I,
	ELITE_II,
	ELITE_III,
	ELITE_IV,
	ELITE_V,
	EXPERT_I,
	EXPERT_II,
	EXPERT_III,
	EXPERT_IV,
	EXPERT_V,
	MASTER_I,
	MASTER_II,
	MASTER_III,
	MASTER_IV,
	MASTER_V,
	LEGEND_I,
	LEGEND_II,
	LEGEND_III,
	LEGEND_IV,
	LEGEND_V,
	CHALLENGER_I,
	CHALLENGER_II,
	CHALLENGER_III,
	CHALLENGER_IV,
	CHALLENGER_V,
	VETERAN_I,
	VETERAN_II,
	VETERAN_III,
	VETERAN_IV,
	VETERAN_V;
	
	
	public int getRequiredLevel() {
		if (this==BEGGINER_I) {
			return 20;
		} else if (this.equals(BEGGINER_II)) {
			return 40;
		} else if (this.equals(BEGGINER_III)) {
			return 60;
		} else if (this.equals(BEGGINER_IV)) {
			return 80;
		} else if (this.equals(BEGGINER_V)) {
			return 100;
		} else if (this==AMATEUR_I) {
			return 180;
		} else if (this.equals(AMATEUR_II)) {
			return 260;
		} else if (this.equals(AMATEUR_III)) {
			return 340;
		} else if (this.equals(AMATEUR_IV)) {
			return 420;
		} else if (this.equals(AMATEUR_V)) {
			return 500;
		} else if (this==MEDIUM_I) {
			return 660;
		} else if (this.equals(MEDIUM_II)) {
			return 820;
		} else if (this.equals(MEDIUM_III)) {
			return 980;
		} else if (this.equals(MEDIUM_IV)) {
			return 1140;
		} else if (this.equals(MEDIUM_V)) {
			return 1300;
		} else if (this==ADVANCED_I) {
			return 1600;
		} else if (this.equals(ADVANCED_II)) {
			return 1900;
		} else if (this.equals(ADVANCED_III)) {
			return 2200;
		} else if (this.equals(ADVANCED_IV)) {
			return 2500;
		} else if (this.equals(ADVANCED_V)) {
			return 2800;
		} else if (this==ELITE_I) {
			return 3240;
		} else if (this.equals(ELITE_II)) {
			return 3680;
		} else if (this.equals(ELITE_III)) {
			return 4120;
		} else if (this.equals(ELITE_IV)) {
			return 4560;
		} else if (this.equals(ELITE_V)) {
			return 5000;
		} else if (this==EXPERT_I) {
			return 5800;
		} else if (this.equals(EXPERT_II)) {
			return 6600;
		} else if (this.equals(EXPERT_III)) {
			return 7400;
		} else if (this.equals(EXPERT_IV)) {
			return 8200;
		} else if (this.equals(EXPERT_V)) {
			return 9000;
		} else if (this==MASTER_I) {
			return 10800;
		} else if (this.equals(MASTER_II)) {
			return 12600;
		} else if (this.equals(MASTER_III)) {
			return 14400;
		} else if (this.equals(MASTER_IV)) {
			return 16200;
		} else if (this.equals(MASTER_V)) {
			return 18000;
		} else if (this==LEGEND_I) {
			return 22400;
		} else if (this.equals(LEGEND_II)) {
			return 26800;
		} else if (this.equals(LEGEND_III)) {
			return 31200;
		} else if (this.equals(LEGEND_IV)) {
			return 35600;
		} else if (this.equals(LEGEND_V)) {
			return 40000;
		} else if (this==CHALLENGER_I) {
			return 52000;
		} else if (this.equals(CHALLENGER_II)) {
			return 64000;
		} else if (this.equals(CHALLENGER_III)) {
			return 76000;
		} else if (this.equals(CHALLENGER_IV)) {
			return 88000;
		} else if (this.equals(CHALLENGER_V)) {
			return 100000;
		} else if (this==VETERAN_I) {
			return 120000;
		} else if (this.equals(VETERAN_II)) {
			return 140000;
		} else if (this.equals(VETERAN_III)) {
			return 160000;
		} else if (this.equals(VETERAN_IV)) {
			return 180000;
		} else if (this.equals(VETERAN_V)) {
			return 200000;
		} 
		return 0;
	}
	
	public int getInt() {
		if (this==BEGGINER_I) {
			return 1;
		} else if (this.equals(BEGGINER_II)) {
			return 2;
		} else if (this.equals(BEGGINER_III)) {
			return 3;
		} else if (this.equals(BEGGINER_IV)) {
			return 4;
		} else if (this.equals(BEGGINER_V)) {
			return 5;
		} else if (this==AMATEUR_I) {
			return 6;
		} else if (this.equals(AMATEUR_II)) {
			return 7;
		} else if (this.equals(AMATEUR_III)) {
			return 8;
		} else if (this.equals(AMATEUR_IV)) {
			return 9;
		} else if (this.equals(AMATEUR_V)) {
			return 10;
		} else if (this==MEDIUM_I) {
			return 11;
		} else if (this.equals(MEDIUM_II)) {
			return 12;
		} else if (this.equals(MEDIUM_III)) {
			return 13;
		} else if (this.equals(MEDIUM_IV)) {
			return 14;
		} else if (this.equals(MEDIUM_V)) {
			return 15;
		} else if (this==ADVANCED_I) {
			return 16;
		} else if (this.equals(ADVANCED_II)) {
			return 17;
		} else if (this.equals(ADVANCED_III)) {
			return 18;
		} else if (this.equals(ADVANCED_IV)) {
			return 19;
		} else if (this.equals(ADVANCED_V)) {
			return 20;
		} else if (this==ELITE_I) {
			return 21;
		} else if (this.equals(ELITE_II)) {
			return 22;
		} else if (this.equals(ELITE_III)) {
			return 23;
		} else if (this.equals(ELITE_IV)) {
			return 24;
		} else if (this.equals(ELITE_V)) {
			return 25;
		} else if (this==EXPERT_I) {
			return 26;
		} else if (this.equals(EXPERT_II)) {
			return 27;
		} else if (this.equals(EXPERT_III)) {
			return 28;
		} else if (this.equals(EXPERT_IV)) {
			return 29;
		} else if (this.equals(EXPERT_V)) {
			return 30;
		} else if (this==MASTER_I) {
			return 31;
		} else if (this.equals(MASTER_II)) {
			return 32;
		} else if (this.equals(MASTER_III)) {
			return 33;
		} else if (this.equals(MASTER_IV)) {
			return 34;
		} else if (this.equals(MASTER_V)) {
			return 35;
		} else if (this==LEGEND_I) {
			return 36;
		} else if (this.equals(LEGEND_II)) {
			return 37;
		} else if (this.equals(LEGEND_III)) {
			return 38;
		} else if (this.equals(LEGEND_IV)) {
			return 39;
		} else if (this.equals(LEGEND_V)) {
			return 40;
		} else if (this==CHALLENGER_I) {
			return 41;
		} else if (this.equals(CHALLENGER_II)) {
			return 42;
		} else if (this.equals(CHALLENGER_III)) {
			return 43;
		} else if (this.equals(CHALLENGER_IV)) {
			return 44;
		} else if (this.equals(CHALLENGER_V)) {
			return 45;
		} else if (this==VETERAN_I) {
			return 46;
		} else if (this.equals(VETERAN_II)) {
			return 47;
		} else if (this.equals(VETERAN_III)) {
			return 48;
		} else if (this.equals(VETERAN_IV)) {
			return 49;
		} else if (this.equals(VETERAN_V)) {
			return 50;
		} 
		return 0;
	}
	
	
	public SpleefRank getNextRank() {
		if (this==BEGGINER_I) {
			return BEGGINER_II;
		} else if (this.equals(BEGGINER_II)) {
			return BEGGINER_III;
		} else if (this.equals(BEGGINER_III)) {
			return BEGGINER_IV;
		} else if (this.equals(BEGGINER_IV)) {
			return BEGGINER_V;
		} else if (this.equals(BEGGINER_V)) {
			return AMATEUR_I;
		} else if (this==AMATEUR_I) {
			return AMATEUR_II;
		} else if (this.equals(AMATEUR_II)) {
			return AMATEUR_III;
		} else if (this.equals(AMATEUR_III)) {
			return AMATEUR_IV;
		} else if (this.equals(AMATEUR_IV)) {
			return AMATEUR_V;
		} else if (this.equals(AMATEUR_V)) {
			return MEDIUM_I;
		} else if (this==MEDIUM_I) {
			return MEDIUM_II;
		} else if (this.equals(MEDIUM_II)) {
			return MEDIUM_III;
		} else if (this.equals(MEDIUM_III)) {
			return MEDIUM_IV;
		} else if (this.equals(MEDIUM_IV)) {
			return MEDIUM_V;
		} else if (this.equals(MEDIUM_V)) {
			return ADVANCED_I;
		} else if (this==ADVANCED_I) {
			return ADVANCED_II;
		} else if (this.equals(ADVANCED_II)) {
			return ADVANCED_III;
		} else if (this.equals(ADVANCED_III)) {
			return ADVANCED_IV;
		} else if (this.equals(ADVANCED_IV)) {
			return ADVANCED_V;
		} else if (this.equals(ADVANCED_V)) {
			return ELITE_I;
		} else if (this==ELITE_I) {
			return ELITE_II;
		} else if (this.equals(ELITE_II)) {
			return ELITE_III;
		} else if (this.equals(ELITE_III)) {
			return ELITE_IV;
		} else if (this.equals(ELITE_IV)) {
			return ELITE_V;
		} else if (this.equals(ELITE_V)) {
			return EXPERT_I;
		} else if (this==EXPERT_I) {
			return EXPERT_II;
		} else if (this.equals(EXPERT_II)) {
			return EXPERT_III;
		} else if (this.equals(EXPERT_III)) {
			return EXPERT_IV;
		} else if (this.equals(EXPERT_IV)) {
			return EXPERT_V;
		} else if (this.equals(EXPERT_V)) {
			return MASTER_I;
		} else if (this==MASTER_I) {
			return MASTER_II;
		} else if (this.equals(MASTER_II)) {
			return MASTER_III;
		} else if (this.equals(MASTER_III)) {
			return MASTER_IV;
		} else if (this.equals(MASTER_IV)) {
			return MASTER_V;
		} else if (this.equals(MASTER_V)) {
			return LEGEND_I;
		} else if (this==LEGEND_I) {
			return LEGEND_II;
		} else if (this.equals(LEGEND_II)) {
			return LEGEND_III;
		} else if (this.equals(LEGEND_III)) {
			return LEGEND_IV;
		} else if (this.equals(LEGEND_IV)) {
			return LEGEND_V;
		} else if (this.equals(LEGEND_V)) {
			return CHALLENGER_I;
		} else if (this==CHALLENGER_I) {
			return CHALLENGER_II;
		} else if (this.equals(CHALLENGER_II)) {
			return CHALLENGER_III;
		} else if (this.equals(CHALLENGER_III)) {
			return CHALLENGER_IV;
		} else if (this.equals(CHALLENGER_IV)) {
			return CHALLENGER_V;
		} else if (this.equals(CHALLENGER_V)) {
			return VETERAN_I;
		} else if (this==VETERAN_I) {
			return VETERAN_II;
		} else if (this.equals(VETERAN_II)) {
			return VETERAN_III;
		} else if (this.equals(VETERAN_III)) {
			return VETERAN_IV;
		} else if (this.equals(VETERAN_IV)) {
			return VETERAN_V;
		} 
		return null;
	}
	
	
	public SpleefRank getPrevRank() {
		if (this.equals(BEGGINER_II)) {
			return BEGGINER_I;
		} else if (this.equals(BEGGINER_III)) {
			return BEGGINER_II;
		} else if (this.equals(BEGGINER_IV)) {
			return BEGGINER_III;
		} else if (this.equals(BEGGINER_V)) {
			return BEGGINER_IV;
		} else if (this==AMATEUR_I) {
			return BEGGINER_V;
		} else if (this.equals(AMATEUR_II)) {
			return AMATEUR_I;
		} else if (this.equals(AMATEUR_III)) {
			return AMATEUR_II;
		} else if (this.equals(AMATEUR_IV)) {
			return AMATEUR_III;
		} else if (this.equals(AMATEUR_V)) {
			return AMATEUR_IV;
		} else if (this==MEDIUM_I) {
			return AMATEUR_V;
		} else if (this.equals(MEDIUM_II)) {
			return MEDIUM_I;
		} else if (this.equals(MEDIUM_III)) {
			return MEDIUM_II;
		} else if (this.equals(MEDIUM_IV)) {
			return MEDIUM_III;
		} else if (this.equals(MEDIUM_V)) {
			return MEDIUM_IV;
		} else if (this==ADVANCED_I) {
			return MEDIUM_V;
		} else if (this.equals(ADVANCED_II)) {
			return ADVANCED_I;
		} else if (this.equals(ADVANCED_III)) {
			return ADVANCED_II;
		} else if (this.equals(ADVANCED_IV)) {
			return ADVANCED_III;
		} else if (this.equals(ADVANCED_V)) {
			return ADVANCED_IV;
		} else if (this==ELITE_I) {
			return ADVANCED_V;
		} else if (this.equals(ELITE_II)) {
			return ELITE_I;
		} else if (this.equals(ELITE_III)) {
			return ELITE_II;
		} else if (this.equals(ELITE_IV)) {
			return ELITE_III;
		} else if (this.equals(ELITE_V)) {
			return ELITE_IV;
		} else if (this==EXPERT_I) {
			return ELITE_V;
		} else if (this.equals(EXPERT_II)) {
			return EXPERT_I;
		} else if (this.equals(EXPERT_III)) {
			return EXPERT_II;
		} else if (this.equals(EXPERT_IV)) {
			return EXPERT_III;
		} else if (this.equals(EXPERT_V)) {
			return EXPERT_IV;
		} else if (this==MASTER_I) {
			return EXPERT_V;
		} else if (this.equals(MASTER_II)) {
			return MASTER_I;
		} else if (this.equals(MASTER_III)) {
			return MASTER_II;
		} else if (this.equals(MASTER_IV)) {
			return MASTER_III;
		} else if (this.equals(MASTER_V)) {
			return MASTER_IV;
		} else if (this==LEGEND_I) {
			return MASTER_V;
		} else if (this.equals(LEGEND_II)) {
			return LEGEND_I;
		} else if (this.equals(LEGEND_III)) {
			return LEGEND_II;
		} else if (this.equals(LEGEND_IV)) {
			return LEGEND_III;
		} else if (this.equals(LEGEND_V)) {
			return LEGEND_IV;
		} else if (this==CHALLENGER_I) {
			return LEGEND_V;
		} else if (this.equals(CHALLENGER_II)) {
			return CHALLENGER_I;
		} else if (this.equals(CHALLENGER_III)) {
			return CHALLENGER_II;
		} else if (this.equals(CHALLENGER_IV)) {
			return CHALLENGER_III;
		} else if (this.equals(CHALLENGER_V)) {
			return CHALLENGER_IV;
		} else if (this==VETERAN_I) {
			return CHALLENGER_V;
		} else if (this.equals(VETERAN_II)) {
			return VETERAN_I;
		} else if (this.equals(VETERAN_III)) {
			return VETERAN_II;
		} else if (this.equals(VETERAN_IV)) {
			return VETERAN_III;
		}  else if (this.equals(VETERAN_V)) {
			return VETERAN_IV;
		}
		return null;
	}
	public String getRankName() {
		String name = this.name();
		name = name.replace("_", " ");
		String[] nombre = name.split(" ",2);
		name = nombre[0];
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		String lvl = nombre[1];
		return getRankColor() + name + " " + lvl;
	}
	
	
	public ChatColor getRankColor() {
		if (getMainRank().equals(BEGGINER_I)) {
			return ChatColor.GRAY;
		} else if (getMainRank().equals(AMATEUR_I)) {
			return ChatColor.WHITE;
		}else if (getMainRank().equals(MEDIUM_I)) {
			return ChatColor.GREEN;
		} else if (getMainRank().equals(ADVANCED_I)) {
			return ChatColor.DARK_GREEN;
		}  else if (getMainRank().equals(ELITE_I)) {
			return ChatColor.YELLOW;
		}else if (getMainRank().equals(EXPERT_I)) {
			return ChatColor.GOLD;
		}else if (getMainRank().equals(MASTER_I)) {
			return ChatColor.RED;
		}else if (getMainRank().equals(LEGEND_I)) {
			return ChatColor.DARK_RED;
		}else if (getMainRank().equals(CHALLENGER_I)) {
			return ChatColor.DARK_PURPLE;
		}else if (getMainRank().equals(VETERAN_I)) {
			return ChatColor.BLACK;
		}
		return null;
	}
	
	public SpleefRank getMainRank() {
		if (this.equals(BEGGINER_I) || this.equals(BEGGINER_II) || this.equals(BEGGINER_III) || this.equals(BEGGINER_IV) || this.equals(BEGGINER_V)) {
			return BEGGINER_I;
		} else if (this.equals(AMATEUR_I) || this.equals(AMATEUR_II) || this.equals(AMATEUR_III) || this.equals(AMATEUR_IV) || this.equals(AMATEUR_V)) {
			return AMATEUR_I;
		} else if (this.equals(MEDIUM_I) || this.equals(MEDIUM_II) || this.equals(MEDIUM_III) || this.equals(MEDIUM_IV) || this.equals(MEDIUM_V)) {
			return MEDIUM_I;
		} else if (this.equals(ADVANCED_I) || this.equals(ADVANCED_II) || this.equals(ADVANCED_III) || this.equals(ADVANCED_IV) || this.equals(ADVANCED_V)) {
			return ADVANCED_I;
		} else if (this.equals(ELITE_I) || this.equals(ELITE_II) || this.equals(ELITE_III) || this.equals(ELITE_IV) || this.equals(ELITE_V)) {
			return ELITE_I;
		} else if (this.equals(EXPERT_I) || this.equals(EXPERT_II) || this.equals(EXPERT_III) || this.equals(EXPERT_IV) || this.equals(EXPERT_V)) {
			return EXPERT_I;
		} else if (this.equals(MASTER_I) || this.equals(MASTER_II) || this.equals(MASTER_III) || this.equals(MASTER_IV) || this.equals(MASTER_V)) {
			return MASTER_I;
		}else if (this.equals(LEGEND_I) || this.equals(LEGEND_II) || this.equals(LEGEND_III) || this.equals(LEGEND_IV) || this.equals(LEGEND_V)) {
			return LEGEND_I;
		}else if (this.equals(CHALLENGER_I) || this.equals(CHALLENGER_II) || this.equals(CHALLENGER_III) || this.equals(CHALLENGER_IV) || this.equals(CHALLENGER_V)) {
			return CHALLENGER_I;
		}else if (this.equals(VETERAN_I) || this.equals(VETERAN_II) || this.equals(VETERAN_III) || this.equals(VETERAN_IV) || this.equals(VETERAN_V)) {
			return VETERAN_I;
		}
		return null;
	}
	
	public SpleefRank getTopRank() {
		if (this.equals(BEGGINER_I) || this.equals(BEGGINER_II) || this.equals(BEGGINER_III) || this.equals(BEGGINER_IV) || this.equals(BEGGINER_V)) {
			return BEGGINER_V;
		} else if (this.equals(AMATEUR_I) || this.equals(AMATEUR_II) || this.equals(AMATEUR_III) || this.equals(AMATEUR_IV) || this.equals(AMATEUR_V)) {
			return AMATEUR_V;
		} else if (this.equals(MEDIUM_I) || this.equals(MEDIUM_II) || this.equals(MEDIUM_III) || this.equals(MEDIUM_IV) || this.equals(MEDIUM_V)) {
			return MEDIUM_V;
		} else if (this.equals(ADVANCED_I) || this.equals(ADVANCED_II) || this.equals(ADVANCED_III) || this.equals(ADVANCED_IV) || this.equals(ADVANCED_V)) {
			return ADVANCED_V;
		} else if (this.equals(ELITE_I) || this.equals(ELITE_II) || this.equals(ELITE_III) || this.equals(ELITE_IV) || this.equals(ELITE_V)) {
			return ELITE_V;
		} else if (this.equals(EXPERT_I) || this.equals(EXPERT_II) || this.equals(EXPERT_III) || this.equals(EXPERT_IV) || this.equals(EXPERT_V)) {
			return EXPERT_V;
		} else if (this.equals(MASTER_I) || this.equals(MASTER_II) || this.equals(MASTER_III) || this.equals(MASTER_IV) || this.equals(MASTER_V)) {
			return MASTER_V;
		}else if (this.equals(LEGEND_I) || this.equals(LEGEND_II) || this.equals(LEGEND_III) || this.equals(LEGEND_IV) || this.equals(LEGEND_V)) {
			return LEGEND_V;
		}else if (this.equals(CHALLENGER_I) || this.equals(CHALLENGER_II) || this.equals(CHALLENGER_III) || this.equals(CHALLENGER_IV) || this.equals(CHALLENGER_V)) {
			return CHALLENGER_V;
		}else if (this.equals(VETERAN_I) || this.equals(VETERAN_II) || this.equals(VETERAN_III) || this.equals(VETERAN_IV) || this.equals(VETERAN_V)) {
			return VETERAN_V;
		}
		return null;
	}
}
