package me.santipingui58.splindux.anouncements;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;

public class Ad extends Announcement {

	public Ad(UUID uuid, String message, String url) {
		super(uuid, message, url);
	}

	public void sendAd(SpleefPlayer sp) {
		if (sp.getOptions().hasAds())  {
		Player p = sp.getPlayer();
		String msg = this.getMessage();
		msg = msg.replaceAll("%name%", p.getName());
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("§a§l-=-=-=-=-=-=-[§2§lAD§a§l]-=-=-=-=-=-=-");
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getMessage()) + " §3§l" + this.getURL());
		p.sendMessage("§a§l-=-=-=-=-=-=-[§2§lAD§a§l]-=-=-=-=-=-=-");
		p.sendMessage("");
		p.sendMessage("");
	}
	}
}
