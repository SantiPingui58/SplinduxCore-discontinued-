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
import me.santipingui58.splindux.commands.DuelCommand;
import me.santipingui58.splindux.commands.EndGameCommand;
import me.santipingui58.splindux.commands.FlyCommand;
import me.santipingui58.splindux.commands.HoverCommand;
import me.santipingui58.splindux.commands.MatchesCommand;
import me.santipingui58.splindux.commands.MsgCommand;
import me.santipingui58.splindux.commands.PingCommand;
import me.santipingui58.splindux.commands.RankCommand;
import me.santipingui58.splindux.commands.ResetCommand;
import me.santipingui58.splindux.commands.RideCommand;
import me.santipingui58.splindux.commands.SetupCommand;
import me.santipingui58.splindux.commands.SpawnCommand;
import me.santipingui58.splindux.commands.SpectateCommand;
import me.santipingui58.splindux.commands.SplinduxLoginCommand;
import me.santipingui58.splindux.commands.SplinduxRegisterCommand;
import me.santipingui58.splindux.commands.StatsCommand;
import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.listener.NPCListener;
import me.santipingui58.splindux.listener.PlayerChat;
import me.santipingui58.splindux.listener.PlayerConnectListener;
import me.santipingui58.splindux.listener.PlayerListener;
import me.santipingui58.splindux.listener.ServerListener;
import me.santipingui58.splindux.task.ArenaTimeTask;
import me.santipingui58.splindux.task.OnMoveTask;
import me.santipingui58.splindux.task.OnlineTimeTask;
import me.santipingui58.splindux.task.ParkourTimerTask;
import me.santipingui58.splindux.task.ScoreboardTask;
import me.santipingui58.splindux.task.TabTask;
import me.santipingui58.splindux.utils.Configuration;
import me.santipingui58.splindux.utils.Utils;


//Stats
//Hologramas
//Añadir todas las renas
//Ranked
//Splegg y BowSpleef
//Discord
//NameMC
//Votifier
//Sistema de niveles
//Friends
//Anticheat
//guilds
//openaudiomc



public class Main extends JavaPlugin {

	public static Plugin pl;
	public static String prefix;
	public static boolean prefix_enabled;
	public static Configuration config,messages,arenas,data;
	public static Location lobby;
	public static Plugin get() {
	    return pl;
	  }	
	
	
	@Override
	public void onEnable() {
		pl = this;
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer("§cSplinduxCore restarting, please rejoin");
		}
		
		GEconomyProvider.setMysteryDustStorage(new EconomyManager(this, "Splindux"));	
		config = new Configuration("config.yml",this);
		data = new Configuration("data.yml",this);
		arenas = new Configuration("arenas.yml",this);	
		DataManager.getManager().loadPlayers();
		
		lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true);
		
		registerTasks();
		registerEvents();
		registerCommands();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				new WorldCreator("arenas").createWorld();
				
			}
			
		}.runTaskLater(this, 20L);
		
		new BukkitRunnable() {
		@Override
		public void run() {
			DataManager.getManager().loadArenas();
		}
		
	}.runTaskLater(this, 20L);
	
		

		 
	
	}
		
	@Override
	public void onDisable() {	
		DataManager.getManager().savePlayers();		
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);
		getServer().getPluginManager().registerEvents(new ServerListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerChat(), this);
		getServer().getPluginManager().registerEvents(new NPCListener(), this);
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
	}
	
	private void registerTasks() {
		new OnMoveTask();
		new ScoreboardTask();
		new ArenaTimeTask();
		new TabTask();
		new OnlineTimeTask();
		new ParkourTimerTask();
	}
	

	
}
