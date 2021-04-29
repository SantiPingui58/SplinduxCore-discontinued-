package me.santipingui58.splindux;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.CATFArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.replay.BrokenBlock;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.URLUtils;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.translate.Language;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;




//In DataManager class is handled everything related to the Data of the Server, such as arenas, players, load of data and save of data, etc.

public class  DataManager {
	private static DataManager manager;	
		
		/**
	    * @return Returns DataManager instance.
	    */
	 public static DataManager getManager() {
	        if (manager == null)
	        	manager = new DataManager();
	        return manager;
	    }
	 
	 //Lists of arenas, players, recordings.
	 private List<Arena> arenas = new ArrayList<Arena>();
	 
	 private List<Arena> tntRunArenas = new ArrayList<Arena>();
	 private List<Arena> spleggArenas = new ArrayList<Arena>();
	 
	 private HashMap<UUID,SpleefPlayer> players = new HashMap<UUID,SpleefPlayer>();
	 
	 private HashSet<SpleefPlayer> toUnload = new HashSet<SpleefPlayer>();
	 
	 private List<GameReplay> recordings = new ArrayList<GameReplay>();
	 
	 private boolean queuesClosed;
	 
	 
	 private LinkedList<UUID> playingPlayers = new LinkedList<UUID>();
	 
	 private LinkedList<UUID> lobbyPlayers = new LinkedList<UUID>();
	 
	  
	 public LinkedList<UUID> getPlayingPlayers() {
		 return this.playingPlayers;
	 }
	 
	 
	 public LinkedList<UUID> getLobbyPlayers() {
		 return this.lobbyPlayers;
	 }
 	 
	 
	 
	 public List<Arena> getFFAArenas(SpleefType type) {
		 if (type.equals(SpleefType.SPLEGG)) {
		 return this.spleggArenas;
		 } 
		 return this.tntRunArenas;
	 }
	 
	 	/**
	    * @return Returns List of SpleefArenas loaded
	    */
	 public List<Arena> getArenas() {
		 return this.arenas;
	 }
	 
	 
	 public HashSet<SpleefPlayer> getToUnloadSet() {
		 return this.toUnload;
	 }
	 
	 public boolean areQueuesClosed() {
		 return this.queuesClosed;
	 }
	 
	 
	 public void queues(boolean b) {
		 this.queuesClosed = b;
	 }
	 
	 	/**
	    * @return Returns List of Replays loaded. Not used
	    */
	 public List<GameReplay> getReplays() {
		 return this.recordings;
	 }
	 
	 	/**
	    * @return Returns List of Players loaded. All players are loaded when the plugin is enabled.
	    */
	 public List<SpleefPlayer> getPlayers() {
		return new ArrayList<SpleefPlayer>(this.players.values());
	 }
	 

	 public SpleefPlayer getPlayer(UUID uuid) {
		 return this.players.get(uuid);
	 }
	 
	 public boolean isPlayerLoaded(UUID uuid) {
		 return this.players.containsKey(uuid);
	 }
	 

	 public void addPlayer(UUID uuid, SpleefPlayer sp) {
		 this.players.put(uuid, sp);
	 }
	 
	 public void removePlayer(UUID uuid) {
		 this.players.remove(uuid);
	 }
		
	 
	 /**
		* Saves Replays. Not used
	    */
	 public void saveReplays() {
		 for (GameReplay replay : DataManager.getManager().getReplays()) {
			 for (BrokenBlock broken : replay.getBrokenBlocks()) {
			 Main.recordings.getConfig().set("replays."+replay.getName()+".brokenblocks."+broken.getUUID().toString()+".time",broken.getTime());
			 Main.recordings.getConfig().set("replays."+replay.getName()+".brokenblocks."+broken.getUUID().toString()+".location",Utils.getUtils().setLoc(broken.getLocation(), false));
		 }
		 }
		 
		 Main.recordings.saveConfig();
	 }
	 
	 

	 
	 public Arena loadArena(String name, Location mainspawn,Location spawn1,Location spawn2,Location lobby,Location arena1,Location arena2,
			 SpleefType spleeftype,GameType gametype,Material item,int min,int max,Location flag1, Location flag2) {  
		 Arena a = null;
		 if (gametype.equals(GameType.FFA)) {
		  a = new Arena(name,mainspawn,lobby,arena1,arena2,spleeftype,gametype,min,max);  
		 } else if (gametype.equals(GameType.DUEL)) {
			 a = new Arena(name,spawn1,spawn2,arena1,arena2,lobby,spleeftype,gametype,item,min,max);
		 } else if (gametype.equals(GameType.CATF)) {
			 a = new CATFArena(name, spawn1, spawn2, arena1, arena2, lobby, item, flag1, flag2);
		 }
        this.arenas.add(a);      
        return a;
    }
	 
    
    
