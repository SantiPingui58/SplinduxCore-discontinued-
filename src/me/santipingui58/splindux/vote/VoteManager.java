package me.santipingui58.splindux.vote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.economy.EconomyManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.utils.URLUtils;
import me.santipingui58.splindux.vote.timelimit.TimeLimitManager;
import me.santipingui58.splindux.vote.timelimit.TimeLimitType;
import me.santipingui58.translate.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class VoteManager {

	
	private static VoteManager manager;	
 public static VoteManager getManager() {
        if (manager == null)
        	manager = new VoteManager();
        return manager;
    }
 

public ItemStack getItem(SpleefPlayer sp,Rewarded network) {
	 ItemStack item = network.getItem();	 
		List<String> lore = new ArrayList<String>();
		TimeLimitType timelimit = TimeLimitType.fromRewarded(network);
		if (sp.getVoteClaims().hasClaimed(sp,network) && !network.isServerList()) {
			lore.add("§cYou already claimed this reward.");
		} else if (TimeLimitManager.getManager().hasActiveTimeLimitBy(sp, timelimit)) {
			lore.add("§cYou can claim this reward again in:");
			lore.add("§e" +TimeLimitManager.getManager().getTimeLimitBy(sp, timelimit).get(0).getLeftTime());
		} else {
			VoteRewards rewards = network.getRewards();
			lore.add("§e§lRewards:");
			
			if (rewards.getCoins()>0) lore.add("§7- §6"+ rewards.getCoins()+ " Coins");
			if (rewards.getExp()>0) lore.add("§7- §b"+rewards.getExp() +" SpleefEXP");
			
			if (rewards.getSplinboxes()!=null) {
				int amount = 0;
				int level = 0;
				 Iterator<Entry<Integer, Integer>> it = rewards.getSplinboxes().entrySet().iterator();
				    while (it.hasNext()) {
				    	Map.Entry<Integer,Integer> pair = (Map.Entry<Integer,Integer>)it.next();
				    	amount = pair.getKey();
				    	level = pair.getValue();
				    }
				lore.add("§7- §3 " + amount +" SplinBox(es) "+ level + " Stars");
			}
				
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
 }
 

	public void sendInstructions(SpleefPlayer sp,Rewarded rewarded) {
		Player p = sp.getPlayer();
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage(rewarded.getColor()+"-=-=-=-=-=-=-=-=-=-=-=-=-");
		TextComponent message = new TextComponent("Click to go to " + rewarded.getName());
		message.setColor(ChatColor.GREEN);
		message.setBold(true);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,rewarded.getURL()));

		String prefix = "§bFollow us at ";
		if (rewarded.equals(Rewarded.NAMEMC)) {
			prefix = "§bLike us at";
		} else if (rewarded.isServerList()) {
			prefix = "§bVote us at";
		} 
		message.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(prefix + rewarded.getColor() + rewarded.getName()+"!").create()));				
		p.spigot().sendMessage(message);
		p.sendMessage(rewarded.getColor()+"-=-=-=-=-=-=-=-=-=-=-=-=-");

		if (rewarded.equals(Rewarded.NAMEMC)) {
			if (!sp.getVoteClaims().hasClaimed(sp,rewarded)) {
			p.sendMessage("§7You need to create an account at NameMC if you don't have one, then like the server.");
			p.sendMessage("§7Once you did this, click the button below");
			TextComponent button = new TextComponent("§6§l[Claim §6§l" + rewarded.getName() + " §6§lReward]");			
			button.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/hover votenamemc"));
			button.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bClick here to claim reward!").create()));
			p.spigot().sendMessage( button);			
			}
	} else if (rewarded.equals(Rewarded.DISCORD)) {
		p.sendMessage("§7After joining our Discord Server, you need to link your Minecraft account. To do so you have to");
		p.sendMessage("§7do /discord link then follow the instructions.");
		
	} else if (!rewarded.isServerList()){
		VoteManager.getManager().suscribe(sp, rewarded);
	}
	}	
	
	
	

	public void voteNameMC(SpleefPlayer sp) {
		if (URLUtils.getURLUtils().onNameMCVote(sp)) {
		if (!sp.getVoteClaims().hasClaimed(sp,Rewarded.NAMEMC)) {
			EconomyManager.getManager().addCoins(sp, 300, true, false);
			LevelManager.getManager().addLevel(sp, 25);
			sp.getPlayer().sendMessage("§e§lSplin§b§lDux§5§lVotes §aYou have claimed your rewards for voting at NameMC!");
			sp.getVoteClaims().claim(Rewarded.NAMEMC, true);
		} else {
			sp.getPlayer().sendMessage("§cYou already claimed this reward.");
		}		
	} else {
		sp.getPlayer().sendMessage("§cYou have not voted at NameMC yet.");
	}
	}
	

	
	public void suscribe(SpleefPlayer sp, Rewarded red) {
		
		if (red.isServerList()) return;
		new BukkitRunnable() {
			public void run() {
		if (red.isDailyClaimeable()) {
			TimeLimitManager.getManager().addTimeLimit(sp, 1440, TimeLimitType.fromRewarded(red),null);
		}
			if (!sp.getVoteClaims().hasClaimed(sp,red)) {
				sp.getVoteClaims().claim(red, true);	
				red.getRewards().giveRewards(sp);
				
			}
			}
	}.runTaskLater(Main.get(),200L);
	}





	
}


//http://www.youtube.com/subscription_center?add_user=santipingui