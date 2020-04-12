package me.santipingui58.splindux.gui;






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
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;





public class DuelMenu extends MenuBuilder {

	public static HashMap<Player,Integer> page = new HashMap<Player,Integer>();
	
	
	public DuelMenu(SpleefPlayer sp,List<SpleefPlayer> sp2,SpleefType type) {
		super("§aDuel request to: §b" + getDuelTo(sp2),4);
		if (sp.getDuelPage()==0) {
		int size = sp2.size()+1;
		int x = 1;
		List<String> main_arena = new ArrayList<String>();
		
		 
		List<String> alph = new ArrayList<String>();
		
		for (SpleefArena arena : DataManager.getManager().getArenas()) {
			if (arena.getSpleefType().equals(type) && arena.getGameType().equals(GameType.DUEL))
			alph.add(arena.getName());
		}
		
		
		
		java.util.Collections.sort(alph);
		
		List<SpleefArena> suggestedArenas = new ArrayList<SpleefArena>();
		List<SpleefArena> allArenas = new ArrayList<SpleefArena>();
		for (String s : alph) {
			SpleefArena arena = GameManager.getManager().getArenaByName(s);
			if (arena.getSpleefType().equals(type)) {
				String n = arena.getName();
				n = n.replaceAll("\\d","");
				if (!Utils.getUtils().containsIgnoreCase(main_arena, n)) {
					
					main_arena.add(arena.getName());				
			if (arena.getMaxPlayersSize()>=size) {
				suggestedArenas.add(arena);
			} else {
				Bukkit.broadcastMessage(""+size);
				allArenas.add(arena);
			}
		}
		
			}
		}
		
		s(0,new ItemBuilder(Material.PAPER).setTitle("§aSuggested arenas for " + size+ "VS"+size).build());
		
		for (SpleefArena arena : suggestedArenas) {
			
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
		}
		
		
		s(x,new ItemBuilder(Material.PAPER).setTitle("§aRest of arenas").build());
		x++;		
		}
		
		for (SpleefArena arena : allArenas) {
			
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
			s(x,item);			
			} else {
				s(x,new ItemBuilder(Material.BARRIER).setTitle("§c"+name).addLore("§cNo available arenas for this map").build());
			}
			x++;
		}
		
		
		
		
		ItemStack random = Utils.getUtils().getSkull("http://textures.minecraft.net/texture/d34e063cafb467a5c8de43ec78619399f369f4a52434da8017a983cdd92516a0");
		ItemMeta meta = random.getItemMeta();
		meta.setDisplayName("§aRandom Arena");
		List<String> lore = new ArrayList<String>();
		lore.add("§8"+type.toString());
		for (SpleefPlayer sp_2 : sp2) {
			lore.add("§8"+sp_2.getOfflinePlayer().getName());
		}
		
		meta.setLore(lore);
		random.setItemMeta(meta);
		
		if (x<=8) {
			x = 9;
		} else if (x<=17) {
			x= 18;
		}else if (x<=26) {
			x= 27;
		}else if (x<=35) {
			x= 36;
		}
		s(x,random);
		}
	}
	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		if (stack==null) {
			return;	
		}
		
		
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
				if (GameManager.getManager().isInGame(sp_2)) {
					
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
	
	private static String getDuelTo(List<SpleefPlayer> list) {
		if (list.size()==1) {
			return list.get(0).getOfflinePlayer().getName();
		} else {
			return list.size()+" players";
		}
	}
	
	private void sendMessage(SpleefPlayer sp, List<SpleefPlayer> sp2,SpleefType type) {
		SpleefDuel duel = sp.getDuelByDueledPlayer(sp2.get(0));
		if (sp2.contains(sp)) sp2.remove(sp);
		
		if (sp2.size()==1) {
			sp.getPlayer().sendMessage("§aYou have sent a duel request to §b" + sp2.get(0).getOfflinePlayer().getName()+ "§a!");
		sp2.get(0).getPlayer().sendMessage("§aThe Player §b" + sp.getPlayer().getName() + " §ahas sent you a duel request for "+ type.toString() + "! §7(This request expires in 1 minute.)");
		sp2.get(0).getPlayer().spigot().sendMessage(getInvitation(sp));	
	
		} else {
			int size = (sp2.size()+1)/2; 
			String mode = size+"VS"+size;
			sp.getPlayer().sendMessage("§aYou have sent a duel request to multiple players for " + mode +"!");
			
			for (SpleefPlayer sp_2 : sp2) {
				sp_2.getPlayer().sendMessage("§aThe Player §b" + sp.getPlayer().getName() + " §ahas sent you a duel request for " + mode + " "+ type.toString() + "! §7(This request expires in 1 minute.)");
				sp_2.getPlayer().spigot().sendMessage(getInvitation(sp));	
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



