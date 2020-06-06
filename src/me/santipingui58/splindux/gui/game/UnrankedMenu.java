package me.santipingui58.splindux.gui.game;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;



public class UnrankedMenu extends MenuBuilder {
	
	
	public UnrankedMenu(SpleefPlayer sp) {
		super("§f§lUnranked Menu",3);
	
		s(11, new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§b§lSpleef Duels")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,0,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,0,2))
				.build());
		
		s(13,new ItemBuilder(Material.SNOW_BLOCK).setTitle("§b§lSpleef FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.FFA,0,0))
				.build());
		
		s(15, new ItemBuilder(Material.EGG).setTitle("§e§lSplegg Duels")
				.addLore("§7Coming soon")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,0,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,0,2))
				.build());
		
	}
	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		if (slot==11) {
			new SpleefDuelsMenu(sp).o(sp.getPlayer());
		} else if (slot==13) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getGameType().equals(GameType.FFA)) {
					arena.addFFAQueue(sp);		
					return;
					}
			}	
		} else if (slot==15) {
			new SpleggDuelsMenu(sp).o(sp.getPlayer());
		}
	}
	
	
	}



