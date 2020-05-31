package me.santipingui58.splindux.listener;

import java.util.ArrayList;
import java.util.List;

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



public class PlayerChat implements Listener {
	
	
	
	private List<Player> cooldown = new ArrayList<Player>();
	@EventHandler (priority= EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		  if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
 			 e.setCancelled(true);
				 return;	 
		 }
		  
		if (!p.hasPermission("splindux.chatcooldown")) {
			if (cooldown.contains(p)) {
					p.sendMessage("§cWait 2 seconds between messages.");
					e.setCancelled(true);
					return;
				}
				
			}
			
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
		prefix = prefix + "§7["+ level+"§7] "+c;
		String msg = e.getMessage();

		 msg = e.getMessage().replaceAll("%", "%%");
		 
		 List<Player> remove = new ArrayList<Player>();
		 for (Player r : e.getRecipients()) {
			 if (r.equals(p)) continue;
			 
			 SpleefPlayer sr = SpleefPlayer.getSpleefPlayer(r);
			 if (sr.getOptions().hasTranslate()) {
				 remove.add(r);
				 String translate = "";
				 Language receptor = sr.getOptions().getLanguage();
				 Language emisor = sp.getOptions().getLanguage();
				 
				 if (receptor!=emisor) {
					 translate = TranslateAPI.getAPI().translate(msg, emisor, receptor);
				 }

				 String output = "";
				 if (translate==null || translate=="") {
					output = msg;
				 }   else {
					 output = translate;
				 }
				 
				 if (p.hasPermission("splindux.staff")) {
					 output = ChatColor.translateAlternateColorCodes('&', output);
					 r.sendMessage(prefix +sp.getName() +"§8: §b"+output );
				} else if (p.hasPermission("splindux.donatorchat")) {
					output = ChatColor.translateAlternateColorCodes('&', output);
					r.sendMessage(prefix +sp.getName() +"§8: §f"+output);
				} else {
					r.sendMessage(prefix +" "+sp.getName() +"§8: §7"+ output );
				}
			 }
		 }
		 
		 for (Player r : remove) {
			 e.getRecipients().remove(r);
		 }
		 		
		
		if (p.hasPermission("splindux.staff")) {
			 msg = ChatColor.translateAlternateColorCodes('&', msg);
			e.setFormat(prefix +sp.getName() +"§8: §b"+msg );
		} else if (p.hasPermission("splindux.donatorchat")) {
			 msg = ChatColor.translateAlternateColorCodes('&', msg);
			e.setFormat(prefix +sp.getName() +"§8: §f"+msg);
		} else {
			e.setFormat(prefix +" "+sp.getName() +"§8: §7"+ msg );
		}
		
		cooldown.add(p);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				cooldown.remove(p);
			}
		}.runTaskLater(Main.get(), 40L);
	}
	
	

}
