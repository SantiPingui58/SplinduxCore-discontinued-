package me.santipingui58.splindux.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.SpleefPlayer;


public class Utils {
	
	private static Utils manager;	
	 public static Utils getUtils() {
	        if (manager == null)
	        	manager = new Utils();
	        return manager;
	    }
	

	 public <T> boolean hasDuplicate(Iterable<T> all) {
		    Set<T> set = new HashSet<T>();
		    // Set#add returns false if the set does not change, which
		    // indicates that a duplicate element has been added.
		    for (T each: all) if (!set.add(each)) return true;
		    return false;
		}
	 
	 
	 public SpleefPlayer getNearestPlayer(SpleefPlayer sp) {
		 SpleefPlayer nearest = null;
		 for (SpleefPlayer online : DataManager.getManager().getOnlinePlayers()) {
			 if (online.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(sp.getPlayer().getWorld().getName())) {
				 if (nearest==null) {
					 nearest = online;
				 } else {
					 if (online.getPlayer().getLocation().distance(sp.getPlayer().getLocation()) >
					 nearest.getPlayer().getLocation().distance(sp.getPlayer().getLocation())) {
						 nearest = online;
					 }
						 
				 }
			 }
		 }
		 
		 return nearest;
	 }
	 
	 
	public String getPlayerNamesFromList(List<SpleefPlayer> list) {		 
		 String p = "";
		 for (SpleefPlayer sp : list) {
			if(p.equalsIgnoreCase("")) {
			p = sp.getOfflinePlayer().getName();	
			}  else {
				p = p+", " + sp.getOfflinePlayer().getName();
			}
		 }
		 
		return p;
	}
	 
	  public  String setLoc(Location loc, boolean pitch)
	  {
	    if (pitch) {
	      return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getYaw() + "," + loc.getPitch();
	    }
	    return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
	  }
	  
