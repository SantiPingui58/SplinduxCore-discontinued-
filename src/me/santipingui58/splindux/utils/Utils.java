package me.santipingui58.splindux.utils;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import net.minecraft.server.v1_12_R1.EntityFishingHook;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;


public class Utils {
	
	private static Utils manager;	
	 public static Utils getUtils() {
	        if (manager == null)
	        	manager = new Utils();
	        return manager;
	    }
	 
	 public String time(int s) {
			
			int minutes = s / 60;
			int seconds = s % 60;

			return String.format("%02d:%02d",  minutes, seconds);
		  }
	 
	 public ItemStack getSkull(OfflinePlayer off) {
		 ItemStack leader = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
			SkullMeta meta = (SkullMeta) leader.getItemMeta();
			meta.setOwningPlayer(off);
			leader.setItemMeta(meta);
			return leader;
	 }
	 
	 public ItemStack getSkull(OfflinePlayer off,String string) {
		 ItemStack leader = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
			SkullMeta meta = (SkullMeta) leader.getItemMeta();
			meta.setOwningPlayer(off);
			meta.setDisplayName(string);
			leader.setItemMeta(meta);
			return leader;
	 }
	 
	 
	 public List<Block> getNearbyBlocks(Location loc, int radius) {
		    ArrayList<Block> blocks = new ArrayList<Block>();
		   
		    for (int x = (loc.getBlockX()-radius); x <= (loc.getBlockX()+radius); x++) {
		            for (int z = (loc.getBlockZ()-radius); z <= (loc.getBlockZ()+radius); z++) {
		                Location l = new Location(loc.getWorld(), x, loc.getBlockY(), z);
		                if (!l.getBlock().getType().equals(Material.AIR)) {
		                    blocks.add(l.getBlock());      
		                }
		            }
		        
		    }
		    return blocks;
	 }
	
	 public List<Block> getBlockBelow(Player player) {
		    Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		   return getNearbyBlocks(block.getLocation(),1);

	 }
	 
