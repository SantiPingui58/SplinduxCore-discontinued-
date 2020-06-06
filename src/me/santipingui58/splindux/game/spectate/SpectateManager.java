package me.santipingui58.splindux.game.spectate;


import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.translate.Main;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;

public class SpectateManager {

	private static SpectateManager manager;	
	 public static SpectateManager getManager() {
	        if (manager == null)
	        	manager = new SpectateManager();
	        return manager;
	    }
	 
	
	public void spectate(SpleefPlayer sp, SpleefPlayer spectate) {
		for (SpleefPlayer playing : spectate.getArena().getPlayers()) {
		playing.getPlayer().hidePlayer(Main.get(), sp.getPlayer());		
		sendKeepInTABPacket(playing.getPlayer(),sp.getPlayer());
		}
		sp.getPlayer().teleport(spectate.getPlayer().getLocation());
		sp.getPlayer().setAllowFlight(true);
		sp.getPlayer().getInventory().clear();
		

		
	}
	
	public void leaveSpectate(SpleefPlayer sp) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(Main.get(),sp.getPlayer());
		}
	}
	
	
	private void sendKeepInTABPacket(Player player, Player toShow) {
		EntityPlayer[] entity = { (EntityPlayer) ((CraftPlayer) toShow).getHandle()};
 		((CraftPlayer) player).getHandle().playerConnection.sendPacket( new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entity));
	}
	
}
