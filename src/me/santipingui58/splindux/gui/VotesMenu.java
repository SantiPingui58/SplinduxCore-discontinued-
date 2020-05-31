package me.santipingui58.splindux.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.ItemBuilder;
import net.archangel99.dailyrewards.DailyRewards;
import net.archangel99.dailyrewards.cfg.Config;
import net.archangel99.dailyrewards.manager.objects.DUser;

public class VotesMenu extends MenuBuilder {

	
	public VotesMenu(SpleefPlayer sp) {
		super("§e§lRewards and Votes",5);
		s(11,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(12,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		DUser duser = DailyRewards.getInstance().getUserManager().getOrLoadUser(sp.getPlayer());
		if (duser.hasActiveReward()) {
		s(13,new ItemBuilder(Material.GOLD_BLOCK).setTitle("§eDaily Rewards").addLore("§aYou have unclaimed rewards!").build());
		} else {
			s(13,new ItemBuilder(Material.COAL_BLOCK).setTitle("§eDaily Rewards").addLore("§7You don't have any unclaimed rewards.").build());
		}
		s(14,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(15,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		
		s(20,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(21,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(22,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(23,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(24,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		
		
		s(30,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(31,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		s(32,new ItemBuilder(Material.BARRIER).setTitle("§cComing soon...").build());
		
	}


	@Override
	public void onClick(SpleefPlayer p, ItemStack stack, int slot) {
		if (slot==13) {
		Config.rewards_gui.open(p.getPlayer());
		}
	}
	
	
	}



