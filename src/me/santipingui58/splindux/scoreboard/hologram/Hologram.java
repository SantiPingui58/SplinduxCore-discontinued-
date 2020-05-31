package me.santipingui58.splindux.scoreboard.hologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.RankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.utils.Utils;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;

public class Hologram {

	private HologramType type;
	private Location location;
	private UUID uuid;
	private HashMap<SpleefPlayer,List<Integer>> ids = new HashMap<SpleefPlayer,List<Integer>>();
	private HashMap<SpleefPlayer,SpleefRankingType> changeType = new HashMap<SpleefPlayer,SpleefRankingType>();
	private HashMap<SpleefPlayer,SpleefRankingPeriod> changePeriod = new HashMap<SpleefPlayer,SpleefRankingPeriod>();
	private HashMap<SpleefPlayer,HashMap<PacketType,Integer>> packets = new HashMap<SpleefPlayer,HashMap<PacketType,Integer>>();
	public Hologram(UUID uuid,Location l,HologramType type) {
		this.uuid = uuid;
		this.type = type;
		this.location = l;
	}
	
	public HologramType getType() {
		return this.type;
	}
 public Location getLocation() {
	 return this.location;
 }
 
 public UUID getUUID() {
	 return this.uuid;
 }
	
 public HashMap<SpleefPlayer,List<Integer>> getIDList() {
	 return this.ids;
 }
 
 public HashMap<SpleefPlayer,SpleefRankingType> getChangeType() {
	 return this.changeType;
 }
 
 public HashMap<SpleefPlayer, SpleefRankingPeriod> getChangePeriod() {
	 return this.changePeriod;
 }
 
 public HashMap<SpleefPlayer,HashMap<PacketType,Integer>> getPacketList() {
	 return this.packets;
 }
 
