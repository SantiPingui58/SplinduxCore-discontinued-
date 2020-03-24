package me.santipingui58.splindux.replay;

import java.util.ArrayList;
import java.util.List;


public class GameReplay {

	private String name;
	private List<BrokenBlock> brokenBlocks = new ArrayList<BrokenBlock>();

	public GameReplay(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<BrokenBlock> getBrokenBlocks() {
		return this.brokenBlocks;
	}
	
	

	
}
