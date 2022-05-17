package me.santipingui58.splindux.game.death;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;

public enum DeathReason {
THEMSELF,SPLEEFED,SNOWBALLED, PK_FAILED, PLAYING_FAILED;
	
	
	public String getDeathMessage(SpleefPlayer killer, SpleefPlayer killed,Arena arena) {
		FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
		String left = Main.ffa2v2 ? ffa.getTeamsAlive().size() +" §bcouples" : ffa.getPlayers().size() + " §bplayers";
		
		if (this.equals(THEMSELF)) {
			return "§6"+ killed.getName() + " §bspleefed themself! §a" + left+ " left!";
		} else if (this.equals(SPLEEFED)) {
			return "§6"+ killed.getName() + "§b was spleefed by §6"+ killer.getName() + "§b! §a" +  left+ " left!";
		} else if (this.equals(SNOWBALLED)) {
			String snowballed = arena.getSpleefType().equals(SpleefType.SPLEEF) ? "snowballed" : "splegged";
			return "§6"+ killed.getName() + "§b was "+snowballed+" by §6"+ killer.getName() + "§b! §a" + left+ " left!";
		} else if (this.equals(PK_FAILED)) {
			return "§6"+ killed.getName() + " §bfailed! §a" +  left+ "left!";
		} else if (this.equals(PLAYING_FAILED)) {
			return "§6"+ killed.getName() + " §bhas fell while playing against §6"+ killer.getName() + "§b! §a"+ left+ " left!";
		}	
	
		return null;
} 
}
