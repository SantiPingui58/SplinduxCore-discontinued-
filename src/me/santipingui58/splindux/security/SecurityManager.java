package me.santipingui58.splindux.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;

public class SecurityManager {

	private static SecurityManager manager;	
	 public static SecurityManager getManager() {
	        if (manager == null)
	        	manager = new SecurityManager();
	        return manager;
	    }
	 
	 //This Manager handles an incomplete system for admins to have to register and login in Spigot Server, if somehow we get UUID Spoofed.
	 //Not being used
	 
	public void adminLogin(SpleefPlayer sp) {
		boolean a = true;
		if (a) {
		} else {
		
		if (sp.getPlayer().isOp() || sp.getPlayer().hasPermission("*")) {			
			if (!Main.config.getConfig().contains("passwords."+sp.getPlayer().getUniqueId())) {
				sp.needsToRegister();	
				sp.needsAdminLogin();
				new BukkitRunnable() {
					public void run() {
						sp.getPlayer().sendMessage("§c§l[SplinduxLogin] §cTo join the server you need to register with: §b/splinduxregister <password>");
						sp.getPlayer().sendMessage("§7(Remember to make a different password from the §9[Auth] §7one.)");				
					}
				}.runTaskLater(Main.get(), 80L);
				
			} else {		
			Date now = new Date();
			if (sp.getLastLogin()==null) {
				sp.needsAdminLogin();		
				new BukkitRunnable() {
					public void run() {
						sp.getPlayer().sendMessage("§c§l[SplinduxLogin] §cTo join the server you need login with: §b/splinduxlogin <password>");
					}
				}.runTaskLater(Main.get(), 80L);
				
			}	else {		
			if (Utils.getUtils().getDateDiff(now, sp.getLastLogin(), TimeUnit.MINUTES) >=60) {
				sp.needsAdminLogin();
				new BukkitRunnable() {
					public void run() {
						sp.getPlayer().sendMessage("§c§l[SplinduxLogin] §cTo join the server you need login with: §b/splinduxlogin <password>");
					}
				}.runTaskLater(Main.get(), 80L);
			}		
			}
			}
		}
	}
	}
	
		public String encryptThisString(String input) 
		{ 
			try { 
				// getInstance() method is called with algorithm SHA-512 
				MessageDigest md = MessageDigest.getInstance("SHA-512"); 

				// digest() method is called 
				// to calculate message digest of the input string 
				// returned as array of byte 
				byte[] messageDigest = md.digest(input.getBytes()); 

				// Convert byte array into signum representation 
				BigInteger no = new BigInteger(1, messageDigest); 

				// Convert message digest into hex value 
				String hashtext = no.toString(16); 

				// Add preceding 0s to make it 32 bit 
				while (hashtext.length() < 32) { 
					hashtext = "0" + hashtext; 
				} 

				// return the HashText 
				return hashtext; 
			} 

			// For specifying wrong message digest algorithms 
			catch (NoSuchAlgorithmException e) { 
				throw new RuntimeException(e); 
			} 
		} 


	}
