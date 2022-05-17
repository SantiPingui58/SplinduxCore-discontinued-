package me.santipingui58.splindux.task.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.parkour.ParkourMode;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.splindux.utils.WeightedRandomList;

public class StressingTask {
	
	
	WeightedRandomList<String> frases = new WeightedRandomList<String>();
	
	public StressingTask() {
		
		frases.addEntry("gg",20);
		frases.addEntry("unban bekah",2);
		frases.addEntry("Join spleef 1v1",20);
		frases.addEntry("xaxaxa",10);
		frases.addEntry("suka",10);
		frases.addEntry("no te encierres",10);
		frases.addEntry("no te encierres",10);
		frases.addEntry("algun admin?",10);
		frases.addEntry("horizon ez",2);
		frases.addEntry("el spleef de imine era mejor",2);
		
		
		new BukkitRunnable() {
			public void run() {
		
				

				if (Main.stressing) {
	
	
	
	
	for (Player p : Bukkit.getOnlinePlayers()) {
		if (p.hasPermission("splindux.staff")) continue;
		
		
		
		int random = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		
		//if (random<=1) p.chat(frases.getRandom());
		
		
		
		if (p.getLocation().getWorld().getName().equalsIgnoreCase("world")) {
		
		if (p.getLocation().distanceSquared(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true))<=5) {
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	
		if (random <=35) {
			//ffa
			
			random = ThreadLocalRandom.current().nextInt(1, 100 + 1);
			int i = 1;
			 new BukkitRunnable() {
				 
					public void run() {
						try {
							 GameManager.getManager().addFFAQueue(sp, i <=33 ? SpleefType.SPLEEF : i <=66 ? SpleefType.TNTRUN : SpleefType.SPLEGG);
						} catch(Exception ex) {}
					}
				}.runTaskLater(Main.get(), 15L);
				
		} else if (random <= 20) {
			//parkour
			new BukkitRunnable() {
				public void run() {
					try {
					ParkourManager.getManager().joinLevel(sp.getParkourPlayer(), ParkourManager.getManager().getLevel(1), ParkourMode.MOST_JUMPS);
					} catch(Exception ex) {}
				}
			}.runTaskLaterAsynchronously(Main.get(), 15L);
			
		
		} else if (random <= 50) {
			//spectate
			
			
			new BukkitRunnable() {
				public void run() {
					try {
						List<Arena> arenas  = new ArrayList<Arena>();
						for (Arena arena : DataManager.getManager().getArenas()) {
							if (arena.getGameType().equals(GameType.DUEL) && arena.getState().equals(GameState.GAME)) {
								arenas.add(arena);
							}
						}
						if (arenas.size()==0) return;
						Collections.shuffle(arenas);
						SpectateManager.getManager().spectateSpleef(sp, arenas.get(0));
					} catch(Exception ex) {}
				}
			}.runTaskLater(Main.get(), 15L);
			
		} else if (random <=70) {
			//lobby
			
			int x = ThreadLocalRandom.current().nextInt(-300, 300 + 1);
			int y = ThreadLocalRandom.current().nextInt(150, 215 + 1);
			int z = ThreadLocalRandom.current().nextInt(-300, 300 + 1);
			new BukkitRunnable() {
				public void run() {
					p.teleport(new Location (p.getLocation().getWorld(), x,y,z));
				}
			}.runTask(Main.get());
			
		} else {
			//duel
			
			random = ThreadLocalRandom.current().nextInt(1, 100 + 1);
			int i = random;
			new BukkitRunnable() {
				public void run() {
					try {
						
						GameManager.getManager().addDuelQueue(sp, 1, null, i <=33 ? SpleefType.SPLEEF : i <=66 ? SpleefType.TNTRUN : SpleefType.SPLEGG, false, null);
					} catch(Exception ex) {}
				}
			}.runTaskLater(Main.get(), 15L);
		}
		} else if (random >=90) {
			new BukkitRunnable() {
				public void run() {
					p.teleport(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("mainlobby"), true));
				}
			}.runTask(Main.get());
			
		}
	} else if (p.getLocation().getWorld().getName().equalsIgnoreCase("arenas") || p.getLocation().getWorld().getName().equalsIgnoreCase("parkour")) {
		if (random<=25) {
			new BukkitRunnable() {
				public void run() {
			p.teleport(new Location(p.getWorld(),p.getLocation().getX(), p.getLocation().getY()-2,p.getLocation().getZ()));
				}
			}.runTask(Main.get());
		}
	}
}
}


			 
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 60L);
	}
}

