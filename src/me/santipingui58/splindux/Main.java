package me.santipingui58.splindux;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import be.isach.ultracosmetics.UltraCosmetics;
import github.scarsz.discordsrv.DiscordSRV;
import me.blackvein.quests.Quests;
import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.anouncements.AnnouncementManager;
import me.santipingui58.splindux.commands.AFKCommand;
import me.santipingui58.splindux.commands.AdCommand;
import me.santipingui58.splindux.commands.AdminCommand;
import me.santipingui58.splindux.commands.BetCommand;
import me.santipingui58.splindux.commands.ChatCommand;
import me.santipingui58.splindux.commands.CoinsCommand;
import me.santipingui58.splindux.commands.CrumbleCommand;
import me.santipingui58.splindux.commands.DuelCommand;
import me.santipingui58.splindux.commands.EndGameCommand;
import me.santipingui58.splindux.commands.FFAEventCommand;
import me.santipingui58.splindux.commands.FlyCommand;
import me.santipingui58.splindux.commands.ForceDecayCommand;
import me.santipingui58.splindux.commands.ForceFinishCommand;
import me.santipingui58.splindux.commands.ForceGameTimeCommand;
import me.santipingui58.splindux.commands.ForcePauseCommand;
import me.santipingui58.splindux.commands.ForcePlaytoCommand;
import me.santipingui58.splindux.commands.ForceRedoCommand;
import me.santipingui58.splindux.commands.ForceResetCommand;
import me.santipingui58.splindux.commands.ForceResumeCommand;
import me.santipingui58.splindux.commands.ForceScoreCommand;
import me.santipingui58.splindux.commands.FriendsCommand;
import me.santipingui58.splindux.commands.GuildChatCommand;
import me.santipingui58.splindux.commands.GuildCommand;
import me.santipingui58.splindux.commands.HeadCommand;
import me.santipingui58.splindux.commands.HelmetsCommand;
import me.santipingui58.splindux.commands.HelpCommand;
import me.santipingui58.splindux.commands.HologramCommand;
import me.santipingui58.splindux.commands.HoverCommand;
import me.santipingui58.splindux.commands.LevelCommand;
import me.santipingui58.splindux.commands.MatchesCommand;
import me.santipingui58.splindux.commands.MsgCommand;
import me.santipingui58.splindux.commands.MutationTokenCommand;
import me.santipingui58.splindux.commands.NightVisionCommand;
import me.santipingui58.splindux.commands.ParkourSetupCommand;
import me.santipingui58.splindux.commands.PartyChatCommand;
import me.santipingui58.splindux.commands.PartyCommand;
import me.santipingui58.splindux.commands.PauseCommand;
import me.santipingui58.splindux.commands.PingCommand;
import me.santipingui58.splindux.commands.PlaytoCommand;
import me.santipingui58.splindux.commands.RankCommand;
import me.santipingui58.splindux.commands.RankingCommand;
import me.santipingui58.splindux.commands.RedoCommand;
import me.santipingui58.splindux.commands.ResetCommand;
import me.santipingui58.splindux.commands.ResumeCommand;
import me.santipingui58.splindux.commands.RideCommand;
import me.santipingui58.splindux.commands.SetupCommand;
import me.santipingui58.splindux.commands.SpawnCommand;
import me.santipingui58.splindux.commands.SpectateCommand;
import me.santipingui58.splindux.commands.SplinduxLoginCommand;
import me.santipingui58.splindux.commands.SplinduxRegisterCommand;
import me.santipingui58.splindux.commands.StaffChatCommand;
import me.santipingui58.splindux.commands.StaffCommand;
import me.santipingui58.splindux.commands.StatsCommand;
import me.santipingui58.splindux.commands.StreamCommand;
import me.santipingui58.splindux.commands.TipCommand;
import me.santipingui58.splindux.commands.TitleCommand;
import me.santipingui58.splindux.commands.TournamentCommand;
import me.santipingui58.splindux.commands.TranslateCommand;
import me.santipingui58.splindux.cosmetics.helmets.HelmetManager;
import me.santipingui58.splindux.cosmetics.particles.ParticleManager;
import me.santipingui58.splindux.cosmetics.petshop.PetShopManager;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.listener.CustomPacketListener;
import me.santipingui58.splindux.listener.DiscordSRVListener;
import me.santipingui58.splindux.listener.MutationListener;
import me.santipingui58.splindux.listener.NPCListener;
import me.santipingui58.splindux.listener.PlayerChatListener;
import me.santipingui58.splindux.listener.PlayerConnectListener;
import me.santipingui58.splindux.listener.PlayerListener;
import me.santipingui58.splindux.listener.QuestListener;
import me.santipingui58.splindux.listener.ServerListener;
import me.santipingui58.splindux.listener.VotifierListener;
import me.santipingui58.splindux.npc.NPCManager;
import me.santipingui58.splindux.placeholdersapi.SplinduxExpansion;
import me.santipingui58.splindux.quest.QuestsManager;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.ranking.RankingManager;
import me.santipingui58.splindux.sws.SWSManager;
import me.santipingui58.splindux.task.TaskManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import net.minecraft.server.v1_12_R1.Block;
import me.santipingui58.splindux.utils.Configuration;
import me.santipingui58.splindux.utils.Utils;




