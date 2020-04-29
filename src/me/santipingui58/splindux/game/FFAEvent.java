package me.santipingui58.splindux.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.level.LevelManager;
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
			if (arena.getEvent()!=null) {
			if (arena.getEvent().equals(this)) {
				return arena;
			}
			}
		}
		return null;
	}
	
	public void sendBroadcast() {
		TextComponent msg1 = new TextComponent("[Points]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.AQUA );
		msg1.setBold(true);
		TextComponent msg2 = new TextComponent("[Prizes]");
		msg2.setColor( net.md_5.bungee.api.ChatColor.GOLD );
		msg1.setBold(true);
		
		TextComponent a1 = new TextComponent("§aEvery round survived: §d1 point\n");
		TextComponent a2 = new TextComponent("§aEvery kill: §d3 points\n");
		TextComponent a3 = new TextComponent("§aEvery win: §d7 points");
		ComponentBuilder acb = new ComponentBuilder(a1);
		acb.append(a2).append(a3);
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, acb.create()));
		
		if (!this.prizes) {
		TextComponent b1 = new TextComponent("§e1st. §bSplinbox 5 Stars §7+ §b200 Spleef EXP §7+ §63 Mutation Tokens\n");
		TextComponent b2 = new TextComponent("§e2nd. §bSplinbox 4 Stars §7+ §b190 Spleef EXP §7+ §62 Mutation Tokens\n");
		TextComponent b3 = new TextComponent("§e3rd. §bSplinbox 4 Stars §7+ §b180 Spleef EXP §7+ §61 Mutation Tokens\n");
		TextComponent b4 = new TextComponent("§e4th. §bSplinbox 3 Stars §7+ §b170 Spleef EXP\n");
		TextComponent b5 = new TextComponent("§e5th. §bSplinbox 3 Stars §7+ §b160 Spleef EXP\n");
		TextComponent b6 = new TextComponent("§e6th. §bSplinbox 2 Stars §7+ §b150 Spleef EXP\n");
		TextComponent b7 = new TextComponent("§e7th. §bSplinbox 2 Stars §7+ §b140 Spleef EXP\n");
		TextComponent b8 = new TextComponent("§e8th. §bSplinbox 2 Stars §7+ §b130 Spleef EXP\n");
		TextComponent b9 = new TextComponent("§e9th. §bSplinbox 1 Stars §7+ §b120 Spleef EXP\n");
	  TextComponent b10 = new TextComponent("§e10th. §bSplinbox 1 Stars §7+ §b110 Spleef EXP");
		ComponentBuilder bcb = new ComponentBuilder(b1);
		bcb.append(b2).append(b3).append(b4).append(b5).append(b6).append(b7).append(b8).append(b9).append(b10);
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,bcb.create()));
		}
		
		ComponentBuilder cb = new ComponentBuilder(msg1);
		if (!this.prizes) {
		cb.append(" ");
		cb.append(msg2);
		}
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
	
		int max = Integer.MAX_VALUE;
		int i = 1;
		for (Entry<String, Integer> entry : hashmap.entrySet()) {
			if (i==1) {
				max = entry.getValue();
			} else if (max<=entry.getValue()) {
				tieBreakerRound();
				return;
			} 
			i++;
		}
		
		
		int pos = 1;
		List<String> top10 = new ArrayList<String>();
		Bukkit.broadcastMessage("§d§lThe Event has finished!");
		Bukkit.broadcastMessage("§5-=-=-=-[§d§lFFA Event Positions§5]-=-=-=-");
		for (Entry<String, Integer> entry : hashmap.entrySet()) {
			if (pos<=10) {
				top10.add(entry.getKey());
			}
					if (pos==1) {
						 Bukkit.broadcastMessage("§6§l" +pos+". §b§l" + entry.getKey() + "§8§l: §e§l" + entry.getValue() +" Points");
					} else if (!entry.getKey().equalsIgnoreCase("NO_PLAYER")){
			 Bukkit.broadcastMessage("§6" +pos+". §b" + entry.getKey() + "§8: §e" + entry.getValue() +" Points");
					}
			pos++;
			}
		
		if (this.prizes) {
			givePrizes(top10);
		}
		
	getArena().setEvent(null);
	}
	
	public void tieBreakerRound() {
		Bukkit.broadcastMessage("§a§lThere is a tie in the #1 place! A tie breaker round will start.");
	}
	
	public void givePrizes(List<String> top10) {
			int i = 1;
		   for (String s : top10) {
			   @SuppressWarnings("deprecation")
			OfflinePlayer p = Bukkit.getOfflinePlayer(s);
			   SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			   
	    			int stars = 0;
	    			int tokens = 0;
	    			if (i==1) stars = 5; 
	    			else if (i<=3) stars = 4;
	    			else if (i<=5) stars = 3;
	    			else if (i<=2) stars = 2;	    			
	    			else stars = 1;
	    			
	    			if (i==1) tokens = 3;
	    			else if (i==2) tokens =2;
	    			else if (i==3) tokens = 3;
	    			
	    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gmysteryboxes give " + p.getName()+ " 1 "  + stars);
	    			sp.setMutationTokens(sp.getMutationTokens()+tokens);
	    			
	    			 switch(i) {
	    			case 1:LevelManager.getManager().addLevel(sp, 250);
	    			case 2:LevelManager.getManager().addLevel(sp, 230);
	    			case 3:LevelManager.getManager().addLevel(sp, 190);
	    			case 4:LevelManager.getManager().addLevel(sp, 170);
	    			case 5:LevelManager.getManager().addLevel(sp, 150);
	    			case 6:LevelManager.getManager().addLevel(sp, 130);
	    			case 7:LevelManager.getManager().addLevel(sp, 110);
	    			case 8:LevelManager.getManager().addLevel(sp, 90);
	    			case 9:LevelManager.getManager().addLevel(sp, 70);
	    			case 10:LevelManager.getManager().addLevel(sp, 50);
			   }
			   i++;
		   }
	}
}
