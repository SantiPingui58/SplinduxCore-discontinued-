package me.santipingui58.splindux.stats.ranking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.parkour.ParkourPlayer;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.level.LevelManager;

public class RankingManager {

	private static RankingManager manager;	
	 public static RankingManager getManager() {
	        if (manager == null)
	        	manager = new RankingManager();
	        return manager;
	    }
	
		private Ranking ranking;
		private HashMap<UUID,List<String>> records = new HashMap<UUID,List<String>>();
		private HashMap<UUID,List<String>> players = new HashMap<UUID,List<String>>();
	
		public void loadParkourGlobalTop() {
			new BukkitRunnable() {
				public void run() {
			loadAllPlayers();
			}
			}.runTaskAsynchronously(Main.get());
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
				ParkourPlayer pp = sp.getParkourPlayer();
			int points = 0;
			
			for (int i = 1; i <25; i++) {
				points = points +i*pp.getRecord(ParkourManager.getManager().getLevel(i));
			}
			pp.getStats().setPoints(points/10);
			int f = points;
			new BukkitRunnable() {
				
				public void run() {
					Bukkit.broadcastMessage(""+f);
				}
			}.runTask(Main.get());
			}
		}
			}.runTaskLaterAsynchronously(Main.get(), 10L);
			
			DataManager.getManager().saveData();
		}
		public Ranking getRanking() {
			return this.ranking;
		}
		 
		
		
		private void addRecord(UUID sp,String record) {
			if (!records.containsKey(sp)) records.put(sp, new ArrayList<String>());
			
			records.get(sp).add(record);
			
		}
		
	public void loadRanking() {
		if (Main.ranking.getConfig().contains("ranking")) {
		Set<String> ranking = Main.ranking.getConfig().getConfigurationSection("ranking").getKeys(false);
		LinkedHashMap<UUID, Integer> hashmap = new LinkedHashMap<UUID,Integer>();
		for (String r : ranking) {
			UUID uuid = UUID.fromString(Main.ranking.getConfig().getString("ranking."+r+".uuid"));
			int points= Main.ranking.getConfig().getInt("ranking."+r+".points");
			//List<String> list = Main.ranking.getConfig().getStringList("ranking."+r+".records");
			//SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(uuid);
			//list.forEach((s) ->sp.getRankingRecords().add(s));
			hashmap.put(uuid, points);
		}
		
		this.ranking = new Ranking(hashmap);
	} else {
		this.ranking = new Ranking();
	}
		
	}
	
	
	public void remove0() {
			new BukkitRunnable() {
				public void run() {
					loadAllPlayers();
				
					
					Set<String> ra = Main.ranking.getConfig().getConfigurationSection("ranking").getKeys(false);
					LinkedHashMap<UUID, Integer> hashmap = new LinkedHashMap<UUID,Integer>();
					for (String r : ra) {
						UUID uuid = UUID.fromString(Main.ranking.getConfig().getString("ranking."+r+".uuid"));
						int points= Main.ranking.getConfig().getInt("ranking."+r+".points");
						hashmap.put(uuid, points);
					}
					
					final Ranking ranking = new Ranking(StatsManager.getManager().sortByValueLinked(hashmap));
					new BukkitRunnable() {
						public void run() {
			int i = 1;
						
			for (Entry<UUID, Integer> entry : ranking.getRanking().entrySet()) {
			    UUID uuid = entry.getKey();
			    int p = entry.getValue();
			    if (p<=0) continue;
			    
			    SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(uuid);
			    Main.ranking.getConfig().set("ranking."+i+".uuid", uuid.toString());
			    Main.ranking.getConfig().set("ranking."+i+".name", Bukkit.getOfflinePlayer(uuid).getName());
			    Main.ranking.getConfig().set("ranking."+i+".points", p);
			    List<String> list = records.get(uuid);		 
			    if (list!=null)sp.getRankingRecords().addAll(list);
			    
			    Main.ranking.getConfig().set("ranking."+i+".records", list);
			    Main.ranking.getConfig().set("ranking."+i+".country", sp.getCountry() !=null ? sp.getCountry() : "Unkown");
			    Main.ranking.getConfig().set("ranking."+i+".oldposition", ranking.getPosition(sp.getUUID()));
			    i++;
				
			}
						}
					}.runTaskLaterAsynchronously(Main.get(), 120L);
			Main.ranking.saveConfig();
				}
				}.runTaskAsynchronously(Main.get());	
			}
	
	
	public void saveRanking(Ranking oldRanking) {
		new BukkitRunnable() {
			public void run() {
		int i = 1;
		for (Entry<UUID, Integer> entry : ranking.getRanking().entrySet()) {
		    UUID uuid = entry.getKey();
		    int p = entry.getValue();
		    
		    Main.ranking.getConfig().set("ranking."+i+".uuid", uuid.toString());
		    Main.ranking.getConfig().set("ranking."+i+".name", Bukkit.getOfflinePlayer(uuid).getName());
		    Main.ranking.getConfig().set("ranking."+i+".points", p);
		    List<String> list = records.get(uuid);		 
		    //if (list!=null) sp.getRankingRecords().addAll(list);
		    
		    Main.ranking.getConfig().set("ranking."+i+".records", list);
		    try {
		   // Main.ranking.getConfig().set("ranking."+i+".country", sp.getCountry() !=null ? sp.getCountry() : "Unkown");
    
		    Main.ranking.getConfig().set("ranking."+i+".oldposition", oldRanking.getPosition(uuid));
		    } catch(Exception ex) {
		    	new BukkitRunnable() {
		    		public void run() {
		    			Bukkit.broadcastMessage(uuid.toString());
		    		}
		    	}.runTask(Main.get());
		    }
		    i++;
			
		}
		Main.ranking.saveConfig();
			}
			
		}.runTaskAsynchronously(Main.get());
		

	}
	
	public void calculate() { 
			Ranking newRanking = new Ranking();
		new BukkitRunnable() {
			public void run() {
				loadRanking();
				loadAllPlayers();
				
				
				new BukkitRunnable() {
					public void run() {
		boolean isEndOfSeason =isEndOfSeason();
		isEndOfSeason = false;
		
		if (isEndOfSeason) 
			decreasePoints();			
		tournaments(newRanking);		
		 monthlyFFA(newRanking);	
		 guildsSeason(newRanking);
		if (isEndOfSeason) rankedELO(newRanking);		
		
		spleefRankMultiply(newRanking);
		
		for (Entry<UUID, Integer> entry : newRanking.getRanking().entrySet()) {
		    UUID key = entry.getKey();
		    int value = entry.getValue();
		  if (ranking.getRanking().containsKey(key)) ranking.getRanking().put(key, ranking.getRanking().get(key)+value);
		}
		
		ranking.setRanking(StatsManager.getManager().sortByValueLinked(ranking.getRanking()));
		ranking.remove0();
		saveRanking(ranking);
			}
				}.runTaskLaterAsynchronously(Main.get(), 20L);
			}
		}.runTaskAsynchronously(Main.get());
	}
	
	
	
	private void loadAllPlayers() {
		this.players.putAll(HikariAPI.getManager().getAllPlayers());
	}
	
	
	public boolean isEndOfSeason() {
		 Date now = new Date();
		 @SuppressWarnings("deprecation")
		int month = now.getMonth();
		 Integer[] months = {2,5,8,11};
		 for (int i = 0;i<months.length;i++) {
		 if (months[i]==month) {
			 return true; 
		 }
		 } 
		 return false;
	}
	
	public void decreasePoints() {
		for (Entry<UUID, Integer> entry : ranking.getRanking().entrySet()) { 
		    UUID sp = entry.getKey();
		    int p = entry.getValue();
		    ranking.getRanking().put(sp, p/6);
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void spleefRankMultiply(Ranking ranking) {
		 Calendar thirtyDaysAgo = Calendar.getInstance();  
		  thirtyDaysAgo.add(Calendar.DAY_OF_MONTH, -30);  
	       Date thirtyDaysAgoDate = thirtyDaysAgo.getTime();
	
		
		
		for (Entry<UUID, List<String>> entry : this.players.entrySet()) {
		 UUID sp = entry.getKey();
		 List<String> list = entry.getValue();
		 int level = Integer.valueOf(list.get(0));
		 
		 Date date = null;
		 try {
			 date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(list.get(1));
		} catch (ParseException e) {
			continue;
		}
		 
			if(thirtyDaysAgoDate.after(date)) continue;

		 int playerPoints = 0;
		 
     	if (ranking.getRanking().containsKey(sp)) playerPoints = ranking.getRanking().get(sp);
			int rank = LevelManager.getManager().getRank(level).getInt();	 
			double multiply  = 1 + ((rank-1)*0.0125);
			playerPoints = (int) (playerPoints*multiply);
			if (Bukkit.getOfflinePlayer("SantiPingui58").getUniqueId().compareTo(sp)==0)  {
				Bukkit.getLogger().info("santi multiply :"+multiply);
				Bukkit.getLogger().info("santi level :"+level);	
			}	
			ranking.getRanking().put(sp, playerPoints);	
		}
	}
	
	public void rankedELO(Ranking ranking) {
		// StatsManager sm = StatsManager.getManager();
			//List<String> top10 = StatsManager.getManager().getTop10Names(sm.getRanking(RankingEnum.SPLEEF1VS1_ELO), 0, RankingEnum.SPLEEF1VS1_ELO);
		List<String> top10 = new ArrayList<String>();
		
		top10.add("Wezuh"); //Extreme
		top10.add("Triffuny"); // Epic
		top10.add("SantiPingui58"); // Epic
		top10.add("Sudzo"); // Epic
		top10.add("JetFire_JTFR"); //Vip
		top10.add("slenderbrine100"); // VIP
		top10.add("ShadowFlames"); // VIP
		top10.add("kiwata");
		top10.add("panter_bebrus");
		top10.add("hi_im_nazar");
			for (UUID sp : this.players.keySet()) {
				int playerPoints = 0;
				if (ranking.getRanking().containsKey(sp)) playerPoints = playerPoints + ranking.getRanking().get(sp);		
				
				if (top10.contains(Bukkit.getOfflinePlayer(sp).getName())) {
					int position = top10.indexOf(Bukkit.getOfflinePlayer(sp).getName())+1;
					playerPoints = playerPoints + 750 + ((10-position)*250);
					addRecord(sp,"&7- &a#"+position + " at Ranked ELO");
				} else {
					playerPoints = playerPoints + 50;
				}
				ranking.getRanking().put(sp, playerPoints);
			}
	}
	
	public void guildsSeason(Ranking ranking) {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		hashmap.put("SantiPingui58",1250);
		hashmap.put("iKeffsito",125);
		hashmap.put("JetFire_JTFR",1250);
		hashmap.put("FipoRk",1250);
		hashmap.put("lostyt666",1000);
		hashmap.put("obivan_kenobi",750);
		hashmap.put("Sudzo",1500);
		hashmap.put("thoget",1500);
		hashmap.put("makimeer",1500);
		hashmap.put("Flycherr",1000);
		hashmap.put("Triffuny",1500);
		hashmap.put("pancircus",1500);
		hashmap.put("onjosh",750);
		hashmap.put("hikarilof",250);
		hashmap.put("kiwata",250);
		hashmap.put("AppAlInG_BOxER ",250);
		hashmap.put("hi_im_Nazar",1000);
		hashmap.put("__YMKA167__",1000);
		hashmap.put("iferus7",1000);
		hashmap.put("Paapr3",1000);
		hashmap.put("Barmink",1000);

		
		for (Map.Entry<String, Integer> entry : hashmap.entrySet()) {
		    String name = entry.getKey();
		    int points = entry.getValue();
		    @SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(name);
		    if (!ranking.getRanking().containsKey(player.getUniqueId())) ranking.getRanking().put(player.getUniqueId(), 0);
		    ranking.getRanking().put(player.getUniqueId(), ranking.getRanking().get(player.getUniqueId())+points);
		}

	}
	private void monthlyFFA(Ranking ranking) {
		//StatsManager sm = StatsManager.getManager();
		//List<String> top10 = StatsManager.getManager().getTop10Names(sm.getRanking(RankingEnum.SPLEEFFFA_WINS_MONTHLY), 0, RankingEnum.SPLEEFFFA_WINS_MONTHLY);	
		List<String> top10 = new ArrayList<String>();
		
		
		top10.add("SantiPingui58");
		top10.add("__YMKA167__");
		top10.add("Paapr3");
		top10.add("hi_im_Nazar");
		top10.add("iferus7");
		top10.add("JeffEatsPie");
		top10.add("kiwata");
		top10.add("aprada");
		top10.add("TheMoritex");
		top10.add("ShadowFlames");
		
		
		 
		 Calendar thirtyDaysAgo = Calendar.getInstance();  
		  thirtyDaysAgo.add(Calendar.DAY_OF_MONTH, -30);  
	       Date thirtyDaysAgoDate = thirtyDaysAgo.getTime();
		for (Entry<UUID, List<String>> entry : this.players.entrySet()) {
			 UUID sp = entry.getKey();
			 List<String> list = entry.getValue();

			
			 Date date = null;
			 try {
				 date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(list.get(1));
			} catch (ParseException e) {
				continue;
			}
			 
			 
				if(thirtyDaysAgoDate.after(date)) continue;
			
			int playerPoints = 0;
			if (ranking.getRanking().containsKey(sp)) playerPoints = playerPoints +ranking.getRanking().get(sp);
			
			ranking.getRanking().put(sp, playerPoints);
			if (top10.contains(Bukkit.getOfflinePlayer(sp).getName())) {
				int position = top10.indexOf(Bukkit.getOfflinePlayer(sp).getName())+1;
				playerPoints = playerPoints + 100 + ((10-position)*100);
				addRecord(sp,"&7- &a#"+position + " at Monthly FFA");
			} else {
		        	playerPoints = playerPoints + 50;
			}
			
			ranking.getRanking().put(sp, playerPoints);
		}
	}
	
	public void tournaments(Ranking ranking) {
		Set<String> tournaments  = Main.tournaments.getConfig().getConfigurationSection("tournaments").getKeys(false);
		
		for (String t : tournaments) {
			FileConfiguration config = Main.tournaments.getConfig();
			boolean ffa = config.getBoolean("tournaments."+t+".ffa");
			boolean weekly = config.getBoolean("tournaments."+t+".weekly");
			boolean teams = config.getBoolean("tournaments."+t+".teams");
			String  league = config.getString("tournaments."+t+".league");
			List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
			List<String> list = config.getStringList("tournaments."+t+".players");
			list.forEach((p) -> players.add(new SpleefPlayer(UUID.fromString(p))));
			
		
			players.forEach((p)-> {
				int position = players.indexOf(p);
				position = position +1;
				if (position<=0) position=1;
				
				int playerPoints = 0;				
				if (ffa) {
				
					switch(position) {
					case 1: 
						addRecord(p.getUUID(),"&7- &b&l"+getTournamentName(t)+" Winner"); 
						break;
					case 2: 
						addRecord(p.getUUID(),"&7- &6#2 at " +getTournamentName(t)); 
					break;
					case 3: 
						addRecord(p.getUUID(),"&7- &f#3 at " +getTournamentName(t)); 
					break;
					}
					if (position<=10) {
						playerPoints = playerPoints + 250+((10-position)*50);
					} else {
						playerPoints = playerPoints +100;
					}
					
				} else {				
					int winner = 1;
					int finalist = 2;
					int semifinalist = 4;		
					int quarterfinalist = 8;
					if (teams) {
						winner = 2;
						finalist = 4;
						semifinalist = 8;
					 quarterfinalist = 16;
					}		
					int multiplier = league.equalsIgnoreCase("A") ?  2 : 1;
						if (position<=winner) {
							addRecord(p.getUUID(),"&7- &b&l"+getTournamentName(t)+" Winner");
							playerPoints = playerPoints + (2500/(3-multiplier));						
						} else if (position<=finalist) {
							addRecord(p.getUUID(),"&7- &6"+getTournamentName(t)+" Finalist");
							playerPoints = playerPoints + (1500/(3-multiplier));
						} else if (position<=semifinalist) {
							addRecord(p.getUUID(),"&7- &f"+getTournamentName(t)+" Semifinalist");
							playerPoints = playerPoints + (1250/(3-multiplier));;
						} else if (position<=quarterfinalist) {
							playerPoints = playerPoints + (750/(3-multiplier));
						} else {
							playerPoints = playerPoints + (500/(3-multiplier));
						}		
				}
				if (!weekly) playerPoints = playerPoints*4;
				if (teams) playerPoints = playerPoints/2;				
				if (ranking.getRanking().containsKey(p.getUUID())) playerPoints = playerPoints +ranking.getRanking().get(p.getUUID());
				ranking.getRanking().put(p.getUUID(), playerPoints);
				
			});
			
		}
	}
	
	
	public void backup() {
		try {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM_yyyy");
		String string = format.format(now);
		File copied = new File(Main.get().getDataFolder()+"/"+string+"_ranking.yml");
		File original = new File(Main.get().getDataFolder()+"/ranking.yml");
	    try (
	      InputStream in = new BufferedInputStream(
	        new FileInputStream(original));
	      OutputStream out = new BufferedOutputStream(
	        new FileOutputStream(copied))) {
	 
	        byte[] buffer = new byte[1024];
	        int lengthRead;
	        while ((lengthRead = in.read(buffer)) > 0) {
	            out.write(buffer, 0, lengthRead);
	            out.flush();
	        }
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		} catch (Exception e) {}
		
	}
	
	
	public void saveTournament(String name,List<SpleefPlayer> players,boolean teams,boolean weekly, boolean ffa, String league) {
		 Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String time = format.format(now);
		 Main.tournaments.getConfig().set("tournaments."+name+".date", time);
		 Main.tournaments.getConfig().set("tournaments."+name+".teams", teams);
		 Main.tournaments.getConfig().set("tournaments."+name+".weekly", weekly);
		 Main.tournaments.getConfig().set("tournaments."+name+".ffa", ffa);
		 Main.tournaments.getConfig().set("tournaments."+name+".league", league);
		 List<String> list = new ArrayList<String>();
		 players.forEach((p)-> list.add(p.getUUID().toString()));
		 
		 Main.tournaments.getConfig().set("tournaments."+name+".players", list);
		 Main.tournaments.saveConfig();
	}


	public void sendRanking(String[] args, CommandSender sender) {
		int page = 0;
			try {
				page = Integer.parseInt(args[0]);
				if (page<=0) {
					sender.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
				}
				page=page-1;
			} catch (Exception e) {
				sender.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
			}
		
		int pag = page+1;
		LinkedHashMap<UUID, Integer> hashmap = getRanking().getRanking();
		int total = (hashmap.size()/10)+1;		
		if (page<=total-1) {
		sender.sendMessage("§6-=-=-=-[§a§l Ranking ("+pag+"/"+total+")§6]-=-=-=-");
		StatsManager.getManager().sendRanking(sender, page, hashmap);
		new BukkitRunnable() {
			public void run() {
		sender.sendMessage("§6-=-=-=-[§a§l Ranking ("+pag+"/"+total+")§6]-=-=-=-");
		}
			}.runTaskLater(Main.get(), 5L);
		} else {
			sender.sendMessage("§cPage not found.");
		}
		
	}
	
	
	
	private String getTournamentName(String t) {
		t = t.replace("_", " ");
		return t;
	}
	
}