/**Querido programador:
*Cuando escribí este código, solo dios y
*yo sabíamos como funcionaba
*Ahora, solo dios lo sabe!

*Igualmente, si estás intentando optimizar
*este programa y falla (lo más probable)
*por favor aumenta este contador como una
*advertencia para la próxima persona:
*
*horas_totales_gastadas_aqui = 254

*/

/**Ideas
 * 
 * @author SantiPingui58
 * 2.9

 * Party TEST
 * New Lobby Pendiente
 * Bajar precios de gadgets DONE
 * Poner para que de SplinBoxes por tiempo online Test
 * Cambiar sueldos guilds TEST
 * Rankings de jugadores (GuildPlayer Value, Spleef EXP, Coins, Votos ) DONE
 * Change Spleef TEST  21x32 // 23x34  DONE

 * 
 * 
 * NETWORKING
 * 
 * 
 * 2.10
 * Poner Lootboxes
 * /stream YT Command 
 * Quests 
 * Change Ranked System Pendiente
 *  Sistema de friendship level + lootboxes
 * Sistema de coins cuando jugas con amigos o miembros de tu guild 
 * 
 * 
 * 
 * 
 * 
 * 2.11
 * Mutations for Duels and Splegg/TNTRun FFA(Minispleef, Ninja, Hypixel tntrun)
 * BowSpleef, EXP Spleef
 * Fishing 
 * Achievements
 * Bombs
 * Anticheat
 * 
 * 
 * 
 * 
 * Spleef KotH
 * Spleef Mobs
 * Spleef CATF y COTF
 * Replay 

 */




public class Main extends JavaPlugin implements Listener, PluginMessageListener {

	/**
	 * Instance of the plugin
	 */
	public static Plugin pl;
	
	//.yml files	
	public static Configuration config,arenas,recordings,timelimit,petblocks,announcements,petshop,ranking,tournaments,jumps,lang,helmets,guilds, quests,match_history,countries;
	
	//Spawn point of the server
	public static Location lobby;
	
	
	public static Location tournament_lobby;
	
	//If the PvP is enabled or not
	public static boolean pvp,canJoin,canBreak;
	public static boolean fly = true;
	//Handles Cosmetics integration
	public static UltraCosmetics ultraCosmetics = null;
	
	//When true, plugin will save data when it gets reloaded
	private static boolean saveOnDisable;
	
	public static boolean ffa2v2 = false;
	
	public static boolean swws = false;
	
	public static boolean torneo_yt = false;
	
	public static boolean stressing = false;
	
	public static Quests questsAPI;
	
