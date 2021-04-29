package me.santipingui58.splindux.gui.game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;




public class AcceptDuelMenu extends MenuBuilder {
	
	Integer accept_slots[] = {18,19,20,27,28,29,36,37,38};
	Integer deny_slots[] = {24,25,26,33,34,35,42,43,44};
	
	public AcceptDuelMenu(SpleefPlayer sp2,SpleefDuel duel) {
		super("§b§lYou received a Duel Request",5);
		
		new BukkitRunnable() {
		public void run() {
			
String m = duel.getArena()==null ? "Random Map" : duel.getArena();
		
		s(13, new ItemBuilder(Material.PAPER).setTitle("§aDuel Request Recieved")
				.addLore("§b" + duel.getChallenger().getOfflinePlayer().getName() + " §7sent you a Duel Request.")
				.addLore("§7Spleef Type: §b"+ duel.getType().toString())
				.addLore("§7Arena: §b" + m)
				.addLore("§b§l"+getVersus(duel.getAllPlayers())).build());
		
		
		ItemStack map = null;
		if (duel.getArena()!=null) {
			String name = duel.getArena();
			Arena arena = GameManager.getManager().getArenaByName(name);
			
			map = new ItemBuilder(arena.getItem()).setTitle("§a"+name).build();
		} else {
			map = Utils.getUtils().getSkull("http://textures.minecraft.net/texture/d34e063cafb467a5c8de43ec78619399f369f4a52434da8017a983cdd92516a0","§aRandom Arena");
			
		}
		
		s(12,map);
		
		Material spleeftype = null;
		switch(duel.getType()) {
		case BOWSPLEEF:
			break;
		case POTSPLEEF:
			break;
		case SPLEEF:
			spleeftype = Material.DIAMOND_SPADE;
			break;
		case SPLEGG:
			spleeftype = Material.EGG;
			break;
		case TNTRUN:
			spleeftype = Material.TNT;
			break;
		default:
			break;
		}
		
		s(14,new ItemBuilder(spleeftype).setTitle(duel.getType().getName()).build());
		

		
		for (int i : accept_slots) {
			s(i, new ItemBuilder(Material.CONCRETE, 1, (byte) 5).setTitle("§a§l[ACCEPT DUEL]").addLore("§8"+duel.getChallenger().getUUID()).build());
		}
		for (int i : deny_slots) {
			s(i, new ItemBuilder(Material.CONCRETE, 1, (byte) 14).setTitle("§c§l[DENY DUEL]").addLore("§8"+duel.getChallenger().getUUID()).build());
		}
		new BukkitRunnable() {
		public void run() {
			buildInventory();
		}
		}.runTask(Main.get());
		
		}
		}.runTaskAsynchronously(Main.get());
	}
	

	public String getVersus(List<SpleefPlayer> players) {

		 List<SpleefPlayer> list1 = new ArrayList<SpleefPlayer>();
		 List<SpleefPlayer> list2 = new ArrayList<SpleefPlayer>();
		 int x = 1;
		 
		 for (SpleefPlayer sp : players) {
			 if (x<=players.size()/2) {
				 list1.add(sp);
			 } else {
				 list2.add(sp);
			 }
			 x++;
		 }
		 
		 
		 String p1 = "";
		 for (SpleefPlayer sp : list1) {
			if(p1.equalsIgnoreCase("")) {
			p1 = sp.getName();	
			}  else {
				p1 = p1+"-" + sp.getName();
			}
		 }
		 
		 String p2 = "";
		 for (SpleefPlayer sp : list2) {
			if(p2.equalsIgnoreCase("")) {
			p2 = sp.getName();	
			}  else {
				p2 = p2+"-" + sp.getName();
			}
		 }
		 
		 return p1+ " vs " +p2;		
	}
	
	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		  List<Integer> a = Arrays.asList(accept_slots);   
		  List<Integer> b = Arrays.asList(deny_slots);   
		  
		  if (!a.contains(slot) && !b.contains(slot)) return;
		  
		  if (stack.getItemMeta().getLore()==null) return;
		  
		  OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(ChatColor.stripColor(stack.getItemMeta().getLore().get(0))));
		if (!player.isOnline()) {
			sp.sendMessage("§cThis duel has expired.");
			sp.getPlayer().closeInventory();
			return;
		}
		
		SpleefPlayer challenger = SpleefPlayer.getSpleefPlayer(player);
		
		
		SpleefDuel duel = challenger.getDuelByDueledPlayer(sp);

		if (duel==null) {
			sp.sendMessage("§cThis duel has expired.");
			sp.getPlayer().closeInventory();
			return;
		}
		
		if (a.contains(slot)) {
			duel.acceptDuel(sp);
			sp.getPlayer().closeInventory();
		} else if (b.contains(slot)) {
					sp.getPlayer().sendMessage("§cYou have denied the duel request from §b" + duel.getChallenger().getName() + "§c!");
					for (SpleefPlayer dueled : duel.getAllPlayers()) {
						if (dueled!=sp)
							dueled.getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has denied the request! Duel cancelled.");
					}	
					challenger.getDuels().remove(duel);
			sp.getPlayer().closeInventory();
		}
		
	}
	

	}



