package me.santipingui58.splindux.gui.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;



public class RankedMenu extends MenuBuilder {
	
	
	public RankedMenu(SpleefPlayer sp) {
		super("§6§lRanked Spleef",3);
	
		s(11, new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§b§lRanked Spleef 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,1,1))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,1,1))
				.build());
		
		s(15, new ItemBuilder(Material.EGG).setTitle("§cRanked Splegg 1v1")
				.addLore("§7Coming soon")
				.build());
		
	}
	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		if (slot==11) {
			List<SpleefPlayer> list= new ArrayList<SpleefPlayer>();
			list.add(sp);
			RankedManager.getManager().getRankedQueue(SpleefType.SPLEEF,1).joinQueue(list);		
			sp.getPlayer().closeInventory();
		} else {
			sp.getPlayer().closeInventory();
			sp.getPlayer().sendMessage("§cComing soon...");
		}
	}
	
	
	}



