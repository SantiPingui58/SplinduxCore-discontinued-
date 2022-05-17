package me.santipingui58.splindux.gui.party;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.utils.ItemBuilder;





public class PartySelectDuelSpleefTypeMenu extends MenuBuilder {

	
	public PartySelectDuelSpleefTypeMenu(SpleefPlayer sp,int teamSize) {
		super("§2§lSelect a Spleef Type" ,3);
		
		
		new BukkitRunnable() {
		public void run() {
		

			String duel = teamSize + "VS" + teamSize;
			s(11,new ItemBuilder(Material.TNT).setTitle("§cTNTRun " +duel)
					.addLore("§8"+SpleefType.TNTRUN.toString())
					.setAmount(teamSize)
					.build());
			s(13, new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§bSpleef " + duel)
					.addLore("§8"+SpleefType.SPLEEF.toString())
					.setAmount(teamSize)
					.build());
			
			s(15, new ItemBuilder(Material.EGG).setTitle("§eSplegg" + duel)
					.addLore("§8"+SpleefType.SPLEGG.toString())
					.setAmount(teamSize)
					.build());
		
			
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
		if (stack.getItemMeta().getLore()==null) return;
		SpleefType type = SpleefType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
		Party party = PartyManager.getManager().getParty(sp.getPlayer());	
		party.sendMultipleDuels(stack.getAmount(), type);
	}
	



	
	
	}



