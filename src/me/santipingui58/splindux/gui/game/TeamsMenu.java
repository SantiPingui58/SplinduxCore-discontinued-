package me.santipingui58.splindux.gui.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;



public class TeamsMenu extends MenuBuilder {
	
	
	public TeamsMenu(SpleefPlayer sp) {
		super("§f§lTeams Menu",4);
		
		
		new BukkitRunnable() {
		public void run() {
			

		
		s(11, new ItemBuilder(Material.TNT)
				.setTitle("§c§lTNTRun 2vs2")
				.setAmount(2)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,2))
				.build());
		

		s(20, new ItemBuilder(Material.TNT)
				.setTitle("§c§lTNTRun 3vs3")
				.setAmount(3)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,3))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,3))
				.build());
			
		s(13, new ItemBuilder(Material.DIAMOND_SPADE)
				.setTitle("§b§lSpleef 2vs2")
				.setAmount(2)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,2))
				.build());
		
		s(22, new ItemBuilder(Material.DIAMOND_SPADE)
				.setTitle("§b§lSpleef 3vs3")
				.setAmount(3)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,3))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,3))
				.build());
		
		s(15, new ItemBuilder(Material.EGG)
				.setTitle("§e§lSplegg 2vs2")
				.setAmount(2)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,2))
				.build());
		
		
		s(24, new ItemBuilder(Material.EGG)
				.setTitle("§e§lSplegg 3vs3")
				.setAmount(3)
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,3))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,3))
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
		Player p = sp.getPlayer();
		if (slot==11) {
			sp.leave(false, false);
			new BukkitRunnable() {
				public void run() {
					GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.TNTRUN, false, null);
				}
			}.runTaskLater(Main.get(),5L);
		
			p.closeInventory();
		} else if (slot==13) {
			sp.leave(false, false);
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.SPLEEF, false, null);
		}
			}.runTaskLater(Main.get(),5L);
			p.closeInventory();
		} else if (slot==15) {
			sp.leave(false, false);
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.SPLEGG, false, null);
		}
			}.runTaskLater(Main.get(),5L);
			p.closeInventory();
		} else if (slot==20) {
			sp.leave(false, false);
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.TNTRUN, false, null);
		}
			}.runTaskLater(Main.get(),5L);
			p.closeInventory();
		} else if (slot==22) {
			sp.leave(false, false);
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.SPLEEF, false, null);
		}
			}.runTaskLater(Main.get(),5L);
			p.closeInventory();
		} else if (slot==24) {
			sp.leave(false, false);
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.SPLEGG, false, null);
		}
			}.runTaskLater(Main.get(),5L);
			p.closeInventory();
		}
		}
	
	
	}



