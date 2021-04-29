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
import me.santipingui58.splindux.gui.game.parkour.ParkourMenu;
import me.santipingui58.splindux.utils.ItemBuilder;



public class UnrankedMenu extends MenuBuilder {
	
	
	public UnrankedMenu(SpleefPlayer sp) {
		super("§f§lUnranked Menu",5);
		
		new BukkitRunnable() {
		public void run() {
			
		s(10,new ItemBuilder(Material.TNT).setTitle("§c§lTNT Run FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.FFA,1,0))
				.build());
		s(18,new ItemBuilder(Material.SULPHUR).setTitle("§cTNT Run 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,1,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,1,0))
			
				.build());
		s(19,new ItemBuilder(Material.SULPHUR).setTitle("§cTNT Run 2v2")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,2,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,2,0))
			
				.build());
		s(20,new ItemBuilder(Material.SULPHUR).setTitle("§cTNT Run 3v3")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,3,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,3,0))
			
				.build());
		
		s(21,new ItemBuilder(Material.SNOW_BALL).setTitle("§bSpleef 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,1,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,1,0))			
				.build());
		s(22,new ItemBuilder(Material.SNOW_BALL).setTitle("§bSpleef 2v2")	
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,2,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,2,0))
				.build());
				
		
		s(23,new ItemBuilder(Material.SNOW_BALL).setTitle("§bSpleef 3v3")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,3,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,3,0))
			
				.build());
		
		s(24,new ItemBuilder(Material.EGG).setTitle("§eSplegg 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,1,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,1,0))
			
				.build());
		s(25,new ItemBuilder(Material.EGG).setTitle("§eSplegg 2v2")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,2,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,2,0))
			
				.build());
		s(26,new ItemBuilder(Material.EGG).setTitle("§eSplegg3v3")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,3,0))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,3,0))
			
				.build());
		
		s(13, new ItemBuilder(Material.SNOW_BLOCK).setTitle("§b§lSpleef FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.FFA,1,0))
				.build());
		
		s(16, new ItemBuilder(Material.CAKE).setTitle("§e§lSplegg FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.FFA,1,0))
				.build());
		
		s(31, new ItemBuilder(Material.FEATHER).setTitle("§a§lParkour").build());
		
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
		new BukkitRunnable() {
			public void run() {
		if (slot==11) {
			
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addFFAQueue(sp,SpleefType.TNTRUN);
				}
			}.runTask(Main.get());
		} else if (slot==18) {
			GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.TNTRUN, false,null);
			p.closeInventory();
		} else if (slot==19) {
			GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.TNTRUN, false,null);
			p.closeInventory();
		} else if (slot==20) {
			GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.TNTRUN, false,null);
			p.closeInventory();
		} else if (slot==13) {
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addFFAQueue(sp,SpleefType.SPLEEF);
				}
			}.runTask(Main.get());
		} else if (slot==21) {
			GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.SPLEEF, false,null);
			p.closeInventory();
		} else if (slot==22) {
			GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.SPLEEF, false,null);
			p.closeInventory();
		} else if (slot==23) {
			GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.SPLEEF, false,null);
			p.closeInventory();
		}else if (slot==15) {
			new BukkitRunnable() {
				public void run() {
			GameManager.getManager().addFFAQueue(sp,SpleefType.SPLEGG);
				}
			}.runTask(Main.get());
		} else if (slot==24) {
			GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.SPLEGG, false,null);
			p.closeInventory();
		} else if (slot==25) {
			GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.SPLEGG, false,null);
			p.closeInventory();
		} else if (slot==26) {
			GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.SPLEGG, false,null);
			p.closeInventory();
		} else if (slot==31) {
			new BukkitRunnable() {
				public void run() {
			new ParkourMenu(sp).o(p);
			}
			}.runTask(Main.get());
		}
		
		}
		}.runTaskAsynchronously(Main.get());
		}
	
	
	}



