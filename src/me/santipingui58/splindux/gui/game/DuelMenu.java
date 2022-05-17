package me.santipingui58.splindux.gui.game;






import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;





public class DuelMenu extends MenuBuilder {

	public static HashMap<Player,Integer> page = new HashMap<Player,Integer>();
	public  HashMap<SpleefPlayer,List<SpleefPlayer>> cache = new HashMap<SpleefPlayer,List<SpleefPlayer>>();
	
	public DuelMenu(SpleefPlayer sp,List<SpleefPlayer> sp2,SpleefType type) {
		super(type==null ? "§2§lSelect Spleef Type" : "§2§lSelect Map",type!=null ? 6 : 3);
		
		
		new BukkitRunnable() {
		public void run() {
			
		cache.put(sp, sp2);
		
		if (type==null) {
			s(11,new ItemBuilder(Material.TNT).setTitle("§c§lTNT Run").addLore("§7Select Spleef Type").build());
			s(13,new ItemBuilder(Material.DIAMOND_SPADE).setTitle("§b§lSpleef").addLore("§7Select Spleef Type").build());
			s(15,new ItemBuilder(Material.EGG).setTitle("§e§lSplegg").addLore("§7Select Spleef Type").build());
		} else {
			
		if (sp.getDuelPage()==0) {
		int size = sp2.size()+1;
		size = size/2;
		int x = 19;
		List<String> main_arena = new ArrayList<String>();
		
		 
		List<String> alph = new ArrayList<String>();
		
		for (Arena arena : DataManager.getManager().getArenas()) {
			if (arena.getSpleefType().equals(type) && arena.getGameType().equals(GameType.DUEL))
			alph.add(arena.getName());
		}
		
		
		
		java.util.Collections.sort(alph);
		
		List<Arena> suggestedArenas = new ArrayList<Arena>();
		List<Arena> allArenas = new ArrayList<Arena>();
		for (String s : alph) {
			Arena arena = GameManager.getManager().getArenaByName(s);
			if (arena.getSpleefType().equals(type)) {
				String n = arena.getName();
				n = n.replaceAll("\\d","");
				if (!Utils.getUtils().containsIgnoreCase(main_arena, n)) {
					
					main_arena.add(arena.getName());		
				
			if (arena.getMaxPlayersSize()>=size && arena.getMinPlayersSize()<=size) {
				suggestedArenas.add(arena);
			} else {
				allArenas.add(arena);
			}
		}
		
			}
		}
		
		s(18,new ItemBuilder(Material.PAPER).setTitle("§aSuggested arenas for " + size+ "VS"+size).build());
		
		for (Arena arena : suggestedArenas) {
			
			int amount = GameManager.getManager().getAvailableArenasFor(arena.getName());
			String name = arena.getName().replaceAll("\\d","");
			name = name.toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			if (amount==0) {
				List<String> lore = new ArrayList<String>();
				lore.add("§8"+type.toString());
				for (SpleefPlayer sp_2 : sp2) {
					lore.add("§8"+sp_2.getOfflinePlayer().getName());
				}
				ItemStack item = new ItemBuilder(arena.getItem()).setTitle("§6"+name).build();
				ItemMeta meta = item.getItemMeta();
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				if (x % 9 == 0) {
					s(x,new ItemBuilder(Material.PAPER).setTitle("§aSuggested arenas for " + size+ "VS"+size).build());
					x++;
				}
				
			s(x,item);			
			} else {
				s(x,new ItemBuilder(Material.BARRIER).setTitle("§c"+name).addLore("§cNo available arenas for this map").build());
			}
			x++;
		}
		
		
		if (allArenas.size()>0) {
		if (x<=8) {
			x = 9;
		} else if (x<=17) {
			x= 18;
		}else if (x<=26) {
			x= 27;
		} else if (x<=35) {
			x = 36;
		} else if (x<=44) {
			x = 45;
		}
		
		
		s(x,new ItemBuilder(Material.PAPER).setTitle("§cNot recommended arenas for " + size+ "VS"+size).build());
		x++;		
		}
		
		for (Arena arena : allArenas) {
			
			int amount = GameManager.getManager().getAvailableArenasFor(arena.getName());
			String name = arena.getName().replaceAll("\\d","");
			name = name.toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			if (amount==0) {
				List<String> lore = new ArrayList<String>();
				lore.add("§8"+type.toString());
				for (SpleefPlayer sp_2 : sp2) {
					lore.add("§8"+sp_2.getOfflinePlayer().getName());
				}
				ItemStack item = new ItemBuilder(arena.getItem()).setTitle("§6"+name).build();
				ItemMeta meta = item.getItemMeta();
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				if (x % 9 == 0) {
					s(x,new ItemBuilder(Material.PAPER).setTitle("§cNot recommended arenas for " + size+ "VS"+size).build());
					x++;
				}
				
				
			s(x,item);			
			} else {
				s(x,new ItemBuilder(Material.BARRIER).setTitle("§c"+name).addLore("§cNo available arenas for this map").build());
			}
			x++;
		}
		
		
		
		
		ItemStack random = Utils.getUtils().getSkull("http://textures.minecraft.net/texture/d34e063cafb467a5c8de43ec78619399f369f4a52434da8017a983cdd92516a0","§aRandom Arena");
		ItemMeta meta = random.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add("§8"+type.toString());
		for (SpleefPlayer sp_2 : sp2) {
			lore.add("§8"+sp_2.getOfflinePlayer().getName());
		}
		
		meta.setLore(lore);
		random.setItemMeta(meta);
		

		s(13,random);
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

		if (stack.getItemMeta().getLore()!=null && stack.getItemMeta().getLore().get(0).equalsIgnoreCase("§7Select Spleef Type")) {
			SpleefType type = null;
			switch(stack.getItemMeta().getDisplayName()) {
			case "§c§lTNT Run": type = SpleefType.TNTRUN; break;
			case "§b§lSpleef": type = SpleefType.SPLEEF; break;
			case "§e§lSplegg": type = SpleefType.SPLEGG; break;
			}
			
			new DuelMenu(sp,cache.get(sp),type).o(sp.getPlayer());
			return;
		}
		if (stack.getItemMeta().getLore()==null) return;
		
		if (stack.getItemMeta().getLore().get(0).equalsIgnoreCase("§cNo available arenas for this map")) {
			return;
		}
	
		SpleefType type = SpleefType.valueOf(ChatColor.stripColor(stack.getItemMeta().getLore().get(0)));
		
		List<SpleefPlayer> dueled = new ArrayList<SpleefPlayer>();
		dueled.add(sp);
		for (String s : stack.getItemMeta().getLore()) {
			if (s.equalsIgnoreCase(stack.getItemMeta().getLore().get(0))) continue;
			s = ChatColor.stripColor(s);
			Player p = Bukkit.getPlayer(s);
			if (Bukkit.getOnlinePlayers().contains(p)) {
				SpleefPlayer sp_2 = SpleefPlayer.getSpleefPlayer(p);
				if (sp_2.isInGame()) {
					
					sp.getPlayer().closeInventory();
					sp.getPlayer().sendMessage("§cThis player is already in game.");
					return;
				} else {
					dueled.add(sp_2);
				}
			} else {
				sp.getPlayer().closeInventory();
				sp.getPlayer().sendMessage("§cThie player §b"+ s + " §cisnt online.");
				return;
			}
		}

		
		
		if (stack.getItemMeta().getDisplayName().equalsIgnoreCase("§aRandom Arena")) {
			SpleefDuel sduel = new SpleefDuel(sp, dueled, null,type);
			sp.getDuels().add(sduel);
			
			new BukkitRunnable() {
				public void run() {
					sp.getDuels().remove(sduel);
				}
			}.runTaskLater(Main.get(), 20*60L);
		} else {
			String arenaName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
			SpleefDuel sduel = new SpleefDuel(sp, dueled, arenaName,type);
			sp.getDuels().add(sduel);
			new BukkitRunnable() {
				public void run() {
					sp.getDuels().remove(sduel);
				}
			}.runTaskLater(Main.get(), 20*60L);
		}

		sendMessage(sp, dueled, type);
		
		
		
	}
	

	private void sendMessage(SpleefPlayer sp, List<SpleefPlayer> sp2,SpleefType type) {
		SpleefDuel duel = sp.getDuelByDueledPlayer(sp2.get(0));
		if (sp2.contains(sp)) sp2.remove(sp);
		
		if (sp2.size()==1) {
			String map = "Random map";
			if (duel.getArena()!=null) {
				map = duel.getArena();
			}
			
			sp.getPlayer().sendMessage("§aYou have sent a duel request to §b" + sp2.get(0).getName()+ "§a for " + type.toString() + " in map " + map+"!");
		sp2.get(0).getPlayer().sendMessage("§aThe Player §b" + sp.getName() + " §ahas sent you a duel request for "+ type.toString() + " in map " + map+ "! §7(This request expires in 1 minute.)");
		
		sp2.get(0).getPlayer().spigot().sendMessage(getInvitation(sp));	
		new AcceptDuelMenu(sp2.get(0), duel).o(sp2.get(0).getPlayer());	
		} else {
			int size = (sp2.size()+1)/2; 
			String mode = size+"VS"+size;
			sp.getPlayer().sendMessage("§aYou have sent a duel request to multiple players for " + mode + " in map " + duel.getArena()+"!");
			
			for (SpleefPlayer sp_2 : sp2) {
				sp_2.getPlayer().sendMessage("§aThe Player §b" + sp.getName() + " §ahas sent you a duel request for " + mode + " "+ type.toString() + " in map " + duel.getArena()+"! §7(This request expires in 1 minute.)");
				sp_2.getPlayer().spigot().sendMessage(getInvitation(sp));	
				new AcceptDuelMenu(sp_2, duel).o(sp_2.getPlayer());	
			}
		}
		
		TextComponent msg1 = new TextComponent("[CANCEL DUEL]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.RED );
		msg1.setBold( true );
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover duelcancel " + duel.getUUID().toString()));	
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cCancel duel request").create()));
		sp.getPlayer().spigot().sendMessage(msg1);
		sp.getPlayer().closeInventory();
	}
	
	private BaseComponent[] getInvitation(SpleefPlayer dueler) {
		TextComponent msg1 = new TextComponent("[ACCEPT]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
		msg1.setBold( true );
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover duelaccept " + dueler.getPlayer().getName()));	
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept duel request").create()));
		TextComponent msg2 = new TextComponent("[DENY]");
		msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
		msg2.setBold( true );
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover dueldeny "  + dueler.getPlayer().getName()));
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny duel request").create()));
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		return cb.create();
	}


	
	
	}



