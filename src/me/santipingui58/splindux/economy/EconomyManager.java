package me.santipingui58.splindux.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;
import com.yapzhenyie.GadgetsMenu.economy.GEconomyProvider;
import com.yapzhenyie.GadgetsMenu.player.OfflinePlayerManager;
import com.yapzhenyie.GadgetsMenu.player.PlayerManager;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class EconomyManager extends GEconomyProvider {

	


	public EconomyManager(Plugin plugin, String storage) {
		super(plugin, storage);
	}

	
	private static EconomyManager pm;
	
	
	  public static EconomyManager getManager() {
	        if (pm == null)
	            pm = new EconomyManager(Main.get(), "Splindux");

	        return pm;
	    }


	
	
	  public void addCoins(SpleefPlayer sp, Integer i,boolean multiplier) {
	  
		  int coins = sp.getCoins();
			if (multiplier) {
			if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.extreme")) {
				i = (int) ((i*1.5));
			} else if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.epic")) {
				i = (int) ((i*1.25));
			} else if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.vip")) {
				i = (int) ((i*1.1));
			} 
			}
			coins = coins+i;
			sp.setCoins(coins);

			if (Bukkit.getOnlinePlayers().contains(sp.getOfflinePlayer())) {
				sp.getPlayer().sendMessage("§aYou have won §6"+i+" coins");
			}
	  } 
		
		
		
	//}

	  public PlayerManager getPlayerManager(Player player) {
		    PlayerManager playerManager = GadgetsMenuAPI.getPlayerManager(player);
			return playerManager;
		}
	  
	  
	@Override
	public void addMysteryDust(OfflinePlayerManager pm, int i) {
		
		OfflinePlayer p = pm.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		sp.setCoins(sp.getCoins()+i);
	}

	@Override
	public int getMysteryDust(OfflinePlayerManager pm) {
		OfflinePlayer p = pm.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		return sp.getCoins();
	}

	@Override
	public void removeMysteryDust(OfflinePlayerManager pm, int i) {		
		OfflinePlayer p = pm.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		sp.setCoins(sp.getCoins()-i);		
	}

	@Override
	public void setMysteryDust(OfflinePlayerManager arg0, int arg1) {
		
	}

	@Override
	public int syncMysteryDust(OfflinePlayerManager arg0) {
		return 0;
	}




	
	
	
	
	
}