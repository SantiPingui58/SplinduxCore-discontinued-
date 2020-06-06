package me.santipingui58.splindux.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.ItemBuilder;

public class OptionsMenu extends MenuBuilder {

	
	public OptionsMenu(SpleefPlayer sp) {
		super("§c§lOptions",6);
		if (sp.getOptions().hasNightVision()) {
		s(10, new ItemBuilder(Material.LAPIS_BLOCK).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§9Night Vision §7- §aENABLED")
				.addLore("§bRight click to disable")
				.addLore("")
				.addLore("§7When enabled, you will gain")
				.addLore("§7permanent Night Vision effect")
				.addLore("§7while in game.").build());
		} else {
			s(10, new ItemBuilder(Material.LAPIS_BLOCK).setTitle("§9Night Vision §7- §cDISABLED")
					.addLore("§bRight click to enable")
					.addLore("")
					.addLore("§7When enabled, you will gain")
					.addLore("§7permanent Night Vision effect")
					.addLore("§7while in game.").build());
		}
		
		
		s(13, new ItemBuilder(Material.BOOKSHELF).setTitle("§eSelect Language").build());
		
		if(sp.getPlayer().hasPermission("splindux.vip")) {
		s(16, new ItemBuilder(Material.SIGN).setTitle("§bDefault Color Chat").build());
		} else {
			s(16, new ItemBuilder(Material.BARRIER).setTitle("§cDefault Color Chat")
					.addLore("§7You don't have permission")
					.addLore("§7to change this option.").build());
		}
		
		if (sp.getPlayer().hasPermission("splindux.vip")) {
			if (sp.getOptions().joinMessageEnabled()) {
			s(28, new ItemBuilder(Material.NETHER_STAR).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§3Join Server Broadcast §7- §aENABLED")
					.addLore("§bRight click to disable")
					.addLore("")
					.addLore("§7When enabled, a broadcast will be sent")
					.addLore("§7to all online players when you join the Server.").build());
			} else {
				s(28, new ItemBuilder(Material.NETHER_STAR).setTitle("§3Join Server Broadcast §7- §cDISABLED")
						.addLore("§bRight click to enable")
						.addLore("")
						.addLore("§7When enabled, a broadcast will be sent")
						.addLore("§7to all online players when you join the Server.").build());
			}
		} else {
			s(28, new ItemBuilder(Material.NETHER_STAR).setTitle("§cJoin Server Broadcast")
					.addLore("§7You don't have permission")
					.addLore("§7to change this option.").build());
		}
		
		
		if (sp.getOptions().hasTranslate()) {
		s(31, new ItemBuilder(Material.PAPER).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§fAutomatic Translate §7- §aENABLED")
				.addLore("§bRight click to disable")
				.addLore("")
				.addLore("§7When enabled, all messages in chat")
				.addLore("§7from players with a different language")
				.addLore("§7than yours, will be automatic translated to")
				.addLore("§7the language you have selected.").build());
		} else {
			s(31, new ItemBuilder(Material.PAPER).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§fAutomatic Translate §7- §cDISABLED")
					.addLore("§bRight click to enable")
					.addLore("")
					.addLore("§7When enabled, all messages in chat")
					.addLore("§7from players with a different language")
					.addLore("§7than yours, will be automatic translated to")
					.addLore("§7the language you have selected.").build());
		}
		
		if (sp.getPlayer().hasPermission("splindux.vip")) {
		if (sp.getOptions().hasAds()) {
			s(34, new ItemBuilder(Material.EMERALD).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§2Ads Broadcast §7- §aENABLED")
					.addLore("§bRight click to disable")
					.addLore("")
					.addLore("§7When enabled, ads will be sent")
					.addLore("§7to you every few minutes.").build());
			} else {
				s(34, new ItemBuilder(Material.EMERALD).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§2Ads Broadcast §7- §cDISABLED")
						.addLore("§bRight click to enable")
						.addLore("")
						.addLore("§7When enabled, ads will be sent")
						.addLore("§7to you every few minutes.").build());
			}
		} else {
			s(34, new ItemBuilder(Material.EMERALD).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§cAds Broadcast")
					.addLore("§7You don't have permission")
					.addLore("§7to change this option.").build());
		}
		
	}


	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
	if (slot==10) {
		sp.getOptions().nightVision(!sp.getOptions().hasNightVision());
		new OptionsMenu(sp).o(sp.getPlayer());
	} else if (slot==28&& sp.getPlayer().hasPermission("splindux.vip")) {
		sp.getOptions().joinMessage(!sp.getOptions().joinMessageEnabled());
		new OptionsMenu(sp).o(sp.getPlayer());
	} else if (slot==31) {
		sp.getOptions().translate(!sp.getOptions().hasTranslate());
		new OptionsMenu(sp).o(sp.getPlayer());
	} else if (slot==34 && sp.getPlayer().hasPermission("splindux.vip")) {
		sp.getOptions().ads(!sp.getOptions().hasAds());
		new OptionsMenu(sp).o(sp.getPlayer());
	}
	}
	
	
	}



