package me.santipingui58.splindux.commands;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.utils.ValueComparator;



public class PingCommand implements CommandExecutor{

	
	public static TreeMap<String, Integer> sortedPing = new TreeMap <String, Integer> ();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("ping")){
			sortPing();
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage("§aPing: §b" + getPing(p) + "ms");
			} else if (args.length >= 1 && args[0].equalsIgnoreCase("all")) {
				
				p.sendMessage("§aPing:");
				
				for(int i = 1; i < Bukkit.getOnlinePlayers().size()+1 ; i++){
					Entry<String, Integer> e = sortedPing.pollLastEntry();
					String pname = e.getKey();
					Integer score = e.getValue();
				p.sendMessage("§3" + pname + "§7:§e " + score + "§ems");
				
			}
			
			
	} else {
		p.sendMessage("§aPing: §e" + getPing(p) + "ms");
	}
}
		} 
		return false;
		
	} 

	
	
	public static int getPing(Player player) {
	    return ((CraftPlayer) player).getHandle().ping;
	}
	
public static void sortPing() {
		
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
    	for (Player s : Bukkit.getOnlinePlayers()) {
    		map.put(s.getName(), getPing(s));
    	}

		 sortedPing = sortMapByValue(map);
		 
		return;
		
	}
 
	public static TreeMap<String, Integer> sortMapByValue(TreeMap<String, Integer> map){
		Comparator<String> comparator = new ValueComparator(map);
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		return result;
	}
	
}
