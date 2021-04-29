package me.santipingui58.splindux.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.vote.Rewarded;
import me.santipingui58.splindux.vote.VoteManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;
import net.archangel99.dailyrewards.DailyRewards;
import net.archangel99.dailyrewards.cfg.Config;
import net.archangel99.dailyrewards.manager.objects.DUser;

public class VotesMenu extends MenuBuilder {

	
	public VotesMenu(SpleefPlayer sp) {
		super("§e§lRewards and Votes",5);
		
		new BukkitRunnable() {
		public void run() {
			
		DUser duser = DailyRewards.getInstance().getUserManager().getOrLoadUser(sp.getPlayer());
		if (duser.hasActiveReward()) {
		s(12,new ItemBuilder(Material.GOLD_BLOCK).setTitle("§eDaily Rewards").addLore("§aYou have unclaimed rewards!").build());
		} else {
			s(12,new ItemBuilder(Material.COAL_BLOCK).setTitle("§eDaily Rewards").addLore("§7You don't have any unclaimed rewards.").build());
		}
		
		VoteManager vm = VoteManager.getManager();
		for (Rewarded network : Rewarded.values()) {
			s(network.getSlotInMenu(), vm.getItem(sp, network));
		}
		 
		new BukkitRunnable() {
		public void run() {
			buildInventory();
		}
		}.runTask(Main.get());
		
		}
		}.runTaskAsynchronously(Main.get());
		
	}


	
	
	
	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		if (stack.getType().equals(Material.BARRIER)) return;
		if (slot==12) {
		Config.rewards_gui.open(sp.getPlayer());
		} else  {
			Rewarded rewarded = Rewarded.getBySlot(slot);
			if (!TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, TimeLimitType.fromRewarded(rewarded))) {
				VoteManager.getManager().sendInstructions(sp, rewarded);	
				sp.getPlayer().closeInventory();
			}
		}
	}
	

	
	}



