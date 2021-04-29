package me.santipingui58.splindux.game.death;

import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public enum DeathReason {
THEMSELF,SPLEEFED,SNOWBALLED, PK_FAILED, PLAYING_FAILED;
	
	
	public String getDeathMessage(SpleefPlayer killer, SpleefPlayer killed,Arena arena) {
		if (this.equals(THEMSELF)) {
			return "§6"+ killed.getName() + " §bspleefed themself! §a" + arena.getFFAPlayers().size()+" §bplayers left!";
		} else if (this.equals(SPLEEFED)) {
			return "§6"+ killed.getName() + "§b was spleefed by §6"+ killer.getName() + "§b! §a" + arena.getFFAPlayers().size()+" §bplayers left!";
		} else if (this.equals(SNOWBALLED)) {
			return "§6"+ killed.getName() + "§b was snowballed by §6"+ killer.getName() + "§b! §a" + arena.getFFAPlayers().size()+" §bplayers left!";
		} else if (this.equals(PK_FAILED)) {
			return "§6"+ killed.getName() + " §bfailed! §a" + arena.getFFAPlayers().size()+" §bplayers left!";
		} else if (this.equals(PLAYING_FAILED)) {
			return "§6"+ killed.getName() + " §bhas fell while playing against §6"+ killer.getName() + "§b! §a" + arena.getFFAPlayers().size()+" §bplayers left!";
		}	
	
		return null;
} 
}
