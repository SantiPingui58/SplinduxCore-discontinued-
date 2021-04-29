package me.santipingui58.splindux.cosmetics.helmets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import me.santipingui58.splindux.Main;

public class HelmetManager {
	private static HelmetManager manager;	
	
	 public static HelmetManager getManager() {
	        if (manager == null)
	        	manager = new HelmetManager();
	        return manager;
	    }
	 
	 private List<Helmet> helmets = new ArrayList<Helmet>();
	 
	 
	 public void loadHelmets() {
		 FileConfiguration config = Main.helmets.getConfig();
		 if (Main.helmets.getConfig().contains("helmets")) {
			 Set<String> helmets = Main.helmets.getConfig().getConfigurationSection("helmets").getKeys(false);
			 for (String h : helmets) {
				 ChatColor color = ChatColor.valueOf(config.getString("helmets."+h+".color"));
				 Material material = Material.valueOf(config.getString("helmets."+h+".material"));
				 int price = config.getInt("helmets."+h+".price");
				 byte id = (byte) config.getInt("helmets."+h+".id");
				 int permsLevel = 0;
				 if (config.contains("helmets."+h+".vip")) permsLevel =1;
				 if (config.contains("helmets."+h+".epic")) permsLevel =2;
				 if (config.contains("helmets."+h+".extreme")) permsLevel =3;
				 
				 boolean bold = false;
				 if (config.contains("helmets."+h+".bold")) bold =true;
				 
				 boolean enchanted = false;
				 if (config.contains("helmets."+h+".enchanted")) enchanted =true;
				 
				 String perm = null;
				 if (config.contains("helmets."+h+".perm")) perm =config.getString("helmets."+h+".perm");
				 
				 String name = color + h.replace("_", " ");
				 
				 Helmet helmet = new Helmet(h,name,permsLevel,price,material,id,perm,bold,enchanted);
				 this.helmets.add(helmet);
			 }
		 }
	 }
	 
	 
	 public List<Helmet> getHelmets() {
		 return this.helmets;
	 }
	 
	 public Helmet getHelmetByName(String name) {
		 for (Helmet helmet : this.helmets) {
			 if (helmet.getId().equalsIgnoreCase(name)) {
				 return helmet;
			 }
		 }
		 return null;
	 }
	 
}
