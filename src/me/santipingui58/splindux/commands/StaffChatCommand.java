package me.santipingui58.splindux.commands;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;





public class StaffChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("staffchat")){
			
			List<Player> list = new ArrayList<Player>();
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online.hasPermission("splindux.staff")) {
					list.add(online);
					}
			}
			Player p = (Player) sender;
			
			StringBuilder builder = new StringBuilder();
		    for (int i = 0; i < args.length; i++)
		    {
		      builder.append(args[i]).append(" ");
		    }
		  String message = builder.toString();
		  
		  for (Player staff : list) {
			  staff.sendMessage("§c[Staff] §f" + p.getName() + "§8: §e" + message);
		  }
		
	}


		}
		return false;
}

}