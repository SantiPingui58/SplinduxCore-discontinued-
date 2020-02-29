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
	private HashMap<SpleefPlayer,Integer> packets = new HashMap<SpleefPlayer,Integer>();
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
 
 public HashMap<SpleefPlayer,Integer> getPacketList() {
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
		if (type.equals(HologramType.SPLEEFRANKING)) {
			Location l = new Location(location.getWorld(),location.getX(),location.getY(),location.getZ());
			 if (!getChangeType().containsKey(sp)) {
				getChangeType().put(sp, SpleefRankingType.WINS);
			 } 
			 
			  String amount = StatsManager.getManager().getAmountByType(fromSpleefRankingType(changeType.get(sp)));
			id.add(line(l,sp,"§b§lSpleef FFA Ranking"));
			l.add(0, -0.25, 0);
			id.add(line(l,sp,"§aAll time - " + StatsManager.getManager().getAmountByType(fromSpleefRankingType(changeType.get(sp)))));
			l.add(0, -0.25, 0);
			int i = 1;
			l.add(0, -0.25, 0);			
			HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
			if (changeType.get(sp).equals(SpleefRankingType.WINS)) {
			hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS);
			} else if (changeType.get(sp).equals(SpleefRankingType.GAMES)) {
				hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_GAMES);
			}else if (changeType.get(sp).equals(SpleefRankingType.KILLS)) {
				hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_KILLS);
			}
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
			        	if (i==7) {
			        		packets.put(sp, iD);
			        	}
			        	l.add(0, -0.25, 0);
			        	i++;
			        } else {
			        	break;
			        }
			    }
			    l.add(0, -0.25, 0);
			    int rank = StatsManager.getManager().getRankingPosition(fromSpleefRankingType(changeType.get(sp)),sp);
			    if (!ranking) {
			    id.add(line(l,sp,"§6§l"+rank+". §b§l"+sp.getOfflinePlayer().getName()+" §7§l- §e§l" + getAmount(sp) + " " + amount));
			    } else {
			    	 id.add(line(l,sp,"§f"));
			    }
			    l.add(0, -0.25, 0);
			    l.add(0, -0.25, 0);
			    id.add(line(l,sp,"§6§lChange Period (CLICK HERE)"));
			    l.add(0, -0.25, 0);
			    id.add(line(l,sp,"§aAll time §7- Monthly - Weekly"));
			    l.add(0, -0.25, 0);
			    id.add(line(l,sp,"§6§lChange Type (CLICK HERE)"));
			    l.add(0, -0.25, 0);
			    if (changeType.get(sp).equals(SpleefRankingType.WINS)) {
			    	id.add(line(l,sp,"§a§oWins §7- Kills - Games - K/G - W/G"));
					} else if (changeType.get(sp).equals(SpleefRankingType.GAMES)) {
						id.add(line(l,sp,"§7Wins - Kills - §a§oGames §7- K/G - W/G"));
					}	  else if (changeType.get(sp).equals(SpleefRankingType.KILLS)) {
						id.add(line(l,sp,"§7Wins - §a§oKills §7- Games - K/G - W/G"));
					}	 
			   
			 
		}

	
	ids.put(sp, id);
		}
	}.runTaskLater(Main.get(), 1L);
	}	
	
	
	private String getAmount(SpleefPlayer sp) {
		SpleefRankingType type = changeType.get(sp);
		if (type.equals(SpleefRankingType.WINS)) {
			return String.valueOf(sp.getFFAWins());
		} else if (type.equals(SpleefRankingType.GAMES)) {
			return String.valueOf(sp.getFFAGames());
		}else if (type.equals(SpleefRankingType.KILLS)) {
			return String.valueOf(sp.getFFAKills());
		} else if (type.equals(SpleefRankingType.KG)) {
			return String.valueOf(sp.getKillGameRatio());
		}else if (type.equals(SpleefRankingType.WG)) {
			return String.valueOf(sp.getWinGameRatio());
		}
		return null;
	}
	private RankingType fromSpleefRankingType(SpleefRankingType srt) {
		if (srt.equals(SpleefRankingType.WINS)) {
			return RankingType.SPLEEFFFA_WINS;
		} else if (srt.equals(SpleefRankingType.GAMES)) {
			return RankingType.SPLEEFFFA_GAMES;
		}if (srt.equals(SpleefRankingType.KILLS)) {
			return RankingType.SPLEEFFFA_KILLS;
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
