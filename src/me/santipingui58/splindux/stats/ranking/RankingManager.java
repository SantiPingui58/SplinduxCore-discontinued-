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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.parkour.ParkourPlayer;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.RankingEnum;
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
		 
		
		
		private void addRecord(SpleefPlayer sp,String record) {
			if (!records.containsKey(sp.getUUID())) records.put(sp.getUUID(), new ArrayList<String>());
			
			records.get(sp.getUUID()).add(record);
			
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
			    Main.ranking.getConfig().set("ranking."+i+".oldposition", ranking.getPosition(sp));
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
		    SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(uuid);
		    Main.ranking.getConfig().set("ranking."+i+".uuid", uuid.toString());
		    Main.ranking.getConfig().set("ranking."+i+".name", Bukkit.getOfflinePlayer(uuid).getName());
		    Main.ranking.getConfig().set("ranking."+i+".points", p);
		    List<String> list = records.get(uuid);		 
		    if (list!=null)sp.getRankingRecords().addAll(list);
		    
		    Main.ranking.getConfig().set("ranking."+i+".records", list);
		    try {
		    Main.ranking.getConfig().set("ranking."+i+".country", sp.getCountry() !=null ? sp.getCountry() : "Unkown");
    
		    Main.ranking.getConfig().set("ranking."+i+".oldposition", oldRanking.getPosition(sp));
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
		
		new BukkitRunnable() {
			public void run() {
	}
			}.runTask(Main.get());	
			
		new BukkitRunnable() {
			public void run() {
				loadRanking();
				loadAllPlayers();
				
				
				new BukkitRunnable() {
					public void run() {
		boolean isEndOfSeason =isEndOfSeason();
		isEndOfSeason = false;
		//SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(Bukkit.getPlayer("SantiPingui58"));
		//Bukkit.getPlayer("SantiPingui58").sendMessage(""+ranking.getRanking().get(sp.getUUID()));
		
		//guildsSeason();
		
		if (isEndOfSeason) {
			decreasePoints();			
		}	
				//Bukkit.getPlayer("SantiPingui58").sendMessage(""+ranking.getRanking().get(sp.getUUID()));
		
		tournaments();		
				//Bukkit.getPlayer("SantiPingui58").sendMessage(""+ranking.getRanking().get(sp.getUUID()));
		 monthlyFFA();	
					//Bukkit.getPlayer("SantiPingui58").sendMessage(""+ranking.getRanking().get(sp.getUUID()));
		if (isEndOfSeason) rankedELO();		
				//Bukkit.getPlayer("SantiPingui58").sendMessage(""+ranking.getRanking().get(sp.getUUID()));
		spleefRankMultiply();
				//Bukkit.getPlayer("SantiPingui58").sendMessage(""+ranking.getRanking().get(sp.getUUID()));
		Ranking oldRanking = new Ranking();
		oldRanking.setRanking(ranking.getRanking());
		ranking.setRanking(StatsManager.getManager().sortByValueLinked(ranking.getRanking()));
		ranking.remove0();
		saveRanking(oldRanking);
			}
				}.runTaskLaterAsynchronously(Main.get(), 120L);
			}
		}.runTaskAsynchronously(Main.get());
	}
	
	
	
	private void loadAllPlayers() {
		for (UUID uuid : HikariAPI.getManager().getAllPlayers()) {
		 new SpleefPlayer(uuid);
			HikariAPI.getManager().loadData(uuid);					
		}
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
		    ranking.getRanking().put(sp, p/4);
		}
	}
	
	
	private void spleefRankMultiply() {
		DataManager.getManager().getPlayers().forEach((sp)-> {
			int playerPoints = 0;
			if (ranking.getRanking().containsKey(sp.getUUID())) playerPoints = ranking.getRanking().get(sp.getUUID());
			int rank = LevelManager.getManager().getRank(sp).getInt();
			double multiply  =1 + ((rank-1)*0.0125);
			playerPoints = (int) (playerPoints*multiply);
			ranking.getRanking().put(sp.getUUID(), playerPoints);
		}); 
	}
	
	public void rankedELO() {
		 StatsManager sm = StatsManager.getManager();
			List<String> top10 = StatsManager.getManager().getTop10Names(sm.getRanking(RankingEnum.SPLEEF1VS1_ELO), 0, RankingEnum.SPLEEF1VS1_ELO);
			top10.clear();
			top10.add("_woozzi_");
			top10.add("TheRioo");
			top10.add("Bratishkin_");
			top10.add("HA3AR41K1");
			top10.add("TotallyAwesome5");
			top10.add("B00mbl4");	
			top10.add("MyNameisVan_");
			top10.add("SirPatata");
			top10.add("CubitoTurtle17");
			top10.add("messengers");
			DataManager.getManager().getPlayers().forEach((sp)-> {
				int playerPoints = 0;
				if (ranking.getRanking().containsKey(sp.getUUID())) playerPoints = playerPoints + ranking.getRanking().get(sp.getUUID());		
				
				if (top10.contains(sp.getOfflinePlayer().getName())) {
					int position = top10.indexOf(sp.getOfflinePlayer().getName())+1;
					playerPoints = playerPoints + 750 + ((10-position)*250);
					addRecord(sp,"&7- &a#"+position + " at Ranked ELO");
				} else {
					playerPoints = playerPoints + 50;
				}
				ranking.getRanking().put(sp.getUUID(), playerPoints);
			}); 
	}
	
	public void guildsSeason() {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		hashmap.put("aprada_",4000);
		hashmap.put("JetFire_JTFR",3000);
		hashmap.put("hosikuzuru",1750);
		hashmap.put("Triffuny",1750);
		hashmap.put("Mcdia",1500);
		hashmap.put("MyNameIsVan_",1250);
		hashmap.put("Neil_",1750);
		hashmap.put("PulpitoDelicioso",1250);
		hashmap.put("MESHOK_S_G08NOM_",2500);
		hashmap.put("CubitoTurtle17",1750);
		hashmap.put("Alexelpro_04",1750);
		hashmap.put("russianhoe",2000);
		hashmap.put("_woozzi_",4000);
		hashmap.put("livanecsboruvky",4000);
		hashmap.put("Jakubusso",2000);
		hashmap.put("Troubina",3000);
		hashmap.put("Wezaz",1500);
		hashmap.put("TheRioo",3500);
		hashmap.put("Mathieu1969",1750);
		hashmap.put("ShadowFlames",1500);
		hashmap.put("HA3AR41K1",4000);
		hashmap.put("lostyt555",3500);
		hashmap.put("SantiPingui58",4000);
		hashmap.put("slenderbrine100",3000);
		hashmap.put("SirPatata",3000);
		hashmap.put("hikarilof",3500);
		hashmap.put("Bratishkin_",1250);
		hashmap.put("Luntario",1750);
		
		DataManager.getManager().getPlayers().forEach((sp)-> {

			int playerPoints = 0;
			if (ranking.getRanking().containsKey(sp.getUUID())) playerPoints = playerPoints +ranking.getRanking().get(sp.getUUID());
			
			ranking.getRanking().put(sp.getUUID(), playerPoints);
			if (hashmap.containsKey(sp.getOfflinePlayer().getName())) {
				playerPoints = playerPoints + hashmap.get(sp.getOfflinePlayer().getName());
			} 
			
			ranking.getRanking().put(sp.getUUID(), playerPoints);
		});

	}
	private void monthlyFFA() {
		StatsManager sm = StatsManager.getManager();
		List<String> top10 = StatsManager.getManager().getTop10Names(sm.getRanking(RankingEnum.SPLEEFFFA_WINS_MONTHLY), 0, RankingEnum.SPLEEFFFA_WINS_MONTHLY);
		top10.clear();
		top10.add("_wozzi_");
		top10.add("thogeth");
		top10.add("tsenumom");
		top10.add("Suunny_");
		top10.add("TheRioo");
		top10.add("Cheburekin123");	
		top10.add("_Nualst_");
		top10.add("Neyron__");
		top10.add("SexMashina_");
		
		DataManager.getManager().getPlayers().forEach((sp)-> {

			int playerPoints = 0;
			if (ranking.getRanking().containsKey(sp.getUUID())) playerPoints = playerPoints +ranking.getRanking().get(sp.getUUID());
			
			ranking.getRanking().put(sp.getUUID(), playerPoints);
			if (top10.contains(sp.getOfflinePlayer().getName())) {
				int position = top10.indexOf(sp.getOfflinePlayer().getName())+1;
				playerPoints = playerPoints + 750 + ((10-position)*250);
				addRecord(sp,"&7- &a#"+position + " at Monthly FFA");
			} else {
				playerPoints = playerPoints + 50;
			}
			
			ranking.getRanking().put(sp.getUUID(), playerPoints);
		});
	}
	
	public void tournaments() {
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
						addRecord(p,"&7- &b&l"+getTournamentName(t)+" Winner"); 
						break;
					case 2: 
						addRecord(p,"&7- &6#2 at " +getTournamentName(t)); 
					break;
					case 3: 
						addRecord(p,"&7- &f#3 at " +getTournamentName(t)); 
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
							addRecord(p,"&7- &b&l"+getTournamentName(t)+" Winner");
							playerPoints = playerPoints + (5000/(3-multiplier));
						} else if (position<=finalist) {
							addRecord(p,"&7- &6"+getTournamentName(t)+" Finalist");
							playerPoints = playerPoints + (3000/(3-multiplier));
						} else if (position<=semifinalist) {
							addRecord(p,"&7- &f"+getTournamentName(t)+" Semifinalist");
							playerPoints = playerPoints + (1500/(3-multiplier));;
						} else if (position<=quarterfinalist) {
							playerPoints = playerPoints + (750/(3-multiplier));
						} else {
							playerPoints = playerPoints + (300/(3-multiplier));
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
