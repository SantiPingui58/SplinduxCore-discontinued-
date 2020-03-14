package me.santipingui58.splindux;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;



public class  DataManager {
	private static DataManager manager;	
	 public static DataManager getManager() {
	        if (manager == null)
	        	manager = new DataManager();
	        return manager;
	    }
	 
	 private List<SpleefArena> arenas = new ArrayList<SpleefArena>();
	 private List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
	 private HashMap<OfflinePlayer,SpleefPlayer> playershashmap = new HashMap<OfflinePlayer,SpleefPlayer>();
	 
	 public List<SpleefArena> getArenas() {
		 return this.arenas;
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
	 
	 public HashMap<OfflinePlayer,SpleefPlayer> getPlayersCache() {
		 return this.playershashmap;
	 }
	 
		public void createSpleefPlayer(Player p) {
			SpleefPlayer nuevo = new SpleefPlayer(p.getUniqueId());
			this.players.add(nuevo);			
			Main.data.getConfig().set("players."+p.getUniqueId()+".stats.ELO",1000);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.1vs1_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.1vs1_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.FFA_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.FFA_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.FFA_kills",0);
			 
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.monthly.FFA_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.monthly.FFA_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.monthly.FFA_kills",0);
			 
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.weekly.FFA_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.weekly.FFA_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.weekly.FFA_kills",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".dailywinlimit",0);
			 saveIP(p);
			 
			 Date now = new Date();
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Main.data.getConfig().set("players."+p.getUniqueId()+".registerdate", format.format(now));
			Main.data.getConfig().set("players."+p.getUniqueId()+".onlinetime",0);
			Main.data.getConfig().set("players."+p.getUniqueId()+".coins",0);
			 Main.data.saveConfig();
			
	
		}
	
	 public void loadPlayers() {
			 if (Main.data.getConfig().contains("players")) {
				 Set<String> players = Main.data.getConfig().getConfigurationSection("players").getKeys(false);
				 for (String p : players) {
					 if (Main.data.getConfig().contains("players."+p)) {
						 int ELO = Main.data.getConfig().getInt("players."+p+".stats.ELO");
						 int _1vs1wins = Main.data.getConfig().getInt("players."+p+".stats.1vs1_wins");
						 int _1vs1games = Main.data.getConfig().getInt("players."+p+".stats.1vs1_games");
						 int FFAwins = Main.data.getConfig().getInt("players."+p+".stats.FFA_wins");
						 int FFAgames = Main.data.getConfig().getInt("players."+p+".stats.FFA_games");
						 int FFAkills = 0;
						 if (Main.data.getConfig().contains("players."+p+".stats.FFA_kills")) {
						  FFAkills = Main.data.getConfig().getInt("players."+p+".stats.FFA_kills");
						 }
						 
						 int FFAWeeklyWins = 0;
						 int FFAWeeklyGames = 0;
						 int FFAWeeklyKills = 0;
						 
						 int FFAMonthlyWins =0;
						 int FFAMonthlyGames = 0;
						 int FFAMonthlyKills = 0;
						 int coins = 0;
						 int dailylimit= 0;
						 int level = 0;
						 int totalonlinetime =  Main.data.getConfig().getInt("players."+p+".onlinetime");
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
						 
						 if (Main.data.getConfig().contains("players."+p+".coins")) {
							 coins = Main.data.getConfig().getInt("players."+p+".coins");
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
						 sp.setCoins(coins);
						 sp.setWeeklyFFAWins(FFAWeeklyWins);
						 sp.setWeeklyFFAGames(FFAWeeklyGames);
						 sp.setWeeklyFFAKills(FFAWeeklyKills);
						 sp.setLevel(level);
						 sp.setMonthlyFFAWins(FFAMonthlyWins);
						 sp.setMonthlyFFAGames(FFAMonthlyGames);
						 sp.setMonthlyFFAKills(FFAMonthlyKills);
						 this.players.add(sp);
						 if (sp.getOfflinePlayer().isOnline()) {
							 DataManager.getManager().giveLobbyItems(sp);
						 }
						 
					 }
			 }
				
			 }
			 Bukkit.getServer().getLogger().info(this.players.size()+" players loaded!");
		 }
	 
	 
	 
	 public void savePlayers() {
		 for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
			 saveData(sp);
		 }
	 }
	 
	 public void saveData(SpleefPlayer sp) {
		 
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.totalgames",sp.getTotalGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.ELO",sp.getELO());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.1vs1_wins",sp.get1vs1Wins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.1vs1_games",sp.get1vs1Games());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_wins",sp.getFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_games",sp.getFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_kills",sp.getFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".level",sp.getLevel());
		Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".onlinetime", sp.getTotalOnlineTime());
		
		 
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_wins",sp.getWeeklyFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_games",sp.getWeeklyFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_kills",sp.getWeeklyFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_wins",sp.getMonthlyFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_games",sp.getMonthlyFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_kills",sp.getMonthlyFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".coins",sp.getCoins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".dailywinlimit",sp.getDailyWinLimit());
		 
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			if (sp.getOfflinePlayer().isOnline()) {
		Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".lastlogin", format.format(sp.getLastLogin()));
			}
		
		 Main.data.saveConfig();
		 
	 }
	  
	 
	 public void saveIP(Player p) {
		 InetAddress ip = p.getAddress().getAddress();
		 if (!Main.data.getConfig().contains("players."+p.getUniqueId()+".IP")) {
			 if (!ip.toString().equalsIgnoreCase(Main.data.getConfig().getString("players."+p.getUniqueId()+".IP"))) {
		   Main.data.getConfig().set("players." + p.getUniqueId() + ".IP", ip.toString());
		   Main.data.saveConfig();
			 }
		 }
		   
	 }
	 public SpleefArena loadArena(String name, Location mainspawn,Location spawn1,Location spawn2,Location lobby,Location arena1,Location arena2,SpleefType type,Material item) {  
		 SpleefArena a = null;
		 if (type.equals(SpleefType.SPLEEFFFA)) {
		  a = new SpleefArena(name,mainspawn,lobby,arena1,arena2,type);
		 } else if (type.equals(SpleefType.SPLEEF1VS1)) {
			 a = new SpleefArena(name,spawn1,spawn2,lobby,arena1,arena2,type,item);
		 }
        this.arenas.add(a);      
        GameManager.getManager().resetArena(a);
        return a;
    }
    
    
    
    public void loadArenas() { 
    	int arenasint = 0;
    	if (Main.arenas.getConfig().contains("arenas")) {
    	Set<String> arenas = Main.arenas.getConfig().getConfigurationSection("arenas").getKeys(false);
    	
    		for (String b : arenas) {		
    			try {
    				Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".lobby"), false);	
        			Location arena1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena1"), false);
        			Location arena2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena2"), false);
    				String type = Main.arenas.getConfig().getString("arenas."+b+".type");
    				type = type.toUpperCase();		
    				SpleefType spleeftype = SpleefType.valueOf(type);
    			
    				
    				if (spleeftype.equals(SpleefType.SPLEEFFFA)) {
    			Location mainspawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".mainspawn"), false);
				DataManager.getManager().loadArena(b,mainspawn,null,null,lobby,arena1,arena2,spleeftype,null);
    				} else if (spleeftype.equals(SpleefType.SPLEEF1VS1)) {
    					Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn1"), true);
            			Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn2"), true);
            			String it = null;
            			 it = Main.arenas.getConfig().getString("arenas."+b+".item");
         				it = it.toUpperCase();
         				Material item = Material.valueOf(it);
            			DataManager.getManager().loadArena(b,null,spawn1,spawn2,lobby,arena1,arena2,spleeftype, item);
    				}
				arenasint++;
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}
    		Main.get().getLogger().info(arenasint+ " arenas cargadas!");
    }
     
    
    public void resetMonthlyStats() {
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players"+s+".stats.monthly.FFA_kills", 0);
    		Main.data.getConfig().set("players"+s+".stats.monthly.FFA_games", 0);
    		Main.data.getConfig().set("players"+s+".stats.monthly.FFA_games", 0);
    	}
    	
    	Main.data.saveConfig();
    	
    	for (SpleefPlayer sp : this.players) {
    		sp.setMonthlyFFAGames(0);
    		sp.setMonthlyFFAKills(0);
    		sp.setMonthlyFFAWins(0);
    	}
    }

    public void resetDailyWinsLimit() {
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players."+s+".dailywinlimit", 0);
    	}
    	
    	Main.data.saveConfig();
    }
    
    public void resetWeeklyStats() {
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players"+s+".stats.weekly.FFA_kills", 0);
    		Main.data.getConfig().set("players"+s+".stats.weekly.FFA_games", 0);
    		Main.data.getConfig().set("players"+s+".stats.weekly.FFA_games", 0);
    	}
    	
    	Main.data.saveConfig();
    	
    	for (SpleefPlayer sp : this.players) {
    		sp.setWeeklyFFAGames(0);
    		sp.setWeeklyFFAKills(0);
    		sp.setWeeklyFFAWins(0);
    	}
    }
    
	
	public void giveGameItems(SpleefPlayer sp) {
		sp.getPlayer().getInventory().clear();
		SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
		if (sp.getPlayer().hasPermission("splindux.diamondshovel")) {
			sp.getPlayer().getInventory().setItem(0, gameitems()[1]);
		} else {
			sp.getPlayer().getInventory().setItem(0, gameitems()[0]);
		}
		if (arena.getType().equals(SpleefType.SPLEEFFFA)) {
		if (sp.getPlayer().hasPermission("splindux.x10snowballs")) {
			sp.getPlayer().getInventory().setItem(1, gameitems()[6]);
		} else if (sp.getPlayer().hasPermission("splindux.x8snowballs")) {
			sp.getPlayer().getInventory().setItem(1, gameitems()[5]);
		}else if (sp.getPlayer().hasPermission("splindux.x6snowballs")) {
			sp.getPlayer().getInventory().setItem(1, gameitems()[4]);
		}else if (sp.getPlayer().hasPermission("splindux.x4snowballs")) {
			sp.getPlayer().getInventory().setItem(1, gameitems()[3]);
		} else {
			sp.getPlayer().getInventory().setItem(1, gameitems()[2]);
		}
			
		
		//giveLeaderboardHelmets(sp);
		}
	}
	
	public void giveLobbyItems(SpleefPlayer sp) {
		
		sp.getPlayer().setGameMode(GameMode.ADVENTURE);
		sp.getPlayer().getInventory().clear();
		sp.getPlayer().getInventory().setItem(4, lobbyitems()[0]);	
		sp.getPlayer().getInventory().setItem(5, lobbyitems()[1]);
	}
	
	public void giveQueueItems(SpleefPlayer sp) {
		sp.getPlayer().setGameMode(GameMode.ADVENTURE);
		sp.getPlayer().getInventory().clear();
		sp.getPlayer().getInventory().setItem(7, queueitems()[0]);	
		sp.getPlayer().getInventory().setItem(8, queueitems()[1]);
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
		
		ItemStack x2snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(2).build();
		ItemStack x4snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(4).build();
		ItemStack x6snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(6).build();
		ItemStack x8snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(8).build();
		ItemStack x10snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(10).build();
		
		ItemStack[] items = {iron_shovel, diamond_shovel, x2snowball,x4snowball,x6snowball,x8snowball,x10snowball};
		return items;
	}
	
	public ItemStack[] lobbyitems() {
		
		ItemStack gadgets = new ItemBuilder(Material.CHEST).setTitle("§6§lGadgets").build();
		ItemStack ranked = new ItemBuilder(Material.NETHER_STAR).setTitle("§a§lRanked").addLore("§cComing Soon").build();
		ItemStack[] items = {ranked,gadgets};
		return items;
	}
	
	public ItemStack[] queueitems() {
		ItemStack powerups = new ItemBuilder(Material.ENDER_CHEST).setTitle("§d§lPowerUps").addLore("§cComing Soon").build();
		ItemStack leave = new ItemBuilder(Material.REDSTONE_TORCH_ON).setTitle("§c§lLeave").build();
		ItemStack[] items = {powerups, leave};
		return items;
	}
	
	
	

	/*
	private void giveLeaderboardHelmets(SpleefPlayer sp) {
		Leaderboard leaderboard = LeaderboardManager.getManager().getLeaderboardByType(LeaderboardType.ALL_TIME_FFA_WINS);
		if (leaderboard.getTop20().contains(sp.getPlayer().getName())) {
		if (leaderboard.getTop20().get(0).equalsIgnoreCase(sp.getPlayer().getName())) {
			sp.getPlayer().getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		} else if (leaderboard.getTop20().get(1).equalsIgnoreCase(sp.getPlayer().getName())) {
			sp.getPlayer().getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
		} else if (leaderboard.getTop20().get(2).equalsIgnoreCase(sp.getPlayer().getName())) {
			sp.getPlayer().getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
		} else {
			List<String> list = new ArrayList<String>();
			int y = 0;
			for (String s : leaderboard.getTop20()) {
				list.add(s);
				y++;
				
				if(y>=10) {
					break;
				}
			}
			
			if (list.contains(sp.getPlayer().getName())) {
				sp.getPlayer().getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
			}  else {
				sp.getPlayer().getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			}
		}
	}
	}
	*/
	
	
	}