	 public void sendTitles(List<SpleefPlayer> players, String titletext,String subtitletext,int fadeIn, int stay, int fadeOut) {
		 new BukkitRunnable() {
			 public void run () {
				 for (SpleefPlayer sp : players) {
					 Player player = sp.getPlayer();
			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + titletext + "\"}");
			IChatBaseComponent chatSubtitle = ChatSerializer.a("{\"text\": \"" + subtitletext + "\"}");
			
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
			PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubtitle);
			
			PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn,stay,fadeOut);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
				 }
		 }
	 }.runTask(Main.get());
	}
	 
	 public void sendTitles(Collection<? extends Player> players, String titletext,String subtitletext,int fadeIn, int stay, int fadeOut) {
		List<SpleefPlayer> spleefPlayers = new ArrayList<SpleefPlayer>();
		for (Player p : players) spleefPlayers.add(SpleefPlayer.getSpleefPlayer(p));
		sendTitles(spleefPlayers,titletext,subtitletext,fadeIn,stay,fadeOut);
	 }
	 
	 
	 public void sendBroadcastTitle(String titletext,String subtitletext,int fadeIn, int stay, int fadeOut) {
		 new BukkitRunnable() {
			 public void run () {
				 for (Player player : Bukkit.getOnlinePlayers()) {
			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + titletext + "\"}");
			IChatBaseComponent chatSubtitle = ChatSerializer.a("{\"text\": \"" + subtitletext + "\"}");
			
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
			PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubtitle);
			
			PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn,stay,fadeOut);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
				 }
		 }
	 }.runTask(Main.get());
	}
	 
	 public void sendTitles(SpleefPlayer sp, String titletext,String subtitletext,int fadeIn, int stay, int fadeOut) {
		 new BukkitRunnable() {
			 public void run () {
				 
					 Player player = sp.getPlayer();
			IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + titletext + "\"}");
			IChatBaseComponent chatSubtitle = ChatSerializer.a("{\"text\": \"" + subtitletext + "\"}");
			
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
			PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubtitle);
			
			PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn,stay,fadeOut);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
				 
		 }
	 }.runTask(Main.get());
	}
	 
	 //Put the specific title and lore to an item
	 public ItemStack doTitleAndLore(ItemStack item,String title, List<String> lore) {
		 ItemMeta meta = item.getItemMeta();
		 meta.setDisplayName(title);
		 meta.setLore(lore);
		 return item;
	 }
	 
	 
	 //Create an tipped arrow based on its PotionType and amount. Used for Mutation Tokens GUI
	 public ItemStack getTippedArrow(PotionType type, int amount) {
		 ItemStack item = new ItemStack(Material.TIPPED_ARROW);
		 PotionMeta meta = (PotionMeta) item.getItemMeta();
		 meta.setBasePotionData(new PotionData(type));
		 item.setItemMeta(meta);
		 item.setAmount(amount);
		 return item;
	 }
	 
	 
	 public SpleefPlayer getFromSet(Collection<SpleefPlayer> collection) {
		 for (SpleefPlayer s : collection) {
			 return s;
		 }
		return null;
	 }

		public <T> Set<T> newShuffledSet(Collection<T> collection) {
		    List<T> shuffleMe = new ArrayList<T>(collection);
		    Collections.shuffle(shuffleMe);
		    return new LinkedHashSet<T>(shuffleMe);
		}
		
		
	 
	 //Check if a set has duplicate values on it
	 public <T> boolean hasDuplicate(Iterable<T> all) {
		    Set<T> set = new HashSet<T>();
		    // Set#add returns false if the set does not change, which
		    // indicates that a duplicate element has been added.
		    for (T each: all) if (!set.add(each)) return true;
		    return false;
		}
	 
	 
	 
	 //Gets nearest player to another player. Used for FFA Kills system
	 public SpleefPlayer getNearestPlayer(SpleefPlayer sp) {
		 SpleefPlayer nearest = null;
		 for (SpleefPlayer online : DataManager.getManager().getPlayers()) {
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
	 
	 
	 //Useful to send a list of names from a list of SpleefPlayer
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
	
	public String getNamesFromList(List<String> list) {
		String p = "";
		for (String s : list) {
		if (p.equalsIgnoreCase("")) {
			p = s;
		} else {
			p = p+", " + s;
		}
		}
		return p;
	}
	 
	//Method to save a Location in a string.
	// pitch is used to save or not the direction the player looks, for example, to teleport to an spawn.
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
	  
	  //Return the difference between 2 dates, in miliseconds
	  public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		    long diffInMillies = date2.getTime() - date1.getTime();
		    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
		}
	  
	  //Check if a list contains the String b, with ignore case
	  public boolean containsIgnoreCase(List<String> list, String b) {
		  
		  for (String o : list) {
			  if (containsIgnoreCase(o,b)) {
				  return true;
			  }
		  }
		return false;
		  
	  }
	  
	  //Check if the String fullStr has inside of it the String serachStr, with ignore case
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
	  
	  
	  //Get center of a location
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
		
		
		
		//Method to generate a list of Locations that look like a circle. Used to teleport players in a round on FFA
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
		
		
		  
		public void cylinder(Location loc, Material mat, Material mask,int r) {
		    int cx = loc.getBlockX();
		    int cy = loc.getBlockY();
		    int cz = loc.getBlockZ();
		    World w = loc.getWorld();
		    int rSquared = r * r;
		    for (int x = cx - r; x <= cx +r; x++) {
		        for (int z = cz - r; z <= cz +r; z++) {
		            if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
		            	if (w.getBlockAt(x,cy,z).getType().equals(mask)) {
		                w.getBlockAt(x, cy, z).setType(mat);
		            	}
		            }
		        }
		    }
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
		
		
		
		
		public void setBiteTime(FishHook hook, int time) {
		   EntityFishingHook hookCopy = (EntityFishingHook) ((CraftEntity) hook).getHandle();

		    Field fishCatchTime = null;

		    try {
		        fishCatchTime = EntityFishingHook.class.getDeclaredField("h");
		    } catch (NoSuchFieldException e) {
		        e.printStackTrace();
		    } catch (SecurityException e) {
		        e.printStackTrace();
		    }

		    fishCatchTime.setAccessible(true);

		    try {
		        fishCatchTime.setInt(hookCopy, time);
		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    }

		    fishCatchTime.setAccessible(false);
		}
		
		
		public  Vector rotateVectorAroundY(Vector vector, double degrees) {
		    double rad = Math.toRadians(degrees);
		   
		    double currentX = vector.getX();
		    double currentZ = vector.getZ();
		   
		    double cosine = Math.cos(rad);
		    double sine = Math.sin(rad);
		   
		    return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
		}
		
		
		
		//Minecraft saves all skins, even if they aren't being used by a player. That way we can get custom heads with a weird code (Base64). This method converts Base64 urls to the Head Item.
		//It was used before for the ranking per countries.
		public ItemStack getSkull(String url,String title) {
	        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
	        if(url.isEmpty())return head;
	       
	       
	        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
	        headMeta.setDisplayName(title);
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
		
		
		
		//Converts seconds to days,hours,minutes and seconds. Used in the /stats command
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
		 
		 
		//Converts seconds to years,months,weeks days, and hours. Used in the Online time Ranking
		public String minutesToDate(int i) {
			
			i = i/60;
			return i+" hours";
			
			/*int years =  i / 525600;
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
			
		*/		  
		 }
		
	
		public int getIDByChatColor(ChatColor c) {
			switch(c) {
			case WHITE: return 0;
			case GOLD: return 1;
			case AQUA: return 3;
			case BLACK: return 15;
			case DARK_AQUA: return 9;
			case DARK_BLUE: return 11;
			case DARK_GRAY: return 7;
			case DARK_GREEN: return 13;
			case DARK_PURPLE: return 10;
			case RED: return 14;
			case GRAY: return 8;
			case GREEN: return 5;
			case LIGHT_PURPLE: return 6;
			case YELLOW: return 4;
			default: break;
			}
			return 16;
		}

		public List<Location> getRadioBlocks(SpleefPlayer p, int radius) {
			Location standing = p.getPlayer().getLocation().clone().subtract(0,1,0);
		    return getRadioBlocks(standing,radius);
		}
		public List<Location> getRadioBlocks(Location l, int radius) {
			Location standing = l;
		    List<Location> locations = new ArrayList<Location>();
		    locations.add(standing);
		    if (radius==0) return locations;
	
		    Location north = standing.clone().add(1,0,0);
		    Location south = standing.clone().add(-1,0,0);
		    Location west = standing.clone().add(0,0,1);
		    Location east = standing.clone().add(0,0,-1);	    
		    locations.add(north);
		    locations.add(south);
		    locations.add(west);
		    locations.add(east);
		    List<Location> list = new ArrayList<Location>();
		    for (Location li : locations) {
		    	if (!li.getBlock().getType().equals(Material.AIR)) {
		    		list.add(li);
		    	}
		    }
	        return list;
		}
	  
		public String getStringMoney(int money) {
			
			if (money>=1000000) {
				double i = (double) money/ (double) 1000000;
				String d =  new DecimalFormat("#.##").format(i);
				return d+"M";
			} else if (money >=1000){
				double i = (double)money/(double) 1000;
				String d =  new DecimalFormat("#.##").format(i);
				return d+"K";
			}else  {
				return String.valueOf(money);
			}
		}
		
}


