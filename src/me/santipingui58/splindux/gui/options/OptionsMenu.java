package me.santipingui58.splindux.gui.options;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;

public class OptionsMenu extends MenuBuilder {

	
	public OptionsMenu(SpleefPlayer sp) {
		super("§c§lOptions",5);
		
		
		new BukkitRunnable() {
		public void run() {
			
		if (sp.getOptions().hasNightVision()) {
		s(2, new ItemBuilder(Material.LAPIS_BLOCK).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§9Night Vision §7- §aENABLED")
				.addLore("§bRight click to disable")
				.addLore("")
				.addLore("§7When enabled, you will gain")
				.addLore("§7permanent Night Vision effect")
				.addLore("§7while in game.").build());
		} else {
			s(2, new ItemBuilder(Material.LAPIS_BLOCK).setTitle("§9Night Vision §7- §cDISABLED")
					.addLore("§bRight click to enable")
					.addLore("")
					.addLore("§7When enabled, you will gain")
					.addLore("§7permanent Night Vision effect")
					.addLore("§7while in game.").build());
		}
		
		
		s(4, new ItemBuilder(Material.BOOKSHELF).setTitle("§eSelect Language")
				.addLore("§7Select your language.").build());
		
		
		if(sp.getPlayer().hasPermission("splindux.epic")) {
		s(6, new ItemBuilder(Material.SIGN).setTitle("§bDefault Color Chat")
				.addLore("§7Select the color of your messages in chat.").build());
		} else {
			s(6, new ItemBuilder(Material.BARRIER).setTitle("§cDefault Color Chat")
					.addLore("§7You don't have permission")
					.addLore("§7to change this option.").build());
		}
		
		/*
		if(sp.getPlayer().hasPermission("splindux.vip")) {
			s(22, new ItemBuilder(Material.PAPER)
					.addLore("§7Select the map you want to play on")
					.addLore("§7a Ranked match. If a player joined a queue before")
					.addLore("§7you would have less chances of having your map picked.")
					.setTitle("§6Map for Ranked").build());
			
			} else {
				s(22, new ItemBuilder(Material.BARRIER).setTitle("§6Map for Ranked")
						.addLore("§7You don't have permission")
						.addLore("§7to change this option.").build());
			}
			*/
		
		if (sp.getPlayer().hasPermission("splindux.vip")) {
			if (sp.getOptions().joinMessageEnabled()) {
			s(20, new ItemBuilder(Material.NETHER_STAR).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§3Join Server Broadcast §7- §aENABLED")
					.addLore("§bRight click to disable")
					.addLore("")
					.addLore("§7When enabled, a broadcast will be sent")
					.addLore("§7to all online players when you join the Server.").build());
			} else {
				s(20, new ItemBuilder(Material.NETHER_STAR).setTitle("§3Join Server Broadcast §7- §cDISABLED")
						.addLore("§bRight click to enable")
						.addLore("")
						.addLore("§7When enabled, a broadcast will be sent")
						.addLore("§7to all online players when you join the Server.").build());
			}
		} else {
			s(20, new ItemBuilder(Material.NETHER_STAR).setTitle("§cJoin Server Broadcast")
					.addLore("§7You don't have permission")
					.addLore("§7to change this option.").build());
		}
		
		
		if (sp.getOptions().hasTranslate()) {
		s(22, new ItemBuilder(Material.BOOK_AND_QUILL).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§fAutomatic Translate §7- §aENABLED")
				.addLore("§bRight click to disable")
				.addLore("")
				.addLore("§7When enabled, all messages in chat")
				.addLore("§7from players with a different language")
				.addLore("§7than yours, will be automatic translated to")
				.addLore("§7the language you have selected.").build());
		} else {
			s(22, new ItemBuilder(Material.BOOK_AND_QUILL).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§fAutomatic Translate §7- §cDISABLED")
					.addLore("§bRight click to enable")
					.addLore("")
					.addLore("§7When enabled, all messages in chat")
					.addLore("§7from players with a different language")
					.addLore("§7than yours, will be automatic translated to")
					.addLore("§7the language you have selected.").build());
		}
		
		if (sp.getPlayer().hasPermission("splindux.vip")) {
		if (sp.getOptions().hasAds()) {
			s(24, new ItemBuilder(Material.EMERALD).addEnchantment(Enchantment. ARROW_DAMAGE, 1).setTitle("§2Ads Broadcast §7- §aENABLED")
					.addLore("§bRight click to disable")
					.addLore("")
					.addLore("§7When enabled, ads will be sent")
					.addLore("§7to you every few minutes.").build());
			} else {
				s(24, new ItemBuilder(Material.EMERALD).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§2Ads Broadcast §7- §cDISABLED")
						.addLore("§bRight click to enable")
						.addLore("")
						.addLore("§7When enabled, ads will be sent")
						.addLore("§7to you every few minutes.").build());
			}
		} else {
			s(24, new ItemBuilder(Material.EMERALD).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setTitle("§cAds Broadcast")
					.addLore("§7You don't have permission")
					.addLore("§7to change this option.").build());
		}
		
		
		if(sp.getOptions().getDuelPermission()==0) {
			s(38, new ItemBuilder(Material.DIAMOND_SWORD).setTitle("§bDuel Permission - §aEVERYONE")
					.addLore("§7Right click to toggle")
					.addLore("")
					.addLore("§7Determines who can send you a duel request.").build());
			} else if (sp.getOptions().getDuelPermission()==1) {
				s(38, new ItemBuilder(Material.DIAMOND_SWORD).setTitle("§bDuel Permission - §9ONLY FRIENDS")
						.addLore("§7Right click to toggle")
						.addLore("")
						.addLore("§7Determines who can send you a duel request.").build());
			} else {
				s(38, new ItemBuilder(Material.DIAMOND_SWORD).setTitle("§bDuel Permission - §cNO ONE")
						.addLore("§7Right click to toggle")
						.addLore("")
						.addLore("§7Determines who can send you a duel request.").build());
			}
		
		if(sp.getOptions().getDuelNotification()==0) {
			s(40, new ItemBuilder(Material.CHEST).setTitle("§6Duel Notification- §aEVERYONE")
					.addLore("§7Right click to toggle")
					.addLore("")
					.addLore("§7Determines which player's duel requests ")
					.addLore("§7will open the duel request menu").build());
			} else if (sp.getOptions().getDuelNotification()==1) {
				s(40, new ItemBuilder(Material.CHEST).setTitle("§6Duel Notification - §9ONLY FRIENDS")
						.addLore("§7Right click to toggle")
						.addLore("§7Determines which player's duel requests ")
						.addLore("§7will open the duel request menu").build());
			} else {
				s(40, new ItemBuilder(Material.CHEST).setTitle("§6Duel Notification - §cNO ONE")
						.addLore("§7Right click to toggle")
						.addLore("§7Determines which player's duel requests ")
						.addLore("§7will open the duel request menu").build());
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
		Player p = sp.getPlayer();
	if (slot==10) {
		sp.getOptions().nightVision(!sp.getOptions().hasNightVision());
		new OptionsMenu(sp).o(sp.getPlayer());
	} else if (slot==13)  {
		new LanguageMenu(sp).o(sp.getPlayer());
	} else if (slot==16 && sp.getPlayer().hasPermission("splindux.vip")) {
		new DefaultColorChatMenu(sp).o(p);
	} else if (slot==22 && p.hasPermission("splindux.vip")) {
		new RankedMapMenu(sp).o(p);
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



