package me.santipingui58.splindux.scoreboard.hologram;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.Main;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;

public class HologramManager {

	private static HologramManager manager;	
	 public static HologramManager getManager() {
	        if (manager == null)
	        	manager = new HologramManager();
	        return manager;
	    }
	
	 private List<Hologram> holograms = new ArrayList<Hologram>();
	 
	 public List<Hologram> getHolograms() {
		 return this.holograms;
	 }
	 
	 public void loadHolograms() {
		 if (Main.arenas.getConfig().contains("holograms")) {
		 Set<String> holograms = Main.arenas.getConfig().getConfigurationSection("holograms").getKeys(false);
	 }
		 }
	 
	public void spawn(Location loc, Player p) {
        WorldServer s = ((CraftWorld)loc.getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(s);
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName("I'm a Armorstand!");
        stand.setCustomNameVisible(true);
        stand.setNoGravity(true);
       
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }
}
