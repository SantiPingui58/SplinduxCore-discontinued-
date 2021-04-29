package me.santipingui58.splindux;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.isach.ultracosmetics.UltraCosmetics;
import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.anouncements.AnnouncementManager;
import me.santipingui58.splindux.commands.AFKCommand;
import me.santipingui58.splindux.commands.AdCommand;
import me.santipingui58.splindux.commands.AdminCommand;
import me.santipingui58.splindux.commands.BetCommand;
import me.santipingui58.splindux.commands.CoinsCommand;
import me.santipingui58.splindux.commands.CrumbleCommand;
import me.santipingui58.splindux.commands.DuelCommand;
import me.santipingui58.splindux.commands.EndGameCommand;
import me.santipingui58.splindux.commands.FFAEventCommand;
import me.santipingui58.splindux.commands.FlyCommand;
import me.santipingui58.splindux.commands.ForceFinishCommand;
import me.santipingui58.splindux.commands.ForceResetCommand;
import me.santipingui58.splindux.commands.ForceShrinkCommand;
import me.santipingui58.splindux.commands.FriendsCommand;
import me.santipingui58.splindux.commands.GuildChatCommand;
import me.santipingui58.splindux.commands.GuildCommand;
import me.santipingui58.splindux.commands.HeadCommand;
import me.santipingui58.splindux.commands.HelpCommand;
import me.santipingui58.splindux.commands.HologramCommand;
import me.santipingui58.splindux.commands.HoverCommand;
import me.santipingui58.splindux.commands.LevelCommand;
import me.santipingui58.splindux.commands.MatchesCommand;
import me.santipingui58.splindux.commands.MsgCommand;
import me.santipingui58.splindux.commands.MutationTokenCommand;
import me.santipingui58.splindux.commands.NightVisionCommand;
import me.santipingui58.splindux.commands.ParkourSetupCommand;
import me.santipingui58.splindux.commands.PingCommand;
import me.santipingui58.splindux.commands.PlaytoCommand;
import me.santipingui58.splindux.commands.RankCommand;
import me.santipingui58.splindux.commands.RankingCommand;
import me.santipingui58.splindux.commands.ResetCommand;
import me.santipingui58.splindux.commands.RideCommand;
import me.santipingui58.splindux.commands.SetupCommand;
import me.santipingui58.splindux.commands.SpawnCommand;
import me.santipingui58.splindux.commands.SpectateCommand;
import me.santipingui58.splindux.commands.SplinduxLoginCommand;
import me.santipingui58.splindux.commands.SplinduxRegisterCommand;
import me.santipingui58.splindux.commands.StaffChatCommand;
import me.santipingui58.splindux.commands.StaffCommand;
import me.santipingui58.splindux.commands.StatsCommand;
import me.santipingui58.splindux.commands.TipCommand;
import me.santipingui58.splindux.commands.TournamentCommand;
import me.santipingui58.splindux.commands.TranslateCommand;
import me.santipingui58.splindux.cosmetics.helmets.HelmetManager;
import me.santipingui58.splindux.cosmetics.particles.ParticleManager;
import me.santipingui58.splindux.cosmetics.petshop.PetShopManager;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.listener.CustomPacketListener;
import me.santipingui58.splindux.listener.MutationListener;
import me.santipingui58.splindux.listener.NPCListener;
import me.santipingui58.splindux.listener.PlayerChat;
import me.santipingui58.splindux.listener.PlayerConnectListener;
import me.santipingui58.splindux.listener.PlayerListener;
import me.santipingui58.splindux.listener.ServerListener;
import me.santipingui58.splindux.listener.VotifierListener;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.placeholdersapi.SplinduxExpansion;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.ranking.RankingManager;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.task.TaskManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.utils.Configuration;
import me.santipingui58.splindux.utils.TeleportFix;
import me.santipingui58.splindux.utils.Utils;
import net.milkbowl.vault.economy.Economy;




//Querido programador:
//Cuando escribí este código, solo dios y
//yo sabíamos como funcionaba
//Ahora, solo dios lo sabe!
//
//Igualmente, si estás intentando optimizar
//este programa y falla (lo más probable)
//por favor aumenta este contador como una
//advertencia para la próxima persona:
//
//horas_totales_gastadas_aqui = 254

//Party
//Update minispleef
//Spleeg and TNTRun stats




//Nuevo Lobby  2.9.0
//Quests
//Achievements
//Interactive Lobby
//Fishing 
//LootBoxes
//TNTRun & Spleeg Ranked



//Anticheat
//BowSpleef, TNT Run, EXP Spleef, TNT Spleef



//Test Arena 2.10.0.0
//Spleef KotH
//Spleef Mobs
//Spleef CATF y COTF
//Replay 



public class Main extends JavaPlugin implements Listener {

	//Instance of the plugin
	public static Plugin pl;
	
	//.yml files	
	public static Configuration config,arenas,recordings,timelimit,petblocks,announcements,petshop,ranking,tournaments,jumps,lang,helmets,guilds;
	
	//Spawn point of the server
	public static Location lobby;
	
