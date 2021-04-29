package me.santipingui58.splindux.game.spectate;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.parkour.ParkourArena;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.utils.ActionBarAPI;
import me.santipingui58.translate.Main;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;

public class SpectateManager {

	private static SpectateManager manager;	
	 public static SpectateManager getManager() {
	        if (manager == null)
	        	manager = new SpectateManager();
	        return manager;
	    }
	 
	 private void spectate(SpleefPlayer sp,Arena arena,List<SpleefPlayer> playing, List<SpleefPlayer> spectators,boolean giveSpectate) {

			new BukkitRunnable() {
				public void run() {
		if (giveSpectate) sp.giveSpectateItems();
		
			for (SpleefPlayer play : playing) {
				play.getPlayer().hidePlayer(Main.get(), sp.getPlayer());		
				sendKeepInTABPacket(play.getPlayer(),sp.getPlayer());
				}
			for (SpleefPlayer spect : spectators) {
				
				if (sp.isHidingSpectators()) {
					sp.getPlayer().hidePlayer(Main.get(),spect.getPlayer());
				} else {
					sp.getPlayer().showPlayer(Main.get(),spect.getPlayer());
				}
			

				if (spect.isHidingSpectators()) {
					spect.getPlayer().hidePlayer(Main.get(), sp.getPlayer());	
				} else {
					spect.getPlayer().showPlayer(Main.get(), sp.getPlayer());	
				}
				
				sendKeepInTABPacket(spect.getPlayer(),sp.getPlayer());
			}
			
			sp.getPlayer().teleport(arena.getLobby());	
				}
			}.runTask(Main.get());
			
	 }
	 
	public void spectateParkour(SpleefPlayer sp, ParkourArena arena) {
		sp.getPlayer().teleport(arena.getPlayer().getPlayer().getPlayer());
		List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		list.add(arena.getPlayer().getPlayer());
		
		//spectate(sp, list, arena.getSpectators());
		 
	}
	 
	 
	public void spectateSpleef(SpleefPlayer sp,Arena arena) {	
		
		if (arena.getGameType().equals(GameType.DUEL)) {
		sp.setScoreboard(ScoreboardType._1VS1GAME);
		} else if (arena.getGameType().equals(GameType.FFA)) {
			sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
			
		}
		
		DataManager.getManager().getLobbyPlayers().remove(sp.getUUID());
		DataManager.getManager().getPlayingPlayers().add(sp.getUUID());
		arena.getSpectators().add(sp);
		
		List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		for (SpleefPlayer playing : arena.getPlayers()) {
			if (!playing.isSpectating()) {
				list.add(playing);
			}
		}
		
		if (arena.isGuildGame()) {
			if (arena.getSpectators().size()+1>arena.getMaxSpectators()) arena.addMaxSpectators();
		}
		
		
		spectate(sp,arena,list,arena.getSpectators(),arena.getGameType().equals(GameType.DUEL));
		
		new BukkitRunnable() {
			public void run() {
				if (sp.isSpectating()) {
		sp.getPlayer().setAllowFlight(true);
		sp.getPlayer().setFlying(true);	
				}
		}
		}.runTaskLater(Main.get(), 5L);
	}
	
	
	
	public void doEverything(Arena arena) {
		new BukkitRunnable() {
			public void run() {
		for (SpleefPlayer sp : arena.getPlayers()) {
			for (SpleefPlayer spect : arena.getSpectators()) {
				if (sp!=null && sp.getOfflinePlayer().isOnline() && spect!=null && spect.getOfflinePlayer().isOnline()) 
				sp.getPlayer().hidePlayer(Main.get(),spect.getPlayer());
				
				for (SpleefPlayer otherPlayers : arena.getPlayers()) {
					if (!otherPlayers.equals(sp)) sp.getPlayer().showPlayer(Main.get(),otherPlayers.getPlayer());
				}
				sendKeepInTABPacket(sp.getPlayer(),spect.getPlayer());
			}
		}
			}
	}.runTask(Main.get());
	}
	
	
	public void showOrHideSpectators(SpleefPlayer sp,boolean show) {
		sp.hideSpectators(!show);
		sp.sendMessage("§aHiding spectators: §b" + String.valueOf(!show).toUpperCase());
		List<SpleefPlayer> spectators = new ArrayList<SpleefPlayer>();
		if (sp.getSpleefArenaSpectating()!=null) {
			spectators.addAll(sp.getSpleefArenaSpectating().getSpectators());
		} else if (sp.getParkourArenaSpectating()!=null) {
			spectators.addAll(sp.getParkourArenaSpectating().getSpectators());
		}
	
			for (SpleefPlayer spect : spectators) {
				if (show) {
					if (sp.getOfflinePlayer().isOnline()) sp.getPlayer().showPlayer(Main.get(), spect.getPlayer());	
				} else {
					if (sp.getOfflinePlayer().isOnline()) sp.getPlayer().hidePlayer(Main.get(), spect.getPlayer());	
					
				}
				sendKeepInTABPacket(sp.getPlayer(),spect.getPlayer());
			}	
	}
	
	public void leaveSpectate(SpleefPlayer sp, boolean allowFly) {
		new BukkitRunnable() {
			public void run() {
		ActionBarAPI.sendActionBar(sp.getPlayer(), "");
		if (allowFly) {
		sp.getPlayer().setAllowFlight(false);
		sp.getPlayer().setFlying(false);
		}
		if (sp.getOfflinePlayer().isOnline()) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.getPlayer().showPlayer(Main.get(),sp.getPlayer());
			sp.getPlayer().showPlayer(Main.get(),p);
		}		 
				}	
		}
		}.runTask(Main.get());
		
		Arena arena = sp.getSpleefArenaSpectating();	
		if (arena!=null) arena.getSpectators().remove(sp);
	
	}
	
	

	
	
	public void sendKeepInTABPacket(Player player, Player toShow) {
		EntityPlayer[] entity = { (EntityPlayer) ((CraftPlayer) toShow).getHandle()};
 		((CraftPlayer) player).getHandle().playerConnection.sendPacket( new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entity));
	}
	
}
