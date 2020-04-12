package me.santipingui58.splindux.game;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FFAEvent {

	
	private int currentRound;
	private int maxRounds;
	private HashMap<SpleefPlayer,Integer> points;
	
	
	public FFAEvent(int maxRounds) {
		this.maxRounds = maxRounds;
		this.currentRound = 1;
		this.points = new HashMap<SpleefPlayer,Integer>();
	}
	
	public int getCurrentRound() {
		return this.currentRound;
	}
	
	public void nextRound() {
		if (this.currentRound<this.maxRounds)
		this.currentRound++;
	}
	
	public int getMaxRounds() {
		return this.maxRounds;
	}
	
	public HashMap<SpleefPlayer,Integer> getPoints() {
		return this.points;
	}
	
	public SpleefArena getArena() {
		for (SpleefArena arena : DataManager.getManager().getArenas()) {
			if (arena.getEvent().equals(this)) {
				return arena;
			}
		}
		return null;
	}
	
	public void sendBroadcast() {
		TextComponent msg1 = new TextComponent("[Points]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.AQUA );
		msg1.setBold(true);
		
		TextComponent a1 = new TextComponent("Every round survived: 1 point");
		TextComponent a2 = new TextComponent("Every kill: 3 points");
		TextComponent a3 = new TextComponent("Every win: 7 points");
		ComponentBuilder acb = new ComponentBuilder(a1);
		acb.append("\\n");
		acb.append(a2);
		acb.append("\\n");
		acb.append(a3);
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, acb.create()));
		TextComponent msg2 = new TextComponent("[Prizes]");
		msg2.setColor( net.md_5.bungee.api.ChatColor.GOLD );
		msg2.setBold( true );
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny duel request").create()));
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		 cb.create();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage("A new FFA Event will start in the next round! Join FFA to participate and get many points as you can! The Event will last for "+ this.maxRounds+ " rounds!");
			p.sendMessage("§7(Rank snowballs and Powerups will be disabled until the Event ends.)");
		}
	}
	
}
