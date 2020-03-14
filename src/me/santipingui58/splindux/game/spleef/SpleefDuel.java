package me.santipingui58.splindux.game.spleef;

public class SpleefDuel {

	private String arena;
	private SpleefPlayer sp1;
	private SpleefPlayer sp2;
	private SpleefType type;
	
	public SpleefDuel(SpleefPlayer sp1, SpleefPlayer sp2, String arena,SpleefType type) {
		this.sp1 = sp1;
		this.sp2 = sp2;
		this.arena = arena;
		this.type = type;
	}
	
	public SpleefPlayer getPlayer1() {
		return this.sp1;
	}
	
	public SpleefPlayer getPlayer2() {
		return this.sp2;
	}
	
	public String getArena() {
		return this.arena;
	}
	
	public SpleefType getType() {
		return this.type;
	}
}
