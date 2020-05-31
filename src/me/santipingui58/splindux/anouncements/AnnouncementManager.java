package me.santipingui58.splindux.anouncements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class AnnouncementManager {

	private static AnnouncementManager manager;	
	 public static AnnouncementManager getManager() {
	        if (manager == null)
	        	manager = new AnnouncementManager();
	        return manager;
	    }
	
	
	 private List<Ad> ads = new ArrayList<Ad>();
	 private List<Announcement> announcements = new ArrayList<Announcement>();
	 private int adTime;
	 private int announcementTime;
	 
	 public List<Ad> getAds() {
		 return this.ads;
	 }
	 
	 public List<Announcement> getAnnouncements() {
		 return this.announcements;
	 }
	 
	 public void sendAnnouncement() {
		 if (Bukkit.getOnlinePlayers().size()>0) {
		    Random rand = new Random();
		    Announcement announcement = this.announcements.get(rand.nextInt(this.announcements.size()));
		    announcement.sendAnnouncement();		    
	 }
	 }
	 
	 public void sendAd() {
		    Random rand = new Random();
		    Ad ad = this.ads.get(rand.nextInt(this.ads.size()));
		    for (Player p : Bukkit.getOnlinePlayers()) {
		    	SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		    ad.sendAd(sp);
		    }
	 }
	 
	 public void loadAnnouncements() {
		 if (Main.announcements.getConfig().contains("announcements")) {
			 Set<String> announcements = Main.announcements.getConfig().getConfigurationSection("announcements").getKeys(false);
			 for (String s : announcements) {
				 UUID uuid = UUID.fromString(s);
				 String message = Main.announcements.getConfig().getString("announcements."+s+".message");
				 String url = null;
				 if ( Main.announcements.getConfig().contains("announcements."+s+".url")) {
					  url = Main.announcements.getConfig().getString("announcements."+s+".url");
				 }
				 Announcement announcement = new Announcement(uuid, message, url);
				 this.announcements.add(announcement);
			 }
		 } 
		 
		 
		 if (Main.announcements.getConfig().contains("ads")) {
			 Set<String> ads = Main.announcements.getConfig().getConfigurationSection("ads").getKeys(false);
			 for (String s : ads) {
				 UUID uuid = UUID.fromString(s);
				 String message = Main.announcements.getConfig().getString("ads."+s+".message");
				 String url = null;
				 if ( Main.announcements.getConfig().contains("ads."+s+".url")) {
					  url = Main.announcements.getConfig().getString("ads."+s+".url");
				 }
				 Ad ad = new Ad(uuid, message, url);
				 this.ads.add(ad);
			 }
		 } 
	 }

	public void time() {
		this.announcementTime++;
		if (this.announcementTime>=10) {
			this.announcementTime= 0;
			sendAnnouncement();
		}
		
		this.adTime++;
		if (this.adTime>=8) {
			this.adTime = 0;
			sendAd();
		}
		
	}
	 
	 
	 

}
