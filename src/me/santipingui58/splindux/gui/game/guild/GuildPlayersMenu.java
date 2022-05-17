package me.santipingui58.splindux.gui.game.guild;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildPlayer;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;



public class GuildPlayersMenu extends MenuBuilder {
	
	
	public GuildPlayersMenu(Guild guild) {
		super("§e§lGuild Players" ,6);
		new BukkitRunnable() {
			public void run() {
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");		
		//s(45,new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
		s(45, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
		if (guild==null) {
			int slot = 0;
			
			for (Guild guilds : GuildsManager.getManager().getGuilds()) {
				
				ItemStack leader = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
				SkullMeta leaderMeta = (SkullMeta) leader.getItemMeta();
				leaderMeta.setDisplayName("§6§l"+guilds.getName() + "Guild");
				leaderMeta.setOwningPlayer(Bukkit.getOfflinePlayer(guilds.getLeader()));
				List<String> lore = new ArrayList<String>();
				lore.add("§e"+guilds.getAchronym());
				lore.add("§9Foundation Date: §f" + format.format(guilds.getFoundationDate()) + "(" + DataManager.getManager().getDateDifference(guilds.getFoundationDate()) + ")");
				lore.add("§9Guild Level: §d" + guilds.getLevel());
				lore.add("§9Guild Value: §6§l" + Utils.getUtils().getStringMoney(guilds.getValue()) + " Coins");
				lore.add("§9Amount of members: §f" + guilds.getMembers().size());
				lore.add("§9Membership Fee: §6" + guilds.getMemberFee() + " Coins");
				leaderMeta.setLore(lore);
				leader.setItemMeta(leaderMeta);
				//s(slot,leader);
				s(slot, leader);
				slot++;
			}
		} else {
		
		/*
			s(4,new ItemBuilder(Material.GOLD_INGOT).addEnchantment(Enchantment.ARROW_DAMAGE, 1)
					
					.setTitle("§6§l"+guild.getName() + "Guild")
					.addLore("§e"+guild.getAchronym())
					.addLore("§9Foundation Date: §f" + format.format(guild.getFoundationDate()) + "(" + DataManager.getManager().getDateDifference(guild.getFoundationDate()) + ")")
					.addLore("§9Guild Level: §d" + guild.getLevel())
					.addLore("§9Guild Value: §6§l" + Utils.getUtils().getStringMoney(guild.getValue()) + " Coins")
					.addLore("§9Amount of members: §f" + guild.getMembers().size())
					.addLore("§9Membership Fee: §6" + guild.getMemberFee() + " Coins")
					.build());
			*/
			s(4, new ItemBuilder(Material.GOLD_INGOT).addEnchantment(Enchantment.ARROW_DAMAGE, 1)
					
					.setTitle("§6§l"+guild.getName() + "Guild")
					.addLore("§e"+guild.getAchronym())
					.addLore("§9Foundation Date: §f" + format.format(guild.getFoundationDate()) + "(" + DataManager.getManager().getDateDifference(guild.getFoundationDate()) + ")")
					.addLore("§9Guild Level: §d" + guild.getLevel())
					.addLore("§9Guild Value: §6§l" + Utils.getUtils().getStringMoney(guild.getValue()) + " Coins")
					.addLore("§9Amount of members: §f" + guild.getMembers().size())
					.addLore("§9Membership Fee: §6" + guild.getMemberFee() + " Coins")
					.build());
			
			
			//s(53,new ItemBuilder(Material.PAPER).setTitle("§e§lView all Guilds").build());
			s(53, new ItemBuilder(Material.PAPER).setTitle("§e§lView all Guilds").build());
			//s(52,new ItemBuilder(Material.PAPER).setTitle("§e§lView all members").build());
			s(52, new ItemBuilder(Material.PAPER).setTitle("§e§lView all members").build());
			
			ItemStack leader = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
			SkullMeta leaderMeta = (SkullMeta) leader.getItemMeta();
			leaderMeta.setDisplayName("§4§lLeader §7- §b"+Bukkit.getOfflinePlayer(guild.getLeader()).getName());
			leaderMeta.setOwningPlayer(Bukkit.getOfflinePlayer(guild.getLeader()));
			leader.setItemMeta(leaderMeta);
			//s(13,leader);
			s(13, leader);
			
			int playerSlot = 19;
			for (GuildPlayer gp : guild.getPlayers()) {
				
				ItemStack player = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
				SkullMeta playerMeta = (SkullMeta) player.getItemMeta();
				playerMeta.setDisplayName("§a§lPlayer §7- §b"+Bukkit.getOfflinePlayer(gp.getUUID()).getName());
				playerMeta.setOwningPlayer(Bukkit.getOfflinePlayer(gp.getUUID()));
				List<String> lore = new ArrayList<String>();
				int min = GuildsManager.getManager().getFutureMinValue(guild, gp.getUUID());
				lore.add("§aPlayer Min Salary: §6" + min);
				lore.add("§aPlayer Daily Salary: §6" + gp.getSalary());
				lore.add("§aPlayer Value: §6§l" + Utils.getUtils().getStringMoney(gp.getValue()) + " Coins" );
				
				playerMeta.setLore(lore);
				player.setItemMeta(playerMeta);
				if(playerSlot==22) playerSlot++;
			//s(playerSlot,player);	
				s(playerSlot, player);
				playerSlot++;
			}
			
			
			int adminsSlot = 28;
			for (UUID uuid : guild.getAdmins(true)) {
				ItemStack player = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
				SkullMeta playerMeta = (SkullMeta) player.getItemMeta();
				playerMeta.setDisplayName("§c§lAdmin §7- §b"+Bukkit.getOfflinePlayer(uuid).getName());
				playerMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
				player.setItemMeta(playerMeta);
				//s(adminsSlot,player);	
				s(adminsSlot, player);
				adminsSlot++;
			}
			
			int modsSlot = 37;
			for (UUID uuid : guild.getMods(true)) {
				ItemStack player = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
				SkullMeta playerMeta = (SkullMeta) player.getItemMeta();
				playerMeta.setDisplayName("§2§lMod §7- §b"+Bukkit.getOfflinePlayer(uuid).getName());
				playerMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
				player.setItemMeta(playerMeta);
				//s(modsSlot,player);		
				s(modsSlot, player);
				modsSlot++;
			}
			
		}
		
		buildInventory();	
			}
			}.runTaskAsynchronously(Main.get());
		
		
	}
	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		Player p = sp.getPlayer();
		
		if (slot==45) { 
			Guild guild = GuildsManager.getManager().getGuild(sp);
			if (guild==null) {
				p.closeInventory();
				return;
			}
			new GuildMainMenu(sp,guild).o(p);
		}  else {
			Guild guilds = null;
					try {
					guilds =GuildsManager.getManager().getGuildByAchronym(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
					} catch(Exception ex) {}
			if (guilds!=null) {
				new GuildPlayersMenu(guilds).o(p);
			} else {
				 if (slot==53) {
						new GuildPlayersMenu(null).o(p);
					}else  if (slot==52) {
						new GuildPlayersMenu(null).o(p);
					}
			}
		}
			
			
		
	}
	
	}



