package me.santipingui58.splindux.npc;

public enum NPCType {

	VOTES, PARKOUR,THETOWERS;
	
	
	public int getId() {
		if (this.equals(VOTES)) {
			return 179;
		} else if (this.equals(PARKOUR)) {
			return 176;
		} else if (this.equals(THETOWERS)) {
			return 331;
		} 
		
		return 0;
	}
 	
	

}
