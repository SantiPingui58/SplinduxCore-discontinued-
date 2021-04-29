package me.santipingui58.splindux.commands;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.RelationshipRequestType;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.friends.Friendship;


public class FriendsCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("friends")){
			final Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				if (args.length==0) {
					sender.sendMessage("§aUse of command: /friend add <player>");
					sender.sendMessage("§aUse of command: /friend remove <player>");
					sender.sendMessage("§aUse of command: /friend list - /friends");
				} else {
					if (args[0].equalsIgnoreCase("add") && args.length==2) {
						
						int limit = p.hasPermission("splindux.extreme") ? 50 : p.hasPermission("splindux.epic") ? 30 : p.hasPermission("splindux.vip") ? 25 : 15;
						if (sp.getFriends().size()>=limit) {						
							   p.sendMessage("§cYour friend list is full.");
							   if (limit<50) {
							   String rank = limit==15 ?  "§a§l[VIP]" : limit==25 ? "§1§l[Epic]" : "§5§l[Extreme]";
				                p.sendMessage("§aGet a "+ rank+ "§aRank or higher to use unlock more friends! Visit the store for more info: §bhttp://store.splindux.com/");	
							   }
							return false;
						}
						
						  @SuppressWarnings("deprecation")
						OfflinePlayer friend = Bukkit.getOfflinePlayer(args[1]);
							if (!friend.hasPlayedBefore()) {
								p.sendMessage("§cThe player §b" + friend.getName() + "§c does not exist.");	
								return false;
							}
							SpleefPlayer sfriend = SpleefPlayer.getSpleefPlayer(friend);
							FriendsManager fm = FriendsManager.getManager();
							Friendship friendship = fm.getFriendship(sp.getUUID(), sfriend.getUUID());
							if (friendship==null) {
								RelationshipRequest request = fm.getFriendRequest(sp.getUUID(), sfriend.getUUID());
								if (request==null) {
									String[] argss = {sp.getOfflinePlayer().getName(),sfriend.getOfflinePlayer().getName()};
									List<UUID> list1 = new ArrayList<UUID>();
									List<UUID> list2= new ArrayList<UUID>();
									list1.add(sp.getUUID());
									list2.add(sfriend.getUUID());
									new RelationshipRequest(list1,list2,RelationshipRequestType.FRIENDS, argss);

								} else {
									 sender.sendMessage("§cYou already have a friend request open with the player §b" + args[1]); 
									  return false;
								}
							} else {
								  sender.sendMessage("§cThe player §b" + args[1] + " §cis already your friend.");
								  return false;
							}
						
					} else if (args[0].equalsIgnoreCase("remove")) {
						  @SuppressWarnings("deprecation")
							OfflinePlayer friend = Bukkit.getOfflinePlayer(args[1]);
								if (!friend.hasPlayedBefore()) {
									p.sendMessage("§cThe player §b" + friend.getName() + "§c does not exist.");	
									return false;
								}
								
								SpleefPlayer sfriend = SpleefPlayer.getSpleefPlayer(friend);
								FriendsManager fm = FriendsManager.getManager();
								Friendship friendship = fm.getFriendship(sp.getUUID(), sfriend.getUUID());
								if (friendship!=null) {
									fm.removeFriend(sp, sfriend.getUUID());
									 sender.sendMessage("§aYou have removed §c" + friend.getName() + "§a from your friend list.");
								} else {
									sender.sendMessage("§cThe player §b" + friend.getName() + "§c is not in your friend list.");
								}
								
					} else if (args[0].equalsIgnoreCase("list")) {
						if (args.length==1) {
						FriendsManager.getManager().sendFriendList(sp,1);
						} else {
							int page = 0;
							try {
								page = Integer.parseInt(args[1]);
								if (page<1) {
									p.sendMessage("§cThe page must be atleast 1.");
									return false;
								}
							} catch (Exception e) {
								p.sendMessage("§a"+ args[1]+ " §cisnt a valid number.");
								return false;
							}
							FriendsManager.getManager().sendFriendList(sp,page);
							
						}
					}
				
				}
		}
			

}
		
		
		return false;
	}
	
	

	
}