    public void loadArenas() { 
    	new BukkitRunnable() {
    		public void run() {
    	    	int arenasint = 0;
    	if (Main.arenas.getConfig().contains("arenas")) {
    	Set<String> arenas = Main.arenas.getConfig().getConfigurationSection("arenas").getKeys(false);
    	
    		for (String b : arenas) {		
    			try {
    			
        			Location arena1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena1"), false);
        			Location arena2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena2"), false);
        			Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".lobby"), false);	
    				String stype = Main.arenas.getConfig().getString("arenas."+b+".spleeftype");
    				String gtype = Main.arenas.getConfig().getString("arenas."+b+".gametype");
    				stype = stype.toUpperCase();	
    				gtype = gtype.toUpperCase();	
    				SpleefType spleeftype = SpleefType.valueOf(stype);
    				GameType gametype = GameType.valueOf(gtype);
    				int min = Main.arenas.getConfig().getInt("arenas."+b+".min_size");
					int max = Main.arenas.getConfig().getInt("arenas."+b+".max_size");
    				if (gametype.equals(GameType.FFA)) {
				
    				Location mainspawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".mainspawn"), false);
    				DataManager.getManager().loadArena(b,mainspawn,null,null,lobby,arena1,arena2,spleeftype,gametype,null,min,max,null,null);
    				} else if (gametype.equals(GameType.DUEL)) {
    					Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn1"), true);
            			Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn2"), true);
            			
            			String it = null;
            			 it = Main.arenas.getConfig().getString("arenas."+b+".item");
         				it = it.toUpperCase();
         				Material item = Material.valueOf(it);
            			DataManager.getManager().loadArena(b,null,spawn1,spawn2,lobby,arena1,arena2,spleeftype,gametype,item,min,max,null,null);
    				}else if (gametype.equals(GameType.CATF)) {
    					Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn1"), true);
            			Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn2"), true);
            			Location flag1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn1"), true);
            			Location flag2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn2"), true);
            			String it = null;
            			 it = Main.arenas.getConfig().getString("arenas."+b+".item");
         				it = it.toUpperCase();
         				Material item = Material.valueOf(it);
            			DataManager.getManager().loadArena(b,null,spawn1,spawn2,lobby,arena1,arena2,spleeftype,gametype,item,min,max,flag1,flag2);
    				}
				arenasint++;
			
    			} catch (Exception e) {
    				Main.get().getLogger().info(b);
    				e.printStackTrace();
    			}
    		}
    		Main.get().getLogger().info(arenasint+ " arenas cargadas!");
    		loadFFAArenaNoRotate(SpleefType.SPLEEF,0,null);
    		
