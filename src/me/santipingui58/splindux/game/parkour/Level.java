package me.santipingui58.splindux.game.parkour;

import java.util.Random;

import org.bukkit.Location;


public class Level {

	
	private int level;
	
	public Level(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	
	public Jump loadJump(ParkourArena arena, ParkourPlayer pp,Location start) {
	int max = this.level*4;
	if (max+8<=100) {
		max = max +8;
	}
	
	int min = (89/24*this.level) - (65/24);
	if (min-6>0) {
		min = min-6;
	}
	
	Random random = new Random();
	int j = random.nextInt(max - min + 1) + min;
	Jump jump = ParkourManager.getManager().getRandomJumpByDifficulty(j,0,true);
	jump.load(arena,pp,start);
	return jump;
	}
	
	
	public int getValue() {
		if (this.level<=11) {
			return 10+(this.level-1);
		} else if (this.level<=15) {
			return 20+((this.level-11)*5);
		}else {
			switch(this.level) {
			case 16: return 50;
			case 17: return 60;
			case 18: return 80;
			case 19: return 120;
			case 20: return 150;
			case 21: return 200;
			case 22: return 250;
			case 23: return 300;
			case 24: return 350;
			}
		}
		
		return 0;
	}
	
	
}
