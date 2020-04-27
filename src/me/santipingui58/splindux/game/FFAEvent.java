package me.santipingui58.splindux.game;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FFAEvent {

	
	private int currentRound;
	private int maxRounds;
	private HashMap<SpleefPlayer,Integer> round_points;
	private HashMap<SpleefPlayer,Integer> total_points;
	private boolean prizes;
	
	public FFAEvent(int maxRounds,boolean prizes) {
		this.maxRounds = maxRounds;
		this.currentRound = 1;
		this.prizes = prizes;
		this.round_points = new HashMap<SpleefPlayer,Integer>();
		this.total_points = new HashMap<SpleefPlayer,Integer>();
	}
	
	public int getCurrentRound() {
		return this.currentRound;
	}
	
	public void nextRound() {
		if (this.currentRound>=this.maxRounds) {
			finishEvent();
		} else {
			this.currentRound++;
		}

	}
	
	public int getMaxRounds() {
		return this.maxRounds;
	}
	
	public void addPoint(SpleefPlayer sp,int p,boolean total) {
		if (!total) {
		if (!this.round_points.containsKey(sp)) {
			this.round_points.put(sp, 0);
		}	
		int oldp = this.round_points.get(sp);
		this.round_points.put(sp, oldp+p);
	} else {
		if (!this.total_points.containsKey(sp)) {
			this.total_points.put(sp, 0);
		}	
		int oldp = this.total_points.get(sp);
		this.total_points.put(sp, oldp+p);
	}
	}
	
	public void finishRound() {
		
		
		for (Entry<SpleefPlayer, Integer> entry : this.round_points.entrySet()) {
			addPoint(entry.getKey(),entry.getValue(),true);
		}
		
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		for (Entry<SpleefPlayer, Integer> entry : this.total_points.entrySet()) {
			  hashmap.put(entry.getKey().getOfflinePlayer().getName(), entry.getValue());
			}
			
			hashmap= StatsManager.getManager().sortByValue(hashmap);
			
			int pos = 1;
			Bukkit.broadcastMessage("§aRound §b" + this.currentRound + " §ahas finished!");
			Bukkit.broadcastMessage("§5-=-=-=-[§d§lFFA Event Positions§5]-=-=-=-");
			for (Entry<String, Integer> entry : hashmap.entrySet()) {
						
				 Bukkit.broadcastMessage("§6" +pos+". §b" + entry.getKey() + "§8: §e" + entry.getValue() +" Points");
				pos++;
				}

			for (SpleefPlayer sp : getArena().getQueue()) {
				if (getTotalPoints().containsKey(sp)) {
					Utils.getUtils().sendTitles(sp.getPlayer(), "§6Total points: " +  getTotalPoints().get(sp), "§aPoints this round: " + getRoundPoints().get(sp), 10, 200, 10);
				}
			}
			
		nextRound();
	}
	
	
	public HashMap<SpleefPlayer,Integer> getTotalPoints() {
		return this.total_points;
	}
	public HashMap<SpleefPlayer,Integer> getRoundPoints() {
		return this.round_points;
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
		acb.append("\n");
		acb.append(a2);
		acb.append("\n");
		acb.append(a3);
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, acb.create()));
		TextComponent msg2 = new TextComponent("[Prizes]");
		msg2.setColor( net.md_5.bungee.api.ChatColor.GOLD );
		msg2.setBold( true );
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny duel request").create()));
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage("§d§lA new FFA Event will start in the next round! Join FFA to participate and get many points as you can! The Event will last for §a§l"+ this.maxRounds+ " rounds§d§l!");
			p.sendMessage("§7(Rank snowballs and Mutations will be disabled until the Event ends.)");
			p.spigot().sendMessage(cb.create());
		}
	}
	
	public void finishEvent() {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		
		for (Entry<SpleefPlayer, Integer> entry : this.total_points.entrySet()) {
		  hashmap.put(entry.getKey().getOfflinePlayer().getName(), entry.getValue());
		}
		
		hashmap= StatsManager.getManager().sortByValue(hashmap);
		
		
		int pos = 1;
		Bukkit.broadcastMessage("§d§lThe Event has finished!");
		Bukkit.broadcastMessage("§5-=-=-=-[§d§lFFA Event Positions§5]-=-=-=-");
		for (Entry<String, Integer> entry : hashmap.entrySet()) {
					if (pos==1) {
						 Bukkit.broadcastMessage("§6§l" +pos+". §b§l" + entry.getKey() + "§8§l: §e§l" + entry.getValue() +" Points");
					} else {
			 Bukkit.broadcastMessage("§6" +pos+". §b" + entry.getKey() + "§8: §e" + entry.getValue() +" Points");
					}
			pos++;
			}
		
		if (this.prizes) {
			
		}
		
	getArena().setEvent(null);
	}
	
}