    		loadFFAArenasRotate(SpleefType.SPLEGG,-1);
    		loadFFAArenasRotate(SpleefType.TNTRUN,-1);
    	}
    	}
    	}.runTaskAsynchronously(Main.get());
    		
    		
    		
    		
   
    }
     
    
    
    //State 0 = Plugin being loaded, smallest FFA Arena
    //State 1 = Going from small to bigger arena, so arena1 max = arena2 min
    //State 2 = Going from big to smaller arena, so arena2 min = arena1 max
    public Arena loadFFAArenaNoRotate(SpleefType type,int state, Arena arena1) {
    	Arena ffa = null;
    	boolean condition = false;
    	for (Arena arena2 : getArenas()) {
    		if (arena1!=null) if (arena1==arena2) continue;
    		if (arena2.getGameType().equals(GameType.FFA) && arena2.getSpleefType().equals(type)) {
    			if (ffa==null) {
        			ffa = arena2;
        		} else {
    	switch(state) {
    	case 0: condition = arena2.getMinPlayersSize()==0; break;
    	case 1: condition = (arena1.getMaxPlayersSize() == arena2.getMinPlayersSize()) && (arena1.getMaxPlayersSize()< arena2.getMaxPlayersSize()); break;
    	case 2: condition = (arena1.getMaxPlayersSize() == arena2.getMinPlayersSize()) && (arena1.getMaxPlayersSize() > arena2.getMaxPlayersSize()); break;
    	}
    	
    	if (condition) ffa = arena2; break;
    		
        		}
    				}
    		}
    	
    	GameManager.getManager().setFFAArena(ffa,SpleefType.SPLEEF);
    	return ffa;
    }
    
    
    
    public void loadFFAArenasRotate(SpleefType type, int playing) {
    	this.getFFAArenas(type).clear();
    	List<Arena> arenas = getArenas();
		Collections.shuffle(arenas); 	
		for (Arena arena : arenas) {
		boolean condition = playing == -1 ? true : arena.getMinPlayersSize()<=playing && arena.getMaxPlayersSize()>=playing;
    		if (arena.getGameType().equals(GameType.FFA)&& arena.getSpleefType().equals(type) && condition) {
    			this.getFFAArenas(type).add(arena);
    		}
    	}
    	
    	
    	if (playing==-1) {
    		GameManager.getManager().setFFAArena(this.getFFAArenas(type).get(0), type);
     }
    }
    
	public void resetMonthlyStats() {
    	HikariAPI.getManager().resetMonthly();
    }


    
	public void resetWeeklyStats() {
    	HikariAPI.getManager().resetWeekly();
    }
    
    
    /**
	* Returns an Array of the items that can be used in game.
    */
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
		
		ItemStack redflag = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
		LeatherArmorMeta redmeta =(LeatherArmorMeta) redflag.getItemMeta();
		redmeta.setColor(Color.RED);
		redflag.setItemMeta(redmeta);
		ItemStack blueflag = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
		LeatherArmorMeta bluemeta =(LeatherArmorMeta) blueflag.getItemMeta();
		bluemeta.setColor(Color.BLUE);
		blueflag.setItemMeta(bluemeta);
		ItemStack[] items = {iron_shovel, diamond_shovel, x2snowball,x4snowball,x6snowball,x8snowball,x10snowball,blueflag,redflag,golden_shovel};
		return items;
	}
    
    
    	/**
    	 * Returns an Array of the items that can be used in lobby.
    	 */
	public ItemStack[] lobbyitems() {
		
		ItemStack gadgets = new ItemBuilder(Material.CHEST).setTitle("§d§lCosmetics").build();
		ItemStack parkour = new ItemBuilder(Material.FEATHER).setTitle("§b§lParkour").build();
		ItemStack options = new ItemBuilder(Material.REDSTONE_COMPARATOR).setTitle("§c§lOptions").build();
		ItemStack ranked = new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§6§lRanked").build();
		ItemStack unranked = new ItemBuilder(Material.IRON_SPADE).setTitle("§b§lUnranked").build();
		ItemStack guilds = new ItemBuilder(Material.TOTEM).setTitle("§e§lGuilds").build();
		ItemStack[] items = {gadgets,parkour,options,ranked, unranked,guilds};
		
		return items;
	}
	
		/**
		* Returns an Array of the items that can be used in queue.
	    */
	public ItemStack[] queueitems() {
		ItemStack powerups = new ItemBuilder(Material.ENDER_CHEST).setTitle("§d§lMutations").build();
		ItemStack leave = new ItemBuilder(Material.REDSTONE_TORCH_ON).setTitle("§c§lLeave").build();
		ItemStack[] items = {powerups, leave};
		return items;
	}
	
	/**
	* Returns an Array of the items that can be used in spectator mode.
    */
