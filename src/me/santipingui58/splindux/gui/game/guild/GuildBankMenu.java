package me.santipingui58.splindux.gui.game.guild;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.ItemBuilder;



public class GuildBankMenu extends MenuBuilder {
	
	
	public GuildBankMenu(SpleefPlayer sp,Guild guild) {
		super("§a§lGuild Bank" ,4);
		
		new BukkitRunnable() {
		public void run() {
			
		s(20, new ItemBuilder(Material.EXP_BOTTLE).setTitle("§e§lDeposit Coins").addLore("§7To deposit coins to your Guild use the command §b/guild deposit <Amount>").build());
		List<String> earnings = new ArrayList<String>();
		earnings.addAll(guild.getEarnings());
		java.util.Collections.reverse(earnings);
		s(21, new ItemBuilder(Material.GOLD_INGOT).setTitle("§a§lLatest Earnings").addLores(earnings).build());
		List<String> losses = new ArrayList<String>();
		losses.addAll(guild.getLoss());
		java.util.Collections.reverse(losses);
		s(23, new ItemBuilder(Material.REDSTONE).setTitle("§c§lLatest Losses").addLores(losses).build());
		
		s(13, new ItemBuilder(Material.EMERALD_BLOCK).setTitle("§6§l" + guild.getCoins() + " Coins").build());
		s(24,new ItemBuilder(Material.PAPER).setTitle("§f§lGuild Bank Info")
				.addLore("§7Expected Monthly Revenue: §a" + guild.getExpectedGains() + " Coins")
				.addLore("§7Expected Monthly Loss: §c" + guild.getExpectedLoss() + " Coins")
				.addLore("")
				.addLore("§aDaily Players Salary: §6" + guild.getDailyPlayersSalary() + " Coins")
				.addLore("§aDaily Leader Revenue: §6" + guild.getDailyLeaderRevenue() + " Coins")
				.build());
		
		s(35, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
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
		Player p = sp.getPlayer();
		Guild guild = GuildsManager.getManager().getGuild(sp);
		if (guild==null) {
			p.closeInventory();
			return;
		}
		
		if (slot==35) {
			new GuildMainMenu(sp,guild).o(p);
		} 
	}
	
	}



