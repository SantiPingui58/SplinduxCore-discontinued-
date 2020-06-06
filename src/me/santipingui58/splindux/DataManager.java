package me.santipingui58.splindux;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.game.PlayerOptions;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.particles.ParticleManager;
import me.santipingui58.splindux.particles.effect.ParticleEffectType;
import me.santipingui58.splindux.particles.type.ParticleTypeSubType;
import me.santipingui58.splindux.replay.BrokenBlock;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.stats.RankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.utils.GetCountry;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.translate.Language;
import ru.tehkode.permissions.bukkit.PermissionsEx;




//In DataManager class is handled everything related to the Data of the Server, such as arenas, players, load of data and save of data, etc.

public class  DataManager {
	private static DataManager manager;	
	 public static DataManager getManager() {
	        if (manager == null)
	        	manager = new DataManager();
	        return manager;
	    }
	 
	 private List<SpleefArena> arenas = new ArrayList<SpleefArena>();
	 private List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
	 private List<GameReplay> recordings = new ArrayList<GameReplay>();
	 private HashMap<String,SpleefPlayer> playershashmap = new HashMap<String,SpleefPlayer>();
	 
	 public List<SpleefArena> getArenas() {
		 return this.arenas;
	 }
	 
	 public List<GameReplay> getReplays() {
		 return this.recordings;
	 }
	 
	 public List<SpleefPlayer> getPlayers() {
		 return this.players;
	 }
	 
	 public List<SpleefPlayer> getOnlinePlayers() {
		 List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		 for (SpleefPlayer sp : getPlayers()) {
			 if (Bukkit.getOnlinePlayers().contains(sp.getPlayer())) {
			 list.add(sp);
			 }
		 }
		 return list;
	 }
	 
	 public HashMap<String,SpleefPlayer> getPlayersCache() {
		 return this.playershashmap;
	 }
	 
	 
	 
