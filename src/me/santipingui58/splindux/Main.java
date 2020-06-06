package me.santipingui58.splindux;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.yapzhenyie.GadgetsMenu.economy.GEconomyProvider;

import me.santipingui58.splindux.anouncements.AnnouncementManager;
import me.santipingui58.splindux.commands.AFKCommand;
import me.santipingui58.splindux.commands.AdCommand;
import me.santipingui58.splindux.commands.AdminCommand;
import me.santipingui58.splindux.commands.CoinsCommand;
import me.santipingui58.splindux.commands.CrumbleCommand;
import me.santipingui58.splindux.commands.DuelCommand;
import me.santipingui58.splindux.commands.EndGameCommand;
import me.santipingui58.splindux.commands.FFAEventCommand;
import me.santipingui58.splindux.commands.FlyCommand;
import me.santipingui58.splindux.commands.ForceResetCommand;
import me.santipingui58.splindux.commands.HeadCommand;
import me.santipingui58.splindux.commands.HelpCommand;
import me.santipingui58.splindux.commands.HologramCommand;
import me.santipingui58.splindux.commands.HoverCommand;
import me.santipingui58.splindux.commands.LevelCommand;
import me.santipingui58.splindux.commands.MatchesCommand;
import me.santipingui58.splindux.commands.MsgCommand;
import me.santipingui58.splindux.commands.MutationTokenCommand;
import me.santipingui58.splindux.commands.PingCommand;
import me.santipingui58.splindux.commands.PlaytoCommand;
import me.santipingui58.splindux.commands.RankCommand;
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
import me.santipingui58.splindux.commands.TranslateCommand;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.listener.CustomPacketListener;
import me.santipingui58.splindux.listener.MutationListener;
import me.santipingui58.splindux.listener.NPCListener;
import me.santipingui58.splindux.listener.PlayerChat;
import me.santipingui58.splindux.listener.PlayerConnectListener;
import me.santipingui58.splindux.listener.PlayerListener;
import me.santipingui58.splindux.listener.ServerListener;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.particles.ParticleManager;
import me.santipingui58.splindux.petshop.PetShopManager;
import me.santipingui58.splindux.placeholdersapi.SplinduxExpansion;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.task.TaskManager;
import me.santipingui58.splindux.timelimit.TimeLimitManager;
import me.santipingui58.splindux.utils.Configuration;
import me.santipingui58.splindux.utils.Utils;
import net.milkbowl.vault.economy.Economy;


//Agregar 3 arenas por mapa  2.2.1.0

//Implement stuff from Store 2.5.0.0
//Ranked
//Optiones menu NIGHT VISION, ADS, DEFAULT COLOR IN CHAT, MAP RANKED
//Spectator  
//Fishing

//Votar NameMC 2.6.0
//Join Discord
//Votifier
//Discord reward for invite 
//Twitter Youtube 

//Youtubers & Streamers, foros Amino Reddit Facebook Twitter 
//Staffs

//In game helmets  2.7.0.0


//Nuevo Lobby 2.8.0.0
//Quests
//Interactive Lobby

//Friends 2.9.0.0
//LootBoxes


//Test Arena 2.10.0.0
//Spleef KotH
//Spleef Mobs

//Parkour
//Replay



//Anticheat
//BowSpleef
//guilds
//TNT Run
//

public class Main extends JavaPlugin {

	//Instance of the plugin
	public static Plugin pl;
	
	//.yml files	
	public static Configuration config,messages,arenas,data,recordings,timelimit,petblocks,announcements,petshop;
	
	//Spawn point of the server
	public static Location lobby;
	
	//If the PvP is enabled or not
	public static boolean pvp;
	
	//Handles Vault Economy integration
	public static Economy econ = null;
	
	
		/**
	    * @return Returns Main plugin instance.
	    */
	public static Plugin get() {
	    return pl;
	  }	
	
	
	
	@Override
	public void onEnable() {
		pl = this;
		
		
		//PlaceholdersAPI expansions to use commands on DiscordSRV
		 if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
             new SplinduxExpansion(this).register();
       }

		//API for GadgetsMenu
		GEconomyProvider.setMysteryDustStorage(new EconomyManager(this, "Splindux"));	
		
		//Creation of .yml files
		config = new Configuration("config.yml",this);
		data = new Configuration("data.yml",this);
		arenas = new Configuration("arenas.yml",this);	
		recordings = new Configuration("recordings.yml",this);	
		timelimit = new Configuration("timelimit.yml",this);
		petblocks = new Configuration("petblocks.yml",this);
		announcements = new Configuration("announcements.yml",this);
		petshop = new Configuration("petshop.yml",this);
		
		
		//Loading of Particles, Holograms, Timelimits, Announcements, pets, players, tasks and queues
		ParticleManager.getManager().loadEffectsAndTypes();	
		HologramManager.getManager().loadHolograms();
		TimeLimitManager.getManager().loadTimeLimit();
		AnnouncementManager.getManager().loadAnnouncements();
		PetShopManager.getManager().loadPets();	
		DataManager.getManager().loadPlayers(false);
		RankedManager.getManager().loadRankedQueues();
		TaskManager.getManager().task();

		//Default spawn of the Server
		lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
		
		//Teleports all players that were in the arena's world, in case the server was restarted while games were being played.
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getWorld().getName().equalsIgnoreCase("arenas")) {
					p.teleport(lobby);
					}
				}
				
		setupEconomy();
		
	
		//Register events and commands
		registerEvents();
		registerCommands();
		
		
		//Load worlds
		new BukkitRunnable() {
			public void run() {
				new WorldCreator("arenas").createWorld();
				new WorldCreator("lobby").createWorld();
				DataManager.getManager().loadParticles();
				//new WorldCreator("construccion").createWorld();	
			}	
		}.runTaskLater(this, 20L);
		
		//Load arenas
		new BukkitRunnable() {
		public void run() {
			DataManager.getManager().loadArenas();
		}
	}.runTaskLater(this, 20L);
	
		
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
	
	
	

	}

	@Override
	public void onDisable() {	
		//Save Data
		DataManager.getManager().savePlayers();	
		NPCManager.getManager().removeNPCs();
		PetShopManager.getManager().savePets();
		TimeLimitManager.getManager().saveTimeLimit();
		HologramManager.getManager().saveHolograms();
	 
		for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
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
		
	}
	
	

	

	
}
