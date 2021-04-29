package me.santipingui58.splindux.game.parkour;


public class PlayerStats {

	private int level1;
	private int level2;
	private int level3;
	private int level4;
	private int level5;
	private int level6;
	private int level7;
	private int level8;
	private int level9;
	private int level10;
	private int level11;
	private int level12;
	private int level13;
	private int level14;
	private int level15;
	private int level16;
	private int level17;
	private int level18;
	private int level19;
	private int level20;
	private int level21;
	private int level22;
	private int level23;
	private int level24;
	private int level25;
	private int points;
	
	public PlayerStats(int level1,int level2,int level3,int level4,int level5,int level6,int level7,int level8,int level9,int level10,
			 		   int level11, int level12, int level13, int level14, int level15, int level16, int level17, int level18, int level19, int level20,
			 		   int level21,int level22,int level23, int level24, int level25,int points) {
		this.level1 = level1;
		this.level2 = level2;
		this.level3 = level3;
		this.level4 = level4;
		this.level5 = level5;
		this.level6 = level6;
		this.level7 = level7;
		this.level8 = level8;
		this.level9 = level9;
		this.level10 = level10;
		this.level11 = level11;
		this.level12 = level12;
		this.level13 = level13;
		this.level14 = level14;
		this.level15 = level15;
		this.level16 = level16;
		this.level17 = level17;
		this.level18 = level18;
		this.level19 = level19;
		this.level20 = level20;
		this.level21 = level21;
		this.level22 = level22;
		this.level23 = level23;
		this.level24 = level24;
		this.level25 = level25;
		this.points = points;

	}
	
	public int getPoints() {
		return this.points;
	}
	
	public void setPoints(int i) {
		this.points = i;
	}
	
	public int getRecordByLevel(Level level) {
		switch(level.getLevel()) {
		case 1: return level1;
		case 2: return level2;
		case 3: return level3;
		case 4: return level4;
		case 5: return level5;
		case 6: return level6;
		case 7: return level7;
		case 8: return level8;
		case 9: return level9;
		case 10: return level10;
		case 11: return level11;
		case 12: return level12;
		case 13: return level13;
		case 14: return level14;
		case 15: return level15;
		case 16: return level16;
		case 17: return level17;
		case 18: return level18;
		case 19: return level19;
		case 20: return level20;
		case 21: return level21;
		case 22: return level22;
		case 23: return level23;
		case 24: return level24;
		case 25: return level25;
		}
		return 0;
	}
	
	public void setRecordByLevel(Level level,int record) {
		switch(level.getLevel()) {
		case 1: setLevel1(record);
		return;
		case 2: setLevel2(record);
		return;
		case 3: setLevel3(record);
		return;
		case 4: setLevel4(record);
		return;
		case 5: setLevel5(record);
		return;
		case 6: setLevel6(record);
		return;
		case 7: setLevel7(record);
		return;
		case 8: setLevel8(record);
		return;
		case 9: setLevel9(record);
		return;
		case 10: setLevel10(record);
		return;
		case 11: setLevel11(record);
		return;
		case 12: setLevel12(record);
		return;
		case 13: setLevel13(record);
		return;
		case 14: setLevel14(record);
		return;
		case 15: setLevel15(record);
		return;
		case 16: setLevel16(record);
		return;
		case 17: setLevel17(record);
		return;
		case 18: setLevel18(record);
		return;
		case 19: setLevel19(record);
		return;
		case 20: setLevel20(record);
		return;
		case 21: setLevel21(record);
		return;
		case 22: setLevel22(record);
		return;
		case 23: setLevel23(record);
		return;
		case 24: setLevel24(record);
		return;
		case 25: setLevel25(record);
		return;
		}
	}
	