	public static int thetowers;
	
	
	  private DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);
		/**
	    * @return Returns Main plugin instance.
	    */
	public static Plugin get() {
	    return pl;
	  }	
	

	@Override
	public void onEnable() {
		pl = this;
		Block block = Block.getByName("snow");
		try {
		    Field field = Block.class.getDeclaredField("durability");
		    field.setAccessible(true);
		    field.set(block, 30f);
		} catch (NoSuchFieldException | IllegalAccessException e) {
		    e.printStackTrace();
		}
		
		 
		//Load worlds
				new WorldCreator("arenas").createWorld();
				new WorldCreator("lobby").createWorld();
				new WorldCreator("parkour").createWorld();
				new WorldCreator("navidad").createWorld();
				new WorldCreator("event").createWorld();
				new WorldCreator("tournament").createWorld();
				
				tournament_lobby = new Location(Bukkit.getWorld("tournament"),-105,122,217);
				//new WorldCreator("construccion").createWorld();	
				DiscordSRV.api.subscribe(discordsrvListener);
				
			    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		
		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman reload SplinduxHikariAPI");
		
		//PlaceholdersAPI expansions to use commands on DiscordSRV
		 if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
             new SplinduxExpansion(this).register();
       }
		 
		 questsAPI =  (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
		 
		 
		 
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
								HologramManager.getManager().showHolograms(p.getUniqueId());
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
		quests = new Configuration("quests.yml",this);
		match_history = new Configuration("match_history.yml",this);
		countries = new Configuration("countries.yml",this);
		
		DataManager.getManager().queues(false);
		
		//Loading of Particles, Holograms, Timelimits, Announcements, pets, players, tasks and queues
		
		

		
		new BukkitRunnable() {
			public void run() {
		ParticleManager.getManager().loadEffectsAndTypes();	
		AnnouncementManager.getManager().loadAnnouncements();
		PetShopManager.getManager().loadPets();	
		HelmetManager.getManager().loadHelmets();
		GuildsManager.getManager().loadGuilds();
		QuestsManager.getManager().loadNPCs();
		QuestsManager.getManager().loadQuests();
		if (Main.swws) SWSManager.getManager().loadSWS();
		}
		}.runTaskAsynchronously(Main.get());
		
		TaskManager.getManager().task();
		//Default spawn of the Server
		lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
		
		//Teleports all players that were in the arena's world, in case the server was restarted while games were being played.
				for (Player p : Bukkit.getOnlinePlayers()) {
					String world = p.getWorld().getName();
					if (world.equalsIgnoreCase("arenas") || world.equalsIgnoreCase("parkour")) {
						if (!p.getGameMode().equals(GameMode.CREATIVE)) {
					p.teleport(lobby);
					p.getInventory().clear();
						}
					new BukkitRunnable() {
						public void run() {
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
							if (!p.getGameMode().equals(GameMode.CREATIVE)) {
							sp.giveLobbyItems();
							sp.updateScoreboard();
							}
							saveOnDisable =true;
						}
						}.runTaskLater(Main.get(), 10L);
					}
				}
				
				
		
			
		setupCosmetics();
		
		ParkourManager gm = ParkourManager.getManager();
		gm.loadJumps();
		gm.loadLevels();
		gm.loadProbabilities();
		StatsManager.getManager().loadParkourLevels();

		//Register events and commands
		registerEvents();
		registerCommands();
		
		
		
		//Load arenas
		new BukkitRunnable() {
		public void run() {
			DataManager.getManager().loadArenas();
			TimeLimitManager.getManager().loadTimeLimit();
			HologramManager.getManager().loadHolograms();
			RankingManager.getManager().loadRanking();
			
		
		}
	}.runTaskLaterAsynchronously(this, 10L);
	
		
	new BukkitRunnable() {
		public void run() {
			//Load NPCs
			NPCManager.getManager().loadNPCs();

		}
		
	}.runTaskLater(this, 60L);	
	
	
	new BukkitRunnable() {
		public void run() {
			Main.canJoin = true;

		}
		
	}.runTaskLaterAsynchronously(this, 20L);	
    getServer().getPluginManager().registerEvents(this, this);

	}
	

	@Override
	public void onDisable() {	
		//Save Data
		   DiscordSRV.api.unsubscribe(discordsrvListener);
		if (saveOnDisable) DataManager.getManager().saveData();
		
		 ByteArrayOutputStream b = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(b);
	        try {
	            out.writeUTF("Connect");
	            out.writeUTF("limbo");
	        } catch (Exception e) {
	            return;
	        }
	        for (Player p : Bukkit.getOnlinePlayers()) {
		        p.sendPluginMessage(Main.get(), "BungeeCord", b.toByteArray());
	        }
		
		GuildsManager.getManager().saveGuilds();
		NPCManager.getManager().removeNPCs();
		PetShopManager.getManager().savePets();
		TimeLimitManager.getManager().saveTimeLimit();
		HologramManager.getManager().saveHolograms();
		SWSManager.getManager().saveCountries();
		SWSManager.getManager().saveData();
		
		ParkourManager.getManager().saveArenas();
		
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			HologramManager.getManager().hideHolograms(sp.getUUID());
		}			
		
		
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);
		getServer().getPluginManager().registerEvents(new ServerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
		getServer().getPluginManager().registerEvents(new NPCListener(), this);
		getServer().getPluginManager().registerEvents(new MutationListener(), this);
		getServer().getPluginManager().registerEvents(new VotifierListener(), this);
		//getServer().getPluginManager().registerEvents(new TeleportFix(this),this);
		getServer().getPluginManager().registerEvents(new QuestListener(),this);
		new CustomPacketListener();
		
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
		getCommand("forcedecay").setExecutor(new ForceDecayCommand());
		getCommand("forcegametime").setExecutor(new ForceGameTimeCommand());
		getCommand("party").setExecutor(new PartyCommand());
		getCommand("partychat").setExecutor(new PartyChatCommand());
		getCommand("title").setExecutor(new TitleCommand());
		getCommand("pause").setExecutor(new PauseCommand());
		getCommand("resume").setExecutor(new ResumeCommand());
		getCommand("helmets").setExecutor(new HelmetsCommand());
		getCommand("chat").setExecutor(new ChatCommand());		
		getCommand("forcepause").setExecutor(new ForcePauseCommand());
		getCommand("forceresume").setExecutor(new ForceResumeCommand());
		getCommand("forceplayto").setExecutor(new ForcePlaytoCommand());
		getCommand("forcescore").setExecutor(new ForceScoreCommand());
		getCommand("redo").setExecutor(new RedoCommand());
		getCommand("forceredo").setExecutor(new ForceRedoCommand());
		getCommand("stream").setExecutor(new StreamCommand());
	}	
	
	
	

	 @Override
	  public void onPluginMessageReceived(String channel, Player player, byte[] message) {
	    if (!channel.equals("BungeeCord")) {
	      return;
	    }
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    if (subchannel.equals("PlayerCount")) {
	    	thetowers = in.readInt();
	    	if (thetowers >100) thetowers = 0;
	    	
	    }
	  }
}
