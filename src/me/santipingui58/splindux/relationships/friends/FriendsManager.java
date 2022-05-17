package me.santipingui58.splindux.relationships.friends;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FriendsManager {

	private static FriendsManager manager;	
	 public static FriendsManager getManager() {
	        if (manager == null)
	        	manager = new FriendsManager();
	        return manager;
	    }
	 
	 
		private List<RelationshipRequest> friendRequests = new ArrayList<RelationshipRequest>();

		private Set<Friendship> friendships = new HashSet<Friendship>();
		
		
		public List<RelationshipRequest> getFriendRequests() {
			return this.friendRequests;
		}
		
		public Set<Friendship> getFriendships() {
			return this.friendships;
		}
		
		
	public Friendship getFriendship(UUID sp1, UUID sp2) {
		for (Friendship f : this.friendships) {
			if ((f.getPlayer1().compareTo(sp1)==0 && f.getPlayer2().compareTo(sp2)==0) || f.getPlayer2().compareTo(sp1)==0 && f.getPlayer1().compareTo(sp2)==0) {
				return f;
			}
		}
		return null;
	}
	
	public void beFriends(UUID s1, UUID s2) {
		SpleefPlayer sp1 = SpleefPlayer.getSpleefPlayer(s1);
		SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(s2);
		
		Friendship friendship = getFriendship(s1,s2);
		if (friendship==null) {
		Friendship fr = new Friendship(sp1.getUUID(),sp2.getUUID());
		HikariAPI.getManager().saveFriend(fr);
}
		
	}
	
	
	
	
	public void removeFriend(SpleefPlayer sp1, UUID sp2) {
		Friendship friendship = getFriendship(sp1.getUUID(),sp2);
		if (friendship!=null) {
			FriendsManager.getManager().getFriendships().remove(friendship);	
			HikariAPI.getManager().deleteFriend(friendship);
}
	}
	

	
	public RelationshipRequest getFriendRequest(UUID sp1 ,UUID sp2) {
		for (RelationshipRequest fr: getFriendRequests()) {
			if ((fr.getReceptor().contains(sp2) && fr.getSender().contains(sp1)) || (fr.getReceptor().contains(sp1) && fr.getSender().contains(sp2))) {
				return fr;
			}
		}
		return null;
	}

	
	public List<RelationshipRequest>  getFriendRequestReceived(UUID sp) {
		List<RelationshipRequest> list = new ArrayList<RelationshipRequest>();
		for (RelationshipRequest fr: getFriendRequests()) {
			if (fr.getReceptor().contains(sp)) {
				list.add(fr);
			}
		}
		return list;
	}


	public List<RelationshipRequest> getAllFriendRequest(UUID uuid) {
		List<RelationshipRequest> list = new ArrayList<RelationshipRequest>();
		list.addAll(getFriendRequestSent(uuid));
		list.addAll(getFriendRequestReceived(uuid));
		return list;
	}

	
	
	public List<RelationshipRequest>  getFriendRequestSent(UUID sp) {
		List<RelationshipRequest> list = new ArrayList<RelationshipRequest>();
		for (RelationshipRequest fr: getFriendRequests()) {
			if (fr.getSender().contains(sp)) {
				list.add(fr);
			}
		}
		return list;
	}

	public void sendFriendList(SpleefPlayer sp,int page) {
		TreeMap<UUID,Integer> friends = new TreeMap<UUID,Integer>();
	
		
		if (sp.getFriends().size()==0) {
			sp.sendMessage("§cYou don't have friends yet. Use §b/friend add <Nick> §cto send friend requests!");
		} else {		
			
			int total = (sp.getFriends().size()/10)+1;		
			
			if (page>total) {
				sp.sendMessage("§cPage not found.");
				return;
			} 
			new BukkitRunnable() {
				public void run() {		
			
			for (UUID u : sp.getFriends()) {
				SpleefPlayer sfriend = SpleefPlayer.getSpleefPlayer(u);
				if (sfriend==null) {
					 new SpleefPlayer(u);
					HikariAPI.getManager().loadData(u);
				
							SpleefPlayer ssfriend = SpleefPlayer.getSpleefPlayer(u);
							if (ssfriend.getOfflinePlayer().isOnline()) {
								friends.put(u, 0);
							} else {
								friends.put(u, DataManager.getManager().getTimeLastConnection(ssfriend));
							}
				}else {
					if (sfriend.getOfflinePlayer().isOnline()) {
						friends.put(u, 0);
					} else {
						
						friends.put(u, DataManager.getManager().getTimeLastConnection(sfriend));
					}
				}					
				} 
			}
			}.runTaskAsynchronously(Main.get());
			
	
			
			
			new BukkitRunnable() {
				public void run() {	
					
			List<UUID> fr = new ArrayList<UUID>();
			  int inicio = ((page-1)*10)+1;
			  int fin = (page)*10;
			  int i = 1;
			  
			  for (Entry<UUID, Integer> entry  : entriesSortedByValues(friends)) {
				   if (i<=fin) {
			        	if (i>=inicio) {
			        		fr.add(entry.getKey());
			        	}
			        i++;
			    }  else {
			    	break;
			    }
				}	        
			List<TextComponent> text = new ArrayList<TextComponent>();
			
			for (UUID u : fr) {
				SpleefPlayer ssfriend = SpleefPlayer.getSpleefPlayer(u);
				text.add(friendListMsg(sp,ssfriend));

			}
			

					sp.sendMessage("§6-=-=-=-[§e§lFriends ("+page+"/"+total+")§6]-=-=-=-");
			for (TextComponent tc : text) sp.getPlayer().spigot().sendMessage(tc);
		
					sp.sendMessage("§6-=-=-=-[§e§lFriends ("+page+"/"+total+")§6]-=-=-=-");
			}
			}.runTaskLater(Main.get(),30L);
			
		
	}
	}
	
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1; // Special fix to preserve items with equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
	
	
	public TextComponent friendListMsg(SpleefPlayer p, SpleefPlayer friend) {
		//Mandar Msg Offline   ✉
		//Mandar msg Online (/msg SantiPingui58)     ✎
		//Enviar regalo ❥
		//Ultima conexión si está offline  ⌛
		//Donde está si está conectado     ✚
		//Eliminar amigo  ✖
		//Mandar duel ✪
		//nivel de amistad ❣ 
		
		//ONLINE
		//SantiPingui58 [✎] [✚] [✪] [❥] [❣ ] [✖] 
		//OFFLINE
		//SantiPingui58 [✉] [⌛] [❥] [❣ ] [✖]
		
		TextComponent message = new TextComponent();
		TextComponent name_on = new TextComponent("§6"+friend.getName());
		TextComponent name_off = new TextComponent ("§c"+friend.getName());
		TextComponent msg_on = new TextComponent("§e§l[✎]");
		msg_on.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + friend.getName()));
			msg_on.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eSend a private message.").create()));
		
		TextComponent msg_off = new TextComponent("§e§l[✉]");
		msg_off.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mail send " + friend.getName()));
		msg_off.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eSend a private message to a offline player. §c(Coming soon)").create()));
		
		TextComponent regalo = new TextComponent ("§d§l[❥]");
		regalo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover gift " + friend.getOfflinePlayer().getUniqueId().toString()));
		regalo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dSend a §625 Coins §das Gift (Every 24 hours)").create()));
		TextComponent lastcon = new TextComponent("§a§l[⌛]");		
		lastcon.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aLast seen: "+ DataManager.getManager().getLastConnection(friend)).create()));
		
		TextComponent delete = new TextComponent("§c§l[✖]");
		delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cRemove friend.").create()));	
		delete.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend remove " + friend.getName()));
		
		
		TextComponent amistad = new TextComponent("§9§l[❣]");
		amistad.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§9Friendship level: §cComing Soon").create()));
	
		if (friend.getOfflinePlayer().isOnline()) {
			TextComponent where = new TextComponent("[✚]");
			if (friend.isInArena()) {
				Arena arena = friend.getArena();		
				where.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fPlaying §b" + GameManager.getManager().getGamePrefix(arena) + "§7 [Click to spectate]").create()));
				where.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spectate " + friend.getName()));
				} else if (friend.isSpectating()) {
					Arena spectating = friend.getSpleefArenaSpectating();
					where.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fSpectating §b" + GameManager.getManager().getGamePrefix(spectating) + "§7 [Click to spectate]").create()));
		
						
					for (SpleefPlayer sppp : spectating.getPlayers()) {
						where.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spectate " + sppp.getOfflinePlayer()));
						break;
					}
			}else  {
				where.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fCurrently at the Lobby").create()));			
			}
			
			 message.addExtra(name_on);
			 message.addExtra(" ");
			 message.addExtra(msg_on);
			 message.addExtra(" ");
			 message.addExtra(where);
			 message.addExtra(" ");
			 message.addExtra(regalo);
			 message.addExtra(" ");
			 message.addExtra(amistad);
			 message.addExtra(" ");
			 message.addExtra(delete);
		} else {
		 message.addExtra(name_off);
		 message.addExtra(" ");
		 message.addExtra(msg_off);
		 message.addExtra(" ");
		 message.addExtra(lastcon);
		 message.addExtra(" ");
		 message.addExtra(regalo);
		 message.addExtra(" ");
		 message.addExtra(amistad);
		 message.addExtra(" ");
		 message.addExtra(delete);
	 
		}
		
		return message;
	}

	public void saveFriendships() {
		List<Friendship> toRemove = new ArrayList<Friendship>();
		for (Friendship f : this.friendships) {
			if (!Bukkit.getOfflinePlayer(f.getPlayer1()).isOnline() && !Bukkit.getOfflinePlayer(f.getPlayer2()).isOnline()) toRemove.add(f);
		}	
		this.friendships.removeAll(toRemove);
		
	}
	
	


	
}
