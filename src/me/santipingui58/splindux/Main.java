package me.santipingui58.splindux;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.yapzhenyie.GadgetsMenu.economy.GEconomyProvider;

import me.santipingui58.splindux.commands.AFKCommand;
import me.santipingui58.splindux.commands.AdminCommand;
import me.santipingui58.splindux.commands.CrumbleCommand;
import me.santipingui58.splindux.commands.DuelCommand;
import me.santipingui58.splindux.commands.EndGameCommand;
import me.santipingui58.splindux.commands.FFAEventCommand;
import me.santipingui58.splindux.commands.FlyCommand;
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
import me.santipingui58.splindux.commands.ReinoCommand;
import me.santipingui58.splindux.commands.ResetCommand;
import me.santipingui58.splindux.commands.RideCommand;
import me.santipingui58.splindux.commands.SetupCommand;
import me.santipingui58.splindux.commands.SpawnCommand;
import me.santipingui58.splindux.commands.SpectateCommand;
import me.santipingui58.splindux.commands.SplinduxLoginCommand;
import me.santipingui58.splindux.commands.SplinduxRegisterCommand;
import me.santipingui58.splindux.commands.StaffChatCommand;
import me.santipingui58.splindux.commands.StatsCommand;
import me.santipingui58.splindux.commands.TranslateCommand;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.listener.CustomPacketListener;
import me.santipingui58.splindux.listener.NPCListener;
import me.santipingui58.splindux.listener.PlayerChat;
import me.santipingui58.splindux.listener.PlayerConnectListener;
import me.santipingui58.splindux.listener.PlayerListener;
import me.santipingui58.splindux.listener.ServerListener;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.placeholdersapi.SplinduxExpansion;
import me.santipingui58.splindux.reino.ReinoManager;
import me.santipingui58.splindux.scoreboard.hologram.HologramManager;
import me.santipingui58.splindux.task.ArenaTimeTask;
import me.santipingui58.splindux.task.OnMoveTask;
import me.santipingui58.splindux.task.OnlineTimeTask;
import me.santipingui58.splindux.task.ParkourTimerTask;
import me.santipingui58.splindux.task.ScoreboardTask;
import me.santipingui58.splindux.task.SortRankingTask;
import me.santipingui58.splindux.task.TabTask;
import me.santipingui58.splindux.utils.Configuration;
import me.santipingui58.splindux.utils.Utils;


//Agregar 3 arenas por mapa  2.2.1.0

//Mutations 2.3.0.0
//FFA Event


//Implement stuff from Store 2.4.0.0
//Delayed messages on login
//Announcements & Ads
//Optiones menu NIGHT VISION, ADS, DEFAULT COLOR IN CHAT

//Votar NameMC 2.5.0.0
//Join Discord
//Votifier
//Discord reward for invite 
//Twitter Youtube 

//In game helmets && particles 2.6.0.0
//Youtubers & Streamers, foros Amino Reddit Facebook Twitter 
//Staffs

//Nuevo Lobby 2.7.0.0
//Quests
//Interactive Lobby

//Friends 2.8.0.0
//LootBoxes
//Ranked 2.7.0.0


//Test Arena 2.9.0.0
//Spleef KotH
//Spleef Mobs

//Parkour
//Replay



//Anticheat
//BowSpleef
//guilds


public class Main extends JavaPlugin {

	
	//Static variables used around the plugin
	public static Plugin pl;
	public static String prefix;
	public static boolean prefix_enabled;
	public static Configuration config,messages,arenas,data,recordings;
	public static Location lobby;
	public static boolean pvp;
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
		 
		//A bug causes player to corrupt when the plugin reloads, to prevent this, the players get kicked when the plugin starts.
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer("§cSplinduxCore restarting, please re join!");
		}
		
		
		//API for GadgetsMenu
		GEconomyProvider.setMysteryDustStorage(new EconomyManager(this, "Splindux"));	
		
		//Creation of .yml files
		config = new Configuration("config.yml",this);
		data = new Configuration("data.yml",this);
		arenas = new Configuration("arenas.yml",this);	
		recordings = new Configuration("recordings.yml",this);	
		
		//Load of players and holograms data
		DataManager.getManager().loadPlayers();
		HologramManager.getManager().loadHolograms();
		
		
		//Default spawn of the Server
		lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
		
		
		
		
		
		registerTasks();
		registerEvents();
		registerCommands();
		
		
		//Load worlds
		new BukkitRunnable() {

			@Override
			public void run() {
				new WorldCreator("arenas").createWorld();
				//new WorldCreator("construccion").createWorld();
				
			}
			
		}.runTaskLater(this, 20L);
		
		new BukkitRunnable() {
		@Override
		public void run() {
			DataManager.getManager().loadArenas();

		}
		
	}.runTaskLater(this, 20L);
	
		
	new BukkitRunnable() {
		public void run() {
			HologramManager.getManager().updateHolograms();
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
		NPCManager.getManager().removeNPCs();
		//Save Data
		for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
			 HologramManager.getManager().removeHolograms(sp);
		}
		DataManager.getManager().savePlayers();	
		HologramManager.getManager().saveHolograms();
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);
		getServer().getPluginManager().registerEvents(new ServerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerChat(), this);
		getServer().getPluginManager().registerEvents(new NPCListener(), this);
		getServer().getPluginManager().registerEvents(new ReinoManager(), this);
		new CustomPacketListener();
		
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
		getCommand("reino").setExecutor(new ReinoCommand());
		getCommand("playto").setExecutor(new PlaytoCommand());
		getCommand("crumble").setExecutor(new CrumbleCommand());
		getCommand("translate").setExecutor(new TranslateCommand());
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("ffaevent").setExecutor(new FFAEventCommand());
		getCommand("staffchat").setExecutor(new StaffChatCommand());
		getCommand("mutationtoken").setExecutor(new MutationTokenCommand());
	}
	
	
	private void registerTasks() {
		new OnMoveTask();
		new ScoreboardTask();
		new ArenaTimeTask();
		new TabTask();
		new OnlineTimeTask();
		new ParkourTimerTask();
		new SortRankingTask();
		
	}
	

	
}
