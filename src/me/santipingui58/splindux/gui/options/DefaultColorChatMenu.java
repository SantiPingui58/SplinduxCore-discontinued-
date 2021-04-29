package me.santipingui58.splindux.gui.options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;

public class DefaultColorChatMenu extends MenuBuilder {

	
	public DefaultColorChatMenu(SpleefPlayer sp) {
		super("§cDefault Color Chat",3);
		
		new BukkitRunnable() {
		public void run() {
			
		int i = 0;
		Utils utils = Utils.getUtils();
		
		for (ChatColor c : ChatColor.values()) {
			int id = utils.getIDByChatColor(c);
			if (c.isColor()) {
			if (!c.name().equalsIgnoreCase("BLACK") && !c.name().equalsIgnoreCase("DARK_BLUE")) {
			ItemStack wool = new ItemStack(Material.WOOL,1,(byte) id);
			ItemMeta meta = wool.getItemMeta();
			meta.setDisplayName(c+c.name()+" Color");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.DARK_GRAY+c.name());
			if (sp.getOptions().getDefaultColorChat().equals(c)) {
				lore.add("§aSelected!");
				meta.addEnchant(Enchantment.ARROW_DAMAGE, 1,true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			} else {
				lore.add("§aRight click to select this color");
			}
			meta.setLore(lore);
			wool.setItemMeta(meta);
			s(i,wool);
			i++;
		}
		}
		}
		s(26, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
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
		if (slot==26) {
			new OptionsMenu(sp).o(sp.getPlayer());
			return;
		}
		
		if (stack!=null) {
			if (stack.getItemMeta().getLore()!=null) {
				List<String> lore = stack.getItemMeta().getLore();
				ChatColor c = ChatColor.valueOf(ChatColor.stripColor(lore.get(0)));
				sp.getOptions().setDefaultColorChat(c);
				sp.getPlayer().sendMessage("§aColor changed to: "+ c+ c.name());
				new DefaultColorChatMenu(sp).o(sp.getPlayer());
			}
		}
	}
	
	
	}



