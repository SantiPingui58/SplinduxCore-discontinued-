package me.santipingui58.splindux.relationships;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.guilds.GuildDuel;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.translate.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class RelationshipRequest {

	private List<UUID> sender;
	private List<UUID> receptor;
	private RelationshipRequestType type;
	private String[] args;
	public RelationshipRequest(List<UUID> sender, List<UUID> receptor, RelationshipRequestType type,String args[]) {
	this.sender = sender;
	this.receptor = receptor;
	this.type = type;
	this.args = args;
	sendRequest();
	}
	
	public List<UUID> getSender() {
		return this.sender;
	}
	
	public List<UUID> getReceptor() {
		return this.receptor;
	}

	public RelationshipRequestType getType() {
		return this.type;
	}
	
	public void acceptRequest(String[] args) {
		
		switch(type) {
		case FRIENDS:
			 FriendsManager.getManager().beFriends(sender.get(0), receptor.get(0));
				FriendsManager.getManager().getFriendRequests().remove(this);
			break;
		case JOIN_GUILD_AS_PLAYER:			
			UUID u = sender.get(0);
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(receptor.get(0));		
			int i = Integer.valueOf(this.args[3]);
			GuildsManager.getManager().joinGuild(u,sp,i);
			GuildsManager.getManager().getJoinGuildPlayersRequests().remove(this);
			break;
		case GUILD_DUEL:
			SpleefPlayer accept = SpleefPlayer.getSpleefPlayer(UUID.fromString(args[0]));
			GuildDuel duel = GuildsManager.getManager().getGuildDuelByCreator(UUID.fromString(args[1]));		
			GuildsManager.getManager().acceptDuel(accept,duel);		
			break;
		case JOIN_GUILD_AS_MEMBER:
			GuildsManager.getManager().joinGuildAsMember(sender.get(0),receptor.get(0));
			GuildsManager.getManager().getJoinGuildMembersRequests().remove(this);
			break;
		case RENEGOCIATE_GUILD:
			GuildsManager.getManager().renegociate(sender.get(0),receptor.get(0),Integer.valueOf(this.args[1]));
			GuildsManager.getManager().getRenegociateRequests().remove(this);
			break;
		case BUY_PLAYER: 
			GuildsManager.getManager().getBuyPlayerRequests().remove(this);
			GuildsManager.getManager().buyPlayer(sender.get(0), SpleefPlayer.getSpleefPlayer(receptor.get(0)), Integer.valueOf(this.args[3]));
		}
	}
	
	public void sendRequest() {
		switch(this.type) {
		
		case FRIENDS: 
			FriendsManager.getManager().getFriendRequests().add(this);
			break;
		case JOIN_GUILD_AS_PLAYER:
			GuildsManager.getManager().getJoinGuildPlayersRequests().add(this);
			break;
		case JOIN_GUILD_AS_MEMBER:
			GuildsManager.getManager().getJoinGuildMembersRequests().add(this);
			break;
		case RENEGOCIATE_GUILD:
			GuildsManager.getManager().getRenegociateRequests().add(this);
			break;
		case GUILD_DUEL:
			GuildsManager.getManager().getDuelRequests().add(this);
		case BUY_PLAYER: 
			GuildsManager.getManager().getBuyPlayerRequests().add(this);
		}
		
		RelationshipRequest request = this;
		new BukkitRunnable() {
			public void run() {	
				switch(type) {
			case FRIENDS: 
				FriendsManager.getManager().getFriendRequests().remove(request);
				break;
			case JOIN_GUILD_AS_PLAYER:
				GuildsManager.getManager().getJoinGuildPlayersRequests().remove(request);
				break;
				case JOIN_GUILD_AS_MEMBER:
					GuildsManager.getManager().getJoinGuildMembersRequests().remove(request);
					break;
				case RENEGOCIATE_GUILD:
					GuildsManager.getManager().getRenegociateRequests().remove(request);
					break;
				case GUILD_DUEL:
					GuildsManager.getManager().getDuelRequests().remove(request);
					break;
				case BUY_PLAYER:
					GuildsManager.getManager().getBuyPlayerRequests().remove(request);
					break;
			}	
			}
		}.runTaskLaterAsynchronously(Main.get(), type.getExpirationTime());
		
		List<OfflinePlayer> se = new ArrayList<OfflinePlayer>();
		List<OfflinePlayer> re = new ArrayList<OfflinePlayer>();
		for (UUID u : sender) se.add(Bukkit.getOfflinePlayer(u));
		for (UUID u : receptor) re.add(Bukkit.getOfflinePlayer(u));

		TextComponent msg1 = new TextComponent("["+this.type.getAcceptText()+"]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
		msg1.setBold( true );
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover "+ this.type.getHoverAccept() + " " + se.get(0).getName()));		
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a"+this.type.getAcceptDescription(args)).create()));
		TextComponent msg2 =new TextComponent("["+this.type.getDenyText()+"]");
		msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
		msg2.setBold( true );
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover "+ this.type.getHoverDeny() + " " + se.get(0).getName()));	
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c"+this.type.getDenyDescription(args)).create()));
		
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		BaseComponent[] bc =  cb.create();						
		

			for (OfflinePlayer p : se) {
			if (p.isOnline()) {
				p.getPlayer().sendMessage(this.type.getSenderMessage(args));
			} 
			}
			
			
			for (OfflinePlayer p : re) {
				if (p.isOnline()) {
					p.getPlayer().sendMessage(this.type.getReceptorMessage(args));
					p.getPlayer().spigot().sendMessage(bc);
				} 
				}		
	}
}
