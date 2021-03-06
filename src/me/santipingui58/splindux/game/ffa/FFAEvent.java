package me.santipingui58.splindux.game.ffa;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.task.FFAEventFinishTask;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FFAEvent {

	
	private int currentRound;
	private int maxRounds;
	private HashMap<UUID,Integer> round_points = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> total_points = new HashMap<UUID,Integer>();
	
	private HashMap<UUID,Integer> teams_round_points = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> teams_total_points= new HashMap<UUID,Integer>();
	
	private boolean prizes;
	private UUID lastWinner;
	

	private SpleefType spleefType;
	
	public FFAEvent(SpleefType spleefType,int maxRounds,boolean prizes) {
		this.maxRounds = maxRounds;
		this.currentRound = 1;
		this.prizes = prizes;
		this.spleefType = spleefType;
	}
	
	public FFAArena getArena() {
		return GameManager.getManager().getFFAArena(spleefType);
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
	
	public UUID getLastWinner() {
		return this.lastWinner;
	}
	
	public int getMaxRounds() {
		return this.maxRounds;
	}
	
	public void addPoint(UUID sp,int p,boolean total) {
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
	
	public void addPoint2v2(UUID sp,int p, boolean total,boolean multiplier) {
		FFATeam team = getArena().getTeamByPlayer(sp);
		addPoint2v2(team,p,total,multiplier);
	}
	
	
	public void addPoint2v2(FFATeam team,int p,boolean total,boolean multiplier) {	
		
		if (multiplier && team.everyoneAlive() && !total) p = p+p;
		
		if (!total) {		
			team.getUUID();
		if (!this.teams_round_points.containsKey(team.getUUID())) this.teams_round_points.put(team.getUUID(), 0);
		
		int oldp = this.teams_round_points.get(team.getUUID());
		this.teams_round_points.put(team.getUUID(), oldp+p);		
	} else {
		if (!this.teams_total_points.containsKey(team.getUUID())) {
			this.teams_total_points.put(team.getUUID(), 0);
		}	
		int oldp = this.teams_total_points.get(team.getUUID());
		this.teams_total_points.put(team.getUUID(), oldp+p);
	}
	}
	
	
	public void finishRound() {
		
		if (Main.ffa2v2) {
			for (Entry<UUID, Integer> entry : this.teams_round_points.entrySet()) {
				addPoint2v2(getArena().getTeam(entry.getKey()),entry.getValue(),true,false);
			}
			
			this.teams_total_points = StatsManager.getManager().sortByValue(this.teams_total_points);
			
		} else {
		for (Entry<UUID, Integer> entry : this.round_points.entrySet()) {
			addPoint(entry.getKey(),entry.getValue(),true);
		}
		
		this.total_points = StatsManager.getManager().sortByValue(this.total_points);
		}
		
		String bc1 = "??d??l["+getArena().getSpleefType().getName() +" FFA Event] ??a??lRound ??b??l(" + this.currentRound+"/"+this.maxRounds+") ??a??lhas finished!";
		DataManager.getManager().broadcast(bc1,true);
		
		if (!this.prizes) {
			rankingBroadcast();
		} else {
			
			for (SpleefPlayer sp : getArena().getArena().getQueue()) {
				if (Main.ffa2v2) {
					FFATeam team = getArena().getTeamByPlayer(sp.getUUID());
					Utils.getUtils().sendTitles(sp, "??dRound ??b(" + this.currentRound+"/"+this.maxRounds+") ??dhas finished!", "??aPoints this round: " + this.teams_round_points.get(team.getUUID()), 10, 200, 10);
				} else {
					Utils.getUtils().sendTitles(sp, "??dRound ??b(" + this.currentRound+"/"+this.maxRounds+") ??dhas finished!", "??aPoints this round: " + getRoundPoints().get(sp.getUUID()), 10, 200, 10);
				}
			}
		}
		
		
		int pos = 1;
		
		if (Main.ffa2v2) {
			for (Entry<UUID, Integer> entry : this.teams_total_points.entrySet()) {				
				UUID uuid = entry.getKey();
				int points = entry.getValue();
				FFATeam team = getArena().getTeam(uuid);
				String bc3 = "??6" +pos+". ??b" + team.getName() + "??8: ??e" + points +" Points";
				Main.get().getLogger().info(bc3);	 
				pos++;
		}
		} else {
		for (Entry<UUID, Integer> entry : this.total_points.entrySet()) {				
			UUID uuid = entry.getKey();
			int points = entry.getValue();
			String bc3 = "??6" +pos+". ??b" + Bukkit.getOfflinePlayer(uuid).getName() + "??8: ??e" + points +" Points";
			Main.get().getLogger().info(bc3);	 
			pos++;
	}
		}
		
		nextRound();
	}
	
	private void rankingBroadcast() {
	
		
			String bc2 = "??5-=-=-=-[??d??l"+getArena().getSpleefType().getName() +" FFA Event Positions??5]-=-=-=-";
			DataManager.getManager().broadcast(bc2,true);
			
			
			int pos = 1;
			if (!Main.ffa2v2) {
			for (Entry<UUID, Integer> entry : this.total_points.entrySet()) {
				
						if (pos==1) this.lastWinner=entry.getKey();				
						String bc3 = "??6" +pos+". ??b" + Bukkit.getOfflinePlayer(entry.getKey()).getName() + "??8: ??e" + entry.getValue() +" Points";
						DataManager.getManager().broadcast(bc3,true);		
						
						
				pos++;
				}
			
			} else {
				for (Entry<UUID, Integer> entry : this.teams_total_points.entrySet()) {
					FFATeam team = getArena().getTeam(entry.getKey());
					if (pos==1) this.lastWinner=team.getPlayer1();		
					String bc3 = "??6" +pos+". ??b" +team.getName() + "??8: ??e" + entry.getValue() +" Points";
					DataManager.getManager().broadcast(bc3,true);			
			pos++;
			}
			}

			for (SpleefPlayer sp : getArena().getQueue()) {
				if (Main.ffa2v2) {
					FFATeam team = getArena().getTeamByPlayer(sp.getUUID());
				if (this.teams_total_points.containsKey(team.getUUID())) {
					Utils.getUtils().sendTitles(sp, "??6Total points: " +  getTotalPoints().get(team.getUUID()), "??aPoints this round: " + getRoundPoints().get(team.getUUID()), 10, 200, 10);
				}
				} else {
					if (getTotalPoints().containsKey(sp.getUUID())) {
						Utils.getUtils().sendTitles(sp, "??6Total points: " +  getTotalPoints().get(sp.getUUID()), "??aPoints this round: " + getRoundPoints().get(sp.getUUID()), 10, 200, 10);
					}
				}
			}
			
	}
	
	
	public HashMap<UUID,Integer> getTotalPoints() {
		return this.total_points;
	}
	
	
	public HashMap<UUID,Integer> getTotalPoints2v2() {
		return this.teams_total_points;
	}
	
	public HashMap<UUID,Integer> getRoundPoints2v2() {
		return this.teams_round_points;
	}
	
	public HashMap<UUID,Integer> getRoundPoints() {
		return this.round_points;
	}
	
	
	public void sendBroadcast() {
		TextComponent msg1 = new TextComponent("[Points]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.AQUA );
		msg1.setBold(true);
		TextComponent msg2 = new TextComponent("[Prizes]");
		msg2.setColor( net.md_5.bungee.api.ChatColor.GOLD );
		msg1.setBold(true);
		
		TextComponent a1 = new TextComponent("??aEvery round survived: ??d1 point\n");
		TextComponent a2 = new TextComponent("??aEvery kill: ??d7 points\n");
		TextComponent a3 = new TextComponent("??aKill 1st. Player: ??d8 points\n");
		TextComponent a4 = new TextComponent("??aEvery win: ??d15 points");
		ComponentBuilder acb = new ComponentBuilder(a1);
		acb.append(a2).append(a3).append(a4);
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, acb.create()));
		if (this.prizes) {
		TextComponent b1 = new TextComponent("??e1st. ??62000 Coins ??7+ ??b500 Spleef EXP ??7+ ??d10 Mutation Tokens\n");
		TextComponent b2 = new TextComponent("??e2nd. ??61800 Coins ??7+ ??b460 Spleef EXP ??7+ ??d7 Mutation Tokens\n");
		TextComponent b3 = new TextComponent("??e3rd. ??61600 Coins ??7+ ??b420 Spleef EXP ??7+ ??d5 Mutation Tokens\n");
		TextComponent b4 = new TextComponent("??e4th. ??61400 Coins ??7+ ??b380 Spleef EXP\n");
		TextComponent b5 = new TextComponent("??e5th. ??61200 Coins ??7+ ??b340 Spleef EXP\n");
		TextComponent b6 = new TextComponent("??e6th. ??61000 Coins ??7+ ??b300 Spleef EXP\n");
		TextComponent b7 = new TextComponent("??e7th. ??6800 Coins ??7+ ??b260 Spleef EXP\n");
		TextComponent b8 = new TextComponent("??e8th. ??6600 Coins ??7+ ??b220 Spleef EXP\n");
		TextComponent b9 = new TextComponent("??e9th. ??6400 Coins ??7+ ??b180 Spleef EXP\n");
	  TextComponent b10 = new TextComponent("??e10th. ??6200 Coins ??7+ ??b140 Spleef EXP\n");
	  TextComponent b11 = new TextComponent("??eParticipating. ????6100 Coins ??7+ ??b100 Spleef EXP");
		ComponentBuilder bcb = new ComponentBuilder(b1);
		bcb.append(b2).append(b3).append(b4).append(b5).append(b6).append(b7).append(b8).append(b9).append(b10).append(b11);
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,bcb.create()));
		}
		
		ComponentBuilder cb = new ComponentBuilder(msg1);
		if (this.prizes) {
		cb.append(" ");
		cb.append(msg2);
		}
		
		String bc1 = "??d??lA new "+ getArena().getSpleefType().getName() +"FFA Event will start in the next round! Join FFA to participate and get many points as you can! The Event will last for ??a??l"+ this.maxRounds+ " rounds??d??l!";
		String bc2 = "??7(Rank snowballs and Mutations will be disabled until the Event ends.)";
		DataManager.getManager().broadcast(bc1,true);
		DataManager.getManager().broadcast(bc2,true);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.spigot().sendMessage(cb.create());
		}
	}
	
	
	
	
	public void finishEvent() {
		
		
		LinkedHashMap<UUID,Integer> hashmap = new LinkedHashMap<UUID,Integer>();

		if (Main.ffa2v2) {
			hashmap.putAll(this.teams_total_points);
		}else {
			hashmap.putAll(this.total_points);
		}
		
		hashmap= StatsManager.getManager().sortByValueLinked(hashmap);
	
		int max = Integer.MAX_VALUE;
		int i = 1;
		for (Entry<UUID, Integer> entry : hashmap.entrySet()) {
			if (i==1) {
				max = entry.getValue();
			} else if (max<=entry.getValue()) {
				tieBreakerRound();
				return;
			} 
			i++;
		}
		
		
		
	
		
		if (this.prizes) {
			new FFAEventFinishTask(this);		
		} else {
			int pos = 1;
			String winner = null;
			
			String bc1 = "??d??lThe Event has finished!";
			String bc2 ="??5-=-=-=-[??d??l"+getArena().getSpleefType().getName() +" FFA Event Positions??5]-=-=-=-";
			DataManager.getManager().broadcast(bc1,true);
			DataManager.getManager().broadcast(bc2,true);
			
			
			for (Entry<UUID, Integer> entry : hashmap.entrySet()) {
				
				String name = Main.ffa2v2 ? getArena().getTeam(entry.getKey()).getName() : Bukkit.getOfflinePlayer(entry.getKey()).getName();
				String bc3 = "??6??l" +pos+". ??b??l" + name + "??8??l: ??e??l" + entry.getValue() +" Points";
				String bc4 = "??6" +pos+". ??b" + name + "??8: ??e" + entry.getValue() +" Points";
				
						if (pos==1) {
							winner = name;
							 DataManager.getManager().broadcast(bc3,true);	 
						} else {
							 DataManager.getManager().broadcast(bc4,true);
						}
				pos++;
				}
			
			
			
			DataManager.getManager().broadcast("??d??lCongratulations to ??b??l" + winner + " ??d??l for winning the "+getArena().getSpleefType().getName() +" FFA Event!",true);
		}
		
		getArena().setEvent(null);
	}

	
	
	public void tieBreakerRound() {
		DataManager.getManager().broadcast("??a??lThere is a tie in the #1 place! A tie breaker round will start.",true);
		
	}
	
	public void givePrizes() {			
		
		if (Main.ffa2v2) {
			for (UUID u : this.teams_total_points.keySet()) {  
				FFATeam team = getArena().getTeam(u);
				for (UUID s : team.getPlayers()) {
				OfflinePlayer pa = Bukkit.getOfflinePlayer(s);
				SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
				if (temp==null) {
					 new SpleefPlayer(pa.getUniqueId());
					HikariAPI.getManager().loadData(pa.getUniqueId());
				}	
		   }
			}
		} else {
		   for (UUID s : this.total_points.keySet()) {  
				OfflinePlayer pa = Bukkit.getOfflinePlayer(s);
				SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
				if (temp==null) {
					 new SpleefPlayer(pa.getUniqueId());
					HikariAPI.getManager().loadData(pa.getUniqueId());
				}	
		   }
		}
		   
			new BukkitRunnable() {
				public void run() {		
					int i = 1;
					Set<UUID> set = Main.ffa2v2 ? teams_total_points.keySet() : total_points.keySet();
		   	for (UUID s : set) {
		   		if (Main.ffa2v2) {
		   			FFATeam team = getArena().getTeam(s);
		   			for (UUID u : team.getPlayers()) {
		   		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(u);
			    		giveReward(sp,i);	
		   			}
		   		  i++;
						}	else {
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(s);
				    		giveReward(sp,i);
				    			  i++;
						}
		   	}
	    			 }
			   	   
		   }.runTaskLaterAsynchronously(Main.get(), 20L);
}
	
	private void giveReward(SpleefPlayer sp,int i) {
		int tokens = 0;	    			
		if (i==1) tokens = 10;
		else if (i==2) tokens =7;
		else if (i==3) tokens = 5;
		
		sp.setMutationTokens(sp.getMutationTokens()+tokens);
		
		if (i<=10) {
		 switch(i) {
		case 1:
			LevelManager.getManager().addLevel(sp, 500);
			sp.addCoins(2000);
			break;
		case 2:
			LevelManager.getManager().addLevel(sp, 460);
			sp.addCoins(1800);
			break;
		case 3:
			LevelManager.getManager().addLevel(sp, 420);
			sp.addCoins(1600);
			break;
		case 4:
			LevelManager.getManager().addLevel(sp, 380);
			sp.addCoins(1400);
			break;
		case 5:
			LevelManager.getManager().addLevel(sp, 340);
			sp.addCoins(1200);
			break;
		case 6:
			LevelManager.getManager().addLevel(sp, 300);
			sp.addCoins(1000);
			break;
		case 7:
			LevelManager.getManager().addLevel(sp, 260);
			sp.addCoins(800);
			break;
		case 8:
			LevelManager.getManager().addLevel(sp, 220);
			sp.addCoins(600);
			break;
		case 9:
			LevelManager.getManager().addLevel(sp, 180);
			sp.addCoins(400);
			break;
		case 10:
			LevelManager.getManager().addLevel(sp, 140);
			sp.addCoins(200);
			break;
		 }
		} else {
			LevelManager.getManager().addLevel(sp, 100);
			sp.addCoins(100);
		}
	}


	
}