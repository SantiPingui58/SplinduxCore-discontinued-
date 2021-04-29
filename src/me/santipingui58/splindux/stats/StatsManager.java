package me.santipingui58.splindux.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.parkour.Level;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.parkour.ParkourPlayer;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StatsManager {

	
	private HashMap<UUID,Integer> spleefffawinsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffakillsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffagamesranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffawinsranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffakillsranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffagamesranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffawinsranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffakillsranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleefffagamesranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleef1vs1winsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleef1vs1gamesranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleef1vs1ELOranking = new HashMap<UUID,Integer>();
	
	
	private HashMap<UUID,Integer> spleggffawinsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffakillsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffagamesranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffawinsranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffakillsranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffagamesranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffawinsranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffakillsranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> spleggffagamesranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> splegg1vs1winsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> splegg1vs1gamesranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> splegg1vs1ELOranking = new HashMap<UUID,Integer>();
	
	
	
	private HashMap<UUID,Integer> tntrunffawinsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffakillsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffagamesranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffawinsranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffakillsranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffagamesranking_monthly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffawinsranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffakillsranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrunffagamesranking_weekly = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrun1vs1winsranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrun1vs1gamesranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> tntrun1vs1ELOranking = new HashMap<UUID,Integer>();
	
	
	private List<ParkourRanking> parkourLevels = new ArrayList <ParkourRanking>();
	private HashMap<UUID,Integer> totalparkourranking = new HashMap<UUID,Integer>();
	private HashMap<UUID,Integer> totalonlinetime = new HashMap<UUID,Integer>();
	
	private static StatsManager manager;	
	 public static StatsManager getManager() {
	        if (manager == null)
	        	manager = new StatsManager();
	        return manager;
	    }
	
	 
	 public ParkourRanking getParkourRanking(Level level) {
		 for (ParkourRanking pr : this.parkourLevels) {
			 if (pr.getLevel().equals(level)) {
				 return pr;
			 }
		 }
		 return null;
	 }
	 
	 
	 public void loadParkourLevels() {
		 for (int i = 1;i <=25;i++) {
			 ParkourRanking ranking = new ParkourRanking(ParkourManager.getManager().getLevel(i));
			 this.parkourLevels.add(ranking);
		 }
	 }
	 
	 
	 
	 //Get the player position in any Ranking
	public int getRankingPosition(RankingEnum type,SpleefPlayer sp) {	
		//Gets ranking based on the type
		HashMap<UUID,Integer> ranking = getRanking(type);
		 return ranking(ranking,sp.getOfflinePlayer().getName());
	}
	
		
	public int getParkourRankingPosition(Level level, SpleefPlayer sp) {
		ParkourRanking ranking = getParkourRanking(level);
		 return ranking(ranking.getRanking(),sp.getOfflinePlayer().getName());
	}
	

	private int ranking(HashMap<UUID,Integer> hashmap, String player) {
		Iterator<Entry<UUID, Integer>> it = hashmap.entrySet().iterator();
		  int i = 1;
		  while (it.hasNext()) {
		        Map.Entry<UUID,Integer> pair = (Map.Entry<UUID,Integer>)it.next();
		        String name = Bukkit.getOfflinePlayer(pair.getKey()).getName();		        
		        if (name !=null &&name.equalsIgnoreCase(player)) {
		        	return i;
		        }
		        
		        i++;
		        
		  }
		  return i;
	}
	
	
	//Method used for /stats ffaspleef. This method let you see an specific page, every page has 10 players.
	public void sendRanking(CommandSender sender,int page,RankingEnum type) {
		HashMap<UUID,Integer> hashmap = getHashMapByType(type);		
		if (hashmap.isEmpty()) {
			updateRankings();
		}
		    List<String> list =getTop10(hashmap,page,type);
		    for (String s : list) {
		    	sender.sendMessage(s);
		    }
		   
	}
	
	public void sendRanking(CommandSender sender,int page,LinkedHashMap<UUID,Integer> hashmap) {	
		if (hashmap.isEmpty()) {
			updateRankings();
		}
		    HashMap<UUID, Integer> h = getTop10(hashmap,page,RankingEnum.RANKING);
		    int i = (10*page)+1;
		    for (Entry<UUID, Integer> entry : h.entrySet()) {
		        UUID key = entry.getKey();
		        int value = entry.getValue();	        
		        OfflinePlayer pa = Bukkit.getOfflinePlayer(key);
				SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
				if (temp==null) {
					 new SpleefPlayer(pa.getUniqueId());
					HikariAPI.getManager().loadData(pa.getUniqueId());
				}
				
					int a = i;					
							SpleefPlayer sp  = SpleefPlayer.getSpleefPlayer(pa);
					        int oldposition = Main.ranking.getConfig().getInt("ranking."+a+".oldposition");
					        int oldWithoutAbs = oldposition;
					        oldposition = Math.abs(oldposition-a);
					       List<String> records = Main.ranking.getConfig().getStringList("ranking."+a+".records");
					       
					        String oldpos = oldposition < 1000 ? oldWithoutAbs != -1 ? oldWithoutAbs>a ? "§a+"+oldposition : oldWithoutAbs<a ? "§c-"+oldposition : "§7=" : "" : "";
					        
									        String s = "§6"+a +". §a"+pa.getName()+"§7: §b" + value + " Points "+oldpos +" §7(" + DataManager.getManager().getCountryString(sp.getCountry())+ "§7)" ;
									     	TextComponent message = new TextComponent(s);
									     	if (records.size()>0) {
									    	String hover = "§aRecords: \n" ;
									    	for (String ss : records) {
									    		ss = ChatColor.translateAlternateColorCodes('&', ss);
									    		hover = hover + ss+ "\n";
									    	}
									    	message.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));	
									     	}
												sender.spigot().sendMessage(message);	
								
						
						i++;
					
		    }	    
	}
	
	
	
	public List<String> getTop10(HashMap<UUID,Integer> hashmap,int page, RankingEnum type) {
		List<String> list = new ArrayList<String>();
		  Iterator<Entry<UUID, Integer>> it = hashmap.entrySet().iterator();
		  int inicio = (page*10)+1;
		  int fin = (page+1)*10;
		  int i = 1;
		    while (it.hasNext()) {
		        Map.Entry<UUID,Integer> pair = (Map.Entry<UUID,Integer>)it.next();
		        String name = Bukkit.getOfflinePlayer(pair.getKey()).getName();
		        int wins = pair.getValue();
		        if (i<=fin) {
		        	if (i>=inicio) {
		        		list.add("§6"+i +". §a"+name+"§7: §b" + wins + " " + getAmountByType(type));
		        	}
		        i++;
		    }  else {
		    	break;
		    }
	}
		    return list;
	}
	
	
	public List<String> getTop10(HashMap<UUID,Integer> hashmap,int page, RankingEnum type,SpleefPlayer sp) {
		List<String> list = new ArrayList<String>();
		  Iterator<Entry<UUID, Integer>> it = hashmap.entrySet().iterator();
		  int inicio = (page*10)+1;
		  int fin = (page+1)*10;
		  int i = 1;
		  boolean istop10 = false;
		    while (it.hasNext()) {
		        Map.Entry<UUID,Integer> pair = (Map.Entry<UUID,Integer>)it.next();
		        String name = Bukkit.getOfflinePlayer(pair.getKey()).getName();
     
		        int wins = pair.getValue();
		        if (i<=fin) {
		        	if (i>=inicio) {
		        		String s = "§6"+i +". §a"+name+"§7: §b" + wins + " " + getAmountByType(type);
		        		if (sp.getOfflinePlayer().getName().equalsIgnoreCase(name)) {
		        			s = "§6§l"+i +". §a§l"+name+"§7§l: §b§l" + wins + " " + getAmountByType(type);
		        			istop10 = true;
		        		}
		        		list.add(s);
		        	}
		        i++;
		    }  else {
		    	break;
		    }
	}
		    
		    if (!istop10) list.add("§6§l"+getRankingPosition(type, sp) +". §a§l"+sp.getOfflinePlayer().getName()+"§7§l: §b§l" + hashmap.get(sp.getUUID()) + " " + getAmountByType(type));
		    return list;
	}
	
	
	public List<String> getTop10Names(HashMap<UUID,Integer> hashmap,int page, RankingEnum type) {
		List<String> list = new ArrayList<String>();
		  Iterator<Entry<UUID, Integer>> it = hashmap.entrySet().iterator();
		  int inicio = (page*10)+1;
		  int fin = (page+1)*10;
		  int i = 1;
		    while (it.hasNext()) {
		        Map.Entry<UUID,Integer> pair = (Map.Entry<UUID,Integer>)it.next();
		        String name = Bukkit.getOfflinePlayer(pair.getKey()).getName();
		        if (i<=fin) {
		        	if (i>=inicio) {
		        		list.add(name);
		        	}
		        i++;
		    }  else {
		    	break;
		    }
	}
		    return list;
	}
	
	
	
	public LinkedHashMap<UUID,Integer> getTop10(LinkedHashMap<UUID,Integer> hashmap,int page, RankingEnum type) {
		LinkedHashMap<UUID,Integer> h = new LinkedHashMap<UUID,Integer>();
		  Iterator<Entry<UUID, Integer>> it = hashmap.entrySet().iterator();
		  int inicio = (page*10)+1;
		  int fin = (page+1)*10;
		  int i = 1;
		    while (it.hasNext()) {
		        Map.Entry<UUID,Integer> pair = (Map.Entry<UUID,Integer>)it.next();
		        UUID name = pair.getKey();
		        int wins = pair.getValue();
		        if (i<=fin) {
		        	if (i>=inicio) {
		        		h.put(name, wins);
		        	}
		        i++;
		    }  else {
		    	break;
		    }
	}
		    return h;
	}
	
	


	public void sendRanking(int page,RankingEnum type,CommandSender sender) {
	int pag = page+1;
	HashMap<UUID,Integer> hashmap = StatsManager.getManager().getRanking(type);
	String title = StatsManager.getManager().getTitleByType(type);
	int total = (hashmap.size()/10)+1;		
	if (page<=total-1) {
	sender.sendMessage("§6-=-=-=-[§a§l"+title +" Top ("+pag+"/"+total+")§6]-=-=-=-");
	StatsManager.getManager().sendRanking(sender, page,type);
	sender.sendMessage("§6-=-=-=-[§a§l"+title +" Top ("+pag+"/"+total+")§6]-=-=-=-");
	} else {
		sender.sendMessage("§cPage not found.");
	}
	}
	

	//Method to update all rankings by putting all the data again, should be optimized in a future with more amount of players.
	public void updateRankings() {

		
		spleefffawinsranking = HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_WINS);
		spleefffagamesranking= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_GAMES);
		spleefffakillsranking= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_KILLS);
		spleefffawinsranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_WINS_MONTHLY);
		spleefffakillsranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_KILLS_MONTHLY);
		spleefffagamesranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_GAMES_MONTHLY);
		spleefffawinsranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_WINS_WEEKLY);
		spleefffakillsranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_KILLS_WEEKLY);
		spleefffagamesranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.SPLEEFFFA_GAMES_WEEKLY);
		spleef1vs1ELOranking= HikariAPI.getManager().getRanking(RankingEnum.SPLEEF1VS1_ELO);
		spleef1vs1winsranking = HikariAPI.getManager().getRanking(RankingEnum.SPLEEF1VS1_WINS);
		
		spleggffawinsranking = HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_WINS);
		spleggffagamesranking= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_GAMES);
		spleggffakillsranking= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_KILLS);
		spleggffawinsranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_WINS_MONTHLY);
		spleggffakillsranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_KILLS_MONTHLY);
		spleggffagamesranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_GAMES_MONTHLY);
		spleggffawinsranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_WINS_WEEKLY);
		spleggffakillsranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_KILLS_WEEKLY);
		spleggffagamesranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.SPLEGGFFA_GAMES_WEEKLY);
		splegg1vs1ELOranking= HikariAPI.getManager().getRanking(RankingEnum.SPLEGG1VS1_ELO);
		splegg1vs1winsranking = HikariAPI.getManager().getRanking(RankingEnum.SPLEGG1VS1_WINS);
		
		tntrunffawinsranking = HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_WINS);
		tntrunffagamesranking= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_GAMES);
		tntrunffakillsranking= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_KILLS);
		tntrunffawinsranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_WINS_MONTHLY);
		tntrunffakillsranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_KILLS_MONTHLY);
		tntrunffagamesranking_monthly= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_GAMES_MONTHLY);
		tntrunffawinsranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_WINS_WEEKLY);
		tntrunffakillsranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_KILLS_WEEKLY);
		tntrunffagamesranking_weekly= HikariAPI.getManager().getRanking(RankingEnum.TNTRUNFFA_GAMES_WEEKLY);
		tntrun1vs1ELOranking= HikariAPI.getManager().getRanking(RankingEnum.TNTRUN1VS1_ELO);
		tntrun1vs1winsranking = HikariAPI.getManager().getRanking(RankingEnum.TNTRUN1VS1_WINS);
		
		totalonlinetime= HikariAPI.getManager().getRanking(RankingEnum.TOTALONLINETIME);
		totalparkourranking = HikariAPI.getManager().getRanking(RankingEnum.PARKOUR);
		
	
		for (ParkourRanking pr : this.parkourLevels) {
			pr.setRanking(HikariAPI.getManager().getParkourRanking(pr.getLevel().getLevel()));
		}
		
		
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			ParkourPlayer pp = sp.getParkourPlayer();
		int points = 0;
		
		for (int i = 1; i <25; i++) {
			points = points + i*pp.getRecord(ParkourManager.getManager().getLevel(i));
		}
		pp.getStats().setPoints(points/10);
		}
		
		
		//Sorts the rankings 
		
		
		//Spleef
		spleefffawinsranking = sortByValue(spleefffawinsranking);
		spleefffakillsranking = sortByValue(spleefffakillsranking);
		spleefffagamesranking = sortByValue(spleefffagamesranking);
		
		spleefffawinsranking_monthly = sortByValue(spleefffawinsranking_monthly);
		spleefffakillsranking_monthly = sortByValue(spleefffakillsranking_monthly);
		spleefffagamesranking_monthly = sortByValue(spleefffagamesranking_monthly);
		
		spleefffawinsranking_weekly = sortByValue(spleefffawinsranking_weekly);
		spleefffakillsranking_weekly = sortByValue(spleefffakillsranking_weekly);
		spleefffagamesranking_weekly = sortByValue(spleefffagamesranking_weekly);

		spleef1vs1winsranking = sortByValue(spleef1vs1winsranking);
		spleef1vs1gamesranking = sortByValue(spleef1vs1gamesranking);
		spleef1vs1ELOranking = sortByValue(spleef1vs1ELOranking);
		
		
		
		//Splegg
		spleggffawinsranking = sortByValue(spleggffawinsranking);
		spleggffakillsranking = sortByValue(spleggffakillsranking);
		spleggffagamesranking = sortByValue(spleggffagamesranking);
		
		spleggffawinsranking_monthly = sortByValue(spleggffawinsranking_monthly);
		spleggffakillsranking_monthly = sortByValue(spleggffakillsranking_monthly);
		spleggffagamesranking_monthly = sortByValue(spleggffagamesranking_monthly);
		
		spleggffawinsranking_weekly = sortByValue(spleggffawinsranking_weekly);
		spleggffakillsranking_weekly = sortByValue(spleggffakillsranking_weekly);
		spleggffagamesranking_weekly = sortByValue(spleggffagamesranking_weekly);

		splegg1vs1winsranking = sortByValue(splegg1vs1winsranking);
		splegg1vs1gamesranking = sortByValue(splegg1vs1gamesranking);
		splegg1vs1ELOranking = sortByValue(splegg1vs1ELOranking);
		
		
		//TNTRun
		tntrunffawinsranking = sortByValue(tntrunffawinsranking);
		tntrunffakillsranking = sortByValue(tntrunffakillsranking);
		tntrunffagamesranking = sortByValue(tntrunffagamesranking);
		
		tntrunffawinsranking_monthly = sortByValue(tntrunffawinsranking_monthly);
		tntrunffakillsranking_monthly = sortByValue(tntrunffakillsranking_monthly);
		tntrunffagamesranking_monthly = sortByValue(tntrunffagamesranking_monthly);
		
		tntrunffawinsranking_weekly = sortByValue(tntrunffawinsranking_weekly);
		tntrunffakillsranking_weekly = sortByValue(tntrunffakillsranking_weekly);
		tntrunffagamesranking_weekly = sortByValue(tntrunffagamesranking_weekly);

		tntrun1vs1gamesranking = sortByValue(tntrun1vs1gamesranking);
		tntrun1vs1ELOranking = sortByValue(tntrun1vs1ELOranking);
		tntrun1vs1winsranking = sortByValue(tntrun1vs1winsranking);
		
		
		totalonlinetime = sortByValue(totalonlinetime);
		totalparkourranking = sortByValue(totalparkourranking);
		
	}
	
	
	//Returns the Ranking HashMap based on their RankingEnum
	public HashMap<UUID,Integer> getRanking(RankingEnum type) {
		HashMap<UUID,Integer> hashmap = new HashMap<UUID,Integer>();
		
		switch (type) {
		case PARKOUR:
			hashmap = this.getTotalParkourRankingHashMap(); break;
		case RANKING:
			break;
		case SPLEEF1VS1_ELO:
			hashmap = this.spleef1vs1ELOranking;break;
		case SPLEEF1VS1_GAMES:
			hashmap = this.spleef1vs1gamesranking;break;
		case SPLEEF1VS1_WINS:
			hashmap = this.spleef1vs1winsranking;break;
		case SPLEEFFFA_GAMES:
			hashmap = this.spleefffagamesranking;break;
		case SPLEEFFFA_GAMES_MONTHLY:
			hashmap = this.spleefffagamesranking_monthly;break;
		case SPLEEFFFA_GAMES_WEEKLY:
			hashmap = this.spleefffagamesranking_weekly;break;
		case SPLEEFFFA_KILLS:
			hashmap = this.spleefffakillsranking;break;
		case SPLEEFFFA_KILLS_MONTHLY:
			hashmap = this.spleefffakillsranking_monthly;break;
		case SPLEEFFFA_KILLS_WEEKLY:
			hashmap = this.spleefffakillsranking_weekly;break;
		case SPLEEFFFA_WINS:
			hashmap = this.spleefffawinsranking;break;
		case SPLEEFFFA_WINS_MONTHLY:
			hashmap = this.spleefffawinsranking_monthly;break;
		case SPLEEFFFA_WINS_WEEKLY:
			hashmap = this.spleefffawinsranking_weekly; break;		
		case SPLEGG1VS1_GAMES:
			hashmap = this.splegg1vs1gamesranking;break;	
		case SPLEGG1VS1_WINS:
			hashmap = this.splegg1vs1winsranking;break;	
		case SPLEGGFFA_GAMES:
			hashmap = this.spleggffagamesranking;break;	
		case SPLEGGFFA_GAMES_MONTHLY:
			hashmap = this.spleggffagamesranking_monthly;break;	
		case SPLEGGFFA_GAMES_WEEKLY:
			hashmap = this.spleggffagamesranking_weekly;break;	
		case SPLEGGFFA_KILLS:
			hashmap = this.spleggffakillsranking;break;	
		case SPLEGGFFA_KILLS_MONTHLY:
			hashmap = this.spleggffakillsranking_monthly;break;	
		case SPLEGGFFA_KILLS_WEEKLY:
			hashmap = this.spleggffakillsranking_weekly;break;	
		case SPLEGGFFA_WINS:
			hashmap = this.spleggffawinsranking;break;	
		case SPLEGGFFA_WINS_MONTHLY:
			hashmap = this.spleggffawinsranking_monthly;break;	
		case SPLEGGFFA_WINS_WEEKLY:
			hashmap = this.spleggffawinsranking_weekly;break;	
		case TNTRUN1VS1_GAMES:
			hashmap = this.tntrun1vs1gamesranking;break;	
		case TNTRUN1VS1_WINS:
			hashmap = this.tntrun1vs1winsranking;break;	
		case TNTRUNFFA_GAMES:
			hashmap = this.tntrunffagamesranking;break;	
		case TNTRUNFFA_GAMES_MONTHLY:
			hashmap = this.tntrunffagamesranking_monthly;break;	
		case TNTRUNFFA_GAMES_WEEKLY:
			hashmap = this.tntrunffagamesranking_weekly;break;	
		case TNTRUNFFA_KILLS:
			hashmap = this.tntrunffakillsranking;break;	
		case TNTRUNFFA_KILLS_MONTHLY:
			hashmap = this.tntrunffakillsranking_monthly;break;	
		case TNTRUNFFA_KILLS_WEEKLY:
			hashmap = this.tntrunffakillsranking_weekly;break;	
		case TNTRUNFFA_WINS:
			hashmap = this.tntrunffawinsranking;break;	
		case TNTRUNFFA_WINS_MONTHLY:
			hashmap = this.tntrunffawinsranking_monthly;break;	
		case TNTRUNFFA_WINS_WEEKLY:
			hashmap = this.tntrunffawinsranking_weekly;break;	
		case TOTALONLINETIME:
			hashmap = totalonlinetime;break;	
		case SPLEGG1VS1_ELO:
			hashmap = this.splegg1vs1ELOranking;break;	
		case TNTRUN1VS1_ELO:
			hashmap = this.tntrun1vs1ELOranking;break;			
		default:
			return hashmap;		
		} 
		
		if (hashmap.isEmpty()) 
			updateRankings();
		
		
		return hashmap;
	}
	
	
	private HashMap<UUID,Integer> getHashMapByType(RankingEnum type) {
		if (type.equals(RankingEnum.SPLEEFFFA_WINS)) {
			return spleefffawinsranking;
		} else if (type.equals(RankingEnum.SPLEEFFFA_KILLS)) {
			return spleefffakillsranking;
		} else if (type.equals(RankingEnum.SPLEEFFFA_GAMES)) {
			return spleefffagamesranking;
		} else if (type.equals(RankingEnum.TOTALONLINETIME)) {
			return totalonlinetime;
		} 
		return null;
	}
	
	
	public String getAmountByType(RankingEnum type) {
		if (type==null) {
			return "Points";
		} else if (type.equals(RankingEnum.PARKOUR)) {
			return "Jumps";
		} 
		else {
		SpleefRankingType srt = type.getSpleefRankingType();
		
		switch (srt) {
		case ELO:
			return "ELO";
		case GAMES:
			return "Games";
		case KILLS:
			return "Kills";
		case WINS:
			return "Wins";
		}
		}
		
		return "Points";
		
	} 
	
	public String getAmountByPeriod(SpleefRankingPeriod type) {
		if (type.equals(SpleefRankingPeriod.ALL_TIME)) {
			return "All time";
		} else if (type.equals(SpleefRankingPeriod.MONTHLY)) {
			return "Monthly";
		} else if (type.equals(SpleefRankingPeriod.WEEKLY)) {
			return "Weekly";
		}
		return null;
	} 
	
	public String getTitleByType(RankingEnum type) {
		if (type.equals(RankingEnum.SPLEEFFFA_WINS)) {
			return "Spleef FFA Wins";
		} else if (type.equals(RankingEnum.SPLEEFFFA_KILLS)) {
			return "Spleef FFA Kills";
		} else if (type.equals(RankingEnum.SPLEEFFFA_GAMES)) {
			return "Spleef FFA Games";
		}else if (type.equals(RankingEnum.TOTALONLINETIME)) {
			return "Total Online Time";
		} else {
			return "Points";
		}
	}
	
	  public  HashMap<UUID, Integer> sortByValue(Map<UUID,Integer> hm) { 
	        // Create a list from elements of HashMap 
	        List<Map.Entry<UUID, Integer> > list = 
	               new LinkedList<Map.Entry<UUID, Integer> >(hm.entrySet()); 
	  
	        // Sort the list 
	        Collections.sort(list, new Comparator<Map.Entry<UUID, Integer> >() { 
	            public int compare(Map.Entry<UUID, Integer> o2,  
	                               Map.Entry<UUID, Integer> o1) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 
	
	        HashMap<UUID, Integer> temp = new LinkedHashMap<UUID, Integer>(); 
	        for (Map.Entry<UUID, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
}
	  
	  
	  public  TreeMap<UUID, Integer> sortByValueTree(Map<UUID,Integer> hm) { 
	        // Create a list from elements of HashMap 
	        List<Map.Entry<UUID, Integer> > list = 
	               new LinkedList<Map.Entry<UUID, Integer> >(hm.entrySet()); 
	  
	        // Sort the list 
	        Collections.sort(list, new Comparator<Map.Entry<UUID, Integer> >() { 
	            public int compare(Map.Entry<UUID, Integer> o2,  
	                               Map.Entry<UUID, Integer> o1) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 
	
	        TreeMap<UUID, Integer> temp = new TreeMap<UUID, Integer>(); 
	        for (Map.Entry<UUID, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
}
	  
	  
	  public LinkedHashMap<UUID, Integer> sortByValueLinked(Map<UUID,Integer> hm) { 
	        // Create a list from elements of HashMap 
	        List<Map.Entry<UUID, Integer> > list = 
	               new LinkedList<Map.Entry<UUID, Integer> >(hm.entrySet()); 
	  
	        // Sort the list 
	        Collections.sort(list, new Comparator<Map.Entry<UUID, Integer> >() { 
	            public int compare(Map.Entry<UUID, Integer> o2,  
	                               Map.Entry<UUID, Integer> o1) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 
	
	        LinkedHashMap<UUID, Integer> temp = new LinkedHashMap<UUID, Integer>(); 
	        for (Map.Entry<UUID, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
}
	  
	  
	  
	  public HashMap<UUID,Integer> getTotalParkourRankingHashMap() {
		  return this.totalparkourranking;
	  }
}