	//If the PvP is enabled or not
	public static boolean pvp;
	

	public static boolean scoreboardUpdate;
	
	//Handles Vault Economy integration
	public static Economy econ = null;
	
	public static UltraCosmetics ultraCosmetics = null;
		/**
	    * @return Returns Main plugin instance.
	    */
	public static Plugin get() {
	    return pl;
	  }	
	
	private static boolean saveOnDisable;
	
	
	@Override
	public void onEnable() {
		pl = this;
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman reload SplinduxHikariAPI");
		//PlaceholdersAPI expansions to use commands on DiscordSRV
		 if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
             new SplinduxExpansion(this).register();
       }

		 Bukkit.getOnlinePlayers().forEach((p) ->  p.setFlying(false));
			 new BukkitRunnable() {
				 public void run() {
					 for (Player p : Bukkit.getOnlinePlayers()) {
						
						 new SpleefPlayer(p.getUniqueId());
					 HikariAPI.getManager().loadData(p.getUniqueId());
					 HikariAPI.getManager().loadFriends(p.getUniqueId());
						new BukkitRunnable() {
							public void run() {
								SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(p);
								splayer.giveLobbyItems();								
							}
						}.runTaskLater(Main.get(), 10L);
				 }
			 }
		 }.runTaskAsynchronously(Main.get());
		 
		 
		 DataManager.getManager().unloadOfflinePlayers();
		
		//Creation of .yml files
		config = new Configuration("config.yml",this);
		arenas = new Configuration("arenas.yml",this);	
		recordings = new Configuration("recordings.yml",this);	
		timelimit = new Configuration("timelimit.yml",this);
		petblocks = new Configuration("petblocks.yml",this);
		announcements = new Configuration("announcements.yml",this);
		petshop = new Configuration("petshop.yml",this);
		ranking = new Configuration("ranking.yml",this);
		tournaments = new Configuration("tournaments.yml",this);
		jumps = new Configuration("jumps.yml",this);
		helmets = new Configuration("helmets.yml",this);
		guilds = new Configuration("guilds.yml",this);
		DataManager.getManager().queues(false);
		
		//Loading of Particles, Holograms, Timelimits, Announcements, pets, players, tasks and queues
		
		new BukkitRunnable() {
			public void run() {
		ParticleManager.getManager().loadEffectsAndTypes();	
		AnnouncementManager.getManager().loadAnnouncements();
		PetShopManager.getManager().loadPets();	
		RankedManager.getManager().loadRankedQueues();
		HelmetManager.getManager().loadHelmets();
		GuildsManager.getManager().loadGuilds();
		}
		}.runTaskAsynchronously(Main.get());
		
		TaskManager.getManager().task();
		//Default spawn of the Server
		lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
		
		//Teleports all players that were in the arena's world, in case the server was restarted while games were being played.
				for (Player p : Bukkit.getOnlinePlayers()) {
					String world = p.getWorld().getName();
					if (world.equalsIgnoreCase("arenas") || world.equalsIgnoreCase("parkour")) {
					p.teleport(lobby);
					p.getInventory().clear();
					
					new BukkitRunnable() {
						public void run() {
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
							sp.giveLobbyItems();
							sp.updateScoreboard();
							saveOnDisable =true;
						}
						}.runTaskLater(Main.get(), 10L);
					}
				}
				
				
		setupEconomy();
		setupCosmetics();
		
		ParkourManager gm = ParkourManager.getManager();
		gm.loadJumps();
		gm.loadLevels();
		gm.loadProbabilities();
		StatsManager.getManager().loadParkourLevels();

		//Register events and commands
		registerEvents();
		registerCommands();
		
		
		//Load worlds
		new BukkitRunnable() {
			public void run() {
				new WorldCreator("arenas").createWorld();
				new WorldCreator("lobby").createWorld();
				new WorldCreator("parkour").createWorld();
				DataManager.getManager().loadArenas();
				//new WorldCreator("construccion").createWorld();	
			}	
		}.runTaskLater(this, 20L);
		
