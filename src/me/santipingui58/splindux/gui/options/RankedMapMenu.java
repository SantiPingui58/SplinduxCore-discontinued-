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

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;

public class RankedMapMenu extends MenuBuilder {

	
	public RankedMapMenu(SpleefPlayer sp) {
		super("§eSelect Ranked Map",3);
		
		new BukkitRunnable() {
		public void run() {
			
		List<String> main_arena = new ArrayList<String>();
		
		 
		List<String> alph = new ArrayList<String>();
		int x = 0;
		for (Arena arena : DataManager.getManager().getArenas()) {
			if (arena.getSpleefType().equals(SpleefType.SPLEEF) && arena.getGameType().equals(GameType.DUEL))
			alph.add(arena.getName());
		}
				
		java.util.Collections.sort(alph);
		List<Arena> allArenas = new ArrayList<Arena>();
		for (String s : alph) {
			Arena arena = GameManager.getManager().getArenaByName(s);
			if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
				String n = arena.getName();
				n = n.replaceAll("\\d","");
				if (!Utils.getUtils().containsIgnoreCase(main_arena, n)) {
					main_arena.add(arena.getName());					
					allArenas.add(arena);	
	}

			}
		}
		
		
		for (Arena arena : allArenas) {		
			String name = arena.getName().replaceAll("\\d","");
			name = name.toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
				List<String> lore = new ArrayList<String>();
				ItemStack item = new ItemBuilder(arena.getItem()).setTitle("§6"+name).build();
				ItemMeta meta = item.getItemMeta();
				
				if (sp.getOptions().getRankedArena()!=null && sp.getOptions().getRankedArena().equalsIgnoreCase(name)) {
					lore.add("§aSelected!");
					meta.addEnchant(Enchantment.ARROW_DAMAGE, 1,true);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				} else {
					lore.add("§7Right click to select this Arena");
				}			
				meta.setLore(lore);
				item.setItemMeta(meta);
			s(x,item);			
			x++;
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
		String arenaName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
		sp.getOptions().setRankedArena(arenaName);
		new RankedMapMenu(sp).o(sp.getPlayer());
	}
	
	
	}



