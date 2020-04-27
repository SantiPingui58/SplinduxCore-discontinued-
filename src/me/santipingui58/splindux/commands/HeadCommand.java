package me.santipingui58.splindux.commands;

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
			if (p.hasPermission("splindux.head")) {
				if (args.length == 0) {
					
						ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);       
				        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
				        headMeta.setOwningPlayer(p);
				        head.setItemMeta(headMeta);
				        p.getInventory().setHelmet(head);
                                                   p.sendMessage("�aNow you are wearing the head of �6 " + p.getName());
				 
				        
				} else {
					if (args[0].equalsIgnoreCase("clear")) {
						  p.getInventory().setHelmet(null);
					} else {
						 if (args[0].length() <= 16) {
					ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);       
			        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
			        headMeta.setOwner(args[0]);
			        head.setItemMeta(headMeta);
			        p.getInventory().setHelmet(head);
			        
			        	p.sendMessage("�aNow you are wearing the head of �6 " + args[0]);		        
					} else {
				        	p.sendMessage("�cThat name is too long");			        
					}
					}
				}
				
				
				
			}  else {
					p.sendMessage("�cYou don't have permission to execute this command.");
					p.sendMessage("�aYou need a rank "
							+ "�5�l[Extreme] �ato use this, visit the store for more info: �bhttp://splinduxstore.buycraft.net/");

			} 
		}
		}
		
		return false;
			}
	
	
	
}