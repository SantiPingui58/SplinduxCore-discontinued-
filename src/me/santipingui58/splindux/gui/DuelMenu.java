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
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;





public class DuelMenu extends MenuBuilder {

	public static HashMap<Player,Integer> page = new HashMap<Player,Integer>();
	
	
	public DuelMenu(SpleefPlayer sp,SpleefPlayer sp2,SpleefType type) {
		super("§aDuel request to: §b" + sp2.getOfflinePlayer().getName(),3);
		if (sp.getDuelPage()==0) {
		
		int x = 0;
		List<String> main_arena = new ArrayList<String>();
		
		 
		List<String> alph = new ArrayList<String>();
		
		for (SpleefArena arena : DataManager.getManager().getArenas()) {
			alph.add(arena.getName());
		}
		
		
		java.util.Collections.sort(alph);
		
		for (String s : alph) {
			SpleefArena arena = GameManager.getManager().getArenaByName(s);
			if (arena.getType().equals(type)) {
				String n = arena.getName();
				n = n.replaceAll("\\d","");
				if (!Utils.getUtils().containsIgnoreCase(main_arena, n)) {
					
					main_arena.add(arena.getName());
					int amount = GameManager.getManager().getAvailableArenasFor(arena.getName());
					String name = arena.getName().replaceAll("\\d","");
					name = name.toLowerCase();
					name = name.substring(0, 1).toUpperCase() + name.substring(1);
					if (amount==0) {
						
					s(x,new ItemBuilder(arena.getItem()).setTitle("§6"+name).addLore("§8"+type.toString()).build());			
					} else {
						s(x,new ItemBuilder(Material.BARRIER).setTitle("§c"+name).addLore("§cNo available arenas for this map").build());
					}
					x++;
					
				}
			}
		}
		
		ItemStack random = Utils.getUtils().getSkull("http://textures.minecraft.net/texture/d34e063cafb467a5c8de43ec78619399f369f4a52434da8017a983cdd92516a0");
		ItemMeta meta = random.getItemMeta();
		meta.setDisplayName("§aRandom Arena");
		List<String> lore = new ArrayList<String>();
		lore.add("§8"+type.toString());
		meta.setLore(lore);
		random.setItemMeta(meta);
		s(x+1,random);
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
		
		String title = ChatColor.stripColor(sp.getPlayer().getPlayer().getOpenInventory().getTitle());	
		String [] split = title.split(" ", 4);
		String name = split[3];
		Player p2 = Bukkit.getPlayer(name);
		if (Bukkit.getOnlinePlayers().contains(p2)) {
			SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(p2);
			if (GameManager.getManager().isInGame(sp2)) {
				
				sp.getPlayer().closeInventory();
				sp.getPlayer().sendMessage("§cThis player is already in game.");
				return;
			}
		if (stack.getItemMeta().getDisplayName().equalsIgnoreCase("§aRandom Arena")) {
			SpleefDuel sduel = new SpleefDuel(sp, sp2, null,type);
			sp.getDueledPlayers().add(sduel);
			new BukkitRunnable() {
				public void run() {
					sp.getDueledPlayers().remove(sduel);
				}
			}.runTaskLater(Main.get(), 20*60L);
		} else {
			String arenaName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
			SpleefDuel sduel = new SpleefDuel(sp, sp2, arenaName,type);
			sp.getDueledPlayers().add(sduel);
			new BukkitRunnable() {
				public void run() {
					sp.getDueledPlayers().remove(sduel);
				}
			}.runTaskLater(Main.get(), 20*60L);
		}
		
		sendMessage(sp, p2, type);
		} else {
			sp.getPlayer().closeInventory();
			sp.getPlayer().sendMessage("§cThis player isnt online.");
		}
		
		
	}
	
	private void sendMessage(SpleefPlayer sp, Player p2,SpleefType type) {
		sp.getPlayer().sendMessage("§aYou have sent a duel request to §b" + p2.getName()+ "§a!");
		p2.sendMessage("§aThe Player §b" + sp.getPlayer().getName() + " §ahas sent you a duel request for "+ type.toString() + "! §7(This request expires in 1 minute.)");
		p2.spigot().sendMessage(getInvitation(sp));	
		sp.getPlayer().closeInventory();
	}
	
	private BaseComponent[] getInvitation(SpleefPlayer dueler) {
		TextComponent msg1 = new TextComponent("[ACCEPT]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
		msg1.setBold( true );
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover accept " + dueler.getPlayer().getName()));
		
		
		TextComponent msg2 = new TextComponent("[DENY]");
		msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
		msg2.setBold( true );
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover deny "  + dueler.getPlayer().getName()));
		
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		return cb.create();
	}
	
	
	}



