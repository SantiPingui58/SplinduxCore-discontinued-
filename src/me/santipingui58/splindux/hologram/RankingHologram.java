package me.santipingui58.splindux.hologram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.stats.RankingEnum;
import me.santipingui58.splindux.stats.SpleefRankingPeriod;
import me.santipingui58.splindux.stats.SpleefRankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.level.LevelManager;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;

public class RankingHologram {

	private Location location;
	private HologramType type;
	 private HashMap<UUID,HologramViewer> viewers = new HashMap<UUID,HologramViewer>(); 
	
	
	public RankingHologram(Location l,HologramType type) {
		this.location = l;
		this.type = type;
	}
	
	
	 public HashMap<UUID,HologramViewer> getViewersCache() {
		 return this.viewers;
	 }
	
public HologramType getType() {
	return this.type;
}
	 
	public Set<HologramViewer> getViewers() {
		return new HashSet<HologramViewer>(this.viewers.values());
	}
	
	
	
 public Location getLocation() {
	 return this.location;
 }
 
	public void setLocation(Location location2) {
		this.location = location2;
		
	}
 
 
	public void loadChunk(Location l) {
		new BukkitRunnable() {
			public void run() {
				if (!l.getChunk().isLoaded()) {
					l.getChunk().load();
				}
			}
		}.runTask(Main.get());
	}

 
	public EntityArmorStand line(Location loc,Player p,String text) {
		try {
        WorldServer s = ((CraftWorld)loc.getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(s);
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName(text);
        stand.setCustomNameVisible(true);
        stand.setNoGravity(true);
        stand.setInvisible(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        return stand;
		} catch (IllegalStateException e) {}
		return null;
    }
	
	
	public void update(HologramViewer viewer) {
		delete(viewer);	
		
		Location left = new Location(location.getWorld(),location.getX(),location.getY(),location.getZ()-5);
		Location mid = new Location(location.getWorld(),location.getX(),location.getY(),location.getZ());
		Location right = new Location(location.getWorld(),location.getX(),location.getY(),location.getZ()+5);
			new BukkitRunnable() {
				public void run() {
		if (!left.getChunk().isLoaded()) left.getChunk().load();
		if (!mid.getChunk().isLoaded()) right.getChunk().load();
		if (!mid.getChunk().isLoaded()) right.getChunk().load();
		new BukkitRunnable() {
			public void  run() { 
		if (getType().equals(HologramType.GAME_STATS)) {
		RankingEnum ranking = getRanking(viewer,SpleefType.SPLEEF);		
			generateRanking(viewer,mid,ranking,true);
			ranking = getRanking(viewer,SpleefType.SPLEGG);
			generateRanking(viewer,left,ranking,false);
			ranking = getRanking(viewer,SpleefType.TNTRUN);
			generateRanking(viewer,right,ranking,false);
		} else {
			RankingEnum ranking = getRanking(viewer,null);
			generateRanking(viewer,mid,ranking,true);
		}
		}
		}.runTaskLaterAsynchronously(Main.get(), 2L);
				}
			}.runTask(Main.get());
		
	}
	
	
	public RankingEnum getRanking(HologramViewer hv,SpleefType spleefType) {
		
		if (this.getType().equals(HologramType.GAME_STATS)) {
			GameType gameType = hv.getHologramSubType().equals(HologramSubType.FFA) ? GameType.FFA : GameType.DUEL;
			SpleefRankingPeriod periodType = hv.getPeriod();
			SpleefRankingType spleefRankingType = hv.getSpleefRankingType();
			return RankingEnum.getRankingEnum(spleefType, gameType, periodType, spleefRankingType);		
		} else {
			switch (hv.getHologramSubType()) {
			case EXP:
				return RankingEnum.EXP;
			case GUILD:
				return RankingEnum.GUILD_VALUE;
			case TIME:
				return RankingEnum.TOTALONLINETIME;
			case COINS:
				return RankingEnum.COINS;
			case RANKING:
				return RankingEnum.RANKING;
			case VOTES:
				return RankingEnum.VOTES;
			default:
				return null;	
			}
		}
	}
	
	
	public void delete(HologramViewer viewer) {
		if (!viewer.getHologramIds().containsKey(this.type)) return;
		Player p = Bukkit.getPlayer(viewer.getPlayer());
		Set<HologramID> s = new HashSet<HologramID>();
		s.addAll(viewer.getHologramIds().get(this.type));
		for (HologramID id : s) {
			try {
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(id.getValue().getId());
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(destroy);
			} catch(Exception ex) {}
		}
		 viewer.cleanHologramsId(getType());
	}
	
	
	private void generateRanking(HologramViewer hv, Location location, RankingEnum ranking,boolean hasChangeMode) {
		if (hv.getHologramIds().get(type)==null) hv.getHologramIds().put(this.type, new HashSet<HologramID>());
		String a = "§a§l"+hv.getHologramSubType().toString();
		if (this.getType().equals(HologramType.GAME_STATS)) 
			 a = ranking.getSpleefType().getName() + " " + ranking.getGameType() + " " + ranking.getSpleefRankingType();
			
			
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(hv.getPlayer());
		Player p = sp.getPlayer();
		HashMap<HologramType, Set<HologramID>> h = hv.getHologramIds();
		Set<HologramID> r = h.get(type);
		
				EntityArmorStand l = line(location,p,a);
				r.add(new HologramID(l,false,false));

    	location.add(0,-0.25,0);
		
		
		String playerName = Bukkit.getOfflinePlayer(hv.getPlayer()).getName();
		String subname = "";

			
		
		String amount = StatsManager.getManager().getAmountByType(ranking);
		boolean istop10 = false;
		LinkedHashMap<UUID,Integer> hashmap = new LinkedHashMap<UUID,Integer>(StatsManager.getManager().getRanking(ranking));
		Iterator<Entry<UUID, Integer>> it = StatsManager.getManager().getTop20HashMap(hashmap, 0,  ranking).entrySet().iterator();
		
		int pos = 1;
		boolean primaryButton = false;
		boolean secondaryButton = false;
		
	    while (it.hasNext()) {
	    
	    	
	        Map.Entry<UUID,Integer> pair = (Map.Entry<UUID,Integer>)it.next();
	    	OfflinePlayer player = Bukkit.getOfflinePlayer(pair.getKey());
	    	
	    	
	    	SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(player);
			if (temp==null) {
				 new SpleefPlayer(player.getUniqueId());
				HikariAPI.getManager().loadData(player.getUniqueId());	
			}
			
			temp = SpleefPlayer.getSpleefPlayer(player);
			switch(hv.getHologramSubType()) {
			case GUILD:
				subname = GuildsManager.getManager().getGuild(player.getUniqueId()).getName(); break;
			case RANKING:
				subname = DataManager.getManager().getCountryString(temp); break;
			case EXP:
				subname = LevelManager.getManager().getRank(temp).getRankName(); 
				break;
			default:
				break;
			}
			
			
			if (subname == null || !subname.equalsIgnoreCase("")) subname = " §7(§a"+subname+"§7)";
	        
	        
	        String name = Bukkit.getOfflinePlayer(pair.getKey()).getName();
	        if (name==null) name= "Unkown";
	        int i = pair.getValue();
	        if (hv.getHologramSubType().equals(HologramSubType.TIME)) i = i/60;
	        if (pos>=13 && pos<=16) primaryButton = true;
	        if (pos>=17 && pos<=20) secondaryButton = true;
	        String text = "§6"+pos + ". §b"+ name +" " + subname+" §7- §e" +i +" " + amount;
	        
	        if (name.equalsIgnoreCase(playerName)) {
	        	text = "§6§l"+pos+". §b§l"+name +" §l" + subname+" §7§l- §e§l" +i +" " + amount;
	        	istop10 = true;
	        }
	    	hv.getHologramIds().get(type).add(new HologramID(line(location,Bukkit.getPlayer(hv.getPlayer()),text),primaryButton,secondaryButton));
	    	if (secondaryButton) secondaryButton = false;
	       	if (primaryButton) primaryButton = false;      	
	    	location.add(0,-0.25,0);
	    	pos++;
	    	
	    	
	    }
	    
	    //1 24
	    // 2 
	    int leftspots = 20-StatsManager.getManager().getTop20HashMap(hashmap, 0,  ranking).size();
	    
	    for (int i = 1; i<=leftspots;i++) {
	    	  String text = "§6"+pos + ". §b"+ "Unknown" +" " + subname+" §7- §e0" +" " + amount;
	        if (pos>=15 && pos<=17) primaryButton = true;
	        if (pos>=18 && pos<=20) secondaryButton = true;
	        
	        
				hv.getHologramIds().get(this.type).add(new HologramID(line(location,Bukkit.getPlayer(hv.getPlayer()),text),primaryButton,secondaryButton));
	    	
	    	if (primaryButton) primaryButton = false;
	    	if (secondaryButton) {
	    		secondaryButton = false;
	    	}
	    	location.add(0,-0.25,0);
	    	
	    pos++;	
	    }
	    
	    if (!istop10) {
	    	
	    	switch(hv.getHologramSubType()) {
			case GUILD:
				subname =  GuildsManager.getManager().getGuild(sp)!=null ? GuildsManager.getManager().getGuild(hv.getPlayer()).getName(): null;  break;
			case RANKING:
				subname = DataManager.getManager().getCountryString(SpleefPlayer.getSpleefPlayer(hv.getPlayer())); 
				subname =subname.substring(0, subname.length() - 1);        
				break;
			case EXP:
				subname = LevelManager.getManager().getRank(SpleefPlayer.getSpleefPlayer(hv.getPlayer())).getRankName(); 
				break;
			default:
				break;
			}
	    	
	    	if (subname == null || !subname.equalsIgnoreCase("")) subname = " §7(§a"+subname+"§7)";
	     	location.add(0,-0.25,0);
	     	pos = StatsManager.getManager().ranking(hashmap, Bukkit.getOfflinePlayer(hv.getPlayer()).getName());
	     	int i = hashmap.get(hv.getPlayer()) != null ? hashmap.get(hv.getPlayer()) : 0;
	     	if (hv.getHologramSubType().equals(HologramSubType.TIME)) i = i/60;
	        String text = "§6§l"+pos+ ". §b§l"+playerName + subname+" §7§l- §e§l" + i +" " + amount;
	    	hv.getHologramIds().get(type).add(new HologramID(line(location,Bukkit.getPlayer(hv.getPlayer()),text),false,true));
	    	location.add(0,-0.25,0);
	    } else {
	    	location.add(0,-0.5,0);
	    }
	    
	    if (hasChangeMode) {
	 	location.add(0,-0.35,0);
	 	
	 	String prev = null;
		String next = null;
		 for (Map.Entry<HologramSubType,HologramSubType> entry : hv.getHologramSubType().getPrevAndNext().entrySet()) {
			 prev = entry.getKey() !=null ? entry.getKey().toString() : "";
			 next = entry.getValue()!=null ? entry.getValue().toString() : "";
			 break;
		 }
		 prev = prev.replaceAll("_", " ");
		 next = next.replaceAll("_", " ");
		 
		 hv.getHologramIds().get(type).add(new HologramID(line(location,Bukkit.getPlayer(hv.getPlayer()),"§6§lCLICK TO TOGGLE"),false,true));
		 location.add(0,-0.35,0);
		 hv.getHologramIds().get(type).add(new HologramID(line(location,Bukkit.getPlayer(hv.getPlayer()),"§7"+prev + "§a§l "+hv.getHologramSubType()+" §7"+ next ),false,true));
		 if (getType().equals(HologramType.GAME_STATS)) {
		 location.add(0,-0.35,0);
		 hv.getHologramIds().get(type).add(new HologramID(line(location,Bukkit.getPlayer(hv.getPlayer()),"§6§lCLICK TO TOGGLE"),false,false));
		 location.add(0,-0.35,0);
		 
		 for (Entry<SpleefRankingPeriod, SpleefRankingPeriod> entry : hv.getPeriod().getPrevAndNext().entrySet()) {
			 prev = entry.getKey() !=null ? entry.getKey().toString() : "";
			 next = entry.getValue()!=null ? entry.getValue().toString() : "";
			 break;
		 }
		 prev = prev.replaceAll("_", " ");
		 next = next.replaceAll("_", " ");
		 String current = hv.getPeriod().toString().replace("_", " ");
		 hv.getHologramIds().get(type).add(new HologramID(line(location,Bukkit.getPlayer(hv.getPlayer()),"§7"+prev + "§a§l "+current+" §7"+ next ),false,false));
		 }
		 
	    }
	    
	    
	
		
	}





}
