package me.santipingui58.splindux.anouncements;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
public class Announcement {
	private UUID uuid;
	private String message;
	private String url;
	
	public Announcement(UUID uuid,String message, String url) {
		this.uuid = uuid;
		this.message = message;
		this.url = url;
	}
	
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getURL() {
		return this.url;
	}
	
	
	public void sendAnnouncement() {
		String prefix = "§2[§cAnnouncement§2] ";
		if (this.url!= null && !this.url.equalsIgnoreCase("")) {
		Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', message) + " §3" + url);
	} else {
		Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
	}
		}
	
}
