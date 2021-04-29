package me.santipingui58.splindux.gui.game.guild.duel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildDuel;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.ItemBuilder;



public class GuildSelectGameDuelMenu extends MenuBuilder {
	
	
	public GuildSelectGameDuelMenu(GuildDuel duel) {
		super("§e§lSelect a Gamemode" ,3);
		
		new BukkitRunnable() {
		public void run() {
			
		s(11,new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§b§lSpleef").addLore("§8SPLEEF").build());
		s(13,new ItemBuilder(Material.TNT).setTitle("§c§lTNT Run").addLore("§8TNTRUN").build());
		s(15,new ItemBuilder(Material.EGG).setTitle("§e§lSplegg").addLore("§8SPLEGG").build());
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
		GuildDuel duel = GuildsManager.getManager().getGuildDuelByCreator(p.getUniqueId());
		if (guild==null) {
			p.closeInventory();
			GuildsManager.getManager().getGuildDuels().remove(duel);
			return;
		}
	
		SpleefType type = SpleefType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
	
		duel.setType(type);
		new GuildSelectPlayersDuelMenu(duel,1).o(p);
		
	}
	
	}