	public int line(Location loc,SpleefPlayer sp,String text) {
		try {
        WorldServer s = ((CraftWorld)loc.getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(s);
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName(text);
        stand.setCustomNameVisible(true);
        stand.setNoGravity(true);
        stand.setInvisible(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        ((CraftPlayer)sp.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        return stand.getId();
		} catch (Exception e) {}
		return 0;
    }
	
	
	public void spawn(SpleefPlayer sp) {		
		delete(sp);
		new BukkitRunnable() {
			public void run() {
		List<Integer> id =  new ArrayList<Integer>();
		Location l = new Location(location.getWorld(),location.getX(),location.getY(),location.getZ());
		int i = 1;
		if (type.equals(HologramType.SPLEEFRANKING)) {	
			 if (!getChangeType().containsKey(sp)) {
				getChangeType().put(sp, SpleefRankingType.WINS);
			 } 
			 
			 if (!getChangePeriod().containsKey(sp)) {
					getChangePeriod().put(sp, SpleefRankingPeriod.ALL_TIME);
				 } 
			 
			 if (!getPacketList().containsKey(sp)) {
				 getPacketList().put(sp, new HashMap<PacketType,Integer>());
				 } 
			  String amount = StatsManager.getManager().getAmountByType(fromSpleefRankingType(changeType.get(sp),changePeriod.get(sp)));
			id.add(line(l,sp,"§b§lSpleef FFA Ranking"));
			l.add(0, -0.25, 0);
			id.add(line(l,sp,"§a" +  StatsManager.getManager().getAmountByPeriod(changePeriod.get(sp))+" - " + StatsManager.getManager().getAmountByType(fromSpleefRankingType(changeType.get(sp),changePeriod.get(sp)))));
			l.add(0, -0.25, 0);
			
			l.add(0, -0.25, 0);			
			HashMap<String,Integer> hashmap = getHashMap(sp);

		
			 Iterator<Entry<String, Integer>> it = hashmap.entrySet().iterator();
			 boolean ranking = false;
			 boolean ranking_b = false;
			    while (it.hasNext()) {
			    	Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
			        String name = pair.getKey();
			        Integer wins = pair.getValue();
			       if (name.equalsIgnoreCase(sp.getOfflinePlayer().getName())) {
			    	   ranking = true;			
			    	   ranking_b =true;
			       }
			      			       
			        if (i<=10) {
			        	int iD = 0;
			        	if (ranking_b && ranking) {
			        		iD =line(l,sp,"§6§l"+i+". §b§l"+name+" §7§l- §e§l" + wins + " " + amount);
			        		ranking_b = false;
			        	} else {
			        	iD = line(l,sp, "§6"+i+". §b"+name+" §7- §e" + wins + " " + amount);
			        	}
			        	id.add(iD);
			        	if (i==7 || i==5) {
			        		HashMap<PacketType,Integer> packet = new HashMap<PacketType,Integer>();
			        		if (packets.containsKey(sp)) {
			        			packet = packets.get(sp);
			        		}
			        		if (i==7) {
			        		packet.put(PacketType.TYPE, iD);
			        		} else {
			        			packet.put(PacketType.PERIOD, iD);
			        		}
			        	}
			        	l.add(0, -0.25, 0);
			        	i++;
			        } else {
			        	break;
			        }
			    }
			    l.add(0, -0.25, 0);
			    int rank = StatsManager.getManager().getRankingPosition(fromSpleefRankingType(changeType.get(sp),changePeriod.get(sp)),sp);
			    if (!ranking) {
			    id.add(line(l,sp,"§6§l"+rank+". §b§l"+sp.getOfflinePlayer().getName()+" §7§l- §e§l" + getAmount(sp) + " " + amount));
			    } else {
			    	 id.add(line(l,sp,"§f"));

			    }
			    l.add(0, -0.25, 0);
			    l.add(0, -0.25, 0);
			    id.add(line(l,sp,"§6§lChange Period (CLICK HERE)"));
			    l.add(0, -0.25, 0);
			    if (changePeriod.get(sp).equals(SpleefRankingPeriod.ALL_TIME)) {
			    	id.add(line(l,sp,"§aAll time §7- Monthly - Weekly"));
					} else if (changePeriod.get(sp).equals(SpleefRankingPeriod.MONTHLY)) {
						id.add(line(l,sp,"§7All time - §aMonthly §7- Weekly"));
					}	  else if (changePeriod.get(sp).equals(SpleefRankingPeriod.WEEKLY)) {
						id.add(line(l,sp,"§7All time - Monthly - §aWeekly"));
					}			    
			    l.add(0, -0.25, 0);
			    id.add(line(l,sp,"§6§lChange Type (CLICK HERE)"));
			    l.add(0, -0.25, 0);
			    if (changeType.get(sp).equals(SpleefRankingType.WINS)) {
			    	id.add(line(l,sp,"§a§oWins §7- Kills - Games"));
					} else if (changeType.get(sp).equals(SpleefRankingType.GAMES)) {
						id.add(line(l,sp,"§7Wins - Kills - §a§oGames"));
					}	  else if (changeType.get(sp).equals(SpleefRankingType.KILLS)) {
						id.add(line(l,sp,"§7Wins - §a§oKills §7- Games"));
					}	 
			   
			 
		} else if (type.equals(HologramType.ONLINETIME)) {
			id.add(line(l,sp,"§b§lTotal Online Time"));
		    l.add(0, -0.25, 0);
			HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
			hashmap = StatsManager.getManager().getRanking(RankingType.TOTALONLINETIME);
			 boolean ranking = false;
			Iterator<Entry<String, Integer>> it = hashmap.entrySet().iterator();
			    while (it.hasNext()) {
			    	Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
			        String name = pair.getKey();
			        Integer wins = pair.getValue();
				
			        if (name.equalsIgnoreCase(sp.getOfflinePlayer().getName())) {
				    	   ranking = true;			
				       }
			        if (i<=20) {
			        	id.add(line(l,sp, "§6"+i+". §b"+name+" §7- §e" + Utils.getUtils().minutesToDate(wins)));			        	
			        	l.add(0, -0.25, 0);
			        	i++;
			        } else {
			        	break;
			        }
			    }
			    
			    l.add(0, -0.25, 0);
			    int rank = StatsManager.getManager().getRankingPosition(RankingType.TOTALONLINETIME, sp);
			    if (!ranking) {
			    id.add(line(l,sp,"§6§l"+rank+". §b§l"+sp.getOfflinePlayer().getName()+" §7§l- §e§l" + " " + Utils.getUtils().minutesToDate(sp.getTotalOnlineTime())));
			    } else {
			    	 id.add(line(l,sp,"§f"));

			    }
			    l.add(0, -0.25, 0);
		} else if (type.equals(HologramType.VOTES)) {
			id.add(line(l,sp,"§e§lRewards and Votes"));   
		    if (sp.hasUnclaimedRewards()) {
		    	 l.add(0, -0.25, 0);
		    	 id.add(line(l,sp,"§bYou have unclaimed rewards!"));
		    }
		}	
	ids.put(sp, id);
		}
	}.runTaskLater(Main.get(), 3L);
	}	
	
	
	private HashMap<String,Integer> getHashMap(SpleefPlayer sp) {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		if (changeType.get(sp).equals(SpleefRankingType.WINS)) {
			if (changePeriod.get(sp).equals(SpleefRankingPeriod.ALL_TIME)) {
			hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS); 
			} else if (changePeriod.get(sp).equals(SpleefRankingPeriod.MONTHLY)) {
				hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_MONTHLY); 
			} else if (changePeriod.get(sp).equals(SpleefRankingPeriod.WEEKLY)) {
				hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_WEEKLY); 
			}
			} else if (changeType.get(sp).equals(SpleefRankingType.GAMES)) {
				if (changePeriod.get(sp).equals(SpleefRankingPeriod.ALL_TIME)) {
					hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_GAMES); 
					} else if (changePeriod.get(sp).equals(SpleefRankingPeriod.MONTHLY)) {
						hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_GAMES_MONTHLY); 
					} else if (changePeriod.get(sp).equals(SpleefRankingPeriod.WEEKLY)) {
						hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_GAMES_WEEKLY); 
					}
			}else if (changeType.get(sp).equals(SpleefRankingType.KILLS)) {
				if (changePeriod.get(sp).equals(SpleefRankingPeriod.ALL_TIME)) {
					hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_KILLS); 
					} else if (changePeriod.get(sp).equals(SpleefRankingPeriod.MONTHLY)) {
						hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_KILLS_MONTHLY); 
					} else if (changePeriod.get(sp).equals(SpleefRankingPeriod.WEEKLY)) {
						hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_KILLS_WEEKLY);
					}
			}
		
		return hashmap;
	}
	
	
	private String getAmount(SpleefPlayer sp) {
		SpleefRankingType srt = changeType.get(sp);
		SpleefRankingPeriod srp = changePeriod.get(sp);
		if (srt.equals(SpleefRankingType.WINS)) {
			if (srp.equals(SpleefRankingPeriod.ALL_TIME)) {
			return String.valueOf(sp.getFFAWins());
			} else if (srp.equals(SpleefRankingPeriod.MONTHLY)) {
				return String.valueOf(sp.getMonthlyFFAWins());
			}else if (srp.equals(SpleefRankingPeriod.WEEKLY)) {
				return String.valueOf(sp.getWeeklyFFAWins());
			}
		} else if (srt.equals(SpleefRankingType.GAMES)) {
			if (srp.equals(SpleefRankingPeriod.ALL_TIME)) {
				return String.valueOf(sp.getFFAGames());
				} else if (srp.equals(SpleefRankingPeriod.MONTHLY)) {
					return String.valueOf(sp.getMonthlyFFAGames());
				}else if (srp.equals(SpleefRankingPeriod.WEEKLY)) {
					return String.valueOf(sp.getWeeklyFFAGames());
				}
		}  if (srt.equals(SpleefRankingType.KILLS)) {
			if (srp.equals(SpleefRankingPeriod.ALL_TIME)) {
				return String.valueOf(sp.getFFAKills());
				} else if (srp.equals(SpleefRankingPeriod.MONTHLY)) {
					return String.valueOf(sp.getMonthlyFFAKills());
				}else if (srp.equals(SpleefRankingPeriod.WEEKLY)) {
					return String.valueOf(sp.getWeeklyFFAKills());
				}
		}
		return null;
	}
	private RankingType fromSpleefRankingType(SpleefRankingType srt,SpleefRankingPeriod srp) {
		if (srt.equals(SpleefRankingType.WINS)) {
			if (srp.equals(SpleefRankingPeriod.ALL_TIME)) {
			return RankingType.SPLEEFFFA_WINS;
			} else if (srp.equals(SpleefRankingPeriod.MONTHLY)) {
				return RankingType.SPLEEFFFA_WINS_MONTHLY;
			}else if (srp.equals(SpleefRankingPeriod.WEEKLY)) {
				return RankingType.SPLEEFFFA_WINS_WEEKLY;
			}
		} else if (srt.equals(SpleefRankingType.GAMES)) {
			if (srp.equals(SpleefRankingPeriod.ALL_TIME)) {
				return RankingType.SPLEEFFFA_GAMES;
				} else if (srp.equals(SpleefRankingPeriod.MONTHLY)) {
					return RankingType.SPLEEFFFA_GAMES_MONTHLY;
				}else if (srp.equals(SpleefRankingPeriod.WEEKLY)) {
					return RankingType.SPLEEFFFA_GAMES_WEEKLY;
				}
		}  if (srt.equals(SpleefRankingType.KILLS)) {
			if (srp.equals(SpleefRankingPeriod.ALL_TIME)) {
				return RankingType.SPLEEFFFA_KILLS;
				} else if (srp.equals(SpleefRankingPeriod.MONTHLY)) {
					return RankingType.SPLEEFFFA_KILLS_MONTHLY;
				}else if (srp.equals(SpleefRankingPeriod.WEEKLY)) {
					return RankingType.SPLEEFFFA_KILLS_WEEKLY;
				}
		}
		return null;
	}
	
	public void delete(SpleefPlayer sp) {
		if (this.ids.containsKey(sp)) {
		for (int id : this.ids.get(sp)) {
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(id);
            ((CraftPlayer)sp.getPlayer()).getHandle().playerConnection.sendPacket(destroy);
		}
	}
	}
}
