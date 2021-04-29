package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ArenaRequest {
	private UUID uuid;
	private SpleefPlayer challenger;
	private List<SpleefPlayer> acceptedPlayers;
	private int amount;
	private RequestType type;
	public ArenaRequest(SpleefPlayer challenger,int amount,RequestType type) {
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
		Arena arena = challenger.getArena();
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
			players.getPlayer().sendMessage("§b"+challenger.getName() + "§6 has requested to crumble the field with " + this.amount+"%. §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				} else {
					players.getPlayer().sendMessage("§6You sent a crumble request to your opponent.");
				}
			} else {
				players.getPlayer().sendMessage("§b"+challenger.getName() + "§6 has requested to play to " + this.amount+". §7(Left to accept: " 
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

	public void crumbleAccept(SpleefPlayer sp) {
		getAcceptedPlayers().add(sp);
		Arena arena = sp.getArena();
		if (getAcceptedPlayers().size()>=arena.getPlayers().size()-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
			GameManager.getManager().crumbleWithCommand(arena,getAmount());
		} else {
			List<SpleefPlayer> leftToAccept = new ArrayList<SpleefPlayer>();		
			
			for (SpleefPlayer splayer : arena.getPlayers()) {
				if (!getAcceptedPlayers().contains(splayer) && !splayer.equals(getChallenger())
						&& !arena.getDeadPlayers1().contains(splayer)&& !arena.getDeadPlayers2().contains(splayer)) {
					leftToAccept.add(splayer);
				}
			}
			TextComponent msg1 = new TextComponent("[CRUMBLE]");
			msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
			msg1.setBold( true );
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbleaccept "));		
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept crumble request").create()));
			TextComponent msg2 = new TextComponent("[DENY CRUMBLE]");
			msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
			msg2.setBold( true );
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbledeny"));
			msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny crumble request").create()));
			
			ComponentBuilder cb = new ComponentBuilder(msg1);
			cb.append(" ");
			cb.append(msg2);
			BaseComponent[] bc =  cb.create();			
			if (leftToAccept.isEmpty()) {
				GameManager.getManager().crumbleWithCommand(arena,getAmount());
				return;
			} 
			for (SpleefPlayer dueled : arena.getPlayers()) {										
				dueled.getPlayer().sendMessage("§6"+sp.getName() + " §ahas accepted the request! §7(Left to accept: " + 
				Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
				if (dueled!=getChallenger()) {
					if (!getAcceptedPlayers().contains(dueled) && !arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp))
				dueled.getPlayer().spigot().sendMessage(bc);
			}
			}
		}
		
		
	}

	public void playtoAccept(SpleefPlayer sp) {
		getAcceptedPlayers().add(sp);
		Arena arena = sp.getArena();
		if (getAcceptedPlayers().size()>=arena.getPlayers().size()) {
			GameManager.getManager().playToWithCommand(arena, getAmount());
		} else {
			List<SpleefPlayer> leftToAccept = new ArrayList<SpleefPlayer>();		
			
			for (SpleefPlayer splayer : arena.getPlayers()) {
				if (!getAcceptedPlayers().contains(splayer) && !splayer.equals(getChallenger())) {
					leftToAccept.add(splayer);
				}
			}
			TextComponent msg1 = new TextComponent("[PLAYTO]");
			msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
			msg1.setBold( true );
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtoaccept "));	
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept play to request").create()));
			
			TextComponent msg2 = new TextComponent("[DENY PLAYTO]");
			
			msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
			msg2.setBold( true );
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtodeny"));
			msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny play to request").create()));
			ComponentBuilder cb = new ComponentBuilder(msg1);
			cb.append(" ");
			cb.append(msg2);
			BaseComponent[] bc =  cb.create();			
			if (leftToAccept.isEmpty()) {
				GameManager.getManager().playToWithCommand(arena, getAmount());
				return;
			} 
			for (SpleefPlayer dueled : arena.getPlayers()) {										
				dueled.getPlayer().sendMessage("§6"+sp.getName() + " §ahas accepted the request! §7(Left to accept: " + 
				Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
				if (dueled!=getChallenger()) {
					if (!getAcceptedPlayers().contains(dueled))
				dueled.getPlayer().spigot().sendMessage(bc);
			}
			}
		}
		
		
	}
	
}
