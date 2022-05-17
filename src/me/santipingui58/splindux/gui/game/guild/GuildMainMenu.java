package me.santipingui58.splindux.gui.game.guild;

import java.text.SimpleDateFormat;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.game.guild.duel.GuildSelectGuildDuelMenu;
import me.santipingui58.splindux.gui.game.guild.duel.GuildSelectPlayersDuelMenu;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildDuel;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;



public class GuildMainMenu extends MenuBuilder {
	
	
	public GuildMainMenu(SpleefPlayer sp,Guild guild) {
		super("§6§l"+guild.getName()+"Menu" ,5);
		
		new BukkitRunnable() {
		public void run() {
			
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");		
		s(22,new ItemBuilder(Material.GOLD_INGOT).addEnchantment(Enchantment.ARROW_DAMAGE, 1)
				.setTitle("§6§l"+guild.getName() + "Guild")
				.addLore("§e"+guild.getAchronym())
				.addLore("§9Foundation Date: §f" + format.format(guild.getFoundationDate()) + " (" + DataManager.getManager().getDateDifference(guild.getFoundationDate()) + ")")
				.addLore("§9Guild Level: §d" + guild.getLevel())
				.addLore("§9Guild Value: §6§l" + Utils.getUtils().getStringMoney(guild.getValue()) + "Coins")
				.addLore("§9Amount of members: §f" + guild.getMembers().size())
				.addLore("§9Membership Fee: §6" + guild.getMemberFee() + " Coins")
				.build());
		
		//String duel = guild.isMod(sp.getUUID(),false) ?  "§7Request a §bGuild Duel §7to any Guild that is currently online" :  "§cThis Menu is only for Mods of the Guild";
		//s(22,new ItemBuilder(Material.DIAMOND_SWORD).setTitle("§b§lGuild Duel").addLore(duel).build());
		
		if (guild.isAdmin(sp.getUUID(),false)) {
		s(19,new ItemBuilder(Material.EMERALD).setTitle("§a§lGuild Bank").addLore("§7Check the amount, recent movements, and everything")
				.addLore("§7related to the Coins of the guild").build());
		} else {
			s(19,new ItemBuilder(Material.EMERALD).setTitle("§a§lGuild Bank").addLore("§cThis Menu is only for Admins of the Guild.").build());
		}
		s(20, new ItemBuilder(Material.EXP_BOTTLE).setTitle("§5§lGuild Levels").addLore("§7Open the Guild Levels Menu").build());
		s(24, new ItemBuilder(Material.TOTEM).setTitle("§e§lPlayers & Members").addLore("§7See the players and members of the Guild").build());
		
		String lore = guild.getPlayer(sp.getUUID())!=null ? "§7See your contract with the Guild." : "§cThis Menu is only for Players of the Guild";
		s(25, new ItemBuilder(Material.PAPER).setTitle("§f§lGuild Contract").addLore(lore).build());
		
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
		
		if (slot==19 && guild.isAdmin(sp.getUUID(), false)) {
			new GuildBankMenu(sp,guild).o(p);
		}
		if (slot==22 && guild.isMod(sp.getUUID(), false)) {
			GuildDuel duel = GuildsManager.getManager().getGuildDuelBySender(guild);
			if (duel==null) {
			new GuildSelectGuildDuelMenu(guild).o(p);
			} else {
				duel.getPlayers1().clear();
				if (duel.getType()!=null) {
				new GuildSelectPlayersDuelMenu(duel,1).o(p);
				} else {
					GuildsManager.getManager().getGuildDuels().remove(duel);
					new GuildSelectGuildDuelMenu(guild).o(p);
				}
			}
		}
		
		if (slot==24) {
			new GuildPlayersMenu(guild).o(p);
		}
		
		if (slot==20) {
			new GuildLevelsMenu(guild).o(p);
		}
		if (slot==25 && guild.getPlayer(sp.getUUID())!=null) {
			new GuildContractMenu(sp,guild).o(p);
		}
		
	}
	
	}



