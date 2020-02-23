package me.santipingui58.splindux.scoreboard.hologram;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
