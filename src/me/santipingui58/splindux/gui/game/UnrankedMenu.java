package me.santipingui58.splindux.gui.game;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.ranked.RankedManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.gui.game.parkour.ParkourMenu;
import me.santipingui58.splindux.utils.ItemBuilder;



public class UnrankedMenu extends MenuBuilder {
	
	
	public UnrankedMenu(SpleefPlayer sp) {
		super("§f§lUnranked Menu",6);
		
		new BukkitRunnable() {
		public void run() {
			
		s(10,new ItemBuilder(Material.TNT).setTitle("§c§lTNT Run FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.FFA,1))
				.build());
		s(18,new ItemBuilder(Material.SULPHUR).setTitle("§cTNT Run 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,1))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,1))
			
				.build());
		s(19,new ItemBuilder(Material.SULPHUR).setTitle("§cTNT Run 2v2")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,2))
			
				.build());
		s(20,new ItemBuilder(Material.SULPHUR).setTitle("§cTNT Run 3v3")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.TNTRUN, GameType.DUEL,3))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.TNTRUN, GameType.DUEL,3))
			
				.build());
		
		s(21,new ItemBuilder(Material.SNOW_BALL).setTitle("§bSpleef 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,1))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,1))			
				.build());
		s(22,new ItemBuilder(Material.SNOW_BALL).setTitle("§bSpleef 2v2")	
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,2))
				.build());
				
		
		s(23,new ItemBuilder(Material.SNOW_BALL).setTitle("§bSpleef 3v3")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.DUEL,3))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEEF, GameType.DUEL,3))
			
				.build());
		
		s(24,new ItemBuilder(Material.EGG).setTitle("§eSplegg 1v1")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,1))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,1))
			
				.build());
		s(25,new ItemBuilder(Material.EGG).setTitle("§eSplegg 2v2")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,2))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,2))
			
				.build());
		s(26,new ItemBuilder(Material.EGG).setTitle("§eSplegg3v3")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.DUEL,3))
				.addLore("§7In Queue: §a" + GameManager.getManager().getQueueSize(SpleefType.SPLEGG, GameType.DUEL,3))
			
				.build());
		
		s(13, new ItemBuilder(Material.SNOW_BLOCK).setTitle("§b§lSpleef FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEEF, GameType.FFA,1))
				.build());
		
		s(16, new ItemBuilder(Material.CAKE).setTitle("§e§lSplegg FFA")
				.addLore("§7Playing: §a" + GameManager.getManager().getPlayingSize(SpleefType.SPLEGG, GameType.FFA,1))
				.build());
		
		s(40, new ItemBuilder(Material.FEATHER).setTitle("§a§lParkour").build());
		
		s(37, new ItemBuilder(Material.NETHER_FENCE).setTitle("§9§lTheTowers")
		.addLore("§7Playing: §a" + Main.thetowers)
		.build());
		
		s(43, new ItemBuilder(Material.BARRIER).setTitle("§c§lComing Soon").build());
		
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
		sp.leave(false, false);
		new BukkitRunnable() {
			public void run() {
		if (slot==10) {
			
			GameManager.getManager().addFFAQueue(sp,SpleefType.TNTRUN);
		} else if (slot==18) {
			//GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.TNTRUN, false,null);
			RankedManager.getManager().getTNTRunQueue().addDuelQueue(sp, 1);
			p.closeInventory();
		} else if (slot==19) {
			//GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.TNTRUN, false,null);
			RankedManager.getManager().getTNTRunQueue().addDuelQueue(sp, 2);
			p.closeInventory();
		} else if (slot==20) {
			//GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.TNTRUN, false,null);
			RankedManager.getManager().getTNTRunQueue().addDuelQueue(sp, 3);
			p.closeInventory();
		} else if (slot==13) {
			GameManager.getManager().addFFAQueue(sp,SpleefType.SPLEEF);
		} else if (slot==21) {
			//GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.SPLEEF, false,null);
			RankedManager.getManager().getSpleefQueue().addDuelQueue(sp,1);
			p.closeInventory();
		} else if (slot==22) {
			//GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.SPLEEF, false,null);
			RankedManager.getManager().getSpleefQueue().addDuelQueue(sp,2);
			p.closeInventory();
		} else if (slot==23) {
		//	GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.SPLEEF, false,null);
			RankedManager.getManager().getSpleefQueue().addDuelQueue(sp,3);
			p.closeInventory();
		}else if (slot==16) {
			GameManager.getManager().addFFAQueue(sp,SpleefType.SPLEGG);
		} else if (slot==24) {
		//	GameManager.getManager().addDuelQueue(sp, 1, null, SpleefType.SPLEGG, false,null);
			RankedManager.getManager().getSpleggQueue().addDuelQueue(sp,1);
			p.closeInventory();
		} else if (slot==25) {
			//GameManager.getManager().addDuelQueue(sp, 2, null, SpleefType.SPLEGG, false,null);
			RankedManager.getManager().getSpleggQueue().addDuelQueue(sp,2);
			p.closeInventory();
		} else if (slot==26) {
			//GameManager.getManager().addDuelQueue(sp, 3, null, SpleefType.SPLEGG, false,null);
			RankedManager.getManager().getSpleggQueue().addDuelQueue(sp,3);
			p.closeInventory();
		} else if (slot==40) {
			new ParkourMenu(sp).o(p);
		} else if (slot==37) {
			  ByteArrayOutputStream b = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(b);

		        try {
		            out.writeUTF("Connect");
		            out.writeUTF("thetowers");
		        } catch (Exception e) {
		            return;
		        }

		        p.sendPluginMessage(Main.get(), "BungeeCord", b.toByteArray());
		}
		
		}
		}.runTaskLater(Main.get(),5L);
		}
	
	
	}



