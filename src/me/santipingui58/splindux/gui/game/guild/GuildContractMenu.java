package me.santipingui58.splindux.gui.game.guild;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildPlayer;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;



public class GuildContractMenu extends MenuBuilder {
	
	
	public GuildContractMenu(SpleefPlayer sp,Guild guild) {
		super("§f§lGuild Contract" ,3);
		
		new BukkitRunnable() {
		public void run() {
			
		GuildPlayer gp = guild.getPlayer(sp.getUUID());
		s(4,new ItemBuilder(Material.PAPER).setTitle("§a§lContract")
				.addLore("§aDaily Salary: §6§l" + gp.getSalary() + " Coins")
				.addLore("§aPlayer Value: §6§l" + Utils.getUtils().getStringMoney(guild.getValue())+ " Coins")
				.build());
		
		s(12,new ItemBuilder(Material.PAPER).setTitle("§a§lRenegociate Contract").addLore("§7To renegociate an Admin of the Guild has to do §b/guild renegociate " + sp.getOfflinePlayer().getName()+ " <New Salary>" ).build());
		s(13, new ItemBuilder(Material.PAPER).setTitle("§a§lLeave your Guild").addLore("§7To leave your GUild you need to do §b/guild resign").build());
		s(14, new ItemBuilder(Material.PAPER).setTitle("§a§lTransferable").addLore("§7To set yourself as a transferable player you need to do §b/guild transferable").build());
		
		s(26,new ItemBuilder(Material.ARROW).setTitle("§cGo Back").build());
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
		
		if (slot==26) {
			new GuildMainMenu(sp,guild).o(p);
		} 
	}
	
	}



