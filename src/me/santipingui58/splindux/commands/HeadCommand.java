package me.santipingui58.splindux.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;



public class HeadCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("head")){
			final Player p = (Player) sender;
			if (p.hasPermission("splindux.staff")) {
				if (args.length == 0) {
					
						ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);       
				        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
				        headMeta.setOwningPlayer(p);
				        head.setItemMeta(headMeta);
				        p.getInventory().setHelmet(head);
				        p.sendMessage("§aNow you are wearing the head of §6" + p.getName());
				 
				        
				} else {
						 if (args[0].length() <= 16) {
								String skin = null;
							 File file = new File("/home/splindux/proxy/plugins/SkinsRestorer/Players/"+args[0].toLowerCase()+".player");
							 if (file.exists()) {
								 BufferedReader br = null;
								try {
									br = new BufferedReader(new FileReader(file));
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								  
								  String st;
								  try {
									while ((st = br.readLine()) != null)
									  skin = st;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								  } else {
									  skin = args[0];
								  }
							 
							        
							 
					ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);       
			        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
			        headMeta.setOwner(skin);
			        headMeta.setDisplayName("§e"+args[0]+"'s Skull");
			        head.setItemMeta(headMeta);
			        p.getInventory().addItem(head);       
					} else {
				        	p.sendMessage("§cThat name is too long");			        
					}
					
				}
				
				
				
			} 
		}
		}
		
		return false;
			}
	
	
	
}