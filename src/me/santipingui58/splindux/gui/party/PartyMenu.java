package me.santipingui58.splindux.gui.party;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.relationships.parties.PartyMode;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;





public class PartyMenu extends MenuBuilder {

	
	public PartyMenu(SpleefPlayer sp ,PartyMode partyMode) {
		super(partyMode==null ? "§2§lSelect Party Mode" : "§5§lParty Menu",partyMode==null ?3 : 6);

		
		new BukkitRunnable() {
		public void run() {
		
		
		if (partyMode==null) {
			s(11,new ItemBuilder(Material.GRASS).setTitle("§aPrivate Party")
					.addLore("§7Only players who are invited with")
					.addLore("§b/party invite <Player> §7are able to")
					.addLore("§7join the party")
					.build());
			s(13,new ItemBuilder(Material.GRASS).setTitle("§aFriends Party")
					.addLore("§7Only your friends are able to")
					.addLore("§7join your party with the")
					.addLore("§7command §b/party join " + sp.getOfflinePlayer().getName())
					.build());
			
			s(15,new ItemBuilder(Material.GRASS).setTitle("§aPublic Party")
					.addLore("§7Everyone is able to")
					.addLore("§7join your party with the")
					.addLore("§7command §b/party join " + sp.getOfflinePlayer().getName())
					.build());
			
		} else {
		
			Party party = PartyManager.getManager().getParty(sp.getPlayer());
		
			s(4,Utils.getUtils().getSkull(Bukkit.getOfflinePlayer(party.getLeader()),"§5§l"+Bukkit.getOfflinePlayer(party.getLeader()).getName()+"'s Party"));
			
			s(12,new ItemBuilder(Material.TNT).setTitle("§c§lTNT Run").addLore("§7Send TNTRun Games").build());
			s(13,new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§b§lSpleef").addLore("§7Send Spleef Games").build());
			s(14,new ItemBuilder(Material.EGG).setTitle("§e§lSplegg").addLore("§7Send Splegg Games").build());
			
			
			
			
			if (!partyMode.equals(PartyMode.PRIVATE)) {
			 if (TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, TimeLimitType.PARTY_BROADCAST)) {
				 s(9,new ItemBuilder(Material.PAPER).setTitle("§f§lSend broadcast")
							.addLore("§7Send a broadcast")
							.addLore("§7to let players join")
							.addLore("§7your party")
							.addLore("")
							.addLore("§cYou can  send this again in:")
							.addLore("§e"+TimeLimitManager.getManager().getTimeLimitBy(sp, TimeLimitType.PARTY_BROADCAST).get(0).getLeftMinutes()+  " minutes")
							.build());
			 } else {
					s(9,new ItemBuilder(Material.PAPER).setTitle("§f§lSend broadcast")
							.addLore("§7Send a broadcast")
							.addLore("§7to let players join")
							.addLore("§7your party")
							.build());
			 }
			 
			} else {
				s(9,new ItemBuilder(Material.PAPER).setTitle("§f§lSend broadcast")
						.addLore("§cDisabled for private parties.")
						.build());
			}
			
			s(10,new ItemBuilder(Material.PISTON_BASE).setTitle("§e§lChange Party Mode")
					.addLore("§7Selects the")
					.addLore("§7party mode")
					.addLore("§7")
					.addLore("§7Actual Party Mode: §a" + partyMode.toString().toUpperCase())
					.build());
			
			s(16,new ItemBuilder(Material.REDSTONE).setTitle("§c§lKick Offline Players")
					.addLore("§7Removes all")
					.addLore("§7Offline players from")
					.addLore("§7your party")
					.build());
			
			s(17,new ItemBuilder(Material.REDSTONE_BLOCK).setTitle("§c§lDisband Party")
					.addLore("§7Disbands the party")
					.build());	
			
			
			
			int i = 27;
				for (UUID m : party.getAllMembers()) {
				OfflinePlayer member = Bukkit.getOfflinePlayer(m);
				s(i,Utils.getUtils().getSkull(member, "§a"+member.getName()));
				i++;
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
		if (stack==null) {
			return;	
		}
		Player p = sp.getPlayer();
		Party party = PartyManager.getManager().getParty(p);
		
		if (party==null || party.getPartyMode()==null) {
			PartyMode partyMode = null;
			switch(slot) {
			case 11: partyMode = PartyMode.PRIVATE; break;
			case 13: partyMode = PartyMode.ONLY_FRIENDS; break;
			case 15: partyMode = PartyMode.PUBLIC; break;
			}
			if (party!=null) {
				party.setPartMode(partyMode);
			}
			
			 party = new Party(sp.getUUID(), partyMode);
			new PartyMenu(sp,partyMode).o(sp.getPlayer());
			
		} else {
			if (party.isLeader(p)) {
			if (slot==9 && !party.getPartyMode().equals(PartyMode.PRIVATE)) {
				party.broadcast();
				sp.getPlayer().closeInventory();
			} else if (slot==10) {
				party.setPartMode(null);
				new PartyMenu(sp,null).o(sp.getPlayer()); 
			} else if (slot==15) {
				party.kickOfflinePlayer();
				sp.getPlayer().closeInventory();
			}else if (slot==16) {
				party.disband();
				sp.getPlayer().closeInventory();
			} else if (slot==12) {
				new PartySelectGameMenu(party, SpleefType.TNTRUN).o(p);
			}else if (slot==13) {
				new PartySelectGameMenu(party, SpleefType.SPLEEF).o(p);
			}else if (slot==14) {
				new PartySelectGameMenu(party, SpleefType.SPLEGG).o(p);
			}else if (slot==17) {
				party.disband();
				p.closeInventory();
			} else if (slot==16) {}
			party.kickOfflinePlayer();
			
		} else {
			p.closeInventory();
			p.sendMessage("§cOnly the Leader of the Party can do this.");
		}
 
		} 
			
	}
	



	
	
	}



