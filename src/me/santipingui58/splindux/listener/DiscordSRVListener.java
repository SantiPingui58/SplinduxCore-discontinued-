package me.santipingui58.splindux.listener;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.*;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.vote.Rewarded;
import me.santipingui58.splindux.vote.VoteManager;
import me.santipingui58.translate.Language;
import me.santipingui58.translate.TranslateAPI;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DiscordSRVListener {

    private final Plugin plugin;

    public DiscordSRVListener(Plugin plugin) {
        this.plugin = plugin;
    }


	//@Subscribe
    public void onDiscordToMinecraft(DiscordGuildMessagePreProcessEvent  e) {
    	e.setCancelled(true);
    	
    	Set<SpleefPlayer> spanish = new HashSet<SpleefPlayer>();
    	Set<SpleefPlayer> english = new HashSet<SpleefPlayer>();
    	Set<SpleefPlayer> russian = new HashSet<SpleefPlayer>();
    	Set<SpleefPlayer> notranslate = new HashSet<SpleefPlayer>();
    	for (Player p : Bukkit.getOnlinePlayers()) {
    		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
    		if (sp.getOptions().hasTranslate()) {
    		if (sp.getOptions().getLanguage().equals(Language.RUSSIAN)) {
    			russian.add(sp);
    		} else if (sp.getOptions().getLanguage().equals(Language.ENGLISH)) {
    			english.add(sp);
    		} else if (sp.getOptions().getLanguage().equals(Language.SPANISH)) { 
    			spanish.add(sp);
    		}
    		}
    		
    		notranslate.add(sp);
    	}
    
    	
    	UUID discordUserUUID = DiscordSRV.getPlugin().getAccountLinkManager().getUuid(e.getAuthor().getId());
    	if (discordUserUUID!=null) {
    		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(discordUserUUID);
			if (temp==null) {
				 new SpleefPlayer(discordUserUUID);
				HikariAPI.getManager().loadData(discordUserUUID);

			}
    	} 
		
				new BukkitRunnable() {
					public void run() {		
						Language from = null;
						if (discordUserUUID!=null) {
						SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(discordUserUUID);
						 from = sp.getOptions().getLanguage();
						} 
						
						try {
							if (spanish.size()>0 && from!=null && !from.equals(Language.SPANISH)) {
 							TranslateAPI.getAPI().translate(from, Language.SPANISH, e.getMessage().getContentStripped()).thenAccept(text -> {	
								for (SpleefPlayer player : spanish) player.sendMessage("§b[Discord] "+e.getAuthor().getAsTag() +": "+ text);
							});
							} else {
								notranslate.addAll(spanish);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						try {
							if (russian.size()>0 && from!=null && !from.equals(Language.RUSSIAN)) {
 							TranslateAPI.getAPI().translate(from, Language.RUSSIAN, e.getMessage().getContentStripped()).thenAccept(text -> {	
								for (SpleefPlayer player : russian) player.sendMessage("§b[Discord] "+e.getAuthor().getAsTag() +": "+ text);
							});
							} else {
								notranslate.addAll(russian);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						try {
							if (english.size()>0 && from!=null && !from.equals(Language.ENGLISH)) {
 							TranslateAPI.getAPI().translate(from, Language.ENGLISH, e.getMessage().getContentStripped()).thenAccept(text -> {	
								for (SpleefPlayer player : english) player.sendMessage("§b[Discord] "+e.getAuthor().getAsTag() +": "+ text);
							});
							} else {
								notranslate.addAll(english);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
						for (SpleefPlayer player : notranslate) player.sendMessage("§b[Discord] "+e.getAuthor().getAsTag() +": "+ e.getMessage().getContentStripped());
						
			}
					}.runTaskLaterAsynchronously(Main.get(), 2L);
			
    	
    	
    	
    	
    }
    
	
	

    @Subscribe
    public void accountsLinked(AccountLinkedEvent e) {
        // Example of broadcasting a message when a new account link has been made
        SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(e.getPlayer());
		  if (!sp.getVoteClaims().hasClaimed(sp,Rewarded.DISCORD)) {
			  sp.getVoteClaims().claim(Rewarded.DISCORD, true);
			  VoteManager.getManager().suscribe(sp, Rewarded.DISCORD);
		  }
    }

    @Subscribe
    public void accountUnlinked(AccountUnlinkedEvent event) {
        // Example of DM:ing user on unlink
        User user = DiscordUtil.getJda().getUserById(event.getDiscordId());

        // will be null if the bot isn't in a Discord server with the user (eg. they left the main Discord server)
        if (user != null) {

            // opens/retrieves the private channel for the user & sends a message to it (if retrieving the private channel was successful)
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Your account has been unlinked").queue());
        }

        // Example of sending a message to a channel called "unlinks" (defined in the config.yml using the Channels option) when a user unlinks
        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("unlinks");

        // null if the channel isn't specified in the config.yml
        if (textChannel != null) {
            textChannel.sendMessage(event.getPlayer().getName() + " (" + event.getPlayer().getUniqueId() + ") has unlinked their associated Discord account: "
                    + (event.getDiscordUser() != null ? event.getDiscordUser().getName() : "<not available>") + " (" + event.getDiscordId() + ")").queue();
        } else {
            plugin.getLogger().warning("Channel called \"unlinks\" could not be found in the DiscordSRV configuration");
        }
    }

}