	public int getLevel(Level level) {
		switch(level.getLevel()) {
		case 1: return  getLevel1();
		case 2: return  getLevel2();
		case 3: return  getLevel3();
		case 4: return  getLevel4();
		case 5: return  getLevel5();
		case 6: return  getLevel6();
		case 7: return  getLevel7();
		case 8: return  getLevel8();
		case 9: return  getLevel9();
		case 10: return  getLevel10();
		case 11: return  getLevel11();
		case 12: return  getLevel12();
		case 13: return  getLevel13();
		case 14: return  getLevel14();
		case 15: return  getLevel15();
		case 16: return  getLevel16();
		case 17: return  getLevel17();
		case 18: return  getLevel18();
		case 19: return  getLevel19();
		case 20: return  getLevel20();
		case 21: return  getLevel21();
		case 22: return  getLevel22();
		case 23: return  getLevel23();
		case 24: return  getLevel24();
		case 25: return  getLevel25();
		}
		return 0;
	}
	
	
	public int getLevel1() {
		return this.level1;
	}
	
	public int getLevel2() {
		return this.level2;
	}
	public int getLevel3() {
		return this.level3;
	}
	public int getLevel4() {
		return this.level4;
	}
	public int getLevel5() {
		return this.level5;
	}
	
	public int getLevel6() {
		return this.level6;
	}
	public int getLevel7() {
		return this.level7;
	}
	public int getLevel8() {
		return this.level8;
	}
	public int getLevel9() {
		return this.level9;
	}
	public int getLevel10() {
		return this.level10;
	}
	public int getLevel11() {
		return this.level11;
	}
	public int getLevel12() {
		return this.level12;
	}
	public int getLevel13() {
		return this.level13;
	}
	public int getLevel14() {
		return this.level14;
	}
	public int getLevel15() {
		return this.level15;
	}
	public int getLevel16() {
		return this.level16;
	}
	public int getLevel17() {
		return this.level17;
	}
	public int getLevel18() {
		return this.level18;
	}
	public int getLevel19() {
		return this.level19;
	}
	public int getLevel20() {
		return this.level20;
	}
	public int getLevel21() {
		return this.level21;
	}
	public int getLevel22() {
		return this.level22;
	}
	public int getLevel23() {
		return this.level23;
	}
	public int getLevel24() {
		return this.level24;
	}
	public int getLevel25() {
		return this.level25;
	}
	
	public void setLevel1(int i) {
		  this.level1= i;
	}
	
	public void setLevel2(int i) {
		  this.level2= i;
	}
	public void setLevel3(int i) {
		  this.level3= i;
	}
	public void setLevel4(int i) {
		  this.level4= i;
	}
	public void setLevel5(int i) {
		  this.level5= i;
	}
	
	public void setLevel6(int i) {
		  this.level6= i;
	}
	public void setLevel7(int i) {
		  this.level7= i;
	}
	public void setLevel8(int i) {
		  this.level8= i;
	}
	public void setLevel9(int i) {
		  this.level9= i;
	}
	public void setLevel10(int i) {
		  this.level10= i;
	}
	public void setLevel11(int i) {
		  this.level11= i;
	}
	public void setLevel12(int i) {
		  this.level12= i;
	}
	public void setLevel13(int i) {
		  this.level13= i;
	}
	public void setLevel14(int i) {
		  this.level14= i;
	}
	public void setLevel15(int i) {
		  this.level15= i;
	}
	public void setLevel16(int i) {
		  this.level16= i;
	}
	public void setLevel17(int i) {
		  this.level17= i;
	}
	public void setLevel18(int i) {
		  this.level18= i;
	}
	public void setLevel19(int i) {
		  this.level19= i;
	}
	public void setLevel20(int i) {
		  this.level20= i;
	}
	public void setLevel21(int i) {
		  this.level21= i;
	}
	public void setLevel22(int i) {
		  this.level22= i;
	}
	public void setLevel23(int i) {
		  this.level23= i;
	}
	public void setLevel24(int i) {
		  this.level24= i;
	}
	public void setLevel25(int i) {
		  this.level25= i;
	}
}
