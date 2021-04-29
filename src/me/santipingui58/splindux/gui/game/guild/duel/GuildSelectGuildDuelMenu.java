package me.santipingui58.splindux.gui.game.guild.duel;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildDuel;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;


public class GuildSelectGuildDuelMenu extends MenuBuilder {
	
	
	public GuildSelectGuildDuelMenu(Guild guild) {
		super("§e§lSelect a Guild to duel" ,6);
		
		new BukkitRunnable() {
		public void run() {
			
		int slot = 0;
		for (Guild onlineGuilds : GuildsManager.getManager().getDueleableGuilds()) {
			if (onlineGuilds.equals(guild)) continue;
			ItemStack head = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			meta.setDisplayName("§6§l"+onlineGuilds.getName());
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(onlineGuilds.getLeader()));
			List<String> lore = new ArrayList<String>();
			lore.add("§8"+onlineGuilds.getAchronym());
			meta.setLore(lore);
			head.setItemMeta(meta);
			s(slot,head);
			slot++;
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
		if (stack==null) return;
		
		Guild duelGuild = GuildsManager.getManager().getGuildByAchronym(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
		GuildDuel duel = new GuildDuel(sp.getUUID(),guild,duelGuild);
		
		new GuildSelectGameDuelMenu(duel).o(p);
	}
	
	}



