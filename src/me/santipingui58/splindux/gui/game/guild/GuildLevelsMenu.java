package me.santipingui58.splindux.gui.game.guild;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.ItemBuilder;



public class GuildLevelsMenu extends MenuBuilder {
	
	
	public GuildLevelsMenu(Guild guild) {
		super("§5§lGuild Levels" ,4);
		
		new BukkitRunnable() {
		public void run() {
			
			s(11,new ItemBuilder(Material.DIAMOND_BLOCK).addEnchantment(Enchantment.ARROW_DAMAGE, 1)
					.setTitle("§a§lLevel 1")
					.addLore("§7Members Amount Limit: §b5")
					.addLore("§7Members Fee: §610 Coins")
					.addLore("§7Leader Revenue: §b0.25%")
					.build());
		s(35, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
			
			for (int level=2; level<=10;level++) {
				
				int members = 10 + ((level-1)*5);
				int fee = 10 + ((level-1)*10);
				double revenue = 0.25 + ((level-1)*0.25);
				int price = guild.getLevelPrice(level);
				String special = level==3 ? "§bUnlock: §7Guild Channel at Splindux Discord" : level==5 ? "§bUnlock: §7Guild [TAG] at Tab" : level==7 ? "§bUnlock: §7[TAG] above head" : "";
		if (guild.getLevel()>=level) {
			s(level <=5 ? 10+level : 17+level-3,new ItemBuilder(Material.DIAMOND_BLOCK).addEnchantment(Enchantment.ARROW_DAMAGE, 1)
					.setTitle("§a§lLevel " + level)
					.addLore("§7Members Amount Limit: §b" + members)
					.addLore("§7Members Fee: §6"+fee+" Coins")
					.addLore("§7Leader Revenue: §b"+String.format("%.2f", revenue))
					.addLore(special)
					.build());
		} else {
			
			s(level <=5 ? 10+level : 17+level-3,new ItemBuilder(Material.COAL_BLOCK)
					.setTitle("§a§lLevel "+level)
					.addLore("§7Members Amount Limit: §b"+members)
					.addLore("§7Members Fee: §6"+fee+" Coins")
					.addLore("§7Leader Revenue: §b0.50%")
					.addLore("")
					.addLore("§eClick to level up for §6"+price+" Coins")
					.addLore(special)
					.build());
		}
		
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
		Guild guild = GuildsManager.getManager().getGuild(sp);
		if (guild==null) {
			p.closeInventory();
			return;
		}
		
		if (slot==35) {
			new GuildMainMenu(sp,guild).o(p);
		} else {
			if (stack!=null) {
				if (!guild.getAdmins(false).contains(p.getUniqueId())) return;
					
				String str = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
				str = str.replaceAll("[^\\d]", " ");
				  str = str.trim(); 
				  str = str.replaceAll(" +", " "); 
				  int level = Integer.valueOf(str);
				  
				  if (guild.getLevel()<level && guild.getLevel()==level-1) {
					  if (guild.getCoins()>=guild.getNextLevelPrice()*1.25) {
					  guild.levelUp();
					  new GuildLevelsMenu(guild).o(p);
				  } else {
					  p.closeInventory();
					  p.sendMessage(GuildsManager.getManager().getPrefix()+"§c The guild does not have enough coins to afford that.");
				  }
				  }
			}
		}
	}
	
	}



