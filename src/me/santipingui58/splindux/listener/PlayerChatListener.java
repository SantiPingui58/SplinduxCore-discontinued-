package me.santipingui58.splindux.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.translate.Language;
import me.santipingui58.translate.TranslateAPI;
import ru.tehkode.permissions.bukkit.PermissionsEx;



public class PlayerChatListener implements Listener {
	
	
	
	private List<Player> cooldown = new ArrayList<Player>();
	@EventHandler (priority= EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		e.getRecipients().clear();
		Player p = e.getPlayer();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		  
		if (!p.hasPermission("splindux.chatcooldown")) {
			if (cooldown.contains(p)) {
					p.sendMessage("§cWait 2 seconds between messages.");
					e.setCancelled(true);
					return;
				}
				
			}
		cooldown.add(p);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				cooldown.remove(p);
			}
		}.runTaskLaterAsynchronously(Main.get(), 40L);
			
		if (p.hasPermission("splindux.afk")) {
			sp.setAFKTimer(0);
			if (sp.isAfk()) {
				sp.back();			
				sp.getPlayer().sendMessage("§7You are not longer AFK");	
			} 
		}
		
		String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(p).getPrefix());
		String level = LevelManager.getManager().getRank(sp).getRankName();
		ChatColor c = ChatColor.WHITE;
		
		if (p.hasPermission("splindux.vip")) {
			c = ChatColor.DARK_AQUA;
		}
	
		
		int pos = sp.getRankingPosition();
		String position = "";
		if (pos!=-1) position = "§7("+pos+"º) ";
		prefix = position + prefix + "§7["+ level+"§7] "+c;
		String msg = e.getMessage();

		 msg = e.getMessage().replaceAll("%", "%%");
		 
		 List<SpleefPlayer> withTranslateESP = new ArrayList<SpleefPlayer>();
		 List<SpleefPlayer> withTranslateENG = new ArrayList<SpleefPlayer>();
		 List<SpleefPlayer> withTranslateRUS = new ArrayList<SpleefPlayer>();
		 
		 List<SpleefPlayer> withoutTranslate = new ArrayList<SpleefPlayer>();
		 withoutTranslate.add(sp);
		 
		 Set<Player> players = new HashSet<Player>();
		 if (p.getWorld().getName().equalsIgnoreCase("tournament")) {
			players.addAll(Bukkit.getWorld("tournament").getPlayers());
		 } else {
			 players.addAll(Bukkit.getOnlinePlayers());
		 }
		 for (Player online : players) {
			 if (online.equals(p)) continue;		
			 SpleefPlayer sonline = SpleefPlayer.getSpleefPlayer(online);
			 if (!sonline.getOptions().hasChat() && !p.hasPermission("splindux.staff")) continue;
			 
			 Language receptor = sonline.getOptions().getLanguage();
			 Language emisor = sp.getOptions().getLanguage();
			 if (sonline.getOptions().hasTranslate() && receptor!=emisor) {
				 if (receptor.equals(Language.SPANISH)) {
					 withTranslateESP.add(sonline);
				 } else if (receptor.equals(Language.ENGLISH)) {
					 withTranslateENG.add(sonline);
				 } else {
					 withTranslateRUS.add(sonline);
				 }
			 } else {
				 withoutTranslate.add(sonline);
			 }
		 }
		 
		 String message = "";
		 
		 ChatColor color = sp.getOptions().getDefaultColorChat();
		 if (p.hasPermission("splindux.staff")) {
			 message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: " + color+msg);
		} else if (p.hasPermission("splindux.donatorchat")) {
			message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: "+color+msg);
		} else {
			message = prefix +" "+sp.getName() +"§8: §7"+ msg;
		}
	 e.setFormat(message);
		 for (SpleefPlayer player : withoutTranslate) {
			 player.sendMessage(message);
		 }
		 

	
				if (p.hasPermission("splindux.staff")) {
					 message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: " + color);
				} else if (p.hasPermission("splindux.donatorchat")) {
					message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: "+color);
				} else {
					message = prefix +" "+sp.getName() +"§8: §7";
				}
				
				final String m = message;
				try {
					TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.SPANISH, msg).thenAccept(text -> {
						
						for (SpleefPlayer player : withTranslateESP) player.sendMessage(m+text);
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.ENGLISH, msg).thenAccept(text -> {
						for (SpleefPlayer player : withTranslateENG) player.sendMessage(m+text);
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					TranslateAPI.getAPI().translate(sp.getOptions().getLanguage(), Language.RUSSIAN, msg).thenAccept(text -> {
						for (SpleefPlayer player : withTranslateRUS) player.sendMessage(m+text);
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		
	}
	
	

}
