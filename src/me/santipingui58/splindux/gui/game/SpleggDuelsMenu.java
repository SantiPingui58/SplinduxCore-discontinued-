package me.santipingui58.splindux.gui.game;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;



public class SpleggDuelsMenu extends MenuBuilder {
	
	
	public SpleggDuelsMenu(SpleefPlayer sp) {
		super("§e§lSplegg Duels",3);
	
		s(10, new ItemBuilder(Material.EGG).setTitle("§a1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,1,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,1,2))
				.build());
		
		s(12, new ItemBuilder(Material.EGG).setTitle("§a2v2").setAmount(2)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,2,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,2,2))
				.build());
		
		s(14, new ItemBuilder(Material.EGG).setTitle("§a3v3").setAmount(3)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,3,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,3,2))
				.build());
		
		s(16, new ItemBuilder(Material.EGG).setTitle("§a+ 4v4").setAmount(4)
				.addLore("§7To play duels with more than 6 players")
				.addLore("§7you need to use the command")
				.addLore("§b/duel splegg <players>")
				.build());
		
		
	}
	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		if (slot==10) {
			GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.SPLEGG, false,null);
			sp.getPlayer().closeInventory();
		} else if (slot==12) {
			GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.SPLEGG, false,null);
			sp.getPlayer().closeInventory();
		} else if (slot==14) {
			GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.SPLEGG, false,null);
			sp.getPlayer().closeInventory();
		}
	}
	
	
	}



