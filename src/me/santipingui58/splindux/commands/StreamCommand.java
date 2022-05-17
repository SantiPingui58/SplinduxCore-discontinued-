package me.santipingui58.splindux.commands;

import java.net.URI;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StreamCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
			Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("stream")) {
			if (p.hasPermission("splindux.stream")) {
			if (args.length==0) {
				sender.sendMessage("§aUse of command: /stream <link>");
			} else {			
				try {
					URI uri = new URI(args[0]);
					String domain = uri.getHost();
				    String pag = domain.startsWith("www.") ? domain.substring(4) : domain;
				    if (Utils.getUtils().containsIgnoreCase(pag, "youtube") || Utils.getUtils().containsIgnoreCase(pag, "twitch")) {
				    	SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				    	if (!TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, TimeLimitType.STREAM_MC)) {
							if (!TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, TimeLimitType.STREAM_DISCORD)) {
								Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #streams "+"**"+ p.getName() + "** is livestreaming! Join them at: " + args[0]);
								TimeLimitManager.getManager().addTimeLimit(sp, 60*12, TimeLimitType.STREAM_DISCORD, null);
							}
							
							TextComponent msg1 = new TextComponent("§a§l" + p.getName().toUpperCase() + " §c§lIS LIVE!");
							msg1.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, args[0]));
							msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aOpen link").create()));
								
							TextComponent msg2 = new TextComponent("§7[Click here to watch]");
							msg2.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, args[0]));
							msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aOpen link").create()));
							
							
							for (Player pl : Bukkit.getOnlinePlayers()) {
								pl.sendMessage("");
								pl.sendMessage("");
								pl.sendMessage("");
								pl.sendMessage("§f§l-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
								pl.getPlayer().spigot().sendMessage(msg1);
								pl.getPlayer().spigot().sendMessage(msg2);
								pl.sendMessage("§f§l-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
								pl.sendMessage("");
							}
							TimeLimitManager.getManager().addTimeLimit(sp, 60, TimeLimitType.STREAM_MC, null);
							
						} else {
							p.sendMessage("§cPlease wait §a" + TimeLimitManager.getManager().getTimeLimitBy(sp, TimeLimitType.STREAM_MC).get(0).getLeftTime() + " §cto use this command.");
							return false;
						}
				    } else {
				    	p.sendMessage("§cYou can only use links from §fyoutube.com §cor §5twitch.tv");
						return false;
				    }
				} catch (Exception e) {
					p.sendMessage("§a" + args[0] + "§c is not a valid URL. (https://example.com/live/");
					return false;
				}
				
			
				
				
			}
			} 
			}
			

}
		
		
		return false;
	}
	

	
}