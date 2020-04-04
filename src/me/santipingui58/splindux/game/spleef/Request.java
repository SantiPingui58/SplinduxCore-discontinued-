package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Request {
	private UUID uuid;
	private SpleefPlayer challenger;
	private List<SpleefPlayer> acceptedPlayers;
	private int amount;
	private RequestType type;
	public Request(SpleefPlayer challenger,int amount,RequestType type) {
		uuid = UUID.randomUUID();
		this.type = type;
		this.challenger = challenger;
		this.acceptedPlayers = new ArrayList<SpleefPlayer>();
		this.amount = amount;
	}
	
	public RequestType getType() {
		return this.type;
	} 
	
	public int getAmount() {
		return this.amount;
	}
	public UUID getUUID() {
		return this.uuid;
	}
	public SpleefPlayer getChallenger() {
		return this.challenger;
	}
	
	public List<SpleefPlayer> getAcceptedPlayers() {
		return this.acceptedPlayers;
	}
	
	public void sendMessage() {
		SpleefArena arena = GameManager.getManager().getArenaByPlayer(challenger);
		List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();		
		
		for (SpleefPlayer splayer : arena.getPlayers()) {
			if (!getAcceptedPlayers().contains(splayer) && !splayer.equals(getChallenger())) {
				if (this.type.equals(RequestType.CRUMBLE) && !arena.getDeadPlayers1().contains(splayer)&& !arena.getDeadPlayers2().contains(splayer)) { 
				list.add(splayer);
			} else  {
				list.add(splayer);
			}
				
				
			}
		}
		
		for (SpleefPlayer players : arena.getViewers()) {
			if (this.type.equals(RequestType.CRUMBLE)) {
				if (players!=this.challenger) {
			players.getPlayer().sendMessage("§b"+challenger.getOfflinePlayer().getName() + "§6 has requested to crumble the field with " + this.amount+"%. §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				} else {
					players.getPlayer().sendMessage("§6You sent a crumble request to your opponent.");
				}
			} else {
				players.getPlayer().sendMessage("§b"+challenger.getOfflinePlayer().getName() + "§6 has requested to play to " + this.amount+". §7(Left to accept: " 
						+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
			}
			if (arena.getPlayers().contains(players)) {
			if (players!=this.challenger) {
				if (this.type.equals(RequestType.PLAYTO)) {
				players.getPlayer().spigot().sendMessage(getInvitation(this.challenger));
				} else {
					if (!arena.getDeadPlayers1().contains(players) && !arena.getDeadPlayers2().contains(players))
					players.getPlayer().spigot().sendMessage(getInvitation(this.challenger));
				}
				
			} else {
				
				TextComponent msg1 = null;
				if (this.type.equals(RequestType.CRUMBLE)) {
					msg1=new TextComponent("[CANCEL CRUMBLE]");
				} else {
					msg1=new TextComponent("[CANCEL PLAYTO]");
				}
				msg1.setColor(net.md_5.bungee.api.ChatColor.RED );
				msg1.setBold( true );
				if (this.type.equals(RequestType.CRUMBLE)) {
				msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumblecancel"));	
				msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cCancel crumble request").create()));
				} else {
					msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtocancel"));	
					msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cCancel play to request").create()));
				}
				ComponentBuilder cb = new ComponentBuilder(msg1);
				this.challenger.getPlayer().spigot().sendMessage(cb.create());
			}
			}
		}
	}
	
	private BaseComponent[] getInvitation(SpleefPlayer dueler) {
		TextComponent msg1 = null;
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg1	= new TextComponent("[CRUMBLE]");
		} else  {
			msg1	= new TextComponent("[PLAYTO]");
		}
		msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN);
		msg1.setBold( true );
		
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbleaccept"));	
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept crumble request").create()));
		} else {
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtoaccept"));	
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept play to request").create()));
		}
		TextComponent msg2 = null;
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg2	= new TextComponent("[DENY CRUMBLE]");
		} else  {
			msg2	= new TextComponent("[DENY PLAYTO]");
		}
		msg2.setColor(net.md_5.bungee.api.ChatColor.RED);
		msg2.setBold( true );
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtodeny"));
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny crumble request").create()));
		} else {
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtodeny"));
			msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny play to request").create()));
		}
		
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		return cb.create();
	}
	
}
