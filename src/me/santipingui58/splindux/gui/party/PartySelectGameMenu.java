package me.santipingui58.splindux.gui.party;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.utils.ItemBuilder;





public class PartySelectGameMenu extends MenuBuilder {

	
	public PartySelectGameMenu(Party party, SpleefType type) {
		super("§2§lSelect Game" ,3);
		
		
		new BukkitRunnable() {
		public void run() {
		

			
			s(15,new ItemBuilder(Material.DIAMOND).setTitle("§aSend Multiple Duels")
					.addLore("§7Use command: /party duels <Teams Size>")	
					.addLore("§8"+type.toString())
					.build());
			s(13, new ItemBuilder(Material.DIAMOND_BLOCK).setTitle("§aFFA")
					.addLore("§7Join " + type.getName() + "FFA")
					.addLore("§8"+type.toString())
					.build());
			
			int teamSize = party.getAllMembers().size()/2;
			s(11, new ItemBuilder(Material.DIAMOND).setTitle("§aSend Duel")
					.addLore("§7Send §a"+teamSize+"VS"+teamSize+" §7duel with random teams")
					.addLore("§8"+type.toString())
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
		SpleefType type = SpleefType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(1)));
		Party party = PartyManager.getManager().getParty(sp.getPlayer());
		if (slot==13) {
			for (UUID u : party.getAllMembers()) {
				SpleefPlayer spp = SpleefPlayer.getSpleefPlayer(u);
			GameManager.getManager().addFFAQueue(spp, type);
			}
		} else if (slot==11) {
			party.sendDuel(type);
		}
	}
	



	
	
	}