		//Load arenas
		new BukkitRunnable() {
		public void run() {
		
			TimeLimitManager.getManager().loadTimeLimit();
			HologramManager.getManager().loadHolograms();
			RankingManager.getManager().loadRanking();
		}
	}.runTaskLaterAsynchronously(this, 20L);
	
		
	new BukkitRunnable() {
		public void run() {
			HologramManager.getManager().updateHolograms(false);
			//Load NPCs
			NPCManager.getManager().loadNPCs();

		}
		
	}.runTaskLater(this, 60L);
	
	
		 
	
	/*
	protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {

	
        @Override
        public void onPacketSending(PacketEvent event) {
           
        	if (event.getPacketType() == PacketType.Play.Server.CHAT) {
        		 try {
             	PacketContainer packet = event.getPacket();
             	 if (packet.getChatComponents() == null) {
                      return; // skip that message
                   }
             	 
             	 //TextComponent text = new TextComponent();
             	// List<WrappedChatComponent> components = packet.getChatComponents().getValues();           	 
             	 WrappedChatComponent msg = packet.getChatComponents().read(0);
                 String message = msg.getJson();
                 BaseComponent[] base = ComponentSerializer.parse(message);               
                
                 String bs = TranslateAPI.getAPI().translate(""+new TextComponent(base), Language.ENGLISH, Language.SPANISH);  
                 Player p = Bukkit.getPlayer("SantiPingui58");    
                 p.sendMessage(bs);
            	 
                }catch(Exception e) {}
        	
        	
        

    });  
	}
	

	 }
            	PacketContainer packet = event.getPacket();
                StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();
                WrappedChatComponent msg = chatComponents.read(0);
                String message = msg.getJson();
                
                BaseComponent[] base = ComponentSerializer.parse(message);
               
                Player p = Bukkit.getPlayer("SantiPingui58");    
                String s =  new TextComponent(base).getText();            
                base = TranslateAPI.getAPI().translate(s, Language.SPANISH, Language.ENGLISH);              
                p.spigot().sendMessage(base);
               
               } catch(Exception e) {}
	 */
	
	
    getServer().getPluginManager().registerEvents(this, this);

	}
	

	@Override
	public void onDisable() {	
		//Save Data
	
		if (saveOnDisable) DataManager.getManager().saveData();
		
		GuildsManager.getManager().saveGuilds();
		NPCManager.getManager().removeNPCs();
		PetShopManager.getManager().savePets();
		TimeLimitManager.getManager().saveTimeLimit();
		HologramManager.getManager().saveHolograms();
	 
		ParkourManager.getManager().saveArenas();
		
		
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			 HologramManager.getManager().removeHolograms(sp);
		}			
		
		
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);
		getServer().getPluginManager().registerEvents(new ServerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerChat(), this);
		getServer().getPluginManager().registerEvents(new NPCListener(), this);
		getServer().getPluginManager().registerEvents(new MutationListener(), this);
		getServer().getPluginManager().registerEvents(new VotifierListener(), this);
		getServer().getPluginManager().registerEvents(new TeleportFix(this),this);
		new CustomPacketListener();
		
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        
        
        econ = rsp.getProvider();
        return econ != null;
          
    }
	
	private boolean setupCosmetics() {
        if (getServer().getPluginManager().getPlugin("UltraCosmetics") == null) {
            return false;
        }
        ultraCosmetics = (UltraCosmetics) Bukkit.getServer().getPluginManager().getPlugin("UltraCosmetics");
        return ultraCosmetics != null;
          
    }
	
	
	private void registerCommands() {
		getCommand("setup").setExecutor(new SetupCommand());
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("rank").setExecutor(new RankCommand());
		getCommand("msg").setExecutor(new MsgCommand());
		getCommand("r").setExecutor(new MsgCommand());
		getCommand("afk").setExecutor(new AFKCommand());
		getCommand("admin").setExecutor(new AdminCommand());
		getCommand("reset").setExecutor(new ResetCommand());
		getCommand("hover").setExecutor(new HoverCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("duel").setExecutor(new DuelCommand());
		getCommand("spectate").setExecutor(new SpectateCommand());
		getCommand("ride").setExecutor(new RideCommand());
		getCommand("endgame").setExecutor(new EndGameCommand());
		getCommand("splinduxregister").setExecutor(new SplinduxRegisterCommand());
		getCommand("splinduxlogin").setExecutor(new SplinduxLoginCommand());
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("matches").setExecutor(new MatchesCommand());
		getCommand("hologram").setExecutor(new HologramCommand());
		getCommand("level").setExecutor(new LevelCommand());
		getCommand("playto").setExecutor(new PlaytoCommand());
		getCommand("crumble").setExecutor(new CrumbleCommand());
		getCommand("translate").setExecutor(new TranslateCommand());
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("ffaevent").setExecutor(new FFAEventCommand());
		getCommand("staffchat").setExecutor(new StaffChatCommand());
		getCommand("mutationtoken").setExecutor(new MutationTokenCommand());
		getCommand("forcereset").setExecutor(new ForceResetCommand());
		getCommand("coins").setExecutor(new CoinsCommand());
		getCommand("head").setExecutor(new HeadCommand());
		getCommand("ad").setExecutor(new AdCommand());
		getCommand("staff").setExecutor(new StaffCommand());
		getCommand("tournament").setExecutor(new TournamentCommand());
		getCommand("tip").setExecutor(new TipCommand());
		getCommand("ranking").setExecutor(new RankingCommand());
		getCommand("parkoursetup").setExecutor(new ParkourSetupCommand());
		getCommand("forcefinish").setExecutor(new ForceFinishCommand());
		getCommand("bet").setExecutor(new BetCommand());
		getCommand("nightvision").setExecutor(new NightVisionCommand());
		getCommand("guild").setExecutor(new GuildCommand());
		getCommand("guildchat").setExecutor(new GuildChatCommand());
		getCommand("friends").setExecutor(new FriendsCommand());
		getCommand("forceshrink").setExecutor(new ForceShrinkCommand());
	}	
}
