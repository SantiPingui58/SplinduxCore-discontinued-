package me.santipingui58.splindux.gui;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.ItemBuilder;






public abstract class MenuBuilder implements Listener {
    Main plugin;
    
    public MenuBuilder(Main plugin) {
    this.plugin = plugin;
  }
    
	Inventory _inv;
	HashMap<Integer,ItemStack> inventory = new HashMap<Integer,ItemStack>();
	
	public MenuBuilder(String name, int rows){
		_inv = Bukkit.createInventory(null, 9 * rows, ChatColor.translateAlternateColorCodes('&', name));
		_inv.setItem(rows%2 == 0 ? (rows*9)-5 : ((rows-1)*9)-5, new ItemBuilder(Material.STAINED_GLASS,1,(byte)14).setTitle("§cLoading...").build());
		Main.get().getServer().getPluginManager().registerEvents(this, Main.get());
		
	}
	
	public  void a(ItemStack stack){
		_inv.addItem(stack);
	}
	public void s(int i , ItemStack stack){
		//_inv.setItem(i, stack);
		inventory.put(i, stack);
	}
	
	public void buildInventory() {
		_inv.clear();
		for (Entry<Integer, ItemStack> entry : inventory.entrySet()) {
		    int i = entry.getKey();
		    ItemStack stack = entry.getValue();
		    _inv.setItem(i, stack);
		}
		
	}
	public Inventory i(){
		return _inv;
	}
	public String n()
	{
		return _inv.getName();
	}
	public void o(Player p){
		new BukkitRunnable() {
			public void run() {
		p.openInventory(_inv);
		}
		}.runTask(Main.get());
	}

	  @EventHandler
	    public void onInventoryClick(InventoryClickEvent event) {
	        if (event.getInventory().equals(this.i())) {
	            if (event.getCurrentItem() != null && this.i().contains(event.getCurrentItem()) && event.getWhoClicked() instanceof Player) {
	            	Player p = (Player) event.getWhoClicked();
	            	SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	                this.onClick(sp, event.getCurrentItem(), event.getSlot());
	                event.setCancelled(true);
	            }
	        }
	    }
	  @EventHandler
	    public void onInventoryClose(InventoryCloseEvent event) {
	        if (event.getInventory().equals(this.i()) && event.getPlayer() instanceof Player) {
	            this.onClose((Player) event.getPlayer());
	        }
	    }
	  public void onClose(Player player) {}
	  public abstract void onClick(SpleefPlayer sp, ItemStack stack, int slot);
}