		public void createSpleefPlayer(UUID uuid) {
			Main.data.getConfig().set("players."+uuid+".stats.ELO",1000);
			 Main.data.getConfig().set("players."+uuid+".stats.1vs1_wins",0);
			 Main.data.getConfig().set("players."+uuid+".stats.1vs1_games",0);
			 Main.data.getConfig().set("players."+uuid+".stats.FFA_wins",0);
			 Main.data.getConfig().set("players."+uuid+".stats.FFA_games",0);
			 Main.data.getConfig().set("players."+uuid+".stats.FFA_kills",0);
			 
			 Main.data.getConfig().set("players."+uuid+".stats.monthly.FFA_wins",0);
			 Main.data.getConfig().set("players."+uuid+".stats.monthly.FFA_games",0);
			 Main.data.getConfig().set("players."+uuid+".stats.monthly.FFA_kills",0);
			 
			 Main.data.getConfig().set("players."+uuid+".stats.weekly.FFA_wins",0);
			 Main.data.getConfig().set("players."+uuid+".stats.weekly.FFA_games",0);
			 Main.data.getConfig().set("players."+uuid+".stats.weekly.FFA_kills",0);
			 Main.data.getConfig().set("players."+uuid+".dailywinlimit",0);
			
			 
			 Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Main.data.getConfig().set("players."+uuid+".registerdate", format.format(now));
			Main.data.getConfig().set("players."+uuid+".onlinetime",0);
			Main.data.getConfig().set("players."+uuid+".coins",0);		
			Main.data.getConfig().set("players."+uuid+".options.translate",true);	
			Main.data.saveConfig();
			
			loadPlayer(uuid.toString(),false);
			
			//p.kickPlayer("§cPlayerData created, please join again!");
			
			
			
		}
	
		
		public void loadReplays() {
			if (Main.recordings.getConfig().contains("replays")) {
				 Set<String> replays = Main.data.getConfig().getConfigurationSection("replays").getKeys(false);
				 for (String r : replays) {
					 r.split("");
				 }
			}
		}
		
		
		public void loadPlayer(String p,boolean reload_data) {
			
			 if (Main.data.getConfig().contains("players."+p)) {
				 int ELO = Main.data.getConfig().getInt("players."+p+".stats.ELO");
				 int _1vs1wins = Main.data.getConfig().getInt("players."+p+".stats.1vs1_wins");
				 int _1vs1games = Main.data.getConfig().getInt("players."+p+".stats.1vs1_games");
				 int FFAwins = Main.data.getConfig().getInt("players."+p+".stats.FFA_wins");
				 int FFAgames = Main.data.getConfig().getInt("players."+p+".stats.FFA_games");
				 int FFAkills = 0;
				 int mutations = Main.data.getConfig().getInt("players."+p+".mutation_tokens");
				 if (Main.data.getConfig().contains("players."+p+".stats.FFA_kills")) {
				  FFAkills = Main.data.getConfig().getInt("players."+p+".stats.FFA_kills");
				 }
				 int totalonlinetime =  Main.data.getConfig().getInt("players."+p+".onlinetime");
				 
				 int FFAWeeklyWins = 0;
				 int FFAWeeklyGames = 0;
				 int FFAWeeklyKills = 0;
				 int FFAMonthlyWins =0;
				 int FFAMonthlyGames = 0;
				 int FFAMonthlyKills = 0;
				 int dailylimit= 0;
				 int level = 0;
	
				 String ip = "";
				 Date d = null;
				 
				 PlayerOptions options = null;
				 String country = null;
				 boolean translate = false;
				 boolean nightVision = false;
				 boolean ads = false;	
				 boolean joinMessage = false;
				 Language language = null;				
				 ChatColor chatcolor = null;

				 
				 if (Main.data.getConfig().contains("players."+p+".dailywinlimit")) {
					 dailylimit =  Main.data.getConfig().getInt("players."+p+".dailywinlimit");
				 }

				 if (Main.data.getConfig().contains("players."+p+".level")) {
					 level =  Main.data.getConfig().getInt("players."+p+".level");
				 }
				 if (Main.data.getConfig().contains("players."+p+".stats.weekly")) {
					 FFAWeeklyWins = Main.data.getConfig().getInt("players."+p+".stats.weekly.FFA_wins");
					 FFAWeeklyGames = Main.data.getConfig().getInt("players."+p+".stats.weekly.FFA_games");
					 FFAWeeklyKills = Main.data.getConfig().getInt("players."+p+".stats.weekly.FFA_kills");
					 }
				 
				 if (Main.data.getConfig().contains("players."+p+".stats.monthly")) {
					 FFAMonthlyWins = Main.data.getConfig().getInt("players."+p+".stats.monthly.FFA_wins");
					 FFAMonthlyGames = Main.data.getConfig().getInt("players."+p+".stats.monthly.FFA_games");
					 FFAMonthlyKills = Main.data.getConfig().getInt("players."+p+".stats.monthly.FFA_kills");
					 }
				 
				 if (Main.data.getConfig().contains("players."+p+".IP")) {
					 ip = Main.data.getConfig().getString("players."+p+".IP");
				 }
				 
				 if (Main.data.getConfig().contains("players."+p+".lastlogin")) {
					   try {
						d= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse((Main.data.getConfig().getString("players."+p+".lastlogin")));
					} catch (ParseException e) {
						
					}  
				 }
				 
				 if (Main.data.getConfig().contains("players."+p+".country")) {
					 country =Main.data.getConfig().getString("players."+p+".country");
				 } 
				 
				 if (Main.data.getConfig().contains("players."+p+".options")) {
					 joinMessage = Main.data.getConfig().getBoolean("players."+p+".options.join_message");
					 if (Main.data.getConfig().contains("players."+p+".options.color")) {
					 chatcolor = ChatColor.valueOf(Main.data.getConfig().getString("players."+p+".options.color"));		
					 }
					 translate = Main.data.getConfig().getBoolean("players."+p+".options.translate");
					 nightVision = Main.data.getConfig().getBoolean("players."+p+".options.nightvision");
					 ads = Main.data.getConfig().getBoolean("players."+p+".options.ads");
					 if (Main.data.getConfig().contains("players."+p+".options.language")) {
					 language = Language.valueOf(Main.data.getConfig().getString("players."+p+".options.language"));
					 } else if (country!=null){
						 language = languageFromCountry(country);	
					 } else {
						 
					 }
					 
				 } else {
					options = new PlayerOptions();
				 }
				 
				
				 if (Main.data.getConfig().contains("players."+p+".options.language")) {
					 
				 } else 
				 
				 if (Main.data.getConfig().contains("players."+p+".IP")) {
					 ip = Main.data.getConfig().getString("players."+p+".IP");
				 }
				 
				 String register = Main.data.getConfig().getString("players."+p+".registerdate");
				 Date date = null;
					   try {
						date=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(register);
					} catch (ParseException e) {
						e.printStackTrace();
					} 
				
				 
				 SpleefPlayer sp = new SpleefPlayer(UUID.fromString(p));	
				 sp.setTotalOnlineTIme(totalonlinetime);
				 sp.setELO(ELO);
				 sp.set1vs1Wins(_1vs1wins);
				 sp.set1vs1Games(_1vs1games);
				 sp.setFFAWins(FFAwins);
				 sp.setFFAGames(FFAgames);
				 sp.setFFAKills(FFAkills);
				 sp.setRegisterDate(date);
				 sp.setDailyWinLimit(dailylimit);
				 sp.setWeeklyFFAWins(FFAWeeklyWins);
				 sp.setWeeklyFFAGames(FFAWeeklyGames);
				 sp.setWeeklyFFAKills(FFAWeeklyKills);
				 sp.setLevel(level);
				 sp.setMonthlyFFAWins(FFAMonthlyWins);
				 sp.setMonthlyFFAGames(FFAMonthlyGames);
				 sp.setMonthlyFFAKills(FFAMonthlyKills);
				 sp.setIP(ip);
				 sp.setMutationTokens(mutations);
				 sp.setLastLogin(d);
				 sp.setCountry(country);
				 sp.back();
				 if (options!=null) {
					 sp.setOptions(options);
				 } else {
					 sp.setOptions(new PlayerOptions(nightVision,translate,language,ads,joinMessage,chatcolor));
				 }
				 this.players.add(sp);
				 if (sp.getOfflinePlayer().isOnline() && !reload_data) {
					sp.giveLobbyItems();
				 }
				 
			 }
			 
			 
		}
		
		
		public void loadParticles() {
				 for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
					 ParticleTypeSubType type = null;
					 ParticleEffectType effect = null;
					 if (Main.data.getConfig().contains("players."+sp.getOfflinePlayer().getUniqueId().toString()+".particles.effect")) {
						 effect = ParticleEffectType.valueOf(Main.data.getConfig().getString("players."+sp.getOfflinePlayer().getUniqueId().toString()+".particles.effect"));
					 }
					 
					 if (Main.data.getConfig().contains("players."+sp.getOfflinePlayer().getUniqueId().toString()+".particles.type")) {
						 type = ParticleTypeSubType.valueOf(Main.data.getConfig().getString("players."+sp.getOfflinePlayer().getUniqueId().toString()+".particles.type"));
					 }
					 
					 if (type!=null) {
						 sp.selectParticleType(ParticleManager.getManager().getTypeBySubType(type),false);
						 }
						 if (effect!=null) {					 
						 sp.selectParticleEffect(ParticleManager.getManager().getEffectByType(effect),false);
						 }
			 }
				
			 
		}
		
	 public void loadPlayers(boolean reload_data) {
		 	this.players.clear();
			 if (Main.data.getConfig().contains("players")) {
				 Set<String> players = Main.data.getConfig().getConfigurationSection("players").getKeys(false);
				 for (String p : players) {
					 loadPlayer(p,reload_data);
			 }
				
			 }
			 Bukkit.getServer().getLogger().info(this.players.size()+" players loaded!");
		 }
	 
	 
	 
	 public void savePlayers() {
		 for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
			 saveData(sp);
		 }
	 }
	 
	 public void saveReplays() {
		 for (GameReplay replay : DataManager.getManager().getReplays()) {
			 for (BrokenBlock broken : replay.getBrokenBlocks()) {
			 Main.recordings.getConfig().set("replays."+replay.getName()+".brokenblocks."+broken.getUUID().toString()+".time",broken.getTime());
			 Main.recordings.getConfig().set("replays."+replay.getName()+".brokenblocks."+broken.getUUID().toString()+".location",Utils.getUtils().setLoc(broken.getLocation(), false));
		 }
		 }
		 
		 Main.recordings.saveConfig();
	 }
	 
	 
	 public void saveData(SpleefPlayer sp) {
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".name",sp.getOfflinePlayer().getName());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.totalgames",sp.getTotalGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.ELO",sp.getELO());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.1vs1_wins",sp.get1vs1Wins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.1vs1_games",sp.get1vs1Games());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_wins",sp.getFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_games",sp.getFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_kills",sp.getFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".level",sp.getLevel());
		 if (sp.getSelectedParticleEffect()==null) {
			 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".particles.effect",null);
		 } else {
			 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".particles.effect",sp.getSelectedParticleEffect().getType().toString());
		 }
		 if (sp.getSelectedParticleType()==null) {
			 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".particles.type",null);
		 } else {
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".particles.type",sp.getSelectedParticleType().getType().toString());
		 }
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".onlinetime", sp.getTotalOnlineTime());	 
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_wins",sp.getWeeklyFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_games",sp.getWeeklyFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_kills",sp.getWeeklyFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_wins",sp.getMonthlyFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_games",sp.getMonthlyFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_kills",sp.getMonthlyFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".coins",sp.getCoins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".dailywinlimit",sp.getDailyWinLimit());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".country",sp.getCountry());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".mutation_tokens",sp.getMutationTokens());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.translate",sp.getOptions().hasTranslate());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.ads",sp.getOptions().hasAds());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.nightvision",sp.getOptions().hasNightVision());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.language",sp.getOptions().getLanguage().toString());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.color",sp.getOptions().getDefaultColorChat().name());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.join_message",sp.getOptions().joinMessageEnabled());
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			if (sp.getOfflinePlayer().isOnline() && sp.getLastLogin()!=null) {
		Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".lastlogin", format.format(sp.getLastLogin()));
			}
		
		 Main.data.saveConfig();
		 
	 }
	  
	 
	 public void saveIP(SpleefPlayer sp) {
		 Player p = sp.getPlayer();
		 InetAddress ip = p.getAddress().getAddress();
		 if (!Main.data.getConfig().contains("players."+p.getUniqueId()+".IP")) {
			 if (!ip.toString().equalsIgnoreCase(Main.data.getConfig().getString("players."+p.getUniqueId()+".IP"))) {
		   Main.data.getConfig().set("players." + p.getUniqueId() + ".IP", ip.toString());
		   Main.data.saveConfig();
			 }	
		 }
		 
		 if (!Main.data.getConfig().contains("players."+p.getUniqueId().toString()+".country")) {
			 String country = GetCountry.getCountry(ip.toString());
		
			sp.setCountry(country);
			 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".country",sp.getCountry());
			 Main.data.saveConfig();
		 }
		   
	 }
	 public SpleefArena loadArena(String name, Location mainspawn,Location spawn1,Location spawn2,Location lobby,Location arena1,Location arena2,SpleefType spleeftype,GameType gametype,Material item,int min,int max) {  
		 SpleefArena a = null;
		 if (gametype.equals(GameType.FFA)) {
		  a = new SpleefArena(name,mainspawn,lobby,arena1,arena2,spleeftype,gametype);
		 } else if (gametype.equals(GameType.DUEL)) {
			 a = new SpleefArena(name,spawn1,spawn2,arena1,arena2,spleeftype,gametype,item,min,max);
		 }
        this.arenas.add(a);      
       a.reset(false,true);
        return a;
    }
    
    
    
    public void loadArenas() { 
    	int arenasint = 0;
    	if (Main.arenas.getConfig().contains("arenas")) {
    	Set<String> arenas = Main.arenas.getConfig().getConfigurationSection("arenas").getKeys(false);
    	
    		for (String b : arenas) {		
    			try {
    			
        			Location arena1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena1"), false);
        			Location arena2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena2"), false);
    				String stype = Main.arenas.getConfig().getString("arenas."+b+".spleeftype");
    				String gtype = Main.arenas.getConfig().getString("arenas."+b+".gametype");
    				stype = stype.toUpperCase();	
    				gtype = gtype.toUpperCase();	
    				SpleefType spleeftype = SpleefType.valueOf(stype);
    				GameType gametype = GameType.valueOf(gtype);
    				
    				if (gametype.equals(GameType.FFA)) {
    				Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".lobby"), false);	
    				Location mainspawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".mainspawn"), false);
    				DataManager.getManager().loadArena(b,mainspawn,null,null,lobby,arena1,arena2,spleeftype,gametype,null,0,0);
    				} else if (gametype.equals(GameType.DUEL)) {
    					Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn1"), true);
            			Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn2"), true);
            			int min = Main.arenas.getConfig().getInt("arenas."+b+".min_size");
						int max = Main.arenas.getConfig().getInt("arenas."+b+".max_size");
            			String it = null;
            			 it = Main.arenas.getConfig().getString("arenas."+b+".item");
         				it = it.toUpperCase();
         				Material item = Material.valueOf(it);
            			DataManager.getManager().loadArena(b,null,spawn1,spawn2,null,arena1,arena2,spleeftype,gametype,item,min,max);
    				}
				arenasint++;
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}
    		Main.get().getLogger().info(arenasint+ " arenas cargadas!");
    }
     
    
    @SuppressWarnings("deprecation")
	public void resetMonthlyStats() {
    	new BukkitRunnable() {
			public void run() {
    	HologramManager.getManager().updateHolograms(false);
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players."+s+".stats.monthly.FFA_kills", 0);
    		Main.data.getConfig().set("players."+s+".stats.monthly.FFA_games", 0);
    		Main.data.getConfig().set("players."+s+".stats.monthly.FFA_wins", 0);
    	}
    	
    	
    	Main.data.saveConfig();
    	
    	for (SpleefPlayer sp : players) {
    		sp.setMonthlyFFAGames(0);
    		sp.setMonthlyFFAKills(0);
    		sp.setMonthlyFFAWins(0);
    	}
    	
    	
    	HashMap<String, Integer> hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_MONTHLY);
    	HashMap<String,Integer> topPositions = new HashMap<String,Integer>();
   	 Iterator<Entry<String, Integer>> it = hashmap.entrySet().iterator();
   	 int i = 1;
   	    while (it.hasNext()) {
   	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
   	        while(i<=10) {
   	        String s = pair.getKey();
   	       topPositions.put(s, i);
   	       i++;
   	        } 
   	    }
   	
   	
   	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
   		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_kills", 0);
   		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_games", 0);
   		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_wins", 0);
   		String name = Bukkit.getOfflinePlayer(s).getName();
   		if (topPositions.containsKey(name)) {
   			int stars = 0;
   			if (topPositions.get(name)==1) stars = 5;
   			else if (topPositions.get(name)<=4) stars = 4;
   			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gmysteryboxes give " + name+ " 3 "  + stars);
   			
   			OfflinePlayer p = Bukkit.getOfflinePlayer(name);
   			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
   			
   			switch(topPositions.get(name)) {
   			case 1:LevelManager.getManager().addLevel(sp, 750);
   			case 2:LevelManager.getManager().addLevel(sp, 625);
   			case 3:LevelManager.getManager().addLevel(sp, 560);
   			case 4:LevelManager.getManager().addLevel(sp, 500);
   			case 5:LevelManager.getManager().addLevel(sp, 430);
   			case 6:LevelManager.getManager().addLevel(sp, 375);
   			case 7:LevelManager.getManager().addLevel(sp, 310);
   			case 8:LevelManager.getManager().addLevel(sp, 250);
   			case 9:LevelManager.getManager().addLevel(sp, 180);
   			case 10:LevelManager.getManager().addLevel(sp, 125);
   			}
		
   		}
   	}
   	
    }
}.runTaskAsynchronously(Main.get());
    }

    public void resetDailyWinsLimit() {
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players."+s+".dailywinlimit", 0);
    	}
    	
    	Main.data.saveConfig();
    }
    
    @SuppressWarnings("deprecation")
	public void resetWeeklyStats() {
    	new BukkitRunnable() {
			public void run() {
    	HologramManager.getManager().updateHolograms(false);
    	
    	HashMap<String, Integer> hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_WEEKLY);
    	
    	HashMap<String,Integer> topPositions = new HashMap<String,Integer>();
    	 Iterator<Entry<String, Integer>> it = hashmap.entrySet().iterator();
    	 int i = 1;
    	    while (it.hasNext()) {
    	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
    	        while(i<=10) {
    	        String s = pair.getKey();
    	       topPositions.put(s, i);
    	       i++;
    	        } 
    	    }
    	
    	
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_kills", 0);
    		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_games", 0);
    		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_wins", 0);
    		String name = Bukkit.getOfflinePlayer(s).getName();
    		if (topPositions.containsKey(name)) {
    			int stars = 0;
    			if (topPositions.get(name)==1) stars = 5;
    			else if (topPositions.get(name)<=4) stars = 4;
    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gmysteryboxes give " + name+ " 1 "  + stars);
    			
    			OfflinePlayer p = Bukkit.getOfflinePlayer(name);
    			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
    			
    			switch(topPositions.get(name)) {
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
		
    		}
    		 
    	}
    	
    	 	
    	Main.data.saveConfig();
    	
    	 
    	
    	for (SpleefPlayer sp : players) {
    		sp.setWeeklyFFAGames(0);
    		sp.setWeeklyFFAKills(0);
    		sp.setWeeklyFFAWins(0);
    	}
    	
			}
		}.runTaskAsynchronously(Main.get());
    }
    public ItemStack[] gameitems() {
		ItemStack iron_shovel = new ItemStack(Material.IRON_SPADE);
		ItemMeta ironMeta = iron_shovel.getItemMeta();
		ironMeta.setUnbreakable(true);
		ironMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		ironMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		iron_shovel.setItemMeta(ironMeta);
		
		ItemStack diamond_shovel = new ItemStack(Material.DIAMOND_SPADE);
		ItemMeta diamondMeta = diamond_shovel.getItemMeta();
		diamondMeta.setUnbreakable(true);
		diamondMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		diamondMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		diamond_shovel.setItemMeta(diamondMeta);
		
		ItemStack golden_shovel = new ItemStack(Material.GOLD_SPADE);
		ItemMeta goldenMeta = diamond_shovel.getItemMeta();
		goldenMeta.setUnbreakable(true);
		goldenMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		goldenMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		golden_shovel.setItemMeta(goldenMeta);
		
		ItemStack x2snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(2).build();
		ItemStack x4snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(4).build();
		ItemStack x6snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(6).build();
		ItemStack x8snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(8).build();
		ItemStack x10snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(10).build();
		
		ItemStack redflag = new ItemBuilder(Material.WOOL,1,(byte)14).build();
		ItemStack blueflag = new ItemBuilder(Material.WOOL,1,(byte)11).build();
	
		ItemStack[] items = {iron_shovel, diamond_shovel, x2snowball,x4snowball,x6snowball,x8snowball,x10snowball,blueflag,redflag,golden_shovel};
		return items;
	}
    
	public ItemStack[] lobbyitems() {
		
		ItemStack gadgets = new ItemBuilder(Material.CHEST).setTitle("§d§lCosmetics").build();
		ItemStack parkour = new ItemBuilder(Material.FEATHER).setTitle("§b§lParkour").build();
		ItemStack options = new ItemBuilder(Material.REDSTONE_COMPARATOR).setTitle("§c§lOptions").build();
		ItemStack ranked = new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§6§lRanked").build();
		ItemStack unranked = new ItemBuilder(Material.IRON_SPADE).setTitle("§b§lUnranked").build();
		ItemStack[] items = {gadgets,parkour,options,ranked, unranked};
		
		return items;
	}
	
	public ItemStack[] queueitems() {
		ItemStack powerups = new ItemBuilder(Material.ENDER_CHEST).setTitle("§d§lMutations").build();
		ItemStack leave = new ItemBuilder(Material.REDSTONE_TORCH_ON).setTitle("§c§lLeave").build();
		ItemStack[] items = {powerups, leave};
		return items;
	}
	
	
	
	public Language languageFromCountry(String s) {
		if (s.equalsIgnoreCase("AR") 
			|| s.equalsIgnoreCase("ES")
			|| s.equalsIgnoreCase("BO")
			|| s.equalsIgnoreCase("BR")
			|| s.equalsIgnoreCase("CL")
			|| s.equalsIgnoreCase("UY")
			|| s.equalsIgnoreCase("PY")
			|| s.equalsIgnoreCase("CO")
			|| s.equalsIgnoreCase("PE")
			|| s.equalsIgnoreCase("VE")
			|| s.equalsIgnoreCase("MX")
			|| s.equalsIgnoreCase("CU")
			|| s.equalsIgnoreCase("EC")
			|| s.equalsIgnoreCase("GT")
			|| s.equalsIgnoreCase("HN")) {
			return Language.SPANISH;
		} else if (s.equalsIgnoreCase("RU")
				|| s.equalsIgnoreCase("BY")
				|| s.equalsIgnoreCase("KZ")
				|| s.equalsIgnoreCase("KG")
				|| s.equalsIgnoreCase("UZ")){
			return Language.RUSSIAN;
		} else {
			return Language.ENGLISH;
		}
	}

	public void giveMutationTokens() {
		new BukkitRunnable() {
			public void run() {
				
			  	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(s));
					SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			  		int i = Main.data.getConfig().getInt("players."+s+".mutation_tokens");
					if (PermissionsEx.getUser(p.getName()).has("splindux.extreme")) {
					i = i+5;
					} else if (PermissionsEx.getUser(p.getName()).has("splindux.epic")) {
					i = i+3;
					} else if (PermissionsEx.getUser(p.getName()).has("splindux.vip")) {
						i = i+1;
						}
					
				
					Main.data.getConfig().set("players."+s+".mutation_tokens", i);
					sp.setMutationTokens(i);
			  	}
			  
			  	Main.data.saveConfig(); 	
		}
		}.runTaskAsynchronously(Main.get());
	}

	
	}
