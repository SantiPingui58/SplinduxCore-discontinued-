package me.santipingui58.splindux.game.spleef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SpleefDuel {

	private String arena;
	private SpleefPlayer challenger;
	private List<SpleefPlayer> dueledPlayers;
	private List<SpleefPlayer> acceptedPlayers;
	private SpleefType type;
	private UUID uuid;
	
	public SpleefDuel(SpleefPlayer challenger, List<SpleefPlayer> sp2, String arena,SpleefType type) {
		uuid = UUID.randomUUID();
		this.challenger = challenger;
		this.dueledPlayers = sp2;
		this.arena = arena;
		this.type = type;
		this.acceptedPlayers = new ArrayList<SpleefPlayer>();
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	public SpleefPlayer getChallenger() {
		return this.challenger;
	}
	
	public List<SpleefPlayer> getDueledPlayers() {
		return this.dueledPlayers;
	}
	public List<SpleefPlayer> getAcceptedPlayers() {
		return this.acceptedPlayers;
	}
	
	public List<SpleefPlayer> getAllPlayers() {
		List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		list.addAll(dueledPlayers);
		list.add(challenger);
		return list;
	}
	
	public String getArena() {
		return this.arena;
	}
	
	public SpleefType getType() {
		return this.type;
	}

	public void acceptDuel(SpleefPlayer sp) {
		new BukkitRunnable() {
			public void run() {
		
		Player p = sp.getPlayer();
		if (getAcceptedPlayers().contains(sp)) {
			new BukkitRunnable() {
				public void run() {
			p.sendMessage("§cYou already accepted this request.");	
			}
			}.runTask(Main.get());
			return;
		}
		getAcceptedPlayers().add(sp);
		if (getAcceptedPlayers().size()>=getDueledPlayers().size()) {
			new BukkitRunnable() {
				public void run() {
					GameManager.getManager().duelGame(challenger, getDueledPlayers(), getArena(),getType(),getAllPlayers().size()/2,false,false,-1);
				}
			}.runTask(Main.get());
		
		} else {
			List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();		
			List<SpleefPlayer> leftToAccept = new ArrayList<SpleefPlayer>();		
			list.add(getChallenger());
			list.addAll(getDueledPlayers());
			
			for (SpleefPlayer splayer : getDueledPlayers()) {
				if (!getAcceptedPlayers().contains(splayer)) {
					leftToAccept.add(splayer);
				}
			}
			TextComponent msg1 = new TextComponent("[ACCEPT]");
			msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
			msg1.setBold( true );
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover duelaccept " + challenger.getPlayer().getName()));		
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept duel request").create()));
			TextComponent msg2 = new TextComponent("[DENY]");
			msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
			msg2.setBold( true );
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover dueldeny "  + challenger.getPlayer().getName()));
			msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny duel request").create()));
			
			ComponentBuilder cb = new ComponentBuilder(msg1);
			cb.append(" ");
			cb.append(msg2);
			BaseComponent[] bc =  cb.create();		
			new BukkitRunnable() {
				public void run() {
			for (SpleefPlayer dueled : list) {										
				dueled.getPlayer().sendMessage("§6"+sp.getName() + " §ahas accepted the request! §7(Left to accept: " + 
				Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
				if (dueled!=getChallenger()) {
					if (!getAcceptedPlayers().contains(dueled))
				dueled.getPlayer().spigot().sendMessage(bc);
			}
			}
				}
			}.runTask(Main.get());
		}
		
	}
	}.runTaskAsynchronously(Main.get());
}
}
