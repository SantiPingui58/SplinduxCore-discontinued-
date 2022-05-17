package me.santipingui58.splindux.sws;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
public class SWSManager {
	
	private static SWSManager manager;	
	 public static SWSManager getManager() {
	        if (manager == null)
	        	manager = new SWSManager();
	        return manager;
	    }
	 
	private Set<SWSCountry> countries = new HashSet<SWSCountry>();
	
	private static int REQUIRED_POINTS = 3000;
	
	public Set<SWSCountry> getCountries() {
		return this.countries;
	}
	
	public void addPoints(SpleefPlayer sp,int i, boolean sendMessage,boolean multiplier) {
		if (!Main.swws) return;
	SWSCountry country = getCountry(sp.getCountry());
		int oldPoints = country.getRanking().containsKey(sp.getUUID()) ? country.getRanking().get(sp.getUUID()) : 0;
	
		if (multiplier && sp.getPlayer().hasPermission("splindux.vip")) {
			i=(int) (i*1.5);
		}
		
		int ii = i;
		new BukkitRunnable() {
			public void run() {
				sp.sendMessage("§5§l[SWS] §6You have won §3§l" + ii + " SplinduxPoints!");
			}
		}.runTaskLater(Main.get(), 10L);
		
		if (oldPoints<REQUIRED_POINTS  && oldPoints+i >=3000) {
			Bukkit.broadcastMessage("§5§l[SWS] §d§lCongratulations to §6§l" + sp.getOfflinePlayer().getName() + " §d§lfor qualifying to the SWS for the country " 
		+DataManager.getManager().getCountryString(sp).toUpperCase()+"§d§l!");
			String text ="Congratulations to **" + sp.getOfflinePlayer().getName() + "** for qualifying to the SWS for the country **" 
					+":flag_"+sp.getCountry().toLowerCase()+":"+DataManager.getManager().getCountryString(sp).toUpperCase()+" :flag_"+sp.getCountry().toLowerCase()+":**!";
			
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #sws-logs "+text);
		}
		country.getRanking().put(sp.getUUID(), oldPoints+i);
		
	}
	
	public int getPoints(SpleefPlayer sp) {
		SWSCountry country = getCountry(sp.getCountry());
		if (!country.getRanking().containsKey(sp.getUUID())) return 0;
		return country.getRanking().get(sp.getUUID());
	}
	
	public SWSCountry getCountry(String code) {
		
		for (SWSCountry country : this.countries) {
			if (country.getCode().equalsIgnoreCase(code)) {
				return country;
			}
		}
		
		return createCountry(code);
	}
	
	
	public void loadSWS() {
		if (!Main.swws) return;
		new BukkitRunnable() {
			public void run() {
				 FileConfiguration config = Main.countries.getConfig();
				 if (config.contains("countries")) {
					 Set<String> paises = config.getConfigurationSection("countries").getKeys(false);
					 for (String c : paises) {
						SWSCountry country = new SWSCountry(c);
						country.setSWCPlayOffsAmount(config.getInt("countries."+c+".swc_playoffs"));
						country.setContinentalPlayOffsAmount(config.getInt("countries."+c+".continental_playoffs"));
						country.nationalLeague(config.getBoolean("countries."+c+".has_nationalleague"));
						countries.add(country);
					 }
					 }
				 
				 for (SWSCountry country : countries) {
				country.setRanking(HikariAPI.getManager().getSWSData(country.getCode()));
				 }
			}
		}.runTaskAsynchronously(Main.get());
		
		
		
	}
	
	public void saveCountries() {
		if (!Main.swws) return;
		 FileConfiguration config = Main.countries.getConfig();
		for (SWSCountry country : this.countries) {
		config.set("countries."+country.getCode()+".swc_playoffs", country.getSWCPlayOffsAmount());	
		config.set("countries."+country.getCode()+".continental_playoffs", country.getContinentalPlayOffsAmount());	
		config.set("countries."+country.getCode()+".has_nationalleague", country.hasNationalLeague());	
		}
		Main.countries.saveConfig();
	}
	
	public SWSCountry createCountry(String code) {
		SWSCountry country = new SWSCountry(code);			
		this.countries.add(country);
		saveCountries();
		return country;
	}
	
	
	public void updateRankings() {
		if (!Main.swws) return;
		saveData();
		for (SWSCountry country : this.countries) {
			country.updateRanking();
		}
	}
	
	public void saveData() {
		if (!Main.swws) return;
		for (SWSCountry country : this.countries) {
			HikariAPI.getManager().updateSWWSData(country.getCode(), country.getRanking());
		}
	}
	
	
	public int getPlayerQualificationState(SpleefPlayer sp) {
		SWSCountry country = getCountry(sp.getCountry());
		if (sp.getSWSState()==0) {
			if (getPoints(sp)>=REQUIRED_POINTS) {
				int pos = country.getPos(sp);
				if (pos<= country.getSWCPlayOffsAmount()) {
					sp.setSWSState(4);
				} else if (country.getContinentalPlayOffsAmount()>0 && pos<=country.getContinentalPlayOffsAmount()) {
					sp.setSWSState(3);
				} else if (country.hasNationalLeague()) {
					sp.setSWSState(2);
				} else {
					sp.setSWSState(1);
				}
			} else {
				sp.setSWSState(1);
			}
		} 
			return sp.getSWSState();
		
	}
	
	
}
