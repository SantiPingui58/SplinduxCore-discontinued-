package me.santipingui58.splindux.gui.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;






public class BetMenu extends MenuBuilder {

	public static HashMap<Player,Integer> page = new HashMap<Player,Integer>();
	
	
	public BetMenu(SpleefPlayer sp,int amount) {
		super("§aFFA Bet:",6);
		
		new BukkitRunnable() {
		public void run() {
			
		Arena arena = sp.getArena();
		
		int slot = 0;
		for (SpleefPlayer players : arena.getPlayers()) {
			 ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			 SkullMeta meta = (SkullMeta) head.getItemMeta();
			 meta.setDisplayName("§a"+players.getOfflinePlayer().getName());
			 meta.setOwningPlayer(players.getOfflinePlayer());
			 List<String> lore = new ArrayList<String>();
			// if (arena.getFFAProbabilities().containsKey(sp)) {
			 double cuota = 0; //arena.getFFAProbabilities().get(sp);
			 int coins = (int) (amount*cuota);
			 String c = String.format("%.00f", cuota);
			 lore.add("§8"+amount);
			 lore.add("§aBet quota: §bx" + c);
			 lore.add("");
			 lore.add("§aIf this player wins you get: §6§l" + coins +" Coins§a!");
			 meta.setLore(lore);
			 head.setItemMeta(meta);
			 s(slot,head);
			// } else {
				// arena.calculateFFAProbabilities();
				 new BetMenu(sp,amount).o(sp.getPlayer());
				 return;
			// }
			// slot++;
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
		
		if(slot==26) {
			p.closeInventory();
		} else {
			SkullMeta meta = (SkullMeta) stack.getItemMeta();
			OfflinePlayer player = meta.getOwningPlayer();
			//SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
			int amount = Integer.valueOf(ChatColor.stripColor(meta.getLore().get(0)));
			//sp.getArena().getNextFFABet().add(new FFABet(sp,splayer,amount));
			sp.removeCoins(amount);
			p.sendMessage("§aYou have bet for §b" + player.getName() + " §awith §6" + amount +" Coins§a!");
			p.closeInventory();
		}
	}
	
	
	}