public ItemStack[] spectateitems() {
	ItemStack showhide = new ItemBuilder(Material.WATCH).setTitle("§eShow/Hide Spectators").build();
	ItemStack[] items = {showhide};
	return items;
}
	
	
	 	/**
		* returns the possible language of the player based on their country code, got from the IP that the player connected. 
		* @param s - 2 Digits Code of the country the player is from.
	    */
	public Language languageFromCountry(String s) {
		try {
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
		} catch(Exception e) {
			return Language.ENGLISH;
		}
	}

	 	/**
		* Called everyday at 00:00 of the Server time. Gives the mutation tokens to all players who are able to get daily mutations. Runs in a Asynchronous Task for better perfomance
	    */
	public void giveMutationTokens() {
		HikariAPI.getManager().giveMutations();
		
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			if (sp.getOfflinePlayer().isOnline()) {
				PermissionUser s = PermissionsEx.getUser(sp.getPlayer());
		int mutations = s.has("splindux.extreme") ? 5 : s.has("splindux.epic") ? 3 : s.has("splindux.vip") ? 1 : 0;
		sp.setMutationTokens(sp.getMutationTokens()+mutations);
			}
		}
	}
	
	
		/**
		 * Called everyday at 00:00 of the Server time. Resets ranked for all players. Runs in a Asynchronous Task for better perfomance
		 */
	public void giveRankeds() {
		HikariAPI.getManager().giveRankeds();
		
		
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			if (sp.getOfflinePlayer().isOnline()) {
				PermissionUser s = PermissionsEx.getUser(sp.getPlayer());
		int rankeds = s.has("splindux.extreme") ? 25 : s.has("splindux.epic") ? 20 : s.has("splindux.vip") ? 15 : 10;
		sp.setRankeds(rankeds);
			}
		}
	}
	
	
	public void resetELO() {
	HikariAPI.getManager().resetELO();
	}
	
	
	public void saveData() {
		for (SpleefPlayer sp : getPlayers()) {
			HikariAPI.getManager().saveData(sp);
		}
	}
	
	
	public String getCountryString(SpleefPlayer sp) {
		if (sp.getCountry()==null) {
			sp.setCountry(URLUtils.getURLUtils().getCountry(sp.getIP()));
		}
		
		return getCountryString(sp.getCountry());
	}
	
	public String getCountryString(String country) {
		if (country==null) return null;
		switch(country) {
		case "AR": return  "§b§lArg§f§lent§b§lina";
		case "MX": return "§2§lMe§f§lxi§c§lco";
		case "CL": return "§4§lCh§f§lil§9§le";
		case "ES": return "§c§lEs§e§lpa§c§lña";
		case "PE": return "§4§lP§f§ler§4§lu";
		case "CO": return "§6§lCol§9§lomb§c§lia";
		case "UY": return "§f§lUru§b§lg§e§lu§f§lay";
		case "US": return "§9§lUn§4§lit§f§led §9§lSt§4§lat§f§les";
		case "CA": return "§c§lCa§f§lna§c§lda";
		case "GB": return "§9§lUnited §4§lKingdom";
		case "PL":return "§f§lPol§c§land";
		case "AU": return "§9§lAus§4§lt§f§lr§9§lalia";
		case "SE": return "§1§lSw§e§led§1§len";
		case "NL": return "§c§lNet§f§lher§9§llands";
		case "RU": return "§f§lRu§1§lss§c§lia";
		case "KZ": return "§b§lKaza§6§lkh§b§lstan";
		case "PR": return "§c§lPu§9§ler§f§lto §c§lRi§f§lco";
		case "GR": return "§9§lG§f§lr§9§le§f§le§9§lc§f§le";
		case "UA": return "§9§lUkr§6§laine";
		case "BY": return "§4§lBel§7§la§2§lrus";
		case "CH": return "§4§lSwitz§f§ler§4§lland";
		case "EC": return "§6§lEcu§9§lad§c§lor";
		case "BR": return "§2§lBr§e§laz§2§lil";
		case "HK": return "§c§lHong §f§lKong"; 
		}
		return null;
	}
	
	
	public String getMessage(SpleefPlayer sp, String path) {
		return Main.lang.getConfig().getString(sp.getOptions().getLanguage().toString().toLowerCase()+"."+path);
	}


	public void broadcast(String bc,boolean bukkitBroadcast) {
		bc = ChatColor.translateAlternateColorCodes('§', bc);		
		final String bcc = bc;
		bc = ChatColor.stripColor(bc);
		final String bccc = bc;
		new BukkitRunnable() {
			public void run () {
					if (bukkitBroadcast) Bukkit.broadcastMessage(bcc);
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #splindux-chat :splindux: **" + bccc + "** :splindux:");
	}
		}.runTask(Main.get());
	}
	
	
	public void joinServer(SpleefPlayer sp) {
		sp.setIP(sp.getPlayer().getAddress().getAddress().toString().replace("/", ""));
		sp.setLastLogin(new Date());
		sp.applyParticles();
	}


	public void unloadOfflinePlayers() {
		List<SpleefPlayer> toRemove = new ArrayList<SpleefPlayer>();
		new BukkitRunnable() {
			@Override
			public void run() {
				
				for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
					if (!sp.getOfflinePlayer().isOnline()) {
						HikariAPI.getManager().saveData(sp);
						toRemove.add(sp);
					}
				}
				
				DataManager.getManager().getPlayers().removeAll(toRemove);
			
			}
 		}.runTaskAsynchronously(Main.get());
		
	}


	public void removeDuplicatedPlayers() {
		List<SpleefPlayer> toRemove = new ArrayList<SpleefPlayer>();
		new BukkitRunnable() {
			@Override
			public void run() {
				
				for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
					if (!sp.getOfflinePlayer().isOnline()) {
						HikariAPI.getManager().saveData(sp);
						toRemove.add(sp);
					}
				}
				
				DataManager.getManager().getPlayers().removeAll(toRemove);
			
			}
 		}.runTaskAsynchronously(Main.get());
		
		
	}
	
	
	public String getLastConnection (SpleefPlayer sp) {
		if (sp.getLastLogin()==null) return " unknown";
		Date date = sp.getLastLogin();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);		
		Date ahora = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(ahora);
		
		int year = now.get(Calendar.YEAR)-calendar.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH)-calendar.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH);
		int hours = now.get(Calendar.HOUR_OF_DAY)-calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = now.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE);
		
		if (year==0) {
			if (month==0) {
				if (day==0) {
					if (hours==0) {
						if (minutes==0) {
							return "a few seconds ago.";
						} else {
							String t = minutes == 1 ? "minute ago." : "minutes ago.";
							return minutes + t;
						}
					} else {
						String t = hours == 1 ? "hour ago." : "hours ago.";
						return hours + t;
					}
				} else {
					String t = day == 1 ? "day ago." : "days ago.";
					return day + t;
				}
			} else {
				String t = month == 1 ? "month ago." : "months ago.";
				return month + t;
			}
		} else {
			String t = year == 1 ? "year ago." : "years ago.";
			return year +" "+ t;
		}
	  }
	
	public int getTimeLastConnection(SpleefPlayer sp) {
		if (sp.getLastLogin()==null) return 0;
		Date date = sp.getLastLogin();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);		
		Date ahora = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(ahora);
		
		int year = now.get(Calendar.YEAR)-calendar.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH)-calendar.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH);
		int hours = now.get(Calendar.HOUR_OF_DAY)-calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = now.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE);
		
		return (year*12*30*24*60)+(month*30*24*60)+(day*24*60)+(hours*60)+minutes;
	}
	
	
	
	public String getDateDifference(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);		
		Date ahora = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(ahora);
		
		int y = now.get(Calendar.YEAR)-calendar.get(Calendar.YEAR);
		int mo = now.get(Calendar.MONTH)-calendar.get(Calendar.MONTH);
		int d = now.get(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH);
		
		String years = y>0 ? y +" years" : "";
		String months = mo>0 ? mo +" months" : "";
		String days = d>0 ? d +" days" : "";
		
		String diff = "";
		
		if (!years.equalsIgnoreCase("") || !months.equalsIgnoreCase("") || !days.equalsIgnoreCase("")) {
			diff = years+" " + months+ " " + days;
					} else {
						diff = "Today";
					}
		

		
		
		
		return diff;
	}
	}