	  public  List<String> setLocs(List<Location> locs) {
		  List<String> list = new ArrayList<String>();
			  for (Location loc : locs) {
		      list.add(loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
		    }
			  return list;
	  }

	  public  Location getLoc(String path, boolean pitch)
	  {
	    Location loc = null;
	    if (!pitch) {
	      String[] locs = path.split(",");
	      loc = new Location(Bukkit.getWorld(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]), Integer.parseInt(locs[3]));
	      loc.add(0.5D, 0.0D, 0.5D);
	      return loc;
	    }
	    String[] locs = path.split(",");
	    loc = new Location(Bukkit.getWorld(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]), Integer.parseInt(locs[3]), Float.valueOf(locs[4]).floatValue(), Float.valueOf(locs[5]).floatValue());
	    loc.add(0.5D, 0.0D, 0.5D);
	    return loc;
	  }

	  public  Location getLoc(String path)
	  {
	    String[] locs = path.split(",");

	    Location loc = new Location(Bukkit.getWorld(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]), Integer.parseInt(locs[3]));
	    loc.add(0.5D, 0.0D, 0.5D);
	    return loc;
	  }
	  
	  
	  public  void debug(String s) {
		  Player p = Bukkit.getPlayer("SantiPingui58");
		  if (Bukkit.getOnlinePlayers().contains(p))
		  p.sendMessage(s);
	  }
	  

	  public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		    long diffInMillies = date2.getTime() - date1.getTime();
		    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
		}
	  
	  
	  public boolean containsIgnoreCase(List<String> list, String b) {
		  
		  for (String o : list) {
			  if (containsIgnoreCase(o,b)) {
				  return true;
			  }
		  }
		return false;
		  
	  }
	  
	  
	  public boolean containsIgnoreCase(String fullStr, String searchStr)   {
		    if(fullStr == null || searchStr == null) return false;

		    final int length = searchStr.length();
		    if (length == 0)
		        return true;

		    for (int i = fullStr.length() - length; i >= 0; i--) {
		        if (fullStr.regionMatches(true, i, searchStr, 0, length))
		            return true;
		    }
		    return false;
		}
	  
	  public Location getCenter(Location loc) {
		    return new Location(loc.getWorld(),
		        getRelativeCoord(loc.getBlockX()),
		        getRelativeCoord(loc.getBlockY()),
		        getRelativeCoord(loc.getBlockZ()));
		}
		 
		private double getRelativeCoord(int i) {
		    double d = i;
		    d = d < 0 ? d - .5 : d + .5;
		    return d;
		}
		
		public String time(int s) {
			
			int minutes = s / 60;
			int seconds = s % 60;

			return String.format("%02d:%02d",  minutes, seconds);
		  }
		
		public List<Location> getCircle(Location center, double radius, int amount) {
		    List<Location> locations = new ArrayList<>();
		    World world = center.getWorld();
		    double increment = (2 * Math.PI) / amount;
		    for(int i = 0; i < amount; i++) {
		        double angle = i * increment;
		        double x = center.getX() + (radius * Math.cos(angle));
		        double z = center.getZ() + (radius * Math.sin(angle));
		        locations.add(new Location(world, x, center.getY(), z));
		    }
		    return locations;
		}
		
		public Location lookAt(Location loc, Location lookat) {
	        //Clone the loc to prevent applied changes to the input loc
	        loc = loc.clone();

	        // Values of change in distance (make it relative)
	        double dx = lookat.getX() - loc.getX();
	        double dy = lookat.getY() - loc.getY();
	        double dz = lookat.getZ() - loc.getZ();

	        // Set yaw
	        if (dx != 0) {
	            // Set yaw start value based on dx
	            if (dx < 0) {
	                loc.setYaw((float) (1.5 * Math.PI));
	            } else {
	                loc.setYaw((float) (0.5 * Math.PI));
	            }
	            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
	        } else if (dz < 0) {
	            loc.setYaw((float) Math.PI);
	        }

	        // Get the distance from dx/dz
	        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

	        // Set pitch
	        loc.setPitch((float) -Math.atan(dy / dxz));

	        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
	        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
	        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

	        return loc;
	    }
		
		public ItemStack getSkull(String url) {
	        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
	        if(url.isEmpty())return head;
	       
	       
	        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
	        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
	        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
	        Field profileField = null;
	        try {
	            profileField = headMeta.getClass().getDeclaredField("profile");
	            profileField.setAccessible(true);
	            profileField.set(headMeta, profile);
	        } catch (NoSuchFieldException e1) {
	            e1.printStackTrace();
	        } catch (IllegalArgumentException e1) {}
	        catch (IllegalAccessException e1) {}
	        head.setItemMeta(headMeta);
	        return head;
	    }
		
		
		 public String secondsToDate(int i) {	 
			 int days = (i % 604800) / 86400;
			 int hours = ((i % 604800) % 86400) / 3600;
			 int minutes = (((i % 604800) % 86400) % 3600) / 60;
			 int seconds = i % 60;
			if (days > 0) {
				return String.format("%01d %01dh %01dm %01ds", days, hours, minutes, seconds);
			} else if (hours > 0) {
				return String.format("%01dh %01dm %01ds", hours, minutes, seconds);
			} else if (minutes > 0) {
				return String.format("%01dm %01ds", minutes, seconds);
			} else {
				return String.format("%01ds", seconds);
			}
		 }
		 
		public String minutesToDate(int i) {
			int years =  i / 525600;
			int months = (i % 525600) / 43800;
			int weeks = ((i % 525600) % 43800) / 10080;
			int days = (((i % 525600) % 43800) % 10080) / 1140;
			int hours = ((((i % 525600) % 43800) % 10080) % 1140) / 60;
			 if (years > 0) {
				 return String.format("%01dyears %01dmonths %01dweeks %01ddays %01dhours", years, months, weeks, days, hours);
			 } else if (months > 0) {
				 return String.format("%01dmonths %01dweeks %01ddays %01dhours", months, weeks, days,hours);
			 } else if (weeks > 0) {
				 return String.format("%01dweeks %01ddays %01dhours",weeks, days,hours);
			 } else if (days > 0) {
				 return String.format("%01ddays %01dhours",days,hours);
			 } else if (hours > 0) {
				 return String.format("%01dhours",hours);
			 } else {
				 return "Less than an hour.";
			 }
			
				  
		 }
		
		
	  
}
